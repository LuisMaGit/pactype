package com.luisma.pactype.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luisma.pactype.services.RouterService
import com.luisma.pactype.services.Routes
import com.luisma.pactype.services.routerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouterViewModel(
    private val routerService: RouterService,
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            addInitializer(RouterViewModel::class) {
                RouterViewModel(
                    routerService = routerService()
                )
            }
        }
    }

    private val _routeState = MutableStateFlow<Routes>(Routes.Levels)
    val routeState = _routeState.asStateFlow()

    init {
        viewModelScope.launch {
            routerService.routeChannelFlow.collect { route ->
                if (route == _routeState.value) {
                    return@collect
                }

                _routeState.value = route
            }
        }
    }
}