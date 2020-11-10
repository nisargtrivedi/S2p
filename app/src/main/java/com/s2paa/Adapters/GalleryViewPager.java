package com.s2paa.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.s2paa.Model.EventGallery;
import com.s2paa.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GalleryViewPager extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    List<EventGallery> banners;

    public GalleryViewPager(Context context, List<EventGallery> banners) {
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
        EventGallery event_img=banners.get(position);
        ImageView img;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.image_gallary_view, container,
                false);

     //   txttext = (TextView) itemView.findViewById(R.id.textView);
     //   txttext.setText(banners.get(position).area);

       // AppLogger.info("Banner title:" + banners.get(position).title);

        img = (ImageView) itemView.findViewById(R.id.img);
        Picasso.with(context).load(event_img.img_urls).placeholder(R.drawable.progress_animation).into(img);


        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);
    }


}
