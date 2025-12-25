package com.example.frontened.presentation.SignupScreen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.RegisterRequestDto
import com.example.frontened.domain.UseCase.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel() {

    private val _state = MutableStateFlow(SingUpState())
    val state: StateFlow<SingUpState> = _state

    fun registerUser(request: RegisterRequestDto) {
        viewModelScope.launch {
            registerUserUseCase(request).collect { result->
                when(result) {

                    is ResultState.Loading -> {
                        _state.value = SingUpState(loading = true)
                    }

                    is ResultState.Success -> {
                        _state.value = SingUpState(
                            loading = false,
                            message = result.data
                        )
                    }

                    is ResultState.Error -> {
                        _state.value = SingUpState(
                            loading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
}