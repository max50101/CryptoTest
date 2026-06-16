package com.example.alert_worker.di;

import com.example.alert_worker.impl.AlertSchedulerImpl;
import com.example.alert_worker.notifications.AlertNotificationSenderImpl;
import com.example.alerts.repository.AlertNotificationSender;
import com.example.alerts.repository.AlertsSchedulerRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AlertWorkerModule {
    @Binds
    abstract AlertsSchedulerRepository alertScheduler(AlertSchedulerImpl impl);

    @Binds
    abstract AlertNotificationSender alertNotificationSender(AlertNotificationSenderImpl impl);
}
