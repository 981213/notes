package com.example.asus.notes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.Note;
import com.example.asus.notes.db.NoteDao;
import com.example.asus.notes.db.Reminder;
import com.example.asus.notes.db.ReminderDao;

import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    public boolean isNote = true;
    private List<Note> noteset;
    private List<Reminder> reminderset;

    private NoteDao noteDao;
    private ReminderDao reminderDao;
    private Query<Note> noteQuery;
    private Query<Reminder> reminderQuery;
    private MainClickListener clickListener;
    private MainClickListener lclickListener;

    private static final String TAG = "MainAdapter";

    public interface MainClickListener {
        void onClick(int position);
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;

        public MainViewHolder(View itemView, final MainClickListener clickListener, final MainClickListener lclickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.ItemTitle);
            date = itemView.findViewById(R.id.ItemDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(clickListener != null)
                        clickListener.onClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(lclickListener != null)
                        lclickListener.onClick(position);
                    return true;
                }
            });

        }
    }

    public MainAdapter(DaoSession daoSession, MainClickListener clickListener, MainClickListener lclickListener) {
        super();
        this.clickListener = clickListener;
        this.lclickListener = lclickListener;
        noteDao = daoSession.getNoteDao();
        reminderDao = daoSession.getReminderDao();
        noteQuery = noteDao.queryBuilder().orderDesc(NoteDao.Properties.Date).build();
        reminderQuery = reminderDao.queryBuilder().orderDesc(ReminderDao.Properties.ModDate).build();
        LoadItems();

    }

    public void LoadItems() {
        if (isNote)
            noteset = noteQuery.list();
        else
            reminderset = reminderQuery.list();
    }

    public void UpdateItems() {
        LoadItems();
        notifyDataSetChanged();
    }

    public Note getNote(int i) {
        return noteset.get(i);
    }

    public Reminder getReminder(int i) {
        return reminderset.get(i);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recycler_item, viewGroup, false);
        return new MainViewHolder(view, clickListener, lclickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int i) {
        if (isNote) {
            Note cur = getNote(i);
            mainViewHolder.title.setText(cur.getTitle());
            mainViewHolder.date.setText(cur.getDate().toString());
        } else {
            Reminder cur = getReminder(i);
            mainViewHolder.title.setText(cur.getTitle());
            Date remindDate = cur.getRemindDate();
            if(remindDate != null)
                mainViewHolder.date.setText(remindDate.toString());
            else
                mainViewHolder.date.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return isNote ? noteset.size() : reminderset.size();
    }
}
