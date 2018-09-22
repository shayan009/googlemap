package com.recharge.demomap.util.pager_adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.natit.hotedin.R;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
List<String> images=new ArrayList<>();
    public ImageSliderPagerAdapter(Context context) {
        this.context = context;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_image_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(context)
                .load(getImages().get(position))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.default_image2)
                        .error(R.drawable.default_image)
                ).into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
