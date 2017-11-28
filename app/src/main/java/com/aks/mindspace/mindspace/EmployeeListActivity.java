package com.aks.mindspace.mindspace;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import static com.aks.mindspace.mindspace.SplashActivity.Dept;
import static com.aks.mindspace.mindspace.SplashActivity.MyPREFERENCES;
import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class EmployeeListActivity extends AppCompatActivity implements View.OnClickListener{
    public ArrayList<emp> empArrayList=new ArrayList<emp>();
    private DataAdapter adapter;
    private RecyclerView recyclerView;
    private AlertDialog.Builder alertDialog,alertDialog2;
    private EditText edid,edname,edemail,edno,edpassword;
    private RatingBar edratingbar;
    private Spinner eddepspinner,edgenderspinner;
    private static ProgressBar spinner;
    private static boolean intrnt=false,srvr=false;
    private int edit_position;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    private String upempid="", upempname="", upempemailid="", upempdep="",
             upempno="", upempgender="",
            upemprating="",preemailid="",
            addempid="", addempname="", addempemailid="", addempdep="", addpassword="", addempno="", addempgender="",
            addemprating="" ;
    private DialogInterface updatedialog,adddialog;
    private DialogInterface deletedialog;
    private int deleteposition=0;
    private String deleteempemailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        spinner= (ProgressBar) findViewById(R.id.emplistprogressbar);
        //new internetavailable().execute();
        initViews();
        if(getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).getString(Dept,"").toLowerCase().equals("admin")) {
            initSwipe();
            initDialog();
        }


    }

    private void initViews(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(getSharedPreferences(MyPREFERENCES,MODE_PRIVATE).getString(Dept,"").toLowerCase().equals("admin"))
        {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        }
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        File mindspaceDirectory = new File(Environment.getExternalStorageDirectory() + "/mindspace/");
        mindspaceDirectory.mkdirs();

        new getempcode().execute();
       // spinner.setVisibility(View.INVISIBLE);
       // new getempcode().execute();
    }
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    alertDialog2.setTitle("Remove Employee?");
                    alertDialog2.setCancelable(false);
                    if(empArrayList.get(position).empgender.equals("male"))
                        alertDialog2.setMessage("Are you really want to remove\n Mr."+empArrayList.get(position).empname+"?");
                    else if (empArrayList.get(position).empgender.equals("female"))
                        alertDialog2.setMessage("Are you really want to remove\n Mrs."+empArrayList.get(position).empname+"?");
                        alertDialog2.setCancelable(false);
                        alertDialog2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        deleteposition=position;
                        deleteempemailid=empArrayList.get(position).empemailid;
                        Log.e("onswipe","calling deleteemp");
                        new deleteempcode().execute();
                        }

                    });
                    alertDialog2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deletedialog.dismiss();
                        }

                    });
                    alertDialog2.show();
                } else {
                    removeView();
                    preemailid=empArrayList.get(position).empemailid;
                    edit_position = position;
                    alertDialog.setTitle("Edit Data");
                    edid.setText(empArrayList.get(position).empid.toString());
                    eddepspinner.setSelection(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Department))).indexOf(empArrayList.get(position).empdep.toString()));
                    edname.setText(empArrayList.get(position).empname.toString());
                    edemail.setText(empArrayList.get(position).empemailid.toString());
                    edno.setText(empArrayList.get(position).empno.toString());
                    edgenderspinner.setSelection(new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.Gender))).indexOf(empArrayList.get(position).empgender.toString()));
                   // Picasso.with(EmployeeListActivity.this).load(new File(Environment.getExternalStorageDirectory() + "/mindspace/" + empArrayList.get(position).empid.toString() + ".jpg")).transform(new HomeActivity.CircleTransform()).into(edimgbtn);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.deleteicon);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
    private void initDialog(){
        alertDialog2= new AlertDialog.Builder(this);
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(add){
                    alertDialog.setCancelable(false);
                    add =false;
                    addempid=edid.getText().toString(); addempname=edname.getText().toString(); addempemailid=edemail.getText().toString();
                    addempdep=eddepspinner.getSelectedItem().toString(); addpassword=edpassword.getText().toString(); addempno=edno.getText().toString();
                    addempgender=edgenderspinner.getSelectedItem().toString(); addemprating=(String.valueOf((int)edratingbar.getRating()));
                    adddialog=dialog;
                    Log.e("onclickdialog","calling addempcode");
                    new addempcode().execute();
                } else {
                    alertDialog.setCancelable(false);
                    upempid=edid.getText().toString(); upempname=edname.getText().toString(); upempemailid=edemail.getText().toString();
                    upempdep=eddepspinner.getSelectedItem().toString();upempno=edno.getText().toString(); upempgender=edgenderspinner.getSelectedItem().toString();
                    upemprating=(String.valueOf((int)edratingbar.getRating()));
                    updatedialog=dialog;
                    Log.e("onclickdialog","calling updateempcode");
                    new updateempcode().execute();
                }

            }
        });
                edid = (EditText)view.findViewById(R.id.edid);
                eddepspinner = (Spinner) view.findViewById(R.id.eddepspinner);
                edname = (EditText)view.findViewById(R.id.edname);
                edemail = (EditText)view.findViewById(R.id.edemail);
                edno = (EditText)view.findViewById(R.id.edno);
                edpassword = (EditText)view.findViewById(R.id.edpass);
                edgenderspinner = (Spinner) view.findViewById(R.id.edgenderspinner);
                edratingbar=(RatingBar) view.findViewById(R.id.edratingbar);
                edpassword.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fab:
                removeView();
                add = true;
                alertDialog.setTitle("Add Emplyee");
                        edid.setText("");
                        edname.setText("");
                        edemail.setText("");
                        edno.setText("");
                        edratingbar.setRating(1.0f);
                        edpassword.setText("");
                        edpassword.setVisibility(View.VISIBLE);
                alertDialog.show();
                break;
        }
    }
    class deleteempcode extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+serverip+"/mindspace/deleteemp.php";
                data  = URLEncoder.encode("emailid", "UTF-8") + "=" +
                        URLEncoder.encode(deleteempemailid, "UTF-8");
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
                Log.e("delete","."+sb.toString()+".");
                return sb.toString();
            } catch(Exception e){
                Log.e("delete",e.getMessage().toString());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                Toast.makeText(EmployeeListActivity.this,res,Toast.LENGTH_SHORT).show();
                emp e;
                if(res.equals(" Record deleted successfully")) {
                    Log.e("delete","success"+res);
                    adapter.removeItem(deleteposition);
                    deletedialog.dismiss();
                    //Intent in =new Intent(EmployeeListActivity.this,EmployeeListActivity.class);
                    //startActivity(in);
                    //finish();
                }
                deletedialog.dismiss();
                for(int i=0;i<empArrayList.size();i++) {
                    e=empArrayList.get(i);
                    Log.e("deletedetails--",e.empid+e.empname+e.empemailid+e.empdep+e.empimage+e.empgender+e.empno+e.emprating);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    class updateempcode extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+serverip+"/mindspace/updateemp.php";
                data  = URLEncoder.encode("empid", "UTF-8") + "=" +
                        URLEncoder.encode(upempid, "UTF-8");
                data += "&" + URLEncoder.encode("empname", "UTF-8") + "=" +
                        URLEncoder.encode(upempname, "UTF-8");
                data += "&" + URLEncoder.encode("emailid", "UTF-8") + "=" +
                        URLEncoder.encode(upempemailid, "UTF-8");
                data += "&" + URLEncoder.encode("empdep", "UTF-8") + "=" +
                        URLEncoder.encode(upempdep, "UTF-8");
                data += "&" + URLEncoder.encode("empno", "UTF-8") + "=" +
                        URLEncoder.encode(upempno, "UTF-8");
                data += "&" + URLEncoder.encode("empgender", "UTF-8") + "=" +
                        URLEncoder.encode(upempgender, "UTF-8");
                data += "&" + URLEncoder.encode("emprating", "UTF-8") + "=" +
                        URLEncoder.encode(upemprating, "UTF-8");
                data += "&" + URLEncoder.encode("preemailid", "UTF-8") + "=" +
                        URLEncoder.encode(preemailid, "UTF-8");
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
                Log.e("update","."+sb.toString()+".");
                return sb.toString();
            } catch(Exception e){
                Log.e("update",e.getMessage().toString());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                Toast.makeText(EmployeeListActivity.this,res,Toast.LENGTH_SHORT).show();
                emp e;
                if(res.equals(" Record updated successfully")) {
                    Log.e("update","success"+res);
                    empArrayList.set(edit_position, new emp(
                            edid.getText().toString(), edname.getText().toString(), edemail.getText().toString(),
                            eddepspinner.getSelectedItem().toString(), "default", edno.getText().toString(),
                            edgenderspinner.getSelectedItem().toString(), (String.valueOf((int) edratingbar.getRating()))));
                    adapter.notifyDataSetChanged();
                 //   Intent in =new Intent(EmployeeListActivity.this,EmployeeListActivity.class);
                   // startActivity(in);
                    //finish();
                }
                edpassword.setVisibility(View.GONE);
                updatedialog.dismiss();
                for(int i=0;i<empArrayList.size();i++) {
                    e=empArrayList.get(i);
                    Log.e("updatedetails--",e.empid+e.empname+e.empemailid+e.empdep+e.empimage+e.empgender+e.empno+e.emprating);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    class addempcode extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+serverip+"/mindspace/addemp.php";
                data  = URLEncoder.encode("empid", "UTF-8") + "=" +
                        URLEncoder.encode(addempid, "UTF-8");
                data += "&" + URLEncoder.encode("empname", "UTF-8") + "=" +
                        URLEncoder.encode(addempname, "UTF-8");
                data += "&" + URLEncoder.encode("empemailid", "UTF-8") + "=" +
                        URLEncoder.encode(addempemailid, "UTF-8");
                data += "&" + URLEncoder.encode("empdep", "UTF-8") + "=" +
                        URLEncoder.encode(addempdep, "UTF-8");
                data += "&" + URLEncoder.encode("empno", "UTF-8") + "=" +
                        URLEncoder.encode(addempno, "UTF-8");
                data += "&" + URLEncoder.encode("empgender", "UTF-8") + "=" +
                        URLEncoder.encode(addempgender, "UTF-8");
                data += "&" + URLEncoder.encode("emprating", "UTF-8") + "=" +
                        URLEncoder.encode(addemprating, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(addpassword, "UTF-8");

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
                Log.e("add","."+sb.toString()+".");
                return sb.toString();
            } catch(Exception e){
                Log.e("add",e.getMessage().toString());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                Toast.makeText(EmployeeListActivity.this,res,Toast.LENGTH_SHORT).show();
                emp e;
                if(res.trim().equals("Employee record inserted successfully")) {
                    Log.e("add","success"+ res);
                    emp newemp=new emp(
                            edid.getText().toString(), edname.getText().toString(), edemail.getText().toString(),
                            eddepspinner.getSelectedItem().toString(), "default", edno.getText().toString(),
                            edgenderspinner.getSelectedItem().toString(), (String.valueOf((int) edratingbar.getRating())));
                    adapter.addItem(newemp);
                    updatedialog.dismiss();
                   // Intent in =new Intent(EmployeeListActivity.this,EmployeeListActivity.class);
                   // startActivity(in);
                   // finish();
                }
                edpassword.setVisibility(View.GONE);
                adddialog.dismiss();
                for(int i=0;i<empArrayList.size();i++) {
                    e=empArrayList.get(i);
                    Log.e("adddetails--",e.empid+e.empname+e.empemailid+e.empdep+e.empimage+e.empgender+e.empno+e.emprating);
                }


            } catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    class getempcode extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String link="",data="";
            try{
                link="http://"+serverip+"/mindspace/getemp.php";

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
                Log.e("getemp",sb.toString());
                return sb.toString();

            } catch(Exception e){
                Log.e("getemp",e.getMessage());
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                JSONObject job=new JSONObject(res);
                JSONArray jsonArray = job.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_obj = jsonArray.getJSONObject(i);
                    empArrayList.add(new emp(json_obj.getString("empid"),json_obj.getString("empname"),
                            json_obj.getString("empemailid"),json_obj.getString("empdep"),
                            json_obj.getString("empimage"),json_obj.getString("empno"),
                            json_obj.getString("empgender"),json_obj.getString("emprating")
                    ));
                }
                emp e;
                for(int i=0;i<empArrayList.size();i++) {
                    e=empArrayList.get(i);
                    Log.e("details--",e.empid+e.empname+e.empemailid+e.empdep+e.empimage+e.empgender+e.empno+e.emprating);
                }

                adapter = new DataAdapter(empArrayList);
                recyclerView.setAdapter(adapter);
              // adapter.notifyDataSetChanged();
               /* for(int i=0;i<empArrayList.size();i++){
                   // new exists().execute(empArrayList.get(i).empid);
                   // imageDownload(EmployeeListActivity.this,empArrayList.get(i).empid);
                    Picasso.with(EmployeeListActivity.this)
                            .load("http://" + serverip + "/img/" + empArrayList.get(i).empid + ".jpg")
                            .into(newgetTarget(empArrayList.get(i).empid));
                }*/
                adapter.notifyDataSetChanged();

            } catch (Exception e){
                e.printStackTrace();
            }

        }
        }






    //target to save
    private static Target newgetTarget(final String userid){
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
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("onBitmapFailed",userid);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }
}


 /*public void choose(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        final int ACTIVITY_SELECT_IMAGE = 1234;
        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 1234:
                if(resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    imageDownload(this, edid.getText().toString());
            // Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want!
                }
        }

    }*/

    /*public void loadlocal(){
        File file = new File(Environment.getExternalStorageDirectory() + "/mindspace/" + empid + ".jpg");
        spinner.setVisibility(View.VISIBLE);
        if (file.exists()) {
            Log.i("load local file dere ",file.getPath());
            Picasso.with(HomeActivity.this).load(file).transform(new HomeActivity.CircleTransform())
                    .into(nav_img, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {spinner.setVisibility(View.INVISIBLE); }
                        @Override
                        public void onError() {
                            if (empgender.equals("female"))
                                Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new HomeActivity.CircleTransform()).into(nav_img);
                            else if (empgender.equals("male"))
                                Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new HomeActivity.CircleTransform()).into(nav_img);
                            spinner.setVisibility(View.INVISIBLE); }
                    });
        } else {
            Log.i("load local No file ",file.getPath());
            if (empgender.equals("female"))
                Picasso.with(HomeActivity.this).load(R.drawable.female).transform(new HomeActivity.CircleTransform()).into(nav_img);
            else if (empgender.equals("male"))
                Picasso.with(HomeActivity.this).load(R.drawable.male).transform(new HomeActivity.CircleTransform()).into(nav_img);
            spinner.setVisibility(View.INVISIBLE);
        }

    }*/



    /*
    class internetavailable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Log.i("Internet","Internet is available");intrnt=true;
                new serveravailable().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Internet is not available", Toast.LENGTH_SHORT).show();
                //new getempcode().execute();
                // loadlocal();
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
                Log.i("server","server is available");srvr=true;
                new getempcode().execute();

            } else {
                Toast.makeText(getApplicationContext(),"Server is not available",Toast.LENGTH_SHORT).show();
                //new getempcode().execute();
                //loadlocal();
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return (isURLReachable(getApplicationContext()));
        }
    }

    //save image
    public static void imageDownload(Context ctx,String userid){
        File fdelete = new File(Environment.getExternalStorageDirectory() + "/mindspace/" + userid + ".jpg");
        if (fdelete.exists() && intrnt && srvr) {
            Log.i("file exists", String.valueOf(fdelete.exists())+fdelete.getPath());
            if (fdelete.delete()) {
                Log.i("file Deleted :" , fdelete.getPath());
                Picasso.with(ctx)
                        .load("http://" + serverip + "/img/" + userid + ".jpg")
                        .into(getTarget(userid));
            } else {
                Log.i("file not Deleted :" , fdelete.getPath());
                Picasso.with(ctx)
                        .load("http://" + serverip + "/img/" + userid + ".jpg")
                        .into(getTarget(userid));
            }
        }
        else {
            Log.i("in imagedownload","file directly saved"+fdelete.getPath());
            Picasso.with(ctx)
                    .load("http://" + serverip + "/img/" + userid + ".jpg")
                    .into(getTarget(userid));

        }
        /*Picasso.with(ctx)
                .load("http://" + serverip + "/img/" + userid + ".jpg")
                .into(getTarget(userid));
    }

    //target to save
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
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("onBitmapFailed",userid);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    private class exists extends AsyncTask<String, Void, Boolean> {

        String localuserid="";
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... params) {
            localuserid=params[0];
            try {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con =  (HttpURLConnection)
                        new URL("http://" + serverip + "/img/" + params[0] + ".jpg").openConnection();
                con.setRequestMethod("HEAD");
                System.out.println(con.getResponseCode());
                if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                Log.i("existsdoinback", "true");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            boolean bResponse = result;
            if (bResponse==true) {
                if(!localuserid.equals(""))
                imageDownload(EmployeeListActivity.this,localuserid);
            }
            else
            {
               Log.i("existspostex","not present"+localuserid);
            }
        }
    }*/
