package io.hoshi.sodiumtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nz.sodium.Cell;
import nz.sodium.Stream;
import swidgets.SButton;
import swidgets.SEditText;
import swidgets.STextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        STextView helloText = (STextView) findViewById(R.id.hello_text);
        SButton redButton = (SButton) findViewById(R.id.red_button);
        SButton greenButton = (SButton) findViewById(R.id.green_button);
        SEditText editText = (SEditText) findViewById(R.id.edit_text);
        SButton replaceButton = (SButton) findViewById(R.id.replace_button);

        Stream<String> sRed = redButton.sClicked.map(u -> "red");
        Stream<String> sGreen = greenButton.sClicked.map(u -> "green");
        Stream<String> sColor = sRed.orElse(sGreen);
        Stream<String> stream = replaceButton.sClicked.snapshot(editText.text);
        Cell<String> cell = sColor.orElse(stream).hold("Hello!");
        helloText.setTextCell(cell);
    }
}
