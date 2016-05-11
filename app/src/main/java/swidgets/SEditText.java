package swidgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import nz.sodium.Cell;
import nz.sodium.Stream;
import nz.sodium.StreamSink;

public class SEditText extends EditText {
    private final StreamSink<String> sUserChangesSink = new StreamSink<>();
    public final Stream<String> sUserChanges = sUserChangesSink;
    public final Cell<String> text = sUserChanges.hold("");

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
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sUserChangesSink.send(getText().toString());
            }
        });
    }
}
