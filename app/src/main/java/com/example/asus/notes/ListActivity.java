package com.example.asus.notes;

import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ListActivity extends android.app.ListActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private String title;
    private Button addItemButton;
    private Button setTimeButton;
//    private String[] items = new String[20];
    private ArrayList<String> items;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        titleEditText = (EditText) findViewById(R.id.list_title_input);
        contentEditText = (EditText) findViewById(R.id.list_content_input);
        title = titleEditText.getText().toString();
        items = new ArrayList<>();
        addItemButton = (Button) findViewById(R.id.add_item);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentEditText.getText().toString();
                if (content != null){
                    Log.d("CONTENT", "SUCCESSFUL");
                    items.add(content);
                    myAdapter.notifyDataSetChanged();
                    contentEditText.setText("");
                }
            }
        });
        setTimeButton = (Button) findViewById(R.id.set_time);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        myAdapter = new MyAdapter();
        setListAdapter(myAdapter);
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public String getItem(int i) {
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
                    .setText(getItem(i));
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
