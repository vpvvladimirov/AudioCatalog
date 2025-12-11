package aduiocatalog;

public abstract class AudioItem {
    private final String title;
    private final String genre;
    private final int duration;
    private final String artist;
    private final int year;

    public AudioItem(String title, String genre, int duration, String artist, int year) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.artist = artist;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] Title: %s | Author: %s | Genre: %s | Year: %d | Duration: %ds", getCategory(), title, artist, genre, year, duration);
    }

}
