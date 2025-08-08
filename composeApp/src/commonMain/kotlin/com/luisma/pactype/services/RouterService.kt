package com.luisma.pactype.services

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object RouterService {
    private val routeChannel = Channel<Routes>()
    val routeChannelFlow = routeChannel.receiveAsFlow()


    var currentRoute: Routes = Routes.Levels
        private set

    suspend fun goTo(route: Routes) {
        if (route == currentRoute) {
            return
        }
        currentRoute = route
        routeChannel.send(route)
    }
}

sealed class Routes {
    data object Levels : Routes()
    data class Game(val levelId: Int, val practiceMode: Boolean) : Routes()
}
