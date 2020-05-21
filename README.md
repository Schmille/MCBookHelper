# MCBookHelper

Small Java program designed to add long texts to books without using ingame commands.
it works by copying a text and pressing the "Import" button, after which the programm will try to
figure out how to best space your text.
<br>
The number of letters you can place on a page can vary based on the letter and format. 
Texturepacks that introduce other fonts may also cause problems.
<br>
When you are ready, select an interval (in seconds) and press the start button. 
The program will push the next page into your clipboard after the selected time.


**Important**: The program will run through one interval before the first page, to give you time
to prepare.

To build this program, download or clone this repository, navigate to the download location using 
the commandline and execute
<br>
```gradlew build``` (or ```.\gradlew build``` if using Powershell).
<br>
you should then find an executable .jar file under build/libs.