package playlist;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LibraryLoaderTest {
    @TempDir Path dir;

    private String write(String content) throws IOException {
        Path p = dir.resolve("lib.txt");
        Files.writeString(p, content);
        return p.toString();
    }

    @Test void validLinesGroupIntoAlbums() throws IOException {
        String f = write("A|X|Alb1|100\nB|X|Alb1|120\nC|Y|Alb2|90\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, m -> fail(m));
        assertEquals(2, lib.size());
        assertEquals(2, lib.get(0).getTracks().size());
    }

    @Test void sameAlbumTitleDifferentArtistIsSeparateAlbum() throws IOException {
        String f = write("A|X|Greatest|100\nB|Y|Greatest|120\n");
        assertEquals(2, LibraryLoader.loadLibrary(f, m -> {}).size());
    }

    @Test void missingFileReturnsEmptyChainAndReports() {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(dir.resolve("nope.txt").toString(), msgs::add);
        assertEquals(0, lib.size());
        assertEquals(1, msgs.size());
    }

    @Test void wrongFieldCountIsSkippedWithLineNumber() throws IOException {
        List<String> msgs = new ArrayList<>();
        String f = write("A|X|Alb|100\nbroken line\nB|X|Alb|50\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, msgs::add);
        assertEquals(2, lib.get(0).getTracks().size());
        assertEquals(1, msgs.size());
        assertTrue(msgs.get(0).contains("2"));
    }

    @Test void nonNumericAndNegativeDurationSkipped() throws IOException {
        List<String> msgs = new ArrayList<>();
        String f = write("A|X|Alb|abc\nB|X|Alb|-5\nC|X|Alb|60\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, msgs::add);
        assertEquals(1, lib.get(0).getTracks().size());
        assertEquals(2, msgs.size());
    }

    @Test void blankTitleSkipped() throws IOException {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(write(" |X|Alb|60\n"), msgs::add);
        assertEquals(0, lib.size());
        assertEquals(1, msgs.size());
    }

    @Test void duplicateSongIsKept() throws IOException {
        String f = write("A|X|Alb|100\nA|X|Alb|100\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, m -> {});
        assertEquals(2, lib.get(0).getTracks().size());
    }

    @Test void oneArgLoadsValidFile() throws IOException {
        assertEquals(1, LibraryLoader.loadLibrary(write("A|X|Alb|100\n")).size());
    }

    @Test void oneArgMissingFileReturnsEmptyChain() {
        assertEquals(0, LibraryLoader.loadLibrary(dir.resolve("nope.txt").toString()).size());
    }

    @Test void emptyFileGivesEmptyChain() throws IOException {
        assertEquals(0, LibraryLoader.loadLibrary(write(""), m -> {}).size());
    }

    @Test void albumsAndTracksKeepFileOrder() throws IOException {
        String f = write("A|X|Alb1|100\nB|Y|Alb2|100\nC|X|Alb1|100\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, m -> fail(m));
        assertEquals(new Album("Alb1", "X"), lib.get(0));
        assertEquals(new Album("Alb2", "Y"), lib.get(1));
        LinkedChain<Song> t = lib.get(0).getTracks();
        assertEquals("A", t.get(0).getTitle());
        assertEquals("C", t.get(1).getTitle());
    }

    @Test void fieldsAreTrimmed() throws IOException {
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(
            write("  A  | X |  Alb | 100 \n"), m -> fail(m));
        assertEquals(new Song("A", "X", "Alb", 100), lib.get(0).getTracks().get(0));
    }

    @Test void windowsLineEndingsLoad() throws IOException {
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(
            write("A|X|Alb|100\r\nB|X|Alb|50\r\n"), m -> fail(m));
        assertEquals(2, lib.get(0).getTracks().size());
    }

    @Test void blankLinesAreIgnoredButCounted() throws IOException {
        List<String> msgs = new ArrayList<>();
        String f = write("\n   \nA|X|Alb|100\n\nbad\n");
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(f, msgs::add);
        assertEquals(1, lib.size());
        assertEquals(1, msgs.size());
        assertTrue(msgs.get(0).startsWith("Line 5"));
    }

    @Test void tooManyFieldsSkipped() throws IOException {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(
            write("A|X|Alb|100|extra\nA|X|Alb|100|\n"), msgs::add);
        assertEquals(0, lib.size());
        assertEquals(2, msgs.size());
    }

    @Test void blankArtistAlbumOrDurationSkipped() throws IOException {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(
            write("A||Alb|100\nA|X| |100\nA|X|Alb|\nA|X|Alb|0\n"), msgs::add);
        assertEquals(0, lib.size());
        assertEquals(4, msgs.size());
    }

    @Test void durationOverflowAndDecimalSkipped() throws IOException {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(
            write("A|X|Alb|99999999999\nB|X|Alb|3.5\n"), msgs::add);
        assertEquals(0, lib.size());
        assertEquals(2, msgs.size());
    }

    @Test void utf8TitlesLoad() throws IOException {
        Path p = dir.resolve("utf8.txt");
        Files.write(p, "Café Olé|Beyoncé|Ñandú|100\n"
            .getBytes(java.nio.charset.StandardCharsets.UTF_8));
        LinkedChain<Album> lib = LibraryLoader.loadLibrary(p.toString(), m -> fail(m));
        assertEquals("Café Olé", lib.get(0).getTracks().get(0).getTitle());
    }

    @Test void nullPathReturnsEmptyChainAndReports() {
        List<String> msgs = new ArrayList<>();
        assertEquals(0, LibraryLoader.loadLibrary(null, msgs::add).size());
        assertEquals(1, msgs.size());
    }

    @Test void directoryPathReturnsEmptyChainAndReports() {
        List<String> msgs = new ArrayList<>();
        assertEquals(0, LibraryLoader.loadLibrary(dir.toString(), msgs::add).size());
        assertEquals(1, msgs.size());
    }

    @Test void bundledLibraryFileLoads() {
        List<String> msgs = new ArrayList<>();
        LinkedChain<Album> lib = LibraryLoader.loadLibrary("data/library.txt", msgs::add);
        assertEquals(4, lib.size());
        assertEquals(3, lib.get(0).getTracks().size());
        assertEquals(4, msgs.size());
    }
}
