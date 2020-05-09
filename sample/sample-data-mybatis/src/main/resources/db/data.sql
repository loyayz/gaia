DELETE FROM user;

INSERT INTO user (id, name, age, email, role_id)
VALUES (1, '张三', 1, 'test1@loyayz.com', 1),
       (2, '李四', 20, 'test2@loyayz.com', 2),
       (3, '王五', 28, 'test3@loyayz.com', 2),
       (4, '赵六', 21, 'test4@loyayz.com', 3);
