# tabinder
If you're a serious guitar player, then you know how painful it is to keep track of all songs' tuning.

It's a difficult process of looking up the song, eventually refinding the tab, see the tuning, tune accordingly and play.
Tabinder is a command line app that helps you do all of that through a series of simple commands - much faster! 
Store, modify and look up tabs through the command line, all accessible at a few characters away.


List of commands:
  - tabinder-store "artist-name" "song-name" "tuning" => add a guitar song to your collection
  - tabinder-remove "artist-name" "song-name"         => removes the song with the specified title from the specified artist  
  - tabinder-get -a "artist-name" => lists all the known songs from the artist, together with the tunings
  - tabinder-get -s "song-name"   => lists all the known songs with the mentioned title, with tunings
  - tabinder-get -t "tuning"      => lists all the known songs with the specified tuning

Built with Scala for the backend and Python for the command line, it includes frameworks and libraries like:
  - Scala Play
  - Scala Cats
  - Scala Cats-Mtl
  - Scala Refined
  - Python Click
  - Mongod
