use addressBook_services;


create table persondata(
personId int auto_increment,
firstName varchar(20) not null,
lastName varchar(20) not null,
city varchar(20) not null,
state varchar(20) not null,
zipCode int not null,
phoneNumber numeric ,
primary key (personId)
);

insert into persondata (firstname,lastname,city,state,zipcode,phoneNumber) values('Pradip','Patil','Pune','Maharashtra',411027,9665353284);
insert into persondata (firstname,lastname,city,state,zipcode,phoneNumber) values('Avinash','Patil','Pune','Maharashtra',411027,9834157022);
insert into persondata  (firstname,lastname,city,state,zipcode,phoneNumber) values('Ketan','Sharma','shirpur','Maharashtra',425427,9923456734);
insert into persondata (firstname,lastname,city,state,zipcode,phoneNumber) values('Mehul','Patil','sendwa','Madhypradesh',394312,8866675434);

select * from persondata;