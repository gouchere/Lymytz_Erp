/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.production.pilotage.YvsProdFluxComposant;
import yvs.entity.production.pilotage.YvsProdSuiviOperations;
import yvs.production.base.EquipeProduction;
import yvs.production.base.OperationsGamme;
import yvs.production.technique.PosteCharge;
import yvs.util.Constantes;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class OperationsOF implements Serializable {

    private long id;
    private OperationsGamme phase = new OperationsGamme();
    private String reference;
    private double dureeAttente, dureeExecution;
    private String commentaire;
    private boolean termine;  //l'opposé c'est en attente  
    private PosteCharge poste = new PosteCharge();
    private EquipeProduction equipe = new EquipeProduction();
    private double cout;
    private List<PosteCharge> postesRemplacement;
    private List<PosteCharge> listCout;

    private double tempsReglage;
    private double tempsOperation;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Date heureDebut = new Date();
    private Date heureFin = new Date();
    private int numero;
    private Date dateSave = new Date();
    private String statutOp;
    private int nbMachine;
    private int nbMainOeuvre;

    private PosteCharge mainOeuvre = new PosteCharge();
    private PosteCharge machine = new PosteCharge();
    private OrdreFabrication ordreFabrication = new OrdreFabrication();

    private List<YvsProdFluxComposant> composants;
    private List<YvsProdSuiviOperations> operations;

    public OperationsOF() {
        postesRemplacement = new ArrayList<>();
        listCout = new ArrayList<>();
        composants = new ArrayList<>();
    }

    public List<PosteCharge> getListCout() {
        return listCout;
    }

    public void setListCout(List<PosteCharge> listCout) {
        this.listCout = listCout;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OperationsGamme getPhase() {
        return phase;
    }

    public void setPhase(OperationsGamme phase) {
        this.phase = phase;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public double getDureeAttente() {
        return dureeAttente;
    }

    public void setDureeAttente(double dureeAttente) {
        this.dureeAttente = dureeAttente;
    }

    public double getDureeExecution() {
        return dureeExecution;
    }

    public void setDureeExecution(double dureeExecution) {
        this.dureeExecution = dureeExecution;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public boolean isTermine() {
        return termine;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    public PosteCharge getPoste() {
        return poste;
    }

    public void setPoste(PosteCharge poste) {
        this.poste = poste;
    }

    public List<PosteCharge> getPostesRemplacement() {
        return postesRemplacement;
    }

    public void setPostesRemplacement(List<PosteCharge> postesRemplacement) {
        this.postesRemplacement = postesRemplacement;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public EquipeProduction getEquipe() {
        return equipe;
    }

    public void setEquipe(EquipeProduction equipe) {
        this.equipe = equipe;
    }

    public double getTempsReglage() {
        return tempsReglage;
    }

    public void setTempsReglage(double tempsReglage) {
        this.tempsReglage = tempsReglage;
    }

    public double getTempsOperation() {
        return tempsOperation;
    }

    public void setTempsOperation(double tempsOperation) {
        this.tempsOperation = tempsOperation;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getHeureDebut() {
        return heureDebut != null ? heureDebut : new Date();
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin != null ? heureFin : new Date();
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getStatutOp() {
        return statutOp != null ? statutOp.trim().length() > 0 ? statutOp : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutOp(String statutOp) {
        this.statutOp = statutOp;
    }

    public int getNbMachine() {
        return nbMachine;
    }

    public void setNbMachine(int nbMachine) {
        this.nbMachine = nbMachine;
    }

    public int getNbMainOeuvre() {
        return nbMainOeuvre;
    }

    public void setNbMainOeuvre(int nbMainOeuvre) {
        this.nbMainOeuvre = nbMainOeuvre;
    }

    public PosteCharge getMainOeuvre() {
        return mainOeuvre;
    }

    public void setMainOeuvre(PosteCharge mainOeuvre) {
        this.mainOeuvre = mainOeuvre;
    }

    public PosteCharge getMachine() {
        return machine;
    }

    public void setMachine(PosteCharge machine) {
        this.machine = machine;
    }

    public OrdreFabrication getOrdreFabrication() {
        return ordreFabrication;
    }

    public void setOrdreFabrication(OrdreFabrication ordreFabrication) {
        this.ordreFabrication = ordreFabrication;
    }

    public List<YvsProdFluxComposant> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdFluxComposant> composants) {
        this.composants = composants;
    }

    public List<YvsProdSuiviOperations> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsProdSuiviOperations> operations) {
        this.operations = operations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final OperationsOF other = (OperationsOF) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
