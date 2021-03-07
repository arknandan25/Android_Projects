package com.example.e2eeapp_alpha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Fragments extends AppCompatActivity {

    private ViewPager myviewPager;
    private TabLayout mytabLayout;
    private tabsAccessorAdapter mytabsAccessorAdapter;

    //Firebase Login
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    //DarkMode
    private boolean isChecked_past = false;

    private boolean isChecked_curr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        myviewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        mytabsAccessorAdapter = new tabsAccessorAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(mytabsAccessorAdapter);

        mytabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.image_enc:
                Intent myIntent = new Intent(Fragments.this, MainActivity2.class);
//                myIntent.putExtra("key", value); //Optional parameters
                Fragments.this.startActivity(myIntent);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logging you out of the App!", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                SendUserToLoginActivity();
                return true;
            case R.id.fragmentz:
                Intent myIntent1 = new Intent(Fragments.this, Fragments.class);
//                myIntent.putExtra("key", value); //Optional parameters
                Fragments.this.startActivity(myIntent1);
                break;
            case R.id.image_upload:
                Intent myIntent2 = new Intent(Fragments.this, ImageUpload.class);
                Fragments.this.startActivity(myIntent2);

                break;
            case R.id.find_friends_temp:
                Intent myIntent3 = new Intent(Fragments.this, FindFriends.class);
                Fragments.this.startActivity(myIntent3);
                break;
            case R.id.dark_mode:
//                MenuItem checkable = menu.findItem(R.id.dark_mode);
                isChecked_curr = !item.isChecked();
                item.setChecked(isChecked_curr);

                if (isChecked_curr == true && isChecked_past == false){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//                    item.setChecked(true);
                    item.setChecked(isChecked_curr);

//                    checkable.setChecked(isChecked);
                    Toast.makeText(this, "dark mode Enabledd", Toast.LENGTH_SHORT).show();
                    Log.i("Checkced status:", String.valueOf(isChecked_curr));
                }else{
//                if(isChecked == false){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    item.setChecked(false);


//                    item.setChecked(isChecked);
//                    item.setChecked(!isChecked);
                    Toast.makeText(this, "dark mode Disabled", Toast.LENGTH_SHORT).show();
                    Log.i("Checkced status:", String.valueOf(isChecked_curr));
                }

                isChecked_past = isChecked_curr;

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void SendUserToLoginActivity() {
        Intent LoginIntent = new Intent(Fragments.this, LoginActivity.class);
        startActivity(LoginIntent);
    }




}