alter table code_description rename column description to description_fi;
alter table code_description add column description_en CHARACTER VARYING(256);

update code_description set description_en = 'ship agent' where domain = 'AGENT' and code = '1';
update code_description set description_en = 'ship manager' where domain = 'AGENT' and code = '2';
update code_description set description_en = 'other' where domain = 'AGENT' and code = '3';

update code_description set description_en = 'unloads all cargo' where domain = 'DISCHARGE' and code = '1';
update code_description set description_en = 'unloads part of cargo' where domain = 'DISCHARGE' and code = '2';
update code_description set description_en = 'does not unload' where domain = 'DISCHARGE' and code = '3';
update code_description set description_en = 'ship arriving in ballast' where domain = 'DISCHARGE' and code = '4';

update code_description set description_en = 'passenger vessel' where domain = 'VESSEL' and code = '10';
update code_description set description_en = 'passenger car ferry' where domain = 'VESSEL' and code = '20';
update code_description set description_en = 'rail ferry' where domain = 'VESSEL' and code = '30';
update code_description set description_en = 'roro' where domain = 'VESSEL' and code = '40';
update code_description set description_en = 'car carrier' where domain = 'VESSEL' and code = '44';
update code_description set description_en = 'container ship' where domain = 'VESSEL' and code = '50';
update code_description set description_en = 'dry-bulk vessel' where domain = 'VESSEL' and code = '60';
update code_description set description_en = 'dry cargo vessel' where domain = 'VESSEL' and code = '70';
update code_description set description_en = 'tanker' where domain = 'VESSEL' and code = '80';
update code_description set description_en = 'oil tanker' where domain = 'VESSEL' and code = '81';
update code_description set description_en = 'gas tanker' where domain = 'VESSEL' and code = '82';
update code_description set description_en = 'chemical tanker' where domain = 'VESSEL' and code = '83';
update code_description set description_en = 'other vessel' where domain = 'VESSEL' and code = '90';
update code_description set description_en = 'tug' where domain = 'VESSEL' and code = '91';
update code_description set description_en = 'barge' where domain = 'VESSEL' and code = '93';
update code_description set description_en = 'pusher' where domain = 'VESSEL' and code = '94';
update code_description set description_en = 'pusher barge' where domain = 'VESSEL' and code = '95';
update code_description set description_en = 'tanker barge' where domain = 'VESSEL' and code = '96';
update code_description set description_en = 'other non propulsory vessel' where domain = 'VESSEL' and code = '97';
update code_description set description_en = 'other vessel not specified' where domain = 'VESSEL' and code = '99';

update code_description set description_en = 'general cargo' where domain = 'CARGO' and code = '301';
update code_description set description_en = 'containers' where domain = 'CARGO' and code = '302';
update code_description set description_en = 'paper, pulp' where domain = 'CARGO' and code = '303';
update code_description set description_en = 'sawn wood' where domain = 'CARGO' and code = '304';
update code_description set description_en = 'metals' where domain = 'CARGO' and code = '305';
update code_description set description_en = 'timber' where domain = 'CARGO' and code = '306';
update code_description set description_en = 'bulk' where domain = 'CARGO' and code = '307';
update code_description set description_en = 'oil' where domain = 'CARGO' and code = '308';
update code_description set description_en = 'chemicals' where domain = 'CARGO' and code = '309';
update code_description set description_en = 'other cargo' where domain = 'CARGO' and code = '310';

alter table code_description alter column description_en SET NOT NULL;
