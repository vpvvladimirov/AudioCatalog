package aduiocatalog;

public class Song extends AudioItem {
    public Song(String title, String genre, int duration, String artist, int year) {
        super(title, genre, duration, artist, year, AudioType.SONG);
    }
}
