/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.th.workflow;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.Date;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.YvsAgences;
import yvs.parametrage.agence.Agence;
import yvs.users.Users;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class Connexion implements Serializable {

    private Long id;
    private String adresseIp;
    private String adresseMac;
    private Date dateConnexion;
    private Users user;
    private YvsAgences agence = new YvsAgences();

    public Connexion() {
    }

    public Connexion(Long id) {
        this.id = id;
    }

    public Connexion(Long id, String adresseIp, Date dateConnexion, Users user) {
        this.id = id;
        this.adresseIp = adresseIp;
        this.dateConnexion = dateConnexion;
        this.user = user;
    }

    public Connexion(String adresseIp, Date dateConnexion, Users user) {
        this.adresseIp = adresseIp;
        this.dateConnexion = dateConnexion;
        this.user = user;
    }

    public Connexion(String adresseIp, String adresseMac, Date dateConnexion, Users user) {
        this.adresseIp = adresseIp;
        this.adresseMac = adresseMac;
        this.dateConnexion = dateConnexion;
        this.user = user;
    }

    public Connexion(String adresseIp, Date dateConnexion, Users user, YvsAgences agence) {
        this.adresseIp = adresseIp;
        this.dateConnexion = dateConnexion;
        this.user = user;
        this.agence = agence;
    }

    public Connexion(String adresseIp, String adresseMac, Date dateConnexion, Users user, YvsAgences agence) {
        this.adresseIp = adresseIp;
        this.adresseMac = adresseMac;
        this.dateConnexion = dateConnexion;
        this.user = user;
        this.agence = agence;
    }

    public Connexion(Long id, String adresseIp, String adresseMac, Date dateConnexion, Users user) {
        this.id = id;
        this.adresseIp = adresseIp;
        this.adresseMac = adresseMac;
        this.dateConnexion = dateConnexion;
        this.user = user;
    }

    public Connexion(Long id, String adresseIp, String adresseMac, Date dateConnexion, Users user, YvsAgences agence) {
        this.id = id;
        this.adresseIp = adresseIp;
        this.adresseMac = adresseMac;
        this.dateConnexion = dateConnexion;
        this.user = user;
        this.agence = agence;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public Date getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(Date dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final Connexion other = (Connexion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
