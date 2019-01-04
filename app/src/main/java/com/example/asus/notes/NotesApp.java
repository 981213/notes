package com.example.asus.notes;

import android.app.Application;

import com.example.asus.notes.db.DaoMaster;
import com.example.asus.notes.db.DaoSession;

import org.greenrobot.greendao.database.Database;

public class NotesApp extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
