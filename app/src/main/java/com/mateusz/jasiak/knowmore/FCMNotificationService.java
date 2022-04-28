package com.mateusz.jasiak.knowmore;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class FCMNotificationService extends FirebaseMessagingService {

    //TODO: Zoptymalizować: emulator -> brak powiadomień, aplikacja nie działa w tle -> brak powiadomień i nie dodaje użytkownika do recycler view,
    // aplikacja w tle -> dodaje do recyclerView ale powiadomienie jest z cyframi - powinno być bez.
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getFirebaseMessage(Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
    }

    public void getFirebaseMessage(String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myFirebaseChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(101, builder.build());
    }
}
