# CS2114-Group25-PlaylistManager

A console Playlist Manager for CS 2114 (Project 2, Deliverable 3: Prototype +
Tests). It loads a song library from a text file, lets the user build a
playlist (add, remove, move, shuffle), and simulates playback (play, pause,
next, previous). Every collection is stored in one generic, singly linked
`LinkedChain<T>`.

Team: Krish Joshi, Kaustubh Pasumarthi, Kim Delgadillo, Colin Fahmy

The full design is in [docs/Spec.md](docs/Spec.md).

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer

## Compile, run, test

Run all commands from the repository root.

```
mvn compile                                   # compile
java -cp target/classes playlist.PlaylistApp  # run with data/library.txt
java -cp target/classes playlist.PlaylistApp path/to/other-library.txt
mvn test                                      # run all JUnit 5 tests
```

`mvn test` runs 200 tests across 8 test classes; all pass.

### MVP path to try

```
1  (browse library)
2  Espresso         (add)
2  Levitating       (add)
6  (show playlist)
4  0 then 1         (move)
5  (shuffle)
7  (play)   9  (next)   10 (previous)   8 (pause)
3  Espresso         (remove)
0  (quit)
```

## Library file format

`data/library.txt`, one song per line, pipe-separated:

```
title|artist|album|durationSec
Espresso|Sabrina Carpenter|Short n' Sweet|175
```

- Exactly 4 fields.
- title, artist and album must be non-blank.
- durationSec must be an integer greater than 0.
- Malformed lines are reported by line number and skipped; loading continues.
- Duplicate songs are allowed.

The sample file has 6 valid lines (including one duplicate) and 4 malformed
lines (wrong field count, non-numeric duration, blank title, duration 0).

## Layout

```
src/main/java/playlist/   application classes (package playlist)
src/test/java/playlist/   JUnit 5 tests
data/library.txt          sample library
docs/Spec.md              specification
```

## How the LinkedChain is used

One generic `LinkedChain<T>` class stores two element types, with no
type-specific copies:

| Element type | Where | Field |
| :-- | :-- | :-- |
| `Song` | `Playlist` (user order) | `LinkedChain<Song> songs` |
| `Song` | `Album` (file order) | `LinkedChain<Song> tracks` |
| `Album` | `PlaylistApp` / `LibraryLoader` (the library) | `LinkedChain<Album> library` |

App classes only call the chain's public methods (`add`, `addToEnd`,
`addAt`, `remove`, `removeAt`, `indexOf`, `get`, `size`, `isEmpty`, `clear`,
`toArray`). Application rules (moving, shuffling, grouping songs into albums,
playback position) live in `Playlist`, `MusicPlayer` and `LibraryLoader`,
not in the chain.

### Big-O of the app methods vs. the spec

| Method | Spec estimate | Actual | Match? |
| :-- | :-- | :-- | :-- |
| `Playlist.addSong` | O(n) | O(n): `addToEnd` walks the chain | Yes |
| `Playlist.removeSong` | O(n) | O(n): `remove(T)` searches | Yes |
| `Playlist.moveSong` | O(n) | O(n): `removeAt` + `addAt` | Yes |
| `Playlist.shuffle` | O(n²) as written | O(n²): n calls to `addToEnd` | Yes |
| `Playlist.getSong` | O(n) | O(n): `get(i)` walks | Yes |
| `Playlist.size` | O(1) | O(1) | Yes |
| `MusicPlayer.play/pause/nextTrack/previousTrack` | O(1) | O(1) | Yes |
| `MusicPlayer.getCurrentSong` | O(n) | O(n): `getSong` | Yes |
| `Album.addTrack` | O(n) | O(n): `addToEnd` | Yes |
| `LibraryLoader.loadLibrary` | O(n²) worst | O(n²) worst: `indexOf` per line | Yes |
| `PlaylistApp` show playlist / find by title | not estimated | O(n²): loops over `get(i)` | No, breaks the spec's traversal rule (see Next Steps) |

## Reflection

### What changed from the specification

| What the spec said | What we built | Why | Origin |
| :-- | :-- | :-- | :-- |
| `LibraryLoader.loadLibrary(String path)` | Kept it, and added `loadLibrary(String path, Consumer<String> report)`; the one-argument version calls it with `System.err::println` | Tests need to capture the "line skipped" messages instead of reading stderr | Own reflection (Krish) |
| `LibraryLoader` collaborates with `InputValidator` | `LibraryLoader` relies on the `Song` constructor to reject bad fields and catches its `IllegalArgumentException` | The spec's validation table already puts invalid song fields in the `Song` constructor; calling `InputValidator` too would check the same thing twice | Own reflection (Krish) |
| `PlaylistApp` has `main` and `showMenu` | Added a public constructor `PlaylistApp(LinkedChain<Album>, Playlist)`; `main` takes an optional library path argument | Lets tests build the app with a small library and scripted input | Own reflection (Krish), GenAI suggested the test setup |
| Library is read in file order | `LibraryLoader` first used `add` (inserts at front), which reversed the album order; changed to `addToEnd` | Found when the merged code was tested together | GenAI (found while merging), confirmed by Kaustubh |
| Duplicate songs are intentionally allowed | The first `LibraryLoaderTest` expected duplicates to be dropped; the test was changed to expect both copies | The test contradicted Spec Section 6; the code was right | GenAI (found while merging), confirmed by Kaustubh |
| `PlaylistApp.main` loads the library at startup | The default path pointed at `data/sample-library.txt`, which does not exist; changed to `data/library.txt` | Without it the app started with an empty library | GenAI review, confirmed by Kaustubh |
| Scope "twist": shuffle never plays the same song twice in a row | Plain Fisher-Yates shuffle with no adjacency guarantee | Already relaxed in the revised spec; the guarantee does not hold when most of the playlist is one duplicated song | Group discussion (spec revision) |
| Scope: `String` as a third element type (artists, genres, lyrics) | Not built; only `Song` and `Album` are stored | Nothing in the MVP needs a list of strings; it returns with the lyrics stretch goal | Group discussion |
| Scope: library is a CSV file | Pipe-separated (`|`) file | Song and album titles contain commas, which would break a CSV split | Group discussion (spec) |

**Where validation lives.** This matches Spec Section 6. `InputValidator`
(`checkTitle`, `checkIndex`, `checkSong`) is called by `PlaylistApp`, which
prints the message and re-prompts. `Song`, `Album.addTrack`,
`Playlist.addSong` and `Playlist.moveSong` throw exceptions as a safety net.
`showMenu` also catches any `RuntimeException`, so a bug in a lower class
prints "Something went wrong" instead of ending the program.

**Tests vs. the test plan.** Every row of the Spec Section 7 test plan has a
test. We added tests the plan did not list: duplicate songs in `Playlist`,
shuffle with duplicates, `moveSong` to the same index, `nextTrack` and
`previousTrack` before `play`, and `LibraryLoader` with an empty file, a blank
title, and two albums with the same title but different artists. The plan's
`PlaylistApp.main` and `showMenu` rows are covered in `PlaylistAppTest`, which
feeds scripted input through `System.in` and checks the printed output.

### What changed in the LinkedChain

The given method signatures are unchanged. We added three public methods that
the spec called for:

- `addAt(int index, T newEntry)` and `removeAt(int index)`: `Playlist.moveSong`
  needs them.
- `indexOf(T anEntry)`: `LibraryLoader` uses it to find an album that already
  exists before adding a track.

Inside the class we added private helpers `checkIndex` and `nodeAt`, which
`get`, `addAt` and `removeAt` share. `contains` now calls `indexOf`, so it
stops at the first match instead of counting every match.

### What happened to our unknowns

| Unknown from the scope | Status |
| :-- | :-- |
| How to simulate playback without freezing the menu | **Solved.** No timer or `Thread.sleep`: `MusicPlayer` keeps `currentIndex` and a `playing` flag, and "playing" is just state. The menu never blocks. |
| How to read the library file in Java and handle a missing file | **Solved.** `BufferedReader` in try-with-resources reads line by line; `IOException` reports the problem and returns an empty library. |
| How to write a generic `LinkedChain<T>` | **Solved.** We started from the provided class, learned how `Node<T>` uses the type parameter, and added `addAt`, `removeAt` and `indexOf`. |
| How to shuffle a linked list (biggest MVP risk) | **Solved.** `toArray` once, Fisher-Yates on the array, `clear`, then re-add in order. The "no back-to-back repeats" guarantee is still open (dropped, see above). |
| How to connect to Spotify | **Open.** Not attempted; the stretch goal is dropped. |
| Where the song data comes from | **Partly solved.** A hand-written `data/library.txt`, but it has 6 valid songs, not the 20 to 30 we planned. |

### What doesn't work yet

- **Removing the song that is playing.** `MusicPlayer` does not adjust
  `currentIndex` when the playlist shrinks. If you remove the last song while
  it is current and press Play, the app prints "Something went wrong: Index 1
  out of bounds for size 1" instead of a clear message. It does not crash,
  but Spec invariant 4 is broken. `moveSong` and `shuffle` also leave
  `currentIndex` pointing at whatever song now sits at that position.
- **Only two element types** in the chain. The scope listed `String` as a third.
- **Sample library is small**: 6 valid songs instead of 20 to 30.
- **Traversal rule**: `PlaylistApp` shows the playlist and finds songs by title
  with a `get(i)` loop, which is O(n²).
- No test is currently failing.

## Next Steps

### What is left to finish the MVP (in order)

| # | Item | Owner |
| :-- | :-- | :-- |
| 1 | `MusicPlayer` keeps `currentIndex` valid after remove, move and shuffle (reset to -1 or clamp to the last song, and stop playing if the playlist becomes empty) | Kim Delgadillo |
| 2 | `PlaylistApp` uses `toArray` once to show the playlist and find a song by title, instead of looping over `get(i)` | Krish Joshi |
| 3 | Grow `data/library.txt` to 20 to 30 songs across several albums, keeping the malformed test lines | Krish Joshi |
| 4 | Add Javadoc to `Song`, `Album` and `InputValidator`, and run the CS 2114 style checker on every class | Colin Fahmy |
| 5 | Re-run all tests, update the Big-O table and this README | Kaustubh Pasumarthi |

### Stretch goals

| Stretch goal (from the scope) | Decision | Owner |
| :-- | :-- | :-- |
| Repeat (one song, or loop the playlist) | **Committed.** Small change to `MusicPlayer.nextTrack/previousTrack`. | Kim Delgadillo |
| Recently Played | **Committed.** A second `LinkedChain<Song>` in `MusicPlayer`, newest first with `add`. | Kim Delgadillo |
| Viewing lyrics | **Committed.** Stores each song's lines in a `LinkedChain<String>`, giving the chain a third element type. | Colin Fahmy (loading), Krish Joshi (menu) |
| Add to Queue | **Dropped.** Merging a queue with playlist navigation adds edge cases we cannot test well in time. | — |
| Sorting | **Dropped.** Needs a sort on a linked chain plus a way to restore the user's order. | — |
| Spotify embedding | **Dropped.** Needs network calls and authentication, outside this course. | — |

### New tests we will need

- `MusicPlayerTest`: remove the current song (middle and last), remove every
  song while playing, then `play`/`getCurrentSong`; move and shuffle while
  playing.
- `MusicPlayerTest` (repeat): `nextTrack` on the last song with repeat-all →
  wraps to 0; `previousTrack` on the first → wraps to the last; repeat-one →
  stays put; repeat on an empty playlist → false.
- `MusicPlayerTest` (recently played): history after play/next/previous;
  history is empty before play; jumping to a song that was later removed from
  the playlist → "Song not found".
- Lyrics: load lyrics for a song → lines in file order; missing lyrics file →
  "No lyrics", no crash; empty lyrics file → empty chain.
- `PlaylistAppTest`: menu options for each new feature, each with an invalid
  input case.
- `LibraryLoaderTest`: the larger library loads with the expected album and
  track counts.

## Attribution

| File                      | Who                 | How                                                                                                                             |
| :------------------------- | :------------------- | :------------------------------------------------------------------------------------------------------------------------------- |
| `LinkedChain.java`        | Kaustubh Pasumarthi | Revised the provided class: implemented given methods, added `addAt`, `removeAt`, `indexOf` and private helpers; GenAI-assisted |
| `LinkedChainTest.java`    | Kaustubh Pasumarthi | Wrote                                                                        |
| `Song.java`               | Colin Fahmy         | Wrote                                                                                                                           |
| `SongTest.java`           | Colin Fahmy         | Wrote                                                                                                                           |
| `Album.java`              | Colin Fahmy         | Wrote                                                                                                                           |
| `AlbumTest.java`          | Colin Fahmy         | Wrote                                                                                                                           |
| `InputValidator.java`     | Colin Fahmy         | Wrote                                                                                                                           |
| `InputValidatorTest.java` | Colin Fahmy         | Wrote                                                                                                                           |
| `Playlist.java`           | Kim Delgadillo      | Wrote                                                                                                                           |
| `PlaylistTest.java`       | Kim Delgadillo      | Wrote                                                                                                                           |
| `MusicPlayer.java`        | Kim Delgadillo      | Wrote                                                                                                                           |
| `MusicPlayerTest.java`    | Kim Delgadillo      | Wrote                                                                                                                           |
| `LibraryLoader.java`      | Krish Joshi         | Wrote                                                                                                                     |
| `LibraryLoaderTest.java`  | Krish Joshi         | Wrote                                                                                                                     |
| `PlaylistApp.java`        | Krish Joshi         | Wrote                                                                                                                     |
| `PlaylistAppTest.java`    | Kaustubh Pasumarthi | Wrote                                                                                                                           |
| Merging and integration   | Kaustubh Pasumarthi | Merged the four feature branches, ran the full suite; GenAI-assisted                                                            |

### How we used GenAI

We used GenAI for three things:

1. **GitHub setup**: the Maven project, branches per class owner, and merging
   the branches together.
2. **Finding loopholes in our code**: it found the reversed album order in
   `LibraryLoader`, the missing default library file in `PlaylistApp`, and
   the playlist bug when the current song is removed.
3. **Checking test coverage**: we gave it the Spec Section 7 test plan and
   the rubric and asked which public methods were missing a normal or
   bad-input case. That is how `PlaylistAppTest`, the one-argument
   `LibraryLoader` tests and the special-characters `Song` test were added.

We read every GenAI suggestion and test before keeping it, and checked each
against the spec. Where GenAI and the spec disagreed, the spec won.
