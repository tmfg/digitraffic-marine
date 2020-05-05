CREATE TABLE aton_fault_type (
	name_fi text PRIMARY KEY,
	name_se text NOT NULL,
	name_en text NOT NULL
);

insert into aton_fault_type(name_fi, name_se, name_en) values
    ('Valo pimeä', 'Ljuset slocknat', 'Unlit'),
    ('Valo virheellinen', 'Felaktigt ljus', 'Light unreliable'),
    ('Virheellinen sijainti', 'Felaktig utplacering', 'Off station'),
    ('Tuhoutunut/kadonnut', 'Förstörd/försvunnen', 'Destroyed/Missing'),
    ('Virheelliset sektorit', 'Felaktiga sektorer', 'Sector limits unreliable'),
    ('Rikkoutunut', 'Söndrig', 'Damaged'),
    ('Näkymäeste', 'Synlighetshinder', 'Limited visibility'),
    ('Turvalaitetyö', 'Underhållsarbete', 'Maintenance works')
    ;

alter table aton_fault add constraint ATON_FAULT_TYPE_FKEY FOREIGN KEY(type) references aton_fault_type(NAME_FI);
create index aton_fault_type_fk_i on aton_fault(type);

