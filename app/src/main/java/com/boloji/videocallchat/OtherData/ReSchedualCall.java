package com.boloji.videocallchat.OtherData;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

public class ReSchedualCall {
    static  void lambda$safeSubscribe$4(Object obj) {
    }

    static  void lambda$safeSubscribe$5(Throwable th) {
    }

    static  void lambda$safeSubscribe$6() {
    }

    public static <T> Observable.Transformer<T, T> applyLogicSchedulers() {
        return ApplyLogic.INSTANCE;
    }

    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return ApplyIoScheduler.INSTANCE;
    }

    public static <T> Observable.Transformer<T, T> applyNewThreadSchedulers() {
        return ApplyNew.INSTANCE;
    }

    public static <T> void onNext(Subscriber<T> subscriber, T t) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(t);
        }
    }

    public static void onError(Subscriber<?> subscriber, Exception exc) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onError(exc);
        }
    }

    public static void onCompleted(Subscriber<?> subscriber) {
        if (!subscriber.isUnsubscribed()) {
            subscriber.onCompleted();
        }
    }

    public static void onStop(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public static <T> Subscription aSyncTask(Observable.OnSubscribe<T> onSubscribe) {
        return aSyncTask(onSubscribe, null);
    }

    public static <T> Subscription aSyncTask(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1) {
        return aSyncTask(onSubscribe, action1, null);
    }

    public static <T> Subscription aSyncTask(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1, Action1<Throwable> action12) {
        return aSyncTask(onSubscribe, action1, action12, null);
    }

    public static <T> Subscription aSyncTask(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1, Action1<Throwable> action12, Action0 action0) {
        return safeSubscribe(Observable.create(onSubscribe).compose(applyLogicSchedulers()), action1, action12, action0);
    }

    public static <T> Subscription aSyncTaskNewThread(Observable.OnSubscribe<T> onSubscribe) {
        return aSyncTaskNewThread(onSubscribe, null);
    }

    public static <T> Subscription aSyncTaskNewThread(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1) {
        return aSyncTaskNewThread(onSubscribe, action1, null, null);
    }

    public static <T> Subscription aSyncTaskNewThread(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1, Action1<Throwable> action12) {
        return aSyncTaskNewThread(onSubscribe, action1, action12, null);
    }

    public static <T> Subscription aSyncTaskNewThread(Observable.OnSubscribe<T> onSubscribe, Action1<? super T> action1, Action1<Throwable> action12, Action0 action0) {
        return safeSubscribe(Observable.create(onSubscribe).compose(applyNewThreadSchedulers()), action1, action12, action0);
    }

    public static Subscription runOnUi(Action1<? super Object> action1) {
        return safeSubscribe(Observable.create(RunOnUI.INSTANCE).compose(applyLogicSchedulers()), action1);
    }

    public static <T> Subscription syncTask(Observable.OnSubscribe<T> onSubscribe) {
        return safeSubscribe(Observable.create(onSubscribe));
    }

    public static <T> Subscription safeSubscribe(Observable<T> observable) {
        return safeSubscribe(observable, null);
    }

    public static <T> Subscription safeSubscribe(Observable<T> observable, Action1<? super T> action1) {
        return safeSubscribe(observable, action1, null);
    }

    public static <T> Subscription safeSubscribe(Observable<T> observable, Action1<? super T> action1, Action1<Throwable> action12) {
        return safeSubscribe(observable, action1, action12, null);
    }

    public static <T> Subscription safeSubscribe(Observable<T> observable, Action1<? super T> action1, Action1<Throwable> action12, Action0 action0) {
        if (action1 == null) {
            action1 = SafeActionOne.INSTANCE;
        }
        if (action12 == null) {
            action12 = SafeActionTwo.INSTANCE;
        }
        if (action0 == null) {
            action0 = SafeAction.INSTANCE;
        }
        return observable.subscribe(action1, action12, action0);
    }
}
