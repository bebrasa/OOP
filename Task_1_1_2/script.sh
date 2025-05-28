javac -d out src/main/java/ru/nsu/kochanov/*.java
jar cf myprogram.jar -C out .
java -cp myprogram.jar ru.nsu.kochanov.Main