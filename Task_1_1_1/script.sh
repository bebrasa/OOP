#!/bin/bash

# 1. Генерация документации с помощью javadoc
javadoc -d doc src/main/java/ru/nsu/kochanov/HeapSort.java

# Проверяем, завершилась ли команда успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при генерации документации"
  exit 1
fi

# 2. Компиляция исходного кода
javac -d build/classes/java/main src/main/java/ru/nsu/kochanov/HeapSort.java

# Проверяем, завершилась ли компиляция успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при компиляции исходного кода"
  exit 1
fi

# 3. Создание JAR-файла
jar -cmf manifest.mf HeapSort.jar -C build/classes/java/main .

# Проверяем, завершилось ли создание JAR-файла успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при создании JAR-файла"
  exit 1
fi

# 4. Запуск тестов с использованием JaCoCo
java -javaagent:libs/jacocoagent.jar=destfile=build/jacoco/test.exec \
  -jar libs/junit-platform-console-standalone-1.11.0.jar \
  --class-path build/classes/java/main:build/classes/java/test \
  --scan-classpath

# Проверяем, завершился ли запуск тестов успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при запуске тестов"
  exit 1
fi

# 5. Генерация отчета о покрытии кода
java -jar libs/jacococli.jar report build/jacoco/test.exec \
    --classfiles build/classes/java/main \
    --sourcefiles src/main/java \
    --html build/reports/jacoco \
    --name "Coverage Report"

# Проверяем, завершилась ли генерация отчета о покрытии успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при генерации отчета о покрытии кода"
  exit 1
fi

# 6. Запуск приложения (опционально)
java -jar HeapSort.jar

# Проверяем, завершился ли запуск приложения успешно
if [ $? -ne 0 ]; then
  echo "Ошибка при запуске приложения"
  exit 1
fi

# 7. Сообщение об успешной сборке
echo "Сборка завершена успешно"