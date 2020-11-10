package com.s2paa.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.s2paa.Model.Multiple_Gallary;
import com.s2paa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GallayViewPager extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
   public List<Multiple_Gallary> banners;
    public static List<Multiple_Gallary> banners2;

    VideoView mSurfaceView;
    MediaController mMediaPlayer;

    public GallayViewPager(Context context, List<Multiple_Gallary> banners) {
        this.context = context;
        this.banners = banners;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        Multiple_Gallary event_img=banners.get(position);
        ImageView img;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_gallary_view, container,
                false);

        img = (ImageView) itemView.findViewById(R.id.img);

        img.setVisibility(View.GONE);
        if(event_img.is_video==0) {
            Picasso.with(context).load(event_img.img_urls).placeholder(R.drawable.loading).into(img);
            img.setVisibility(View.VISIBLE);
        }else{
            img.setVisibility(View.GONE);
        }
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }



//    private void openVoideoPopup(String url) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        final View view = inflater.inflate(R.layout._detail_page, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(view);
//        final AlertDialog alertDialog = builder.create();
//        mSurfaceView = (VideoView) view.findViewById(R.id.videoView);
//       // final ProgressBar loading=(ProgressBar)view.findViewById(R.id.loading);
//        mMediaPlayer = new MediaController(context);
//        mMediaPlayer.setMediaPlayer( mSurfaceView);
//        mMediaPlayer.setAnchorView(mSurfaceView);
//        mSurfaceView.setMediaController(mMediaPlayer);
//        mSurfaceView.setVideoURI(Uri.parse(url));
//
//        //mSurfaceView.start();
//        mSurfaceView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//
//                mSurfaceView.start();
//                mMediaPlayer.show(900000000);
//            }
//        });
//
//
//        mSurfaceView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//
//                Toast.makeText(context, "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
//            }
//        });
//        mSurfaceView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Toast.makeText(context, "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
//                return false;
//            }
//        });
//        alertDialog.show();
//    }

}
