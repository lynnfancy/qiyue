--root登录mysql
create database user;
grant all privileges on user.* to 'test'@'localhost' identified by 'root123';