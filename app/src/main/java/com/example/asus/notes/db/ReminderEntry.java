package com.example.asus.notes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class ReminderEntry {
    @Id(autoincrement = true)
    private Long id;
    private String content;
    private Long reminderId;
    private boolean checked;
    @ToOne(joinProperty = "reminderId")
    private Reminder reminder;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 192958847)
    private transient ReminderEntryDao myDao;
    @Generated(hash = 874614764)
    public ReminderEntry(Long id, String content, Long reminderId,
            boolean checked) {
        this.id = id;
        this.content = content;
        this.reminderId = reminderId;
        this.checked = checked;
    }
    @Generated(hash = 682307106)
    public ReminderEntry() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getReminderId() {
        return this.reminderId;
    }
    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }
    public boolean getChecked() {
        return this.checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    @Generated(hash = 251961730)
    private transient Long reminder__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2069036571)
    public Reminder getReminder() {
        Long __key = this.reminderId;
        if (reminder__resolvedKey == null || !reminder__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReminderDao targetDao = daoSession.getReminderDao();
            Reminder reminderNew = targetDao.load(__key);
            synchronized (this) {
                reminder = reminderNew;
                reminder__resolvedKey = __key;
            }
        }
        return reminder;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 668954891)
    public void setReminder(Reminder reminder) {
        synchronized (this) {
            this.reminder = reminder;
            reminderId = reminder == null ? null : reminder.getId();
            reminder__resolvedKey = reminderId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1522908623)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReminderEntryDao() : null;
    }
}
