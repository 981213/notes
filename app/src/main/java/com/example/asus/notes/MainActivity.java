package com.example.asus.notes;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.asus.notes.db.DaoSession;
import com.example.asus.notes.db.Note;
import com.example.asus.notes.db.NoteDao;
import com.example.asus.notes.db.Reminder;
import com.example.asus.notes.db.ReminderDao;
import com.example.asus.notes.db.ReminderEntryDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;
    private FloatingActionButton fab01Add;
    private boolean isAdd = false;
    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01, R.id.ll02};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01, R.id.miniFab02};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];
    private AnimatorSet addBillTranslate1;
    private AnimatorSet addBillTranslate2;

    private DaoSession daoSession;
    private NoteDao noteDao;
    private ReminderDao reminderDao;

    public static final String RECORD_ID = "com.example.asus.notes.RECORD_ID";
    private static final String TAG = "MainActivity";

    MainAdapter.MainClickListener mainClickListener = new MainAdapter.MainClickListener() {
        @Override
        public void onClick(int position) {
            if (mAdapter.isNote) {
                long cid = mAdapter.getNote(position).getId();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(RECORD_ID, cid);
                startActivity(intent);
            } else {
                long cid = mAdapter.getReminder(position).getId();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(RECORD_ID, cid);
                startActivity(intent);
            }
        }
    };

    MainAdapter.MainClickListener mainlClickListener = new MainAdapter.MainClickListener() {
        @Override
        public void onClick(int position) {
            final int wpos = position;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("确认")
                    .setMessage("您确定要删除吗？")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (mAdapter.isNote) {
                                noteDao.delete(mAdapter.getNote(wpos));
                            } else {
                                Reminder entr = mAdapter.getReminder(wpos);
                                ReminderEntryDao dao = daoSession.getReminderEntryDao();
                                dao.queryBuilder().where(ReminderEntryDao.Properties.ReminderId.eq(entr.getId())).buildDelete().executeDeleteWithoutDetachingEntities();
                                daoSession.clear();
                                reminderDao.delete(entr);
                            }
                            mAdapter.UpdateItems();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主界面的recycview , xml文件在content_main中
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayout);

        daoSession = ((NotesApp) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();
        reminderDao = daoSession.getReminderDao();

        mAdapter = new MainAdapter(daoSession, mainClickListener, mainlClickListener);
        recyclerView.setAdapter(mAdapter);

        //app顶部toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //对悬浮纽的初始化和点击监听
        initView();
        //悬浮动画的初始设置
        setDefaultValues();
        setSupportActionBar(toolbar);
        //侧边抽屉
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //侧边栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.UpdateItems();
    }

    private void initView() {
        fab01Add = (FloatingActionButton) findViewById(R.id.fab01Add);
        FloatingActionButton miniFab01 = (FloatingActionButton) findViewById(R.id.miniFab01);
        FloatingActionButton miniFab02 = (FloatingActionButton) findViewById(R.id.miniFab02);
        //对add悬浮纽的初始化
        rlAddBill = (RelativeLayout) findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length; i++) {
            ll[i] = (LinearLayout) findViewById(llId[i]);
        }
        for (int i = 0; i < fabId.length; i++) {
            fab[i] = (FloatingActionButton) findViewById(fabId[i]);
        }
        //add悬浮纽的点击事件
        fab01Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab01Add.setImageResource(isAdd ? R.mipmap.add : R.mipmap.cancel);
                isAdd = !isAdd;
                rlAddBill.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addBillTranslate1.setTarget(ll[0]);
                    addBillTranslate1.start();
                    addBillTranslate2.setTarget(ll[1]);
                    addBillTranslate2.start();
                }
            }
        });
        //提醒事项悬浮纽的点击事件
        miniFab01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminder entry = new Reminder();
                entry.setTitle("");
                reminderDao.insert(entry);
                hideFABMenu();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra(RECORD_ID, entry.getId());
                Log.i(TAG, "onClick: Add Reminder: " + entry.getId());
                startActivity(intent);
            }
        });
        //备忘录悬浮纽的点击事件
        miniFab02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note entry = new Note();
                entry.setTitle("");
                entry.setContent("");
                noteDao.insert(entry);
                hideFABMenu();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(RECORD_ID, entry.getId());
                Log.i(TAG, "onClick: Add Note: " + entry.getId());
                startActivity(intent);
            }
        });
    }

    //收起 悬浮纽列表
    private void hideFABMenu() {
        rlAddBill.setVisibility(View.GONE);
        fab01Add.setImageResource(R.mipmap.add);
        isAdd = false;
    }

    //悬浮动画的初始设置
    private void setDefaultValues() {
        addBillTranslate1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.add_bill_anim);
        addBillTranslate2 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.add_bill_anim);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    //menu的oncreate
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    //actionbar的点击事件
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //侧边栏的点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_list) {
            mAdapter.isNote = false;
            mAdapter.UpdateItems();
        } else if (id == R.id.nav_document) {
            mAdapter.isNote = true;
            mAdapter.UpdateItems();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
