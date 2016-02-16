
package ch.gelion.searchcolumn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * Reads the settings from the XML configuration file
 * @author gP
 *
 */
public final class Config {
    private static Logger log;
    protected static String sSystem, sWindowsAuthentication, sSearchText, sColumnSQL;
    protected static String sConfigFile = "Config.xml";
    protected static String sDB, sClassForName, sURL, sDatabase, sDBUser, sDBPassword;
    protected static String sDBServerType, sDBServerName, sDBPortNumber, sDBDriver;
    protected static String sExpression, sEval;
    
    private Config() {
    }

    /**
     *
     * initialize with values set in Config.xml file
     *
     */
    public static void initialize() {
        try {
            log = LoggerFactory.getLogger(Config.class);
            
            NodeList nodeList;

            FileInputStream file = new FileInputStream(new File("Config.xml"));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(file);
            XPath xPath = XPathFactory.newInstance().newXPath();

            sExpression = "//global//*";
            sEval = xPath.compile(sExpression).evaluate(xmlDocument);

            nodeList = (NodeList) xPath.compile(sExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                String sNodeName = nodeList.item(i).getNodeName();
                String sNodeValue = "";
                if (nodeList.item(i).getFirstChild() != null) {
                    sNodeValue = nodeList.item(i).getFirstChild().getNodeValue();
                }

                switch (sNodeName) {
                    case "db":
                        sDB = sNodeValue;
                        log.debug("db = {}", sDB);
                        break;
    
                     case "system":
                        sSystem = sNodeValue;
                        log.debug("sSystem = {}", sSystem);
                        break;
                        
                     case "windowsauthentication":
                        sWindowsAuthentication = sNodeValue;
                        log.debug("sWindowsAuthentication = {}", sWindowsAuthentication);
                        break;
                        
                    default:
                        log.error("wrong node name = {}", sNodeName);
                        break;
                }
            }

            //sExpression = "//db//*";
            sExpression = "//db[@name='" + sDB + "']/*";
            sEval = xPath.compile(sExpression).evaluate(xmlDocument);

            nodeList = (NodeList) xPath.compile(sExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                String sNodeName = nodeList.item(i).getNodeName();
                String sNodeValue = "";
                if (nodeList.item(i).getFirstChild() != null) {
                    sNodeValue = nodeList.item(i).getFirstChild().getNodeValue();
                }

                switch (sNodeName) {
                    case "columnsql":
                        sColumnSQL = sNodeValue;
                        log.debug("columnsql = {}", sColumnSQL);
                        break;
        
                    default:
                        log.error("wrong node name = {}", sNodeName);
                        break;
                }
            }
            
            sExpression = "//system[@name='" + sSystem + "']/*";
            sEval = xPath.compile(sExpression).evaluate(xmlDocument);

            nodeList = (NodeList) xPath.compile(sExpression).evaluate(xmlDocument, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                String sNodeName = nodeList.item(i).getNodeName();
                String sNodeValue = nodeList.item(i).getFirstChild().getNodeValue();

                switch (sNodeName) {
                    case "database":
                        sDatabase = sNodeValue;
                        log.debug("database = {}", sDatabase);
                        break;

                    case "dbservertype":
                        sDBServerType = sNodeValue;
                        log.debug("dbservertype = {}", sDBServerType);
                        break;

                    case "dbservername":
                        sDBServerName = sNodeValue;
                        log.debug("dbservername = {}", sDBServerName);
                        break;

                    case "dbportNumber":
                        sDBPortNumber = sNodeValue;
                        log.debug("dbportNumber = {}", sDBPortNumber);
                        break;

                    case "dbuser":
                        sDBUser = sNodeValue;
                        log.debug("dbuser = {}", sDBUser);
                        break;

                    case "dbpassword":
                        sDBPassword = sNodeValue;
                        log.debug("dbservername = {}", sDBPassword);
                        break;

                    case "dbdriver":
                        sDBDriver = sNodeValue;
                        log.debug("dbdriver = {}", sDBDriver);
                        break;

                    default:
                        log.error("wrong node name = {}", sNodeName);
                        break;
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            log.error(e.getMessage());
            log.error("aborting");
            System.exit(1);
        }
    }
}
