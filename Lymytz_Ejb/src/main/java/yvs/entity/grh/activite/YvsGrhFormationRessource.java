/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

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
import yvs.entity.produits.YvsBaseArticles;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_formation_ressource")
@NamedQueries({
    @NamedQuery(name = "YvsGrhFormationRessource.findAll", query = "SELECT y FROM YvsGrhFormationRessource y"),
    @NamedQuery(name = "YvsGrhFormationRessource.findById", query = "SELECT y FROM YvsGrhFormationRessource y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhFormationRessource.findByQuantite", query = "SELECT y FROM YvsGrhFormationRessource y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsGrhFormationRessource.findBySupp", query = "SELECT y FROM YvsGrhFormationRessource y WHERE y.supp = :supp")})
public class YvsGrhFormationRessource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_formation_ressource_id_seq", name = "yvs_grh_formation_ressource_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_formation_ressource_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "quantite")
    private Integer quantite;
    @Column(name = "supp")
    private Boolean supp;
    @JoinColumn(name = "formation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormationEmps formation;
    @JoinColumn(name = "ressource", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseArticles ressource;

    public YvsGrhFormationRessource() {
    }

    public YvsGrhFormationRessource(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public YvsGrhFormationEmps getFormation() {
        return formation;
    }

    public void setFormation(YvsGrhFormationEmps formation) {
        this.formation = formation;
    }

    public YvsBaseArticles getRessource() {
        return ressource;
    }

    public void setRessource(YvsBaseArticles ressource) {
        this.ressource = ressource;
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
        if (!(object instanceof YvsGrhFormationRessource)) {
            return false;
        }
        YvsGrhFormationRessource other = (YvsGrhFormationRessource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhFormationRessource[ id=" + id + " ]";
    }

}
