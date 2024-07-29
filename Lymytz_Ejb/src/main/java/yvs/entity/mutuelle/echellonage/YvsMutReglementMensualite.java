/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.echellonage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_reglement_mensualite")
@NamedQueries({
    @NamedQuery(name = "YvsMutReglementMensualite.findAll", query = "SELECT y FROM YvsMutReglementMensualite y"),
    @NamedQuery(name = "YvsMutReglementMensualite.findAllReglementCredit", query = "SELECT y FROM YvsMutReglementMensualite y WHERE y.mensualite.echellonage.credit=:credit"),
    @NamedQuery(name = "YvsMutReglementMensualite.findById", query = "SELECT y FROM YvsMutReglementMensualite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutReglementMensualite.findByDateReglement", query = "SELECT y FROM YvsMutReglementMensualite y WHERE y.dateReglement = :dateReglement"),
    @NamedQuery(name = "YvsMutReglementMensualite.findByMontant", query = "SELECT y FROM YvsMutReglementMensualite y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutReglementMensualite.findByEcheancier", query = "SELECT y FROM YvsMutReglementMensualite y WHERE y.mensualite.echellonage = :echeancier"),
    @NamedQuery(name = "YvsMutReglementMensualite.findSumVerse", query = "SELECT SUM(y.montant) FROM YvsMutReglementMensualite y WHERE y.mensualite.echellonage = :echeancier AND y.mensualite.etat = :statutMens AND y.statutPiece =:statutReg"),
    @NamedQuery(name = "YvsMutReglementMensualite.findSumVerseForMensualite", query = "SELECT SUM(y.montant) FROM YvsMutReglementMensualite y WHERE y.mensualite.id IN :mensualites"),
    @NamedQuery(name = "YvsMutReglementMensualite.findSoldeInteret", query = "SELECT SUM(y.montant) FROM YvsMutReglementMensualite y WHERE y.mensualite.echellonage.credit.etat=:etat AND y.mensualite.echellonage.etat=:etatEch AND (y.mensualite.echellonage.dateEchellonage BETWEEN :dateDebut AND :dateFin)"),
    @NamedQuery(name = "YvsMutReglementMensualite.findTotalByCodeOp", query = "SELECT SUM(y.montant) FROM YvsMutReglementMensualite y WHERE y.codeOperation=:codeOperation"),
    @NamedQuery(name = "YvsMutReglementMensualite.findSoldeCredit", query = "SELECT SUM(y.montant) FROM YvsMutReglementMensualite y WHERE y.mensualite.echellonage.credit.compte.mutualiste = :mutualiste AND y.mensualite.echellonage.credit.etat=:etat AND y.mensualite.echellonage.etat=:etatEch")})
public class YvsMutReglementMensualite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_reglement_mensualite_id_seq", name = "yvs_mut_reglement_mensualite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_reglement_mensualite_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_reglement")
    @Temporal(TemporalType.DATE)
    private Date dateReglement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "regle_par")
    private String reglePar;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "mode_paiement")
    private String modePaiement;
    @Column(name = "code_operation")
    private String codeOperation;    // Permet lors de l'enregistrement des salaire de faire le lien avec d'autres ligne (Retenues, epargne mensuelle, mensualite)

    @JoinColumn(name = "mensualite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMensualite mensualite;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte compte;
//    @JoinColumn(name = "souce_reglement", referencedColumnName = "id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private YvsMutPaiementSalaire souceReglement; // modélise le règlement d'un salaire
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private double montantReste;

    public YvsMutReglementMensualite() {
    }

    public YvsMutReglementMensualite(Long id) {
        this.id = id;
    }

    public YvsMutReglementMensualite(Long id, YvsMutReglementMensualite y) {
        this.id = id;
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.dateReglement = y.dateReglement;
        this.montant = y.montant;
        this.reglePar = y.reglePar;
        this.statutPiece = y.statutPiece;
        this.mensualite = y.mensualite;
        this.caisse = y.caisse;
        this.author = y.author;
        this.selectActif = y.selectActif;
        this.new_ = y.new_;
        this.montantReste = y.montantReste;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsMutCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsMutCaisse caisse) {
        this.caisse = caisse;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getStatutPiece() {
        return statutPiece;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateReglement() {
        return dateReglement;
    }

    public void setDateReglement(Date dateReglement) {
        this.dateReglement = dateReglement;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public YvsMutMensualite getMensualite() {
        return mensualite;
    }

    public void setMensualite(YvsMutMensualite mensualite) {
        this.mensualite = mensualite;
    }

    public String getCodeOperation() {
        return codeOperation;
    }

    public void setCodeOperation(String codeOperation) {
        this.codeOperation = codeOperation;
    }

    public String getReglePar() {
        return reglePar;
    }

    public void setReglePar(String reglePar) {
        this.reglePar = reglePar;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public YvsMutCompte getCompte() {
        return compte;
    }

    public void setCompte(YvsMutCompte compte) {
        this.compte = compte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsMutReglementMensualite)) {
            return false;
        }
        YvsMutReglementMensualite other = (YvsMutReglementMensualite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite[ id=" + id + " ]";
    }

}
