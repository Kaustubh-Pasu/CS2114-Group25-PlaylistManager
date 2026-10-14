package playlist;

/**
 * Simulated playback state: which playlist position is current, and
 * whether it is playing or paused.
 *
 * Invariant: currentIndex is -1 or in [0, playlist.size()-1]; if the
 * playlist is empty it is -1 and playing is false.
 *
 * @author Kim Delgadillo
 * @version October 2026
 */
public class MusicPlayer
{
    private Playlist playlist;
    private int currentIndex; // -1 means nothing selected
    private boolean playing;

    /**
     * Creates a player with nothing selected and not playing. O(1).
     *
     * @param playlist the playlist to play
     */
    public MusicPlayer(Playlist playlist)
    {
        this.playlist = playlist;
        this.currentIndex = -1;
        this.playing = false;
    }

    /**
     * Starts playback. Selects index 0 if nothing is selected. O(1).
     *
     * @return false if the playlist is empty, otherwise true
     */
    public boolean play()
    {
        if (playlist.size() == 0)
        {
            return false;
        }

        if (currentIndex == -1)
        {
            currentIndex = 0;
        }

        playing = true;
        return true;
    }

    /**
     * Sets playing to false; harmless if already stopped. O(1).
     */
    public void pause()
    {
        playing = false;
    }

    /**
     * Advances currentIndex by one. O(1).
     *
     * @return true if moved, or false (staying put) on the last song or
     *         an empty playlist
     */
    public boolean nextTrack()
    {
        if (currentIndex >= 0
            && currentIndex < playlist.size() - 1)
        {
            currentIndex++;
            return true;
        }

        return false;
    }

    /**
     * Moves currentIndex back by one. O(1).
     *
     * @return true if moved, or false (staying put) on the first song or
     *         an empty playlist
     */
    public boolean previousTrack()
    {
        if (currentIndex > 0)
        {
            currentIndex--;
            return true;
        }
        
        return false;
    }

    /**
     * Gets the song at currentIndex. O(n).
     *
     * @return the current song, or null if currentIndex is -1
     */
    public Song getCurrentSong()
    {
        if (currentIndex == -1)
        {
            return null;
        }
        
        return playlist.getSong(currentIndex);
    }
}
