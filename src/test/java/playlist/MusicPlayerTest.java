package playlist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MusicPlayer class.
 *
 * @author Kim Delgadillo
 * @version October 2026
 */
public class MusicPlayerTest
{
    //~ Fields ................................................................

    private Playlist playlist;
    private MusicPlayer player;
    private Song song1;
    private Song song2;
    private Song song3;


    // Set Up
    // ----------------------------------------------------------

    @BeforeEach
    public void setUp()
    {
        playlist = new Playlist("Hokie Playlist");
        player = new MusicPlayer(playlist);

        song1 = new Song("Enter Sandman", "Metallica",
            "Metallica", 331);

        song2 = new Song("Mr. Brightside", "The Killers",
            "Hot Fuss", 222);

        song3 = new Song("Viva La Vida", "Coldplay",
            "Viva La Vida or Death and All His Friends", 242);
    }


    // Constructor Tests
    // ----------------------------------------------------------

    @Test
    public void testConstructor()
    {
        assertNull(player.getCurrentSong());
    }


    // ----------------------------------------------------------
    // Play Tests
    // ----------------------------------------------------------

    @Test
    public void testPlayEmpty()
    {
        assertFalse(player.play());
        assertNull(player.getCurrentSong());
    }

    @Test
    public void testPlay()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertTrue(player.play());
        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPlayAgain()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertTrue(player.play());
        assertTrue(player.play());

        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPlayAfterPause()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();
        player.nextTrack();
        player.pause();

        assertTrue(player.play());
        assertEquals(song2, player.getCurrentSong());
    }


    // Pause Tests
    // ----------------------------------------------------------

    @Test
    public void testPauseBeforePlay()
    {
        player.pause();

        assertNull(player.getCurrentSong());
    }

    @Test
    public void testPause()
    {
        playlist.addSong(song1);

        player.play();
        player.pause();

        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPauseMultipleTimes()
    {
        playlist.addSong(song1);

        player.play();
        player.pause();
        player.pause();

        assertEquals(song1, player.getCurrentSong());
    }


    // Next Track Tests
    // ----------------------------------------------------------

    @Test
    public void testNextTrack()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        player.play();

        assertTrue(player.nextTrack());
        assertEquals(song2, player.getCurrentSong());

        assertTrue(player.nextTrack());
        assertEquals(song3, player.getCurrentSong());
    }

    @Test
    public void testNextTrackAtLastSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();
        player.nextTrack();

        assertFalse(player.nextTrack());
        assertEquals(song2, player.getCurrentSong());
    }

    @Test
    public void testNextTrackEmpty()
    {
        assertFalse(player.nextTrack());
        assertNull(player.getCurrentSong());
    }

    @Test
    public void testNextTrackBeforePlay()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertFalse(player.nextTrack());
        assertNull(player.getCurrentSong());
    }

    @Test
    public void testNextTrackOneSong()
    {
        playlist.addSong(song1);

        player.play();

        assertFalse(player.nextTrack());
        assertEquals(song1, player.getCurrentSong());
    }


    // Previous Track Tests
    // ----------------------------------------------------------

    @Test
    public void testPreviousTrack()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        player.play();
        player.nextTrack();
        player.nextTrack();

        assertTrue(player.previousTrack());
        assertEquals(song2, player.getCurrentSong());

        assertTrue(player.previousTrack());
        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPreviousTrackAtFirstSong()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();

        assertFalse(player.previousTrack());
        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPreviousTrackEmpty()
    {
        assertFalse(player.previousTrack());
        assertNull(player.getCurrentSong());
    }

    @Test
    public void testPreviousTrackBeforePlay()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        assertFalse(player.previousTrack());
        assertNull(player.getCurrentSong());
    }

    @Test
    public void testPreviousTrackOneSong()
    {
        playlist.addSong(song1);

        player.play();

        assertFalse(player.previousTrack());
        assertEquals(song1, player.getCurrentSong());
    }


    // Get Current Song Tests
    // ----------------------------------------------------------

    @Test
    public void testGetCurrentSongBeforePlay()
    {
        playlist.addSong(song1);

        assertNull(player.getCurrentSong());
    }

    @Test
    public void testGetCurrentSongAfterPlay()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();

        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testGetCurrentSongAfterNavigation()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        player.play();
        player.nextTrack();

        assertEquals(song2, player.getCurrentSong());

        player.previousTrack();

        assertEquals(song1, player.getCurrentSong());
    }


    // Combined Behavior Tests
    // ----------------------------------------------------------

    @Test
    public void testPauseAndNavigate()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();
        player.pause();

        assertTrue(player.nextTrack());
        assertEquals(song2, player.getCurrentSong());

        assertTrue(player.play());
        assertEquals(song2, player.getCurrentSong());
    }

    @Test
    public void testPlayAfterAddingSong()
    {
        assertFalse(player.play());

        playlist.addSong(song1);

        assertTrue(player.play());
        assertEquals(song1, player.getCurrentSong());
    }


    // Playing Flag Tests
    // ----------------------------------------------------------

    @Test
    public void testNotPlayingInitially()
    {
        assertFalse(player.isPlaying());
    }

    @Test
    public void testPlaySetsPlaying()
    {
        playlist.addSong(song1);

        player.play();

        assertTrue(player.isPlaying());
    }

    @Test
    public void testPlayEmptyStaysStopped()
    {
        player.play();

        assertFalse(player.isPlaying());
    }

    @Test
    public void testPauseClearsPlaying()
    {
        playlist.addSong(song1);

        player.play();
        player.pause();

        assertFalse(player.isPlaying());
    }

    @Test
    public void testPauseWhenStoppedStaysStopped()
    {
        player.pause();

        assertFalse(player.isPlaying());
    }

    @Test
    public void testNavigationKeepsPlayingFlag()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();
        player.nextTrack();

        assertTrue(player.isPlaying());
    }

    @Test
    public void testConstructorRejectsNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> new MusicPlayer(null));
    }


    // Playlist Changes Under The Player
    // ----------------------------------------------------------

    @Test
    public void testCurrentSongAfterLastSongRemoved()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);

        player.play();
        player.nextTrack();
        playlist.removeSong(song2);

        assertEquals(song1, player.getCurrentSong());
        assertTrue(player.play());
        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testPlaylistEmptiedWhilePlaying()
    {
        playlist.addSong(song1);

        player.play();
        playlist.removeSong(song1);

        assertNull(player.getCurrentSong());
        assertFalse(player.isPlaying());
        assertFalse(player.play());
        assertFalse(player.nextTrack());
        assertFalse(player.previousTrack());
    }

    @Test
    public void testPlaylistEmptiedThenRefilled()
    {
        playlist.addSong(song1);
        player.play();
        playlist.removeSong(song1);

        playlist.addSong(song2);

        assertTrue(player.play());
        assertEquals(song2, player.getCurrentSong());
    }

    @Test
    public void testPreviousAfterPlaylistShrinks()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        player.play();
        player.nextTrack();
        player.nextTrack();
        playlist.removeSong(song3);
        playlist.removeSong(song2);

        assertFalse(player.previousTrack());
        assertEquals(song1, player.getCurrentSong());
    }

    @Test
    public void testNextAfterPlaylistShrinks()
    {
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);

        player.play();
        player.nextTrack();
        player.nextTrack();
        playlist.removeSong(song3);

        assertFalse(player.nextTrack());
        assertEquals(song2, player.getCurrentSong());
    }
}
