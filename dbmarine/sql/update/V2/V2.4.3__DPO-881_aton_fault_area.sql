CREATE TABLE area (
	area_number integer PRIMARY KEY,
	description_fi text NOT NULL,
	description_se text NOT NULL,
	description_en text NOT NULL
);

alter table aton_fault add constraint ATON_FAULT_AREA_FKEY foreign key(area_number) references area(area_number);
create index aton_fault_area_fk_i on aton_fault(area_number);

alter table aton_fault drop column area_description_fi;
alter table aton_fault drop column area_description_se;

insert into area(area_number, description_fi, description_en, description_se) values
(0,'Tuntematon', 'Unknown', 'Okänd'),
(1,'Perämeri','Bay of Bothnia','Bottenviken'),
(2,'Selkämeri','Bothnian Sea','Bottenhavet'),
(3,'Ahvenanmeri','Aland Sea','Ålands hav'),
(4,'Saaristomeri','Archipelago Sea','Skärgårdshavet'),
(5,'Suomenlahti','Gulf of Finland','Finska viken'),
(6,'Itämeri', 'Baltic Sea', 'Östersjön'),
(7,'Vuoksen vesistö','Vuoksi watercourse','Vuoksi vattendrag'),
(8,'Kymijoen vesistö','Kymijoki watercourse','Kymmene älv vattendrag'),
(9,'Kokemäenjoen vesistö','Kokemäkijoki watercourse','Kumo älv vattendrag'),
(10,'Oulujärvi','Oulujoki watercourse','Ule träsk'),
(11,'Sotkamon järvet', 'Other lakes and canals', 'Sotkamo sjöar'),
(12,'Kuhmon järvet', 'Other lakes and canals', 'Kuhmo sjöar'),
(13,'Kuusamon järvet', 'Other lakes and canals', 'Kuusamo sjöar'),
(14,'Kiantajärvi', 'Other lakes and canals', 'Kianta sjö'),
(15,'Kemijärvi', 'Other lakes and canals', 'Kemijärvi sjö'),
(16,'Lokka-Porttipahta', 'Other lakes and canals', ''),
(17,'Kemijärvi', 'Other lakes and canals', 'Kemijärvi sjö'),
(18,'Inarinjärvi', 'Other lakes and canals', 'Enare träsk'),
(19,'Nitsijärvi', 'Other lakes and canals', 'Nitsi sjö'),
(20,'Miekkojärvi', 'Other lakes and canals', 'Miekko sjö'),
(21,'Tornionjoki', 'Other lakes and canals', 'Torne älv'),
(22,'Ähtärinjärvi', 'Other lakes and canals', 'Etseri sjö'),
(23,'Lappajärvi', 'Other lakes and canals', 'Lappajärvi'),
(24,'Pyhäjärvi', 'Other lakes and canals', 'Pyhäjärvi'),
(25,'Lohjanjärvi', 'Other lakes and canals', 'Lojo sjön');