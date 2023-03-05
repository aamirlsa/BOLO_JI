package com.boloji.videocallchat.OtherData;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public final class ApplyIoScheduler implements Observable.Transformer {
    public static final ApplyIoScheduler INSTANCE = new ApplyIoScheduler();

    private ApplyIoScheduler() {
    }

    @Override
    public final Object call(Object obj) {
        return ((Observable) obj).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
