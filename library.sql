create table admin
(
    id       int AUTO_INCREMENT primary key,
    username varchar(64),
    password char(32)
) ENGINE = InnoDB;

create table user
(
    id          int AUTO_INCREMENT primary key,
    username    varchar(64),
    password    char(32),
    max_hold    int,
    create_time datetime
) ENGINE = InnoDB;

create table book
(
    id     int AUTO_INCREMENT primary key,
    name   varchar(128),
    author varchar(64),
    intro  varchar(1024),
    count  int,
    hot    int
) ENGINE = InnoDB;

create table hold
(
    id   int AUTO_INCREMENT primary key,
    user int,
    book int,
    foreign key (user) references user (id),
    foreign key (book) references book (id)
) ENGINE = InnoDB;

create table br_log
(
    id   int AUTO_INCREMENT primary key,
    user int,
    book int,
    type int,
    time datetime,
    foreign key (user) references user (id),
    foreign key (book) references book (id)
) ENGINE = InnoDB;

create table admin_operate_log
(
    id      int AUTO_INCREMENT primary key,
    admin   int,
    time    datetime,
    content varchar(256),
    foreign key (admin) references admin (id)
) ENGINE = InnoDB;