# cloc

A Clojure program used to analyse C++ (and Ruby projects), outputting number of Files, Total lines of text, Lines of Code, Comments and Blanks.

Count all files with extension c, cpp, cc. Toggle to include header files

## Usage

Create an executable with
``` sh
lein bin
```

if that does not work you can run the commands below by replace `cloc` with `lein run`

``` sh
cloc --help
```

``` sh
cloc --lang cpp <file-path>
```

``` sh
cloc --lang ruby <file-path>
```


## Assumptions
For multi-line comments, similar to ruby where `=begin` and `=end` should strictly be the only thing on the line. We are not accounting for the case where there is code after the end of a multi-line comment region 

```cpp
/* 
questionable multi-line comment 
*/ int x = 3;
```


## License

Copyright © 2021 zackteo

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
