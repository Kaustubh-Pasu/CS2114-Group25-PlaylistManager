package playlist;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Drives PlaylistApp through scripted console input and checks what it
 * prints. System.in must be set before the app is constructed, because
 * the app creates its Scanner at construction time.
 */
class PlaylistAppTest
{
    @TempDir
    Path dir;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream out;

    private LinkedChain<Album> library;
    private Playlist playlist;

    @BeforeEach
    void setUp()
    {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Album album = new Album("Short n' Sweet", "Sabrina Carpenter");
        album.addTrack(new Song("Espresso", "Sabrina Carpenter",
            "Short n' Sweet", 175));
        album.addTrack(new Song("Please Please Please", "Sabrina Carpenter",
            "Short n' Sweet", 186));
        library = new LinkedChain<Album>();
        library.addToEnd(album);
        playlist = new Playlist("Test");
    }

    @AfterEach
    void tearDown()
    {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void input(String lines)
    {
        System.setIn(new ByteArrayInputStream(lines.getBytes()));
    }

    // ---------- constructor ----------

    @Test
    void testConstructorWithLibrary()
    {
        input("1\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Espresso"));
    }

    @Test
    void testConstructorWithEmptyLibrary()
    {
        input("1\n0\n");
        new PlaylistApp(new LinkedChain<Album>(), playlist).showMenu();
        assertTrue(out.toString().contains("Library is empty"));
    }

    // ---------- showMenu ----------

    @Test
    void testShowMenuAddAndPlay()
    {
        input("2\nespresso\n7\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertEquals(1, playlist.size());
        assertTrue(out.toString().contains("Now playing: Espresso"));
    }

    @Test
    void testShowMenuInvalidOption()
    {
        input("abc\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Invalid option"));
    }

    @Test
    void testShowMenuBlankTitleReprompts()
    {
        input("2\n   \nEspresso\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Title must be 1-200 characters"));
        assertEquals(1, playlist.size());
    }

    @Test
    void testShowMenuSongNotInLibrary()
    {
        input("2\nNot A Song\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Song not found"));
        assertEquals(0, playlist.size());
    }

    @Test
    void testShowMenuBadIndexReprompts()
    {
        input("2\nEspresso\n2\nPlease Please Please\n4\n5\n1\n0\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Enter a position from 0 to 1"));
        assertEquals("Please Please Please", playlist.getSong(0).getTitle());
    }

    @Test
    void testShowMenuEmptyPlaylistActions()
    {
        input("3\n7\n9\n10\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertTrue(text.contains("Playlist is empty"));
        assertTrue(text.contains("Nothing to play"));
    }

    @Test
    void testShowMenuEndOfInputDoesNotCrash()
    {
        input("6\n");
        assertDoesNotThrow(() -> new PlaylistApp(library, playlist).showMenu());
    }

    @Test
    void testShowMenuRemoveSong()
    {
        input("2\nEspresso\n3\nESPRESSO\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Removed: Espresso"));
        assertEquals(0, playlist.size());
    }

    @Test
    void testShowMenuRemoveSongNotInPlaylist()
    {
        input("2\nEspresso\n3\nPlease Please Please\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Song not found"));
        assertEquals(1, playlist.size());
    }

    @Test
    void testShowMenuMoveSong()
    {
        input("2\nEspresso\n2\nPlease Please Please\n4\n0\n1\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Moved"));
        assertEquals("Please Please Please", playlist.getSong(0).getTitle());
        assertEquals("Espresso", playlist.getSong(1).getTitle());
    }

    @Test
    void testShowMenuMoveNonNumericIndexReprompts()
    {
        input("2\nEspresso\n4\nfirst\n\n0\n0\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Enter a position from 0 to 0"));
        assertTrue(out.toString().contains("Moved"));
    }

    @Test
    void testShowMenuMoveOnEmptyPlaylist()
    {
        input("4\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Playlist is empty"));
    }

    @Test
    void testShowMenuShowPlaylist()
    {
        input("2\nEspresso\n2\nPlease Please Please\n6\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertTrue(text.contains("0: Espresso - Sabrina Carpenter (2:55)"));
        assertTrue(text.contains("1: Please Please Please - Sabrina Carpenter (3:06)"));
    }

    @Test
    void testShowMenuShowEmptyPlaylist()
    {
        input("6\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Playlist is empty"));
    }

    @Test
    void testShowMenuShuffle()
    {
        input("2\nEspresso\n2\nPlease Please Please\n5\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Shuffled"));
        assertEquals(2, playlist.size());
    }

    @Test
    void testShowMenuShuffleOneSongIsSilent()
    {
        input("2\nEspresso\n5\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertFalse(out.toString().contains("Shuffled"));
        assertEquals(1, playlist.size());
    }

    @Test
    void testShowMenuPlayNextPreviousPause()
    {
        input("2\nEspresso\n2\nPlease Please Please\n7\n9\n9\n10\n10\n8\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertTrue(text.contains("Now playing: Please Please Please"));
        assertTrue(text.contains("End of playlist reached"));
        assertTrue(text.contains("Start of playlist reached"));
        assertTrue(text.contains("Paused"));
    }

    @Test
    void testShowMenuNextBeforePlay()
    {
        input("2\nEspresso\n9\n10\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertFalse(text.contains("End of playlist reached"));
        assertFalse(text.contains("Start of playlist reached"));
        assertTrue(text.contains("Press Play to start"));
    }

    @Test
    void testShowMenuRemoveCurrentSongThenPlay()
    {
        // add both, play, skip to the second, remove it, play again
        input("2\nEspresso\n2\nPlease Please Please\n7\n9\n"
            + "3\nPlease Please Please\n7\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertFalse(text.contains("Something went wrong"));
        assertTrue(text.lastIndexOf("Now playing: Espresso")
            > text.lastIndexOf("Removed:"));
    }

    @Test
    void testShowMenuRemoveOnlySongThenNext()
    {
        input("2\nEspresso\n7\n3\nEspresso\n9\n7\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertFalse(text.contains("Something went wrong"));
        assertTrue(text.lastIndexOf("Nothing to play")
            > text.lastIndexOf("Removed:"));
    }

    @Test
    void testShowMenuTooLongTitleReprompts()
    {
        input("2\n" + "a".repeat(201) + "\nEspresso\n0\n");
        new PlaylistApp(library, playlist).showMenu();
        assertTrue(out.toString().contains("Title must be 1-200 characters"));
        assertEquals(1, playlist.size());
    }

    @Test
    void testShowMenuOutOfRangeAndBlankChoices()
    {
        input("11\n\n-1\n 0 \n");
        new PlaylistApp(library, playlist).showMenu();
        String text = out.toString();
        assertEquals(3, text.split("Invalid option", -1).length - 1);
    }

    @Test
    void testShowMenuEndOfInputMidPrompt()
    {
        input("2\n");
        assertDoesNotThrow(() -> new PlaylistApp(library, playlist).showMenu());
        assertEquals(0, playlist.size());
    }

    // ---------- main ----------

    @Test
    void testMainWithNoArgsUsesBundledLibrary()
    {
        input("1\n0\n");
        PlaylistApp.main(new String[0]);
        String text = out.toString();
        assertTrue(text.contains("Bohemian Rhapsody"));
        assertTrue(text.contains("Line 7 skipped"));
    }

    @Test
    void testMainLoadsLibraryFile() throws IOException
    {
        Path file = dir.resolve("lib.txt");
        Files.writeString(file, "Espresso|Sabrina Carpenter|Short n' Sweet|175\n");
        input("1\n0\n");
        PlaylistApp.main(new String[] { file.toString() });
        assertTrue(out.toString().contains("Espresso"));
    }

    @Test
    void testMainMissingFileStillStarts()
    {
        input("1\n0\n");
        PlaylistApp.main(new String[] { dir.resolve("nope.txt").toString() });
        String text = out.toString();
        assertTrue(text.contains("Library file not found"));
        assertTrue(text.contains("Library is empty"));
    }
}
