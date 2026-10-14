package playlist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AlbumTest
{
    @Test
    void testConstructorCreatesEmptyAlbum()
    {
        Album album = new Album("Short n' Sweet", "Sabrina Carpenter");

        assertEquals(0, album.getTracks().size());
    }

    @Test
    void testAddTrack()
    {
        Album album = new Album("Short n' Sweet", "Sabrina Carpenter");
        Song song = new Song(
            "Espresso",
            "Sabrina Carpenter",
            "Short n' Sweet",
            175);

        album.addTrack(song);

        assertEquals(1, album.getTracks().size());
        assertEquals(song, album.getTracks().get(0));
    }

    @Test
    void testConstructorRejectsNullOrBlank()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new Album(null, "Artist"));
        assertThrows(IllegalArgumentException.class,
            () -> new Album("Album", null));
        assertThrows(IllegalArgumentException.class,
            () -> new Album("   ", "Artist"));
        assertThrows(IllegalArgumentException.class,
            () -> new Album("Album", ""));
    }

    @Test
    void testAddTrackKeepsFileOrder()
    {
        Album album = new Album("Album", "Artist");
        Song first = new Song("First", "Artist", "Album", 100);
        Song second = new Song("Second", "Artist", "Album", 200);
        Song third = new Song("Third", "Artist", "Album", 300);

        album.addTrack(first);
        album.addTrack(second);
        album.addTrack(third);

        assertArrayEquals(new Song[] {first, second, third},
            album.getTracks().toArray(new Song[3]));
    }

    @Test
    void testAddTrackAllowsDuplicates()
    {
        Album album = new Album("Album", "Artist");
        Song song = new Song("Song", "Artist", "Album", 100);

        album.addTrack(song);
        album.addTrack(song);

        assertEquals(2, album.getTracks().size());
    }

    @Test
    void testAddTrackNullLeavesTracksUnchanged()
    {
        Album album = new Album("Album", "Artist");
        album.addTrack(new Song("Song", "Artist", "Album", 100));

        assertThrows(IllegalArgumentException.class,
            () -> album.addTrack(null));
        assertEquals(1, album.getTracks().size());
    }

    @Test
    void testEqualsIsCaseSensitive()
    {
        assertNotEquals(new Album("Album", "Artist"),
            new Album("album", "Artist"));
    }

    @Test
    void testAddTrackRejectsNull()
    {
        Album album = new Album("Album", "Artist");

        assertThrows(
            IllegalArgumentException.class,
            () -> album.addTrack(null));
    }

    @Test
    void testEquals()
    {
        Album album1 = new Album("Album", "Artist");
        Album album2 = new Album("Album", "Artist");
        Album differentTitle = new Album("Different", "Artist");
        Album differentArtist = new Album("Album", "Different");

        assertEquals(album1, album2);
        assertNotEquals(album1, differentTitle);
        assertNotEquals(album1, differentArtist);
        assertNotEquals(album1, null);
        assertNotEquals(album1, "Album");
    }

    @Test
    void testEqualsIgnoresTracks()
    {
        Album album1 = new Album("Album", "Artist");
        Album album2 = new Album("Album", "Artist");

        album1.addTrack(
            new Song("Song", "Artist", "Album", 180));

        assertEquals(album1, album2);
    }

    @Test
    void testHashCode()
    {
        Album album1 = new Album("Album", "Artist");
        Album album2 = new Album("Album", "Artist");

        assertEquals(album1.hashCode(), album2.hashCode());
    }

    @Test
    void testToStringEmptyAlbum()
    {
        Album album = new Album("Album", "Artist");

        assertEquals(
            "Album - Artist (0 tracks)",
            album.toString());
    }

    @Test
    void testToStringWithTracks()
    {
        Album album = new Album("Album", "Artist");

        album.addTrack(
            new Song("Song 1", "Artist", "Album", 180));
        album.addTrack(
            new Song("Song 2", "Artist", "Album", 200));

        assertEquals(
            "Album - Artist (2 tracks)",
            album.toString());
    }
}