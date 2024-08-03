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
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_serie_conge_technique")
@NamedQueries({
    @NamedQuery(name = "YvsGrhSerieCongeTechnique.findAll", query = "SELECT y FROM YvsGrhSerieCongeTechnique y"),
    @NamedQuery(name = "YvsGrhSerieCongeTechnique.findById", query = "SELECT y FROM YvsGrhSerieCongeTechnique y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhSerieCongeTechnique.findByTitre", query = "SELECT y FROM YvsGrhSerieCongeTechnique y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsGrhSerieCongeTechnique.findByDateDebut", query = "SELECT y FROM YvsGrhSerieCongeTechnique y WHERE y.dateDebut = :dateDebut")
})
public class YvsGrhSerieCongeTechnique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_serie_conge_technique_id_seq", name = "yvs_grh_serie_conge_technique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_serie_conge_technique_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre")
    private String titre;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "dat_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_conge_tech", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeChomage typeCongeTech;

    public YvsGrhSerieCongeTechnique() {
    }

    public YvsGrhSerieCongeTechnique(Long id) {
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhTypeChomage getTypeCongeTech() {
        return typeCongeTech;
    }

    public void setTypeCongeTech(YvsGrhTypeChomage typeCongeTech) {
        this.typeCongeTech = typeCongeTech;
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
        if (!(object instanceof YvsGrhSerieCongeTechnique)) {
            return false;
        }
        YvsGrhSerieCongeTechnique other = (YvsGrhSerieCongeTechnique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsGrhSerieCongeTechnique[ id=" + id + " ]";
    }

}
