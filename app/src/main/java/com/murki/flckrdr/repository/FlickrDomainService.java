package com.murki.flckrdr.repository;

import android.content.Context;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.murki.flckrdr.ITimestampedView;
import com.murki.flckrdr.model.RecentPhotosResponse;
import com.murki.flckrdr.viewmodel.FlickrApiToVmMapping;
import com.murki.flckrdr.viewmodel.FlickrCardVM;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

public class FlickrDomainService {

    private static final String CLASSNAME = FlickrDomainService.class.getCanonicalName();

    private final FlickrApiRepository flickrApiRepository;
    private final FlickrDiskRepository flickrDiskRepository;

    public FlickrDomainService(Context context) {
        flickrApiRepository = new FlickrApiRepository(); // TODO: Make Injectable Singleton
        flickrDiskRepository = new FlickrDiskRepository(context); // TODO: Make Injectable Singleton
    }

    @RxLogObservable
    public Observable<Timestamped<List<FlickrCardVM>>> getRecentPhotos(ITimestampedView timestampedView) {
        return getMergedPhotos()
                .filter(getRecentPhotosFilter(timestampedView))
                .map(FlickrApiToVmMapping.instance());
    }

    @RxLogObservable
    private Observable<Timestamped<RecentPhotosResponse>> getMergedPhotos() {
        return Observable.merge(
                flickrDiskRepository.getRecentPhotos().subscribeOn(Schedulers.io()),
                flickrApiRepository.getRecentPhotos().timestamp().doOnNext(new Action1<Timestamped<RecentPhotosResponse>>() {
                    @Override
                    public void call(Timestamped<RecentPhotosResponse> recentPhotosResponse) {
                        Log.d(CLASSNAME, "Frodo (!) => flickrApiRepository.getRecentPhotos().doOnNext() - Saving photos to disk - thread=" + Thread.currentThread().getName());
                        flickrDiskRepository.savePhotos(recentPhotosResponse); // TODO: Make it work with chained Rx Observables
                    }
                }).subscribeOn(Schedulers.io())
        );
    }

    private Func1<Timestamped<RecentPhotosResponse>, Boolean> getRecentPhotosFilter(final ITimestampedView timestampedView) {
        return new Func1<Timestamped<RecentPhotosResponse>, Boolean>() {
            @Override
            public Boolean call(Timestamped<RecentPhotosResponse> recentPhotosResponseTimestamped) {

                StringBuilder logMessage = new StringBuilder("Frodo (!) => getMergedPhotos() - Filtering results");
                if (recentPhotosResponseTimestamped == null) {
                    logMessage.append(", recentPhotosResponseTimestamped is null");
                } else {
                    logMessage.append(", timestamps=").append(recentPhotosResponseTimestamped.getTimestampMillis()).append(">").append(timestampedView.getViewDataTimestampMillis()).append("?");
                }
                logMessage.append(", thread=").append(Thread.currentThread().getName());
                Log.d(CLASSNAME, logMessage.toString());

                // filter it
                // if result is null - ignore it
                // if timestamp of new arrived (emission) data is less than timestamp of already displayed data — ignore it.
                return recentPhotosResponseTimestamped != null
                        && recentPhotosResponseTimestamped.getValue() != null
                        && recentPhotosResponseTimestamped.getTimestampMillis() > timestampedView.getViewDataTimestampMillis();
            }
        };
    }
}
