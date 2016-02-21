package com.murki.flckrdr;

import android.support.annotation.IntDef;
import android.util.SparseArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;

// TODO: Add cache eviction policy
// TODO: Make collection + generic
// TODO: Make thread-safe
public enum ObservableSingletonManager {
    INSTANCE {};

    @IntDef({FLICKR_GET_RECENT_PHOTOS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceMethod {}

    public static final int FLICKR_GET_RECENT_PHOTOS = 0;

    private SparseArray<Observable> inMemoryCache = new SparseArray<>();

    @SuppressWarnings("unchecked")
    public <T extends Observable> T getObservable(@ServiceMethod int key) {
        return (T) inMemoryCache.get(key);
    }

    public void putObservable(@ServiceMethod int key, Observable obs) {
        inMemoryCache.put(key, obs);
    }

    public void removeObservable(@ServiceMethod int key) {
        inMemoryCache.delete(key);
    }

}