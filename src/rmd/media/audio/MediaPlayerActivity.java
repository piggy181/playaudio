package rmd.media.audio;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

public class MediaPlayerActivity extends Activity {
    private MediaPlayer mPlayer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediaplayer);


        findViewById(R.id.playfromfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer = MediaPlayer.create(MediaPlayerActivity.this, R.raw.moment4life);
                mPlayer.start();
            }
        });

        findViewById(R.id.playfromurl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText url = (EditText) findViewById(R.id.url);

                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mPlayer.setDataSource(url.getText().toString());
                    mPlayer.prepare();

                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
