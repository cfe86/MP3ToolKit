# MP3 Tool Kit

A short non technical introduction can be found here: 
This document is more a technical introduction and explains how to modify this application.

MP3 Tool Kit is construction kit which allows to bundle components to manipulate audio files (currently only mp3). Each component is per default independent but can also be dependent to other components. Some already existent components are for example an ID3Tag Editor and a graphical user interface for [Mp3Gain](http://mp3gain.sourceforge.net/). More components can be added too.


## Some Features

* Modular architecture. All components are build as modules and can be replaced or updated e.g. collectors.
* Offers an interface to add more tabs and extend the application with more functionalities.
* Due to [JMLS](https://github.com/cf86/JMLS) it is easy to add a new language or to modify an already existing language to the Toolkit. 
* Architecture allows to extend the Toolkit with new tabs and more functionalities for example an audio converter or playlist generator


## Building and Requirements

In order to build a *.jar file the MP3 Tool Kit requires:

* [Maven](http://maven.apache.org/) - is used to resolve dependencies.
* [JDK 7+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) - The Toolkit is developed with Oracle JDK 7 but should also work with OpenJDK. 
* [JMLS](https://github.com/cf86/JMLS) - is used to support more than one language.
* [JTattoo](http://www.jtattoo.net/) - is used to offer a few skins to choose from.
* [MigLayout](http://www.miglayout.com/) - is used as the layout manager for the GUI.
* [Mp3Gain](http://mp3gain.sourceforge.net/) - is used by the mp3gain module to adjust the mp3 volume
* [JLayer](http://www.javazoom.net/javalayer/javalayer.html) - is used by the default player to play audio files
* [mp3agic](https://github.com/cf86/mp3agic) - this is a modified version which also supports asynchronous lyrics and ID3v24 year tags. I already made pull request, so as soon as this branch goes into main branch this modified version will be replaced by the original mp3agic. As long as this is not done the modified version has to be used. The modified `mp3agic_mod.jar` is in this project.

<br />
To build the Mp3 Tool Kit use the following command:

```shell
mvn clean package
```

The \*.jar file can be found in the **_target_** directory afterwards. 

A version for **windows** and **linux** is already compiled and can be found in the specific zip files.



## How to extend the Toolkit

Here I will shortly explain how to extend the Toolkit.

### Add a new language
The Tool Kit uses JMLS to support multi languages. Therefore you will just have to add a new language file and add the language to the `mls.conf`. For more information about the language file and `mls.conf` take a look [here](https://github.com/cf86/JMLS). 


### Mp3Gain

The mp3 tab uses [Mp3Gain](http://mp3gain.sourceforge.net/) to adjust the gain for each mp3. The following commandline commands are used to do this:

_anylize file without force calculation:_
```shell
$ mp3gain -c [files]
```
_analyze file with force calculation:_
```shell
$ mp3gain -c -s r [files]
```
_change track gain:_
```shell
$ mp3gain -c -r -d [gain change] [files]
```
The track gain will be set to the default value 89 and then modified using -d with the difference of the target volume and 89.
_change album gain:_
```shell
$ mp3gain -c -a -d [gain change] [files]
```
The album gain will be set to the default value 89 and then modified using -d with the difference of the target volume and 89.


### Change a module

The Toolkit has a modular architecture. Nearly every component can be extended or exchanged for another version. To allow this the modules are managed by different _managers_ which offer one instance for each task. The following _managers_ are available which use the following interfaces.
The `TabManager` has a special role and will be explained later.

**_CollectorManager_**: manages the data collectors for ID3data, covert art and lyrics.

**_AudioManager_**: 

* **Audio Player** which is used as the Default Audio Player
* **Audio File** which is used as the audio file class for the ID3Tag edtor


### Add more Audio File support

Right now [mp3agic](https://github.com/mpatric/mp3agic) is used to write the ID3Tag thats why only mp3s are supported right now. In order to change the mp3agic backend with another one which may support more audio formats the new backend needs to implement the `model.audio.interfaces.IAudioPlayer` interface. To tell the application that this new backend should be used register the new audio file backend in the **AudioManager** as _Audio File_. From now on the new backend will be used.


### Change the default Audio Player

The application uses a small audio player which uses [JLayer](http://www.javazoom.net/javalayer/javalayer.html) to play audio files as a default player. To change the default player the new player has to implement the `model.audio.interfaces.IAudioPlayer` interface and register the new Player in the **AudioManager** as _Audio Player_. After this the new player will be used as the default player.


### Add a Collector

The ID3 Editor module supports to search online for ID3data, Covert Art and Lyrics. For each of this 3 types a special collector is responsible. If more than 1 collector is registered each collector will be used until some data could be found. Right now there is 1 collector for ID3Data and Cover Art and 2 for Lyrics. 
To add more specific collectors the new collector has to implement one of the three interfaces `IID3DataCollector`, `ICoverArtCollector`, `ILyricsCollector` which can be found in the `model.collector.interfaces` package. After implementing the collector it needs to be registered. To do so open the file `manager.CollectorManager` and register the new collector in the `register()` method:
```java
	private void register() {
		// ***************************
		// Register Collectors here: *
		// ***************************
            ...
		// *************************
		// End Register Collectors *
		// *************************
	}
```
using the correct register method `registerID3DataCollector(...)` , `registerCoverCollector(...)` or `registerLyricsCollector(...)`. The register order is the default order in which the collectors are used. This order can be changed by the user in the settings too.


### Add a new tab

To add a new tab you have to create a new `JPanel` first. To do so create a new class and extend it via the `view.interfaces.AbstractTab` class. A template for a new tab could look like this:
```java
public class ExampleTab extends AbstractTab {

	private MLS mls;

	public ExampleTab() {
		mls = new MLS("view/languageFiles/ExampleTab", Config.getInstance().getCurrentLanguage());
		mls.setToolTipDuration(-1);
	}

	@Override
	public void init() throws TabInitException {
		// init all GUI components
	}

	@Override
	public void changeLanguage(Locale lang) {
		this.mls.setLocale(lang);
		this.mls.translate();
	}

	public void setActionListener(ActionListener l) {
		// add the Actionlistener to the components or do this for any other listener
	}
}
```

After this you will need a controller to control the created tab. This _controller_ needs to extend `controller.AbstractController`. The `AbstractController` also has an attribute `mainWindow` (this is no instance from the main window, it is just an command interface) which has a method `setWindowEnabled(boolean en)` which can be called to enable (unfreeze) or disable (freeze) the main window. This may come handy if a sub window needs to be open and the main window should be frozen as long as this sub window is open. A template for a new _controller_ could look like this:
```java
public class ExampleController extends AbstractController {

	private ExampleTab window;
	
	public ExampleController() {
		// init model and so on
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// observer 
	}

	@Override
	public void init(AbstractTab tab) throws ControllerInitException {
		this.window = (ExampleTab) tab;
		this.window.setActionListener(this);
		
		// init more stuff
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("anActionCommand"))
			exampleButtonPressed();
	}
	
	private void exampleButtonPressed() {
		// this is called if exampleButton with Actioncommand "anActionCommand" is pressed.
	}
}
```

After this is done you will have to register your new tab in the `TabManager` found in the `manager` package. To register the tab just add a new register entry in the constructor. For example lets add the new _controller_ as the third tab:
```java
// Register Tabs here:
register("id3Tab", new ID3TagTab(), new ID3TagController(), new String[] {"RenameTab", "Main"});
register("mp3GainTab", new MP3GainTab(), new MP3GainController(), new String[] {});
register("RenameTab", new RenameToolTab(), new RenameToolController(), new String[] {});
register("StructureTab", new FolderCreatorTab(), new FolderCreatorController(), new String[] {});
		
register("ID3", new SmartcastPanel(), new SmartcastController(), new String[]{"champinfoTab", "Main", "exampleTab"});
register("champnoteTab", new ChampNotePanel(), new ChampionNoteController(), new String[]{});
register("exampleTab", new ExampleTab(), new ExampleController(), new String[]{"smartcastTab"});
```
The first String is the _identifier_ which will be used for this _controller_. This _identifier_ is also used in [JMLS](https://github.com/cf86/JMLS) in the `MainWindow` _languagefile_. The second parameter is an instance of the created tab and the third parameter is an instance of the corresponding _controller_. The fourth parameter contains the _identifiers_ of all other _controller_ this new _controller_ should be observed by. For example in the example above the `id3Tab` will be observed by the `RenameTab` and the `MainWindow` (short '_Main_'). This is only neccessary if 2 tabs are dependent to each other. Right now this is not the case.

The name of the new tab is defined in the `MainWindow` _languagefile_ and has the given _identifier_. For more information on how to use the _languagefile_ take a look on the [JMLS](https://github.com/cf86/JMLS) site.

Per default you will just have to register your new Tab to the `TabManager`. The only exception is, if your tab needs some special informations for example when a window like the Settings window got closed. In those cases you would also have to add a few lines to the `MainController`. But I guess this shouldn't be neccessary.


## Configuration

Every configuration will be saved in `config.Config`. There the every config will be written too. If your tab needs to to save some configuration add it in the configuration map and add it to the writer too.
If this settings should be editable by the user, you have to extend the settings window.


## Updater

The updater is a small project in its own. For an update the `updater.jar` will be extracted from the ToolKit and started. Afterwards the ToolKit closes itself. The `updater.jar` makes a backup and downloads the newer version. After the download is complete the `updater.jar` starts the new version of the ToolKit and closes itself.
The sourcecode of the updater can be found in the `Updater-1.0.jar`.



## Copyright

Copyright (c) 2014 Christian Feier. See licence.txt for details.