package com.boloji.videocallchat.OtherData;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

public class OverTheTopLayer {
    private Bitmap mBitmap;
    private FrameLayout mCreatedOttLayer;
    private int[] mDrawLocation = {0, 0};
    private float mScalingFactor = 1.0f;
    private WeakReference<Activity> mWeakActivity;
    private WeakReference<ViewGroup> mWeakRootView;

    public static class OverTheTopLayerException extends RuntimeException {
        public OverTheTopLayerException(String str) {
            super(str);
        }
    }

    public OverTheTopLayer with(Activity activity) {
        this.mWeakActivity = new WeakReference<>(activity);
        return this;
    }

    public OverTheTopLayer generateBitmap(Resources resources, int i, float f, int[] iArr) {
        if (iArr == null) {
            iArr = new int[]{0, 0};
        } else if (iArr.length != 2) {
            throw new OverTheTopLayerException("Requires location as an array of length 2 - [x,y]");
        }
        Bitmap decodeResource = BitmapFactory.decodeResource(resources, i);
        this.mBitmap = Bitmap.createScaledBitmap(decodeResource, (int) (((float) decodeResource.getWidth()) * f), (int) (((float) decodeResource.getHeight()) * f), false);
        this.mDrawLocation = iArr;
        return this;
    }

    public OverTheTopLayer setBitmap(Bitmap bitmap, int[] iArr) {
        if (iArr == null) {
            iArr = new int[]{0, 0};
        } else if (iArr.length != 2) {
            throw new OverTheTopLayerException("Requires location as an array of length 2 - [x,y]");
        }
        this.mBitmap = bitmap;
        this.mDrawLocation = iArr;
        return this;
    }

    public OverTheTopLayer scale(float f) {
        if (f > 0.0f) {
            this.mScalingFactor = f;
            return this;
        }
        throw new OverTheTopLayerException("Scaling should be > 0");
    }

    public OverTheTopLayer attachTo(ViewGroup viewGroup) {
        this.mWeakRootView = new WeakReference<>(viewGroup);
        return this;
    }

    public FrameLayout create() {
        ViewGroup viewGroup;
        if (this.mCreatedOttLayer != null) {
            destroy();
        }
        WeakReference<Activity> weakReference = this.mWeakActivity;
        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity != null) {
                WeakReference<ViewGroup> weakReference2 = this.mWeakRootView;
                if (weakReference2 == null || weakReference2.get() == null) {
                    viewGroup = (ViewGroup) activity.findViewById(16908290);
                } else {
                    viewGroup = this.mWeakRootView.get();
                }
                ImageView imageView = new ImageView(activity);
                imageView.setImageBitmap(this.mBitmap);
                int width = this.mBitmap.getWidth();
                int height = this.mBitmap.getHeight();
                imageView.measure(View.MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new FrameLayout.LayoutParams(-2, -2, 48);
                    imageView.setLayoutParams(layoutParams);
                }
                int[] iArr = this.mDrawLocation;
                int i = iArr[0];
                int i2 = iArr[1];
                layoutParams.width = width;
                layoutParams.height = height;
                layoutParams.leftMargin = i;
                layoutParams.topMargin = i2;
                imageView.setLayoutParams(layoutParams);
                FrameLayout frameLayout = new FrameLayout(activity);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1, 48));
                frameLayout.addView(imageView);
                viewGroup.addView(frameLayout);
                this.mCreatedOttLayer = frameLayout;
            } else {
                Log.e(OverTheTopLayer.class.getSimpleName(), "Could not create the layer. Reference to the activity was lost");
            }
            return this.mCreatedOttLayer;
        }
        throw new OverTheTopLayerException("Could not create the layer as not activity reference was provided.");
    }

    public void destroy() {
        ViewGroup viewGroup;
        WeakReference<Activity> weakReference = this.mWeakActivity;
        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity != null) {
                WeakReference<ViewGroup> weakReference2 = this.mWeakRootView;
                if (weakReference2 == null || weakReference2.get() == null) {
                    viewGroup = (ViewGroup) activity.findViewById(16908290);
                } else {
                    viewGroup = this.mWeakRootView.get();
                }
                FrameLayout frameLayout = this.mCreatedOttLayer;
                if (frameLayout != null) {
                    viewGroup.removeView(frameLayout);
                    this.mCreatedOttLayer = null;
                    return;
                }
                return;
            }
            Log.e(OverTheTopLayer.class.getSimpleName(), "Could not destroy the layer as the layer was never created.");
            return;
        }
        throw new OverTheTopLayerException("Could not create the layer as not activity reference was provided.");
    }

    public void applyAnimation(Animation animation) {
        FrameLayout frameLayout = this.mCreatedOttLayer;
        if (frameLayout != null) {
            ((ImageView) frameLayout.getChildAt(0)).startAnimation(animation);
        }
    }
}
