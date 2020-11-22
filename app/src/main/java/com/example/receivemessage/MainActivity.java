package com.example.receivemessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private NotificationManager notificationManager;
    private final String CHANNEL_ID = "CHANNEL_ID";
    private final String GROUP_KEY = "GROUP_KEY";
    private int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String message;
        super.onCreate(savedInstanceState);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        try {
            message = intent.getStringExtra("Message");
        } catch (Exception exc) {
            message = "";
        }
        textView.setText(message);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("kz.astana.MESSAGE");
                Log.d("Hello", s);
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                textView.setText(s);
                createNotification(s);
            }
        };
        IntentFilter intentFilter = new IntentFilter("kz.astana.ACTION");
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private void createNotification(String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("Message", message);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_large))
                .setAutoCancel(true)
                .setContentTitle("Получено сообщение")
                .setContentText("Текст = " + message)
                .setContentInfo("Text")
                .setColor(Color.BLACK)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentIntent(resultPendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(NOTIFY_ID, notification);
        NOTIFY_ID++;
        
    }

}