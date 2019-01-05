package com.example.asus.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.Note;
import com.example.asus.notes.db.NoteDao;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText contentText;
    private NoteDao noteDao;
    private long note_id;
    private Note note;
    private boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        titleText = (EditText) findViewById(R.id.note_title_input);
        contentText = (EditText) findViewById(R.id.note_content_input);
        Intent intent = getIntent();
        note_id = intent.getLongExtra(MainActivity.RECORD_ID, -1);
        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();
        note = noteDao.queryBuilder().where(NoteDao.Properties.Id.eq(note_id)).list().get(0);
        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());
    }

    public void saveNote(){
        note.setTitle(titleText.getText().toString());
        note.setContent(contentText.getText().toString());
        note.setDate(new Date());
        noteDao.update(note);
        finish();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save){
            saveNote();
            Toast.makeText(this, "备忘录已保存", Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
