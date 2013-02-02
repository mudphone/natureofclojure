# natureofclojure

A bunch of Clojure Quil examples based on Daniel Shiffman's Nature of Code video channel on Vimeo.

[Check out the videos on Vimeo.](http://here: https://vimeo.com/channels/464686 "Nature of Code videos")  
Seriously, check them out, they're great.

## Caveat
I'm probably going to base this on the videos, as opposed to the website/book.  This is for no better reason than that I've read a lot of the book already and want to watch the videos now.

Go [buy the book](http://natureofcode.com "Nature of Code book") if you haven't alreaddy.  Seriously, [buy it](http://natureofcode.com "Nature of Code book"), it's the decent thing to do.

## Usage

These sketches require the [Clojure Quil library](https://github.com/quil/quil "Quil"), for working with [Processing](http://processing.org "Processing").  

Install dependencies in root dir:  

````
$ cd path/to/natureofclojure      
$ lein deps
````  

You can view these example sketches by running them from the repl.  I use Emacs and nREPL.  If you have [the same setup](https://gist.github.com/4698169 "My Clojure Emacs setup") you can just start a nREPL process and eval the sketch buffer:
````
M-x nrepl-jack-in
C-c C-k (in the sketch's buffer)
;; The sketch should appear.  Check the Java process window.
````

## License

Copyright © 2013  [Pas de Chocolat, LLC](http://pasdechocolat.com/ "Awesome website")  
This work is licensed under a [Creative Commons Attribution-ShareAlike 2.0 Generic License](http://creativecommons.org/licenses/by-sa/2.0/ "Creative Commons Attribution-ShareAlike 2.0 Generic License").
