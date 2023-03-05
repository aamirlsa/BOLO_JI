package com.boloji.videocallchat.OtherData;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final  class ApplyLogic implements Observable.Transformer {
    public static final ApplyLogic INSTANCE = new ApplyLogic();

    private ApplyLogic() {
    }

    @Override
    public final Object call(Object obj) {
        return ((Observable) obj).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }
}
