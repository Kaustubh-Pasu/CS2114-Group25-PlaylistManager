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
}
