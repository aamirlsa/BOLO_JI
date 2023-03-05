package com.boloji.videocallchat.InternetConnection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.boloji.videocallchat.OtherActivities.AppCheck;
import com.boloji.videocallchat.WiFiConnection.NetwrokRec;
import com.boloji.videocallchat.WiFiConnection.WiFiRec;
import com.boloji.videocallchat.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

public class NetworkDialog extends Dialog implements View.OnClickListener, ConnectListener, NetworkCallBackListner {

    private static final long ANIMATION_DELAY = 800;
    private static final long ANIMATION_DURATION = 1500;
    private static final float FLIGHT_BACK_END = -400.0f;
    private static final float FLIGHT_BACK_START = 1000.0f;
    private static final long FLIGHT_DURATION = 2500;
    private static final float FLIGHT_THERE_END = 1300.0f;
    private static final float FLIGHT_THERE_START = -200.0f;
    private static final float GHOST_SCALE_ANIMATION_VALUE = 1.3f;
    private static final float GHOST_X_ANIMATION_VALUE = 320.0f;
    private static final float GHOST_Y_ANIMATION_VALUE = -100.0f;

    private static final float RADIUS = 12.0f;
    private NetworkCallBackListner ConnectCallBackListener;
    private Button airplaneOff;
    private int bgGradientCenter;
    private int bgGradientEnd;
    private int bgGradientOrientation;
    private int bgGradientStart;
    private int bgGradientType;
    private int buttonColor;
    private int buttonIconsColor;
    private int buttonTextColor;
    private boolean cancelable;
    private ImageView close;
    private float dialogRadius;
    private int direction;
    private ImageView ghost;
    private boolean isHalloween;
    private boolean isWifiOn;
    private Typeface messageTypeface;
    private Button mobileOn;
    private ImageView moon;
    private NetwrokRec networkStatusReceiver;
    private TextView noInternet;
    private TextView noInternetBody;
    private ImageView plane;
    private FrameLayout root;
    private Typeface titleTypeface;
    private ImageView tomb;
    private Guideline topGuide;
    private TextView turnOn;
    private ObjectAnimator wifiAnimator;
    private ImageView wifiIndicator;
    private int wifiLoaderColor;
    private ProgressBar wifiLoading;
    private Button wifiOn;
    private WiFiRec wifiReceiver;

    @Retention(RetentionPolicy.RUNTIME)
    @interface Orientation {
    }

    @Override
    public void onWifiTurnedOff() {
    }

    private NetworkDialog(Context context, int i, int i2, int i3, int i4, int i5, float f, Typeface typeface, Typeface typeface2, int i6, int i7, int i8, int i9, boolean z) {
        super(context);
        int i10;
        int i11;
        int i12;
        int i13;
        int i14 = i4;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        boolean equals = NetworkUtils.getCurrentDate().equals("10-31");
        this.isHalloween = equals;
        if (i == 0) {
            i10 = equals ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradStartH) : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradStart);
        } else {
            i10 = i;
        }
        this.bgGradientStart = i10;
        if (i2 == 0) {
            i11 = this.isHalloween ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenterH) : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenter);
        } else {
            i11 = i2;
        }
        this.bgGradientCenter = i11;
        if (i3 == 0) {
            i12 = this.isHalloween ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradEndH) : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradEnd);
        } else {
            i12 = i3;
        }
        this.bgGradientEnd = i12;
        this.bgGradientOrientation = (i14 < 10 || i14 > 17) ? 10 : i14;
        this.bgGradientType = (i5 <= 0 || i5 > 2) ? 0 : i5;
        this.dialogRadius = f == 0.0f ? RADIUS : f;
        if (f == -1.0f) {
            this.dialogRadius = 0.0f;
        }
        this.titleTypeface = typeface;
        this.messageTypeface = typeface2;
        if (i6 == 0) {
            i13 = this.isHalloween ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenterH) : ContextCompat.getColor(getContext(), R.color.colorWhite);
        } else {
            i13 = i6;
        }
        this.buttonColor = i13;
        this.buttonTextColor = i7 == 0 ? ContextCompat.getColor(getContext(), R.color.lightGrey) : i7;
        this.buttonIconsColor = i8 == 0 ? ContextCompat.getColor(getContext(), R.color.lightGrey) : i8;
        this.wifiLoaderColor = i9 == 0 ? ContextCompat.getColor(getContext(), R.color.lightGrey) : i9;
        this.cancelable = z;
        initReceivers(context);
    }

    private void initReceivers(Context context) {
        WiFiRec wifiReceiver2 = new WiFiRec();
        this.wifiReceiver = wifiReceiver2;
        context.registerReceiver(wifiReceiver2, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        NetwrokRec networkStatusReceiver2 = new NetwrokRec();
        this.networkStatusReceiver = networkStatusReceiver2;
        context.registerReceiver(networkStatusReceiver2, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.wifiReceiver.setConnectionListener(this);
        this.networkStatusReceiver.setConnectionCallback(this);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.interner_dialog);
        initMainWindow();
        initView();
        initGuideLine();
        initBackground();
        initButtonStyle();
        initListeners();
        initAnimations();
        initTypefaces();
        initWifiLoading();
        initClose();
    }

    private void initMainWindow() {
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    private void initView() {
       root = findViewById(R.id.root);
       close =  findViewById(R.id.close);
       moon =  findViewById(R.id.moon);
       plane =  findViewById(R.id.plane);
       ghost =  findViewById(R.id.ghost);
       tomb =  findViewById(R.id.tomb);
       wifiIndicator =  findViewById(R.id.wifi_indicator);
       noInternet = findViewById(R.id.no_internet);
       noInternetBody = findViewById(R.id.no_internet_body);
       turnOn = findViewById(R.id.turn_on);
       wifiOn =  findViewById(R.id.wifi_on);
       mobileOn =  findViewById(R.id.mobile_on);
       airplaneOff =  findViewById(R.id.airplane_off);
       wifiLoading = findViewById(R.id.wifi_loading);
       topGuide =  findViewById(R.id.top_guide);
    }

    private void initGuideLine() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.topGuide.getLayoutParams();
        layoutParams.guidePercent = this.isHalloween ? 0.34f : 0.3f;
        this.topGuide.setLayoutParams(layoutParams);
    }

    private void initBackground() {
        GradientDrawable gradientDrawable = new GradientDrawable(getOrientation(), new int[]{this.bgGradientStart, this.bgGradientCenter, this.bgGradientEnd});
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(this.dialogRadius);
        int i = this.bgGradientType;
        if (i == 1) {
            gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        } else if (i != 2) {
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        } else {
            gradientDrawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        }
        if (this.isHalloween) {
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setGradientRadius((float) (getContext().getResources().getDimensionPixelSize(R.dimen.dialog_height) / 2));
        } else {
            gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            this.root.setBackground(gradientDrawable);
        } else {
            this.root.setBackgroundDrawable(gradientDrawable);
        }
    }

    private void initButtonStyle() {
        this.wifiOn.getBackground().mutate().setColorFilter(this.buttonColor, PorterDuff.Mode.SRC_IN);
        this.mobileOn.getBackground().mutate().setColorFilter(this.buttonColor, PorterDuff.Mode.SRC_IN);
        this.airplaneOff.getBackground().mutate().setColorFilter(this.buttonColor, PorterDuff.Mode.SRC_IN);
        this.wifiOn.setTextColor(this.buttonTextColor);
        this.mobileOn.setTextColor(this.buttonTextColor);
        this.airplaneOff.setTextColor(this.buttonTextColor);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.icon_wifi_white);
        Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.icon_four_g);
        Drawable drawable3 = ContextCompat.getDrawable(getContext(), R.drawable.icon_airplane_off);
        drawable.mutate().setColorFilter(this.buttonIconsColor, PorterDuff.Mode.SRC_ATOP);
        drawable2.mutate().setColorFilter(this.buttonIconsColor, PorterDuff.Mode.SRC_ATOP);
        drawable3.mutate().setColorFilter(this.buttonIconsColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= 17) {
            this.wifiOn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
            this.mobileOn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, (Drawable) null, (Drawable) null, (Drawable) null);
            this.airplaneOff.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable3, (Drawable) null, (Drawable) null, (Drawable) null);
            return;
        }
        this.wifiOn.setCompoundDrawablesWithIntrinsicBounds(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        this.mobileOn.setCompoundDrawablesWithIntrinsicBounds(drawable2, (Drawable) null, (Drawable) null, (Drawable) null);
        this.airplaneOff.setCompoundDrawablesWithIntrinsicBounds(drawable3, (Drawable) null, (Drawable) null, (Drawable) null);
    }

    private GradientDrawable.Orientation getOrientation() {
        switch (this.bgGradientOrientation) {
            case 11:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 12:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 13:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 14:
                return GradientDrawable.Orientation.BL_TR;
            case 15:
                return GradientDrawable.Orientation.TR_BL;
            case 16:
                return GradientDrawable.Orientation.BR_TL;
            case 17:
                return GradientDrawable.Orientation.TL_BR;
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }

    private void initListeners() {
        this.close.setOnClickListener(this);
        this.wifiOn.setOnClickListener(this);
        this.mobileOn.setOnClickListener(this);
        this.airplaneOff.setOnClickListener(this);
    }

    private void initAnimations() {
        int animateDirection = animateDirection();
        this.direction = animateDirection;
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.ghost, "translationX", 1.0f, ((float) animateDirection) * GHOST_X_ANIMATION_VALUE);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.ghost, "translationY", 1.0f, GHOST_Y_ANIMATION_VALUE);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.ghost, "scaleX", 1.0f, GHOST_SCALE_ANIMATION_VALUE);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.ghost, "scaleY", 1.0f, GHOST_SCALE_ANIMATION_VALUE);
        final ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.ghost, "translationX", ((float) this.direction) * GHOST_X_ANIMATION_VALUE, 1.0f);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this.ghost, "translationY", GHOST_Y_ANIMATION_VALUE, 1.0f);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(this.ghost, "scaleX", GHOST_SCALE_ANIMATION_VALUE, 1.0f);
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(this.ghost, "scaleY", GHOST_SCALE_ANIMATION_VALUE, 1.0f);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4);
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setStartDelay(ANIMATION_DELAY);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(ofFloat5, ofFloat6, ofFloat7, ofFloat8);
        animatorSet2.setDuration(ANIMATION_DURATION);
        animatorSet2.setStartDelay(ANIMATION_DELAY);
        animatorSet2.setInterpolator(new DecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass1 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ofFloat5.setFloatValues(((float) NetworkDialog.this.direction) * NetworkDialog.GHOST_X_ANIMATION_VALUE, 1.0f);
                animatorSet2.start();
            }
        });
        animatorSet2.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass2 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NetworkDialog internetDialog = NetworkDialog.this;
                internetDialog.direction = internetDialog.animateDirection();
                ofFloat.setFloatValues(1.0f, ((float) NetworkDialog.this.direction) * NetworkDialog.GHOST_X_ANIMATION_VALUE);
                animatorSet.start();
            }
        });
        animatorSet.start();
        startFlight();
    }

    private void startFlight() {
        if (!NetworkUtils.isAirplaneModeOn(getContext())) {
            this.plane.setVisibility(View.GONE);
            return;
        }
        this.plane.setVisibility(View.VISIBLE);
        this.noInternetBody.setText(R.string.airplane_on);
        this.turnOn.setText(R.string.turn_off);
        this.airplaneOff.setVisibility(View.VISIBLE);
        this.wifiOn.setVisibility(View.INVISIBLE);
        this.mobileOn.setVisibility(View.INVISIBLE);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.plane, "translationX", FLIGHT_THERE_START, FLIGHT_THERE_END);
        final ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.plane, "translationX", FLIGHT_BACK_START, FLIGHT_BACK_END);
        ofFloat.setDuration(FLIGHT_DURATION);
        ofFloat2.setDuration(FLIGHT_DURATION);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass3 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NetworkDialog.this.plane.setRotationY(180.0f);
                ofFloat2.start();
            }
        });
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass4 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NetworkDialog.this.plane.setRotationY(0.0f);
                ofFloat.start();
            }
        });
        ofFloat.start();
    }

    private void initTypefaces() {
        Typeface typeface = this.titleTypeface;
        if (typeface != null) {
            this.noInternet.setTypeface(typeface);
        }
        Typeface typeface2 = this.messageTypeface;
        if (typeface2 != null) {
            this.noInternetBody.setTypeface(typeface2);
        }
    }

    private void initWifiLoading() {
        this.wifiLoading.getIndeterminateDrawable().setColorFilter(this.wifiLoaderColor, PorterDuff.Mode.SRC_IN);
        ViewCompat.setElevation(this.wifiLoading, 10.0f);
    }

    private void initClose() {
        setCancelable(this.cancelable);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close) {
            dismiss();
            AppCheck.getInstance().activity.finishAffinity();
        } else if (id == R.id.wifi_on) {
            turnOnWifiWithAnimation();
        } else if (id == R.id.mobile_on) {
            NetworkUtils.turnOn3g(getContext());
            dismiss();
        } else if (id == R.id.airplane_off) {
            NetworkUtils.turnOffAirplaneMode(getContext());
            dismiss();
        }
    }

    private void turnOnWifiWithAnimation() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat((float) getContext().getResources().getDimensionPixelSize(R.dimen.button_width), (float) (getContext().getResources().getDimensionPixelSize(R.dimen.button_width) + 10), (float) getContext().getResources().getDimensionPixelSize(R.dimen.button_size2));
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) NetworkDialog.this.wifiOn.getLayoutParams();
                layoutParams.width = (int) floatValue;
                NetworkDialog.this.wifiOn.setLayoutParams(layoutParams);
            }
        });
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.wifiOn, "translationX", 1.0f, 110.0f);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.wifiOn, "translationY", 1.0f, 0.0f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.wifiLoading, "translationX", 1.0f, 104.0f);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.wifiLoading, "translationY", 1.0f, -10.0f);
        ValueAnimator ofFloat6 = ValueAnimator.ofFloat(13.0f, 0.0f);
        ofFloat6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                NetworkDialog.this.wifiOn.setTextSize(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass7 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NetworkDialog.this.wifiLoading.setVisibility(View.VISIBLE);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5, ofFloat6);
        animatorSet.setDuration(400L);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            /* class com.gtu.newlivevideocall.CustomDialog.InternetDialog.AnonymousClass8 */

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                NetworkUtils.turnOnWifi(NetworkDialog.this.getContext());
                NetworkDialog.this.animateWifi();
            }
        });
    }

    
    
    private void animateWifi() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.wifiIndicator, "alpha", 0.0f, 0.5f);
        this.wifiAnimator = ofFloat;
        ofFloat.setDuration(ANIMATION_DURATION);
        this.wifiAnimator.setRepeatMode(ValueAnimator.RESTART);
        this.wifiAnimator.setRepeatCount(-1);
        this.wifiAnimator.start();
    }

    
    
    private int animateDirection() {
        return new Random().nextInt(2);
    }

    @Override
    public void onWifiTurnedOn() {
        ObjectAnimator objectAnimator = this.wifiAnimator;
        if (objectAnimator != null && objectAnimator.isStarted()) {
            this.wifiAnimator.cancel();
            this.wifiIndicator.setImageResource(R.drawable.icon_wifi);
            this.wifiIndicator.setAlpha(0.5f);
            this.isWifiOn = true;
            getContext().unregisterReceiver(this.wifiReceiver);
        }
    }

    @Override
    public void hasActiveConnection(boolean z) {
        NetworkCallBackListner connectCallBackListener = this.ConnectCallBackListener;
        if (connectCallBackListener != null) {
            connectCallBackListener.hasActiveConnection(z);
        }
        if (!z) {
            showDialog();
            return;
        }
        AppCheck.getInstance().BindService(AppCheck.getInstance().activity);
        AppCheck.getInstance().RegisterUser();
        dismiss();
    }

    public void show() {
        super.show();
        startFlight();
    }

    public void showDialog() {
        SyncingTask syncingTask = new SyncingTask();
        syncingTask.setConnectionCallback(new NetworkCallBackListner() {


            @Override
            public void hasActiveConnection(boolean z) {
                if (!z) {
                    NetworkDialog.this.show();
                }
            }
        });
        syncingTask.execute(getContext());
    }

    public void dismiss() {
        reset();
        super.dismiss();
    }

    private void reset() {
        Button button = this.airplaneOff;
        if (button != null) {
            button.setVisibility(View.GONE);
        }
        Button button2 = this.wifiOn;
        if (button2 != null) {
            button2.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.wifiOn.getLayoutParams();
            layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.button_width);
            this.wifiOn.setLayoutParams(layoutParams);
            this.wifiOn.setTextSize(13.0f);
            this.wifiOn.setTranslationX(1.0f);
            this.wifiOn.setTranslationY(1.0f);
        }
        Button button3 = this.mobileOn;
        if (button3 != null) {
            button3.setVisibility(View.VISIBLE);
        }
        TextView textView = this.turnOn;
        if (textView != null) {
            textView.setText(R.string.turn_on);
        }
        ProgressBar progressBar = this.wifiLoading;
        if (progressBar != null) {
            progressBar.setTranslationX(1.0f);
            this.wifiLoading.setVisibility(View.INVISIBLE);
        }
        ImageView imageView = this.ghost;
        if (imageView != null) {
            imageView.setTranslationY(1.0f);
        }
    }


    public void onDestroy() {
        getContext().unregisterReceiver(this.networkStatusReceiver);
        getContext().unregisterReceiver(this.wifiReceiver);
    }

    public void setConnectCallBackListener(NetworkCallBackListner connectCallBackListener) {
        this.ConnectCallBackListener = connectCallBackListener;
    }

    public static class Builder {
        private NetworkCallBackListner ConnectCallBackListener;
        private int bgGradientCenter;
        private int bgGradientEnd;
        private int bgGradientOrientation;
        private int bgGradientStart;
        private int bgGradientType;
        private int buttonColor;
        private int buttonIconsColor;
        private int buttonTextColor;
        private boolean cancelable;
        private Context context;
        private float dialogRadius;
        private Typeface messageTypeface;
        private Typeface titleTypeface;
        private int wifiLoaderColor;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder(Fragment fragment) {
            this.context = fragment.getContext();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public Builder(android.app.Fragment fragment) {
            this.context = fragment.getContext();
        }

        public Builder setBgGradientStart(int i) {
            this.bgGradientStart = i;
            return this;
        }

        public Builder setBgGradientCenter(int i) {
            this.bgGradientCenter = i;
            return this;
        }

        public Builder setBgGradientEnd(int i) {
            this.bgGradientEnd = i;
            return this;
        }

        public Builder setBgGradientOrientation(@Orientation int i) {
            this.bgGradientOrientation = i;
            return this;
        }

        public Builder setBgGradientType(int i) {
            this.bgGradientType = i;
            return this;
        }

        public Builder setDialogRadius(float f) {
            this.dialogRadius = f;
            return this;
        }

        public Builder setDialogRadius(int i) {
            this.dialogRadius = (float) this.context.getResources().getDimensionPixelSize(i);
            return this;
        }

        public Builder setTitleTypeface(Typeface typeface) {
            this.titleTypeface = typeface;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setTitleTypeface(int i) {
            this.titleTypeface = this.context.getResources().getFont(i);
            return this;
        }

        public Builder setMessageTypeface(Typeface typeface) {
            this.messageTypeface = typeface;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setMessageTypeface(int i) {
            this.messageTypeface = this.context.getResources().getFont(i);
            return this;
        }

        public Builder setButtonColor(int i) {
            this.buttonColor = i;
            return this;
        }

        public Builder setButtonTextColor(int i) {
            this.buttonTextColor = i;
            return this;
        }

        public Builder setButtonIconsColor(int i) {
            this.buttonIconsColor = i;
            return this;
        }

        public Builder setWifiLoaderColor(int i) {
            this.wifiLoaderColor = i;
            return this;
        }

        public Builder setConnectCallBackListener(NetworkCallBackListner connectCallBackListener) {
            this.ConnectCallBackListener = connectCallBackListener;
            return this;
        }

        public Builder setCancelable(boolean z) {
            this.cancelable = z;
            return this;
        }

        public NetworkDialog build() {
            NetworkDialog internetDialog = new NetworkDialog(this.context, this.bgGradientStart, this.bgGradientCenter, this.bgGradientEnd, this.bgGradientOrientation, this.bgGradientType, this.dialogRadius, this.titleTypeface, this.messageTypeface, this.buttonColor, this.buttonTextColor, this.buttonIconsColor, this.wifiLoaderColor, this.cancelable);
            internetDialog.setConnectCallBackListener(this.ConnectCallBackListener);
            return internetDialog;
        }
    }
}
