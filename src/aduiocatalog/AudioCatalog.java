package aduiocatalog;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AudioCatalog {

    private Map<Integer, AudioItem> items;
    private Map<String, Playlist> playlists;

    public AudioCatalog() {
        this.items = new HashMap<>();
        this.playlists = new HashMap<>();
    }

    public void add(AudioItem item) {
        items.put(item.getId(), item);
    }

    public boolean removeById(int id) {
        for (Playlist p : playlists.values()) {
            p.removeById(id);
        }
        return items.remove(id) != null;
    }

    public AudioItem getById(int id) {
        return items.get(id);
    }

    public Collection<AudioItem> getAll() {
        return items.values();
    }

    public List<AudioItem> searchByTitle(String title) {
        String q = title.toLowerCase();
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getTitle().toLowerCase().contains(q)) result.add(item);
        }
        return result;
    }

    public List<AudioItem> searchByArtist(String artist) {
        String q = artist.toLowerCase();
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getArtist().toLowerCase().contains(q)) result.add(item);
        }
        return result;
    }

    public List<AudioItem> searchByGenre(String genre) {
        String q = genre.toLowerCase();
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getGenre().toLowerCase().contains(q)) result.add(item);
        }
        return result;
    }

    public List<AudioItem> filterByCategory(AudioType category) {
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getCategory() == category) result.add(item);
        }
        return result;
    }

    public List<AudioItem> filterByGenreExact(String genre) {
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getGenre().equalsIgnoreCase(genre)) result.add(item);
        }
        return result;
    }

    public List<AudioItem> filterByYear(int year) {
        List<AudioItem> result = new ArrayList<>();
        for (AudioItem item : items.values()) {
            if (item.getYear() == year) result.add(item);
        }
        return result;
    }

    public List<AudioItem> sortByTitle() {
        List<AudioItem> list = new ArrayList<>(items.values());
        list.sort(Comparator.comparing(AudioItem::getTitle, String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public List<AudioItem> sortByYear() {
        List<AudioItem> list = new ArrayList<>(items.values());
        list.sort(Comparator.comparingInt(AudioItem::getYear));
        return list;
    }

    public List<AudioItem> sortByDuration() {
        List<AudioItem> list = new ArrayList<>(items.values());
        list.sort(Comparator.comparingInt(AudioItem::getDuration));
        return list;
    }

    public boolean createPlaylist(String name) {
        String key = name.trim();
        if (key.isEmpty() || playlists.containsKey(key.toLowerCase())) return false;
        playlists.put(key.toLowerCase(), new Playlist(key));
        return true;
    }

    public Collection<Playlist> getAllPlaylists() {
        return playlists.values();
    }

    public Playlist getPlaylist(String name) {
        if (name == null) return null;
        return playlists.get(name.trim().toLowerCase());
    }

    public boolean addItemToPlaylist(String playlistName, int itemId) {
        Playlist p = getPlaylist(playlistName);
        if (p == null) return false;
        if (!items.containsKey(itemId)) return false;
        p.addById(itemId);
        return true;
    }

    public boolean removeItemFromPlaylist(String playlistName, int itemId) {
        Playlist p = getPlaylist(playlistName);
        if (p == null) return false;
        return p.removeById(itemId);
    }

    public int getPlaylistTotalDuration(String playlistName) {
        Playlist p = getPlaylist(playlistName);
        if (p == null) return 0;
        int sum = 0;
        for (int id : p.getItemIds()) {
            AudioItem it = items.get(id);
            if (it != null) sum += it.getDuration();
        }
        return sum;
    }

    public List<AudioItem> getPlaylistItems(String playlistName) {
        Playlist p = getPlaylist(playlistName);
        if (p == null) return List.of();
        List<AudioItem> result = new ArrayList<>();
        for (int id : p.getItemIds()) {
            AudioItem it = items.get(id);
            if (it != null) result.add(it);
        }
        return result;
    }

    public void saveCatalogToJson(String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(items, writer);
        } catch (IOException e) {
            System.out.println("Error saving catalog.");
        }
    }

    public void loadCatalogFromJson(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(fileName)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            Map<Integer, AudioItem> loaded = new HashMap<>();
            int maxId = 0;

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                int idKey = Integer.parseInt(entry.getKey());
                JsonObject obj = entry.getValue().getAsJsonObject();

                String title = obj.get("title").getAsString();
                String genre = obj.get("genre").getAsString();
                int duration = obj.get("duration").getAsInt();
                String artist = obj.get("artist").getAsString();
                int year = obj.get("year").getAsInt();
                AudioType category = AudioType.valueOf(obj.get("category").getAsString().toUpperCase());

                AudioItem item = switch (category) {
                    case SONG -> new Song(title, genre, duration, artist, year);
                    case PODCAST -> new Podcast(title, genre, duration, artist, year);
                    case AUDIOBOOK -> new AudioBook(title, genre, duration, artist, year);
                    case ALBUM -> new Album(title, genre, duration, artist, year);
                };

                item.setId(idKey);
                loaded.put(idKey, item);
                maxId = Math.max(maxId, idKey);
            }

            this.items = loaded;
            AudioItem.setNextId(maxId + 1);

        } catch (Exception e) {
            System.out.println("Error loading catalog.");
            e.printStackTrace();
        }
    }

    public void savePlaylistsToJson(String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(playlists, writer);
        } catch (IOException e) {
            System.out.println("Error saving playlists.");
        }
    }

    public void loadPlaylistsFromJson(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Playlist>>() {
            }.getType();
            Map<String, Playlist> loaded = gson.fromJson(reader, type);
            if (loaded != null) this.playlists = loaded;
        } catch (IOException e) {
            System.out.println("Error loading playlists.");
        }
    }
}
