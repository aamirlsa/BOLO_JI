package com.boloji.videocallchat.OtherData;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import java.io.IOException;

public class ZeroGravityAnimation {
    private static final int RANDOM_DURATION = -1;
    private String emojiName;
    private Animation.AnimationListener mAnimationListener;
    private int mCount = 1;
    private Direction mDestinationDirection = Direction.RANDOM;
    private int mDuration = -1;
    private Direction mOriginationDirection = Direction.RANDOM;
    private float mScalingFactor = 1.0f;
    int i;
    public ZeroGravityAnimation setOriginationDirection(Direction direction) {
        this.mOriginationDirection = direction;
        return this;
    }

    public ZeroGravityAnimation setDestinationDirection(Direction direction) {
        this.mDestinationDirection = direction;
        return this;
    }

    public ZeroGravityAnimation setRandomDuration() {
        return setDuration(-1);
    }

    public ZeroGravityAnimation setDuration(int i) {
        this.mDuration = i;
        return this;
    }

    public ZeroGravityAnimation setImage(String str) {
        this.emojiName = str;
        return this;
    }

    public ZeroGravityAnimation setScalingFactor(float f) {
        this.mScalingFactor = f;
        return this;
    }

    public ZeroGravityAnimation setAnimationListener(Animation.AnimationListener animationListener) {
        this.mAnimationListener = animationListener;
        return this;
    }

    public ZeroGravityAnimation setCount(int i) {
        this.mCount = i;
        return this;
    }

    public void play(Activity activity, ViewGroup viewGroup) {
        DirectionGenerator directionGenerator = new DirectionGenerator();
        if (this.mCount > 0) {

            for ( i = 0; i < this.mCount; i++) {
                Direction randomDirection = this.mOriginationDirection == Direction.RANDOM ? directionGenerator.getRandomDirection() : this.mOriginationDirection;
                Direction randomDirection2 = this.mDestinationDirection == Direction.RANDOM ? directionGenerator.getRandomDirection(randomDirection) : this.mDestinationDirection;
                int[] pointsInDirection = directionGenerator.getPointsInDirection(activity, randomDirection);
                int[] pointsInDirection2 = directionGenerator.getPointsInDirection(activity, randomDirection2);
                Bitmap bitmapFromAsset = getBitmapFromAsset(activity, this.emojiName);
                int i2 = AnonymousClass2.$SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[randomDirection.ordinal()];
                if (i2 == 1) {
                    pointsInDirection[0] = pointsInDirection[0] - bitmapFromAsset.getWidth();
                } else if (i2 == 2) {
                    pointsInDirection[0] = pointsInDirection[0] + bitmapFromAsset.getWidth();
                } else if (i2 == 3) {
                    pointsInDirection[1] = pointsInDirection[1] - bitmapFromAsset.getHeight();
                } else if (i2 == 4) {
                    pointsInDirection[1] = pointsInDirection[1] + bitmapFromAsset.getHeight();
                }
                int i3 = AnonymousClass2.$SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[randomDirection2.ordinal()];
                if (i3 == 1) {
                    pointsInDirection2[0] = pointsInDirection2[0] - bitmapFromAsset.getWidth();
                } else if (i3 == 2) {
                    pointsInDirection2[0] = pointsInDirection2[0] + bitmapFromAsset.getWidth();
                } else if (i3 == 3) {
                    pointsInDirection2[1] = pointsInDirection2[1] - bitmapFromAsset.getHeight();
                } else if (i3 == 4) {
                    pointsInDirection2[1] = pointsInDirection2[1] + bitmapFromAsset.getHeight();
                }
                final OverTheTopLayer overTheTopLayer = new OverTheTopLayer();
                overTheTopLayer.with(activity).scale(this.mScalingFactor).attachTo(viewGroup).setBitmap(bitmapFromAsset, pointsInDirection).create();
                int i4 = AnonymousClass2.$SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[randomDirection.ordinal()];
                int i5 = pointsInDirection2[0] - pointsInDirection[0];
                int i6 = pointsInDirection2[1] - pointsInDirection[1];
                int i7 = this.mDuration;
                if (i7 == -1) {
                    i7 = RandomUtil.generateRandomBetween(3500, 12500);
                }
                TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, (float) i5, 0.0f, (float) i6);
                translateAnimation.setDuration((long) i7);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    /* class randomlivecall.random.live.call.videocall.livechat.emojianimation.ZeroGravityAnimation.AnonymousClass1 */

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                        if (i == 0 && ZeroGravityAnimation.this.mAnimationListener != null) {
                            ZeroGravityAnimation.this.mAnimationListener.onAnimationStart(animation);
                        }
                    }

                    public void onAnimationEnd(Animation animation) {
                        overTheTopLayer.destroy();
                        if (i == ZeroGravityAnimation.this.mCount - 1 && ZeroGravityAnimation.this.mAnimationListener != null) {
                            ZeroGravityAnimation.this.mAnimationListener.onAnimationEnd(animation);
                        }
                    }
                });
                overTheTopLayer.applyAnimation(translateAnimation);
            }
            return;
        }
        Log.e(ZeroGravityAnimation.class.getSimpleName(), "Count was not provided, animation was not started");
    }

   public static  class AnonymousClass2 {
        static final  int[] $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction;


        static {
            int[] iArr = new int[Direction.values().length];
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction = iArr;
            iArr[Direction.LEFT.ordinal()] = 1;
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.RIGHT.ordinal()] = 2;
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.TOP.ordinal()] = 3;
            try {
                $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public void play(Activity activity) {
        play(activity, null);
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int i) {
        Drawable drawable = ContextCompat.getDrawable(context, i);
        if (Build.VERSION.SDK_INT < 21) {
            drawable = DrawableCompat.wrap(drawable).mutate();
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static Bitmap getBitmapFromAsset(Context context, String str) {
        AssetManager assets = context.getAssets();
        try {
            return BitmapFactory.decodeStream(assets.open("emojis/" + str));
        } catch (IOException unused) {
            return null;
        }
    }
}
