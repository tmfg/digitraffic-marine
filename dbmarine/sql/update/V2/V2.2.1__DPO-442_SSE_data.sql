CREATE TABLE SSE_CONFIDENCE
(
  CONFIDENCE TEXT PRIMARY KEY
);

INSERT INTO SSE_CONFIDENCE(CONFIDENCE) VALUES ('POOR');
INSERT INTO SSE_CONFIDENCE(CONFIDENCE) VALUES ('MODERATE');
INSERT INTO SSE_CONFIDENCE(CONFIDENCE) VALUES ('GOOD');


CREATE TABLE SSE_SEA_STATE
(
  STATE TEXT PRIMARY KEY
);

INSERT INTO SSE_SEA_STATE(STATE) VALUES ('CALM');
INSERT INTO SSE_SEA_STATE(STATE) VALUES ('LIGHT');
INSERT INTO SSE_SEA_STATE(STATE) VALUES ('MODERATE');
INSERT INTO SSE_SEA_STATE(STATE) VALUES ('BREEZE');
INSERT INTO SSE_SEA_STATE(STATE) VALUES ('GALE');
INSERT INTO SSE_SEA_STATE(STATE) VALUES ('STORM');


CREATE TABLE SSE_TREND
(
  TREND TEXT PRIMARY KEY
);

INSERT INTO SSE_TREND(TREND) VALUES ('UNKNOWN');
INSERT INTO SSE_TREND(TREND) VALUES ('NO_CHANGE');
INSERT INTO SSE_TREND(TREND) VALUES ('ASCENDING');
INSERT INTO SSE_TREND(TREND) VALUES ('DESCENDING');


CREATE TABLE SSE_LIGHT_STATUS
(
  STATUS TEXT PRIMARY KEY
);

INSERT INTO SSE_LIGHT_STATUS(STATUS) VALUES ('ON');
INSERT INTO SSE_LIGHT_STATUS(STATUS) VALUES ('OFF');
INSERT INTO SSE_LIGHT_STATUS(STATUS) VALUES ('ON_D');


CREATE SEQUENCE SEQ_SSE_REPORT;
CREATE TABLE SSE_REPORT
(
  SSE_REPORT_ID                   BIGINT PRIMARY KEY,
  CREATED                         TIMESTAMP(0) WITH TIME ZONE,
  LATEST                          BOOLEAN NOT NULL,
  -- SSE data
  SITE_NUMBER                     INTEGER NOT NULL,
  SITE_NAME                       TEXT NOT NULL,
  LAST_UPDATE                     TIMESTAMP(0) WITH TIME ZONE,
  SEA_STATE                       TEXT REFERENCES SSE_SEA_STATE(STATE),
  TREND                           TEXT REFERENCES SSE_TREND(TREND),
  WIND_WAVE_DIR                   INTEGER,
  CONFIDENCE                      TEXT REFERENCES SSE_CONFIDENCE(CONFIDENCE),
  -- Extra fields / metadata
  HEEL_ANGLE                      NUMERIC(4,1),
  LIGHT_STATUS                    TEXT REFERENCES SSE_LIGHT_STATUS(STATUS),
  TEMPERATURE                     INTEGER,
  LONGITUDE                       NUMERIC(10,7) NOT NULL,
  LATITUDE                        NUMERIC(10,7) NOT NULL
);

CREATE UNIQUE INDEX SSE_REPORT_SITE_NUMBER_LATEST_U_I ON SSE_REPORT (site_number, latest) WHERE (latest IS TRUE);
COMMENT ON INDEX SSE_REPORT_SITE_NUMBER_LATEST_U_I is 'Only one per site can be latest report';

CREATE INDEX SSE_REPORT_SITE_NUMBER_PARTIAL_LATEST_I ON SSE_REPORT (site_number) WHERE latest = TRUE;
COMMENT ON INDEX SSE_REPORT_SITE_NUMBER_PARTIAL_LATEST_I is 'Used for updating latest flag and finding latest rows';

CREATE INDEX SSE_TLSC_REPORT_ID_PARTIAL_HANDLED_NULL_I ON SSE_TLSC_REPORT (ID) WHERE (handled IS NULL );
COMMENT ON INDEX SSE_TLSC_REPORT_ID_PARTIAL_HANDLED_NULL_I is 'Used for finding non handled SSE_TLSC_REPORTs';

-- Foreign key indexes
CREATE INDEX SSE_REPORT_TREND_FKEY_I ON SSE_REPORT USING BTREE (trend);
CREATE INDEX SSE_REPORT_CONFIDENCE_FKEY_I ON SSE_REPORT USING BTREE (confidence);
CREATE INDEX SSE_REPORT_LIGHT_STATUS_FKEY_I ON SSE_REPORT USING BTREE (light_status);
CREATE INDEX SSE_REPORT_SEA_STATE_FKEY_I ON SSE_REPORT USING BTREE (sea_state);