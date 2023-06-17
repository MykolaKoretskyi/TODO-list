uk:
  Даний додаток являє собою Java-CRUD REST API демонстраційну програму, 
написану з використанням фреймворку Spring Boot.

Опис додатку:

  Логіка програми працює зі списком TODO завдань, які під час роботи програми записуються 
в БД H2 in-memory. Для зберігання даних створюється та заповнюється таблиця з допомогою 
скриптів які знаходяться в папці resources/database. В об'єкті TaskStatus.enum у пекеджі "model" 
описані допустимі значення статусів TODO завдань.

  Додаток запускається на порті 8085 (вказаний у файлі application.properties)

  Для даного додатку реалізовано формування авто-докементації для Swagger, 
з якою можна працювати за адресою: http://localhost:8085/swagger-ui/index.html#

  Реалізовано автентифікацію типу Basic, наявні два користувача з різними рівнями доступу 
до контрольних точок: admin(GET, POST, PUT, DELETE), user(GET, PUT).
Інформація для логування: (username:admin, password:200); (username:user, password:100)

  Налаштовано логування (файл налаштувань logback.xml) із виводом у консоль та записами у файл

  Також реалізована валідація даних що вводяться

  Налаштовано інтернаціоналізацію повідомлень додаток підтримує такі шифри мов: de, en, fr, it, uk. 
Мова задається шляхом введення в адресному рядку браузера після url-адреси: ?language=en 

  Крім цього методи класу контролера та класу сервісу покриті модульними тестами з використанням 
мок-об'єктів. Значна увага приділялась тестуванні зміни статусів завдань, оскільки в кожного 
статусу наявний список допустимих значень. Також реалізовано тестування вийнятків при неправильній 
зміні статусу.

====================================================================================

en:
 This application is a Java-CRUD REST API demo application,
written using the Spring Boot framework.

Application description:

  The logic of the program works with the TODO list of tasks that are recorded during the program's operation
in the H2 in-memory database. To store data, a table is created and filled usingscripts located in the 
resources/database folder. In the TaskStatus.enum object in the "model" package allowable values ​​of TODO task 
statuses are described.

  The application runs on port 8085 (specified in the "application.properties" file)

  For this application, the formation of auto-documentation for Swagger has been implemented,
which you can work with at: http://localhost:8085/swagger-ui/index.html#

  Basic authentication is implemented, there are two users with different access levels
to checkpoints: admin(GET, POST, PUT, DELETE), user(GET, PUT).
Login information: (username:admin, password:200); (username:user, password:100)

  Configured logging (config file logback.xml) with output to the console and records to a file

  Validation of entered data is also implemented

  Internationalization of messages is configured, the application supports the following language codes: de, en, fr, it, uk.
The language is set by entering in the address bar of the browser after the url address: ?language=en

  In addition, the methods of the controller class and the service class are covered by unit tests using
mock objects. Considerable attention was paid to the testing of changes in task statuses, since everyone has
status has a list of valid values. Exception testing is also implemented in the event of an error change of status.