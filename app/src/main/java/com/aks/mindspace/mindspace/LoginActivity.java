package com.aks.mindspace.mindspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.aks.mindspace.mindspace.SplashActivity.Dept;
import static com.aks.mindspace.mindspace.SplashActivity.Email;
import static com.aks.mindspace.mindspace.SplashActivity.Gender;
import static com.aks.mindspace.mindspace.SplashActivity.Id;
import static com.aks.mindspace.mindspace.SplashActivity.Imagepath;
import static com.aks.mindspace.mindspace.SplashActivity.MyPREFERENCES;
import static com.aks.mindspace.mindspace.SplashActivity.Name;
import static com.aks.mindspace.mindspace.SplashActivity.Password;
import static com.aks.mindspace.mindspace.SplashActivity.Phoneno;
import static com.aks.mindspace.mindspace.SplashActivity.Rating;
import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class LoginActivity extends AppCompatActivity {
    EditText usernameField,passwordField;ProgressBar spinner;
    private int count=0;
    String empname="",empemailid="",empid="",empimg="",empdep="",password="",empno="",empgender="",emprating="";
    emp useremp;
/******************************************/
     class internetavailable extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
               Toast.makeText(getApplicationContext(),"Internet available",Toast.LENGTH_SHORT).show();
                new serveravailable().execute();
            } else {
                Toast.makeText(getApplicationContext(),"Internet is not available",Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return (isConnected(getApplicationContext()));
        }
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }
        return false;
    }
  /***********************************************************************************/
   class serveravailable extends AsyncTask<Void, Void, Boolean> {

      @Override
      protected void onPostExecute(Boolean aBoolean) {
          if (aBoolean) {
              Toast.makeText(getApplicationContext(),"Server available",Toast.LENGTH_SHORT).show();
              new get1empcode().execute(empemailid,password);
          } else {
              Toast.makeText(getApplicationContext(),"Server is not available",Toast.LENGTH_SHORT).show();
              spinner.setVisibility(View.GONE);
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
                URL url = new URL("http://"+serverip);   // Change to "http://google.com" for www  test.
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
  /************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameField= (EditText) findViewById(R.id.email);
        passwordField= (EditText) findViewById(R.id.password);
        spinner = (ProgressBar)findViewById(R.id.progressBar3);
        Button btn_login=(Button)findViewById(R.id.btn_login);
        Button btn_signup=(Button)findViewById(R.id.btn_signup);
        Button btn_reset_password=(Button)findViewById(R.id.btn_reset_password);

      btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                empemailid = usernameField.getText().toString();
                password = passwordField.getText().toString();
                spinner.setVisibility(View.VISIBLE);
               new serveravailable().execute();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_reset_password.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    class get1empcode extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            empemailid = arg0[0];
            password = arg0[1];
            String link="",data="";
            try{
                link="http://"+serverip+"/mindspace/get1emp.php";
                data  = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(empemailid, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(password, "UTF-8");

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
                Log.e("doinlogin",sb.toString());
                return sb.toString();
            } catch(Exception e){
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {


            Log.e("resut in post",res);

            try {
                JSONObject job = new JSONObject(res);
                JSONArray jsonArray = job.getJSONArray("result");

                if (jsonArray.length() == 1) {
                    JSONObject json_obj = jsonArray.getJSONObject(0);
                    empid = json_obj.getString("empid");
                    empname = json_obj.getString("empname");
                    empdep = json_obj.getString("empdep");
                    empimg = json_obj.getString("empimage");
                    empno = json_obj.getString("empno");
                    empgender = json_obj.getString("empgender");
                    emprating = json_obj.getString("emprating");
                }
            }catch(Exception e) {
                Log.e("loginerror",e.getMessage());
                Toast.makeText(LoginActivity.this,"Loginerror ="+e.getMessage(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                }
      /***Change****/  Toast.makeText(getApplicationContext(), "Welcome " + empname+empid+empdep+empimg+empno+empgender+emprating, Toast.LENGTH_LONG).show();

                if(!empname.trim().equals("<br />") && !empid.trim().equals("<br />") && !empdep.trim().equals("<br />")
                        && !empimg.trim().equals("<br />") && !empno.trim().equals("<br />")&& !empgender.trim().equals("<br />")
                        && !emprating.trim().equals("<br />")) {
                    //Toast.makeText(getApplicationContext(), "Welcome " + empname+empid+empdep+empimg+empno+empgender+emprating, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("empname", empname);
                    i.putExtra("empid", empid);
                    i.putExtra("empno", empno);
                    i.putExtra("empemailid", empemailid);
                    i.putExtra("empimg", empimg);
                    i.putExtra("empdep", empdep);
                    i.putExtra("empgender", empgender);
                    i.putExtra("emprating", emprating);
                    SharedPreferences sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    // Writing data to SharedPreferences
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Email, empemailid);
                    editor.putString(Password, password);
                    editor.putString(Name, empname);
                    editor.putString(Phoneno, empno);
                    editor.putString(Id, empid);
                    editor.putString(Imagepath, empimg);
                    editor.putString(Dept, empdep);
                    editor.putString(Gender, empgender);
                    editor.putString(Rating, emprating);
                    editor.commit();
                    spinner.setVisibility(View.GONE);
                    startActivity(i);
                    finish();
                }
                else Toast.makeText(getApplicationContext(), "Error data is not available", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
            }
        }
    }