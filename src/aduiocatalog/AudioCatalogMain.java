package aduiocatalog;

import java.util.List;
import java.util.Scanner;

public class AudioCatalogMain {

    public static void main(String[] args) {

        AudioCatalog catalog = new AudioCatalog();
        catalog.loadCatalogFromJson("catalog.json");
        catalog.loadPlaylistsFromJson("playlists.json");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== AUDIO CATALOG =====");
            System.out.println("1. Add Song");
            System.out.println("2. Add Podcast");
            System.out.println("3. Add Audiobook");
            System.out.println("4. Add Album");
            System.out.println("5. Show all audio");
            System.out.println("6. Search (title/artist/genre)");
            System.out.println("7. Filter (category/genre/year)");
            System.out.println("8. Sort (title/year/duration)");
            System.out.println("9. Remove by ID");
            System.out.println("10. Playlists menu");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = readInt(sc);

            switch (choice) {

                case 1 -> catalog.add(readItem(sc, AudioType.SONG));
                case 2 -> catalog.add(readItem(sc, AudioType.PODCAST));
                case 3 -> catalog.add(readItem(sc, AudioType.AUDIOBOOK));
                case 4 -> catalog.add(readItem(sc, AudioType.ALBUM));

                case 5 -> {
                    System.out.println("\n--- All Audio ---");
                    for (AudioItem item : catalog.getAll()) System.out.println(item);
                }

                case 6 -> {
                    System.out.println("Search by: 1) Title  2) Artist  3) Genre");
                    int s = readInt(sc);
                    System.out.print("Query: ");
                    String q = sc.nextLine();

                    List<AudioItem> result = switch (s) {
                        case 1 -> catalog.searchByTitle(q);
                        case 2 -> catalog.searchByArtist(q);
                        case 3 -> catalog.searchByGenre(q);
                        default -> List.of();
                    };

                    if (result.isEmpty()) System.out.println("No results.");
                    else result.forEach(System.out::println);
                }

                case 7 -> {
                    System.out.println("Filter by: 1) Category  2) Genre  3) Year");
                    int f = readInt(sc);

                    List<AudioItem> result = List.of();
                    if (f == 1) {
                        System.out.print("Category (SONG/PODCAST/AUDIOBOOK/ALBUM): ");
                        String input = sc.nextLine();

                        try {
                            AudioType type = AudioType.valueOf(input.trim().toUpperCase());
                            result = catalog.filterByCategory(type);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid category.");
                            result = List.of();
                        }
                    } else if (f == 2) {
                        System.out.print("Genre: ");
                        result = catalog.filterByGenreExact(sc.nextLine());
                    } else if (f == 3) {
                        System.out.print("Year: ");
                        int y = readInt(sc);
                        result = catalog.filterByYear(y);
                    }

                    if (result.isEmpty()) System.out.println("No results.");
                    else result.forEach(System.out::println);
                }

                case 8 -> {
                    System.out.println("Sort by: 1) Title  2) Year  3) Duration");
                    int s = readInt(sc);

                    List<AudioItem> sorted = switch (s) {
                        case 1 -> catalog.sortByTitle();
                        case 2 -> catalog.sortByYear();
                        case 3 -> catalog.sortByDuration();
                        default -> List.of();
                    };

                    if (sorted.isEmpty()) System.out.println("Nothing to show.");
                    else sorted.forEach(System.out::println);
                }

                case 9 -> {
                    System.out.print("Enter ID to remove: ");
                    int id = readInt(sc);

                    if (catalog.removeById(id)) System.out.println("Removed.");
                    else System.out.println("ID not found.");
                }

                case 10 -> playlistsMenu(sc, catalog);

                case 0 -> {
                    catalog.saveCatalogToJson("catalog.json");
                    catalog.savePlaylistsToJson("playlists.json");
                    return;
                }

                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void playlistsMenu(Scanner sc, AudioCatalog catalog) {
        while (true) {
            System.out.println("\n===== PLAYLISTS =====");
            System.out.println("1. Create playlist");
            System.out.println("2. List playlists");
            System.out.println("3. Show playlist");
            System.out.println("4. Add item to playlist (by ID)");
            System.out.println("5. Remove item from playlist (by ID)");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int c = readInt(sc);
            switch (c) {
                case 1 -> {
                    System.out.print("Playlist name: ");
                    String name = sc.nextLine();
                    if (catalog.createPlaylist(name)) System.out.println("Created.");
                    else System.out.println("Could not create (empty or already exists).");
                }
                case 2 -> {
                    if (catalog.getAllPlaylists().isEmpty()) {
                        System.out.println("No playlists.");
                    } else {
                        for (Playlist p : catalog.getAllPlaylists()) System.out.println(p);
                    }
                }
                case 3 -> {
                    System.out.print("Playlist name: ");
                    String name = sc.nextLine();
                    Playlist p = catalog.getPlaylist(name);
                    if (p == null) {
                        System.out.println("Not found.");
                        break;
                    }
                    System.out.println(p);
                    int dur = catalog.getPlaylistTotalDuration(name);
                    System.out.println("Total duration: " + dur + "s");
                    List<AudioItem> items = catalog.getPlaylistItems(name);
                    if (items.isEmpty()) System.out.println("(empty)");
                    else items.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Playlist name: ");
                    String name = sc.nextLine();
                    System.out.print("Item ID: ");
                    int id = readInt(sc);
                    if (catalog.addItemToPlaylist(name, id)) System.out.println("Added.");
                    else System.out.println("Failed (check playlist name or ID).");
                }
                case 5 -> {
                    System.out.print("Playlist name: ");
                    String name = sc.nextLine();
                    System.out.print("Item ID: ");
                    int id = readInt(sc);
                    if (catalog.removeItemFromPlaylist(name, id)) System.out.println("Removed.");
                    else System.out.println("Failed.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static AudioItem readItem(Scanner sc, AudioType category) {
        System.out.print("Title: ");
        String title = sc.nextLine();

        System.out.print("Artist: ");
        String artist = sc.nextLine();

        System.out.print("Genre: ");
        String genre = sc.nextLine();

        System.out.print("Year: ");
        int year = readInt(sc);

        System.out.print("Duration (seconds): ");
        int duration = readInt(sc);

        return switch (category) {
            case SONG -> new Song(title, genre, duration, artist, year);
            case PODCAST -> new Podcast(title, genre, duration, artist, year);
            case AUDIOBOOK -> new AudioBook(title, genre, duration, artist, year);
            case ALBUM -> new Album(title, genre, duration, artist, year);
            default -> new AudioItem(title, genre, duration, artist, year, category);
        };
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (Exception e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
