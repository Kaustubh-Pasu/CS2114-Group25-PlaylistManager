package playlist;

/**
 * Stateless yes/no checks on raw user input. No printing, no storage.
 *
 * @author Person B
 * @version October 2026
 */
public class InputValidator
{
    /** Maximum allowed title length, in characters. */
    public static final int MAX_TITLE_LENGTH = 200;

    /**
     * Checks a title. O(1).
     *
     * @param text the title to check
     * @return true if text is non-null, non-blank and at most
     *         MAX_TITLE_LENGTH characters
     */
    public static boolean checkTitle(String text)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks an index against a size. O(1).
     *
     * @param i the index to check
     * @param size the collection size
     * @return true if 0 &lt;= i &lt; size
     */
    public static boolean checkIndex(int i, int size)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Checks a song reference. O(1).
     *
     * @param song the song to check
     * @return true if song is non-null
     */
    public static boolean checkSong(Song song)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
