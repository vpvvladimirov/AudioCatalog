package aduiocatalog;

public class Podcast extends AudioItem {
    public Podcast(String title, String genre, int duration, String artist, int year) {
        super(title, genre, duration, artist, year);
    }

    @Override
    public String getCategory() {
        return "Podcast";
    }
}
