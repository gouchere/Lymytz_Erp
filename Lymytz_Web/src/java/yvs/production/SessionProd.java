/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.production.base.EquipeProduction;
import yvs.users.Users;

/**
 *
 * @author hp Elite 8300
 */
@SessionScoped
@ManagedBean
public class SessionProd implements Serializable {

    private long id;
    private Date dateSession = new Date();
    private OrdreFabrication ordre = new OrdreFabrication();
    private EquipeProduction equipe = new EquipeProduction();
    private Depots depot = new Depots();
    private TrancheHoraire tranche = new TrancheHoraire();
    private Users producteur = new Users();
    private YvsUsersAgence author;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private boolean actif;

    public SessionProd() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateSession() {
        return dateSession;
    }

    public void setDateSession(Date dateSession) {
        this.dateSession = dateSession;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public OrdreFabrication getOrdre() {
        return ordre;
    }

    public void setOrdre(OrdreFabrication ordre) {
        this.ordre = ordre;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    public TrancheHoraire getTranche() {
        return tranche;
    }

    public void setTranche(TrancheHoraire tranche) {
        this.tranche = tranche;
    }

    public Users getProducteur() {
        return producteur;
    }

    public void setProducteur(Users producteur) {
        this.producteur = producteur;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Depots getDepot() {
        return depot;
    }

    public void setDepot(Depots depot) {
        this.depot = depot;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

}
