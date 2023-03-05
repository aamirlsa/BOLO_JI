package com.boloji.videocallchat.OtherData;

import rx.functions.Action0;


public final class SafeAction implements Action0 {
    public static final SafeAction INSTANCE = new SafeAction();

    private SafeAction() {
    }

    @Override
    public final void call() {
        ReSchedualCall.lambda$safeSubscribe$6();
    }
}
