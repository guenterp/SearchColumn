package ch.gelion.searchcolumn;

import java.sql.SQLException;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gP
 */
public class Main {

    private Logger log;
    private SearchColumn searchcolumn;
    private final String sSearch;

    public Main(String[] args) {
        this.sSearch = args[0];
    }

    protected void doIt() throws SQLException, ParseException {
        this.log = LoggerFactory.getLogger(this.getClass());
        
        log.info("application started successfully");       
        log.debug("search text: {}", sSearch);

        Config.initialize();
        
        searchcolumn = new SearchColumn();
        searchcolumn.openDatabase();
        searchcolumn.doIt(sSearch);
        searchcolumn.closeDatabase();

        log.info("application terminated successfully");
    }

    public static void main(String[] args) throws SQLException, ParseException {
        Main m = new Main(args);
        m.doIt();        
    }
}
