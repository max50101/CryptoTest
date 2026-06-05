package com.example.network

import android.net.http.NetworkException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun  <T> safeApiCall(call: suspend ()-> T): T {
    return try {
        call()
    }catch (e: UnknownHostException){
        throw NetworkException(NetworkError.NoInternet)
    }catch (e: SocketTimeoutException) {
        throw NetworkException(NetworkError.Timeout)
    } catch (e: retrofit2.HttpException) {
        val error = when (e.code()) {
            401 -> NetworkError.Unauthorized
            404 -> NetworkError.NotFound
            in 500..599 -> NetworkError.ServerError
            else -> NetworkError.Unknown(e.message())
        }

        throw com.example.network.NetworkException(error)
    } catch (e: java.io.IOException) {
        throw NetworkException(NetworkError.NoInternet)
    }
    catch (e: Throwable){
        throw NetworkException(NetworkError.Unknown(e.message))
        }
    }
