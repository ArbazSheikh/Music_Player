package com.example.android.playmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.IBinder;
import android.support.v7.widget.CardView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;

public class MyPlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnSeekCompleteListener {
    private static MediaPlayer mediaPlayer;
   private String link;
   CardView c;
   ImageButton play;
   static int length;
   SeekBar seekBar;
  // Context context;







    //MyPlayService myPlayService=new MyPlayService();

    public MyPlayService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        MainActivity.oncomplete(mediaPlayer);
        //MainActivity.media(mediaPlayer);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        link=intent.getStringExtra("SUrl");
        mediaPlayer.reset();

        if(!mediaPlayer.isPlaying()){
            try {

                mediaPlayer.setDataSource(link);
                mediaPlayer.prepareAsync();
                //c.setCardBackgroundColor(getResources().getColor(R.color.lightPrimary));


            }catch (IOException e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
            }
        }
        else{
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(link);
                mediaPlayer.prepareAsync();
                //c.setCardBackgroundColor(getResources().getColor(R.color.lightPrimary));


            }catch (IOException e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT);
            }
        }
        return START_STICKY;
    }



    public static void pause(){
        mediaPlayer.pause();
        length=mediaPlayer.getCurrentPosition();

    }
    public static void resume(){
    mediaPlayer.seekTo(length);
    mediaPlayer.start();
    }
    public void onDestroy() {


        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){

                mediaPlayer.stop();



            }
         //   mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
           MainActivity.oncomplete(mediaPlayer);
        }

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        switch (i){
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Toast.makeText(this,"MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",Toast.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();


        }

    }



    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }
}
