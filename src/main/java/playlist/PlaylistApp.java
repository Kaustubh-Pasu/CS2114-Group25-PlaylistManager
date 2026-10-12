package playlist;

/**
 * Console menu: reads input, calls the other classes, prints results and
 * errors.
 *
 * @author Person D
 * @version October 2026
 */
public class PlaylistApp
{
    private MusicPlayer player;
    private LinkedChain<Album> library; // loaded at startup

    /**
     * Loads the library, builds the player, and runs the menu loop. If
     * the library file is missing, the app still starts with an empty
     * library.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Prints the options, reads a choice, and dispatches it. Prints
     * "Invalid option" and re-prompts on an invalid choice. O(1) per
     * prompt.
     */
    public void showMenu()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
