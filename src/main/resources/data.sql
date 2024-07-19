INSERT INTO role(role_id,role) VALUES(nextval('role_id_seq'),'admin'),(nextval('role_id_seq'),'client');

INSERT INTO account_user(user_id,name,password,role_id) VALUES(nextval('user_id_seq'),'user1','testpassword',1),(nextval('user_id_seq'),'user2','testpassword',2),(nextval('user_id_seq'),'user3','testpassword',2);

INSERT INTO account(account_id, number, balance, block,user_id) VALUES(nextval('account_id_seq'),'1234-1234-1234',100.00, FALSE, 2),(nextval('account_id_seq'),'1233-1233-1233', 200.00,TRUE,3);