package com.example.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController


interface AppNavigator {
    fun openCoinDetails(navController: NavController, symbol:String)

    fun openAlerts(fragment: Fragment, symbol: String)
}