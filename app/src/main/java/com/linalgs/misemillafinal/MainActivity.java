package com.linalgs.misemillafinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.media.MediaCas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GoogleApiClient mGoogleApiClient;
    private int Tipo; //1 registro, 2 es google, 3 facebook
    TextView textView2;
    ImageView imageView;
    FragmentManager fm;
    FragmentTransaction ft;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    private String correoR, contraseñaR, urlf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            correoR = extras.getString("correo");
            contraseñaR = extras.getString("contraseña");
            urlf = extras.getString("urlf");
        }



        //Glide.with(this).load(urlf).crossFade().placeholder(R.drawable.user).into(imageView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //textView2 =(TextView) findViewById(R.id.textView2);
        //imageView =(ImageView) findViewById(R.id.imageView);

        //textView2.setText(correoR);

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        Intent intent;
        prefs = getSharedPreferences("MisPreferencias" , Context.MODE_PRIVATE);
        editor = prefs.edit();
        Tipo = prefs.getInt("optLog",0);
        String f = prefs.getString("correo", correoR);
        String nombre = prefs.getString("contraseña", contraseñaR);
        String foto = prefs.getString("urlf", urlf);


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Toast.makeText(getApplicationContext(),f,Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Bundle bundle = new Bundle();
            bundle.putString("correo", f);
            bundle.putString("contraseña", nombre);
            bundle.putString("urlf", foto);


            PerfilFragment fragment = new PerfilFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.frame,fragment).commit();


        } else if (id == R.id.nav_send) {
            if(Tipo == 1){
                intent = new Intent(MainActivity.this,LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            else if (Tipo == 4 ){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"No se Puede Cerrar Sesion en Google",Toast.LENGTH_SHORT).show();}

                    }
                });
            }
            else if (Tipo == 3){
                LoginManager.getInstance().logOut();
                Toast.makeText(getApplicationContext(),"Facebook" +
                        "" +
                        "",Toast.LENGTH_SHORT).show();

                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();
            editor.putInt("optLog",0);
            editor.commit();
            Toast.makeText(getApplicationContext(),"Goodbye",Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


