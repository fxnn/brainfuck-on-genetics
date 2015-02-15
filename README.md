# brainfuck-on-genetics

This project explores [genetic algorithms](http://en.wikipedia.org/wiki/Genetic_algorithm) running with [Brainfuck](http://en.wikipedia.org/Brainfuck).

Backed by my own Brainfuck interpreter [github.com/fxnn/brainfuck](https://github.com/fxnn/brainfuck), these genetic algorithms modify Brainfuck programs according to a target function. 

The sources contain

* a general framework for building genetic algorithms (cf. `de.fxnn.genetics`) and
* an implementation for Brainfuck programs (cf. `de.fxnn.brainfuckongenetics`).

Among the Brainfuck implementation are

* fitness functions (cf. `de.fxnn.brainfuckongenetics.fitness`),
* operators for modifying / mutating Brainfuck programs (cf. `de.fxnn.brainfuckongenetics.operators`) and
* an interactive commandline interface (cf. `de.fxnn.brainfuckongenetics.cli`).

[![Build Status](https://travis-ci.org/fxnn/brainfuck-on-genetics.svg)](https://travis-ci.org/fxnn/brainfuck-on-genetics)

## Usage

Please note that you need to _mvn install_ the [github.com/fxnn/brainfuck](https://github.com/fxnn/brainfuck) project first, as it's not published in a public Maven repository.

```
$ git clone https://github.com/fxnn/brainfuck
$ cd brainfuck
$ mvn install
$ cd ..
```

Then you may build this project.

```
$ git clone https://github.com/fxnn/brainfuck-on-genetics
$ cd brainfuck-on-genetics
$ mvn package
```

## License

Licensed under MIT, see for [LICENSE](LICENSE) file.
