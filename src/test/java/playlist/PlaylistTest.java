
package playlist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Playlist class.
 *
 * @author Kim Delgadillo
 * @version October 2026
 */
public class PlaylistTest
{
    //~ Fields ................................................................
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;

    // Set Up
    // ----------------------------------------------------------

    @BeforeEach
    public void setUp()
    {
        playlist = new Playlist("Hokie Playlist");

        song1 = new Song("Enter Sandman", "Metallica",
            "Metallica", 331);

        song2 = new Song("Mr. Brightside", "The Killers",
            "Hot Fuss", 222);

        song3 = new Song("Viva La Vida", "Coldplay",
            "Viva La Vida or Death and All His Friends", 242);
    }


    // Constructor and Size Tests
    // ----------------------------------------------------------

    @Test
    public void testConstructor()
    {
        assertEquals(0, playlist.size());
    }

    @Test
    public void testSize()
    {
        assertEquals(0, playlist.size());

        playlist.addSong(song1);
        assertEquals(1, playlist.size());

        playlist.addSong(song2);
        assertEquals(2, playlist.size());
    }


    // Add Song Tests
    // ----------------------------------------------------------

    @Test
    public void testAddSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testAddSongNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> playlist.addSong(null));

        assertEquals(0, playlist.size());
    }

    @Test
    public void testAddDuplicateSongs()
    {
        playlist.addSong(song1);
        playlist.addSong(song1);

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song1, playlist.getSong(1));
    }


    // Remove Song Tests
    // ----------------------------------------------------------

    @Test
    public void testRemoveSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        assertTrue(playlist.removeSong(song2));

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song3, playlist.getSong(1));
    }

    @Test
    public void testRemoveSongNotFound()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertFalse(playlist.removeSong(song3));

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testRemoveSongEmpty()
    {
        assertFalse(playlist.removeSong(song1));
        assertEquals(0, playlist.size());
    }

    @Test
    public void testRemoveDuplicateSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song1);

        assertTrue(playlist.removeSong(song1));

        assertEquals(2, playlist.size());
        assertEquals(song2, playlist.getSong(0));
        assertEquals(song1, playlist.getSong(1));
    }


    // Move Song Tests
    // ----------------------------------------------------------

    @Test
    public void testMoveSongForward()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        playlist.moveSong(0, 2);

        assertEquals(song2, playlist.getSong(0));
        assertEquals(song3, playlist.getSong(1));
        assertEquals(song1, playlist.getSong(2));
    }

    @Test
    public void testMoveSongBackward()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        playlist.moveSong(2, 0);

        assertEquals(song3, playlist.getSong(0));
        assertEquals(song1, playlist.getSong(1));
        assertEquals(song2, playlist.getSong(2));
    }

    @Test
    public void testMoveSongSameIndex()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        playlist.moveSong(0, 0);

        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
        assertEquals(2, playlist.size());
    }

    @Test
    public void testMoveSongInvalidFrom()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(-1, 1));

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testMoveSongInvalidTo()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(0, 5));

        assertEquals(2, playlist.size());
        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testMoveSongEmpty()
    {
        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(0, 0));
    }

    // Shuffle Tests
    // ----------------------------------------------------------

    @Test
    public void testShuffleEmpty()
    {
        playlist.shuffle();

        assertEquals(0, playlist.size());
    }

    @Test
    public void testShuffleOneSong()
    {
        playlist.addSong(song1);

        playlist.shuffle();

        assertEquals(1, playlist.size());
        assertEquals(song1, playlist.getSong(0));
    }

    @Test
    public void testShuffleMultipleSongs()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        playlist.shuffle();

        assertEquals(3, playlist.size());

        int song1Count = 0;
        int song2Count = 0;
        int song3Count = 0;

        for (int i = 0; i < playlist.size(); i++)
        {
            Song current = playlist.getSong(i);

            if (current.equals(song1))
            {
                song1Count++;
            }
            else if (current.equals(song2))
            {
                song2Count++;
            }
            else if (current.equals(song3))
            {
                song3Count++;
            }
        }

        assertEquals(1, song1Count);
        assertEquals(1, song2Count);
        assertEquals(1, song3Count);
    }

    @Test
    public void testShuffleDuplicates()
    {
        playlist.addSong(song1);
        playlist.addSong(song1);
        playlist.addSong(song2);

        playlist.shuffle();

        assertEquals(3, playlist.size());

        int duplicates = 0;
        int otherSongs = 0;

        for (int i = 0; i < playlist.size(); i++)
        {
            if (playlist.getSong(i).equals(song1))
            {
                duplicates++;
            }
            else if (playlist.getSong(i).equals(song2))
            {
                otherSongs++;
            }
        }

        assertEquals(2, duplicates);
        assertEquals(1, otherSongs);
    }

    // Get Song Tests
    // ----------------------------------------------------------

    @Test
    public void testGetSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertEquals(song1, playlist.getSong(0));
        assertEquals(song2, playlist.getSong(1));
    }

    @Test
    public void testGetSongInvalidIndex()
    {
        playlist.addSong(song1);

        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.getSong(-1));

        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.getSong(1));
    }

    @Test
    public void testGetSongEmpty()
    {
        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.getSong(0));
    }


    // Spec Section 7 Fixture P = [A, B, C]
    // ----------------------------------------------------------

    /**
     * Fills the playlist with song1, song2, song3 (A, B, C).
     */
    private void fillABC()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
    }

    /**
     * Asserts the playlist holds exactly the given songs in order.
     */
    private void assertSongs(Song... expected)
    {
        assertEquals(expected.length, playlist.size());
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], playlist.getSong(i));
        }
    }

    @Test
    public void testMoveSongToMiddle()
    {
        fillABC();

        playlist.moveSong(0, 1);

        assertSongs(song2, song1, song3);
    }

    @Test
    public void testMoveSongToLastFromMiddle()
    {
        fillABC();

        playlist.moveSong(1, 2);

        assertSongs(song1, song3, song2);
    }

    @Test
    public void testMoveSongToSizeIsRejected()
    {
        fillABC();

        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(0, 3));
        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(3, 0));
        assertThrows(IndexOutOfBoundsException.class,
            () -> playlist.moveSong(0, -1));

        assertSongs(song1, song2, song3);
    }

    @Test
    public void testMoveSongMessageNamesValidRange()
    {
        fillABC();

        IndexOutOfBoundsException e = assertThrows(
            IndexOutOfBoundsException.class,
            () -> playlist.moveSong(-1, 1));

        assertTrue(e.getMessage().contains("0 to 2"));
    }

    @Test
    public void testRemoveSongNull()
    {
        fillABC();

        assertFalse(playlist.removeSong(null));
        assertSongs(song1, song2, song3);
    }

    @Test
    public void testRemoveLastRemainingSong()
    {
        playlist.addSong(song1);

        assertTrue(playlist.removeSong(song1));
        assertEquals(0, playlist.size());
        assertFalse(playlist.removeSong(song1));
    }

    @Test
    public void testAddSongNullLeavesPlaylistUnchanged()
    {
        fillABC();

        assertThrows(IllegalArgumentException.class,
            () -> playlist.addSong(null));
        assertSongs(song1, song2, song3);
    }

    @Test
    public void testAddAfterShuffleAppendsToEnd()
    {
        fillABC();
        playlist.shuffle();

        Song extra = new Song("Extra", "Artist", "Album", 100);
        playlist.addSong(extra);

        assertEquals(4, playlist.size());
        assertEquals(extra, playlist.getSong(3));
    }

    /**
     * Shuffles A, B, C many times. Every one of the 6 orders must show up,
     * including the unchanged order. A broken Fisher-Yates (for example
     * nextInt(i) instead of nextInt(i + 1)) never leaves the list in its
     * original order, so this test would catch it. The chance a correct
     * shuffle misses an order in 600 tries is below 1e-40.
     */
    @Test
    public void testShuffleProducesEveryOrder()
    {
        java.util.Set<String> orders = new java.util.HashSet<String>();

        for (int round = 0; round < 600; round++)
        {
            playlist = new Playlist("P");
            fillABC();
            playlist.shuffle();

            orders.add(playlist.getSong(0).getTitle() + "|"
                + playlist.getSong(1).getTitle() + "|"
                + playlist.getSong(2).getTitle());
        }

        assertEquals(6, orders.size());
    }
}
