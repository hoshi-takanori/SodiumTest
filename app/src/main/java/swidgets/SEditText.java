package swidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import nz.sodium.Cell;
import nz.sodium.Listener;
import nz.sodium.Stream;
import nz.sodium.StreamSink;

public class SEditText extends EditText {
    private StreamSink<String> sUserChangesSink;
    private Cell<String> text;
    private Listener listener;
    private boolean updateFlag;

    public SEditText(Context context) {
        super(context);
        init();
    }

    public SEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        sUserChangesSink = new StreamSink<>();

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!updateFlag) {
                    sUserChangesSink.send(getText().toString());
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        if (listener != null) {
            listener.unlisten();
        }
        super.onDetachedFromWindow();
    }

    public void setTextStream(Stream<String> sText) {
        if (listener != null) {
            listener.unlisten();
        }

        if (sText != null) {
            text = sUserChangesSink.orElse(sText).hold(getText().toString());
            listener = sText.listen(text -> {
                post(() -> {
                    updateFlag = true;
                    setText(text);
                    updateFlag = false;
                });
            });
        } else {
            text = sUserChangesSink.hold(getText().toString());
            listener = null;
        }
    }

    public Stream<String> sUserChanges() {
        return sUserChangesSink;
    }

    public Cell<String> text() {
        if (text == null) {
            setTextStream(null);
        }
        return text;
    }
}
