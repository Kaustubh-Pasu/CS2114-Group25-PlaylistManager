package playlist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SongTest
{
    @Test
    void testConstructorAndGetters()
    {
        Song song = new Song(
            "Espresso",
            "Sabrina Carpenter",
            "Short n' Sweet",
            175);

        assertEquals("Espresso", song.getTitle());
        assertEquals("Sabrina Carpenter", song.getArtist());
        assertEquals("Short n' Sweet", song.getAlbum());
        assertEquals(175, song.getDurationSec());
    }

    @Test
    void testConstructorRejectsBadInput()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Song(null, "Artist", "Album", 100));

        assertThrows(
            IllegalArgumentException.class,
            () -> new Song("", "Artist", "Album", 100));

        assertThrows(
            IllegalArgumentException.class,
            () -> new Song("Title", "", "Album", 100));

        assertThrows(
            IllegalArgumentException.class,
            () -> new Song("Title", "Artist", "", 100));

        assertThrows(
            IllegalArgumentException.class,
            () -> new Song("Title", "Artist", "Album", 0));
    }

    @Test
    void testConstructorRejectsEveryBadField()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Song("   ", "Artist", "Album", 100));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", null, "Album", 100));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", "\t", "Album", 100));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", "Artist", null, 100));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", "Artist", "  ", 100));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", "Artist", "Album", -1));
        assertThrows(IllegalArgumentException.class,
            () -> new Song("Title", "Artist", "Album", Integer.MIN_VALUE));
    }

    @Test
    void testConstructorAcceptsOneSecond()
    {
        assertEquals(1, new Song("T", "A", "B", 1).getDurationSec());
    }

    @Test
    void testEqualsComparesEveryField()
    {
        Song song = new Song("Song", "Artist", "Album", 180);

        assertEquals(song, song);
        assertNotEquals(song, new Song("Other", "Artist", "Album", 180));
        assertNotEquals(song, new Song("Song", "Other", "Album", 180));
        assertNotEquals(song, new Song("Song", "Artist", "Other", 180));
        assertNotEquals(song, new Song("song", "Artist", "Album", 180));
    }

    @Test
    void testToStringLongSong()
    {
        assertEquals("Long - Artist (61:01)",
            new Song("Long", "Artist", "Album", 3661).toString());
        assertEquals("Short - Artist (0:59)",
            new Song("Short", "Artist", "Album", 59).toString());
    }

    @Test
    void testEquals()
    {
        Song song1 = new Song("Song", "Artist", "Album", 180);
        Song song2 = new Song("Song", "Artist", "Album", 180);
        Song differentDuration =
            new Song("Song", "Artist", "Album", 181);

        assertEquals(song1, song2);
        assertNotEquals(song1, differentDuration);
        assertNotEquals(song1, null);
        assertNotEquals(song1, "Song");
        assertNotEquals(song1, new Album("Album", "Artist"));
    }

    @Test
    void testHashCode()
    {
        Song song1 = new Song("Song", "Artist", "Album", 180);
        Song song2 = new Song("Song", "Artist", "Album", 180);

        assertEquals(song1.hashCode(), song2.hashCode());
    }

    @Test
    void testToString()
    {
        Song song = new Song(
            "Espresso",
            "Sabrina Carpenter",
            "Short n' Sweet",
            175);

        assertEquals(
            "Espresso - Sabrina Carpenter (2:55)",
            song.toString());
    }

    @Test
    void testToStringZeroPadsSeconds()
    {
        Song song =
            new Song("Test", "Artist", "Album", 125);

        assertEquals("Test - Artist (2:05)", song.toString());
    }

    @Test
    void testToStringSpecialCharacters()
    {
        Song song =
            new Song("Rock & \"Roll\", null", "Artist", "Album", 60);

        assertEquals("Rock & \"Roll\", null - Artist (1:00)", song.toString());
    }
}
