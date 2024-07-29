/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Lymytz Dowes
 */
public class IEntityReader extends DefaultHandler {

    String name;
    boolean isClass = false;
    List<Class> values;

    public IEntityReader() {
    }

    public IEntityReader(String name) {
        this.name = name;
    }

    public List<Class> getValues() {
        return values;
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
        values = new ArrayList<>();
    }

    @Override
    public void endDocument() throws SAXException {
//        System.out.println("VALUES SIZE : " + values.size());
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("class")) {
            isClass = false;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("class".equals(localName)) {
            if (name != null) {
                String value = attributes.getValue(0);
                if (!name.equals(value)) {
                    return;
                }
            }
            isClass = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub
        String value = new String(ch, start, length);
        if (isClass) {
            try {
                values.add(Class.forName(value));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(IEntityReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
