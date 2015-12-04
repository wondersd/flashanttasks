# FlashAntTasks #

Provides ant tasks for compiling Flash CS4 Flas from Flex Builder.

## An example Ant task ##
```

<taskdef resource="FlashAntTasks.properties" classpath="lib/FlashAntTasks.jar"/>
 

<flashcommand>
   <movie export="true" source="/Project/test.fla" output="/Project/swfs/test.swf" />
   <movie export="true" source="/Project/shared.fla" output="/Project/swfs/shared.swf" />
</flashcommand>

<flashcommand>
   <movie test="true" source="/Project/shell.fla" output="/Project/swfs/shell.swf" />
</flashcommand>

<!-- WINDOWS USERS NEED FLASHAPP -->
<flashcommand flashapp="c:\\program files\\Adobe\\Flash.exe">
   <movie test="true" source="/Project/shell.fla" output="/Project/swfs/shell.swf" />
</flashcommand>

```

## Variables ##
### flashcommand ###

  * verbose: (true or false)
  * flashapp: **WINDOWS USERS ONLY** path to your flash executable

### movie ###

  * export: (true or false) exports the flash movie from source to the output directory
  * test: (true or false) tests the flash movie from source to the output directory, will immediately open in the ide window
  * source: is the path to the fla
  * output: is the path to the swf

## Ant Installation ##

What you need:

  * Flex Builder or Eclipse
  * Flash IDE (CS3 and CS4)

Installation help

  * Help > Software Updates > Find and Install
  * Select "New Features to Install"
  * Check "Eclipse Project Updates" and hit the Run button
  * Click on 3.3.2 (.3 or .4) and check on the "Eclipse Java Development Tools"
  * Accept the license and go!

After the restart, you'll be able to use Ant.  If you go to Window > Other Views... > and type in Ant you'll see the Ant view. Click that and the view should come up.  In the Ant view you'll see about 5 buttons, the far left one will add your build.xml.