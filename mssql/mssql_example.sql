--create table for testing:
 CREATE TABLE [dbo].[MY_TABLE]
(
  MY_KEY                 INT          NOT NULL,
  MY_DATE                DATE,
  DESCRIPTION            NVARCHAR(400)
);

--insert some values into table:
INSERT INTO MY_TABLE VALUES(1, '20160229', 'your future will be bright and sunny, if you always use a dummy');
INSERT INTO MY_TABLE VALUES(2, '20151231', 'one of these days');
INSERT INTO MY_TABLE VALUES(3, '20151231', 'freedom''s just another word for ...');

DECLARE
@tablename nvarchar(400),
@columnname nvarchar(400),
@searchstring nvarchar(400),
@SQL nvarchar(400),
@result nvarchar(400),
@RowCount int

SET @tablename = 'MY_TABLE'
SET @searchstring = '2'

DECLARE ColCur CURSOR FAST_FORWARD READ_ONLY
 FOR
	select t.name, c.name
	from sys.tables t
	inner join sys.columns c
    on c.object_id = t.object_id 
	where 1=1
	and t.name=@tablename -- comment this out if you want to look for all tables
	order by t.name, c.column_ID

	OPEN ColCur

	FETCH NEXT FROM ColCur INTO @tablename, @columnname

	WHILE @@FETCH_STATUS = 0
    BEGIN
		
		PRINT 'tablename=' + @tablename + ', columnname=' + @columnname

		SET @SQL = 'SELECT * FROM ' + @tablename + ' WHERE ' + @columnname + ' = ' + @searchstring
		EXEC(@SQL)

		Set @RowCount = @@RowCount
		If @Rowcount > 0
			Print 'Match: table = ' + @tablename + ', column = ' + @columnname + ', searchstring = ' + @searchstring

		FETCH NEXT FROM ColCur INTO @tablename, @columnname
    END
 CLOSE ColCur
 DEALLOCATE ColCur

 DROP TABLE [dbo].[MY_TABLE]

/*
 expected test results (note the error messages because of incompatible data types)
    
    search string '2'
    =>
    tablename=MY_TABLE, columnname=MY_KEY
    Match: table = MY_TABLE, column = MY_KEY, searchstring = 2
		tablename=MY_TABLE, columnname=MY_DATE
    Operand type clash: date is incompatible with tinyint
    Match: table = MY_TABLE, column = MY_DATE, searchstring = 2
		tablename=MY_TABLE, columnname=DESCRIPTION
		Conversion failed when converting the nvarchar value 'your future will be bright and sunny, if you always use a dummy' to data type int.
*/