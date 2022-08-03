/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

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
import javax.validation.constraints.Size;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_element_statistique")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementStatistique.findAll", query = "SELECT y FROM YvsGrhElementStatistique y"),
    @NamedQuery(name = "YvsGrhElementStatistique.findByActif", query = "SELECT y FROM YvsGrhElementStatistique y WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhElementStatistique.findById", query = "SELECT y FROM YvsGrhElementStatistique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhElementStatistique.findByCode", query = "SELECT y FROM YvsGrhElementStatistique y WHERE y.code = :code")})
public class YvsGrhElementStatistique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "actif")
    private Boolean actif;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_element_statistique_id_seq", name = "yvs_grh_element_statistique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_element_statistique_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "code")
    private String code;
    @JoinColumn(name = "etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhDocStatistiques etat;
    @JoinColumn(name = "element_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementSalaire elementSalaire;

    public YvsGrhElementStatistique() {
    }

    public YvsGrhElementStatistique(Long id) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public YvsGrhDocStatistiques getEtat() {
        return etat;
    }

    public void setEtat(YvsGrhDocStatistiques etat) {
        this.etat = etat;
    }

    public YvsGrhElementSalaire getElementSalaire() {
        return elementSalaire;
    }

    public void setElementSalaire(YvsGrhElementSalaire elementSalaire) {
        this.elementSalaire = elementSalaire;
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
        if (!(object instanceof YvsGrhElementStatistique)) {
            return false;
        }
        YvsGrhElementStatistique other = (YvsGrhElementStatistique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsGrhElementStatistique[ id=" + id + " ]";
    }

}
