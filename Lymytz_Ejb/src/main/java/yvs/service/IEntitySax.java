/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.service.base.produit.AYvsBaseArticles;

/**
 *
 * @author Lymytz Dowes
 */
public class IEntitySax implements Serializable{

    File file = new File(Constantes.CLASSLOADER.getParent(), "META-INF" + Constantes.FILE_SEPARATOR + "IEntity.xml");

    public IEntitySax() {
    }

    public IEntitySax(File file) {
        this.file = file;
    }

    public List<Class> getValues() {
        try {
            IEntityReader reader = new IEntityReader();
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(reader);
            xr.parse(new InputSource(new FileInputStream(file)));
            return reader.getValues();
        } catch (SAXException | IOException ex) {
            Logger.getLogger(IEntitySax.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Class getValue(String name) {
        try {
            IEntityReader reader = new IEntityReader(name);
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(reader);
            xr.parse(new InputSource(new FileInputStream(file)));
            return !reader.getValues().isEmpty() ? reader.getValues().get(0) : null;
        } catch (SAXException | IOException ex) {
            Logger.getLogger(IEntitySax.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object createInstance(String name, DaoInterfaceLocal dao) {
        try {
            Class CLass = getValue(name);
            if (CLass != null) {
                Object instance = CLass.newInstance();
                try {
                    Method setDao = CLass.getMethod("setDao", DaoInterfaceLocal.class);
                    try {
                        setDao.invoke(instance, new Object[]{dao});
                    } catch (IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(IEntitySax.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(IEntitySax.class.getName()).log(Level.SEVERE, null, ex);
                }
                return instance;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(AYvsBaseArticles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
