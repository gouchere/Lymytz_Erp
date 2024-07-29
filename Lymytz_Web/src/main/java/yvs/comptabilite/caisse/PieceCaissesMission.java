/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite.caisse;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.param.workflow.YvsWorkflowValidPcMission;
import yvs.entity.users.YvsUsers;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class PieceCaissesMission implements Serializable {

    private long id;
    private Date datePiece = new Date();
    private Date datePaiement = new Date();
    private Date datePaimentPrevu = new Date();
    private YvsUsers caissier;
    private String numeroPiece;
    private double montant;
    private String author;
    private Object objetSource;
    private Caisses caisse = new Caisses();
    private String typePiece;
    private double montantTotal;
    private double montantAvance;
    private boolean error;    
    private char statutPiece;
    private List<PieceCaissesMission> listePieceLie;
    private List<YvsWorkflowValidPcMission> etapesValidation;

    public PieceCaissesMission() {
        listePieceLie=new ArrayList<>();
        etapesValidation=new ArrayList<>();
    }

    public PieceCaissesMission(long id) {
        this.id = id;
        listePieceLie=new ArrayList<>();
        etapesValidation=new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public String getNumeroPiece() {
        return numeroPiece;
    }

    public void setNumeroPiece(String numeroPiece) {
        this.numeroPiece = numeroPiece;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public char getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(char statutPiece) {
        this.statutPiece = statutPiece;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Object getObjetSource() {
        return objetSource;
    }

    public void setObjetSource(Object objetSource) {
        this.objetSource = objetSource;
    }

    public Caisses getCaisse() {
        return caisse;
    }

    public void setCaisse(Caisses caisse) {
        this.caisse = caisse;
    }

    public String getTypePiece() {
        return typePiece;
    }

    public void setTypePiece(String typePiece) {
        this.typePiece = typePiece;
    }

    public double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<PieceCaissesMission> getListePieceLie() {
        return listePieceLie;
    }

    public void setListePieceLie(List<PieceCaissesMission> listePieceLie) {
        this.listePieceLie = listePieceLie;
    }

    public List<YvsWorkflowValidPcMission> getEtapesValidation() {
        return etapesValidation;
    }

    public void setEtapesValidation(List<YvsWorkflowValidPcMission> etapesValidation) {
        this.etapesValidation = etapesValidation;
    }

    public Date getDatePaimentPrevu() {
        return datePaimentPrevu;
    }

    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final PieceCaissesMission other = (PieceCaissesMission) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
