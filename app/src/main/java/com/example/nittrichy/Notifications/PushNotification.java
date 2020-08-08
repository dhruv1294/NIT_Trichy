package com.example.nittrichy.Notifications;

public class PushNotification {
    public NotificationData notificationData;
    public String to;

    public PushNotification(NotificationData notificationData, String to) {
        this.notificationData = notificationData;
        this.to = to;
    }
}
