/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.credit;

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
 * @author lymytz Cette classe modélise la remise de fond au mutualiste. elle
 * est consécutive à l'acceptation ou validation du crédit
 */
@Entity
@Table(name = "yvs_mut_reglement_credit")
@NamedQueries({
    @NamedQuery(name = "YvsMutReglementCredit.findAll", query = "SELECT y FROM YvsMutReglementCredit y"),
    @NamedQuery(name = "YvsMutReglementCredit.findById", query = "SELECT y FROM YvsMutReglementCredit y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutReglementCredit.findByDateReglement", query = "SELECT y FROM YvsMutReglementCredit y WHERE y.dateReglement = :dateReglement"),
    @NamedQuery(name = "YvsMutReglementCredit.findByMontant", query = "SELECT y FROM YvsMutReglementCredit y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsMutReglementCredit.findByCredit", query = "SELECT y FROM YvsMutReglementCredit y WHERE y.credit = :credit")})
public class YvsMutReglementCredit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_reglement_credit_id_seq", name = "yvs_mut_reglement_credit_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_reglement_credit_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
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
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCredit credit;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCaisse caisse;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutCompte comptes;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutReglementCredit() {
    }

    public YvsMutReglementCredit(Long id) {
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
        return statutPiece != null ? statutPiece : 'W';
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public YvsMutCredit getCredit() {
        return credit;
    }

    public void setCredit(YvsMutCredit credit) {
        this.credit = credit;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public YvsMutCompte getComptes() {
        return comptes;
    }

    public void setComptes(YvsMutCompte comptes) {
        this.comptes = comptes;
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
        if (!(object instanceof YvsMutReglementCredit)) {
            return false;
        }
        YvsMutReglementCredit other = (YvsMutReglementCredit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.echellonage.YvsMutReglementCredit[ id=" + id + " ]";
    }

    public String getReglePar() {
        return reglePar;
    }

    public void setReglePar(String reglePar) {
        this.reglePar = reglePar;
    }

}
