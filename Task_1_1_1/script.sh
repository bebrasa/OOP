javadoc -d doc src/main/java/ru/nsu/kochanov/Main.java
javac -d bin src/main/java/ru/nsu/kochanov/Main.java
jar -cmf manifest.mf HeapSort.jar -C bin .
java -jar HeapSort.jar
./gradlew test jacocoTestReport
./gradlew build
if [ $? -eq 0 ]; then
  echo "Сборка завершена успешно"
else
  echo "Сборка завершилась с ошибкой"
fi