package playlist;
import java.util.Scanner;

/**
 * Console menu: reads input, calls the other classes, prints results and
 * errors.
 *
 * @author Krish Joshi
 * @version October 2026
 */
public class PlaylistApp {

    /**
     * The menu text displayed to the user in the console.
     */
    static final String MENU = String.join("\n",
            "",
            "1) Browse library   2) Add song      3) Remove song   4) Move song",
            "5) Shuffle          6) Show playlist 7) Play          8) Pause",
            "9) Next            10) Previous      0) Quit");

    /**
     * Indicates that the console input stream has been closed.
     */
    private static class InputClosed extends RuntimeException {}

    private final MusicPlayer player;
    private final Playlist playlist;
    private final LinkedChain<Album> library;
    private final Scanner in = new Scanner(System.in);

    /**
     * Creates a command-line interface bound to a library and playlist.
     *
     * @param library the song library available for browsing and lookup
     * @param playlist the playlist managed by this application
     */
    public PlaylistApp(LinkedChain<Album> library, Playlist playlist) {
        this.library = library;
        this.playlist = playlist;
        this.player = new MusicPlayer(playlist);
    }

    /**
     * Starts the application using the default library file or a user-provided path.
     *
     * @param args optional first argument specifying a custom library path
     */
    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : "data/library.txt";
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(path, System.out::println);
        new PlaylistApp(lib, new Playlist("My Playlist")).showMenu();
    }

    /**
     * Runs the main console loop and repeatedly displays the menu until the user exits or input
     * ends.
     */
    public void showMenu() {
        try {
            while (true) {
                System.out.println(MENU);
                System.out.print("Choice: ");
                String choice = readLine().trim();
                try {
                    if (choice.equals("0")) return;
                    dispatch(choice);
                } catch (InputClosed e) {
                    throw e;
                } catch (RuntimeException e) {          // safety net: a data-layer bug must not crash the app
                    System.out.println("Something went wrong: " + e.getMessage());
                }
            }
        } catch (InputClosed e) {
            // end of input: leave quietly
        }
    }

    /**
     * Executes the action associated with a single menu choice.
     *
     * @param choice the user's menu selection string
     */
    private void dispatch(String choice) {
        switch (choice) {
            case "1":  browse();        break;
            case "2":  addSong();       break;
            case "3":  removeSong();    break;
            case "4":  moveSong();      break;
            case "5":  shuffle();       break;
            case "6":  showPlaylist();  break;
            case "7":  play();          break;
            case "8":  player.pause(); System.out.println("Paused"); break;
            case "9":  next();          break;
            case "10": previous();      break;
            default:   System.out.println("Invalid option");
        }
    }

    /**
     * Prints all library albums and their songs in a readable format.
     */
    private void browse() {
        if (library.isEmpty()) { System.out.println("Library is empty"); return; }
        Album[] albums = library.toArray(new Album[library.size()]);
        for (Album a : albums) {
            System.out.println(a);
            LinkedChain<Song> t = a.getTracks();
            Song[] songs = t.toArray(new Song[t.size()]);
            for (Song s : songs) System.out.println("    " + s);
        }
    }

    /**
     * Adds a song to the playlist after locating it in the library by title.
     */
    private void addSong() {
        String title = promptTitle();
        Song song = findInLibrary(title);
        if (!InputValidator.checkSong(song)) { System.out.println("Song not found"); return; }
        playlist.addSong(song);
        System.out.println("Added: " + song);
    }

    /**
     * Removes a song from the playlist when the user enters a valid title.
     */
    private void removeSong() {
        if (playlist.size() == 0) { System.out.println("Playlist is empty"); return; }
        String title = promptTitle();
        Song song = findInPlaylist(title);
        if (song == null || !playlist.removeSong(song)) { System.out.println("Song not found"); return; }
        System.out.println("Removed: " + song);
    }

    /**
     * Moves a playlist item from one index to another.
     */
    private void moveSong() {
        if (playlist.size() == 0) { System.out.println("Playlist is empty"); return; }
        int from = promptIndex("From position", playlist.size());
        int to = promptIndex("To position", playlist.size());
        playlist.moveSong(from, to);
        System.out.println("Moved");
    }

    /**
     * Randomizes the current playlist order when at least two songs exist.
     */
    private void shuffle() {
        if (playlist.size() < 2) return;                // silent no-op
        playlist.shuffle();
        System.out.println("Shuffled");
    }

    /**
     * Displays the playlist with numbered entries.
     */
    private void showPlaylist() {
        if (playlist.size() == 0) { System.out.println("Playlist is empty"); return; }
        for (int i = 0; i < playlist.size(); i++) System.out.println(i + ": " + playlist.getSong(i));
    }

    /**
     * Starts playback if the playlist contains at least one song.
     */
    private void play() {
        if (!player.play()) { System.out.println("Nothing to play"); return; }
        System.out.println("Now playing: " + player.getCurrentSong());
    }

    /**
     * Advances to the next track in the current playlist.
     */
    private void next() {
        if (!player.nextTrack()) {
            System.out.println(stuckMessage("End of playlist reached"));
            return;
        }
        System.out.println("Now playing: " + player.getCurrentSong());
    }

    /**
     * Moves to the previous track in the current playlist.
     */
    private void previous() {
        if (!player.previousTrack()) {
            System.out.println(stuckMessage("Start of playlist reached"));
            return;
        }
        System.out.println("Now playing: " + player.getCurrentSong());
    }

    /**
     * Explains why next or previous could not move.
     *
     * @param atEdge the message to use when a song is selected
     * @return "Nothing to play", "Press Play to start", or atEdge
     */
    private String stuckMessage(String atEdge) {
        if (playlist.size() == 0) return "Nothing to play";
        if (player.getCurrentSong() == null) return "Press Play to start";
        return atEdge;
    }

    // ---------- input helpers: validate and re-prompt ----------

    /**
     * Reads the next non-closed line from standard input.
     *
     * @return the next line entered by the user
     * @throws InputClosed if the input stream has ended
     */
    private String readLine() {
        if (!in.hasNextLine()) throw new InputClosed();
        return in.nextLine();
    }

    /**
     * Prompts for a song title until the user enters a valid one.
     *
     * @return a trimmed song title that passes validation
     */
    private String promptTitle() {
        while (true) {
            System.out.print("Song title: ");
            String t = readLine();
            if (InputValidator.checkTitle(t)) return t.trim();
            System.out.println("Title must be 1-200 characters");
        }
    }

    /**
     * Prompts for a playlist index until the user enters a valid position.
     *
     * @param label the prompt label shown to the user
     * @param size the number of available positions in the playlist
     * @return a valid zero-based index within the playlist
     */
    private int promptIndex(String label, int size) {
        while (true) {
            System.out.print(label + ": ");
            String raw = readLine().trim();
            try {
                int i = Integer.parseInt(raw);
                if (InputValidator.checkIndex(i, size)) return i;
            } catch (NumberFormatException ignored) {
                // fall through to the message below
            }
            System.out.println("Enter a position from 0 to " + (size - 1));
        }
    }

    // ---------- lookups (titles, case-insensitive; toArray once per the traversal rule) ----------

    /**
     * Finds a song in the library by title, ignoring case differences.
     *
     * @param title the song title to search for
     * @return the matching song if present; otherwise null
     */
    private Song findInLibrary(String title) {
        Album[] albums = library.toArray(new Album[library.size()]);
        for (Album a : albums) {
            LinkedChain<Song> t = a.getTracks();
            Song[] songs = t.toArray(new Song[t.size()]);
            for (Song s : songs) if (s.getTitle().equalsIgnoreCase(title)) return s;
        }
        return null;
    }

    /**
     * Finds a song in the current playlist by title, ignoring case differences.
     *
     * @param title the song title to search for
     * @return the matching song if present; otherwise null
     */
    private Song findInPlaylist(String title) {
        for (int i = 0; i < playlist.size(); i++) {
            Song s = playlist.getSong(i);
            if (s.getTitle().equalsIgnoreCase(title)) return s;
        }
        return null;
    }
}