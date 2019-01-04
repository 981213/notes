package com.example.asus.notes;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.Reminder;
import com.example.asus.notes.db.ReminderDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

public class ReminderService extends JobIntentService {

    private static final String TAG = "ReminderService";
    private static final String CHANNEL_ID = "com.example.asus.notes.NCHANNEL";
    private static boolean started = false;

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onHandleWork(Intent intent) {
        if (started)
            return;
        started = true;
        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        ReminderDao reminderDao = daoSession.getReminderDao();
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        while (true) {
            Date cur = new Date();
            Date nxt = new Date(cur.getTime() + 60 * 1000);
            QueryBuilder<Reminder> reminderQueryBuilder = reminderDao.queryBuilder();
            reminderQueryBuilder.where(ReminderDao.Properties.RemindDate.ge(cur), ReminderDao.Properties.RemindDate.le(nxt));
            List<Reminder> reminders = reminderQueryBuilder.list();
            for (Reminder creminder : reminders) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("Reminder");
                mBuilder.setContentText(creminder.getTitle());
                mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, mBuilder.build());
            }
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
