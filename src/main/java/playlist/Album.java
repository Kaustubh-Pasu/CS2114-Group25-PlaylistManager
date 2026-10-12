package playlist;

/**
 * A titled group of tracks loaded from the library file. Tracks keep
 * file order.
 *
 * @author Person B
 * @version October 2026
 */
public class Album
{
    private String title;
    private String artist;
    private LinkedChain<Song> tracks;

    /**
     * Creates an empty album. O(1).
     *
     * @param title the album title
     * @param artist the album artist
     */
    public Album(String title, String artist)
    {
        this.title = title;
        this.artist = artist;
        this.tracks = new LinkedChain<Song>();
    }

    /**
     * Appends a track via addToEnd. O(n).
     *
     * @param song the track to add
     * @throws IllegalArgumentException if song is null
     */
    public void addTrack(Song song)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the tracks. O(1).
     *
     * @return the chain of tracks in file order
     */
    public LinkedChain<Song> getTracks()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * True if other is an Album with the same title and artist. O(1).
     *
     * @param other the object to compare with
     * @return true if equal, or false if not
     */
    @Override
    public boolean equals(Object other)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a hash code consistent with equals. O(1).
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the title, artist and track count, e.g. "... 2 tracks".
     * O(1).
     *
     * @return the display string
     */
    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
