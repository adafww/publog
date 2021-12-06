INSERT INTO users(is_moderator, reg_time, name, email, password) VALUE (1, '2021-12-06', 'Роман', 'mxhx23@gmail.com', '1234');
INSERT INTO captcha_codes (time, code, secret_code) VALUE ('2021-12-06', 'sad1', 'dsadsad');
INSERT INTO global_settings (code, name, value) VALUE ('2021-12-06', 'status', 'visible');
INSERT INTO posts (is_active, text, time, title, moderation_status, view_count, moderator_id, user_id) VALUE (1, 'some text', '2021-12-06', 'some title', 'NEW', 2, 1, 1);
INSERT INTO post_comments (text, time, post_id, user_id) VALUE ('some text', '2021-12-06', 1, 1);
INSERT INTO post_votes (time, value, post_id, user_id) VALUE ('2021-12-06', 1, 1, 1);
INSERT INTO tags (name) VALUE ('some text');
INSERT INTO tag2post (post_id, tag_id) VALUE (1, 1);
