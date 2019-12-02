package com.mga.vikram.odor;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment(),"navigation_home");
    }


    private boolean loadFragment(Fragment fragment, String tag){
        if(fragment != null ){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment,tag).commit();
            return true;
        }
        return  false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String tag = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                tag = "navigation_home";
                break;
            case R.id.navigation_dashboard:
                fragment = new DashboardFragment();
                tag = "navigation_dashboard";
                break;
            case R.id.navigation_notifications:
                fragment = new MapFragment();
                tag = "navigation_notifications";
                break;
        }
        return loadFragment(fragment,tag);
    }
}
