/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

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
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_domaines_qualifications")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDomainesQualifications.findAll", query = "SELECT DISTINCT y FROM YvsGrhDomainesQualifications y LEFT JOIN FETCH y.qualifications WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsDomainesQualifications.findById", query = "SELECT y FROM YvsGrhDomainesQualifications y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDomainesQualifications.findByTitreDomaine", query = "SELECT y FROM YvsGrhDomainesQualifications y WHERE y.titreDomaine = :titreDomaine")})
public class YvsGrhDomainesQualifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_domaines_qualifications_id_seq", name = "yvs_domaines_qualifications_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_domaines_qualifications_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre_domaine")
    private String titreDomaine;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "domaine")
    private List<YvsGrhQualifications> qualifications;
    
    public YvsGrhDomainesQualifications() {
    }

    public YvsGrhDomainesQualifications(Long id) {
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

    public String getTitreDomaine() {
        return titreDomaine;
    }

    public void setTitreDomaine(String titreDomaine) {
        this.titreDomaine = titreDomaine;
    }

    public List<YvsGrhQualifications> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<YvsGrhQualifications> qualifications) {
        this.qualifications = qualifications;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsGrhDomainesQualifications)) {
            return false;
        }
        YvsGrhDomainesQualifications other = (YvsGrhDomainesQualifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.YvsDomainesQualifications[ id=" + id + " ]";
    }

}
