How to build
============

To create unpacked executable in `./build/install/emots/bin`:

```
./gradlew instDist
```

To create zip with app in `./build/distributions`:

```
./gradlew distZip
```

How to use
===========

Example usage:

```
build/install/emots/bin/emots -t <YOUR TOKEN> -d ~/emoji
```

This will create two sub-catalogs in `~/emoji`: `group`
and `global`. In `group` you will find your orgranization
customs emoji in, `global` you will find hipchat
default emojis (if you want those).
