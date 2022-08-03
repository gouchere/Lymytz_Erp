/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_categorie_pro_employe")
@NamedQueries({
    @NamedQuery(name = "YvsCategorieProEmploye.findAll", query = "SELECT y FROM YvsCategorieProEmploye y"),
    @NamedQuery(name = "YvsCategorieProEmploye.findById", query = "SELECT y FROM YvsCategorieProEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsCategorieProEmploye.findByDateChange", query = "SELECT y FROM YvsCategorieProEmploye y WHERE y.dateChange = :dateChange"),
    @NamedQuery(name = "YvsCategorieProEmploye.findByEmploye", query = "SELECT y FROM YvsCategorieProEmploye y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsCategorieProEmploye.countC", query = "SELECT COUNT (y) FROM YvsCategorieProEmploye y WHERE y.employe = :employe AND y.actif=true AND y.categorie.id=:categorie"),
    @NamedQuery(name = "YvsCategorieProEmploye.findByActif", query = "SELECT y FROM YvsCategorieProEmploye y WHERE y.actif = :actif")})
public class YvsCategorieProEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorie_pro_employe_id_seq")
    @SequenceGenerator(sequenceName = "categorie_pro_employe_id_seq", allocationSize = 1, name = "categorie_pro_employe_id_seq")
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_change")
    @Temporal(TemporalType.DATE)
    private Date dateChange;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCategorieProfessionelle categorie;

    public YvsCategorieProEmploye() {
    }

    public YvsCategorieProEmploye(Long id) {
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

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsCategorieProEmploye)) {
            return false;
        }
        YvsCategorieProEmploye other = (YvsCategorieProEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsCategorieProEmploye[ id=" + id + " ]";
    }

}
