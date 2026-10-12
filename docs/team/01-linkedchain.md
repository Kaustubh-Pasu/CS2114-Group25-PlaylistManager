I'm Person A on a 4-person Java project (Playlist Manager). My job is the repo

scaffold, the skeleton classes for the whole team, and the real LinkedChain&lt;T&gt;.

Everyone else is blocked on me, so correctness and exact signatures matter more

than speed.

## Read first

1. [Spec.md](http://Spec.md): source of truth. Pay close attention to Section 3 (generic contract),

   Section 4 (data and invariants), Section 5 (all method signatures), Section 6

   (validation), Section 7 (test tables).

2. [01-linkedchain.md](http://01-linkedchain.md): my task doc.

3. The provided [LinkedChain.java](http://LinkedChain.java) (find it in this folder). Read it fully before

   writing anything. Keep as much of it as makes sense and match its existing

   return types and style.

If anything in the spec contradicts the provided [LinkedChain.java](http://LinkedChain.java) (especially

the return types of the "given" methods in Section 5.1), STOP and tell me what

differs before changing anything.

## Part 1: Repo scaffold (do this first)

Create this layout (Maven, Java 17 unless the provided file requires otherwise):

- pom.xml with JUnit 5 and the surefire plugin, so `mvn test` works

- .gitignore (target/, .idea/, *.iml, .vscode/, .DS\_Store,* .class)

- [README.md](http://README.md): short description, build/run/test commands, library file format,

  and the class-ownership table (use Person A/B/C/D, no names)

- docs/[Spec.md](http://Spec.md), and docs/team/ containing the 01-04 task docs and the team README

- data/library.txt: sample library with format title|artist|album|durationSec,

  including several valid lines, two songs on the same album, one duplicate

  song, and a few malformed lines (wrong field count, non-numeric duration,

  blank title, duration 0)

- src/main/java/playlist/ and src/test/java/playlist/ (package: playlist)

## Part 2: Skeleton commit (Checkpoint 0)

Create a class for each of: LinkedChain, Song, Album, Playlist, MusicPlayer,

InputValidator, LibraryLoader, PlaylistApp.

- Use the EXACT signatures from Spec Section 5 (5.1 for the chain, 5.2 for the

  rest), including field declarations from the Section 4 table.

- Add Javadoc on every public method summarizing behavior, exceptions thrown,

  and Big-O from the spec.

- Every method body throws UnsupportedOperationException (constructors may

  initialize fields where trivial). Do NOT implement Song, Album, Playlist,

  MusicPlayer, InputValidator, LibraryLoader or PlaylistApp. Those belong to

  my teammates.

- The whole project must compile with `mvn compile`.

- LinkedChain's skeleton should already show the final public API with

  private static nested Node&lt;T&gt;. No raw types, no casts, no bound on T.

Commit this on `main` as one commit: "Add project scaffold and class skeletons".

Do not push.

## Part 3: Implement LinkedChain&lt;T&gt; (separate branch: `chain`)

Branch from main after the skeleton commit. Implement all 15 methods per

Section 5.1 and the Section 3 contract:

- Singly linked: firstNode and numberOfEntries only. No tail, no prev.

- Node&lt;T&gt; is a private static nested class (data, next). Outside code only sees T.

- add, addToEnd, addAt throw IllegalArgumentException on null.

- remove(T), contains, count, indexOf treat null as "not found" and never throw.

- get, removeAt throw IndexOutOfBoundsException when out of range; addAt allows

  0..size inclusive and throws IndexOutOfBoundsException otherwise.

- toArray(T\[\]) throws IllegalArgumentException if null or too small.

- toString returns "\[a, b, c\]" and "\[\]" when empty.

- Search uses equals() and acts on the first match. Duplicates allowed.

- Maintain the Section 4 invariants: empty means firstNode == null and

  numberOfEntries == 0; numberOfEntries always equals the real node count; no

  null data. Update the count in EVERY add/remove path (including removeAt(0),

  remove() on a one-element chain, and clear).

Commit in small steps in this order, so I can push early:

1. add, addToEnd, size, isEmpty, get, toString

2. addAt, removeAt, indexOf

3. remove(), remove(T), clear, contains, count, toArray

## Part 4: LinkedChainTest (JUnit 5)

Write tests for every method using the Section 7 chain table (fixture

X = [a, b, c]), normal case and bad/edge case. Also cover:

- addAt at index 0, middle, and size; and at size+1 and -1 (exception)

- removeAt on the first, middle and last element, and on an empty chain

- remove(T) on the first, middle, last and absent element; with duplicates it

  removes only the first match

- indexOf and count with duplicates

- null handling for every method per the contract

- invariant checks after sequences of mixed operations (size matches

  toString/toArray contents, empty chain behaves correctly after clear and

  after removing the last element)

- type safety: a LinkedChain&lt;String&gt; used with strings, and one with a custom

  class that overrides equals

## Rules

- Keep my teammates' skeleton files untouched on the `chain` branch.

- Don't add dependencies beyond JUnit 5.

- Don't invent behavior the spec doesn't define. If something is ambiguous, ask me.

- No emojis in code or comments.

## When done

Run `mvn test` and show me the result. Then give me a short summary: what was

created, the git log, any spec/provided-file mismatches you found, and anything

my teammates should know about the chain API before they use it.