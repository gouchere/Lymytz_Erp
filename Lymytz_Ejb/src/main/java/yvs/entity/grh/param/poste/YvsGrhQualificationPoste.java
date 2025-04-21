/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import yvs.entity.grh.param.YvsGrhQualifications;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_qualification_poste")
@NamedQueries({
    @NamedQuery(name = "YvsQualificationPoste.findAll", query = "SELECT y FROM YvsGrhQualificationPoste y"),
    @NamedQuery(name = "YvsQualificationPoste.findByPoste", query = "SELECT y FROM YvsGrhQualificationPoste y JOIN FETCH y.qualification WHERE y.poste=:poste"),
    @NamedQuery(name = "YvsQualificationPoste.findByDescription", query = "SELECT y FROM YvsGrhQualificationPoste y WHERE y.description = :description")})
public class YvsGrhQualificationPoste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(sequenceName = "yvs_qualification_poste_id_seq", name = "yvs_qualification_poste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_qualification_poste_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "supp")
    private Boolean supp;
    @JoinColumn(name = "poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPosteDeTravail poste;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "qualification", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhQualifications qualification;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhQualificationPoste() {
    }

    public YvsGrhQualificationPoste(Long id) {
        this.id = id;
    }

    public YvsGrhQualificationPoste(Long id, YvsUsersAgence author) {
        this.id = id;
        this.author = author;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YvsGrhQualifications getQualification() {
        return qualification;
    }

    public void setQualification(YvsGrhQualifications qualification) {
        this.qualification = qualification;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public YvsGrhPosteDeTravail getPoste() {
        return poste;
    }

    public void setPoste(YvsGrhPosteDeTravail poste) {
        this.poste = poste;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhQualificationPoste other = (YvsGrhQualificationPoste) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsQualificationPoste{" + "id=" + id + '}';
    }

}
