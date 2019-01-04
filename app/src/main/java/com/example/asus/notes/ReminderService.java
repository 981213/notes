package com.example.asus.notes;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.JobIntentService;
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

    @Override
    protected void onHandleWork(Intent intent) {
        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        ReminderDao reminderDao = daoSession.getReminderDao();
        Log.i(TAG, "onHandleIntent: now in");
        while (true) {
            Date cur = new Date();
            Date nxt = new Date(cur.getTime() + 60 * 1000);
            QueryBuilder<Reminder> reminderQueryBuilder = reminderDao.queryBuilder();
            reminderQueryBuilder.where(ReminderDao.Properties.RemindDate.ge(cur), ReminderDao.Properties.RemindDate.le(nxt));
            List<Reminder> reminders = reminderQueryBuilder.list();
            for (Reminder creminder : reminders) {
                Log.i(TAG, "onHandleIntent: Trigger reminder: " + creminder.getTitle());
            }
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "onHandleIntent: triggered once");
        }
    }
}
