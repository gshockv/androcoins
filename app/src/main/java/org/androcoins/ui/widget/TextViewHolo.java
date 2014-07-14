package org.androcoins.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import org.androcoins.AppConstants;
import org.androcoins.ui.UIUtils;

/**
 * Holo TextView
 * @author vitaly gashock
 */
public class TextViewHolo extends TextView {
    public TextViewHolo(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        if (UIUtils.isRobotoRequired()) {
            final Typeface font = Typeface.createFromAsset(ctx.getAssets(), AppConstants.TTF_ROBOTO_REGULAR);
            setTypeface(font);
        }
    }
}
