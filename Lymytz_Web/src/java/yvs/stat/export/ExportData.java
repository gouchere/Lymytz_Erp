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

/**
 *
 * @author dowes
 */
@ManagedBean
@SessionScoped
public class ExportData implements Serializable {

    private String query;
    private List<String> headers;
    private List<YvsStatExportColonne> colonnes;
    private List<ExportDataCondition> conditions;
    private List<Object> values;

    public ExportData() {
        query = "";
        headers = new ArrayList();
        colonnes = new ArrayList();
        conditions = new ArrayList();
        values = new ArrayList();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<YvsStatExportColonne> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<YvsStatExportColonne> colonnes) {
        this.colonnes = colonnes;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<ExportDataCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<ExportDataCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.query);
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
        final ExportData other = (ExportData) obj;
        if (!Objects.equals(this.query, other.query)) {
            return false;
        }
        return true;
    }

    public static class ExportDataCondition implements Serializable {

        private String condition;
        private String operateur;
        private String type;

        public ExportDataCondition(String type, String condition, String operateur) {
            this.condition = condition;
            this.operateur = operateur;
            this.type = type;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOperateur() {
            return operateur;
        }

        public void setOperateur(String operateur) {
            this.operateur = operateur;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.condition);
            hash = 47 * hash + Objects.hashCode(this.type);
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
            final ExportDataCondition other = (ExportDataCondition) obj;
            if (!Objects.equals(this.condition, other.condition)) {
                return false;
            }
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            return true;
        }
    }
}
