package com.example.android.playmusic;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */


public class Songs extends Fragment {


    public ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    // Boolean musicPlaying=false,next=false;
    String url;
    ImageButton play, prev, next;
    int count = 0;
    String duration;
    SongInfo songInfo;
    int songnext = 1;
    int songprev = 1;
    SeekBar seekBar;
    int sec, min, hr, dur;
    static MediaPlayer mediaPlayer1;
    Handler handler = new Handler();
    int duration1;
    boolean paused=false;
    TextView start,end;
    int play1=0,play2 = 0;
    int cid;
    MaterialSearchView searchView;
    Fragment selectedFragment;


    public Songs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_songs, container, false);
    }




}
