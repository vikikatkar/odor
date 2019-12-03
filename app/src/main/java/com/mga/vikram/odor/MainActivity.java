package com.mga.vikram.odor;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, HomeFragment.OnLoginStatusChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);

        homeFragment = new HomeFragment();
        loadFragment(homeFragment,"navigation_home");
    }


    private boolean loadFragment(Fragment fragment, String tag){
        if(fragment != null ){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment,tag).commit();
            getSupportFragmentManager().executePendingTransactions();
            return true;
        }
        return  false;
    }

    HomeFragment homeFragment;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String tag = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                homeFragment = new HomeFragment();
                fragment = homeFragment;
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

    // create an action bar button
    public Menu appBarMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.app_bar_menu is a reference to an xml file named app_bar_menu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);

        appBarMenu = menu;
        updateLoginMenu();

        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String tag = null;


        if (id == R.id.loginLogoutAppBarButton) {
            if( loginSuccessful ){
                //User wants to logout
                homeFragment.onActivityMessage("sign-out");
                updateLoginMenu();
            }
            tag = "navigation_home";
            return loadFragment(homeFragment,tag);
        }
        return super.onOptionsItemSelected(item);
    }
    boolean loginSuccessful= false;


    //For communication from HomeFragment
    public void onLoginItemSelected(String signal){

        if( signal.equals("login-success") ){
            loginSuccessful = true;
            updateLoginMenu();
        }else{
            loginSuccessful = false;
            updateLoginMenu();
        }
    }
    void updateLoginMenu(){
        if( appBarMenu!=null ){
            MenuItem item = appBarMenu.findItem(R.id.loginLogoutAppBarButton);
            if( loginSuccessful ){
                item.setIcon(R.drawable.ic_logout_black_24dp);
            }else{
                item.setIcon(R.drawable.ic_login_black_24dp);
            }
        }
    }


}
