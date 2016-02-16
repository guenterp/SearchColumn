
search text in database columns

tested with following libraries:
ojdbc6.jar
slf4j-log4j12-1.7.5.jar
slf4j-api-1.7.5.jar
log4j-1.2.17.jar
sqljdbc41.jar

java >= 1.7

for access via Windows Authentication an additional library (dll file) has to be added.
details can be found in mssql/WindowsAuthentication_README.txt.

whole database or some tables can be searched
display columns that match
search is not aware of datatypes, so the search is not looking for dates or numbers but only for strings (text)
if datatypes do not match an exception can be raised. this exception is logged in DEBUG mode but ignored otherwise and the next column is tested.

example how to do in Java in java/src/... subdirectory.
search text is the first and only argument (args[0])
in config file java/Config.xml it can be specified which database and which tables to search.
logging is done with slf4j/log4j. see also java/log4j.xml
log file example can be found in directory java/log.

implemented for Oracle and MSSQL
TODO: implement for MYSQL

example how to search in Oracle with PL/SQL:
see oracle/oracle_example.txt

example how to search in MSSQL with T-SQL:
see mssql/mssql_example.txt
