/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.bases;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import yvs.entity.users.YvsResourcePageGroup;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public class Autorisations implements Serializable {

    private int id;
    private String reference;
    private String designation;
    private String description;
    private boolean acces;
    private String type;
    private String code;
    private String parent;
    private Autorisations page;
    private Autorisations module;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private YvsUsersAgence author;
    private YvsResourcePageGroup groupe = new YvsResourcePageGroup();

    public Autorisations() {
    }

    public Autorisations(int id, String type) {
        this();
        this.id = id;
        this.type = type;
    }

    public Autorisations(int id, String reference, String designation, String description, boolean acces, String type, String parent, Date dateUpdate, YvsUsersAgence author) {
        this(id, type);
        this.reference = reference;
        this.designation = designation;
        this.description = description;
        this.acces = acces;
        this.parent = parent;
        this.dateUpdate = dateUpdate;
        this.author = author;
    }

    public Autorisations(int id, String reference, String designation, String description, boolean acces, String type, String parent, Autorisations module, Date dateUpdate, YvsUsersAgence author) {
        this(id, reference, designation, description, acces, type, parent, dateUpdate, author);
        this.module = module;
    }

    public Autorisations(int id, String reference, String designation, String description, boolean acces, String type, String parent, Autorisations page, Autorisations module, Date dateUpdate, YvsUsersAgence author) {
        this(id, reference, designation, description, acces, type, parent, module, dateUpdate, author);
        this.page = page;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAcces() {
        return acces;
    }

    public void setAcces(boolean acces) {
        this.acces = acces;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Autorisations getPage() {
        return page;
    }

    public void setPage(Autorisations page) {
        this.page = page;
    }

    public Autorisations getModule() {
        return module;
    }

    public void setModule(Autorisations module) {
        this.module = module;
    }

    public String getCode() {
        code = type.equals("YvsModule") ? "M" : type.equals("YvsPageModule") ? "P" : "R";
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsResourcePageGroup getGroupe() {
        return groupe;
    }

    public void setGroupe(YvsResourcePageGroup groupe) {
        this.groupe = groupe;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.type);
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
        final Autorisations other = (Autorisations) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

}
