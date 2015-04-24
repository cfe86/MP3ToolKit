# MP3 Tool Kit

MP3 Tool Kit is construction kit which allows to bundle components to manipulate audio files. Each component is per default independent but can also be dependent to other components. Some already existent components are for example an ID3Tag Editor and a graphical user interface for [Mp3Gain](http://mp3gain.sourceforge.net/). More components can be added too.

This is a documentation about how to use this application. If you want a documentation from a more technical point of you and how to extend it take a look on the [technical description](https://github.com/cf86/MP3ToolKit/blob/master/TechnicalDescription.md).

## Introduction

Guess all of you know the problem. You got some new audio files and some of them do not have an ID3Tag, the filename doesn't match your needs, the volume differs from your other audio files, so you always have to adjust the volume in your player for every track. Additionally you have to sort the audio files to your music collection.
Another problem I had alot is I want to transfer some audio files to my smartphone, so copied them into the folder and thats it.The problem is I really like a special folder structure on my smartphne for example [artist]/[album]/[track].mp3.
Of course there are some programs for windows and linux to solve this problems, but I really don't want to install a bunch programs on my system just to solve it. Additionally most of the programs are just available for either windows or linux.

Thats why I decided to write my own application to solve this problems. The application is written in Java, so it should work on windows and linux based systems as well.


## Some Features

Here are some features of the application. A more detailed description of all components comes later.

* Has an **ID3 Tag** editor with some features like automatic ID3Tag, Cover Art and Lyrics search from the internet using some predefined collectors.
* Includes a **Mp3Gain** wrapper to adjust the volume of all audio files so that adjusting the volume for every song is not neccessary anymore. Furthermore **Mp3Gain** does not re-encode the audio files, so the volume adjustment is completely undo-able.
* Has a **Rename Tool** which can rename files using some special rules to make the files name fit your needs.
* Has a **Folder Generator** to sort new audio files in your music collection or to generate a folder structure depending on your made rules out of a bunch of unsorted audio files.
* Supports Drag&Drop of folders or files into the application folder or tree structure to load them instant. Which means folders or files can be dragged from the systems file system directly into the application.
* Is easy extendable and customizable by offering a few interfaces to add more collectors or add some new tabs with new features for example an audio converter or playlist generator and so on. For more details about how to extend the application have a look into the [technical description](https://github.com/cf86/MP3ToolKit/blob/master/TechnicalDescription.md).


## Installation

To run this application a java runtime environment is neccessary.

**Windows**:
Just download the *MP33ToolKit_Windows.zip* ([here](https://github.com/cf86/MP3ToolKit/blob/master/MP3ToolKit_Windows.zip?raw=true)) and extract it in a folder of your choice (it is recommended that this folder is empty). It is usuable out of the box and nothing needs to be installed. Mp3Gain is included in this bundle and does not need to be installed seperatly.

**Linux/Mac OS X**:
Download the *MP3ToolKit_Linux.zip* ([here](https://github.com/cf86/MP3ToolKit/blob/master/MP3ToolKit_Linux.zip?raw=true)) and extract it in a folder of your choice (it is recommended that this folder is empty). This zip file only contains the runnable \*.jar file which can be startet using any java runtime environment. In order to use the *Mp3Gain* function *Mp3Gain* needs to be installed. On the most linux derivatives like Ubuntu or Debian this can be done using
```shell
apt-get install mp3gain
```
(I have no idea how to install this on Mac OS X, the shell command `mp3gain` needs to be available in order to use this.)


## Available Components

In this section we will take a look on all available components and see how to use them.

### ID3Tag Editor

![ID3Tag Editor](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/MainWindow.png)

The ID3Tag editor is as the name says there to edit the ID3Tag. All ID3v2 tags will be read and can be updated. If no tag is set a new ID3v2.4 tag will be created. Additionally ID3v1 tags can be written to support older devices like pretty old car radios. If this is neccessary it can be enabled in the Preferences.

To add files to the list you can use the buttons on the bottom side to add a file or a complete folder (if the recursive checkbox is checked all subfolders will be added too). Alternatively you can add folders using the tree on the left side. To add a folder just mark this folder and click the load button under the tree. If the auto add checkbox is enabled the folder will be loaded instantly after selecting it without the need to click the load button.
To delete files you can use the delete buttons on the bottom side.

![Add and Remove Buttons](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3AddRemove.png)


After selecting a file the ID3 data is shown and can be edited. If more than one file is selected the last one will be shown. 

![Add and Remove Buttons](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3Data.png)

If more than one file is selected one tag can be copied to all other marked tags by pressing the small button next to the tag field on the right side. For example if the small button next to the cover art would be pressed this cover would be set to all selected audio files.
The lyrics or cover art can be loaded from the harddrive or saved to the harddrive too.
If a files ID3 data is changed the changed column will be selected.

The ID3tag can be changed using some features which can be found directly under the ID3 data.

![ID3 Options](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3Options.png)

The first 3 buttons use the enabled collectors to try to find the ID3 data, the lyrics and the cover art from the internet using informations from websites e.g. from lastfm. For ID3 data this would for example looks like this:

![ID3 Data Changer](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3DataChanger.png)

In this example the collector found 5 values. Using the checkboxes next to the values you can decide which of them should be used and which shouldn't. To use the collector at least the artist and the song title needs to be given. To find the cover art the album name would be prefered too.

Another feature the ID3Tag Editor brings with it is the auto generation of the ID3 data depending on the filename (the yellow symbol).

![By Name Generator](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3GenByName.png)

The generator needs a regular expression and extracts all data depending on this expression out of the filename. For example lets say the filename of our file is "Metallica-08-Nothing else matters". Now we need to tell the generator the syntax of this filename. Obviously the first part is the Artist follows by the track number and the song title. So lets pick the the expression "%a-%n-%t" and apply it on the filename. It would extract artist: Metallica, track#: 08 and title: Nothing else matters. All abbreviations for each tag can be found by clicking the question mark next to the combobox. Additionally some expressions are already defined and more can be added in the preferences.
Using the radionbutton next to the close and apply button you can decide if the generator should apply the given expression on all loaded files, or just the selected ones. After applying all changes will be shown and have to be confirmed by you.

The next mentionable feature is the field replacer. 

![Field Replacer](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3FieldReplacer.png)

The field replacer allows to manipulate specific tag fields which can be selected. The following manipulations can be made:

* replace all spaces with underscores
* replace all underscores with spaces
* remove all spaces
* make all letters to uppercase
* make all letters to lowercase
* make the the first letter of each word to uppercase
* convert a given sequence to another sequence for example replace all 'a' with 'b'

Using the radionbutton next to the close and apply button you can decide if the generator should apply the given expression on all loaded files, or just the selected ones.

The last 2 options are for deleting the ID3 tags for the selected files and to undo all changes to the selected files by reloading the file.

The last features for the ID3Tag editor are the file options.

![File Options](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3FileOptions.png)

The button on the right side deletes the selected files from the harddrive. But careful, deleted files can **not** be restored.
The button on the left side opens an audio player to play the selected audio file. Per default the default audio player will be loaded.

![Default Audio Player](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3AudioPlayer.png)

If you prefer another player in the preferences it can be changed which player should be used. The default player or a custom player which can be set.


After the ID3 tags are edited they just need to be saved, to do so press the start button.

![Start Button](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/ID3Start.png)

You just need to decide if you want to change just the selected ones, or to save all changed audio files. The ID3 data will now be written into the audio file.


### Mp3Gain

![Mp3Gain](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/Mp3GainWindow.png)

[Mp3Gain](http://mp3gain.sourceforge.net/) is used adjust the loudness of an mp3/mp3s to the same value. To do this Mp3Gain analyses the track and calculates its volume. After this is done you can adjust the volume of all your mp3s to the same loudness. You can choose between track gain and album gain, the track gain changes the volume of one file. The album gain adjusts the volume of all chosen files to the same volume so they appear to have to same loudness.
Mp3Gain does *not* re-encode the mp3. The gain data will only be written in the tag and is therefore undo-able.
The Mp3Gain tab ist just a wrapper to the Mp3Gain console commands. Which commands are used is described in the [technical description](https://github.com/cf86/MP3ToolKit/blob/master/TechnicalDescription.md).

To use Mp3Gain you have to load the files using the bottons as the bottom first. 

![Mp3Gain Buttons](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/Mp3GainButtons.png)

After this decide which gain type you would like to adjust (track or album gain) and decide how load the file should be. Recommended is 89 dB in order to avoid [clipping](http://en.wikipedia.org/wiki/Clipping_(audio)). After this analyze all files. If the gain is already saved in the mp3 it will be loaded directly, if not or if the recalculation is forced it may take a few seconds per file. After analyzing press the start button to change the gain.


### Rename Tool

![Rename Tool](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/RenameToolWindow.png)

The rename tool allows it to rename a file by using the old filename or the ID3Tag of the file in combination with a regular expression to define how the new name should look like. Additionally some processes can be done too for example transform the extension to upper- or lowercase, replace a space with underscore, or underscore with space, trim the file or replace a string with another one. 

To use the rename tool we have to choose some files to we want to rename first. To do this, use the buttons on the bottom side. After this is done, we have to decide if the new name should depend on the old filename, or the ID3Tag. 

![Rename Tool Information](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/RenameToolInfo.png)

The next step is to define the expression which will be used to generate the new filename. Lets say the the file has the following ID3 tags artist: artist1, album: album1, title: title1, track#:01 and we use the following expression "%a-%z-%n-%t" which stands for artist-album-track#-title. After applying this expression on the track would generate the new filename "artist1-album1-01-title1".

To finish the renaming we have to analyze (the left button) the files first and then apply the renaming (the right button).

![Rename Tool start buttons](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/AnalyzeStart.png)


### Folder Generator

![Folder Generator](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/FolderGeneratorWindow.png)

The folder generator can be used to sort new audio files into an existing music collection or do generate a folder structure out of a bunch of unsorted audio files for example to it on your smartphone.

To use the folder generator load all files which should be inserted into the list using the buttons on the bottom side.

The next step is to select a target directory in which the new folder structure should be generated in.

![Folder Information](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/FGRegex.png)

After this is done a regular expression to define the new folder structure needs be set. The expression describes the structure in which every file should be sorted in. For example lets say we have the following 5 files with the following ID3 Tags:

* *File1:* artist: Artist1, title: Title1, Album: Album1, CD: 1/2
* *File2:* artist: Artist1, title: Title2, Album: Album1, CD: 1/2
* *File3:* artist: Artist2, title: Title3, Album: Album2
* *File4:* artist: Artist3, title: Title4, Album: Album3
* *File5:* artist: Artist1, title: Title5, Album: Album1

Now lets say the structure expression is "%a / %z / [%c]" which means each file will be in the folder "[artist]/[album]/[[cd]]/file.ext". The [] in the structure regex means if this tag is available then use it, if it isn't available skip it. In other words if the file has a cd defined in its ID3 tag, then put the file into a subfolder for the CD. If it has no CD tag then don't generate a folder for a CD. The generated folder structure would be the following:
```
Target Folder
|-- Artist1
|   |-- Album1
|   |   |-- 1
|   |   |   |-- File1
|   |   |   |-- File2
|   |   |-- File5
|-- Artist2
|   |-- Album2
|   |   |-- File3
|-- Artist3
|   |-- Album3
|   |   |-- File4
```
The neccessary information can be get from the ID3Tag or from filename if for example no ID3Tag is set. For example lets say the filename of our file is "Metallica-08-Nothing else matters". Now we need to tell the generator the syntax of this filename. Obviously the first part is the Artist follows by the track number and the song title. So lets pick the the expression "%a-%n-%t" and lets apply it on. It would extract artist: Metallica, track#: 08 and title: Nothing else matters. No album could be found from the filename, so unknown album would be inserted because the album has no [%z], so it is not optional. So the generated folder structure for this case would be
```
Target Folder
|-- Metallica
|   |--unknown album
|   |   |--Metallica-08-Nothing else matters.mp3
```
All abbreviations for each tag can be found by clicking the question mark. Additionally some expressions are already defined and more can be added in the preferences.

To make sure that the generated folders are correct they need to be analyzed first by pressing the analyze button (the left button on the right down side)

![Folder Analyze and Start](https://raw.github.com/cf86/MP3ToolKit/master/Screenshots/AnalyzeStart.png)

The new folder is shown in the table on the right side. After analyzing (this step is not optional, before the the folder structure can be generated it was to be analyzed first) the files the after confirming that the generated folder structure is fine it can be generated by pressing the start button (the right one). The application will then start to generate the necessary folders and to copy the files. The files will only be copied, it will not move the files. So the original files are still there too.


## Copyright

Copyright (c) 2015 Christian Feier. See licence.txt for details.
