/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.local;

import java.util.Objects;

/**
 *
 * @author LENOVO Cette classe est un bean destiné à avoir la liste des des
 * classes portant l'annotation @Entity et l'annotation @Table
 */
public class LymytzEntity {

    private String entity;
    private String annotation;
    private int equalsField = 1;

    public LymytzEntity() {
    }

    public LymytzEntity(String annotation) {
        this.annotation = annotation;
    }

    public LymytzEntity(String entity, String annotation) {
        this.entity = entity;
        this.annotation = annotation;
    }

    public LymytzEntity(String entity, String annotation, int equal) {
        this.entity = entity;
        this.annotation = annotation;
        this.equalsField = (equal <= 0) ? 1 : equal;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public int getEqualsField() {
        return equalsField;
    }

    public void setEqualsField(int equalsField) {
        this.equalsField = equalsField;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        if (equalsField == 1) {
            hash = 79 * hash + Objects.hashCode(this.annotation);
        } else {
            hash = 79 * hash + Objects.hashCode(this.entity);
        }
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
        final LymytzEntity other = (LymytzEntity) obj;
        if (equalsField == 1) {
            if (!Objects.equals(this.annotation, other.annotation)) {
                return false;
            }
        } else {
            if (!Objects.equals(this.entity, other.entity)) {
                return false;
            }
        }
        return true;
    }

}
