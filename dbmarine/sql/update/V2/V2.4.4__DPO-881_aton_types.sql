 create table aton_type (
	name_fi text primary key,
	name_se text not null,
	name_en text
);

insert into aton_type(name_fi, name_se, name_en) values
('Sektoriloisto','Sektorfyr', 'Sector light'),
('Merimajakka','Havsfyr', 'Lighthouse'),
('Linjamerkki','Linjemärke', 'Leading mark'),
('Suuntaloisto','Riktningsfyr', 'Direction light'),
('Apuloisto','Hjälpfyr', 'Minor light'),
('Muu merkki', 'Andra märke', 'Other AtoN')
('Reunamerkki','Randmärke', 'Edge mark'),
('Tutkamerkki','Radarmärke', 'Ramark'),
('Poiju','Boj', 'Buoy'),
('Viitta','Remmare', 'Spar buoy'),
('Tunnusmajakka', 'Båk', 'Beacon tower'),
('Kummeli','Kummel', 'Cairn'),
('Määräys- tai rajoitusmerkki','Påbud eller begränsningsmärke', 'Mandatory or warning sign'),
('Kaapeli- tai johtotaulu','Kabel- och ledningstavla', 'Cable and pipeline panel'),
('Kieltomerkki','Förbudsmärke', 'Prohibitory sign'),
('Tiedotusmerkki','Informationsmärke', 'Informative sign')
;

alter table aton_fault add constraint ATON_FAULT_ATON_TYPE_FKEY foreign key(aton_type_fi) references aton_type(NAME_FI);
create index aton_fault_aton_type_fk_i on aton_fault(aton_type_fi);

alter table aton_fault drop column aton_type_se;