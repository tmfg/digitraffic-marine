create table aton_fault (
	id bigint primary key,
	entry_timestamp timestamp(0) with time zone not null,
	fixed_timestamp timestamp(0) with time zone,
	domain text not null,
	state text not null,
	type text not null,
	fixed boolean not null,
	aton_id int not null,
	aton_name_fi text not null,
	aton_name_se text not null,
	aton_type_fi text not null,
	aton_type_se text not null,
	fairway_number int not null,
	fairway_name_fi text not null,
	fairway_name_se text not null,
	area_number int not null,
	area_description_fi text not null,
	area_description_se text not null,
	geometry geometry not null
);

insert into updated_timestamp(updated_name, updated_time, updated_by)
values('ATON_FAULTS', current_date, 'Updater');
