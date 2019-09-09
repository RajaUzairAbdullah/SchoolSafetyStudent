package studentapp.schoolsafety.com;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;



public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    DrawerLayout drawer;
    TextView HeaderText;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        HeaderText = (TextView) findViewById(R.id.headertxt);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ViewCompat.setLayoutDirection(navigationView,ViewCompat.LAYOUT_DIRECTION_RTL);

        View navHeader = navigationView.getHeaderView(0);
//        displyView(0); //call search fragment.

        SharedPreferences prefs = getSharedPreferences(Constant.USER_PREFS, MODE_PRIVATE);//object of shared pref
        TextView userNameText = navHeader.findViewById(R.id.txt_user_name);
        TextView userEmailText = navHeader.findViewById(R.id.txt_user_email);



        userNameText.setText(prefs.getString("user_name",""));//called
        userEmailText.setText(prefs.getString("user_email",""));//called

        //Settting Default Fragment
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboardfragment()).commit();
            HeaderText.setText(R.string.home_heading);
            navigationView.setCheckedItem(R.id.nav_home);
        }
        //Settting Default Fragment


        Button menubtn = (Button) findViewById(R.id.menubutton);
        menubtn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


    }

    @Override
    public void onClick(View view) {


        drawer.openDrawer(GravityCompat.END);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);

        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Dashboardfragment()).addToBackStack(null).commit();
            HeaderText.setText(R.string.home_heading);
            HeaderText.setTextSize(24);

        } else if (id == R.id.nav_packages) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Packages()).addToBackStack(null).commit();
            HeaderText.setText("");
            HeaderText.setText(R.string.packages_menu);
            HeaderText.setTextSize(24);


        } else if (id == R.id.nav_contact) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ContactUs()).addToBackStack(null).commit();
            HeaderText.setText("");
            HeaderText.setText(R.string.contactus_menu);
            HeaderText.setTextSize(24);

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            HeaderText.setText("");
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }



}
