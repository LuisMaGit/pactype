package com.luisma.pactype.views.levels

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.pactype.ui.components.APPBAR_H
import com.luisma.pactype.ui.theme.levelThemeConverter
import com.luisma.pactype.ui.utils.CommonMeasurements
import com.luisma.pactype.ui.utils.ScreenSizeUtil
import com.luisma.pactype.views.levels.components.LEVEL_ARROW_H
import com.luisma.pactype.views.levels.components.LEVEL_CARD_H
import com.luisma.pactype.views.levels.components.LEVEL_CARD_SPACE_BETWEEN
import com.luisma.pactype.views.levels.components.LEVEL_CARD_W
import com.luisma.pactype.views.levels.components.LEVEL_CARET_W
import com.luisma.pactype.views.levels.components.LEVEL_NAME_H
import com.luisma.pactype.views.levels.components.LEVEL_NUMB_INDICATOR_H
import com.luisma.pactype.views.levels.components.LevelAppbar
import com.luisma.pactype.views.levels.components.LevelArrows
import com.luisma.pactype.views.levels.components.LevelBottomBanner
import com.luisma.pactype.views.levels.components.LevelKeysLegend
import com.luisma.pactype.views.levels.components.LevelNumbIndicator
import com.luisma.pactype.views.levels.components.LevelScrollableContent
import com.luisma.pactype.views.levels.components.Logo
import com.luisma.pactype.views.levels.states.LevelsLogoState
import com.luisma.pactype.views.levels.states.LevelsState
import com.luisma.pactype.views.levels.states.LevelsStatusState
import kotlinx.coroutines.launch
import kotlin.math.sign


@Composable
fun Levels(
    modifier: Modifier = Modifier,
    levelsState: LevelsState,
    logoState: LevelsLogoState,
    statusState: LevelsStatusState,
    sendEvent: (event: LevelsEvents) -> Unit
) {
    val currentPage = levelsState.currentOnScreen - 1
    val currentLevel = levelsState.levels[currentPage]
    val levelTheme = levelThemeConverter(currentLevel.levelTheme)

    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = {
            levelsState.total
        }
    )
    val pagerCoroutineScroll = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        if (statusState.levelInit) {
            return@LaunchedEffect
        }

        sendEvent(
            LevelsEvents.OnCreateLevel(
                goToPageCallback = { page ->
                    pagerCoroutineScroll.launch {
                        pagerState.animateScrollToPage(page = page)
                    }
                }
            )
        )
    }


    ScreenSizeUtil { breakPoints, _, screenH ->
        val measurements = CommonMeasurements.fromScreenSize(breakPoints)
        val scrollableContentH = (LEVEL_CARD_H * 2) + (LEVEL_CARD_SPACE_BETWEEN * 2) + LEVEL_NAME_H
        val scrollableContentVerticalPadding = (screenH - scrollableContentH) / 2
        val trueScrollableContentVerticalPadding =
            if (scrollableContentVerticalPadding.value.sign == -1f) {
                0.dp
            } else {
                scrollableContentVerticalPadding
            }
        val heightUntilNumbIndicator = trueScrollableContentVerticalPadding + scrollableContentH
        val allContentFit = screenH >= 580.dp
        Box(
            modifier = modifier
                .background(color = levelTheme.background)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier.width(measurements.gameW.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                if (allContentFit)
                    LevelAppbar(
                        levelTheme = levelTheme,
                        onTapSettings = { sendEvent(LevelsEvents.ToggleSettingsDialog) }
                    )

                if (allContentFit)
                    Logo(
                        modifier = Modifier
                            .padding(top = APPBAR_H)
                            .width((logoState.charWidth * (logoState.chars.size + 1) + LEVEL_CARET_W + 2).dp),
                        levelTheme = levelTheme,
                        logoState = logoState
                    )

                HorizontalPager(
                    modifier = Modifier
                        .padding(vertical = trueScrollableContentVerticalPadding)
                        .width(LEVEL_CARD_W + 32.dp),
                    state = pagerState,
                ) {
                    val level = levelsState.levels[pagerState.currentPage]
                    LevelScrollableContent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        levelTheme = levelTheme,
                        level = level,
                        goToLevel = { levelId, practiceMode ->
                            sendEvent(
                                LevelsEvents.GoToGame(
                                    levelId = levelId,
                                    practiceMode = practiceMode
                                )
                            )
                        },
                        onScreen = { id ->
                            sendEvent(
                                LevelsEvents.SetLevelOnScreen(id)
                            )
                        }
                    )
                }
            }

            LevelArrows(
                modifier = Modifier
                    .padding(
                        top = trueScrollableContentVerticalPadding + LEVEL_CARD_H +
                                LEVEL_CARD_SPACE_BETWEEN / 2 - LEVEL_ARROW_H / 2
                    )
                    .width(measurements.gameW.dp),
                levelTheme = levelTheme,
                showPrevious = currentLevel.levelId != 1,
                showNext = currentLevel.levelId != levelsState.total,
                onPrevious = {
                    sendEvent(
                        LevelsEvents.GoToPage(page = currentLevel.levelId - 2)
                    )
                },
                onNext = {
                    sendEvent(
                        LevelsEvents.GoToPage(page = currentLevel.levelId)
                    )
                }
            )

            if (allContentFit)
                LevelNumbIndicator(
                    modifier = Modifier.padding(
                        top = heightUntilNumbIndicator
                    ),
                    level = currentLevel.levelId,
                    total = levelsState.total,
                    levelTheme = levelTheme
                )
            if (allContentFit)
                LevelKeysLegend(
                    modifier = Modifier.padding(
                        top = heightUntilNumbIndicator + LEVEL_NUMB_INDICATOR_H
                    ),
                    levelTheme = levelTheme
                )


            if (allContentFit && statusState.showLastPlayedBanner)
                LevelBottomBanner(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomEnd)
                        .clickable {
                            sendEvent(LevelsEvents.GoToLastPlayed)
                        },
                    levelTheme = levelTheme
                )
        }

    }
}
