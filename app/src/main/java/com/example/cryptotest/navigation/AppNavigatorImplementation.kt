package com.example.cryptotest.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.coin_alert.AlertBottomSheet
import com.example.cryptotest.R
import com.example.cryptotest.navigation.AppNavigatorImplementation.Companion.navOptions
import com.example.navigation.AppNavigator
import javax.inject.Inject

class AppNavigatorImplementation @Inject constructor(): AppNavigator {
    override fun openCoinDetails(navController: NavController,symbol: String) {
        navController.navigate(
            R.id.coinDetailsFragment,
            Bundle().apply {
                putString("symbol",symbol)
            },
            navOptions
        )

    }

    override fun openAlerts(fragment: Fragment, symbol: String) {
        AlertBottomSheet.newInstance(symbol).show(fragment.childFragmentManager,"create_alert")
    }

    companion object{
        val navOptions = navOptions {
            anim {
                enter = R.anim.fragment_center_open
                exit = R.anim.fragment_fade_out
                popEnter = R.anim.fragment_fade_in
                popExit = R.anim.fragment_center_close
            }
        }
    }

}