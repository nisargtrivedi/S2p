package com.s2paa.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.s2paa.Activities.Gallery_;
import com.s2paa.Model.GallaryObjects;
import com.s2paa.Model.Multiple_Gallary;
import com.s2paa.R;
import com.s2paa.Utils.TTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 8/1/2017.
 */

public class GallaryVideoAdapter extends BaseAdapter {


    public List<Multiple_Gallary> list;
   // VideoView mSurfaceView;
    MediaController mMediaPlayer;
    Context context;
    LayoutInflater inflater;
    VideoView mSurfaceView;
    public class ViewHolder {

        public ImageView thumbnail;


        public ViewHolder(View view) {
            thumbnail =  view.findViewById(R.id.thumbnail);
        }
    }

    public GallaryVideoAdapter(Context context, List<Multiple_Gallary> list){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Multiple_Gallary getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Multiple_Gallary home_work = list.get(position);
        final ViewHolder holder;

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout._detail_page, parent, false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else
        {
            holder=(ViewHolder)convertView.getTag();
        }
        if(list.get(position).is_video==1) {
            Picasso.with(context).load(home_work.img_thumb).placeholder(R.drawable.loading).into(holder.thumbnail);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openVoideoPopup(home_work.img_urls);
                }
            });
        }
        return convertView;
    }

    private void openVoideoPopup(String url) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout._detail_page_two, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        mSurfaceView = (VideoView) view.findViewById(R.id.video);
        // final ProgressBar loading=(ProgressBar)view.findViewById(R.id.loading);
        mMediaPlayer = new MediaController(context);
        mMediaPlayer.setMediaPlayer( mSurfaceView);
        mMediaPlayer.setAnchorView(mSurfaceView);
        mSurfaceView.setMediaController(mMediaPlayer);
        mSurfaceView.setVideoURI(Uri.parse(url));

        //mSurfaceView.start();
        mSurfaceView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mSurfaceView.start();
                mMediaPlayer.show(900000000);
            }
        });


        mSurfaceView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                Toast.makeText(context, "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        mSurfaceView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(context, "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
        alertDialog.show();
    }

}
