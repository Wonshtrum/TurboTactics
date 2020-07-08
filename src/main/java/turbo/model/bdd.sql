DROP DATABASE jeebdd;
CREATE DATABASE jeebdd;
USE jeebdd;
CREATE TABLE users (
	id int(10) NOT NULL UNIQUE auto_increment,
	login varchar(30),
	password varchar(30),
	score numeric(9,2),
	PRIMARY KEY(id)
);
CREATE TABLE achievements (
	id int(10) NOT NULL UNIQUE auto_increment,
	ref varchar(30) NOT NULL UNIQUE,
	name varchar(255),
	description varchar(255),
	img_url varchar(255),
	PRIMARY KEY(id)
);
CREATE TABLE links (
	id int(10) NOT NULL auto_increment,
	id_user int(10) NOT NULL,
	id_achievement int(10) NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_user FOREIGN KEY (id_user) REFERENCES users(id),
	CONSTRAINT fk_achievement FOREIGN KEY (id_achievement) REFERENCES achievements(id)
);
INSERT INTO users(login, password, score) VALUES ("Ines", "1234", 0);
INSERT INTO users(login, password, score) VALUES ("Seytkamal", "1234", 0);
INSERT INTO achievements(ref, name, description, img_url) VALUES ("create_party", "A Long-expected Party", "Create, for the first time, a party you can play with your friends", "createParty.png");
INSERT INTO achievements(ref, name, description, img_url) VALUES ("touch_slime", "The Battle", "Approach menacingly a Slime", "slime.png");
