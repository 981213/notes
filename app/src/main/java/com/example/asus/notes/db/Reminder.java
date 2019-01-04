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
    private String content;
    private Date modDate;
    private Date remindDate;
    private Date eventDate;

    public Reminder() {
        this.modDate = new Date();
    }

    @Generated(hash = 1739681815)
    public Reminder(Long id, String title, String content, Date modDate,
            Date remindDate, Date eventDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modDate = modDate;
        this.remindDate = remindDate;
        this.eventDate = eventDate;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getModDate() {
        return this.modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    public Date getRemindDate() {
        return this.remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public Date getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}
