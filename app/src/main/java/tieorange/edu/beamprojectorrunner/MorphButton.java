package tieorange.edu.beamprojectorrunner;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;

import com.dd.morphingbutton.MorphingButton;

/**
 * Created by tieorange on 13/03/16.
 */
public class MorphButton {

    private static int mMorphCounter1 = 1;


    public static void onMorphButton1ClickedSimple(MainActivity activity, MorphingButton btnMorphSimple) {
        if (mMorphCounter1 == 0) {
            mMorphCounter1++;
            morphToSquare(btnMorphSimple, integer(activity, R.integer.mb_animation), activity);
        } else if (mMorphCounter1 == 1) {
            mMorphCounter1 = 0;
            morphToSuccessSimple(btnMorphSimple, activity);

            activity.runSSHCommand();
        }

        morphToSuccessSimple(btnMorphSimple, activity);
    }

    private static void morphToSuccessSimple(final MorphingButton btnMorph, MainActivity activity) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(activity, R.integer.mb_animation))
                .cornerRadius(dimen(activity, R.dimen.mb_height_56) * 2)
                .width(dimen(activity, R.dimen.mb_height_56) * 3)
                .height(dimen(activity, R.dimen.mb_height_56) * 3)
                .color(color(activity, R.color.mb_green))
                .colorPressed(color(activity, R.color.mb_green_dark))
                .icon(R.drawable.ic_projector);
        btnMorph.morph(circle);
    }

    public static void morphToSquare(final MorphingButton btnMorph, int duration, MainActivity activity) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(activity, R.dimen.mb_height_56))
                .width(dimen(activity, R.dimen.mb_width_square))
                .height(dimen(activity, R.dimen.mb_height_square))
                .color(color(activity, R.color.mb_blue))
                .colorPressed(color(activity, R.color.mb_blue_dark))
                .text(activity.getString(R.string.mb_button));
        btnMorph.morph(square);
    }

    public static int dimen(MainActivity mainActivity, @DimenRes int resId) {
        return (int) mainActivity.getResources().getDimension(resId);
    }

    public static int color(MainActivity mainActivity, @ColorRes int resId) {
        return mainActivity.getResources().getColor(resId);
    }

    public static int integer(MainActivity mainActivity, @IntegerRes int resId) {
        return mainActivity.getResources().getInteger(resId);
    }
}

