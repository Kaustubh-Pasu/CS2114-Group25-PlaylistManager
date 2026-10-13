package playlist;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InputValidatorTest
{
    @Test
    void testCheckTitleValid()
    {
        assertTrue(InputValidator.checkTitle("Espresso"));
    }

    @Test
    void testCheckTitleInvalid()
    {
        assertFalse(InputValidator.checkTitle(null));
        assertFalse(InputValidator.checkTitle(""));
        assertFalse(InputValidator.checkTitle("   "));
    }

    @Test
    void testCheckTitleLengthBoundary()
    {
        String validTitle = "a".repeat(200);
        String tooLongTitle = "a".repeat(201);

        assertTrue(InputValidator.checkTitle(validTitle));
        assertFalse(InputValidator.checkTitle(tooLongTitle));
    }

    @Test
    void testCheckIndexValid()
    {
        assertTrue(InputValidator.checkIndex(0, 3));
        assertTrue(InputValidator.checkIndex(2, 3));
    }

    @Test
    void testCheckIndexInvalid()
    {
        assertFalse(InputValidator.checkIndex(-1, 3));
        assertFalse(InputValidator.checkIndex(3, 3));
        assertFalse(InputValidator.checkIndex(0, 0));
    }

    @Test
    void testCheckSong()
    {
        Song song = new Song("Song", "Artist", "Album", 180);

        assertTrue(InputValidator.checkSong(song));
        assertFalse(InputValidator.checkSong(null));
    }
}