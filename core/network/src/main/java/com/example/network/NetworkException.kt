package com.example.network

class NetworkException(val error: NetworkError): Exception(error.message)