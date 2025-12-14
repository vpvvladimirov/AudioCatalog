package aduiocatalog;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private final String name;
    private final List<AudioItem> items;

    public Playlist(String name) {
        this.name = name;
        this.items = new ArrayList<AudioItem>();
    }

    public String getName() {
        return name;
    }

    public List<AudioItem> getItems() {
        return items;
    }

    public void add(AudioItem item) {
        items.add(item);
    }

    public boolean remove(String title) {
        return items.removeIf(i -> i.getTitle().equalsIgnoreCase(title));
    }

    public int getDuration() {
        return items.stream().mapToInt(AudioItem::getDuration).sum();
    }

    public int getSize() {
        return items.size();
    }

    public String toString() {
        return String.format("Playlist: %s (%d items, total %ds)", name, getSize(), getDuration());
    }
}
