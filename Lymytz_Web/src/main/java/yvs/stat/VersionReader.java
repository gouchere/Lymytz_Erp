/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.w3c.dom.DOMException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import static yvs.init.Initialisation.FILE_SEPARATOR;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
public class VersionReader extends DefaultHandler {

    private String header;
    private String content;
    private Version.Update update = new Version.Update();
    private Version version = new Version();

    boolean isVersion = false;
    boolean isUpdates = false;
    boolean isUpdate = false;
    boolean isHeader = false;
    boolean isContent = false;

    public VersionReader() {

    }

    public Version create() {
        try {
            String file = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF") + FILE_SEPARATOR + "version.xml";
            if (new File(file).exists()) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(file, this);
            }
        } catch (DOMException | ParserConfigurationException | TransformerFactoryConfigurationError | SAXException | IOException ex) {
            Logger.getLogger(VersionReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }

    public void parse(InputStream input) throws ParserConfigurationException, SAXException, IOException {
        // creation du parser SAX
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        // lancement du parsing en précisant le flux et le "handler"
        // l'instance qui implémente les méthodes appelées par le parser
        // dans notre cas: this
        parser.parse(input, this);
    }

    public void parse(String filename) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        parse(new FileInputStream(filename));
    }

    @Override
    public void startDocument() throws SAXException {
        version = new Version();
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("VERSION : " + version.getName());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "version":
                isVersion = true;
                break;
            case "updates":
                isUpdates = true;
                break;
            case "update":
                update = new Version.Update();
                isUpdate = true;
                break;
            case "header":
                isHeader = true;
                break;
            case "content":
                isContent = true;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case "version":
                isVersion = false;
                break;
            case "updates":
                isUpdates = false;
                break;
            case "update":
                if (update != null ? Util.asString(update.getHeader()) : false) {
                    version.getUpdates().add(update);
                }
                isUpdate = false;
                break;
            case "header":
                update.setHeader(header);
                isHeader = false;
                break;
            case "content":
                update.setContent(content);
                isContent = false;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub
        String value = new String(ch, start, length);
        if (isVersion) {
            version.setName(value);
        } else if (isHeader) {
            header = value;
        } else if (isContent) {
            content = value;
        }
    }
}
