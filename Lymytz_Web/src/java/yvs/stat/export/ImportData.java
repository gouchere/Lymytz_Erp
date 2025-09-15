/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.stat.export.YvsStatExportColonne;
import yvs.util.Util;

/**
 *
 * @author dowes
 */
@ManagedBean
@SessionScoped
public class ImportData implements Serializable {

    private List<YvsStatExportColonne> keys;
    private List<YvsStatExportColonne> colonnes;

    public ImportData() {
        keys = new ArrayList<>();
        colonnes = new ArrayList<>();
    }

    public List<YvsStatExportColonne> getColonnes() {
        return colonnes;
    }

    public List<YvsStatExportColonne> getKeys() {
        return keys;
    }

    public void setKeys(List<YvsStatExportColonne> keys) {
        this.keys = keys;
    }

    public void setColonnes(List<YvsStatExportColonne> colonnes) {
        this.colonnes = colonnes;
    }

    public static Object getValues(String type, String value) {
        if (type == null || !Util.asString(value)) {
            return null;
        }
        return Util.getValues(type, value);
    }

    public YvsStatExportColonne getKey(String table, String colonne) {
        for (YvsStatExportColonne k : keys) {
            if (k.getTableName().equals(table) ? (Util.asString(colonne) ? k.getColonne().equals(colonne) : true) : false) {
                return k;
            }
        }
        return null;
    }

    public static class ImportValue implements Serializable {

        private Long id;
        private YvsStatExportColonne key;
        private List<YvsStatExportColonne> colonnes;

        public ImportValue() {
            this.colonnes = new ArrayList<>();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public List<YvsStatExportColonne> getColonnes() {
            return colonnes;
        }

        public void setColonnes(List<YvsStatExportColonne> colonnes) {
            this.colonnes = colonnes;
        }

        public YvsStatExportColonne getKey() {
            return key;
        }

        public void setKey(YvsStatExportColonne key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "{id=" + id + ", key = " + key + "}";
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + Objects.hashCode(this.id);
            hash = 37 * hash + Objects.hashCode(this.key);
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
            final ImportValue other = (ImportValue) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            return true;
        }
    }

    public static class TableOrder implements Serializable {

        private String table;
        private String key;
        private List<TableContrainte> contraintes;
        private List<ImportValue> values;
        private List<TableContrainte> conditionsForce;

        public TableOrder() {
            this.values = new ArrayList<>();
            this.contraintes = new ArrayList<>();
            this.conditionsForce = new ArrayList<>();
        }

        public TableOrder(String table) {
            this();
            this.table = table;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<ImportValue> getValues() {
            return values;
        }

        public void setValues(List<ImportValue> values) {
            this.values = values;
        }

        public List<TableContrainte> getConditionsForce() {
            return conditionsForce;
        }

        public void setConditionsForce(List<TableContrainte> conditionsForce) {
            this.conditionsForce = conditionsForce;
        }

        private List<TableContrainte> getContraintes() {
            return contraintes;
        }

        private void setContraintes(List<TableContrainte> contraintes) {
            this.contraintes = contraintes;
        }

        private int indexOfContrainte(String table) {
            for (int i = 0; i < contraintes.size() - 1; i++) {
                if (contraintes.get(i).getTable().equals(table)) {
                    return i;
                }
            }
            return -1;
        }

        private boolean containsOfContrainte(String table) {
            int index = indexOfContrainte(table);
            return index > -1;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.table);
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
            final TableOrder other = (TableOrder) obj;
            if (!Objects.equals(this.table, other.table)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "{" + "table=" + table + '}';
        }

    }

    public static class TableContrainte implements Serializable {

        private String table;
        private String colonne;
        private Object value;

        public TableContrainte(String table, String colonne) {
            this.table = table;
            this.colonne = colonne;
        }

        public TableContrainte(String table, String colonne, Object value) {
            this(table, colonne);
            this.value = value;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }

        public String getColonne() {
            return colonne;
        }

        public void setColonne(String colonne) {
            this.colonne = colonne;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.table);
            hash = 97 * hash + Objects.hashCode(this.colonne);
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
            final TableContrainte other = (TableContrainte) obj;
            if (!Objects.equals(this.table, other.table)) {
                return false;
            }
            if (!Objects.equals(this.colonne, other.colonne)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "{" + "table=" + table + ", colonne=" + colonne + '}';
        }

    }
}
