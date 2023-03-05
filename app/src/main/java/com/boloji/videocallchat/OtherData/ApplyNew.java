package com.boloji.videocallchat.OtherData;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ApplyNew implements Observable.Transformer {
    public static final ApplyNew INSTANCE = new ApplyNew();

    private ApplyNew() {
    }

    @Override
    public final Object call(Object obj) {
        return ((Observable) obj).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }
}
