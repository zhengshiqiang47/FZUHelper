package com.helper.west2ol.fzuhelper.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.helper.west2ol.fzuhelper.R;
import com.helper.west2ol.fzuhelper.fragment.CourseTableFragment;
import com.helper.west2ol.fzuhelper.fragment.GradeFragment;
import com.helper.west2ol.fzuhelper.fragment.MathFragment;
import com.helper.west2ol.fzuhelper.util.ActivityController;
import com.helper.west2ol.fzuhelper.util.HttpUtil;

public class MainContainerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG="MainActivity";

    public CourseTableFragment courseTableFragment;
    GradeFragment gradeFragment;
    MathFragment mathFragment;

    NavigationView navigationView;
    MenuItem course_Item;
    private String id;

    Bundle parameterToFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        ActivityController.addActivity(this);

        id = getIntent().getStringExtra("id");
        parameterToFragment = new Bundle();
        parameterToFragment.putString("id",id);
        courseTableFragment = new CourseTableFragment();
        courseTableFragment.setArguments(parameterToFragment);
        getFragmentManager().beginTransaction()
                .replace(R.id.main_container , courseTableFragment)
                .commit();

        new Login().execute();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        course_Item = navigationView.getMenu().getItem(0);
        course_Item.setChecked(true);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
    @Override
    public void onResume(){
        super.onResume();
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
    /*
    菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_table, menu);
        return true;
    }
    /*
    菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                parameterToFragment.putString("id",id);
                courseTableFragment = new CourseTableFragment();
                courseTableFragment.setArguments(parameterToFragment);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_container , courseTableFragment)
                        .commit();
                break;
            case R.id.item2:
                parameterToFragment.putString("id",id);
                gradeFragment = new GradeFragment();
                gradeFragment.setArguments(parameterToFragment);
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_container , gradeFragment)
                        .commit();
                break;
            case R.id.item3:
                mathFragment = new MathFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_container , mathFragment)
                        .commit();
                break;
            case R.id.item4:
                Intent intent4 = new Intent(MainContainerActivity.this , OtherActivity.class);
                intent4.putExtra("id" , id);
                startActivity(intent4);
                break;
            case R.id.item8:
                parameterToFragment.putString("id",id);
                Intent intent_9 = new Intent(MainContainerActivity.this,SettingActivity.class);
                startActivity(intent_9);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class Login extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpUtil.Login("dkfjd","djkf");
            return null;
        }

    }
}
