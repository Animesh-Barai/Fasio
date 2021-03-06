package customfonts;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

@SuppressLint("AppCompatCustomView")
public class Regular_Radiobutton extends RadioButton {
    public Regular_Radiobutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Regular_Radiobutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Regular_Radiobutton(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

}