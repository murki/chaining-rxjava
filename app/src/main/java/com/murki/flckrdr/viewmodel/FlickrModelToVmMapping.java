package com.murki.flckrdr.viewmodel;

import android.util.Log;

import com.murki.flckrdr.model.FlickrPhoto;
import com.murki.flckrdr.model.RecentPhotosResponse;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;
import rx.schedulers.Timestamped;

public class FlickrModelToVmMapping implements Func1<Timestamped<RecentPhotosResponse>, Timestamped<List<FlickrCardVM>>> {

    private static final String CLASSNAME = FlickrModelToVmMapping.class.getCanonicalName();
    private static volatile FlickrModelToVmMapping instance;

    public static FlickrModelToVmMapping instance() {
        if (instance == null) {
            instance = new FlickrModelToVmMapping();
        }
        return instance;
    }

    @Override
    public Timestamped<List<FlickrCardVM>> call(Timestamped<RecentPhotosResponse> recentPhotosResponse) {
        List<FlickrPhoto> photoList = recentPhotosResponse.getValue().photos.photo;
        Log.d(CLASSNAME, "FlickrModelToVmMapping.call() - Response list size=" + photoList.size());
        List<FlickrCardVM> flickrCardVMs = new ArrayList<>(photoList.size());
        for (FlickrPhoto photo : photoList) {
            flickrCardVMs.add(new FlickrCardVM(photo.title, photo.url_n));
        }
        return new Timestamped<>(recentPhotosResponse.getTimestampMillis(), flickrCardVMs);
    }
}
