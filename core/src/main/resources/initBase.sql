create table Users (
   id int primary key auto_increment,
   name varchar(30),
   surname varchar(30)
);


create table Tags (
    id int primary key auto_increment,
    name varchar(30)
);

create table Certificates (
    id int primary key auto_increment,
    title varchar(30),
    description mediumtext,
    price double,
    creation_date timestamp,
    last_update_time timestamp,
    duration int(5),
    user_id int
);


create table certificate_tags (
    id int primary key auto_increment,
    certificate_id int,
    tag_id int
);