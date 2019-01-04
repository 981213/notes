package com.example.asus.notes.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Reminder {
    @Id(autoincrement = true)
    private Long id;

    private String title;
    //private String content;
    //private Date modDate;
    private Date remindDate;
    //private Date eventDate;
    @Generated(hash = 1525862740)
    public Reminder(Long id, String title, Date remindDate) {
        this.id = id;
        this.title = title;
        this.remindDate = remindDate;
    }
    @Generated(hash = 4427342)
    public Reminder() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Date getRemindDate() {
        return this.remindDate;
    }
    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }
}
