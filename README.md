![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Debian](https://img.shields.io/badge/Debian-D70A53?style=for-the-badge&logo=debian&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

1. Программа подсчитывает слова в файлах, расположенных в папке files, которая находится в корневой директории проекта. Путь к папке определяется автоматически с использованием переменных среды окружения.

2. Регулярное выражение, длина игнорируемых слов и количество слов с максимальной частотой нахождения "захардкожены" в классе Main.java

3. Узким местом является подсчет частоты нахождения слова с использованием стандартного инструментария JAVA: строка 35 в классе Counter.java
Однако поскольку задачи оптимизировать код не было, было оставлено данное решение.

4. Синхронизация потоков достигается использованием java.util.concurrent.Phaser

5. Логирование и обработка исключений не делались, поскольку не предусмотрены заданием.


