package swidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.Button;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Operational;
import nz.sodium.Stream;
import nz.sodium.StreamSink;
import nz.sodium.Transaction;
import nz.sodium.Unit;

public class SButton extends Button {
    private StreamSink<Unit> sClickedSink;
    private Listener listener;

    public SButton(Context context) {
        super(context);
        init();
    }

    public SButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        sClickedSink = new StreamSink<>();
        setOnClickListener(view -> sClickedSink.send(Unit.UNIT));
    }

    @Override
    protected void onDetachedFromWindow() {
        if (listener != null) {
            listener.unlisten();
        }
        super.onDetachedFromWindow();
    }

    public void setEnabledCell(Cell<Boolean> enabled) {
        if (listener != null) {
            listener.unlisten();
        }

        Transaction.post(() -> setEnabled(enabled.sample()));

        listener = Operational.updates(enabled).listen(ena -> {
            if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
                setEnabled(ena);
            } else {
                post(() -> setEnabled(ena));
            }
        });
    }

    public Stream<Unit> sClicked() {
        return sClickedSink;
    }
}
