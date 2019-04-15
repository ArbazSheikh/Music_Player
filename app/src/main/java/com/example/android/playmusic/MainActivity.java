package com.example.android.playmusic;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import java.lang.String;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<SongInfo> songs = new ArrayList<SongInfo>();
    RecyclerView recyclerView,recyclerView1;
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
    final Thread t = new MyThread();
    int cid;
    MaterialSearchView searchView;
    Fragment selectedFragment;
    int count1=0,count2=0,count3=0,count4=0,count5=0,count6=0;
    int tfrag=0;

    // ImageButton play = (ImageButton) findViewById(R.id.play);

    static int btnchange = 0;
   int tsec=0,tmin=0,thr=0;

    public static void oncomplete(MediaPlayer mediaPlayer) {
        mediaPlayer1 = mediaPlayer;


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       // themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seek);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        songAdapter = new SongAdapter(this, songs);
        recyclerView.setLongClickable(true);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        android.support.v7.widget.Toolbar toolbar= (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Play Music");
        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Radio()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                selectedFragment=new Radio();
                switch (menuItem.getItemId()){
                    case R.id.action_songs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Songs()).commit();
                        break;
                    case R.id.radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Radio()).commit();
                        break;
                    case R.id.action_playlist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Playlist()).commit();
                        break;
                }
              //  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                selectedFragment=null;
                switch (menuItem.getItemId()){
                    case R.id.action_songs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Songs()).commit();
                        break;
                    case R.id.radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Radio()).commit();
                        break;
                    case R.id.action_playlist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Playlist()).commit();
                        break;
                }
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            }
        });



        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        start.setText("0:00");
        end.setText("0:00");


        searchView=(MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                songAdapter = new SongAdapter(MainActivity.this, songs);
                recyclerView.setAdapter(songAdapter);


                songAdapter.setOnitemClickListener(new SongAdapter.OnitemClickListener() {
                    @Override
                    public void OnItemClick(CardView c, View v, final SongInfo obj, final int pos) {

                        paused=false;



                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                final int position[] = {pos};




                                if (true) {

                                    if(paused==true){
                                        paused=false;
                                    }
                                    if(play1==0){
                                        t.start();
                                    }
                                    play1=1;
                                    duration1=0;
                                    //  b.setText("Stop");
                                    url = obj.getSongUrl();
                                    duration = obj.getDuration();
                                    dur = Integer.parseInt(duration);
                                    sec = (dur / 1000);
                                    min = sec / 60;
                                    sec = sec % 60;
                                    hr = min / 60;
                                    min = min % 60;
                                    start = (TextView) findViewById(R.id.start);
                                    start.setText("0:00");
                                    end = (TextView) findViewById(R.id.end);
                                    if (hr >= 1) {
                                        if (sec == 0) {
                                            end.setText(hr + ":" + min + ":00");

                                        } else {
                                            end.setText(hr + ":" + min + ":" + sec);
                                        }
                                    } else {
                                        if (sec == 0) {
                                            end.setText(min + ":00");

                                        } else {
                                            end.setText(min + ":" + sec);
                                        }
                                    }
                                    seekBar.setProgress(0);
                                    seekBar.setMax(dur);





                                    play = (ImageButton) findViewById(R.id.play);
                                    play.setImageResource(R.drawable.pause);
                                    next = (ImageButton) findViewById(R.id.next);
                                    prev = (ImageButton) findViewById(R.id.prev);


                                    play.setOnClickListener(new View.OnClickListener() {
                                        @Override

                                        public void onClick(View view) {
                                            if (count % 2 == 0) {
                                                MyPlayService.pause();

                                                play = (ImageButton) findViewById(R.id.play);
                                                play.setImageResource(R.drawable.play);

                                                // state=t.getState();
                                                paused=true;

                                                count++;


                                            } else {
                                                MyPlayService.resume();

                                                play = (ImageButton) findViewById(R.id.play);
                                                play.setImageResource(R.drawable.pause);

                                                Thread t = new MyThread();
                                                paused=false;
                                                //t.start();


                                                count++;
                                            }
                                        }
                                    });

                                    next.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (count % 2 != 0) {
                                                play = (ImageButton) findViewById(R.id.play);
                                                play.setImageResource(R.drawable.pause);
                                            }


                                            if (position[0] >= songs.size() - 1) {
                                                position[0] = -1;
                                            }

                                            songInfo = songs.get(++position[0]);


                                            url = songInfo.getSongUrl();
                                            duration = songInfo.getDuration();
                                            int dur = Integer.parseInt(duration);
                                            sec = (dur / 1000);
                                            min = sec / 60;
                                            sec = sec % 60;
                                            hr = min / 60;
                                            min = min % 60;
                                            TextView start = (TextView) findViewById(R.id.start);
                                            start.setText("0:00");
                                            TextView end = (TextView) findViewById(R.id.end);
                                            if (hr >= 1) {
                                                if (sec == 0) {
                                                    end.setText(hr + ":" + min + ":00");

                                                } else {
                                                    end.setText(hr + ":" + min + ":" + sec);
                                                }
                                            } else {
                                                if (sec == 0) {
                                                    end.setText(min + ":00");

                                                } else {
                                                    end.setText(min + ":" + sec);
                                                }
                                            }

                                            seekBar.setProgress(0);
                                            seekBar.setMax(dur);
                                            duration1=0;


                                            // Thread t = new MyThread();
                                            // t.start();


                                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                            serviceIntent.putExtra("SUrl", url);
                                            startService(serviceIntent);
                                        }
                                    });

                                    prev.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (count % 2 != 0) {
                                                play = (ImageButton) findViewById(R.id.play);
                                                play.setImageResource(R.drawable.pause);
                                            }

                                            if (position[0] <= 0) {

                                                position[0] = songs.size();


                                            }
                                            songInfo = songs.get(--position[0]);


                                            // songInfo=songs.get(position-1);
                                            url = songInfo.getSongUrl();
                                            duration = songInfo.getDuration();
                                            int dur = Integer.parseInt(duration);
                                            sec = (dur / 1000);
                                            min = sec / 60;
                                            sec = sec % 60;
                                            hr = min / 60;
                                            min = min % 60;
                                            TextView start = (TextView) findViewById(R.id.start);

                                            start.setText("0:00");
                                            TextView end = (TextView) findViewById(R.id.end);
                                            if (hr >= 1) {
                                                if (sec == 0) {
                                                    end.setText(hr + ":" + min + ":00");

                                                } else {
                                                    end.setText(hr + ":" + min + ":" + sec);
                                                }
                                            } else {
                                                if (sec == 0) {
                                                    end.setText(min + ":00");

                                                } else {
                                                    end.setText(min + ":" + sec);
                                                }
                                            }
                                            seekBar.setProgress(0);
                                            seekBar.setMax(dur);
                                            duration1=0;

                                            //Thread t = new MyThread();


                                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                            serviceIntent.putExtra("SUrl", url);
                                            startService(serviceIntent);
                                        }
                                    });


                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                                    serviceIntent.putExtra("SUrl", url);
                                    startService(serviceIntent);
                                } else {
                                    // next=false;
                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                                    stopService(serviceIntent);

                                }
                            }
                        };
                        handler.postDelayed(r, 100);
                    }


           /* private void stopPlay() {

                stopService(serviceIntent);
               // serviceIntent.putExtra("SUrl",url);

            }

            private void playAudio() {
                serviceIntent.putExtra("SUrl",url);
                startService(serviceIntent);

            }*/
                });

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText!=null & !newText.isEmpty()){
                    ArrayList<SongInfo> songsfound =new ArrayList<SongInfo>();
                    for(SongInfo item:songs){

                        String sitem=item.getSongName().toLowerCase();
                        if(sitem.contains(newText))
                            songsfound.add(item);
                        songAdapter = new SongAdapter(MainActivity.this, songsfound);
                        recyclerView.setAdapter(songAdapter);


                        songAdapter.setOnitemClickListener(new SongAdapter.OnitemClickListener() {
                            @Override
                            public void OnItemClick(CardView c, View v, final SongInfo obj, final int pos) {



                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        final int position[] = {pos};




                                        if (true) {

                                            if(paused==true){
                                                paused=false;
                                            }
                                            if(play1==0){
                                                t.start();
                                            }
                                            play1=1;
                                            duration1=0;
                                            //  b.setText("Stop");
                                            url = obj.getSongUrl();
                                            duration = obj.getDuration();
                                            dur = Integer.parseInt(duration);
                                            sec = (dur / 1000);
                                            min = sec / 60;
                                            sec = sec % 60;
                                            hr = min / 60;
                                            min = min % 60;
                                            start = (TextView) findViewById(R.id.start);
                                            start.setText("0:00");
                                            end = (TextView) findViewById(R.id.end);
                                            if (hr >= 1) {
                                                if (sec == 0) {
                                                    end.setText(hr + ":" + min + ":00");

                                                } else {
                                                    end.setText(hr + ":" + min + ":" + sec);
                                                }
                                            } else {
                                                if (sec == 0) {
                                                    end.setText(min + ":00");

                                                } else {
                                                    end.setText(min + ":" + sec);
                                                }
                                            }
                                            seekBar.setProgress(0);
                                            seekBar.setMax(dur);





                                            play = (ImageButton) findViewById(R.id.play);
                                            play.setImageResource(R.drawable.pause);
                                            next = (ImageButton) findViewById(R.id.next);
                                            prev = (ImageButton) findViewById(R.id.prev);


                                            play.setOnClickListener(new View.OnClickListener() {
                                                @Override

                                                public void onClick(View view) {
                                                    if (count % 2 == 0) {
                                                        MyPlayService.pause();

                                                        play = (ImageButton) findViewById(R.id.play);
                                                        play.setImageResource(R.drawable.play);

                                                        // state=t.getState();
                                                        paused=true;

                                                        count++;


                                                    } else {
                                                        MyPlayService.resume();

                                                        play = (ImageButton) findViewById(R.id.play);
                                                        play.setImageResource(R.drawable.pause);

                                                        Thread t = new MyThread();
                                                        paused=false;
                                                        t.start();


                                                        count++;
                                                    }
                                                }
                                            });

                                            next.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if (count % 2 != 0) {
                                                        play = (ImageButton) findViewById(R.id.play);
                                                        play.setImageResource(R.drawable.pause);
                                                    }


                                                    if (position[0] >= songs.size() - 1) {
                                                        position[0] = -1;
                                                    }

                                                    songInfo = songs.get(++position[0]);


                                                    url = songInfo.getSongUrl();
                                                    duration = songInfo.getDuration();
                                                    int dur = Integer.parseInt(duration);
                                                    sec = (dur / 1000);
                                                    min = sec / 60;
                                                    sec = sec % 60;
                                                    hr = min / 60;
                                                    min = min % 60;
                                                    TextView start = (TextView) findViewById(R.id.start);
                                                    start.setText("0:00");
                                                    TextView end = (TextView) findViewById(R.id.end);
                                                    if (hr >= 1) {
                                                        if (sec == 0) {
                                                            end.setText(hr + ":" + min + ":00");

                                                        } else {
                                                            end.setText(hr + ":" + min + ":" + sec);
                                                        }
                                                    } else {
                                                        if (sec == 0) {
                                                            end.setText(min + ":00");

                                                        } else {
                                                            end.setText(min + ":" + sec);
                                                        }
                                                    }

                                                    seekBar.setProgress(0);
                                                    seekBar.setMax(dur);
                                                    duration1=0;


                                                    // Thread t = new MyThread();
                                                    // t.start();


                                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                                    serviceIntent.putExtra("SUrl", url);
                                                    startService(serviceIntent);
                                                }
                                            });

                                            prev.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if (count % 2 != 0) {
                                                        play = (ImageButton) findViewById(R.id.play);
                                                        play.setImageResource(R.drawable.pause);
                                                    }

                                                    if (position[0] <= 0) {

                                                        position[0] = songs.size();


                                                    }
                                                    songInfo = songs.get(--position[0]);


                                                    // songInfo=songs.get(position-1);
                                                    url = songInfo.getSongUrl();
                                                    duration = songInfo.getDuration();
                                                    int dur = Integer.parseInt(duration);
                                                    sec = (dur / 1000);
                                                    min = sec / 60;
                                                    sec = sec % 60;
                                                    hr = min / 60;
                                                    min = min % 60;
                                                    TextView start = (TextView) findViewById(R.id.start);

                                                    start.setText("0:00");
                                                    TextView end = (TextView) findViewById(R.id.end);
                                                    if (hr >= 1) {
                                                        if (sec == 0) {
                                                            end.setText(hr + ":" + min + ":00");

                                                        } else {
                                                            end.setText(hr + ":" + min + ":" + sec);
                                                        }
                                                    } else {
                                                        if (sec == 0) {
                                                            end.setText(min + ":00");

                                                        } else {
                                                            end.setText(min + ":" + sec);
                                                        }
                                                    }
                                                    seekBar.setProgress(0);
                                                    seekBar.setMax(dur);
                                                    duration1=0;

                                                    //Thread t = new MyThread();


                                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                                    serviceIntent.putExtra("SUrl", url);
                                                    startService(serviceIntent);
                                                }
                                            });


                                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                                            serviceIntent.putExtra("SUrl", url);
                                            startService(serviceIntent);
                                        } else {
                                            // next=false;
                                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                                            stopService(serviceIntent);

                                        }
                                    }
                                };
                                handler.postDelayed(r, 100);
                            }


           /* private void stopPlay() {

                stopService(serviceIntent);
               // serviceIntent.putExtra("SUrl",url);

            }

            private void playAudio() {
                serviceIntent.putExtra("SUrl",url);
                startService(serviceIntent);

            }*/
                        });


                    }
                }
                else{
                    songAdapter = new SongAdapter(MainActivity.this, songs);
                    recyclerView.setAdapter(songAdapter);
                }
                return true;
            }
        });

        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        // recyclerView.addItemDecoration(dividerItemDecoration);

        songAdapter.setOnitemClickListener(new SongAdapter.OnitemClickListener() {
            @Override
            public void OnItemClick(CardView c, View v, final SongInfo obj, final int pos) {



                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        final int position[] = {pos};




                        if (true) {

                            if(paused==true){
                                paused=false;
                            }
                            if(play1==0){
                                t.start();
                            }
                            play1=1;
                            duration1=0;
                            //  b.setText("Stop");
                            url = obj.getSongUrl();
                            duration = obj.getDuration();
                            dur = Integer.parseInt(duration);
                            sec = (dur / 1000);
                            min = sec / 60;
                            sec = sec % 60;
                            hr = min / 60;
                            min = min % 60;
                            start = (TextView) findViewById(R.id.start);
                            start.setText("0:00");
                          end = (TextView) findViewById(R.id.end);
                            if (hr >= 1) {
                                if (sec == 0) {
                                    end.setText(hr + ":" + min + ":00");

                                } else {
                                    end.setText(hr + ":" + min + ":" + sec);
                                }
                            } else {
                                if (sec == 0) {
                                    end.setText(min + ":00");

                                } else {
                                    end.setText(min + ":" + sec);
                                }
                            }
                            seekBar.setProgress(0);
                            seekBar.setMax(dur);





                            play = (ImageButton) findViewById(R.id.play);
                            play.setImageResource(R.drawable.pause);
                            next = (ImageButton) findViewById(R.id.next);
                            prev = (ImageButton) findViewById(R.id.prev);


                            play.setOnClickListener(new View.OnClickListener() {
                                @Override

                                public void onClick(View view) {
                                    if (count % 2 == 0) {
                                        MyPlayService.pause();

                                        play = (ImageButton) findViewById(R.id.play);
                                        play.setImageResource(R.drawable.play);

                                        // state=t.getState();
                                        paused=true;
                                        tfrag=0;

                                        count++;


                                    } else {
                                        MyPlayService.resume();

                                        play = (ImageButton) findViewById(R.id.play);
                                        play.setImageResource(R.drawable.pause);

                                        Thread t = new MyThread();
                                        paused=false;
                                        t.start();


                                        count++;
                                    }
                                }
                            });

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (count % 2 != 0) {
                                        play = (ImageButton) findViewById(R.id.play);
                                        play.setImageResource(R.drawable.pause);
                                    }


                                    if (position[0] >= songs.size() - 1) {
                                        position[0] = -1;
                                    }

                                    songInfo = songs.get(++position[0]);


                                    url = songInfo.getSongUrl();
                                    duration = songInfo.getDuration();
                                    int dur = Integer.parseInt(duration);
                                    sec = (dur / 1000);
                                    min = sec / 60;
                                    sec = sec % 60;
                                    hr = min / 60;
                                    min = min % 60;
                                    TextView start = (TextView) findViewById(R.id.start);
                                    start.setText("0:00");
                                    TextView end = (TextView) findViewById(R.id.end);
                                    if (hr >= 1) {
                                        if (sec == 0) {
                                            end.setText(hr + ":" + min + ":00");

                                        } else {
                                            end.setText(hr + ":" + min + ":" + sec);
                                        }
                                    } else {
                                        if (sec == 0) {
                                            end.setText(min + ":00");

                                        } else {
                                            end.setText(min + ":" + sec);
                                        }
                                    }

                                    seekBar.setProgress(0);
                                    seekBar.setMax(dur);
                                    duration1=0;


                                   // Thread t = new MyThread();
                                   // t.start();


                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                    serviceIntent.putExtra("SUrl", url);
                                    startService(serviceIntent);
                                }
                            });

                            prev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (count % 2 != 0) {
                                        play = (ImageButton) findViewById(R.id.play);
                                        play.setImageResource(R.drawable.pause);
                                    }

                                    if (position[0] <= 0) {

                                        position[0] = songs.size();


                                    }
                                    songInfo = songs.get(--position[0]);


                                    // songInfo=songs.get(position-1);
                                    url = songInfo.getSongUrl();
                                    duration = songInfo.getDuration();
                                    int dur = Integer.parseInt(duration);
                                    sec = (dur / 1000);
                                    min = sec / 60;
                                    sec = sec % 60;
                                    hr = min / 60;
                                    min = min % 60;
                                    TextView start = (TextView) findViewById(R.id.start);

                                    start.setText("0:00");
                                    TextView end = (TextView) findViewById(R.id.end);
                                    if (hr >= 1) {
                                        if (sec == 0) {
                                            end.setText(hr + ":" + min + ":00");

                                        } else {
                                            end.setText(hr + ":" + min + ":" + sec);
                                        }
                                    } else {
                                        if (sec == 0) {
                                            end.setText(min + ":00");

                                        } else {
                                            end.setText(min + ":" + sec);
                                        }
                                    }
                                    seekBar.setProgress(0);
                                    seekBar.setMax(dur);
                                    duration1=0;

                                    //Thread t = new MyThread();


                                    Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                                    serviceIntent.putExtra("SUrl", url);
                                    startService(serviceIntent);
                                }
                            });


                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                            serviceIntent.putExtra("SUrl", url);
                            startService(serviceIntent);
                        } else {
                            // next=false;
                            Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                            stopService(serviceIntent);

                        }
                    }
                };
                handler.postDelayed(r, 100);
            }


           /* private void stopPlay() {

                stopService(serviceIntent);
               // serviceIntent.putExtra("SUrl",url);

            }

            private void playAudio() {
                serviceIntent.putExtra("SUrl",url);
                startService(serviceIntent);

            }*/
        });
        CheckPermission();


    }

    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    return;
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    return;
                }
            } else {
                loadSongs();
            }
        } else {
            loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadSongs();
                } else {
                    CheckPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    private void loadSongs() {
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = contentResolver.query(uri, null, selection, null, MediaStore.Audio.Media.TITLE+" ASC");



        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    if (artist == null) {
                        artist = "Unknown Artist";
                    }
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String dur = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                  String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));




                    SongInfo s = new SongInfo(name, artist, url, dur,id);
                    songs.add(s);

                } while (cursor.moveToNext());
            } else {
            }
            cursor.close();
            songAdapter = new SongAdapter(this, songs);
        }
    }


    public class MyThread extends Thread {


        public void run() {


            while (duration1 < dur-1000) {
                try {
                    if(paused){
                        if(tfrag!=0){
                            start.setText("-:--");
                            end.setText("-:--");
                        }
                        break;

                    }
                    Thread.sleep(1000);

                    duration1 = duration1 + 1000;

                    seekBar.setProgress(duration1);

                    tsec = (duration1 / 1000);
                    tmin = tsec / 60;
                    if(tmin>=1) {
                        tsec = tsec % 60;
                    }
                    thr = tmin / 60;
                    if(thr>=1) {
                        tmin = tmin % 60;
                    }

                    if (thr >= 1 && tmin>=1) {
                        if (tsec >=0 && tsec<=9) {
                           start.setText(thr + ":" + tmin + ":0"+tsec);

                        } else {
                            start.setText(thr + ":" + tmin + ":" + tsec);
                        }
                    } else if(tmin>=1) {
                        if (tsec >=0 && tsec<=9) {
                            start.setText(tmin + ":0"+tsec);

                        } else {
                            start.setText(tmin + ":" + tsec);
                        }
                    }
                    else {
                        if (tsec >=0 && tsec<=9){
                            start.setText("0" + ":0" +tsec);

                        }

                        else {
                            start.setText("0" + ":" + tsec);
                        }
                    }



                } catch (Exception e) {
                }

            }



        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    public void clicked1(View v){

        tfrag=1;

        paused=true;


        try {
            ImageButton mic1=v.findViewById(R.id.mirchi);
            if(count1!=0){
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                stopService(serviceIntent);
                count1=0;
            }
            else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                count1=1;
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                serviceIntent.putExtra("SUrl", "http://prclive1.listenon.in:9960");

                startService(serviceIntent);
            }

        }catch (Exception e){


        }

    }


    public void clicked2(View v){
        tfrag=1;

        paused=true;

        try {
            ImageButton mic2=v.findViewById(R.id.insanity);
            if(count2!=0){
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                stopService(serviceIntent);
                count2=0;

            }
            else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                count2=1;
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);

                serviceIntent.putExtra("SUrl", "https://stream.cor.insanityradio.com/insanity192.mp3");

                startService(serviceIntent);
            }

        }catch (Exception e){


        }

    }
    public void clicked3(View v){

        tfrag=1;

        paused=true;





        try {
            ImageButton mic3=v.findViewById(R.id.hsl);
            if(count3!=0){
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count3=0;
                stopService(serviceIntent);
            }
            else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count3=1;
                serviceIntent.putExtra("SUrl", "http://50.7.68.251:7064/stream");

                startService(serviceIntent);
            }

        }catch (Exception e){


        }

    }
    public void clicked4(View v){
        tfrag=1;

        paused=true;


        try {
            ImageButton mic4=v.findViewById(R.id.wyep);
            if(count4!=0){
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count4=0;
                stopService(serviceIntent);
            }
            else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count4=1;
                serviceIntent.putExtra("SUrl", "http://16893.live.streamtheworld.com:3690/WYEPFM_SC?DIST=TuneIn&TGT=TuneIn&maxServers=2&tdtok=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImtpZCI6ImZTeXA4In0.eyJpc3MiOiJ0aXNydiIsInN1YiI6I");

                startService(serviceIntent);
            }

        }catch (Exception e){


        }

    }
    public void clicked5(View v) {
        tfrag=1;
        paused = true;



        try {

            ImageButton mic5=v.findViewById(R.id.z95);
            if (count5!=0) {
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count5=0;
                stopService(serviceIntent);
            } else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count5=1;
                serviceIntent.putExtra("SUrl", "http://newcap.leanstream.co/CKZZFM-MP3?args=tunein_01");
            }

        } catch (Exception e) {


        }

    }

    public void clicked6(View v) {


            tfrag=1;
            paused = true;


        try {
            ImageButton mic6=v.findViewById(R.id.capital);
            if (count6!=0) {
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count6=0;
                stopService(serviceIntent);
            } else {
                TextView start = (TextView) findViewById(R.id.start);
                start.setText("-:--");
                TextView end = (TextView) findViewById(R.id.end);
                end.setText("-:--");
                seekBar.setProgress(0);
                seekBar.setMax(0);
                Intent serviceIntent = new Intent(MainActivity.this, MyPlayService.class);
                count6=1;
                serviceIntent.putExtra("SUrl", "http://media-ice.musicradio.com/CapitalSouthWalesMP3");
            }

        } catch (Exception e) {


        }

    }
    public void facebook(View view){

                Uri uri=Uri.parse("http://www.facebook.com/arbaz.sheikh.779");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }

    public void instagram(View view){


                Uri uri=Uri.parse("http://Instagram.com/arbaz_sheikh01");
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }

    }






