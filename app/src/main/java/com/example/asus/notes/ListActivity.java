package com.example.asus.notes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ListActivity extends android.app.ListActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private String[] items;
    private static int contentCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        titleEditText = (EditText) findViewById(R.id.list_title_input);
        contentEditText = (EditText) findViewById(R.id.list_content_input);
        contentCnt = 0; //当有数据存储时，等于上一次保留的count
        setListAdapter(new MyAdapter());
    }

    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager.isActive()){
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }

                String content = getEditTextContent();
                if (content != null){
                    items[contentCnt++] = content;
                }

                return true;
            }
            return false;
        }
    };

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public String getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return items[i].hashCode();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup container) {
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
            }

            ((TextView) convertView.findViewById(android.R.id.text1))
                    .setText(getItem(i));
            return convertView;
        }
    }





    String getEditTextTitle(){
        String title = titleText.getText().toString();
        return title;
    }

    String getEditTextContent(){
        String content = contentText.getText().toString();
        return content;
    }
}
