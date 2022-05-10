CREATE INDEX IF NOT EXISTS sse_report_created_i
  ON sse_report
    USING BTREE (created ASC);