package playlist;

import java.util.Objects;

/**
 * A titled group of tracks loaded from the library file. Tracks keep
 * file order.
 *
 * @author Colin Fahmy
 * @version October 2026
 */
public class Album
{
    private String title;
    private String artist;
    private LinkedChain<Song> tracks;

    public Album(String title, String artist)
    {
        if (title == null || title.isBlank()
            || artist == null || artist.isBlank())
        {
            throw new IllegalArgumentException("Invalid album data.");
        }

        this.title = title;
        this.artist = artist;
        this.tracks = new LinkedChain<Song>();
    }

    public void addTrack(Song song)
    {
        if (song == null)
        {
            throw new IllegalArgumentException("Song cannot be null.");
        }

        tracks.addToEnd(song);
    }

    public LinkedChain<Song> getTracks()
    {
        return tracks;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (!(other instanceof Album))
        {
            return false;
        }

        Album otherAlbum = (Album)other;

        return title.equals(otherAlbum.title)
            && artist.equals(otherAlbum.artist);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, artist);
    }

    @Override
    public String toString()
    {
        return title + " - " + artist + " (" + tracks.size() + " tracks)";
    }
}