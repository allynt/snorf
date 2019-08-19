snorf
=====

This is a minimal text editor with just the features I want, and *no crap*.

It lets me concentrate on writing with no distractions; It provides a full-screen window with lots of calming blank space; It doesn't try to deal with icky formatting, it just deals with raw text. The only features it bothers with are opening & saving files, updating the color scheme, resizing the font, tracking the word count, etc., and searching for text.

-----

commands are as follows:

- **ctrl-mouse wheel**: increases/decreases the text size
- **ctrl-up/down**: increases/decreases the text size
- **mouse click on statusBar**: update statistics (word count, etc.) in statusBar
- **rt. mouse button**: brings up the context menu which offers the following options (some of which also have keyboard shortcuts)
  - **ctrl-N**: new document
  - **ctrl-O**: open document
  - **ctrl-S**: save document
  - save document as...
  - **ctrl-F**: display find panel
  - change the color scheme
  - **ctrl-U**: update statistics (word count, etc.) in statusBar
  - **esc**: minimizes the application
  - **ctrl-H**: display this help page
  - **ctrl-Q**: quit application
- **ctrl-C**: copy
- **ctrl-X**: cut
- **ctrl-V**: paste

-----

snorf uses a property file, typically located at "$HOME/.snorfrc" with the following format:

    marginPct=2.5
    bgColor=010101
    fontSize=41
    resizeable=false
    fgColor=ad2a57

If settings are changed while running snorf, this file is overwritten with the new settings.

-----

snorf is provided with a wrapper shell script to make calling it easy.  Usage is: ```snorf [-p <property file>] [<file to edit>]```

test change
