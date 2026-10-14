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
