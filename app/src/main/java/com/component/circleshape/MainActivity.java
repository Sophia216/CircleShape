package com.component.circleshape;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircleShape mPR = (CircleShape) findViewById(R.id.circle_shape);
        mPR.setProgress(60);
        mPR.setColor(R.color.bgColor, R.color.progressStartColor, R.color.progressEndColor);
    }
}
