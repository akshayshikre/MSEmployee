package com.aks.mindspace.mindspace;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class AddEmplyeeActivity extends AppCompatActivity{
    String empnameadd="",empidadd="",empdepadd="",empemailadd="",empimgadd="",empnoadd="";
TextView empnameaddtv, empidaddtv, empdepaddtv, empemailaddtv, empnoaddtv;
ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emplyee);
        empnameaddtv= (TextView) findViewById(R.id.empnameadd);
        empidaddtv= (TextView) findViewById(R.id.empidadd);
        empdepaddtv= (TextView) findViewById(R.id.empdepadd);
        empemailaddtv= (TextView) findViewById(R.id.empemailadd);
        empnoaddtv= (TextView) findViewById(R.id.empnoadd);
        spinner= (ProgressBar) findViewById(R.id.progressBaradd);
        spinner.setVisibility(View.GONE);
    }



    public void addmethod(View view) {
        empnameadd=empnameaddtv.getText().toString();empidadd=empidaddtv.getText().toString();empdepadd=empdepaddtv.getText().toString();
        empemailadd=empemailaddtv.getText().toString();empimgadd="default";empnoadd=empnoaddtv.getText().toString();
        spinner.setVisibility(View.VISIBLE);
        new empaddcode().execute();
    }


    class empaddcode extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            spinner.setProgress(spinner.getProgress() + 1);
        }

        @Override
        protected String doInBackground(Void... values) {
            String link = "", data = "";
            try {
                link = "http://"+serverip+"/mindspace/empadd.php";
                data = URLEncoder.encode("empname", "UTF-8") + "=" +
                        URLEncoder.encode(empnameadd, "UTF-8");
                data += "&" +URLEncoder.encode("empid", "UTF-8") + "=" +
                        URLEncoder.encode(empidadd, "UTF-8");
                data += "&" +URLEncoder.encode("empemail", "UTF-8") + "=" +
                        URLEncoder.encode(empemailadd, "UTF-8");
                data += "&" +URLEncoder.encode("empdep", "UTF-8") + "=" +
                        URLEncoder.encode(empdepadd, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode("c", "UTF-8");
                data += "&" + URLEncoder.encode("empimg", "UTF-8") + "=" +
                        URLEncoder.encode(empimgadd, "UTF-8");
                data += "&" + URLEncoder.encode("empno", "UTF-8") + "=" +
                        URLEncoder.encode(empnoadd, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
    }
}
