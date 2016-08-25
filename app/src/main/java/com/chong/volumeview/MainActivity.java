package com.chong.volumeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private VolumeView volumeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volumeView = (VolumeView) findViewById(R.id.volume_view);
//        volumeView.setRectCount(10);
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                volumeView.start();
                break;
            case R.id.btn_stop:
                volumeView.stop();
                break;
        }
    }
}
