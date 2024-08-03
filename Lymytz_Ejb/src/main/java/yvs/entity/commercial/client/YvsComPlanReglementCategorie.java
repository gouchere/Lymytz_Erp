/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.client;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_plan_reglement_categorie")
@NamedQueries({
    @NamedQuery(name = "YvsComPlanReglementCategorie.findAll", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.categorie.societe = :societe"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findById", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findByCategorie", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findByCategorieMontant", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.categorie = :categorie AND :montant BETWEEN y.montantMinimal AND y.montantMaximal"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findByMontantMinimal", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.montantMinimal = :montantMinimal"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findByMontantMaximal", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.montantMaximal = :montantMaximal"),
    @NamedQuery(name = "YvsComPlanReglementCategorie.findByActif", query = "SELECT y FROM YvsComPlanReglementCategorie y WHERE y.actif = :actif")})
public class YvsComPlanReglementCategorie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_plan_reglement_categorie_id_seq", name = "yvs_com_plan_reglement_categorie_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_plan_reglement_categorie_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_minimal")
    private Double montantMinimal;
    @Column(name = "montant_maximal")
    private Double montantMaximal;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieClient categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsComPlanReglementCategorie() {
    }

    public YvsComPlanReglementCategorie(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantMinimal() {
        return montantMinimal != null ? montantMinimal : 0;
    }

    public void setMontantMinimal(Double montantMinimal) {
        this.montantMinimal = montantMinimal;
    }

    public Double getMontantMaximal() {
        return montantMaximal != null ? montantMaximal : 0;
    }

    public void setMontantMaximal(Double montantMaximal) {
        this.montantMaximal = montantMaximal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsBaseCategorieClient getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsBaseCategorieClient categorie) {
        this.categorie = categorie;
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
        if (!(object instanceof YvsComPlanReglementCategorie)) {
            return false;
        }
        YvsComPlanReglementCategorie other = (YvsComPlanReglementCategorie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.client.YvsComPlanReglementCategorie[ id=" + id + " ]";
    }

}
