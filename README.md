# DevPub
Блоговый движок DevPub позволяет пользователям публиковать статьи и оставлять комментарии, а посетителям просматривать статьи, отсортированные по дате создания, тегу, ключевому слову или популярности статьи. В блоге есть возможность авторизации, регистрации новых пользователей и восстановления пароля. Если пользователь является модератором, то есть возможность модерации и редактирования глобальных настроек. Возможен просмотр статистики всего блога или статистики авторизованного пользователя.

### Стек технологий
- Java 11
- Spring Boot 2.2.4.RELEASE
- Maven
- MySQL 8
- Git

### С чего начать
Для запуска проекта локально необходимо создать пустую базу данных. В resources/application.yml указываются ее URL, пользователь и пароль (spring.datasource (url, user, password)).

Пароли для тестовых пользователей зашифрованы в БД с помощью BCrypt (12 rounds).

### Ссылка на рабочий проект
[devpub](http://devpub.ddns.net/)
