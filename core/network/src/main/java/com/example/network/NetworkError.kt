package com.example.network

sealed class NetworkError(val message:String) {

    data object NoInternet : NetworkError(
        "Нет интернета или не удалось найти сервер"
    )

    data object Timeout : NetworkError(
        "Сервер слишком долго не отвечает"
    )

    data object Unauthorized : NetworkError(
        "Ошибка авторизации"
    )

    data object NotFound : NetworkError(
        "Данные не найдены"
    )

    data object ServerError : NetworkError(
        "Ошибка сервера"
    )

    data class Unknown(
        val originalMessage: String?
    ) : NetworkError(
        originalMessage ?: "Неизвестная ошибка"
    )
}