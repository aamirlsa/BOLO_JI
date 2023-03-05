package com.boloji.videocallchat.OtherData;

import rx.functions.Action1;

public final  class SafeActionTwo implements Action1 {
    public static final SafeActionTwo INSTANCE = new SafeActionTwo();

    private SafeActionTwo() {
    }

    @Override
    public final void call(Object obj) {
        ReSchedualCall.lambda$safeSubscribe$5((Throwable) obj);
    }
}
