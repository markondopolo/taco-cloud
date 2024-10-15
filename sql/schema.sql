create table Ingredient
(
    id             varchar(255) not null
        primary key,
    name           varchar(255) null,
    type           varchar(255) null
);

create table TacoOrder
(
    id             bigint auto_increment
        primary key,
    placedAt       datetime(6)  null,
    ccCVV          varchar(255) null,
    ccExpiration   varchar(255) null,
    ccNumber       varchar(255) null,
    deliveryCity   varchar(255) null,
    deliveryName   varchar(255) null,
    deliveryState  varchar(255) null,
    deliveryStreet varchar(255) null,
    deliveryZip    varchar(255) null
);

create table Taco
(
    created_at     datetime(6)  null,
    id             bigint auto_increment
        primary key,
    taco_order_id  bigint       null,
    name           varchar(255) not null,
    constraint FK_TacoOrder
        foreign key (taco_order_id) references TacoOrder (id)
);

create table Ingredient_Ref
(
    taco           bigint       not null,
    ingredient     varchar(255) not null,
    constraint FK_Ingredient
        foreign key (ingredient) references Ingredient (id),
    constraint FK_Taco
        foreign key (taco) references Taco (id)
);

