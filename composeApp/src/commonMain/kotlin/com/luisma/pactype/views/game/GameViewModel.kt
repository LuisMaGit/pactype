package com.luisma.pactype.views.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisma.pactype.services.RouterService
import com.luisma.pactype.services.Routes
import com.luisma.pactype.services.db_services.SettingsDBService
import com.luisma.pactype.services.db_services.UserProgressDBService
import com.luisma.pactype.services.db_services.db_models.UserProgressDBModel
import com.luisma.pactype.services.routerService
import com.luisma.pactype.services.settingsDBService
import com.luisma.pactype.services.userProgressDBService
import com.luisma.pactype.ui.theme.LevelTheme
import com.luisma.pactype.views.game.data.EnemyType
import com.luisma.pactype.views.game.data.GameContractsByMapType
import com.luisma.pactype.views.game.data.LOST_SHAKE_ANIMATION_DURATION_MS
import com.luisma.pactype.views.game.data.PKey
import com.luisma.pactype.views.game.data.PKeyType
import com.luisma.pactype.views.game.data.PLAYER_START_COORDINATE
import com.luisma.pactype.views.game.data.gameContractsByMapType
import com.luisma.pactype.views.game.services.EnemiesData
import com.luisma.pactype.views.game.services.GameKeyboardChannelService
import com.luisma.pactype.views.game.services.GameLevelService
import com.luisma.pactype.views.game.services.LinearMoveService
import com.luisma.pactype.views.game.services.ROBOT_MOVE_DELAY_MS
import com.luisma.pactype.views.game.services.RobotPlayerService
import com.luisma.pactype.views.game.services.enemies_services.MoveEnemiesService
import com.luisma.pactype.views.game.services.gameKeyboardChannelService
import com.luisma.pactype.views.game.services.gameLevelService
import com.luisma.pactype.views.game.services.linearMoveService
import com.luisma.pactype.views.game.services.moveEnemiesService
import com.luisma.pactype.views.game.services.robotPlayerService
import com.luisma.pactype.views.game.states.GameKeyboardState
import com.luisma.pactype.views.game.states.GameMapState
import com.luisma.pactype.views.game.states.GamePlayState
import com.luisma.pactype.views.game.states.GameProgressState
import com.luisma.pactype.views.game.states.GameStatus
import com.luisma.pactype.views.game.states.GameStatusState
import com.luisma.pactype.views.game.states.GameTimeState
import com.luisma.pactype.views.game.states.PlayerState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class GameViewModel(
    private val gameLevelService: GameLevelService,
    private val keyboardChannelService: GameKeyboardChannelService,
    private val linearMoveService: LinearMoveService,
    private val moveEnemiesService: MoveEnemiesService,
    private val userProgressDBService: UserProgressDBService,
    private val settingsDBService: SettingsDBService,
    private val robotPlayerService: RobotPlayerService,
    private val routerService: RouterService
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            addInitializer(GameViewModel::class) {
                GameViewModel(
                    gameLevelService = gameLevelService(),
                    keyboardChannelService = gameKeyboardChannelService(),
                    linearMoveService = linearMoveService(),
                    moveEnemiesService = moveEnemiesService(),
                    userProgressDBService = userProgressDBService(),
                    settingsDBService = settingsDBService(),
                    robotPlayerService = robotPlayerService(),
                    routerService = routerService()
                )
            }
        }
    }

    // control
    private var contractsFromMap = GameContractsByMapType.initial()
    private var updatedLastPlayedLevel = false

    private var enemyHitJob: Job? = null
    private var playerProgressJob: Job? = null
    private var moveEnemyJob: Job? = null
    private var counterJob: Job? = null
    private var saveUserStatsJob: Job? = null
    private var lostAnimationJob: Job? = null
    private var listenKeyboardJob: Job? = null
    private var robotPlayerJob: Job? = null

    // states
    private val _mapState = MutableStateFlow(GameMapState.initial())
    val mapState = _mapState.asStateFlow()

    private val _playState = MutableStateFlow(GamePlayState.initial())
    val playState = _playState.asStateFlow()

    private val _statusState = MutableStateFlow(GameStatusState.initial())
    val statusState = _statusState.asStateFlow()

    private val _timeState = MutableStateFlow(GameTimeState.initial())
    val timeState = _timeState.asStateFlow()

    private val _progressState = MutableStateFlow(GameProgressState.initial())
    val progressState = _progressState.asStateFlow()

    private val _keyboardState = MutableStateFlow(GameKeyboardState.initial())
    val keyboardState = _keyboardState.asStateFlow()

    private val _themeState = MutableStateFlow(LevelTheme.initial())
    val themeState = _themeState.asStateFlow()

    fun sendEvent(event: GameEvents) {
        when (event) {
            is GameEvents.OnCreate -> onCreate(
                levelId = event.levelId,
                practiceMode = event.practiceMode
            )

            is GameEvents.OnPause -> onPause(pause = event.setPause)
            is GameEvents.KeyboardTap -> keyboardTap(key = event.key)
            GameEvents.GoBack -> goBack()
            GameEvents.OnReset -> onReset()
        }
    }

    // events
    private fun goBack() {
        resetStateAndJobs()
        viewModelScope.launch {
            routerService.goTo(Routes.Levels)
        }
    }

    private fun onCreate(levelId: Int, practiceMode: Boolean) {
        if ((levelId == _mapState.value.levelId) || levelId == -1) {
            return
        }

        startLevel(levelId = levelId, practiceMode = practiceMode)
    }

    private fun onPause(pause: Boolean) {
        if (pause && _statusState.value.status == GameStatus.Play) {
            _statusState.update {
                it.copy(status = GameStatus.Pause)
            }
        } else if (!pause && _statusState.value.status == GameStatus.Pause) {
            _statusState.update {
                it.copy(status = GameStatus.Play)
            }
            runGameLoops()
        }
    }

    private fun onReset() {
        val levelId = _mapState.value.levelId
        val practiceMode = _statusState.value.practiceMode
        resetStateAndJobs()
        startLevel(levelId = levelId, practiceMode = practiceMode)
    }

    private fun keyboardTap(key: PKey) {
        if (key.type == PKeyType.Shift) {
            _keyboardState.update {
                it.copy(
                    capital = !_keyboardState.value.capital
                )
            }
            return
        }
        val char = if (key.type == PKeyType.Letter) {
            if (_keyboardState.value.capital) key.capital else key.lower
        } else {
            key.lower
        }
        handleKeyboardChar(char)
    }

    // control
    private fun startLevel(
        levelId: Int,
        practiceMode: Boolean,
        onLostProgress: Int = 0
    ) {
        val gameMode = !practiceMode
        listenToKeyboard()
        viewModelScope.launch {
            val levelDataDeferred = async { gameLevelService.getGameData(levelId = levelId) }
            val settingsDeferred = async { settingsDBService.getSettings() }
            val enemies = if (gameMode) {
                val enemiesDataDeferred = async {
                    gameLevelService.getEnemiesDataInLevel(levelId = levelId)
                }
                enemiesDataDeferred.await()
            } else {
                null
            }

            val levelData = levelDataDeferred.await()
            val settings = settingsDeferred.await()

            _keyboardState.update {
                it.copy(
                    showKeyboard = settings.showKeyboard
                )
            }

            contractsFromMap = gameContractsByMapType(
                mapType = levelData.mapType
            )

            _themeState.value = levelData.levelTheme

            _mapState.update {
                it.copy(
                    levelId = levelData.levelId,
                    mapType = levelData.mapType,
                    totalChars = levelData.charCount,
                    map = levelData.map,
                    levelName = levelData.name
                )
            }

            _playState.update {
                it.copy(
                    player = PlayerState(
                        coordinates = PLAYER_START_COORDINATE,
                        prevCoordinate = PLAYER_START_COORDINATE
                    )
                )
            }

            // show from previous game play lost if the user achieved a new best.
            if (onLostProgress != 0) {
                _progressState.update {
                    it.copy(onLostBestProgress = onLostProgress)
                }
            }

            if (gameMode) {
                initEnemies(enemies = enemies!!)
            }

            _statusState.update {
                it.copy(
                    practiceMode = practiceMode,
                    status = GameStatus.ReadyToPlay
                )
            }
        }
    }

    private fun listenToKeyboard() {
        listenKeyboardJob = viewModelScope.launch {
            keyboardChannelService.keyChannelFlow.collect { char ->
                handleKeyboardChar(char)
            }
        }
    }

    private fun handleKeyboardChar(char: Char?) {
        if (char == null || !_statusState.value.acceptKeyboardEvents) {
            return
        }

        if (_statusState.value.status == GameStatus.ReadyToPlay) {
            _statusState.update {
                it.copy(status = GameStatus.Play)
            }
            if (_progressState.value.onLostBestProgress != 0) {
                _progressState.update {
                    it.copy(onLostBestProgress = 0)
                }
            }
            if (!updatedLastPlayedLevel) {
                viewModelScope.launch {
                    settingsDBService.updateLastPlayedLevel(
                        levelId = _mapState.value.levelId
                    )
                    updatedLastPlayedLevel = true
                }
            }
            runGameLoops()
        }


        val newPlayer = linearMoveService.movePlayerToChar(
            player = _playState.value.player,
            map = _mapState.value.map,
            char = char,
        )
        if (newPlayer == null) {
            return
        }
        _playState.update {
            it.copy(player = newPlayer)
        }
    }

    private fun initEnemies(enemies: List<EnemiesData>) {
        val startResponse = moveEnemiesService.startEnemiesPosition(
            contractsFromMap = contractsFromMap,
            enemies = enemies
        )
        _playState.update {
            it.copy(
                enemies = startResponse.toImmutableList()
            )
        }
    }

    private fun checkEnemyHit() {
        enemyHitJob = viewModelScope.launch {
            while (true) {
                if (_statusState.value.status != GameStatus.Play) {
                    return@launch
                }
                _playState.value.enemies.forEach { enemy ->
                    if (enemy.coordinates == _playState.value.player.coordinates) {
                        lostAnimationJob = viewModelScope.launch {
                            // set lost state
                            _statusState.update { state ->
                                state.copy(
                                    status = GameStatus.Lost
                                )
                            }
                            // shake animation
                            repeat(LOST_SHAKE_ANIMATION_DURATION_MS) { value ->
                                val factor = if (value == LOST_SHAKE_ANIMATION_DURATION_MS) {
                                    0
                                } else {
                                    value
                                }
                                _statusState.update { state ->
                                    state.copy(
                                        lostShakeAnimationFactor = factor,
                                    )
                                }
                                delay(1)
                            }
                            // save user stats and wait to finish to reset
                            onFinishSaveUserStats()
                            saveUserStatsJob?.join()
                            val levelId = _mapState.value.levelId
                            val onLostProgress = _progressState.value.onLostBestProgress
                            resetStateAndJobs()
                            startLevel(
                                levelId = levelId,
                                practiceMode = _statusState.value.practiceMode,
                                onLostProgress = onLostProgress,
                            )
                        }
                    }
                    delay(1)
                }
            }
        }
    }

    private fun checkPlayerProgress() {
        playerProgressJob = viewModelScope.launch {
            while (true) {
                if (_statusState.value.status != GameStatus.Play) {
                    return@launch
                }
                val map = _mapState.value.map.toMutableMap()
                val coordinate = _playState.value.player.coordinates
                // set current as done but not enabled
                if (!map[coordinate]!!.letterDone) {
                    map[coordinate] = map[coordinate]!!.copy(
                        letterDone = true
                    )
                    _mapState.update {
                        it.copy(map = map.toImmutableMap())
                    }
                    _progressState.update {
                        val newCount = _progressState.value.charCounter + 1
                        it.copy(
                            charCounter = newCount,
                            progress = (newCount * 100) / _mapState.value.totalChars
                        )
                    }
                    if (_progressState.value.charCounter == _mapState.value.totalChars) {
                        _statusState.update {
                            it.copy(
                                status = GameStatus.Win
                            )
                        }
                        onFinishSaveUserStats()
                    }
                }
                // check if previous was done to enable
                val prevCoordinate = _playState.value.player.prevCoordinate
                if (map[prevCoordinate]!!.letterDone) {
                    map[prevCoordinate] = map[prevCoordinate]!!.copy(
                        enableLetter = true
                    )
                    _mapState.update {
                        it.copy(map = map.toImmutableMap())
                    }
                }
                delay(1)
            }
        }
    }

    private fun moveEnemies() {
        _playState.value.enemies.map { it.enemyType }.forEach { enemyType ->
            moveEnemy(enemyType)
        }
    }

    private fun moveEnemy(enemyType: EnemyType) {
        moveEnemyJob = viewModelScope.launch {
            while (true) {
                if (_statusState.value.status != GameStatus.Play) {
                    return@launch
                }
                val enemies = _playState.value.enemies
                val enemyIdx = enemies.indexOfFirst { it.enemyType == enemyType }
                val enemy = enemies[enemyIdx]
                // just when a enemy (except the last) is on [EnemyMode.Chasing] will trigger the next enemy
                if (enemy.startNextEnemyTrigger &&
                    enemyIdx != enemies.count() - 1 &&
                    enemies[enemyIdx + 1].startMovingTrigger &&
                    _timeState.value.playingTimeCS >= enemies[enemyIdx + 1].startMoveDelaySec * 100
                ) {
                    val enemiesUpdated = _playState.value.enemies.toMutableList()
                    enemiesUpdated[enemyIdx + 1] =
                        enemies[enemyIdx + 1].copy(startTrigger = true)
                    _playState.update { state ->
                        state.copy(enemies = enemiesUpdated.toImmutableList())
                    }
                }
                if (enemy.startTrigger) {
                    val newEnemy = moveEnemiesService.moveEnemy(
                        contractsFromMap = contractsFromMap,
                        enemy = enemy,
                        map = _mapState.value.map,
                        playerCoordinate = _playState.value.player.coordinates,
                        playerPreviousCoordinate = _playState.value.player.prevCoordinate
                    )
                    val enemiesUpdated = _playState.value.enemies.toMutableList()
                    enemiesUpdated[enemyIdx] = newEnemy
                    _playState.update { state ->
                        state.copy(
                            enemies = enemiesUpdated.toImmutableList(),
                        )
                    }
                }
                delay(enemy.moveDelayMS.toLong())
            }
        }
    }

    private fun startPlayTimeCounter() {
        counterJob = viewModelScope.launch {
            while (true) {
                if (_statusState.value.status != GameStatus.Play) {
                    return@launch
                }
                delay(10)
                _timeState.update {
                    it.copy(playingTimeCS = _timeState.value.playingTimeCS + 1)
                }
            }
        }
    }

    private fun onFinishSaveUserStats() {
        saveUserStatsJob = viewModelScope.launch {
            val dbProgress = userProgressDBService.getProgress(
                levelId = _mapState.value.levelId
            )

            val existsData = dbProgress.levelId != -1
            if (_statusState.value.practiceMode) {
                if (!existsData) {
                    userProgressDBService.insertProgress(
                        UserProgressDBModel.empty().copy(
                            levelId = _mapState.value.levelId,
                            practiceModeBestTimeCS = _timeState.value.playingTimeCS
                        )
                    )
                    _timeState.update {
                        it.copy(
                            onWinBestTimeCS = _timeState.value.playingTimeCS
                        )
                    }
                } else if (existsData &&
                    (dbProgress.practiceModeBestTimeCS == 0 ||
                            _timeState.value.playingTimeCS < dbProgress.practiceModeBestTimeCS)
                ) {
                    userProgressDBService.updateProgress(
                        dbProgress.copy(
                            practiceModeBestTimeCS = _timeState.value.playingTimeCS
                        )
                    )
                    _timeState.update {
                        it.copy(
                            onWinBestTimeCS = _timeState.value.playingTimeCS
                        )
                    }
                }
                return@launch
            }

            val lostGame = _statusState.value.status == GameStatus.Lost
            val winedGame = _statusState.value.status == GameStatus.Win

            if (lostGame) {
                if (!existsData) {
                    userProgressDBService.insertProgress(
                        UserProgressDBModel.empty().copy(
                            levelId = _mapState.value.levelId,
                            playModeProgress = _progressState.value.progress
                        )
                    )
                    _progressState.update {
                        it.copy(
                            onLostBestProgress = _progressState.value.progress
                        )
                    }
                } else if (existsData &&
                    _progressState.value.progress > dbProgress.playModeProgress
                ) {
                    userProgressDBService.updateProgress(
                        dbProgress.copy(
                            playModeProgress = _progressState.value.progress
                        )
                    )
                    _progressState.update {
                        it.copy(
                            onLostBestProgress = _progressState.value.progress
                        )
                    }
                }
            } else if (winedGame) {
                if (!existsData) {
                    userProgressDBService.insertProgress(
                        UserProgressDBModel.empty().copy(
                            levelId = _mapState.value.levelId,
                            playModeProgress = _progressState.value.progress,
                            playModeBestTimeCS = _timeState.value.playingTimeCS
                        )
                    )
                    _timeState.update {
                        it.copy(
                            onWinBestTimeCS = _timeState.value.playingTimeCS
                        )
                    }
                } else if (existsData &&
                    (dbProgress.playModeBestTimeCS == 0 || _timeState.value.playingTimeCS < dbProgress.playModeBestTimeCS)
                ) {
                    userProgressDBService.updateProgress(
                        dbProgress.copy(
                            playModeProgress = _progressState.value.progress,
                            playModeBestTimeCS = _timeState.value.playingTimeCS,
                        )
                    )
                    _timeState.update {
                        it.copy(
                            onWinBestTimeCS = _timeState.value.playingTimeCS
                        )
                    }
                }
            }
        }
    }

    private fun resetStateAndJobs() {
        contractsFromMap = GameContractsByMapType.initial()
        updatedLastPlayedLevel = false
        _mapState.value = GameMapState.initial()
        _playState.value = GamePlayState.initial()
        _statusState.value = GameStatusState.initial()
        _timeState.value = GameTimeState.initial()
        _progressState.value = GameProgressState.initial()
        enemyHitJob?.cancel()
        playerProgressJob?.cancel()
        moveEnemyJob?.cancel()
        counterJob?.cancel()
        saveUserStatsJob?.cancel()
        lostAnimationJob?.cancel()
        listenKeyboardJob?.cancel()
        robotPlayerJob?.cancel()
    }

    private fun playWithRobot() {
        robotPlayerJob = viewModelScope.launch {
            while (true) {
                if (_statusState.value.status != GameStatus.Play) {
                    return@launch
                }

                val robotResp = robotPlayerService.movePlayer(
                    map = _mapState.value.map,
                    player = _playState.value.player,
                    enemies = _playState.value.enemies
                )
                if (robotResp != null) {
                    _playState.update {
                        it.copy(
                            player = PlayerState(
                                coordinates = robotResp.coordinates,
                                prevCoordinate = robotResp.previousCoordinates
                            )
                        )
                    }
                }

                delay(ROBOT_MOVE_DELAY_MS)
            }
        }
    }

    private fun runGameLoops() {
        if (_playState.value.playWithRobot) {
            playWithRobot()
        }
        if (_statusState.value.practiceMode) {
            checkPlayerProgress()
            startPlayTimeCounter()
            return
        }
        // default mode
        moveEnemies()
        checkEnemyHit()
        checkPlayerProgress()
        startPlayTimeCounter()
    }

}