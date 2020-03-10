create table aton_fault_state (
    name_fi text primary key,
    name_se text not null,
    name_en text not null
);

insert into aton_fault_state(name_fi, name_se, name_en) values
    ('Peruttu', 'Annulerrad', 'Cancelled'),
    ('Kirjattu', 'Registrerad', 'Registered'),
    ('Avoin', 'Öppen', 'Open'),
    ('Korjattu', 'Reparerad', 'Repaired'),
    ('Aiheeton', 'Obefogad', 'False alarm');

alter table aton_fault add constraint ATON_FAULT_STATE_FKEY foreign key(state) references aton_fault_state(NAME_FI);
create index aton_fault_state_fk_i on aton_fault(state);