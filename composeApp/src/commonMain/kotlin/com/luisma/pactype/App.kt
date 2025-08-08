package com.luisma.pactype

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luisma.pactype.router.RouterViewModel
import com.luisma.pactype.services.Routes
import com.luisma.pactype.views.game.GameBuilder
import com.luisma.pactype.views.levels.LevelsBuilder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val routerViewModel = viewModel<RouterViewModel>(factory = RouterViewModel.Factory)
    val routerState by routerViewModel.routeState.collectAsState()
    MaterialTheme {
        when (val route = routerState) {
            is Routes.Game -> {
                GameBuilder(
                    levelId = route.levelId,
                    practiceMode = route.practiceMode
                )
            }

            Routes.Levels -> LevelsBuilder()
        }
    }
}