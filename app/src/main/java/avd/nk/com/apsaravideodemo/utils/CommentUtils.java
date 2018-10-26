package avd.nk.com.apsaravideodemo.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Description:
 * <p></p>
 * Usage:
 * <p></p>
 *
 * <h4>Created by Nikou Karter on 2018/10/22.</h4>
 * <h5>Email: nikou.karter@outlook.com</h5>
 * <h5>Tim: 2482 17909(as QQ)</h5>
 */
public class CommentUtils {
    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue,
                context.getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue,
                context.getResources().getDisplayMetrics());
    }
}
