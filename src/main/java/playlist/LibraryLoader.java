package playlist;

/**
 * Reads the song-library file and builds a chain of albums. Skips and
 * reports malformed lines. Stateless.
 *
 * File format, one song per line: title|artist|album|durationSec
 *
 * @author Person D
 * @version October 2026
 */
public class LibraryLoader
{
    /**
     * Reads the library file and groups songs into albums (using indexOf
     * to find an existing album). A missing file is reported and gives
     * an empty library; a bad line is reported by line number, skipped,
     * and loading continues. O(n^2) worst case.
     *
     * @param path the path to the library file
     * @return the albums, possibly empty; never null
     */
    public static LinkedChain<Album> loadLibrary(String path)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
