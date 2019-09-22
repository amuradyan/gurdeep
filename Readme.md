# Gurdeep ![ ](https://github.com/amuradyan/gurdeep/blob/master/gurdeep.png)

Gurdeep is a slack bot that retrieves technical term definitions.  
Gurdeep stores terms in a dictionary. Each term is defined by it's name and has tags
attached to it. Gurdeep allows browsing the dictionary using tags or alphabetically.

It is written in Scala and sports a popular Punjabi name, meaning ‘lamp of the teacher’.

Gurdeep is also a fan of star wars.

## Commands

All commands start with a "$" sign.

### def

***def*** responds with the definition of the term.

    > $def <term containing spaces>

To see the definition of _port_, we should do the following:

    > $def port

If the term is omitted, Gurdeep responds with a random definition.

### aweme

***aweme*** responds with a random fact.

    > $aweme

### list

***list*** responds with the interactive list of available alphabet in the dictionary.
It is case insensitive.

    > $list <prefix>

Given the words _Port_, _Internet Protocol (IP)_, _HDD_, _adapter_ and _3D Printer_,
sending _list_ will result in the following output:

    > $list
    < 3 A H I P

It also allows for listing by prefix in which case it returns the interactive search result.  
Search by _int_ prefix, given the words _Port_, _Internet Protocol (IP)_, _Integer_,s
_Internationalization (I18N)_ and _3D Printer_ would output the following:

    > $ list int
    < Integer Internationalization (I18N) Internet Protocol (IP)

A click on a term will pull it's definition.

### tags

***tags*** responds with the interactive list of all available tags in the dictionary.
All arguments to tag will be ignored.

### tag

***tag*** responds with the interactive list of terms associated with this tag.
It allows for browsing using multiple tags and is case insensitive.

    > $tag <tag1> <tag2> ...

Given terms _Port (tags: *Internet*, *Network*, *Hardware*, *Software*)_ and
_Linux (tags: *Kernel*, *OS*, *Software*, *Operating System*)_ it behaves as is
described below:

    > $tag Software
    < Port Linux
    ...
    > $tag Software OS
    < Linux 

## Build and run

You can build and run the bot like so:

    > sbt clean run
