package swidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.TextView;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Operational;
import nz.sodium.Transaction;

public class STextView extends TextView {
    private Listener listener;

    public STextView(Context context) {
        super(context);
    }

    public STextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public STextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public STextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (listener != null) {
            listener.unlisten();
        }
        super.onDetachedFromWindow();
    }

    public void setTextCell(final Cell<String> text) {
        if (listener != null) {
            listener.unlisten();
        }

        listener = Operational.updates(text).listen(str -> {
            if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
                setText(str);
            } else {
                post(() -> setText(str));
            }
        });

        Transaction.post(() -> {
            post(() -> setText(text.sample()));
        });
    }
}
