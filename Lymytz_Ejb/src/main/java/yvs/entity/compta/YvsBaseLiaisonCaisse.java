/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_liaison_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findAll", query = "SELECT y FROM YvsBaseLiaisonCaisse y"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findByActif", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findById", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findByCaisse", query = "SELECT y FROM YvsBaseLiaisonCaisse y WHERE y.caisseCible = :caisseCible AND y.caisseSource = :caisseSource"),
    @NamedQuery(name = "YvsBaseLiaisonCaisse.findCaisseCibleByCaisse", query = "SELECT DISTINCT y.caisseCible FROM YvsBaseLiaisonCaisse y WHERE y.caisseSource = :caisseSource")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseLiaisonCaisse extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "actif")
    private Boolean actif;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_liaison_caisse_id_seq", name = "yvs_base_liaison_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_liaison_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "caisse_source", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisseSource;
    @JoinColumn(name = "caisse_cible", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisseCible;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseLiaisonCaisse() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseLiaisonCaisse(Long id) {
        this();
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBaseCaisse getCaisseSource() {
        return caisseSource;
    }

    public void setCaisseSource(YvsBaseCaisse caisseSource) {
        this.caisseSource = caisseSource;
    }

    public YvsBaseCaisse getCaisseCible() {
        return caisseCible;
    }

    public void setCaisseCible(YvsBaseCaisse caisseCible) {
        this.caisseCible = caisseCible;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
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
        if (!(object instanceof YvsBaseLiaisonCaisse)) {
            return false;
        }
        YvsBaseLiaisonCaisse other = (YvsBaseLiaisonCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsBaseLiaisonCaisse[ id=" + id + " ]";
    }

}
