CREATE SEQUENCE seq_agent_info
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_cargo_info
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_imo_information
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_port_area_details
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_port_restriction
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_ship_activity
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



CREATE SEQUENCE seq_ship_planned_activity
INCREMENT BY 1
MAXVALUE 9223372036854775807
MINVALUE 1
NO CYCLE;



-- ------------ Write CREATE-TABLE-stage scripts -----------

CREATE TABLE IF NOT EXISTS agent_info(
agent_info_id NUMERIC(10,0) NOT NULL,
port_call_id NUMERIC(10,0) NOT NULL,
role NUMERIC(1,0),
port_call_direction CHARACTER VARYING(32),
name CHARACTER VARYING(50),
edi_number CHARACTER VARYING(16)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS berth(
locode CHARACTER VARYING(5) NOT NULL,
port_area_code CHARACTER VARYING(6) NOT NULL,
berth_code CHARACTER VARYING(6) NOT NULL,
berth_name CHARACTER VARYING(64) NOT NULL
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS cargo_info(
cargo_info_id NUMERIC(10,0) NOT NULL,
port_area_details_id NUMERIC(10,0) NOT NULL,
cargo_discharge_code NUMERIC(3,0),
cargo_description CHARACTER VARYING(50),
cargo_amount NUMERIC(8,0)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS code_description(
domain CHARACTER VARYING(16) NOT NULL,
code CHARACTER VARYING(8) NOT NULL,
description CHARACTER VARYING(256) NOT NULL
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS imo_information(
imo_information_id NUMERIC(10,0) NOT NULL,
port_call_id NUMERIC(10,0) NOT NULL,
imo_general_declaration CHARACTER VARYING(9),
brief_particulars_voyage CHARACTER VARYING(512),
port_of_discharge CHARACTER VARYING(5),
number_of_crew NUMERIC(9,0),
number_of_passangers NUMERIC(9,0),
cargo_declaration_ob NUMERIC(9,0),
crew_lists_ob NUMERIC(9,0),
crews_effects_declarations_ob NUMERIC(9,0),
ship_stores_declarations_ob NUMERIC(9,0),
passanger_lists_ob NUMERIC(9,0),
health_declarations_ob NUMERIC(9,0)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS locking_table(
lock_name CHARACTER VARYING(40) NOT NULL,
instance_id CHARACTER VARYING(80),
lock_locked TIMESTAMP(0) WITH TIME ZONE,
lock_expires TIMESTAMP(0) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS port_area(
locode CHARACTER VARYING(5) NOT NULL,
port_area_code CHARACTER VARYING(6) NOT NULL,
port_area_name CHARACTER VARYING(32) NOT NULL,
wgs84_long NUMERIC(8,5),
wgs84_lat NUMERIC(7,5)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS port_area_details(
port_area_details_id NUMERIC(10,0) NOT NULL,
port_call_id NUMERIC(10,0) NOT NULL,
port_area_code CHARACTER VARYING(8),
port_area_name CHARACTER VARYING(32),
berth_code CHARACTER VARYING(6),
berth_name CHARACTER VARYING(64),
eta TIMESTAMP(6) WITH TIME ZONE,
eta_timestamp TIMESTAMP(6) WITH TIME ZONE,
eta_source CHARACTER VARYING(12),
etd TIMESTAMP(6) WITH TIME ZONE,
etd_timestamp TIMESTAMP(6) WITH TIME ZONE,
etd_source CHARACTER VARYING(12),
ata TIMESTAMP(6) WITH TIME ZONE,
ata_timestamp TIMESTAMP(6) WITH TIME ZONE,
ata_source CHARACTER VARYING(12),
atd TIMESTAMP(6) WITH TIME ZONE,
atd_timestamp TIMESTAMP(6) WITH TIME ZONE,
atd_source CHARACTER VARYING(12),
arrival_draught NUMERIC(7,2),
departure_draught NUMERIC(7,2)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS port_call(
port_call_id NUMERIC(10,0) NOT NULL,
port_call_timestamp TIMESTAMP(6) WITH TIME ZONE,
customs_reference CHARACTER VARYING(256),
port_to_visit CHARACTER VARYING(5),
prev_port CHARACTER VARYING(5),
next_port CHARACTER VARYING(5),
domestic_traffic_arrival boolean,
domestic_traffic_departure boolean,
arrival_with_cargo boolean,
discharge NUMERIC(1,0),
not_loading boolean,
ship_master_arrival CHARACTER VARYING(50),
ship_master_departure CHARACTER VARYING(50),
management_name_arrival CHARACTER VARYING(50),
management_name_departure CHARACTER VARYING(50),
forwarder_name_arrival CHARACTER VARYING(50),
forwarder_name_departure CHARACTER VARYING(50),
free_text_arrival CHARACTER VARYING(2000),
free_text_departure CHARACTER VARYING(2000),
vessel_name CHARACTER VARYING(50) NOT NULL,
vessel_name_prefix CHARACTER VARYING(4),
radio_call_sign CHARACTER VARYING(8) NOT NULL,
radio_call_sign_type CHARACTER VARYING(4) NOT NULL,
imo_lloyds NUMERIC(10,0),
mmsi NUMERIC(10,0),
certificate_issuer CHARACTER VARYING(50),
certificate_start_date TIMESTAMP(6) WITH TIME ZONE,
certificate_end_date TIMESTAMP(6) WITH TIME ZONE,
current_security_level NUMERIC(1,0),
nationality CHARACTER VARYING(32),
vessel_type_code NUMERIC(2,0)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS port_restriction(
id NUMERIC(10,0) NOT NULL,
locode CHARACTER VARYING(7) NOT NULL,
order_number NUMERIC(38,0) NOT NULL,
is_current boolean,
port_restricted boolean,
port_closed boolean,
issue_time TIMESTAMP(6) WITH TIME ZONE,
last_modified TIMESTAMP(6) WITH TIME ZONE,
valid_from TIMESTAMP(0) WITH TIME ZONE,
valid_until TIMESTAMP(0) WITH TIME ZONE,
raw_text CHARACTER VARYING(1024),
formatted_text CHARACTER VARYING(1024)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS ship_activity(
id NUMERIC(10,0) NOT NULL,
vessel_pk CHARACTER VARYING(16),
order_number NUMERIC(38,0) NOT NULL,
activity_type CHARACTER VARYING(5) NOT NULL,
activity_text CHARACTER VARYING(64) NOT NULL,
activity_comment CHARACTER VARYING(256),
begin_time TIMESTAMP(6) WITH TIME ZONE,
end_time TIMESTAMP(6) WITH TIME ZONE,
timestamp_begin TIMESTAMP(6) WITH TIME ZONE NOT NULL,
timestamp_end TIMESTAMP(6) WITH TIME ZONE,
timestamp_canceled TIMESTAMP(6) WITH TIME ZONE,
operating_icebreaker_pk CHARACTER VARYING(16),
operating_icebreaker_name CHARACTER VARYING(128),
operated_vessel_pk CHARACTER VARYING(16),
operated_vessel_name CHARACTER VARYING(128),
convoy_order NUMERIC(38,0)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS ship_planned_activity(
id NUMERIC(10,0) NOT NULL,
vessel_pk CHARACTER VARYING(16),
order_number NUMERIC(38,0) NOT NULL,
activity_type CHARACTER VARYING(3) NOT NULL,
activity_text CHARACTER VARYING(64) NOT NULL,
planned_vessel_pk CHARACTER VARYING(16) NOT NULL,
planning_vessel_pk CHARACTER VARYING(16) NOT NULL,
ordering NUMERIC(38,0) NOT NULL,
planned_when CHARACTER VARYING(1024),
planned_where CHARACTER VARYING(1024),
plan_comment CHARACTER VARYING(1024),
plan_timestamp TIMESTAMP(6) WITH TIME ZONE,
plan_timestamp_realized TIMESTAMP(6) WITH TIME ZONE,
plan_timestamp_canceled TIMESTAMP(6) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS ship_state(
vessel_pk CHARACTER VARYING(16) NOT NULL,
timestamp TIMESTAMP(6) WITH TIME ZONE NOT NULL,
longitude NUMERIC(11,8) NOT NULL,
latitude NUMERIC(11,8) NOT NULL,
pos_printable CHARACTER VARYING(32) NOT NULL,
pos_accuracy CHARACTER VARYING(16),
pos_source CHARACTER VARYING(4) NOT NULL,
pos_area CHARACTER VARYING(64),
speed NUMERIC(4,1),
course NUMERIC(4,1),
heading NUMERIC(3,0),
ais_draught NUMERIC(4,1),
ais_state NUMERIC(38,0),
ais_state_text CHARACTER VARYING(64),
ais_destination CHARACTER VARYING(64),
moving_since TIMESTAMP(6) WITH TIME ZONE,
stopped_since TIMESTAMP(6) WITH TIME ZONE,
inactive_since TIMESTAMP(6) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS ship_voyage(
vessel_pk CHARACTER VARYING(16) NOT NULL,
from_locode CHARACTER VARYING(7),
from_name CHARACTER VARYING(64),
from_atd TIMESTAMP(6) WITH TIME ZONE,
in_locode CHARACTER VARYING(7),
in_name CHARACTER VARYING(64),
in_ata TIMESTAMP(6) WITH TIME ZONE,
in_etd TIMESTAMP(6) WITH TIME ZONE,
dest_locode CHARACTER VARYING(7),
dest_name CHARACTER VARYING(64),
dest_eta TIMESTAMP(6) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS ssn_location(
locode CHARACTER VARYING(5) NOT NULL,
location_name CHARACTER VARYING(64),
country CHARACTER VARYING(64) NOT NULL,
wgs84_lat NUMERIC(7,5),
wgs84_long NUMERIC(8,5)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS updated_timestamp(
updated_name CHARACTER VARYING(256) NOT NULL,
updated_time TIMESTAMP(6) WITH TIME ZONE NOT NULL,
updated_by CHARACTER VARYING(32)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel(
mmsi NUMERIC(9,0) NOT NULL,
timestamp NUMERIC(13,0) NOT NULL,
name CHARACTER VARYING(256) NOT NULL,
ship_type NUMERIC(2,0) NOT NULL,
reference_point_a NUMERIC(3,0) NOT NULL,
reference_point_b NUMERIC(3,0) NOT NULL,
reference_point_c NUMERIC(2,0) NOT NULL,
reference_point_d NUMERIC(2,0) NOT NULL,
pos_type NUMERIC(2,0) NOT NULL,
draught NUMERIC(5,0) NOT NULL,
imo NUMERIC(9,0) NOT NULL,
eta NUMERIC(7,0) NOT NULL,
call_sign CHARACTER VARYING(256),
destination CHARACTER VARYING(256)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_construction(
vessel_id NUMERIC(10,0) NOT NULL,
vessel_type_code NUMERIC(9,0) NOT NULL,
vessel_type_name CHARACTER VARYING(50) NOT NULL,
ice_class_code CHARACTER VARYING(32) NOT NULL,
ice_class_issue_date TIMESTAMP(6) WITH TIME ZONE,
ice_class_issue_place CHARACTER VARYING(50),
ice_class_end_date TIMESTAMP(6) WITH TIME ZONE,
classification_society CHARACTER VARYING(50),
double_bottom boolean,
inert_gas_system boolean,
ballast_tank boolean
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_details(
vessel_id NUMERIC(10,0) NOT NULL,
mmsi NUMERIC(10,0),
name CHARACTER VARYING(50) NOT NULL,
name_prefix CHARACTER VARYING(4),
imo_lloyds NUMERIC(10,0),
radio_call_sign CHARACTER VARYING(8) NOT NULL,
radio_call_sign_type CHARACTER VARYING(4) NOT NULL,
update_timestamp TIMESTAMP(6) WITH TIME ZONE NOT NULL,
data_source CHARACTER VARYING(16)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_dimensions(
vessel_id NUMERIC(10,0) NOT NULL,
tonnage_certificate_issuer CHARACTER VARYING(50),
date_of_issue TIMESTAMP(6) WITH TIME ZONE,
gross_tonnage NUMERIC(9,0) NOT NULL,
net_tonnage NUMERIC(9,0) NOT NULL,
death_weight NUMERIC(9,0),
length NUMERIC(10,2),
overall_length NUMERIC(10,2),
height NUMERIC(10,2),
breadth NUMERIC(10,2),
draught NUMERIC(10,2),
max_speed NUMERIC(10,2),
engine_power CHARACTER VARYING(30),
total_power NUMERIC(9,0),
max_persons NUMERIC(9,0),
max_passengers NUMERIC(9,0),
keel_date TIMESTAMP(6) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_location(
mmsi NUMERIC(9,0) NOT NULL,
timestamp_ext NUMERIC(15,0) NOT NULL,
x NUMERIC(17,15) NOT NULL,
y NUMERIC(17,15) NOT NULL,
sog NUMERIC(4,1) NOT NULL,
cog NUMERIC(4,1) NOT NULL,
nav_stat NUMERIC(2,0) NOT NULL,
rot NUMERIC(3,0) NOT NULL,
pos_acc boolean NOT NULL,
raim boolean NOT NULL,
timestamp NUMERIC(13,0) NOT NULL,
heading NUMERIC(3,0)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_registration(
vessel_id NUMERIC(10,0) NOT NULL,
nationality CHARACTER VARYING(3),
port_of_registry CHARACTER VARYING(50),
domicile CHARACTER VARYING(16)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS vessel_system(
vessel_id NUMERIC(10,0) NOT NULL,
ship_owner CHARACTER VARYING(50),
ship_telephone1 CHARACTER VARYING(25),
ship_telephone2 CHARACTER VARYING(25),
ship_fax CHARACTER VARYING(25),
ship_email CHARACTER VARYING(256),
ship_verifier CHARACTER VARYING(32)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS winter_navigation_dirway(
name CHARACTER VARYING(256) NOT NULL,
issue_time TIMESTAMP(6) WITH TIME ZONE,
issuer_code CHARACTER VARYING(16),
issuer_name CHARACTER VARYING(64),
valid_until TIMESTAMP(6) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS winter_navigation_dirway_point(
dirway_name CHARACTER VARYING(256) NOT NULL,
order_number NUMERIC(10,0) NOT NULL,
longitude NUMERIC(11,8) NOT NULL,
latitude NUMERIC(11,8) NOT NULL,
sea_area CHARACTER VARYING(128)
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS winter_navigation_port(
locode CHARACTER VARYING(7) NOT NULL,
name CHARACTER VARYING(50),
nationality CHARACTER VARYING(3),
longitude NUMERIC(11,8),
latitude NUMERIC(11,8),
sea_area CHARACTER VARYING(128),
obsolete_date TIMESTAMP(0) WITH TIME ZONE
)
WITH (
OIDS=FALSE
);



CREATE TABLE IF NOT EXISTS winter_navigation_ship(
vessel_pk CHARACTER VARYING(16) NOT NULL,
mmsi CHARACTER VARYING(10),
vessel_source CHARACTER VARYING(16),
name CHARACTER VARYING(128) NOT NULL,
imo CHARACTER VARYING(10),
call_sign CHARACTER VARYING(32),
dwt NUMERIC(38,0),
length NUMERIC(7,2),
width NUMERIC(5,2),
ais_length NUMERIC(7,2),
ais_width NUMERIC(5,2),
dimensions CHARACTER VARYING(128),
nominal_draught NUMERIC(5,2),
ice_class CHARACTER VARYING(8),
nat_code CHARACTER VARYING(2),
nationality CHARACTER VARYING(64),
ship_type CHARACTER VARYING(16),
ais_ship_type NUMERIC(38,0)
)
WITH (
OIDS=FALSE
);



-- ------------ Write CREATE-INDEX-stage scripts -----------

CREATE INDEX agin_poca_fk_i
ON agent_info
USING BTREE (port_call_id ASC);



CREATE INDEX cain_pade_fk_i
ON cargo_info
USING BTREE (port_area_details_id ASC);



CREATE INDEX imin_poca_fk_i
ON imo_information
USING BTREE (port_call_id ASC);



CREATE UNIQUE INDEX locking_table_ui
ON locking_table
USING BTREE (lock_name ASC, instance_id ASC);



CREATE INDEX pade_poca_fk_id
ON port_area_details
USING BTREE (port_call_id ASC);



CREATE INDEX poca_port_to_visit_i
ON port_call
USING BTREE (port_to_visit ASC, port_call_id ASC);



CREATE INDEX port_rstrctn_winter_port_fk_i
ON port_restriction
USING BTREE (locode ASC);



CREATE INDEX ship_activity_ship_fk_i
ON ship_activity
USING BTREE (vessel_pk ASC);



CREATE INDEX ship_planned_activity_ship_f_i
ON ship_planned_activity
USING BTREE (vessel_pk ASC);



CREATE INDEX vessel_timestamp_i
ON vessel
USING BTREE (timestamp ASC);



CREATE INDEX vessel_location_timestamp_i
ON vessel_location
USING BTREE (timestamp_ext ASC);



-- ------------ Write CREATE-CONSTRAINT-stage scripts -----------

ALTER TABLE agent_info
ADD CONSTRAINT agin_pk PRIMARY KEY (agent_info_id);



ALTER TABLE berth
ADD CONSTRAINT berth_pk PRIMARY KEY (locode, port_area_code, berth_code);



ALTER TABLE cargo_info
ADD CONSTRAINT cain_pk PRIMARY KEY (cargo_info_id);



ALTER TABLE code_description
ADD CONSTRAINT code_description_pk PRIMARY KEY (domain, code);



ALTER TABLE imo_information
ADD CONSTRAINT imin_general_declaration_c CHECK (imo_general_declaration IN ('Arrival', 'Departure'));



ALTER TABLE imo_information
ADD CONSTRAINT imin_pk PRIMARY KEY (imo_information_id);



ALTER TABLE locking_table
ADD CONSTRAINT sys_c001326399 PRIMARY KEY (lock_name);



ALTER TABLE port_area
ADD CONSTRAINT porta_pk PRIMARY KEY (locode, port_area_code);



ALTER TABLE port_area_details
ADD CONSTRAINT pade_pk PRIMARY KEY (port_area_details_id);




ALTER TABLE port_call
ADD CONSTRAINT poca_pk PRIMARY KEY (port_call_id);



ALTER TABLE port_restriction
ADD CONSTRAINT port_restriction_pk PRIMARY KEY (id);




ALTER TABLE ship_activity
ADD CONSTRAINT ship_activity_pk PRIMARY KEY (id);



ALTER TABLE ship_planned_activity
ADD CONSTRAINT ship_planned_activity_pk PRIMARY KEY (id);



ALTER TABLE ship_state
ADD CONSTRAINT ship_state_pk PRIMARY KEY (vessel_pk);



ALTER TABLE ship_voyage
ADD CONSTRAINT ship_voyage_pk PRIMARY KEY (vessel_pk);



ALTER TABLE ssn_location
ADD CONSTRAINT ssnl_pk PRIMARY KEY (locode);



ALTER TABLE updated_timestamp
ADD CONSTRAINT upti_fk PRIMARY KEY (updated_name);



ALTER TABLE vessel
ADD CONSTRAINT vessel_pk PRIMARY KEY (mmsi);



ALTER TABLE vessel_construction
ADD CONSTRAINT vessel_construction_pk PRIMARY KEY (vessel_id);



ALTER TABLE vessel_details
ADD CONSTRAINT vessel_details_pk PRIMARY KEY (vessel_id);



ALTER TABLE vessel_dimensions
ADD CONSTRAINT vessel_dimensions_pk PRIMARY KEY (vessel_id);



ALTER TABLE vessel_location
ADD CONSTRAINT vessel_location_pk PRIMARY KEY (mmsi);




ALTER TABLE vessel_registration
ADD CONSTRAINT vessel_registration_pk PRIMARY KEY (vessel_id);



ALTER TABLE vessel_system
ADD CONSTRAINT vessel_system_pk PRIMARY KEY (vessel_id);



ALTER TABLE winter_navigation_dirway
ADD CONSTRAINT winter_navigation_dirway_pk PRIMARY KEY (name);



ALTER TABLE winter_navigation_dirway_point
ADD CONSTRAINT dirway_point_pk PRIMARY KEY (dirway_name, order_number);



ALTER TABLE winter_navigation_port
ADD CONSTRAINT winter_navigation_port_pk PRIMARY KEY (locode);



ALTER TABLE winter_navigation_ship
ADD CONSTRAINT winter_navigation_ship_pk PRIMARY KEY (vessel_pk);



-- ------------ Write CREATE-FOREIGN-KEY-CONSTRAINT-stage scripts -----------

ALTER TABLE agent_info
ADD CONSTRAINT agin_poca_fk FOREIGN KEY (port_call_id)
REFERENCES port_call (port_call_id)
ON DELETE NO ACTION;



ALTER TABLE berth
ADD CONSTRAINT berth_porta_fk FOREIGN KEY (locode, port_area_code)
REFERENCES port_area (locode, port_area_code)
ON DELETE NO ACTION;



ALTER TABLE cargo_info
ADD CONSTRAINT cain_pade_fk FOREIGN KEY (port_area_details_id)
REFERENCES port_area_details (port_area_details_id)
ON DELETE NO ACTION;



ALTER TABLE imo_information
ADD CONSTRAINT imin_poca_fk FOREIGN KEY (port_call_id)
REFERENCES port_call (port_call_id)
ON DELETE NO ACTION;



ALTER TABLE port_area
ADD CONSTRAINT porta_ssnl_fk FOREIGN KEY (locode)
REFERENCES ssn_location (locode)
ON DELETE NO ACTION;



ALTER TABLE port_area_details
ADD CONSTRAINT pade_poca_fk FOREIGN KEY (port_call_id)
REFERENCES port_call (port_call_id)
ON DELETE NO ACTION;



ALTER TABLE port_restriction
ADD CONSTRAINT port_rstrctn_winter_port_fk FOREIGN KEY (locode)
REFERENCES winter_navigation_port (locode)
ON DELETE NO ACTION;





ALTER TABLE ship_activity
ADD CONSTRAINT ship_activity_ship_fk FOREIGN KEY (vessel_pk)
REFERENCES winter_navigation_ship (vessel_pk)
ON DELETE NO ACTION;



ALTER TABLE ship_planned_activity
ADD CONSTRAINT ship_planned_activity_ship_fk FOREIGN KEY (vessel_pk)
REFERENCES winter_navigation_ship (vessel_pk)
ON DELETE NO ACTION;



ALTER TABLE ship_state
ADD CONSTRAINT ship_state_ship_fk FOREIGN KEY (vessel_pk)
REFERENCES winter_navigation_ship (vessel_pk)
ON DELETE NO ACTION;



ALTER TABLE ship_voyage
ADD CONSTRAINT ship_voyage_ship_fk FOREIGN KEY (vessel_pk)
REFERENCES winter_navigation_ship (vessel_pk)
ON DELETE NO ACTION;



ALTER TABLE vessel_construction
ADD CONSTRAINT vecons_vess_fk FOREIGN KEY (vessel_id)
REFERENCES vessel_details (vessel_id)
ON DELETE NO ACTION;



ALTER TABLE vessel_dimensions
ADD CONSTRAINT vedim_vess_fk FOREIGN KEY (vessel_id)
REFERENCES vessel_details (vessel_id)
ON DELETE NO ACTION;



ALTER TABLE vessel_registration
ADD CONSTRAINT vesr_vess_fk FOREIGN KEY (vessel_id)
REFERENCES vessel_details (vessel_id)
ON DELETE NO ACTION;



ALTER TABLE vessel_system
ADD CONSTRAINT vesys_vess_fk FOREIGN KEY (vessel_id)
REFERENCES vessel_details (vessel_id)
ON DELETE NO ACTION;



ALTER TABLE winter_navigation_dirway_point
ADD CONSTRAINT dirway_point_dirway_fk FOREIGN KEY (dirway_name)
REFERENCES winter_navigation_dirway (name)
ON DELETE CASCADE;



-- ------------ Write CREATE-FUNCTION-stage scripts -----------

CREATE OR REPLACE FUNCTION great_circle_distance(IN lat1 DOUBLE PRECISION, IN lon1 DOUBLE PRECISION, IN lat2 DOUBLE PRECISION, IN lon2 DOUBLE PRECISION)
RETURNS DOUBLE PRECISION
AS
$BODY$
DECLARE
    R DOUBLE PRECISION := 6371
    /* Radius of earth in kilometers */;
    pi DOUBLE PRECISION := 3.14159265358979;
    dLat DOUBLE PRECISION := lat2 * pi / 180 - lat1 * pi / 180;
    dLon DOUBLE PRECISION := lon2 * pi / 180 - lon1 * pi / 180;
    a DOUBLE PRECISION := SIN(dLat / 2) * SIN(dLat / 2) + COS(lat1 * pi / 180) * COS(lat2 * pi / 180) * SIN(dLon / 2) * SIN(dLon / 2);
    d DOUBLE PRECISION := 2 * R * ASIN(SQRT(a));
BEGIN
    RETURN d;
END;
$BODY$
LANGUAGE  plpgsql;