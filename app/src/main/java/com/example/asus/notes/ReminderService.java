package com.example.asus.notes;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.ReminderDao;

public class ReminderService extends IntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        ReminderDao reminderDao = daoSession.getReminderDao();
        
    }
}
