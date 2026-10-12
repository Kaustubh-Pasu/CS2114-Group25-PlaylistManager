# Team task docs

Read [../Spec.md](../Spec.md) first. It is the source of truth for every
signature, exception, and test case.

| Doc | Owner | Classes |
| :-- | :---- | :------ |
| [01-linkedchain.md](01-linkedchain.md) | Person A | LinkedChain, repo scaffold, skeletons |
| 02 (to be added) | Person B | Song, Album, InputValidator |
| 03 (to be added) | Person C | Playlist, MusicPlayer |
| 04 (to be added) | Person D | LibraryLoader, PlaylistApp |

## Working rules

- Every class already exists as a skeleton with the exact Spec Section 5
  signatures. Replace the `UnsupportedOperationException` bodies; do not
  change signatures without telling the team.
- Only edit the classes you own.
- Use `LinkedChain<T>` only through its public methods. To visit every
  entry, call `toArray(new T[chain.size()])` once instead of looping over
  `get(i)` (that is O(n^2)).
- Run `mvn test` before pushing.
