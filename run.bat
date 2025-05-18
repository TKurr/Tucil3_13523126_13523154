@echo off
if exist bin rmdir /s /q bin
mkdir bin
javac -d bin src\main\java\rushhours\Main.java src\main\java\rushhours\algorithm\*.java src\main\java\rushhours\io\*.java src\main\java\rushhours\model\*.java src\main\java\rushhours\model\Colors\*.java
java -cp bin rushhours.Main