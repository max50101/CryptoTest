package com.example.network.сonverters

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CollectionQueryConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation?>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if(type is ParameterizedType&&type.rawType==List::class.java){
            return Converter<List<*>,String>{list ->
                list.joinToString(
                    separator = ",",
                    prefix = "[",
                    postfix = "]"
                ) { "\"$it\"" }
            }
        }
        return null
    }
}