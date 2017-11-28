package com.aks.mindspace.mindspace;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.aks.mindspace.mindspace.SplashActivity.MyPREFERENCES;
import static com.aks.mindspace.mindspace.SplashActivity.Name;
import static com.aks.mindspace.mindspace.SplashActivity.serverip;


public class SchoolListActivity extends AppCompatActivity  {
    private String uploadurl="http://"+com.aks.mindspace.mindspace.SplashActivity.serverip+"/mindspace/updateinfo.php";
    private final int IMG_REQUEST=1;
    private Bitmap bitmap;
    private static final int CAMERA_REQUEST = 1888;
    public static String schoolserverip=serverip;
    public  static ArrayList<school> schoolArrayList =new ArrayList<school>();
    public  static ArrayList<String> scityArrayList =new ArrayList<String>();
    private SchoolAdapter adapter;
    private RecyclerView recyclerView;
    //boolean flag=false;
    View v;
    Button gotomap;
    Spinner spin;
    ProgressBar pb;
    FloatingActionButton fab;
    AlertDialog.Builder alertDialog;
    EditText edschoolid,edschoolname,edschoolarea,edschoolcity,edschooladdr,edpname,edpno,edschoolother,edschoolreq;
    Button buttonfrom,buttonto;
    int cd,cm,cy,ctd,ctm,cty;
    View adview;
    school ads;
    ImageButton img;
    private DialogInterface adddialog;
    ArrayAdapter<String> cityAdapter;
    private java.lang.String from;
    private java.lang.String to;
    private String addimgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
        buttonfrom= (Button) findViewById(R.id.buttonfrom);
        buttonto= (Button) findViewById(R.id.buttonto);
        spin=(Spinner)findViewById(R.id.spinner);
        spin.setEnabled(false);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!spin.getSelectedItem().toString().equals("All"))
                    new gets1code().execute(spin.getSelectedItem().toString());
                else if(spin.getSelectedItem().toString().equals("All")){
                    new getscode().execute();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recview);
        gotomap =(Button)findViewById(R.id.button3);
        fab=(FloatingActionButton)findViewById(R.id.fabschool);
        pb=(ProgressBar)findViewById(R.id.progressBar44);
        gotomap.setEnabled(false);
        adapter = new SchoolAdapter(schoolArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.i("@@@@@",""+ schoolArrayList.get(position).schoolname
                                + schoolArrayList.get(position).longi
                                + schoolArrayList.get(position).latti);
                        Intent in=new Intent(getApplicationContext(),MapsActivity.class);
                        in.putExtra("hname", schoolArrayList.get(position).schoolname);
                        in.putExtra("longi", schoolArrayList.get(position).longi);
                        in.putExtra("latti", schoolArrayList.get(position).latti);
                        in.putExtra("single",true);
                        in.putExtra("iter",position);
                        startActivity(in);
                    }
                })
        );
        alertDialog = new AlertDialog.Builder(this);
        /*LayoutInflater inflater = alertDialog.getLayoutInflater();
        dialoglayout = inflater.inflate(R.layout.customized_builder, frameView);*/
        adview = getLayoutInflater().inflate(R.layout.schooldialog_layout,null);
        alertDialog.setView(adview);
        img= (ImageButton) adview.findViewById(R.id.imageButton);
        edschoolid= (EditText) adview.findViewById(R.id.edsid);
        edschoolname= (EditText) adview.findViewById(R.id.edsname);
        edschoolarea= (EditText) adview.findViewById(R.id.edsarea);
        edschoolcity= (EditText) adview.findViewById(R.id.edscity);
        edschooladdr= (EditText) adview.findViewById(R.id.edsaddr);
        edpname= (EditText) adview.findViewById(R.id.edspname);
        edpno= (EditText) adview.findViewById(R.id.edspno);
        edschoolother= (EditText) adview.findViewById(R.id.edslother);
        edschoolreq= (EditText) adview.findViewById(R.id.edslreq);
        alertDialog.setTitle("Add School");
        alertDialog.setCancelable(false);
        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat dispdateformat = new SimpleDateFormat("dd-MMM-yyyy");
        final SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm aa");
        String date = dateformat.format(c.getTime());
        String time = timeformat.format(c.getTime());
        Log.e("date",date);
        Log.e("time",time);
        /*LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);*/
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           dialogInterface.dismiss();
                Intent in =new Intent(SchoolListActivity.this,SchoolListActivity.class);
                startActivity(in);
                finish();
            }
        });
        alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               // GPSTracker gps = new GPSTracker(SchoolListActivity.this);
                String date = dateformat.format(c.getTime());
                String time = timeformat.format(c.getTime());
                // Check if GPS enabled
                double latitude = 2,longitude=2;
               /* if(gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Log.e("latlong",String.valueOf(latitude)+":"+String.valueOf(longitude));
                    Log.e("datetime",date+";"+time);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }*/
                if(!((int)latitude==0) && !((int)longitude==0))
                {
                    if(     !edschoolname.getText().toString().equals("") &&
                            !edschoolarea.getText().toString().equals("") &&
                            !edschooladdr.getText().toString().equals("") &&
                            !edschoolcity.getText().toString().equals("") &&
                            !edpname.getText().toString().equals("") &&
                            !edpno.getText().toString().equals("") &&
                            !edschoolother.getText().toString().equals("") &&
                            !edschoolreq.getText().toString().equals("") ) {
                         ads = new school(
                                edschoolname.getText().toString(),
                                edschoolarea.getText().toString(),
                                edschooladdr.getText().toString(),
                                edschoolcity.getText().toString(),
                                edpname.getText().toString(),
                                edpno.getText().toString(),
                                edschoolother.getText().toString(),
                                edschoolreq.getText().toString(),
                                getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString(Name, "unkonwn"),
                                date, time, edschoolid.getText().toString(),
                                String.valueOf(latitude), String.valueOf(longitude)
                        );
                        adddialog = dialogInterface;
                       /* Toast.makeText(SchoolListActivity.this, ads.schoolid + ads.schoolname + ads.schoolarea + ads.schooladdr + ads.schoolcity + ads.other + ads.req +
                                ads.empname + ads.empdate + ads.emptime + ads.latti + ads.longi, Toast.LENGTH_LONG).show();*/
                       addimgname=ads.schoolid;
                       new addscode().execute();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please Enter Full Information!",Toast.LENGTH_LONG).show();
                        Intent in =new Intent(SchoolListActivity.this,SchoolListActivity.class);
                        startActivity(in);
                        finish();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    dialogInterface.dismiss();
                    //gps.showSettingsAlert();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                alertDialog.show();
                //fab.setVisibility(View.VISIBLE);
                /*if (view != null) {
                    v=view;
                    ViewGroup parent = (ViewGroup) view.getParent();
                    if (parent != null) {
                        parent.removeView(view);
                        //fab.setVisibility(View.VISIBLE);

                        if (view == null) {
                            view=v;
                            ViewGroup parent2 = (ViewGroup) view.getParent();
                            if (parent2 != null) {
                                parent2.addView(view);
                                //fab.setVisibility(View.VISIBLE);
                            }
                    }*/
                //adapter.notifyDataSetChanged();
            }
        });

        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("gotomap", String.valueOf(schoolArrayList.size()));
                if(schoolArrayList.size()>0) {
                    Intent in = new Intent(SchoolListActivity.this, MapsActivity.class);
                    in.putExtra("hname", schoolArrayList.get(0).schoolname);
                    in.putExtra("longi", schoolArrayList.get(0).longi);
                    in.putExtra("latti", schoolArrayList.get(0).latti);
                    in.putExtra("single",false);
                    startActivity(in);
                }
            }
        });
        final Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.DATE, 1);
        buttonfrom.setText(dateformat.format(c2.getTime()));
        buttonto.setText(dateformat.format(c.getTime()));
        from=dateformat.format(c2.getTime());
        to=dateformat.format(c.getTime());
        cy = c2.get(Calendar.YEAR);
        cm = c2.get(Calendar.MONTH);
        cd = c2.get(Calendar.DAY_OF_MONTH);
        cty = c.get(Calendar.YEAR);
        ctm = c.get(Calendar.MONTH);
        ctd = c.get(Calendar.DAY_OF_MONTH);
        buttonfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                int mYear = cy;
                int mMonth = cm;
                int mDay = cd;
                DatePickerDialog datePickerDialog = new DatePickerDialog(SchoolListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String sday,smonth;
                                if((monthOfYear+1)<10) smonth ="0"+(monthOfYear+1); else smonth =""+(monthOfYear+1);
                                if((dayOfMonth)<10) sday ="0"+dayOfMonth; else sday =""+dayOfMonth;
                                from=year  + "-" + smonth+ "-" +sday;
                                buttonfrom.setText(year  + "-" + smonth+ "-" +sday);
                                synchronized(spin){
                                    if(!spin.getSelectedItem().toString().equals("All"))
                                        new gets1code().execute(spin.getSelectedItem().toString());
                                    else if(spin.getSelectedItem().toString().equals("All")){
                                        new getscode().execute();
                                    }
                                }
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        buttonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear = cty;
                int mMonth = ctm;
                int mDay = ctd;
                DatePickerDialog datePickerDialog = new DatePickerDialog(SchoolListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String sday,smonth;
                                if((monthOfYear+1)<10) smonth ="0"+(monthOfYear+1); else smonth =""+(monthOfYear+1);
                                if((dayOfMonth)<10) sday ="0"+dayOfMonth; else sday =""+dayOfMonth;
                                to=year  + "-" + smonth+ "-" +sday;
                                buttonto.setText(year  + "-" + smonth+ "-" +sday);
                                synchronized(spin){
                                    if(!spin.getSelectedItem().toString().equals("All"))
                                        new gets1code().execute(spin.getSelectedItem().toString());
                                    else if(spin.getSelectedItem().toString().equals("All")){
                                        new getscode().execute();
                                    }
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        new serveravailable().execute();
    }



    class getscode extends AsyncTask<Void, Void, String> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //pb.setProgress(pb.getProgress()+10);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            from=buttonfrom.getText().toString();
            to=buttonto.getText().toString();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                Log.e("from.to","."+from+"."+to+".");
                link="http://"+schoolserverip+"/mindspace/gets.php";
                data  = URLEncoder.encode("from", "UTF-8") + "=" +
                        URLEncoder.encode(from, "UTF-8");
                data  +="&" + URLEncoder.encode("to", "UTF-8") + "=" +
                        URLEncoder.encode(to, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.i("doinback",sb.toString());
                return sb.toString();
            } catch(Exception e){
                Log.i("doinback",new String("Exception: " + e.getMessage()));
                schoolArrayList.clear();
                adapter = new SchoolAdapter(schoolArrayList);
               // recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //pb.setVisibility(View.GONE);
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            Log.i("onpost","in");
            Log.i("onpost",res);
            schoolArrayList.clear();
            try {
                JSONObject job=new JSONObject(res);
                JSONArray jsonArray = job.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    schoolArrayList.add(new school(
                            json_obj.getString("schoolname"),json_obj.getString("schoolarea"),
                            json_obj.getString("schooladdr"),json_obj.getString("schoolcity"),
                            json_obj.getString("princiname"),json_obj.getString("princino"),
                            json_obj.getString("other"),json_obj.getString("req"),
                            json_obj.getString("empname"),json_obj.getString("empdate"),
                            json_obj.getString("emptime"),json_obj.getString("schoolid"),
                            json_obj.getString("latti"),json_obj.getString("longi")
                    ));
                }
                school e;
                for(int i = 0; i< schoolArrayList.size(); i++) {
                    e= schoolArrayList.get(i);
                    Log.i("details--",e.schoolname+e.schoolarea+e.schooladdr+e.schoolcity+e.princiname+
                            e.princino+e.other+e.req+e.empname+e.empdate+e.emptime+e.req+e.schoolid
                            +e.longi+e.latti);
                }

                adapter = new SchoolAdapter(schoolArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(schoolArrayList.size()>0)
                    gotomap.setEnabled(true);
                pb.setVisibility(View.GONE);
            } catch (Exception e){
                schoolArrayList.clear();
                adapter = new SchoolAdapter(schoolArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
    }

    class gets1code extends AsyncTask<String, Void, String> {
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //pb.setProgress(pb.getProgress()+10);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            from=buttonfrom.getText().toString();
            to=buttonto.getText().toString();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String cat=arg0[0];
            String link="",data="";
            try{
                link="http://"+schoolserverip+"/mindspace/gets1.php";
                data  = URLEncoder.encode("schoolcity", "UTF-8") + "=" +
                        URLEncoder.encode(cat, "UTF-8");
                data  +="&" + URLEncoder.encode("from", "UTF-8") + "=" +
                        URLEncoder.encode(from, "UTF-8");
                data  +="&" + URLEncoder.encode("to", "UTF-8") + "=" +
                        URLEncoder.encode(to, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.i("doinback1",sb.toString());
                return sb.toString();
            } catch(Exception e){
                Log.i("doinback1",new String("Exception: " + e.getMessage()));
                schoolArrayList.clear();
                adapter = new SchoolAdapter(schoolArrayList);
                //recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //pb.setVisibility(View.GONE);
                return new String("Exception: " + e.getMessage());

            }
        }

        @Override
        protected void onPostExecute(String res) {
            Log.i("onpost1","in");
            Log.i("onpost1",res);
            try {
                schoolArrayList.clear();
                JSONObject job=new JSONObject(res);
                JSONArray jsonArray = job.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    schoolArrayList.add(new school(
                            json_obj.getString("schoolname"),json_obj.getString("schoolarea"),
                            json_obj.getString("schooladdr"),json_obj.getString("schoolcity"),
                            json_obj.getString("princiname"),json_obj.getString("princino"),
                            json_obj.getString("other"),json_obj.getString("req"),
                            json_obj.getString("empname"),json_obj.getString("empdate"),
                            json_obj.getString("emptime"),json_obj.getString("schoolid"),
                            json_obj.getString("latti"),json_obj.getString("longi")
                    ));
                }
                school e;
                for(int i = 0; i< schoolArrayList.size(); i++) {
                    e= schoolArrayList.get(i);
                    Log.i("details--",e.schoolname+e.schoolarea+e.schooladdr+e.schoolcity+e.princiname+
                            e.princino+e.other+e.req+e.empname+e.empdate+e.emptime+e.req+e.schoolid
                            +e.longi+e.latti);
                }

                adapter = new SchoolAdapter(schoolArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(schoolArrayList.size()>0)
                    gotomap.setEnabled(true);
                pb.setVisibility(View.GONE);
            } catch (Exception e){
                pb.setVisibility(View.GONE);
                schoolArrayList.clear();
                adapter = new SchoolAdapter(schoolArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
    }

    class serveravailable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.i("server","server is available");
                new getscode().execute();
                new getscitycode().execute();
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

    static public boolean isURLReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://"+schoolserverip);   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(10 * 1000);          // 10 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.wtf("Connection", "Success !");
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    class addscode extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+schoolserverip+"/mindspace/adds.php";
                data  = URLEncoder.encode("schoolid", "UTF-8") + "=" +
                        URLEncoder.encode(ads.schoolid, "UTF-8");
                data += "&" + URLEncoder.encode("schoolname", "UTF-8") + "=" +
                        URLEncoder.encode(ads.schoolname, "UTF-8");
                data += "&" + URLEncoder.encode("schoolarea", "UTF-8") + "=" +
                        URLEncoder.encode(ads.schoolarea, "UTF-8");
                data += "&" + URLEncoder.encode("schoolcity", "UTF-8") + "=" +
                        URLEncoder.encode(ads.schoolcity, "UTF-8");
                data += "&" + URLEncoder.encode("schooladdr", "UTF-8") + "=" +
                        URLEncoder.encode(ads.schooladdr, "UTF-8");
                data += "&" + URLEncoder.encode("princiname", "UTF-8") + "=" +
                        URLEncoder.encode(ads.princiname, "UTF-8");
                data += "&" + URLEncoder.encode("princino", "UTF-8") + "=" +
                        URLEncoder.encode(ads.princino, "UTF-8");
                data += "&" + URLEncoder.encode("other", "UTF-8") + "=" +
                        URLEncoder.encode(ads.other, "UTF-8");
                data += "&" + URLEncoder.encode("req", "UTF-8") + "=" +
                        URLEncoder.encode(ads.req, "UTF-8");
                data += "&" + URLEncoder.encode("empname", "UTF-8") + "=" +
                        URLEncoder.encode(ads.empname, "UTF-8");
                data += "&" + URLEncoder.encode("empdate", "UTF-8") + "=" +
                        URLEncoder.encode(ads.empdate, "UTF-8");
                data += "&" + URLEncoder.encode("emptime", "UTF-8") + "=" +
                        URLEncoder.encode(ads.emptime, "UTF-8");
                data += "&" + URLEncoder.encode("latti", "UTF-8") + "=" +
                        URLEncoder.encode(ads.latti, "UTF-8");
                data += "&" + URLEncoder.encode("longi", "UTF-8") + "=" +
                        URLEncoder.encode(ads.longi, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));
                Log.i("addscode","reader-");
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    Log.i("addscode","line-"+line);
                    sb.append(line);
                    break;
                }
                Log.i("add","."+sb.toString()+".");
                return sb.toString();
            } catch(Exception e){
                Log.d("adderror",new String("Exception: " + e.getMessage()));
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                Toast.makeText(SchoolListActivity.this,res,Toast.LENGTH_SHORT).show();
                school s;
                if(res.trim().equals("School record inserted successfully")) {
                    Log.e("add","success"+ res);
                    uploadImage(addimgname);
                    adapter.addItem(ads);
                    adddialog.dismiss();
                    adapter.notifyAll();
                }
                adddialog.dismiss();
                for(int i=0;i<schoolArrayList.size();i++) {
                    s=schoolArrayList.get(i);
                    Log.e("adddetails--",s.schoolid+s.schoolname+s.schoolarea+s.schoolcity+s.schooladdr+s.other+s.req+
                            s.princiname+s.princino+s.empname+s.empdate+s.emptime+s.latti+s.longi);
                }
                Intent in =new Intent(SchoolListActivity.this,SchoolListActivity.class);
                startActivity(in);
                finish();
            } catch (Exception e){
                e.printStackTrace();
                Intent in =new Intent(SchoolListActivity.this,SchoolListActivity.class);
                startActivity(in);
                finish();
            }
        }
    }

    class getscitycode extends AsyncTask<Void, Void, String> {
       /* @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            pb.setProgress(pb.getProgress()+1);
        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+schoolserverip+"/mindspace/getscity.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.i("doinback",sb.toString());
                return sb.toString();
            } catch(Exception e){
                Log.i("doinback",new String("Exception: " + e.getMessage()));
                scityArrayList.clear();
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            Log.i("onpost","in");
            Log.i("onpost",res);
            scityArrayList.clear();
            try {
                JSONObject job=new JSONObject(res);
                JSONArray jsonArray = job.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    scityArrayList.add(json_obj.getString("schoolcity"));
                }
                for(int i = 0; i< scityArrayList.size(); i++) {
                    Log.i("details--",scityArrayList.get(i));
                }
                Set<String> hs = new HashSet<>();
                hs.addAll(scityArrayList);
                scityArrayList.clear();
                scityArrayList.add("All");
                scityArrayList.addAll(hs);
                for(int i = 0; i< scityArrayList.size(); i++) {
                    Log.i("afterhashsetdetails--",scityArrayList.get(i));
                }
                cityAdapter = new ArrayAdapter<String>(SchoolListActivity.this, android.R.layout.simple_spinner_item, scityArrayList);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(cityAdapter);
                spin.setEnabled(true);
                //spin.setAdapter(cityAdapter);
                pb.setVisibility(View.GONE);
            } catch (Exception e){
                scityArrayList.clear();
                pb.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmap=photo;
            img.setImageBitmap(photo);
        }
    }
    private void uploadImage(final String imgname)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, uploadurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SchoolListActivity.this, "hii 2",Toast.LENGTH_LONG).show();   //toast
                        Log.e("response",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response=jsonObject.getString("response");
                            Toast.makeText(SchoolListActivity.this,Response,Toast.LENGTH_LONG).show();
                            img.setImageResource(0);
                        } catch (JSONException e) {
                            Toast.makeText(SchoolListActivity.this, "hii 3-"+e.getMessage().toString(),Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("resonse","error-"+error.getMessage());
                // Toast.makeText(MainActivity.this, "hii 4-"+error.getMessage().toString(),Toast.LENGTH_LONG).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",imgname.trim());
                params.put("image",imageToString(bitmap));

                return params;
            }
        };

        MySingleTon.getInstance(SchoolListActivity.this).addToRequestQue(stringRequest);

    }
    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imgBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
}