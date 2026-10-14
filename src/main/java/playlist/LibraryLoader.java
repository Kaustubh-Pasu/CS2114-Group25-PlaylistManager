package playlist;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;
/**
 * Reads the song-library file and builds a chain of albums. Skips and
 * reports malformed lines. Stateless.
 *
 * File format, one song per line: title|artist|album|durationSec
 *
 * @author Krish Joshi
 * @version October 2026
 */
public class LibraryLoader {

    /**
     * Prevents instantiation of this utility class.
     */
    private LibraryLoader() {}

    /**
     * Loads the song library from the given file path using the default stderr reporter.
     *
     * @param path the file path to the library data source
     * @return a linked chain of albums parsed from the file; empty if the file cannot be read
     */
    public static LinkedChain<Album> loadLibrary(String path) {
        return loadLibrary(path, System.err::println);
    }

    /**
     * Loads the song library from the given file path and reports malformed records through the
     * supplied consumer.
     *
     * @param path the file path to the library data source
     * @param report callback used to print warnings about skipped or unreadable lines
     * @return a linked chain of albums parsed from the file; empty if the file cannot be read
     */
    public static LinkedChain<Album> loadLibrary(String path, Consumer<String> report) {
        LinkedChain<Album> library = new LinkedChain<>();
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNo = 0;
            while ((line = in.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) continue;
                try {
                    addLine(library, line);
                } catch (IllegalArgumentException e) {
                    report.accept("Line " + lineNo + " skipped: " + e.getMessage());
                }
            }
        } catch (IOException e) {            // includes FileNotFoundException
            report.accept("Library file not found or unreadable: " + path);
            return new LinkedChain<>();
        }
        return library;
    }

    /**
     * Parses a single song entry and adds it to the correct album in the library.
     *
     * @param library the collection of albums being built
     * @param line a single song record in the format title|artist|album|durationSec
     * @throws IllegalArgumentException if the line does not contain exactly four fields or the
     *         duration is not a valid integer
     */
    private static void addLine(LinkedChain<Album> library, String line) {
        String[] f = line.split("\\|", -1);
        if (f.length != 4) {
            throw new IllegalArgumentException("expected 4 fields but found " + f.length);
        }
        int duration;
        try {
            duration = Integer.parseInt(f[3].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("duration is not a number: '" + f[3].trim() + "'");
        }
        // Song constructor validates; Album.addTrack may reject duplicates
        Song song = new Song(f[0].trim(), f[1].trim(), f[2].trim(), duration);

        Album probe = new Album(f[2].trim(), f[1].trim());
        int idx = library.indexOf(probe);
        if (idx >= 0) {
            library.get(idx).addTrack(song);
        } else {
            probe.addTrack(song);
            library.add(probe);
        }
    }
}
