package com.aks.mindspace.mindspace;

/**
 * Created by 123 on 10/25/2017.
 */

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.aks.mindspace.mindspace.SplashActivity.serverip;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.MyViewHolder> {
    public void addItem(school e) {
        hlist.add(e);
        notifyItemInserted(hlist.size());
    }
    private ArrayList<school> hlist;
    Picasso.Builder builder;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sname, princiname, princino, sempname, sempdatetime,sreq,sarea;
        public ImageView img;
        public MyViewHolder(View view) {
            super(view);
            sname = (TextView) view.findViewById(R.id.sname);
            princiname = (TextView) view.findViewById(R.id.princiname);
            princino = (TextView) view.findViewById(R.id.princino);
            sempname = (TextView) view.findViewById(R.id.sempname);
            sempdatetime = (TextView) view.findViewById(R.id.sempdatetime);
            sreq = (TextView) view.findViewById(R.id.sreq);
            sarea=(TextView) view.findViewById(R.id.sarea);
            img=(ImageView) view.findViewById(R.id.imageView3);
        }
    }


    public SchoolAdapter(ArrayList<school> hlist) {
        this.hlist = hlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.srow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final school h = (school) hlist.get(position);
                holder.sname.setText(h.schoolname);
                holder.princiname.setText(h.princiname);
                holder.princino.setText(h.princino);
                holder.sempname.setText(h.empname);
                holder.sempdatetime.setText(h.empdate+" ; "+h.emptime);
                holder.sreq.setText(h.req);
                holder.sarea.setText(h.schoolarea);
        builder = new Picasso.Builder(holder.img.getContext());
        builder.listener(new Picasso.Listener() {

            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
                    Picasso.with(holder.img.getContext()).load(R.drawable.highschool).transform(new HomeActivity.CircleTransform()).into(holder.img);
           }
        });
                builder.build().load("http://" + serverip + "/mindspace/uploads/" + h.schoolid + ".jpg").transform(new HomeActivity.CircleTransform())
                        .into(holder.img, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                //  holder.saveimg(e.empid,holder.empimg.getDrawable());
                            }

                            @Override
                            public void onError() {

                            }
                        });
    }

    @Override
    public int getItemCount() {
        return hlist.size();
    }


}