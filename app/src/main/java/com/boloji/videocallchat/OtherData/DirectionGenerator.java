package com.boloji.videocallchat.OtherData;

import android.app.Activity;

import java.util.Random;

public class DirectionGenerator {

   public static  class AnonymousClass1 {
        static final  int[] $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction;


        static {
            int[] iArr = new int[Direction.values().length];
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction = iArr;
            iArr[Direction.LEFT.ordinal()] = 1;
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.RIGHT.ordinal()] = 2;
            $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.BOTTOM.ordinal()] = 3;
            try {
                $SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[Direction.TOP.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public int[] getPointsInDirection(Activity activity, Direction direction) {
        int i = AnonymousClass1.$SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[direction.ordinal()];
        if (i == 1) {
            return getRandomLeft(activity);
        }
        if (i == 2) {
            return getRandomRight(activity);
        }
        if (i == 3) {
            return getRandomBottom(activity);
        }
        if (i == 4) {
            return getRandomTop(activity);
        }
        return getPointsInDirection(activity, new Direction[]{Direction.LEFT, Direction.TOP, Direction.BOTTOM, Direction.RIGHT}[new Random().nextInt(4)]);
    }

    public int[] getRandomLeft(Activity activity) {
        return new int[]{0, new Random().nextInt(activity.getResources().getDisplayMetrics().heightPixels)};
    }

    public int[] getRandomTop(Activity activity) {
        return new int[]{new Random().nextInt(activity.getResources().getDisplayMetrics().widthPixels), 0};
    }

    public int[] getRandomRight(Activity activity) {
        return new int[]{activity.getResources().getDisplayMetrics().widthPixels, new Random().nextInt(activity.getResources().getDisplayMetrics().heightPixels)};
    }

    public int[] getRandomBottom(Activity activity) {
        int i = activity.getResources().getDisplayMetrics().widthPixels;
        int i2 = activity.getResources().getDisplayMetrics().heightPixels;
        return new int[]{new Random().nextInt(i), i2};
    }

    public Direction getRandomDirection() {
        return new Direction[]{Direction.LEFT, Direction.TOP, Direction.BOTTOM, Direction.RIGHT}[new Random().nextInt(4)];
    }

    public Direction getRandomDirection(Direction direction) {
        int i = AnonymousClass1.$SwitchMap$randomlivecall$random$live$call$videocall$livechat$emojianimation$Direction[direction.ordinal()];
        Direction[] directionArr = i != 1 ? i != 2 ? i != 3 ? i != 4 ? new Direction[]{Direction.LEFT, Direction.TOP, Direction.BOTTOM, Direction.RIGHT} : new Direction[]{Direction.LEFT, Direction.BOTTOM, Direction.RIGHT} : new Direction[]{Direction.TOP, Direction.LEFT, Direction.RIGHT} : new Direction[]{Direction.TOP, Direction.BOTTOM, Direction.LEFT} : new Direction[]{Direction.TOP, Direction.BOTTOM, Direction.RIGHT};
        return directionArr[new Random().nextInt(directionArr.length)];
    }
}
