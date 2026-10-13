package playlist;

/**
 * Stateless yes/no checks on raw user input. No printing, no storage.
 *
 * @author Colin Fahmy
 * @version October 2026
 */
public class InputValidator
{
    public static final int MAX_TITLE_LENGTH = 200;

    public static boolean checkTitle(String text)
    {
        return text != null
            && !text.isBlank()
            && text.length() <= MAX_TITLE_LENGTH;
    }

    public static boolean checkIndex(int i, int size)
    {
        return i >= 0 && i < size;
    }

    public static boolean checkSong(Song song)
    {
        return song != null;
    }
}
