/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_categorie_preavis")
@NamedQueries({
    @NamedQuery(name = "YvsGrhCategoriePreavis.findAll", query = "SELECT y FROM YvsGrhCategoriePreavis y"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findById", query = "SELECT y FROM YvsGrhCategoriePreavis y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findByCategorie", query = "SELECT y FROM YvsGrhCategoriePreavis y JOIN FETCH y.anciennete WHERE y.categorie = :categorie"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findByPreavis", query = "SELECT y FROM YvsGrhCategoriePreavis y WHERE y.preavis = :preavis"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findByIntervalle", query = "SELECT y FROM YvsGrhCategoriePreavis y WHERE y.anciennete.ancienneteMin<=:anciennete AND y.anciennete.ancienneteMax>:anciennete AND y.categorie=:categorie"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findPreavis", query = "SELECT y FROM YvsGrhCategoriePreavis y WHERE y.categorie = :categorie AND y.anciennete.ancienneteMin<=:ancienete AND y.anciennete.ancienneteMax>:ancienete"),
    @NamedQuery(name = "YvsGrhCategoriePreavis.findByUnitePreavis", query = "SELECT y FROM YvsGrhCategoriePreavis y WHERE y.unitePreavis = :unitePreavis")})
public class YvsGrhCategoriePreavis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_categorie_preavis_id_seq", name = "yvs_grh_categorie_preavis_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_categorie_preavis_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "preavis")
    private int preavis;
    @Size(max = 2147483647)
    @Column(name = "unite_preavis")
    private String unitePreavis;
    @JoinColumn(name = "anciennete", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhIntervalleAnciennete anciennete;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCategorieProfessionelle categorie;

    public YvsGrhCategoriePreavis() {
    }

    public YvsGrhCategoriePreavis(Integer id) {
        this.id = id;
    }

    public YvsGrhCategoriePreavis(Integer id, int preavis) {
        this.id = id;
        this.preavis = preavis;
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

    public int getPreavis() {
        return preavis;
    }

    public void setPreavis(int preavis) {
        this.preavis = preavis;
    }

    public String getUnitePreavis() {
        return unitePreavis;
    }

    public void setUnitePreavis(String unitePreavis) {
        this.unitePreavis = unitePreavis;
    }

    public YvsGrhIntervalleAnciennete getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(YvsGrhIntervalleAnciennete anciennete) {
        this.anciennete = anciennete;
    }

    public YvsGrhCategorieProfessionelle getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsGrhCategorieProfessionelle categorie) {
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
        if (!(object instanceof YvsGrhCategoriePreavis)) {
            return false;
        }
        YvsGrhCategoriePreavis other = (YvsGrhCategoriePreavis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhCategoriePreavis[ id=" + id + " ]";
    }

}
