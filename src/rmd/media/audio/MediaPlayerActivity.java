package rmd.media.audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MediaPlayerActivity extends Activity
{
    private MediaPlayer mPlayer;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediaplayer);

        
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer=MediaPlayer.create(MediaPlayerActivity.this, R.raw.moment4life);
                mPlayer.start();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.stop();
            }
        });
    }
}
