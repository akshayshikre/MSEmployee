package com.aks.mindspace.mindspace;

/**
 * Created by 123 on 10/16/2017.
 */

        import android.graphics.Bitmap;
        import android.graphics.drawable.BitmapDrawable;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Environment;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.util.ArrayList;

        import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList empArrayList;
    Picasso.Builder builder;
    public DataAdapter(ArrayList empArrayList) {
        this.empArrayList = empArrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_emplist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final emp e = (emp) empArrayList.get(position);
        builder = new Picasso.Builder(holder.empimg.getContext());
        builder.listener(new Picasso.Listener() {

            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                if (e.empgender.equals("female"))
                    Picasso.with(holder.empimg.getContext()).load(R.drawable.female).transform(new HomeActivity.CircleTransform()).into(holder.empimg);
                else if (e.empgender.equals("male"))
                    Picasso.with(holder.empimg.getContext()).load(R.drawable.male).transform(new HomeActivity.CircleTransform()).into(holder.empimg);
            }
        });

        holder.empname.setText(e.empname.toString());
        holder.empemailidno.setText(e.empemailid + " ; " + e.empno);
        holder.empiddep.setText(e.empdep + " : " + e.empid);
        Log.e("onbind","."+e.emprating+".");
        holder.emprating.setRating((float) Integer.parseInt(e.emprating));
       /* File file = new File(Environment.getExternalStorageDirectory() + "/mindspace/" + e.empid + ".jpg");
        if(file.exists()){
            System.out.println("file is already there");
            builder.build().load(file).transform(new HomeActivity.CircleTransform())
                    .into(holder.empimg,new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {

                        }
                    });
        }else*/{//System.out.println("NOO file is there");
            builder.build().load("http://" + serverip + "/img/" + e.empid + ".jpg").transform(new HomeActivity.CircleTransform())
                    .into(holder.empimg, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                          //  holder.saveimg(e.empid,holder.empimg.getDrawable());
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }
    }



    @Override
    public int getItemCount() {
        return empArrayList.size();
    }

    public void addItem(emp e) {
        empArrayList.add(e);
        notifyItemInserted(empArrayList.size());
    }

    public void removeItem(int position) {
        empArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, empArrayList.size());
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView empname, empemailidno, empiddep; RatingBar emprating;ImageView empimg;
        public ViewHolder(View view) {
            super(view);
            empname = (TextView)view.findViewById(R.id.rowname);
            empemailidno = (TextView)view.findViewById(R.id.rowemailno);
            empiddep = (TextView)view.findViewById(R.id.rowiddep);
            emprating = (RatingBar)view.findViewById(R.id.rowratingbar);
            empimg = (ImageView) view.findViewById(R.id.rowimg);
        }

        public void saveimg(String newid,Drawable drawable){

            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();;
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
            File file;
            file = new File( Environment.getExternalStorageDirectory() + "/mindspace/"+newid+".png");
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

        }
    }
}