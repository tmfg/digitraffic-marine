create table wn_location (
  id			    text primary key,
  name		    text not null,
  type		    text not null,
  locode_list	text,
  nationality	text not null,
  latitude	  numeric(5, 2),
  longitude 	numeric(5, 2),
  winterport 	boolean not null,
  deleted 	  boolean not null,
  created     timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_restriction (
  id					      text primary key,
  location_id			  text not null,
  text_compilation	text not null,
  start_time			  timestamp with time zone not null,
  end_time			    timestamp with time zone,
  deleted 			    boolean not null,
  created     		  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_vessel (
  id					  text primary key,
  name				  text not null,
  callsign			text,
  shortcode			text,
  imo					  numeric,
  mmsi				  numeric,
  --mcr?
  --csr?
  --iceclass?
  type				  text,
  deleted				boolean not null,
  created 	  	timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_activity (
  id				      text primary key,
  icebreaker_id	  text not null,
  vessel_id		    text,
  type			      text not null,
  reason			    text,
  public_comment	text,
  start_time		  timestamp with time zone not null,
  end_time		    timestamp with time zone,
  deleted 		    boolean not null,
  created		  	  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_source (
  id				  text primary key,
  name			  text not null,
  shortname		text,
  nationality	text not null,
  type			  text not null,
  vessel_id		text,
  deleted 		boolean not null,
  created		  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_port_suspension (
  id				      text primary key,
  start_time		  timestamp with time zone not null,
  end_time		    timestamp with time zone,
  prenotification	text not null,
  ports_closed	  boolean not null,
  due_to			    text not null,
  specifications	text,
  deleted 		    boolean not null,
  created		  	  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_port_suspension_location (
  id				    text primary key,
  suspension_id	text not null,
  location_id		text not null,
  deleted 		  boolean not null,
  created		  	timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_queue (
  id				    text primary key,
  icebreaker_id	text not null,
  vessel_id		  text not null,
  start_time		timestamp with time zone not null,
  end_time		  timestamp with time zone,
  order_num		  numeric not null,
  deleted 		  boolean not null,
  created		  	timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_dirway (
  id				  text primary key,
  name			  text not null,
  description	text not null,
  deleted 		boolean not null,
  created		  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_dirwaypoint (
  id				  text primary key,
  dirway_id		text not null,
  order_num		numeric not null,
  name			  text not null,
  latitude		numeric not null,
  longitude		numeric not null,
  deleted 		boolean not null,
  created		  timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);

create table wn_data_version (
  table_name	text primary key,
  version     numeric not null,
  modified    timestamp with time zone not null DEFAULT CURRENT_TIMESTAMP
);