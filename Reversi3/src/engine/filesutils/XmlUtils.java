package engine.filesutils;

import com.sun.javaws.jnl.XMLUtils;
import engine.gamelogic.GameManager;
import engine.gamesettings.GameDescriptor;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.plaf.SeparatorUI;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.Map;

import static com.oracle.jrockit.jfr.ContentType.Bytes;

public class XmlUtils implements Serializable {
    public static final String RESOURCES_FOLDER_NAME = "resources";
    public static final String SEPARATOR = "/";
    public static final String FILE_NAME = "ex1-small-regular.xml";
    public static final String SCHEMA_NAME = "Reversi.xsd";
    private File xmlFile;
    String xmlDescreption;
    private static InputStream schemaFile;
    private Element head;
    private Map<String, String> parsedXml;


    public GameDescriptor getGameSettingsObjectFromFile(String xmlDescreption) throws Exception {
          schemaFile = XmlUtils.class.getResourceAsStream(SEPARATOR + RESOURCES_FOLDER_NAME + SEPARATOR + SCHEMA_NAME);
//        if (xmlPath.length() > 4) {
//            String checkFileExtension = xmlPath.substring(xmlPath.length() - 4);
//            if (!checkFileExtension.toLowerCase().equals(".xml")) {
//                xmlPath += ".xml";
//            }
//        }
        this.xmlDescreption = xmlDescreption;
        xmlFile = new File(xmlDescreption);
       // validPaths();

        GameDescriptor gameSettings = null;
        validXML();
        try {
            JAXBContext jax = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller jUnmarsh = jax.createUnmarshaller();
            gameSettings = (GameDescriptor) jUnmarsh.unmarshal(new StreamSource(new StringReader(xmlDescreption)));
            if( gameSettings.getPlayers()!=null) {
                if (gameSettings.getGame().getInitialPositions().getParticipant().size() < gameSettings.getPlayers().getPlayer().size()) {
                    throw new Exception("You haven't entered at least one Starting Position for each player");
                }
            }
            else if(gameSettings.getDynamicPlayers()!=null) {
                if (gameSettings.getGame().getInitialPositions().getParticipant().size() < gameSettings.getDynamicPlayers().getTotalPlayers()) {
                    throw new Exception("You haven't entered at least one Starting Position for each player");
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return gameSettings;
    }

    private void validPaths() throws Exception {
        try {
            if (!xmlFile.exists()) {
                throw new Exception("No XML was found at: \n" + xmlFile.getPath());
            }
        } catch (Exception e) {
            throw new Exception("No XML file was found");
        }

    }


    private void validXML() throws Exception {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = schemaFactory.newSchema(new StreamSource(schemaFile));
        } catch (SAXException e) {
            throw new Exception("There is no schema file located at :\n" + XMLUtils.class.getResource(SEPARATOR + RESOURCES_FOLDER_NAME + SEPARATOR + SCHEMA_NAME).getPath(), e);
        }
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(new StringReader(xmlDescreption)));
        } catch (SAXException e) {
            String formatedException = setFriendlyMessage(e);
            throw new Exception("The XML does not meet with the schema definition\n" + formatedException);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String setFriendlyMessage(Exception e) {
        String message = e.getMessage();
        String outPut = null;
        String[] arrMessage = message.split("'");
        outPut = "You entered invalid value in the xml line " + ((SAXParseException) e).getLineNumber() + System.lineSeparator();
        if (message.contains("maxInclusive") || message.contains("maxExclusive")) {
            if (message.contains("rowsBoard")) {
                outPut += "The value for board's rows is higher than the limit, enter lower number";
            } else {
                outPut += "The value for board's columns is higher than the limit, enter lower number";
            }
        } else if (message.contains("minInclusive")) {
            if (message.contains("rowsBoard")) {
                outPut += "The value for board's rows is lower than the limit, enter higher number";
            } else {
                outPut += "The value for board's columns is lower than the limit, enter higher number";
            }
        } else if (message.contains("enumeration")) {
            outPut += "The value you entered is \"" + arrMessage[1] + "\" instead of one of the following: " + arrMessage[3];

        } else if (message.contains("not complete")) {
            outPut += "The element \"" + arrMessage[1] + "\" is missing the value: " + arrMessage[3];
        }
        return outPut;
    }
}



