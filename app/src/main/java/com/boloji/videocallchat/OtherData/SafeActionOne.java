package com.boloji.videocallchat.OtherData;

import rx.functions.Action1;


public final  class SafeActionOne implements Action1 {
    public static final SafeActionOne INSTANCE = new SafeActionOne();

    private SafeActionOne() {
    }

    @Override
    public final void call(Object obj) {
        ReSchedualCall.lambda$safeSubscribe$4(obj);
    }
}
