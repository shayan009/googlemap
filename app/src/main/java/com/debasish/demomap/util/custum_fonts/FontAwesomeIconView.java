package com.debasish.demomap.util.custum_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by nits-32 on 7/2/17.
 */

public class FontAwesomeIconView extends TextView {
    private static final String TAG = "TextViewFontAwesome";

    public FontAwesomeIconView(Context context) {
        super(context);
        init();
    }

    public FontAwesomeIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesomeIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FontAwesomeIconView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
            setTypeface(tf);
        }
    }
}
