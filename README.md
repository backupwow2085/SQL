# 3.2.-SQL

Инструкция по запуску

1) Запустить БД:
   docker-compose up -d

2) Запустить SUT:
   java -jar artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://localhost:3306/app -P:jdbc.user=app -P:jdbc.password=pass
   Для корректного запуска тестов при повторных прогонах перезапускайте SUT.

3) Запустить тесты:
   ./gradlew test
