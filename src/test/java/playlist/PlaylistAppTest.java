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

    // ---------- main ----------

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
