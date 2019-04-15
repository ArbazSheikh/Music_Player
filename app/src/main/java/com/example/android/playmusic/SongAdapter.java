package com.example.android.playmusic;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
    @NonNull
    ArrayList<SongInfo> _songs;
    Context context;



    OnitemClickListener onitemClickListener;
    OnLongitemClickListener onLongitemClickListener;

    public interface OnitemClickListener{
        void OnItemClick(CardView cardView,View v,SongInfo obj,int pos);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener){
        this.onitemClickListener=onitemClickListener;
    }

    public interface OnLongitemClickListener{
        void OnLongItemClick(CardView cardView,View v,SongInfo obj,int pos);
    }

    public void setOnLongitemClickListener(OnLongitemClickListener onLongitemClickListener){
        this.onLongitemClickListener=onLongitemClickListener;
    }




    SongAdapter (Context context,ArrayList<SongInfo> _songs){
        this.context=context;
        this._songs=_songs;

    }





    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View myview= LayoutInflater.from(context).inflate(R.layout.row_layout,viewGroup,false);

        return new SongHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongHolder songHolder, final int i) {
        final SongInfo c=_songs.get(i);
        String sname1,sname2;
        if(c.getSongName().length()>27){
            String sname=c.getSongName().substring(0,27);
             sname1=sname +"...";
            songHolder.songName.setText(sname1);
        }

        else{
            songHolder.songName.setText(c.getSongName());
        }

        if(c.getArtistName().length()>30) {
            String sname=c.getArtistName().substring(0,30);
            sname2=sname +"...";
            songHolder.artist.setText(sname2);
        }
        else {
            songHolder.artist.setText(c.getArtistName());

        }

        Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor music =context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(c.getId())},
                null);


        music.moveToFirst();            //i put only one song in my external storage to keep things simple
        int x=music.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART);
        String thisArt = music.getString(x);



            Bitmap bm = BitmapFactory.decodeFile(thisArt);
            if(bm==null){
                songHolder.album_art.setImageResource(R.drawable.cover);
            }
            else {
                songHolder.album_art.setImageBitmap(bm);
            }








        songHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onitemClickListener!=null){

                    onitemClickListener.OnItemClick(songHolder.cardView,view,c,i);



                }
            }
        });

       songHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               if(onLongitemClickListener!=null){
                   onLongitemClickListener.OnLongItemClick(songHolder.cardView,view,c,i);
               }
               return true;
           }
       });


    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
         TextView songName;
         TextView artist;
        // Button btnAction;
         ImageView album_art;
         CardView cardView;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            songName=(TextView)itemView.findViewById(R.id.tvSongName);
            artist=(TextView)itemView.findViewById(R.id.tvArtistName);
          //  btnAction=(Button) itemView.findViewById(R.id.btnAction);
            album_art=(ImageView) itemView.findViewById(R.id.album_art);
            cardView=(CardView) itemView.findViewById(R.id.card);


        }
        }
    }


