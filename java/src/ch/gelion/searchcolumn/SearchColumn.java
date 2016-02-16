package ch.gelion.searchcolumn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import oracle.jdbc.pool.OracleDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
        
/*
 * @author gP
 *
 */
public class SearchColumn {

    private final Logger log;

    private String sDB;
    private OracleDataSource ds;
    private SQLServerDataSource sds;
    private Connection con;
    
    /**
     * Constructor
     *
     */
    public SearchColumn() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * connect to database
     *
     * @throws java.sql.SQLException
     */
    protected void openDatabase() throws SQLException {
        log.debug("log in to database...");    
        sDB = Config.sDB;
        switch(sDB.toUpperCase()) {
            case "ORACLE":
                ds = new OracleDataSource();
                ds.setDriverType(Config.sDBServerType);
                ds.setServerName(Config.sDBServerName);
                ds.setPortNumber(Integer.parseInt(Config.sDBPortNumber));
                ds.setDatabaseName(Config.sDatabase);
                ds.setUser(Config.sDBUser);
                ds.setPassword(Config.sDBPassword);
                con = ds.getConnection();
                break;
                
            case "MSSQL":
                sds = new SQLServerDataSource();
                //sds.setDriverType(Config.sDBServerType); // TODO: how to set driver type?
                sds.setServerName(Config.sDBServerName);
                sds.setPortNumber(Integer.parseInt(Config.sDBPortNumber));
                sds.setDatabaseName(Config.sDatabase);
                Boolean bWindowsAuthentication = Boolean.valueOf(Config.sWindowsAuthentication);
                if (bWindowsAuthentication) {
                    sds.setIntegratedSecurity(true);
                }
                else {
                    sds.setUser(Config.sDBUser);
                    sds.setPassword(Config.sDBPassword);
                }
                con = sds.getConnection();
                break;
            
            default:
                log.error("database {} not yet implemented", sDB);
                System.exit(1);
        }
        
        log.debug("connected to database {}", Config.sDatabase);
        // Getting database info
        DatabaseMetaData meta = con.getMetaData();
        log.debug("Server name: {}", meta.getDatabaseProductName());
        log.debug("Server version: {}", meta.getDatabaseProductVersion());
        switch(sDB.toUpperCase()) {
            case "ORACLE":
                log.debug("Connection URL: {}", ds.getURL());
                break;
            case "MSSQL":
                log.debug("Connection URL: {}", sds.getURL());
                break;
        }
        log.debug("log in to database successful");
    }

    protected void closeDatabase() throws SQLException {
        con.close();      
    }

    /**
     * @param sSearch
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    protected void doIt(String sSearch) throws SQLException, ParseException {
        String sTable, sColumn;
        String sAllColumnsSQL, sColumnValueSQL;
        
        sAllColumnsSQL = Config.sColumnSQL;
        
        log.debug("search for columns containing text {}", sSearch);
        
        log.debug("execute SQL statement {}", sAllColumnsSQL);
        // execute SQL stmt that iterates over all columns
        try (Statement stmtAC = con.createStatement(); ResultSet rsAC = stmtAC.executeQuery(Config.sColumnSQL)) {    
            while (rsAC.next()) {
                sTable = rsAC.getString(1);
                sColumn = rsAC.getString(2);
                
                sColumnValueSQL = "SELECT " + sColumn + " FROM " + sTable + " WHERE " + sColumn + " like " + "'%" + sSearch + "%'";
                log.debug("executing sql statement {}", sColumnValueSQL);
                
                try (Statement stmtCV = con.createStatement(); ResultSet rsCV = stmtCV.executeQuery(sColumnValueSQL)) {    
                    while (rsCV.next()) {
                        log.info("match: table = {}, column = {}, column value = {}", sTable, sColumn, rsCV.getString(1));
                    }
                }
                catch(Exception e) {
                    log.debug("exception when looking for text {} in table {}, column {}: {}", sSearch, sTable, sColumn, e.getMessage());
                }
            }
        }
    }
}
