package com.boloji.videocallchat.OtherData;

import rx.Observable;
import rx.Subscriber;


public final class RunOnUI implements Observable.OnSubscribe {
    public static final RunOnUI INSTANCE = new RunOnUI();

    private RunOnUI() {
    }

    @Override
    public final void call(Object obj) {
        ((Subscriber) obj).onNext(true);
    }
}
