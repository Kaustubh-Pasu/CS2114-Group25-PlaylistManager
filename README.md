# CS2114-Group25-PlaylistManager

A console Playlist Manager for CS 2114. It loads a song library from a text
file, lets the user build a playlist (add, remove, move, shuffle), and
simulates playback (play, pause, next, previous). All collections are backed
by a generic, singly linked `LinkedChain<T>`.

The full design is in [docs/Spec.md](docs/Spec.md). Per-person task docs are
in [docs/team/](docs/team/).

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer

## Build, run, test

```
mvn compile                                  # build
mvn test                                     # run JUnit 5 tests
java -cp target/classes playlist.PlaylistApp # run (after mvn compile)
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

## Layout

```
src/main/java/playlist/   application classes (package playlist)
src/test/java/playlist/   JUnit 5 tests
data/library.txt          sample library
docs/Spec.md              specification (source of truth)
docs/team/                task docs
```

## Class ownership

| Class          | Owner    | Branch                |
| :------------- | :------- | :-------------------- |
| LinkedChain    | Person A | `chain`               |
| Song           | Person B | `song-album-validator`|
| Album          | Person B | `song-album-validator`|
| InputValidator | Person B | `song-album-validator`|
| Playlist       | Person C | `playlist-player`     |
| MusicPlayer    | Person C | `playlist-player`     |
| LibraryLoader  | Person D | `app-loader`          |
| PlaylistApp    | Person D | `app-loader`          |
