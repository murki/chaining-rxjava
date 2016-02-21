package com.murki.flckrdr.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.murki.flckrdr.BuildConfig;
import com.squareup.picasso.Picasso;

public class FlickrCardVM {
    private final String title;
    private final String imageUrl;

    public FlickrCardVM(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso pic = Picasso.with(view.getContext());
//        if (BuildConfig.DEBUG) {
//            pic.setLoggingEnabled(true);
//        }
        pic.load(url).into(view);
    }
}
