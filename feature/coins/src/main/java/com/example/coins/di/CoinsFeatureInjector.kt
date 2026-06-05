package com.example.coins.di

import com.example.coins.CoinsFragment

interface CoinsFeatureInjector {
    fun inject(fragment: CoinsFragment)
}