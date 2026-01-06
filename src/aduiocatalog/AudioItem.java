package aduiocatalog;

public class AudioItem {
    private static int NEXT_ID = 1;

    private int id;
    private String title;
    private String genre;
    private int duration;
    private String artist;
    private int year;
    private AudioType category;

    public AudioItem(String title, String genre, int duration, String artist, int year, AudioType category) {
        this.id = NEXT_ID++;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.artist = artist;
        this.year = year;
        this.category = category;
    }

    public AudioItem() {
    }

    public int getId() {
        return id;
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

    public AudioType getCategory() {
        return category;
    }

    public static void setNextId(int nextId) {
        NEXT_ID = nextId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | [%s] Title: %s | Author: %s | Genre: %s | Year: %d | Duration: %ds", id, getCategory(), title, artist, genre, year, duration);
    }

}
