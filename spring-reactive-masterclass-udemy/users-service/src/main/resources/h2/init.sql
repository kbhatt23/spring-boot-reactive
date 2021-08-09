create table users(
	id bigint auto_increment,
	name varchar(250) NOT NULL,
	balance REAL,
	primary key(id)
);

create table user_transactions(
	id bigint auto_increment,
	amount REAL,
	user_id bigint NOT NULL,
	transaction_time timestamp,
	transaction_type varchar(40) NOT NULL,
	foreign key (user_id) references users(id) on delete cascade,
	primary key(id)
);

insert into users (name,balance)
values('messi',1000);

insert into users (name,balance)
values('xavi',700);