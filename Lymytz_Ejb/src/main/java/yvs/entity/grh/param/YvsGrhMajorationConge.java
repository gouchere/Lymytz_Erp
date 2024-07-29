/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import yvs.entity.grh.taches.YvsGrhIntervalMajoration;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_majoration_conge")
@NamedQueries({
    @NamedQuery(name = "YvsMajorationConge.findAll", query = "SELECT y FROM YvsGrhMajorationConge y WHERE y.societe =:societe"),
    @NamedQuery(name = "YvsMajorationConge.findById", query = "SELECT y FROM YvsGrhMajorationConge y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMajorationConge.countBySociete", query = "SELECT COUNT(y) FROM YvsGrhMajorationConge y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsMajorationConge.findByNatureSociete", query = "SELECT y FROM YvsGrhMajorationConge y WHERE y.nature = :nature AND y.societe = :societe"),
    @NamedQuery(name = "YvsMajorationConge.findByNature", query = "SELECT y FROM YvsGrhMajorationConge y WHERE y.nature = :nature"),
    @NamedQuery(name = "YvsMajorationConge.findByActif", query = "SELECT y FROM YvsGrhMajorationConge y WHERE y.actif = :actif")})
public class YvsGrhMajorationConge implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_majoration_conge_id_seq", name = "yvs_majoration_conge_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_majoration_conge_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "nature")
    private String nature;
    @Column(name = "actif")
    private Boolean actif;
    @OneToMany(mappedBy = "majorationConge")
    private List<YvsGrhIntervalMajoration> listIntervalle;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "unite_intervale")
    private String uniteIntervalle;

    public YvsGrhMajorationConge() {
    }

    public YvsGrhMajorationConge(Long id) {
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

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public List<YvsGrhIntervalMajoration> getListIntervalle() {
        return listIntervalle;
    }

    public void setListIntervalle(List<YvsGrhIntervalMajoration> listIntervalle) {
        this.listIntervalle = listIntervalle;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getUniteIntervalle() {
        return uniteIntervalle;
    }

    public void setUniteIntervalle(String uniteIntervalle) {
        this.uniteIntervalle = uniteIntervalle;
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
        if (!(object instanceof YvsGrhMajorationConge)) {
            return false;
        }
        YvsGrhMajorationConge other = (YvsGrhMajorationConge) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsMajorationConge[ id=" + id + " ]";
    }

}
