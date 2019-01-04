package com.example.asus.notes;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主界面的recycview , xml文件在content_main中
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        mLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayout);

        DaoSession daoSession = ((NotesApp) getApplication()).getDaoSession();
        mAdapter = new MainAdapter(daoSession);
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
                hideFABMenu();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
        //备忘录悬浮纽的点击事件
        miniFab02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFABMenu();
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
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

    //menu的oncreate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //actionbar的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //侧边栏的点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_list) {

        } else if (id == R.id.nav_document) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
