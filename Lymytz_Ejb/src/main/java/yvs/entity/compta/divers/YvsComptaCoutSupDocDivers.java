/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_cout_sup_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findAll", query = "SELECT y FROM YvsComptaCoutSupDocDivers y"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findById", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findByMontant", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findBySupp", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findByActif", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findByDateUpdate", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findByDateSave", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.dateSave = :dateSave"),

    @NamedQuery(name = "YvsComptaCoutSupDocDivers.findByDocDivers", query = "SELECT y FROM YvsComptaCoutSupDocDivers y WHERE y.docDivers = :docDivers")})
public class YvsComptaCoutSupDocDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_cout_sup_doc_divers_id_seq", name = "yvs_compta_cout_sup_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_cout_sup_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "lier_tiers")
    private Boolean lierTiers;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;
    @OneToOne(mappedBy = "cout")
    private YvsComptaCaissePieceCoutDivers pieceCout;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsComptaCoutSupDocDivers() {
    }

    public YvsComptaCoutSupDocDivers(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public Boolean getLierTiers() {
        return lierTiers != null ? lierTiers : false;
    }

    public void setLierTiers(Boolean lierTiers) {
        this.lierTiers = lierTiers;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public YvsComptaCaissePieceCoutDivers getPieceCout() {
        return pieceCout;
    }

    public void setPieceCout(YvsComptaCaissePieceCoutDivers pieceCout) {
        this.pieceCout = pieceCout;
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
        if (!(object instanceof YvsComptaCoutSupDocDivers)) {
            return false;
        }
        YvsComptaCoutSupDocDivers other = (YvsComptaCoutSupDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.divers.YvsComptaCoutSupDocDivers[ id=" + id + " ]";
    }

}
