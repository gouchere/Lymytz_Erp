/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
public class JsonString {

    DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    DateFormat formatDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    DateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
    List<Element> elements = new ArrayList<>();
    String new_line = "";
    Object object;

    public JsonString(Object object) {
        this.object = object;
        this.elements = new ArrayList<>();
    }

    public String value() {
        return getJsonStringObject(object);
    }

    private String getJsonStringObject(Object object) {
        String result = null;
        try {
            if (object != null) {
                result = "{";
                for (Field field : object.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        field.setAccessible(true);
                        String name = field.getName();
                        Element sous = new Element(field.getType().getSimpleName(), object.getClass().getSimpleName());
                        String value = null;
                        int index = elements.indexOf(sous);
                        if (index < 0) {
                            elements.add(sous);
                            Object get = new PropertyDescriptor(name, object.getClass()).getReadMethod().invoke(object);
                            if (get != null) {
                                value = getJsonStringObject(get);
                            }
                            sous.value = value;
                            index = elements.indexOf(sous);
                            if (index > -1) {
                                elements.get(index).value = value;
                            }
                        } else {
                            value = elements.get(index).value;
                        }
                        if (Util.asString(value)) {
                            if (!result.equals("{")) {
                                result += ",";
                            }
                            result += new_line + "\"" + name + "\":" + value;
                        }
                    } else if (field.isAnnotationPresent(Column.class)) {
                        field.setAccessible(true);
                        String name = field.getName();
                        Object get = new PropertyDescriptor(name, object.getClass()).getReadMethod().invoke(object);
                        String value = null;
                        if (get != null) {
                            switch (field.getType().getSimpleName()) {
                                case "Date":
                                    value = "\"" + formatDateTime.format(get) + "\"";
                                    break;
                                case "String":
                                    value = "\"" + get.toString() + "\"";
                                    break;
                                case "Character":
                                    value = "'" + get.toString() + "'";
                                    break;
                                default:
                                    value = get.toString();
                                    break;
                            }
                        }
                        if (Util.asString(value)) {
                            if (!result.equals("{")) {
                                result += ",";
                            }
                            result += new_line + "\"" + name + "\":" + value;
                        }
                        elements.add(new Element(name, object.getClass().getSimpleName()));
                    }
                }
                result += "}";
            }
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | IntrospectionException | InvocationTargetException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    static class Element {

        public String name;
        public String value;
        public String parent;

        public Element(String name) {
            this.name = name;
        }

        public Element(String name, String parent) {
            this(name);
            this.parent = parent;
        }

        public Element(String name, String parent, String value) {
            this(name, parent);
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + Objects.hashCode(this.name);
            hash = 89 * hash + Objects.hashCode(this.parent);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Element other = (Element) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.parent, other.parent)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "Element{" + "name=" + name + ", parent=" + parent + '}';
        }

    }
}
