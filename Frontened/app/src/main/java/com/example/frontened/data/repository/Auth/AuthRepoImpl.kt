package com.example.frontened.data.repository.Auth

import androidx.compose.material3.CircularProgressIndicator
import com.example.frontened.common.ResultState
import com.example.frontened.data.dto.LoginRequestData
import com.example.frontened.data.dto.RegisterRequestDto
import com.example.frontened.domain.di.AuthApi
import com.example.frontened.domain.repo.AuthRepository
import com.example.frontened.utils.TokenManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

class AuthRepoImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override fun registerUser(
        request: RegisterRequestDto
    ): Flow<ResultState<String>> = flow {

        emit(ResultState.Loading)

        try {
            val response = api.registerUser(request)

            if (response.success) {
                emit(ResultState.Success(response.message))
            } else {
                emit(ResultState.Error(response.message))
            }

        } catch (e: CancellationException) {
            throw e

        } catch (e: HttpException) {
            emit(ResultState.Error("Server Error: ${e.code()}"))

        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Something went wrong"))
        }
    }

    override fun loginUser(request: LoginRequestData): Flow<ResultState<String>> = flow {
         emit(ResultState.Loading)

        try {
            val response = api.loginUser(request)
            if(response.success && response.data != null){
                tokenManager.saveAccessToken(response.data.accessToken)
                emit(ResultState.Success("Login Successful"))
            }else{
                emit(ResultState.Error("Invalid credentials"))
            }
        }catch (e: CancellationException) {
            throw e
        }catch (e: HttpException) {
            emit(ResultState.Error("Server error: ${e.code()}"))
        }catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage?:"Something went wrong"))
        }
    }
}
