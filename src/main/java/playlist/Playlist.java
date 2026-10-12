package playlist;

/**
 * The user's ordered list of songs: add, remove, move, shuffle, read.
 * Duplicates are allowed.
 *
 * @author Person C
 * @version October 2026
 */
public class Playlist
{
    private String name;
    private LinkedChain<Song> songs;

    /**
     * Creates an empty playlist. O(1).
     *
     * @param name the playlist name
     */
    public Playlist(String name)
    {
        this.name = name;
        this.songs = new LinkedChain<Song>();
    }

    /**
     * Appends a song to the end. O(n).
     *
     * @param song the song to add
     * @throws IllegalArgumentException if song is null
     */
    public void addSong(Song song)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes the first matching song. O(n).
     *
     * @param song the song to remove
     * @return true if removed, or false if absent or the playlist is
     *         empty
     */
    public boolean removeSong(Song song)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Moves the song at from so it lands at to: removeAt(from) then
     * addAt(to, song). Both indices are checked before removing, so the
     * playlist is unchanged on error. O(n).
     *
     * @param from the current index, 0 to size()-1
     * @param to the target index, 0 to size()-1
     * @throws IndexOutOfBoundsException naming the valid range if either
     *         index is out of range
     */
    public void moveSong(int from, int to)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Shuffles with Fisher-Yates: toArray, shuffle the array, clear, and
     * re-add with addToEnd. No-op if size is less than 2. No
     * adjacent-duplicate guarantee. O(n^2) as written.
     */
    public void shuffle()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the song at index i via songs.get(i). O(n).
     *
     * @param i the zero-based index
     * @return the song at i
     * @throws IndexOutOfBoundsException if i is out of range
     */
    public Song getSong(int i)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the number of songs. O(1).
     *
     * @return songs.size()
     */
    public int size()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
