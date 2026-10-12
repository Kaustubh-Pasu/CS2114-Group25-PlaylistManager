package playlist;

/**
 * Immutable data for one track, plus equality and display.
 *
 * Invariant: title, artist and album are non-blank and durationSec is
 * greater than 0.
 *
 * @author Person B
 * @version October 2026
 */
public class Song
{
    private final String title;
    private final String artist;
    private final String album;
    private final int durationSec;

    /**
     * Builds a song. O(1).
     *
     * @param title the song title; must not be null or blank
     * @param artist the artist name; must not be null or blank
     * @param album the album title; must not be null or blank
     * @param durationSec the length in seconds; must be greater than 0
     * @throws IllegalArgumentException if any string is null or blank,
     *         or durationSec is 0 or less
     */
    public Song(String title, String artist, String album, int durationSec)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the title. O(1).
     *
     * @return the title
     */
    public String getTitle()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the artist. O(1).
     *
     * @return the artist
     */
    public String getArtist()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the album title. O(1).
     *
     * @return the album title
     */
    public String getAlbum()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the duration in seconds. O(1).
     *
     * @return the duration in seconds
     */
    public int getDurationSec()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * True if other is a Song with the same title, artist, album and
     * duration; false for null or other types. O(1).
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
     * Returns "Title - Artist (m:ss)", e.g.
     * "Espresso - Sabrina Carpenter (2:55)". O(1).
     *
     * @return the display string
     */
    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
