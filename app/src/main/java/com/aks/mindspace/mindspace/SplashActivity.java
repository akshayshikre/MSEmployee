package com.aks.mindspace.mindspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sp;
    ProgressBar spinner;
    public static final String serverip = "192.168.1.116";
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Phoneno = "noKey";
    public static final String Dept = "deptKey";
    public static final String Email = "emailKey";
    public static final String Id = "idKey";
    public static final String Password = "passKey";
    public static final String Imagepath = "imgKey";
    public static final String Gender = "genderKey";
    public static final String Rating = "ratingKey";
    String empname = "", empemailid = "", empid = "", empimg = "", password = "", empdep = "", empno = "", empgender = "", emprating = "";
    ImageView anm;
    Intent in;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        spinner = (ProgressBar) findViewById(R.id.progressBar2);
        anm = (ImageView) findViewById(R.id.anm);
        getSupportActionBar().hide();
        anm.setBackgroundResource(R.drawable.animation_list);
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        empemailid = sp.getString(Email, "");
        password = sp.getString(Password, "");
        empname = sp.getString(Name, "");
        empno = sp.getString(Phoneno, "");
        empid = sp.getString(Id, "");
        empdep = sp.getString(Dept, "");
        empimg = sp.getString(Imagepath, "");
        empgender = sp.getString(Gender, "");
        emprating = sp.getString(Rating, "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!empemailid.equals("") && !password.equals("") && !empname.equals("") && !empid.equals("") &&
                        !empdep.equals("") && !empimg.equals("") && !empno.equals("") && !empgender.equals("") &&
                        !emprating.equals("")) {
                    Intent i  = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("empname", empname);
                    i.putExtra("empid", empid);
                    i.putExtra("empno", empno);
                    i.putExtra("empdep", empdep);
                    i.putExtra("empimg", empimg);
                    i.putExtra("empemailid", empemailid);
                    i.putExtra("empgender", empgender);
                    i.putExtra("emprating", emprating);
                    startActivity(i);
                    finish();
                    startActivity(i);
                    finish();
                } else {
                    in = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        }, 3080);
       // Toast.makeText(getApplicationContext(), empemailid + password + empname + empid + empdep + empimg + empno + empgender + emprating, Toast.LENGTH_LONG).show();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AnimationDrawable frameAnimation =
                (AnimationDrawable) anm.getBackground();
        if (hasFocus) {
            frameAnimation.start();
        } else {
            frameAnimation.stop();
        }
    }
}