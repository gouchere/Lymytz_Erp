/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;
import yvs.entity.commercial.client.YvsComElementContratsClient;

/**
 *
 * @author Lymytz Dowes
 */
public class ContratsClient implements Serializable {

    private long id;
    private String reference;
    private String referenceExterne;
    private char type = 'I';
    private Date dateExpiration = new Date();
    private Date dateDebutFacturation = new Date();
    private int intervalFacturation = 30;
    private char periodeFacturation = 'M';
    private boolean actif = true;
    private Date dateSave = new Date();
    private Client client = new Client();
    private List<YvsComElementContratsClient> elements;
    private List<YvsComElementAddContratsClient> additionnels;

    public ContratsClient() {
        elements = new ArrayList<>();
        additionnels = new ArrayList<>();
    }

    public ContratsClient(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public char getType() {
        return type != ' ' ? type : 'I';
    }

    public void setType(char type) {
        this.type = type;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Date getDateDebutFacturation() {
        return dateDebutFacturation;
    }

    public void setDateDebutFacturation(Date dateDebutFacturation) {
        this.dateDebutFacturation = dateDebutFacturation;
    }

    public int getIntervalFacturation() {
        return intervalFacturation;
    }

    public void setIntervalFacturation(int intervalFacturation) {
        this.intervalFacturation = intervalFacturation;
    }

    public char getPeriodeFacturation() {
        return periodeFacturation != ' ' ? periodeFacturation : 'M';
    }

    public void setPeriodeFacturation(char periodeFacturation) {
        this.periodeFacturation = periodeFacturation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<YvsComElementContratsClient> getElements() {
        return elements;
    }

    public void setElements(List<YvsComElementContratsClient> elements) {
        this.elements = elements;
    }

    public List<YvsComElementAddContratsClient> getAdditionnels() {
        return additionnels;
    }

    public void setAdditionnels(List<YvsComElementAddContratsClient> additionnels) {
        this.additionnels = additionnels;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final ContratsClient other = (ContratsClient) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
