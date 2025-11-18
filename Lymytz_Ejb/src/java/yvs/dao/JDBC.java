/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.sql.DataSource;
import static yvs.dao.JDBC.inner;
import yvs.entity.tiers.YvsBaseTiers;

/**
 *
 * @author Lymytz Dowes
 * @param <T> toute classe implémentant Zerializable et portant l'annotation
 * Entity
 */
public class JDBC<T extends Serializable> implements Serializable {

    List<Tables> tables = new ArrayList<>();
    List<Tables> classes = new ArrayList<>();
    T entity;
    Connection connect;

    public JDBC(T entity, Connection connect) {
        this.entity = entity;
        this.connect = connect;
    }

    public JDBC(T entity, DataSource connect) throws SQLException {
        this(entity, connect.getConnection());
    }

    public Connection getConnect() {
        return connect;
    }

    public static String select(Class classe) {
        String query = "SELECT " + colonnes(classe);
        String inner = "";
        String table = table(classe);
        for (Field field : classe.getDeclaredFields()) {
            if (contrainte(field)) {
                String foreignTable = table(field.getType());
                if (!foreignTable.equals(table) ? (!inner.contains(table(field.getType()))) : false) {
                    query += ", " + colonnes(field.getType());
                    inner += inner(classe, field.getType(), field.getName());
                }
            }
        }
        return query + " FROM " + table + " " + table + inner;
    }

    public static String count(Class classe, String select) {
        String count = "SELECT COUNT(*)";
        String from = "FROM ";
        String column = "*";
        if (classe != null) {
            String table = table(classe);
            if (table != null ? table.trim().length() > 0 : false) {
                from += table;
                Field id = key(classe);
                if (id != null) {
                    column = table + "." + colonne(id);
                    if (select != null && !select.isEmpty()) {
                        int idx = select.indexOf("FROM");
                        from = select.substring(idx, select.length() - 1);
                    }
                }
            }
        }
        return count.replace("*", column) + " " + from;
    }

    public static String colonnes(Class classe) {
        return colonnes(classe, 0);
    }

    public static String colonnes(Class classe, int index) {
        String colonne = "";
        String table = table(classe);
        for (Field field : classe.getDeclaredFields()) {
            String column = colonne(field);
            if (column != null ? column.trim().length() > 0 : false) {
                column += index > 0 ? ("_" + index) : "";
                if (colonne != null ? colonne.trim().length() < 1 : true) {
                    colonne = table + "." + column + " AS " + table + "_" + column;
                } else {
                    colonne += ", " + table + "." + column + " AS " + table + "_" + column;
                }
            }
        }
        return colonne;
    }

    public static boolean iskey(Field field) {
        if (!field.getName().equals("serialVersionUID")) {
            Annotation a = field.getAnnotation(Id.class);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    public static Field key(Class classe) {
        for (Field field : classe.getDeclaredFields()) {
            if (!field.getName().equals("serialVersionUID")) {
                for (Annotation a : field.getDeclaredAnnotations()) {
                    if (!a.annotationType().getSimpleName().equals("Transient") && a.annotationType().getSimpleName().equals("Id")) {
                        return field;
                    }
                }
            }
        }
        return null;
    }

    public static Object getValue(Field field, Object classe) {
        try {
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(classe);
                if (value instanceof java.util.Date) {
                    value = new java.sql.Date(((Date) value).getTime());
                }
                return value;
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Object getBasicValue(Field field, Object classe) {
        try {
            if (field != null) {
                Object value = getValue(field, classe);
                if (value != null && contrainte(field)) {
                    String table = table(value.getClass());
                    if (table != null ? table.trim().length() > 0 : false) {
                        Field id = key(value.getClass());
                        if (id != null) {
                            return getValue(id, value);
                        }
                    }
                } else {
                    return value;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean setValue(Field field, Object classe, Object value) {
        try {
            if (field != null) {
                field.setAccessible(true);
                field.set(classe, value);
                return true;
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.getStackTrace();
//            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static Object value(Object classe) {
        String table = table(classe.getClass());
        if (table != null ? table.trim().length() > 0 : false) {
            Field id = key(classe.getClass());
            if (id != null) {
                return getValue(id, classe);
            }
        }
        return classe;
    }

    public String table(T classe) {
        return table(classe.getClass());
    }

    /**
     * Renvoi
     *
     * @param classe: Classe portant une annotation @Table du package
     * javax.persistence.Table;
     * @return String: nom de la table donnée en paramètre à l'annotation @Table
     */
    public static String table(Class classe) {
        String table = "";
        boolean view = false;
        for (Annotation a : classe.getDeclaredAnnotations()) {
            if (a.annotationType().getSimpleName().equals("Table")) {
                try {
                    table = ((Table) a).name();
                    view = true;
                } catch (Exception ex) {
                    Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        if (!view) {
            table = classe.getSimpleName();
        }
        return table.trim();
    }

    public static String inner(Class v1, Class v2, String name) {
        String inner = "";
        String classe = v2.getSimpleName();
        for (Field field : v1.getDeclaredFields()) {
            if (contrainte(field) ? (field.getType().getSimpleName().equals(classe) ? field.getName().equals(name) : false) : false) {
                inner = " LEFT JOIN " + table(v2) + " ON " + table(v1) + "." + colonne(field) + " = " + table(v2) + "." + contrainteName(field);
            }
        }
        return inner;
    }

    public String order(T classe, String order) {
        if (classe != null) {
            return order(classe.getClass(), order);
        }
        return order;
    }

    public static String order(Class classe, String order) {
        String value = "";
        if (order != null ? order.trim().length() > 0 : false) {
            for (String champ : order.split(",")) {
                String write = champ;
                if (champ.contains(".")) {
                    String[] o = champ.trim().split(" ");
                    write = rewrite(classe, o[0]);
                    if (o.length > 1) {
                        write += " " + o[1];
                    }
                }
                if (value != null ? value.trim().length() < 1 : true) {
                    value = write;
                } else {
                    value += ", " + write;
                }
            }
        }
        return value;
    }

    public String rewrite(T classe, String attribut) {
        if (classe != null) {
            return rewrite(classe.getClass(), attribut);
        }
        return attribut;
    }

    public static String rewrite(Class classe, String attribut) {
        String column = attribut;
        if (classe != null) {
            if (attribut != null ? attribut.trim().length() > 0 : false) {
                String value = attribut.replace(".", "-");
                String contour = "";
                int idx = value.indexOf("(");
                if (idx > -1) {
                    contour = attribut.substring(0, idx + 1) + "*)";
                    value = attribut.substring(idx + 1, attribut.length() - 1).replace(".", "-");
                }
                String table = table(classe);
                if (isRelation(value)) {
                    int i = 1;
                    boolean continu = false;
                    while (!continu) {
                        table = table(classe);
                        String champ = value.split("-")[i];
                        Class relation = relation(classe, champ);
                        column = table + "." + colonne(classe, champ);
                        if (relation != null) {
                            classe = relation;
                        }
                        if (value.split("-").length <= (i + 1)) {
                            continu = true;
                            continue;
                        }
                        i++;
                    }
                } else {
                    column = "";
                    for (String champ : value.split("-")) {
                        if (column != null ? column.trim().length() < 1 : true) {
                            column = table;
                        } else {
                            column += "." + colonne(classe, champ);
                        }
                    }
                }
                if (contour != null ? contour.trim().length() > 0 : false) {
                    column = contour.replace("*", column);
                }
            }
        }
        return column;
    }

    private static boolean isRelation(String attribut) {
        if (attribut != null ? attribut.trim().length() > 0 : false) {
            String[] champs = attribut.split("-");
            return champs.length > 2;
        }
        return false;
    }

    public static Field field(Class classe, String attribut) {
        if (classe != null) {
            for (Field field : classe.getDeclaredFields()) {
                if (field.getName().equals(attribut)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static String colonne(Class classe, String attribut) {
        if (classe != null) {
            for (Field field : classe.getDeclaredFields()) {
                if (field.getName().equals(attribut)) {
                    return colonne(field);
                }
            }
        }
        return "";
    }

    private static String colonne(Field field) {
        String colonne = "";
        if (field != null) {
            for (Annotation a : field.getDeclaredAnnotations()) {
                if (!field.getName().equals("serialVersionUID")) {
                    if (!a.annotationType().getSimpleName().equals("Transient") && (a.annotationType().getSimpleName().equals("Column") || a.annotationType().getSimpleName().equals("JoinColumn"))) {
                        if (a.annotationType().getSimpleName().equals("Column")) {
                            colonne = ((Column) a).name();
                        } else {
                            colonne = ((JoinColumn) a).name();
                        }
                        if (colonne != null ? colonne.trim().length() > 0 : false) {
                            break;
                        }
                    }
                }
            }
        }
        return colonne.trim();
    }

    private static String contrainteName(Field field) {
        String colonne = "";
        for (Annotation a : field.getDeclaredAnnotations()) {
            if (!field.getName().equals("serialVersionUID")) {
                if (!a.annotationType().getSimpleName().equals("Transient") && a.annotationType().getSimpleName().equals("JoinColumn")) {
                    colonne = ((JoinColumn) a).referencedColumnName();
                    if (colonne != null ? colonne.trim().length() > 0 : false) {
                        break;
                    }
                }
            }
        }
        return colonne.trim();
    }

    private static boolean contrainte(Field field) {
        for (Annotation a : field.getDeclaredAnnotations()) {
            if (!field.getName().equals("serialVersionUID")) {
                if (!a.annotationType().getSimpleName().equals("Transient") && a.annotationType().getSimpleName().equals("ManyToOne")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Class relation(Class classe, String colonne) {
        for (Field field : classe.getDeclaredFields()) {
            if (field.getName().equals(colonne)) {
                return field.getType();
            }
        }
        return null;
    }

    private static boolean existe(ResultSet lect, String column) {
        try {
            lect.getObject(column);
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public T one(String query) {
        return one(query, new String[]{}, new Object[]{}, -1, -1);
    }

    public T one(String query, int offset, int limit) {
        return one(query, new String[]{}, new Object[]{}, offset, limit);
    }

    public T one(String query, String[] champ, Object[] val) {
        return one(query, champ, val, -1, -1);
    }

    public T one(String query, String[] champ, Object[] val, int offset, int limit) {
        try {
            if (connect != null) {
                tables.clear();
                classes.clear();
                try {
                    query = offset < 0 ? query : (query + " offset " + offset);
                    query = limit < 0 ? query : (query + " limit " + limit);
                    PreparedStatement st = connect.prepareStatement(query);
                    for (int i = 1; i < champ.length + 1; i++) {
                        st.setObject(i, value(val[i - 1]));
                    }
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        return (T) retourne(rs, entity);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entity;
    }

    public List<T> list(String query) {
        return list(query, new Object[]{}, -1, -1);
    }

    public List<T> list(String query, int offset, int limit) {
        return list(query, new Object[]{}, offset, limit);
    }

    public List<T> list(String query, Object[] params) {
        return list(query, params, -1, -1);
    }

    public List<T> list(String query, Object[] params, int offset, int limit) {
        List<T> result = new ArrayList<>();
        try {
            if (connect != null) {
                tables.clear();
                classes.clear();
                try {
                    query = offset < 0 ? query : (query + " offset " + offset);
                    query = limit < 0 ? query : (query + " limit " + limit);
                    PreparedStatement st = connect.prepareStatement(query);
                    for (int i = 1; i < params.length + 1; i++) {
                        st.setObject(i, params[i - 1]);
                    }
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        T current = (T) entity.getClass().newInstance();
                        result.add((T) retourne(rs, current));
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean insert(String schema, T entity, boolean addKey) {
        try {
            schema = (schema != null && !schema.isEmpty()) ? schema : "public";
            String colonnes = null;
            String values = null;
            List<Object> params = new ArrayList();
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (addKey || !iskey(field)) {
                    String column = colonne(field);
                    if (column != null ? column.trim().length() > 0 : false) {
                        colonnes = colonnes == null ? column : colonnes + "," + column;
                        values = values == null ? "?" : values + ",?";
                        params.add(getBasicValue(field, entity));
                    }
                }
            }
            String query = "INSERT INTO " + schema + "." + table(entity) + "(" + colonnes + ") VALUES (" + values + ")";
            try (PreparedStatement st = connect.prepareStatement(query)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject((i + 1), params.get(i));
                }
                return st.executeUpdate() > 0;
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean update(String schema, T entity) {
        try {
            schema = (schema != null && !schema.isEmpty()) ? schema : "public";
            String colonnes = null;
            Long id = null;
            List<Object> params = new ArrayList();
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!iskey(field)) {
                    String column = colonne(field);
                    if (column != null ? column.trim().length() > 0 : false) {
                        colonnes = colonnes == null ? column + "=?" : colonnes + ("," + column + "=?");
                        params.add(getBasicValue(field, entity));
                    }
                } else {
                    id = (Long) getBasicValue(field, entity);
                }
            }
            String query = "UPDATE " + schema + "." + table(entity) + " SET " + colonnes + " WHERE id = " + id;
            try (PreparedStatement st = connect.prepareStatement(query)) {
                for (int i = 0; i < params.size(); i++) {
                    st.setObject((i + 1), params.get(i));
                }
                return st.executeUpdate() > 0;
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Object object(String query) {
        return object(query, new String[]{}, new Object[]{});
    }

    public Object object(String query, String[] champ, Object[] val) {
        try {
            if (connect != null) {
                try {
                    PreparedStatement st = connect.prepareStatement(query);
                    for (int i = 1; i < champ.length + 1; i++) {
                        st.setObject(i, value(val[i - 1]));
                    }
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        return rs.getObject(1);
                    }
                } catch (SQLException ex) {
                    System.err.println("QUERY : " + query);
                    Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private T retourne(ResultSet lect, Object r) {
        return retourne(lect, r, 0);
    }

    private T retourne(ResultSet lect, Object r, int index) {
        try {
            String table = table(r.getClass());
            if (lect != null) {
                Tables y;
                for (Field field : r.getClass().getDeclaredFields()) {
                    String column = colonne(field);
                    if (column != null ? column.trim().length() > 0 : false) {
                        column = table + "_" + column + (index > 0 ? ("_" + index) : "");
                        if (existe(lect, column)) {
                            field.setAccessible(true);
                            if (contrainte(field)) {
                                try {
                                    long id = lect.getLong(column);
                                    if (id > 0) {
                                        Object classe = field.getType().newInstance();
                                        y = new Tables(id, table(field.getType()));
                                        int idx = tables.indexOf(y);
                                        if (idx > -1) {
                                            y = tables.get(idx);
                                        }
                                        if (idx < 0) {
                                            if (classe.getClass().equals(r.getClass())) {
                                                index = 1;
                                                idx = classes.indexOf(new Tables(table(classe.getClass()), index));
                                                if (idx > -1) {
                                                    index = classes.get(idx).occurence + 1;
                                                    classes.get(idx).occurence = index;
                                                } else {
                                                    classes.add(new Tables(table(classe.getClass()), index));
                                                }
                                                y.value = retourne(lect, classe, index);
                                            } else {
                                                y.value = retourne(lect, classe);
                                            }
                                            Field key = key(classe.getClass());
                                            if (!setValue(key, y.value, id)) {
                                                setValue(key, y.value, (int) id);
                                            }
                                            tables.add(y);
                                        }
                                        field.set(r, y.value);
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
                                    Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                load(field, lect.getObject(column), r);
                            }
                        } else {
//                            System.out.println("NOT EXIST : " + column);
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) r;
    }

    private Date convert(java.sql.Date data) {
        if (data == null) {
            return null;
        }
        return new Date(data.getTime());
    }

    private void load(Field field, Object value, Object classe) {
        try {
            if (value != null) {
                switch (field.getType().getSimpleName()) {
                    case "Character":
                    case "char":
                        try {
                            field.set(classe, value.toString().charAt(0));
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Character " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Boolean":
                    case "boolean":
                        try {
                            field.set(classe, value);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Boolean " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Double":
                    case "double":
                        try {
                            field.set(classe, value);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Double " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Long":
                    case "long":
                        try {
                            try {
                                field.set(classe, value);
                            } catch (Exception ex) {
                                field.set(classe, (int) value);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Long " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Integer":
                    case "int":
                        try {
                            try {
                                field.set(classe, value);
                            } catch (Exception ex) {
                                field.set(classe, (long) value);
                            }
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Integer " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "Date":
                        try {
                            field.set(classe, value);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Date " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    default:
                        try {
                            field.set(classe, value);
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            System.out.println("Other " + value.toString());
                            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
            }
        } catch (SecurityException ex) {
            Logger.getLogger(YvsBaseTiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class Tables {

        public long id;
        public String name;
        public int occurence;
        public T value;

        public Tables(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Tables(String name) {
            this((long) 0, name);
        }

        public Tables(String name, int occurence) {
            this((long) 0, name);
            this.occurence = occurence;
        }

        public Tables(Long id, String name, T value) {
            this(id, name);
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
            hash = 67 * hash + Objects.hashCode(this.name);
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
            final Tables other = (Tables) obj;
            if (this.id != other.id) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

    }
}
