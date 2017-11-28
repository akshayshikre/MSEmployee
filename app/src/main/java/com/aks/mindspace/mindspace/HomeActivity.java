package com.aks.mindspace.mindspace;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static com.aks.mindspace.mindspace.LoginActivity.isConnected;
import static com.aks.mindspace.mindspace.LoginActivity.isURLReachable;
import static com.aks.mindspace.mindspace.SplashActivity.Dept;
import static com.aks.mindspace.mindspace.SplashActivity.Email;
import static com.aks.mindspace.mindspace.SplashActivity.Gender;
import static com.aks.mindspace.mindspace.SplashActivity.Id;
import static com.aks.mindspace.mindspace.SplashActivity.Imagepath;
import static com.aks.mindspace.mindspace.SplashActivity.MyPREFERENCES;
import static com.aks.mindspace.mindspace.SplashActivity.Name;
import static com.aks.mindspace.mindspace.SplashActivity.Password;
import static com.aks.mindspace.mindspace.SplashActivity.Rating;
import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Picasso.Builder builder;
    ProgressBar spinner;
    SharedPreferences sharedpreferences;
 static String empname="",empid="",empemailid="",empimg="",empdep="unknown",empgender="",emprating="",empno="";
    ImageView nav_img;
    RatingBar rb;
   // ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spinner= (ProgressBar) findViewById(R.id.homeprogressbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        empname = intent.getStringExtra("empname");
        empid = intent.getStringExtra("empid");
        empdep = intent.getStringExtra("empdep");
        empimg= intent.getStringExtra("empimg");
        empemailid= intent.getStringExtra("empemailid");
        empgender= intent.getStringExtra("empgender");
        emprating= intent.getStringExtra("emprating");
        empno= intent.getStringExtra("empno");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //*********************Our code*************************
        navigationView.getMenu().clear(); //clear old inflated items.
        if(empdep.equals("admin"))
        navigationView.inflateMenu(R.menu.activity_home_admindrawer); //inflate new items.
        if(empdep.equals("sale"))
            navigationView.inflateMenu(R.menu.activity_home_saledrawer); //inflate new items.
        View hView =  navigationView.getHeaderView(0);
        nav_img = (ImageView)hView.findViewById(R.id.navimg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nav_img.setElevation(8.0f);
        }
        builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener()
        {

            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                if(empgender.equals("female"))
                    Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new CircleTransform()).into(nav_img);
                else if(empgender.equals("male"))
                    Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new CircleTransform()).into(nav_img);
            }
        });
        File mindspaceDirectory = new File(Environment.getExternalStorageDirectory() + "/mindspace/");
        mindspaceDirectory.mkdirs();
        new serveravailable().execute();
 //*************************Header view display***********************************
        TextView nav_user = (TextView)hView.findViewById(R.id.navname);
        nav_user.setText(empname);
        TextView nav_email = (TextView)hView.findViewById(R.id.navemail);
        nav_email.setText(empemailid);
        TextView nav_empid = (TextView) hView.findViewById(R.id.navid);
        TextView nav_empno = (TextView) hView.findViewById(R.id.navno);
        nav_empno.setText(empno);
         rb=(RatingBar)hView.findViewById(R.id.empratingbar);
        if(!empdep.equals("admin")) {
            nav_empid.setText(empdep + " : " + empid);
            rb.setVisibility(View.VISIBLE);
           // rb.setRating(Float.parseFloat(emprating));
        }
        else {
            nav_empid.setText(empdep);
        }
    }
//*****************************End on create*************************************************************************
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
        if (id == R.id.action_refresh) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Name, "");
            editor.putString(Dept, "");
            editor.putString(Id, "");
            editor.putString(Password, "");
            editor.putString(Email, "");
            editor.putString(Imagepath, "");
            editor.putString(Gender, "");
            editor.putString(Rating, "");
            editor.commit();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.nav_add) {
             Intent in=new Intent(this,AddEmplyeeActivity.class);
             startActivity(in);

        } else if (id == R.id.nav_requests) {

        } else if (id == R.id.nav_targets) {

        } else if (id == R.id.nav_myperformance) {

        } else if (id == R.id.nav_schools) {
             Intent in=new Intent(this,SchoolListActivity.class);
             startActivity(in);

        } else if (id == R.id.nav_targets) {

        } else if (id == R.id.nav_list) {
             Intent in=new Intent(this,EmployeeListActivity.class);
             startActivity(in);

        } else if (id == R.id.nav_queries) {

        } else if (id == R.id.nav_mytargets) {

        } else if (id == R.id.nav_maps) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   static public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    class internetavailable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.i("Internet","Internet is available");
                spinner.setVisibility(View.VISIBLE);
                new serveravailable().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Internet is not available", Toast.LENGTH_SHORT).show();
                //loadlocal();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return (isConnected(getApplicationContext()));
        }
    }
    class serveravailable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.i("server","server is available");
                builder.build().load("http://" + serverip + "/img/" + empid + ".jpg").transform(new CircleTransform())
                        .into(nav_img, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                spinner.setVisibility(View.INVISIBLE);
                               // Log.i("calling saveimg",empid+"onsuccess");
                                //saveimg();

                            }

                            @Override
                            public void onError() {
                                if (empgender.equals("female"))
                                    Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new CircleTransform()).into(nav_img);
                                else if (empgender.equals("male"))
                                    Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new CircleTransform()).into(nav_img);
                                spinner.setVisibility(View.INVISIBLE);
                            }
                        });
                spinner.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(getApplicationContext(),"Server is not available",Toast.LENGTH_SHORT).show();
              //  loadlocal();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return (isURLReachable(getApplicationContext()));
        }
    }
   /* public void loadlocal(){
        File file = new File(Environment.getExternalStorageDirectory() + "/mindspace/" + empid + ".jpg");
        spinner.setVisibility(View.VISIBLE);
        if (file.exists()) {
            Log.i("load local file dere ",file.getPath());
            Picasso.with(HomeActivity.this).load(file).transform(new CircleTransform())
                    .into(nav_img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {spinner.setVisibility(View.INVISIBLE); }
                        @Override
                        public void onError() {
                            if (empgender.equals("female"))
                                Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new CircleTransform()).into(nav_img);
                            else if (empgender.equals("male"))
                                Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new CircleTransform()).into(nav_img);
                            spinner.setVisibility(View.INVISIBLE); }
                    });
        } else {
            Log.i("load local No file ",file.getPath());
            if (empgender.equals("female"))
                Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new CircleTransform()).into(nav_img);
            else if (empgender.equals("male"))
                Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new CircleTransform()).into(nav_img);
            spinner.setVisibility(View.INVISIBLE);
        }

    }
    public void saveimg(){/*
        Drawable drawable = nav_img.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytearrayoutputstream);
        File file;
        file = new File( Environment.getExternalStorageDirectory() + "/mindspace/"+empid+".png");
        FileOutputStream fileoutputstream;
        try
        {
            file.createNewFile();
            fileoutputstream = new FileOutputStream(file);
            fileoutputstream.write(bytearrayoutputstream.toByteArray());
            fileoutputstream.close();
            //  Toast.makeText(HomeActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        File fdelete = new File(Environment.getExternalStorageDirectory() + "/mindspace/" + empid + ".jpg");
        if (fdelete.exists()) {
            Log.i("file exists", String.valueOf(fdelete.exists()));
            if (fdelete.delete()) {
                Log.i("file Deleted :" , fdelete.getPath());
                Picasso.with(HomeActivity.this)
                        .load("http://" + serverip + "/img/" + empid + ".jpg")
                        .into(getTarget(empid));
            } else {
                Log.i("file not Deleted :" , fdelete.getPath());
                Picasso.with(HomeActivity.this)
                        .load("http://" + serverip + "/img/" + empid + ".jpg")
                        .into(getTarget(empid));
            }
        }
        else {
            Log.i("in saveimg","file directly saved"+fdelete.getPath());
            Picasso.with(HomeActivity.this)
                    .load("http://" + serverip + "/img/" + empid + ".jpg")
                    .into(getTarget(empid));

        }

    }
    private static Target getTarget(final String userid){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/mindspace/" + userid + ".jpg");
                        Log.i("get target :" , file.getPath());
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                            Log.i("file saved :" , file.getPath());
                        } catch (IOException e) {
                            Log.e("IOException target", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }
*/
}
