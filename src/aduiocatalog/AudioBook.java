package aduiocatalog;

public class AudioBook extends AudioItem {
    public AudioBook(String title, String genre, int duration, String artist, int year) {
        super(title, genre, duration, artist, year);
    }

    @Override
    public String getCategory() {
        return "Audio Book";
    }
}
