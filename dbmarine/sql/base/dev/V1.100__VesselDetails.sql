insert into vessel_details(vessel_id,mmsi,name,name_prefix,imo_lloyds,radio_call_sign,radio_call_sign_type,update_timestamp,data_source)
values(1,1122,'test1','ms','123','t1','t1','2019-01-1 10:00:00','test');

insert into vessel_details(vessel_id,mmsi,name,name_prefix,imo_lloyds,radio_call_sign,radio_call_sign_type,update_timestamp,data_source)
values(2,2222,'test2','hs','1234','t2','t2','2019-01-10 10:00:00','test');

insert into vessel_construction(vessel_id, vessel_type_code, vessel_type_name, ice_class_code)
values (1, 30, 'test', 1);

insert into vessel_construction(vessel_id, vessel_type_code, vessel_type_name, ice_class_code)
values (2, 32, 'test2', 1);

insert into vessel_registration(vessel_id, nationality)
values (1, 'FI');

insert into vessel_registration(vessel_id, nationality)
values (2, 'SE');