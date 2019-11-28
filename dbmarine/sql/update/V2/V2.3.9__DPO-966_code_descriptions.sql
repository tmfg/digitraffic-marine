alter table code_description rename column description to description_fi;
alter table code_description add column description_en CHARACTER VARYING(256);

update code_description set description_en = 'ship agent' where domain = 'AGENT' and code = '1';
update code_description set description_en = 'ship manager' where domain = 'AGENT' and code = '2';
update code_description set description_en = 'other' where domain = 'AGENT' and code = '3';

update code_description set description_en = 'unloads all cargo' where domain = 'DISCHARGE' and code = '1';
update code_description set description_en = 'unloads part of cargo' where domain = 'DISCHARGE' and code = '2';
update code_description set description_en = 'does not unload' where domain = 'DISCHARGE' and code = '3';
update code_description set description_en = 'ship arriving in ballast' where domain = 'DISCHARGE' and code = '4';

update code_description set description_en = 'Passenger vessel' where domain = 'VESSEL' and code = '10';
update code_description set description_en = 'Passenger car ferry' where domain = 'VESSEL' and code = '20';
update code_description set description_en = 'Rail ferry' where domain = 'VESSEL' and code = '30';
update code_description set description_en = 'Roro' where domain = 'VESSEL' and code = '40';
update code_description set description_en = 'Car carrier' where domain = 'VESSEL' and code = '44';
update code_description set description_en = 'Container ship' where domain = 'VESSEL' and code = '50';
update code_description set description_en = 'Dry-bulk vessel' where domain = 'VESSEL' and code = '60';
update code_description set description_en = 'Dry cargo vessel' where domain = 'VESSEL' and code = '70';
update code_description set description_en = 'Tanker' where domain = 'VESSEL' and code = '80';
update code_description set description_en = 'Oil tanker' where domain = 'VESSEL' and code = '81';
update code_description set description_en = 'Gas tanker' where domain = 'VESSEL' and code = '82';
update code_description set description_en = 'Chemical tanker' where domain = 'VESSEL' and code = '83';
update code_description set description_en = 'Other vessel' where domain = 'VESSEL' and code = '90';
update code_description set description_en = 'Tug' where domain = 'VESSEL' and code = '91';
update code_description set description_en = 'Barge' where domain = 'VESSEL' and code = '93';
update code_description set description_en = 'Pusher' where domain = 'VESSEL' and code = '94';
update code_description set description_en = 'Pusher barge' where domain = 'VESSEL' and code = '95';
update code_description set description_en = 'Tanker barge' where domain = 'VESSEL' and code = '96';
update code_description set description_en = 'Other non propulsory vessel' where domain = 'VESSEL' and code = '97';
update code_description set description_en = 'Other vessel not specified' where domain = 'VESSEL' and code = '99';

update code_description set description_en = 'General cargo' where domain = 'CARGO' and code = '301';
update code_description set description_en = 'Containers' where domain = 'CARGO' and code = '302';
update code_description set description_en = 'Paper, pulp' where domain = 'CARGO' and code = '303';
update code_description set description_en = 'Sawn wood' where domain = 'CARGO' and code = '304';
update code_description set description_en = 'Metals' where domain = 'CARGO' and code = '305';
update code_description set description_en = 'Timber' where domain = 'CARGO' and code = '306';
update code_description set description_en = 'Bulk' where domain = 'CARGO' and code = '307';
update code_description set description_en = 'Oil' where domain = 'CARGO' and code = '308';
update code_description set description_en = 'Chemicals' where domain = 'CARGO' and code = '309';
update code_description set description_en = 'Other cargo' where domain = 'CARGO' and code = '310';

alter table code_description alter column description_en SET NOT NULL;
