/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.ration;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_doc_ration")
@NamedQueries({
    @NamedQuery(name = "YvsComDocRation.findAll", query = "SELECT y FROM YvsComDocRation y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocRation.countAll", query = "SELECT COUNT(y) FROM YvsComDocRation y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocRation.findById", query = "SELECT y FROM YvsComDocRation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocRation.findCOne", query = "SELECT y.id FROM YvsComDocRation y WHERE y.dateFiche=:date AND y.creneauHoraire.id=:idCreneau AND y.depot.id=:idDepot"),
    @NamedQuery(name = "YvsComDocRation.findByNumDocL", query = "SELECT y FROM YvsComDocRation y WHERE y.numDoc LIKE :numDoc AND y.depot.agence=:agence ORDER BY y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocRation.findByNumDoc", query = "SELECT y FROM YvsComDocRation y WHERE y.numDoc LIKE :numDoc AND y.depot.agence.societe=:societe ORDER BY y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocRation.findByNumReference", query = "SELECT y FROM YvsComDocRation y WHERE y.numReference = :numReference"),
    @NamedQuery(name = "YvsComDocRation.findByDateSave", query = "SELECT y FROM YvsComDocRation y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComDocRation.findByDateUpdate", query = "SELECT y FROM YvsComDocRation y WHERE y.dateUpdate = :dateUpdate"),

    @NamedQuery(name = "YvsComDocRation.findPrecedent", query = "SELECT y FROM YvsComDocRation y WHERE y.periode.dateDebut < :date ORDER BY y.periode.dateDebut DESC")})
public class YvsComDocRation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_doc_ration_id_seq", name = "yvs_com_doc_ration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_doc_ration_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "num_doc")
    private String numDoc;
    @Size(max = 2147483647)
    @Column(name = "num_reference")
    private String numReference;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "nbr_jr_usine")
    private Integer nbrJrUsine;
    @Column(name = "date_fiche")
    @Temporal(TemporalType.DATE)
    private Date dateFiche;
    @Column(name = "cloturer")
    private Boolean cloturer = false;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "magasinier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers magasinier;
    @JoinColumn(name = "periode", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPeriodeRation periode;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "creneau_horaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauDepot creneauHoraire;
    @OneToMany(mappedBy = "docRation")
    private List<YvsComRation> listRations;
    @Transient
    private boolean new_;
    @Transient
    private double qteDistribue;
    @Transient
    private double qteParametre;
    @Transient
    private YvsComDocRation precedent;

    public YvsComDocRation() {
        listRations = new ArrayList<>();
    }

    public YvsComDocRation(Long id) {
        this();
        this.id = id;
    }

    public YvsComDocRation(YvsComDocRation y) {
        this.id = y.getId();
        this.numDoc = y.getNumDoc();
        this.numReference = y.getNumReference();
        this.statut = y.getStatut();
        this.dateSave = y.getDateSave();
        this.dateUpdate = y.getDateUpdate();
        this.author = y.getAuthor();
        this.magasinier = y.getMagasinier();
        this.periode = y.getPeriode();
        this.depot = y.getDepot();
        this.nbrJrUsine = y.getNbrJrUsine();
        this.listRations = y.getListRations();
        this.new_ = y.isNew_();
        this.qteDistribue = y.getQteDistribue();
        this.qteParametre = y.getQteParametre();
        this.creneauHoraire = y.getCreneauHoraire();
        this.dateFiche = y.getDateFiche();
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsComDocRation getPrecedent() {
        return precedent;
    }

    public void setPrecedent(YvsComDocRation precedent) {
        this.precedent = precedent;
    }

    public double getQteParametre() {
        return qteParametre;
    }

    public void setQteParametre(double qteParametre) {
        this.qteParametre = qteParametre;
    }

    public double getQteDistribue() {
        return qteDistribue;
    }

    public void setQteDistribue(double qteDistribue) {
        this.qteDistribue = qteDistribue;
    }

    public Integer getNbrJrUsine() {
        return nbrJrUsine != null ? nbrJrUsine : 30;
    }

    public void setNbrJrUsine(Integer nbrJrUsine) {
        this.nbrJrUsine = nbrJrUsine;
    }

    public String getNumDoc() {
        return numDoc != null ? numDoc : "";
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getNumReference() {
        return numReference != null ? numReference : "";
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
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

    public YvsUsers getMagasinier() {
        return magasinier;
    }

    public void setMagasinier(YvsUsers magasinier) {
        this.magasinier = magasinier;
    }

    public YvsComPeriodeRation getPeriode() {
        return periode;
    }

    public void setPeriode(YvsComPeriodeRation periode) {
        this.periode = periode;
    }

    public List<YvsComRation> getListRations() {
        return listRations;
    }

    public void setListRations(List<YvsComRation> listRations) {
        this.listRations = listRations;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public Character getStatut() {
        return statut != null ? statut : Constantes.STATUT_DOC_EDITABLE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public YvsComCreneauDepot getCreneauHoraire() {
        return creneauHoraire;
    }

    public void setCreneauHoraire(YvsComCreneauDepot creneauHoraire) {
        this.creneauHoraire = creneauHoraire;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Date getDateFiche() {
        return dateFiche != null ? dateFiche : new Date();
    }

    public void setDateFiche(Date dateFiche) {
        this.dateFiche = dateFiche;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
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
        if (!(object instanceof YvsComDocRation)) {
            return false;
        }
        YvsComDocRation other = (YvsComDocRation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.ration.YvsComDocRation[ id=" + id + " ]";
    }

}
