package playlist;

import java.util.Objects;

/**
 * Immutable data for one track, plus equality and display.
 *
 * Invariant: title, artist and album are non-blank and durationSec is
 * greater than 0.
 *
 * @author Colin Fahmy
 * @version October 2026
 */
public class Song
{
    private final String title;
    private final String artist;
    private final String album;
    private final int durationSec;

    public Song(String title, String artist, String album, int durationSec)
    {
        if (title == null || title.isBlank()
            || artist == null || artist.isBlank()
            || album == null || album.isBlank()
            || durationSec <= 0)
        {
            throw new IllegalArgumentException("Invalid song data.");
        }

        this.title = title;
        this.artist = artist;
        this.album = album;
        this.durationSec = durationSec;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getAlbum()
    {
        return album;
    }

    public int getDurationSec()
    {
        return durationSec;
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (!(other instanceof Song))
        {
            return false;
        }

        Song otherSong = (Song)other;

        return title.equals(otherSong.title)
            && artist.equals(otherSong.artist)
            && album.equals(otherSong.album)
            && durationSec == otherSong.durationSec;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, artist, album, durationSec);
    }

    @Override
    public String toString()
    {
        int minutes = durationSec / 60;
        int seconds = durationSec % 60;

        return String.format(
            "%s - %s (%d:%02d)",
            title,
            artist,
            minutes,
            seconds);
    }
}
