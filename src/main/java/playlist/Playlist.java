package playlist;

/**
 * The user's ordered list of songs: add, remove, move, shuffle, read.
 * Duplicates are allowed.
 *
 * @author Kim Delgadillo
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
        if (song == null)
        {
            throw new IllegalArgumentException("Song cannot be null.");
        }

        songs.addToEnd(song);
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
        return songs.remove(song);
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
        if (from < 0 || from >= songs.size()
            || to < 0 || to >= songs.size())
        {
            throw new IndexOutOfBoundsException(
                "Valid range: 0 to " + (songs.size() - 1));
        }

        Song song = songs.removeAt(from);
        songs.addAt(to, song);
    }

    /**
     * Shuffles with Fisher-Yates: toArray, shuffle the array, clear, and
     * re-add with addToEnd. No-op if size is less than 2. No
     * adjacent-duplicate guarantee. O(n^2) as written.
     */
    public void shuffle()
    {
        if (songs.size() < 2)
        {
            return;
        }

        Song[] array = songs.toArray(new Song[songs.size()]);

        java.util.Random random = new java.util.Random();

        for (int i = array.length - 1; i > 0; i--)
        {
            int j = random.nextInt(i + 1);

            Song temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        songs.clear();

        for (Song song : array)
        {
            songs.addToEnd(song);
        }
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
        return songs.get(i);
    }

    /**
     * Gets the number of songs. O(1).
     *
     * @return songs.size()
     */
    public int size()
    {
        return songs.size();
    }
}
