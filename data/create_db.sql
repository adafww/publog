CREATE DATABASE IF NOT EXISTS lib;initResponse
INSERT INTO users(is_moderator, reg_time, name, email, password) VALUES (1, '2022-9-06', 'Роман', 'mxhx23@gmail.com', '$2a$12$O3n8pzn04/Sp.0Mkx23dHur1BUkTdQYiR.fawJTVQD4giPDfE0sqK'),
                                                                        (0, '2022-9-07', 'Пётр', 'qwrty76@yandex.ru', '$2a$12$O3n8pzn04/Sp.0Mkx23dHur1BUkTdQYiR.fawJTVQD4giPDfE0sqK');
INSERT INTO global_settings (code, name, value) VALUES ('MULTIUSER_MODE', 'Многопользовательский режим ', 'YES'),
                                                       ('POST_PREMODERATION', 'Премодерация постов ', 'YES'),
                                                       ('STATISTICS_IS_PUBLIC ', 'Показывать всем статистику блога', 'YES');