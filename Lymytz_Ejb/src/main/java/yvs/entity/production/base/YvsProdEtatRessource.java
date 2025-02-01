/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

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
import yvs.entity.param.YvsBaseTypeEtat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_etat_ressource")
@NamedQueries({
    @NamedQuery(name = "YvsProdEtatRessource.findAll", query = "SELECT y FROM YvsProdEtatRessource y"),
    @NamedQuery(name = "YvsProdEtatRessource.findById", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdEtatRessource.findByDateEtat", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.dateEtat = :dateEtat"),
    @NamedQuery(name = "YvsProdEtatRessource.findByCapaciteH", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.capaciteH = :capaciteH"),
    @NamedQuery(name = "YvsProdEtatRessource.findByCapaciteQ", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.capaciteQ = :capaciteQ"),
    @NamedQuery(name = "YvsProdEtatRessource.findByChargeH", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.chargeH = :chargeH"),
    @NamedQuery(name = "YvsProdEtatRessource.findByChargeQ", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.chargeQ = :chargeQ"),
    @NamedQuery(name = "YvsProdEtatRessource.findByTauxObsolescence", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.tauxObsolescence = :tauxObsolescence"),
    @NamedQuery(name = "YvsProdEtatRessource.findByActif", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.actif = :actif AND y.ressourceProduction = :ressource"),

    @NamedQuery(name = "YvsProdEtatRessource.findByRessourceType", query = "SELECT y FROM YvsProdEtatRessource y WHERE y.typeEtat = :type AND y.ressourceProduction = :ressource")})
public class YvsProdEtatRessource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_etat_ressource_id_seq", name = "yvs_prod_etat_ressource_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_etat_ressource_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_etat")
    @Temporal(TemporalType.DATE)
    private Date dateEtat;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capacite_h")
    private Double capaciteH;
    @Column(name = "capacite_q")
    private Double capaciteQ;
    @Column(name = "charge_h")
    private Double chargeH;
    @Column(name = "charge_q")
    private Double chargeQ;
    @Column(name = "taux_obsolescence")
    private Double tauxObsolescence;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "ressource_production", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge ressourceProduction;
    @JoinColumn(name = "type_etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTypeEtat typeEtat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdEtatRessource() {
    }

    public YvsProdEtatRessource(Integer id) {
        this.id = id;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public Double getCapaciteH() {
        return capaciteH != null ? capaciteH : 0.0;
    }

    public void setCapaciteH(Double capaciteH) {
        this.capaciteH = capaciteH;
    }

    public Double getCapaciteQ() {
        return capaciteQ != null ? capaciteQ : 0.0;
    }

    public void setCapaciteQ(Double capaciteQ) {
        this.capaciteQ = capaciteQ;
    }

    public Double getChargeH() {
        return chargeH != null ? chargeH : 0.0;
    }

    public void setChargeH(Double chargeH) {
        this.chargeH = chargeH;
    }

    public Double getChargeQ() {
        return chargeQ != null ? chargeQ : 0.0;
    }

    public void setChargeQ(Double chargeQ) {
        this.chargeQ = chargeQ;
    }

    public Double getTauxObsolescence() {
        return tauxObsolescence != null ? tauxObsolescence : 0.0;
    }

    public void setTauxObsolescence(Double tauxObsolescence) {
        this.tauxObsolescence = tauxObsolescence;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsProdPosteCharge getRessourceProduction() {
        return ressourceProduction;
    }

    public void setRessourceProduction(YvsProdPosteCharge ressourceProduction) {
        this.ressourceProduction = ressourceProduction;
    }

    public YvsBaseTypeEtat getTypeEtat() {
        return typeEtat;
    }

    public void setTypeEtat(YvsBaseTypeEtat typeEtat) {
        this.typeEtat = typeEtat;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsProdEtatRessource)) {
            return false;
        }
        YvsProdEtatRessource other = (YvsProdEtatRessource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdEtatRessource[ id=" + id + " ]";
    }

}
