package aduiocatalog;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<Integer> itemIds;

    public Playlist(String name) {
        this.name = name;
        this.itemIds = new ArrayList<>();
    }

    public Playlist() {
        this.itemIds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void addById(int id) {
        itemIds.add(id);
    }

    public boolean removeById(int id) {
        return itemIds.removeIf(x -> x == id);
    }

    public int getSize() {
        return itemIds.size();
    }

    @Override
    public String toString() {
        return String.format("Playlist: %s (%d items)", name, getSize());
    }
}
