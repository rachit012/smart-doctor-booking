package com.example.frontened.presentation.DoctorDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.SlotDto
import com.example.frontened.domain.UseCase.AddAvailabilityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorDashboardViewModel @Inject constructor(
    private val addAvailabilityUseCase: AddAvailabilityUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow<ResultState<String>>(ResultState.Loading)

    val state: StateFlow<ResultState<String>> = _state

    fun addAvailability(date: String, slots: List<SlotDto>) {
        viewModelScope.launch {
            _state.value = ResultState.Loading
            _state.value = addAvailabilityUseCase(date, slots)
        }
    }
}
