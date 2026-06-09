package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.DashboardData
import com.codequest.domain.usecase.ObserveDashboardUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val loading: Boolean = true,
    val data: DashboardData? = null
)

class DashboardViewModel(
    observeDashboardUseCase: ObserveDashboardUseCase
) : ViewModel() {
    val uiState: StateFlow<DashboardUiState> = observeDashboardUseCase()
        .map { DashboardUiState(loading = false, data = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )
}
