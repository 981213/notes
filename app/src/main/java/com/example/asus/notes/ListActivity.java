package com.example.asus.notes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.Note;
import com.example.asus.notes.db.NoteDao;
import com.example.asus.notes.db.Reminder;
import com.example.asus.notes.db.ReminderDao;
import com.example.asus.notes.db.ReminderEntry;
import com.example.asus.notes.db.ReminderEntryDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ListActivity extends android.app.ListActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private Button addItemButton;
    private Button setTimeButton;
//    private String[] items = new String[20];
    //private ArrayList<String> items;
    private List<ReminderEntry> items;
    private MyAdapter myAdapter;

    private int mYear, mMonth, mDay, mHour, mMinute;
    //private String date_time;

    private ReminderDao reminderDao;
    private ReminderEntryDao reminderEntryDao;
    private long reminder_id;
    private Reminder reminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        titleEditText = (EditText) findViewById(R.id.list_title_input);
        contentEditText = (EditText) findViewById(R.id.list_content_input);
        items = new ArrayList<>();
        addItemButton = (Button) findViewById(R.id.add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentEditText.getText().toString();
                if (content != null){
                    Log.d("CONTENT", "SUCCESSFUL");
                    ReminderEntry reminderEntry = new ReminderEntry();
                    reminderEntry.setContent(content);
                    reminderEntry.setReminderId(reminder_id);
                    reminderEntryDao.insert(reminderEntry);
                    items = reminderEntryDao.queryBuilder().where(ReminderEntryDao.Properties.ReminderId.eq(reminder_id)).list();
                    //items.add(content);
                    myAdapter.notifyDataSetChanged();
                    contentEditText.setText("");
                }
            }
        });
        setTimeButton = (Button) findViewById(R.id.set_time);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay, mHour, mMinute);
                Date date = cal.getTime();
            }
        });

        Intent intent = getIntent();
        reminder_id = intent.getLongExtra(MainActivity.RECORD_ID, -1);
        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        reminderDao = daoSession.getReminderDao();
        reminder = reminderDao.queryBuilder().where(ReminderDao.Properties.Id.eq(reminder_id)).list().get(0);
        titleEditText.setText(reminder.getTitle());

        reminderEntryDao = daoSession.getReminderEntryDao();
        items = reminderEntryDao.queryBuilder().where(ReminderEntryDao.Properties.ReminderId.eq(reminder_id)).list();

        myAdapter = new MyAdapter();
        setListAdapter(myAdapter);
    }

    public void saveReminder(){
        reminder.setTitle(titleEditText.getText().toString());
        reminderDao.update(reminder);
    }

    @Override
    protected void onPause() {
        saveReminder();
        super.onPause();
    }

    private void datePicker(){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timePicker(){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        //date_time += (" " + hourOfDay + ":" + minute);
                        //titleEditText.setText(date_time);
                        //et_show_date_time.setText(date_time+" "+hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public ReminderEntry getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.get(i).hashCode();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup container) {
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            ((TextView) convertView.findViewById(R.id.text1))
                    .setText(getItem(i).getContent());
            return convertView;
        }
    }




    String getEditTextTitle(){
        String title = titleEditText.getText().toString();
        return title;
    }

    String getEditTextContent(){
        String content = contentEditText.getText().toString();
        return content;
    }
}
