package rmd.media.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.util.Log;

/**
 * AudioTrackActivity activity sends and recv audio data through udp
 */
public class AudioTrackActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udpstream);
        Button btnSend = (Button) findViewById(R.id.btnSend);

        /*
            using static mode.
         */
        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Log.d(LOG_TAG, "btnSend clicked");
//                SendAudio();
                playFromRaw();
            }
        });

        /*  using stream mode.

        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "btnSend clicked");
                SendAudio();
            }
        });

        Button btnRecv = (Button) findViewById(R.id.btnRecv);
        btnRecv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "btnRecv clicked");
                RecvAudio();
            }
        });
        */

    }

    static final String LOG_TAG = "AudioTrackActivity";
    static final String AUDIO_FILE_PATH = "/data/1.wav";
    static final int SAMPLE_RATE = 9142;
    static final int AUDIO_PORT = 2048;
    static final int SAMPLE_INTERVAL = 20; // milliseconds
    static final int SAMPLE_SIZE = 2; // bytes per sample
    static final int BUF_SIZE = SAMPLE_RATE;//AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT)*4;

    public void RecvAudio() {
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Sophat", "Buffered size: " + BUF_SIZE);
                Log.e(LOG_TAG, "start recv thread, thread id: "
                        + Thread.currentThread().getId());
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                        SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, BUF_SIZE,
                        AudioTrack.MODE_STREAM);
                track.play();
                try {
                    long totalByteReceived = 0;
                    DatagramSocket sock = new DatagramSocket(AUDIO_PORT);
                    byte[] buf = new byte[BUF_SIZE];

                    while (true) {
                        DatagramPacket pack = new DatagramPacket(buf, BUF_SIZE);
                        sock.receive(pack);

                        totalByteReceived += pack.getLength();
                        Log.d(LOG_TAG, String.format("recv pack: %d, totalByteReceived: %d", pack.getLength(), totalByteReceived));
                        track.write(pack.getData(), 0, pack.getLength());
                    }
                } catch (SocketException se) {
                    Log.e(LOG_TAG, "SocketException: " + se.toString());
                } catch (IOException ie) {
                    Log.e(LOG_TAG, "IOException" + ie.toString());
                }
            } // end run
        });
        thrd.start();
    }

    public void SendAudio() {
        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(LOG_TAG, "start send thread, thread id: "
                        + Thread.currentThread().getId());
                long file_size = 0;
                int bytes_read = 0;
                int bytes_count = 0;
                File audio = new File(AUDIO_FILE_PATH);
                FileInputStream audio_stream = null;
                file_size = audio.length();
                byte[] buf = new byte[BUF_SIZE];
                try {
                    InetAddress addr = InetAddress.getLocalHost();
                    DatagramSocket sock = new DatagramSocket();
                    audio_stream = new FileInputStream(audio);

                    while (bytes_count < file_size) {
                        bytes_read = audio_stream.read(buf, 0, BUF_SIZE);
                        DatagramPacket pack = new DatagramPacket(buf, bytes_read,
                                addr, AUDIO_PORT);
                        sock.send(pack);
                        bytes_count += bytes_read;
                        Log.d(LOG_TAG, String.format("bytes_count : %d, filesize: %d", bytes_count, file_size));
                        Thread.sleep(SAMPLE_INTERVAL, 0);
                    }
                    Log.i("Sophat", "one last sleep");
                    Thread.sleep(1000 * 14, 0);
                } catch (InterruptedException ie) {
                    Log.e(LOG_TAG, "InterruptedException");
                } catch (FileNotFoundException fnfe) {
                    Log.e(LOG_TAG, "FileNotFoundException");
                } catch (SocketException se) {
                    Log.e(LOG_TAG, "SocketException");
                } catch (UnknownHostException uhe) {
                    Log.e(LOG_TAG, "UnknownHostException");
                } catch (IOException ie) {
                    Log.e(LOG_TAG, "IOException");
                }
            } // end run
        });
        thrd.start();
    }

    private void playFromRaw() {
        try {
            File audio = new File(AUDIO_FILE_PATH);
            Log.i("Sophat"," 0size: "+audio.length());
            int file_size = (int)audio.length();
            byte[] buffer = new byte[file_size];
            int byte_count = 0;

            Log.i("Sophat","size: "+file_size);
            AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, file_size,
                    AudioTrack.MODE_STATIC);

            FileInputStream audio_stream = new FileInputStream(audio);

            byte_count = audio_stream.read(buffer, 0, file_size);
            track.write(buffer, 0, byte_count);
            track.play();
//
//            byte_count = audio_stream.read(buffer, 0, BUF_SIZE);
//            while (byte_count > -1) {
//                Log.i("Sophat", "write stream");
//
//                track.write(buffer, 0, byte_count);
//                byte_count = audio_stream.read(buffer, 0, BUF_SIZE);
//            }
            
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }
}
