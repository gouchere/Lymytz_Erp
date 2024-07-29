/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import javax.validation.constraints.Size;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_com_parametre_stock")
@NamedQueries({
    @NamedQuery(name = "YvsComParametreStock.findAll", query = "SELECT y FROM YvsComParametreStock y"),
    @NamedQuery(name = "YvsComParametreStock.findById", query = "SELECT y FROM YvsComParametreStock y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParametreStock.findByAgence", query = "SELECT y FROM YvsComParametreStock y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreStock.findnbJourAnByAgence", query = "SELECT y.jourAnterieur FROM YvsComParametreStock y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreStock.findByValidAuto", query = "SELECT y FROM YvsComParametreStock y WHERE y.comptabilisationAuto = :comptabilisationAuto"),
    @NamedQuery(name = "YvsComParametreStock.findByJourAnterieur", query = "SELECT y FROM YvsComParametreStock y WHERE y.jourAnterieur = :jourAnterieur")})
public class YvsComParametreStock extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_parametre_stock_id_seq", name = "yvs_com_parametre_stock_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_parametre_stock_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "comptabilisation_mode")
    private String comptabilisationMode;
    @Column(name = "comptabilisation_auto")
    private Boolean comptabilisationAuto;
    @Column(name = "active_ration")
    private Boolean activeRation;
    @Column(name = "calcul_pr")
    private Boolean calculPr = true;
    @Column(name = "jour_anterieur")
    private Integer jourAnterieur;
    @Column(name = "duree_update")
    private Integer dureeUpdate;
    @Column(name = "taille_code_ration")
    private Integer tailleCodeRation;
    @Column(name = "marge_time_fiche_ration")
    private Integer margeTimeFicheRation;
    @Column(name = "print_document_when_valide")
    private Boolean printDocumentWhenValide;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;

    public YvsComParametreStock() {
    }

    public YvsComParametreStock(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPrintDocumentWhenValide() {
        return printDocumentWhenValide != null ? printDocumentWhenValide : false;
    }

    public void setPrintDocumentWhenValide(Boolean printDocumentWhenValide) {
        this.printDocumentWhenValide = printDocumentWhenValide;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getComptabilisationMode() {
        return comptabilisationMode != null ? comptabilisationMode.trim().length() > 0 ? comptabilisationMode : "D" : "D";
    }

    public void setComptabilisationMode(String comptabilisationMode) {
        this.comptabilisationMode = comptabilisationMode;
    }

    public Boolean getComptabilisationAuto() {
        return comptabilisationAuto != null ? comptabilisationAuto : false;
    }

    public void setComptabilisationAuto(Boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public Integer getJourAnterieur() {
        return jourAnterieur != null ? jourAnterieur : 0;
    }

    public void setJourAnterieur(Integer jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public Integer getMargeTimeFicheRation() {
        return margeTimeFicheRation != null ? margeTimeFicheRation : 6;
    }

    public void setMargeTimeFicheRation(Integer margeTimeFicheRation) {
        this.margeTimeFicheRation = margeTimeFicheRation;
    }

    public Integer getTailleCodeRation() {
        return tailleCodeRation != null ? tailleCodeRation : 5;
    }

    public void setTailleCodeRation(Integer tailleCodeRation) {
        this.tailleCodeRation = tailleCodeRation;
    }

    public Boolean getActiveRation() {
        return activeRation != null ? activeRation : true;
    }

    public void setActiveRation(Boolean activeRation) {
        this.activeRation = activeRation;
    }

    public Boolean getCalculPr() {
        return calculPr != null ? calculPr : false;
    }

    public void setCalculPr(Boolean calculPr) {
        this.calculPr = calculPr;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public Integer getDureeUpdate() {
        return dureeUpdate != null ? dureeUpdate : 0;
    }

    public void setDureeUpdate(Integer dureeUpdate) {
        this.dureeUpdate = dureeUpdate;
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
        if (!(object instanceof YvsComParametreStock)) {
            return false;
        }
        YvsComParametreStock other = (YvsComParametreStock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComParametreStock[ id=" + id + " ]";
    }

}
