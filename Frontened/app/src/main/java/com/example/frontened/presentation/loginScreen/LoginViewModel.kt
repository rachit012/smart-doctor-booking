package com.example.frontened.presentation.loginScreen



import dagger.hilt.android.lifecycle.HiltViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.LoginRequestData
import com.example.frontened.domain.UseCase.RegisterUserUseCase

import com.example.frontened.domain.repo.AuthRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(request: LoginRequestData) {
        viewModelScope.launch {
            registerUserUseCase(request).collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _state.value = LoginState(loading = true)
                    }

                    is ResultState.Success -> {
                        _state.value = LoginState(message = result.data)
                    }

                    is ResultState.Error -> {
                        _state.value = LoginState(error = result.message)
                    }
                }
            }
        }
    }
}
