
--create table for testing:
CREATE TABLE MY_TABLE
(
  MY_KEY                NUMBER          NOT NULL,
  MY_DATE                DATE,
  DESCRIPTION        VARCHAR2(100),
  PICTURE                BLOB
);

--insert some values into table:
INSERT INTO MY_TABLE VALUES(1, TO_DATE('20160229', 'YYYYMMDD'), 'your future will be bright and sunny, if you always use a dummy', '');
INSERT INTO MY_TABLE VALUES(2, TO_DATE('20151231', 'YYYYMMDD'), 'one of these days', '');
INSERT INTO MY_TABLE VALUES(3, TO_DATE('20151231', 'YYYYMMDD'), 'freedom''s just another word for ...', '');

commit;

DECLARE
   v_search        VARCHAR2 (400) := ' LIKE ''%sunny%''';
   --v_search        VARCHAR2 (400) := ' LIKE ''%2015%''';
   --v_search        VARCHAR2 (400) := ' LIKE ''%Feb%''';
   --v_search        VARCHAR2 (400) := ' LIKE ''%3%''';
   
   v_result        VARCHAR2 (400);

--
   /* first cursor gets column names */
   CURSOR col_cur
   IS
      SELECT table_name, column_name
        FROM all_tab_cols
       WHERE     table_name = 'MY_TABLE' -- replace 'MY_TABLE' by 1=1 if you want to search all tables
             AND data_type NOT IN ('BLOB', 'CLOB', 'LONG'); -- probably some more data types need to be excluded

   col             col_cur%ROWTYPE;

   /* second cursor gets values */
   val_cur         SYS_REFCURSOR;
 
BEGIN
   OPEN col_cur;
   LOOP
      FETCH col_cur INTO col;
      EXIT WHEN col_cur%NOTFOUND;

      OPEN val_cur FOR
            'SELECT '
         || col.column_name
         || ' FROM '
         || col.table_name
         || ' WHERE '
         || col.column_name
         || v_search;

      LOOP
         FETCH val_cur INTO v_result;
         EXIT WHEN val_cur%NOTFOUND;
         DBMS_OUTPUT.put_line (
               'table: '
            || col.table_name
            || ', column: '
            || col.column_name
            || ',  value: '
            || v_result);
      END LOOP;

      CLOSE val_cur;
   END LOOP;

   CLOSE col_cur;
END;

-- drop testing table
DROP table MY_TABLE;


/*
    expected test results:
    
    1. search string '%sunny%'
    =>
    table: MY_TABLE, column: DESCRIPTION,  value: your future will be bright and sunny, if you always use a dummy
    
    2. search string '%2015%'
    =>
    table: MY_TABLE, column: MY_DATE,  value: 31-DEC-2015
    table: MY_TABLE, column: MY_DATE,  value: 31-DEC-2015
    
    3. search string '%Feb%'
    =>
    
    4. search string '%3%'
    =>
    table: MY_TABLE, column: MY_DATE,  value: 31-DEC-2015
    table: MY_TABLE, column: MY_DATE,  value: 31-DEC-2015
    table: MY_TABLE, column: MY_KEY,  value: 3
*/

-- another example in PL/SQL; example is easier than above but finds only the number of columns 
-- e.g. MY_TABLE DESCRIPTION 1 (string searched occurs once in column DESCRIPTION, table MY_TABLE)
/*
DECLARE
   match_count       INTEGER;
   
   v_search_string   VARCHAR2 (4000) := '%sunny%';
BEGIN

   FOR t
      IN (SELECT table_name, column_name
            FROM all_tab_cols
           WHERE table_name = 'MY_TABLE' AND data_type not in ('BLOB', 'CLOB', 'LONG'))
   LOOP

      EXECUTE IMMEDIATE
            'SELECT COUNT(*) FROM '
         || t.table_name
         || ' WHERE '
         || t.column_name
         || ' like :1'
         INTO match_count
         USING v_search_string;

      IF match_count > 0
      THEN
         DBMS_OUTPUT.put_line (
            t.table_name || ' ' || t.column_name || ' ' || match_count);
      END IF;

   END LOOP;

END;
*/
