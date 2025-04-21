/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.presence;

import java.io.Serializable;

/**
 *
 * @author Lymytz Dowes
 */
public class Pointeuse implements Serializable{

    private int id;
    private String ip;
    private int port;
    private int i_machine;
    private String description;
    private String emplacement;
    private boolean connecter;
    private boolean actif;
    private String type = "TFT";
    private Long societe;
    private boolean multiSociete;
    private boolean tampon, fileLoad;

    public Pointeuse() {
    }

    public Pointeuse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getI_machine() {
        return i_machine;
    }

    public void setI_machine(int i_machine) {
        this.i_machine = i_machine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public boolean isConnecter() {
        return connecter;
    }

    public void setConnecter(boolean connecter) {
        this.connecter = connecter;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSociete() {
        return societe;
    }

    public void setSociete(Long societe) {
        this.societe = societe;
    }

    public boolean isMultiSociete() {
        return multiSociete;
    }

    public void setMultiSociete(boolean multiSociete) {
        this.multiSociete = multiSociete;
    }

    public boolean isTampon() {
        return tampon;
    }

    public void setTampon(boolean tampon) {
        this.tampon = tampon;
    }

    public boolean isFileLoad() {
        return fileLoad;
    }

    public void setFileLoad(boolean fileLoad) {
        this.fileLoad = fileLoad;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.id;
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
        final Pointeuse other = (Pointeuse) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
