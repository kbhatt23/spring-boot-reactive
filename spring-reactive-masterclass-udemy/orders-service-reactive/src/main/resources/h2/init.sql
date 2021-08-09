create table orders(
	id bigint auto_increment,
	transaction_id bigint,
	user_id bigint,
	product_id varchar(50),
	price REAL,
	primary key(id)
);