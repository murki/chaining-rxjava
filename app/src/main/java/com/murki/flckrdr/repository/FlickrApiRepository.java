package com.murki.flckrdr.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.murki.flckrdr.model.RecentPhotosResponse;

import retrofit.MoshiConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import rx.Observable;

public class FlickrApiRepository {

    private static final String CLASSNAME = FlickrApiRepository.class.getCanonicalName();
    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "";
    private static IFlickrAPI flickrAPI;

    public FlickrApiRepository() {
        if (flickrAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            flickrAPI = retrofit.create(IFlickrAPI.class);
        }
    }

    @RxLogObservable
    public Observable<RecentPhotosResponse> getRecentPhotos() {
        return flickrAPI.getRecentPhotos();
    }

    private interface IFlickrAPI {
        @GET("?method=flickr.photos.getRecent&format=json&nojsoncallback=1&extras=url_n&api_key=" + API_KEY)
        Observable<RecentPhotosResponse> getRecentPhotos();
    }
}
