/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_type_assurance")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeAssurance.findAll", query = "SELECT y FROM YvsGrhTypeAssurance y ORDER BY y.assureur.nom ASC"),
    @NamedQuery(name = "YvsGrhTypeAssurance.findById", query = "SELECT y FROM YvsGrhTypeAssurance y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeAssurance.findByLibelle", query = "SELECT y FROM YvsGrhTypeAssurance y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhTypeAssurance.findByDescription", query = "SELECT y FROM YvsGrhTypeAssurance y WHERE y.description = :description")})
public class YvsGrhTypeAssurance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_assurance_id_seq", name = "yvs_grh_type_assurance_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_assurance_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "assureur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhAssureur assureur;

    public YvsGrhTypeAssurance() {
    }

    public YvsGrhTypeAssurance(Integer id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsGrhAssureur getAssureur() {
        return assureur;
    }

    public void setAssureur(YvsGrhAssureur assureur) {
        this.assureur = assureur;
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
        if (!(object instanceof YvsGrhTypeAssurance)) {
            return false;
        }
        YvsGrhTypeAssurance other = (YvsGrhTypeAssurance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhTypeAssurance[ id=" + id + " ]";
    }

}
