package com.example.alert_worker.di

import com.example.alert_worker.BootReceiver

interface BootReceiverBackground{
    fun inject(bootReceiver: BootReceiver)
}