package aduiocatalog;

public class Album extends AudioItem {
    public Album(String title, String genre, int duration, String artist, int year) {
        super(title, genre, duration, artist, year);
    }

    @Override
    public String getCategory() {
        return "Album";
    }
}
