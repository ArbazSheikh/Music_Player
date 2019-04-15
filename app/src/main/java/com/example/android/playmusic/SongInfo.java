package com.example.android.playmusic;

public class SongInfo {
    public String songName,artistName,songUrl,coverPath,duration,id;

    public SongInfo(String songName, String artistName, String songUrl,String duration,String id) {
        this.songName = songName;
        this.artistName = artistName;

        this.songUrl = songUrl;
       this.coverPath=coverPath;
       this.duration=duration;
       this.id=id;
    }
    public SongInfo(){

    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public String getCoverpath() {
        return coverPath;
    }

    public String getDuration() {
        return duration;
    }
    public String getId(){
        return id;
    }


}
