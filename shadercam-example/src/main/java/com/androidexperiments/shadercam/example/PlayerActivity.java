package com.androidexperiments.shadercam.example;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.VideoView;

import java.io.File;

public class PlayerActivity extends FragmentActivity {

  public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    VideoView videoView = (VideoView) findViewById(R.id.video_view);
    String videoPath = getIntent().getStringExtra(EXTRA_VIDEO_PATH);
    File file = new File(videoPath);
    videoView.setVideoPath(file.toString());
    videoView.start();
  }
}
