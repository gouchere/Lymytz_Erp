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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_specialite_diplomes")
@NamedQueries({
    @NamedQuery(name = "YvsSpecialiteDiplomes.findAll", query = "SELECT DISTINCT y FROM YvsSpecialiteDiplomes y LEFT JOIN FETCH y.diplomes WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsSpecialiteDiplomes.findById", query = "SELECT y FROM YvsSpecialiteDiplomes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSpecialiteDiplomes.findByTitreSpecialite", query = "SELECT y FROM YvsSpecialiteDiplomes y WHERE y.titreSpecialite = :titreSpecialite")})
public class YvsSpecialiteDiplomes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_specialite_diplomes_id_seq", name = "yvs_specialite_diplomes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_specialite_diplomes_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "code_interne")
    private String codeInterne;
    @Size(max = 2147483647)
    @Column(name = "titre_specialite")
    private String titreSpecialite;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @OneToMany(mappedBy = "specialite")
    private List<YvsDiplomes> diplomes;

    public YvsSpecialiteDiplomes() {
    }

    public YvsSpecialiteDiplomes(Long id) {
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

    public String getTitreSpecialite() {
        return titreSpecialite;
    }

    public void setTitreSpecialite(String titreSpecialite) {
        this.titreSpecialite = titreSpecialite;
    }

    public List<YvsDiplomes> getDiplomes() {
        return diplomes;
    }

    public void setDiplomes(List<YvsDiplomes> diplomes) {
        this.diplomes = diplomes;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getCodeInterne() {
        return codeInterne;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
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
        if (!(object instanceof YvsSpecialiteDiplomes)) {
            return false;
        }
        YvsSpecialiteDiplomes other = (YvsSpecialiteDiplomes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsSpecialiteDiplomes[ id=" + id + " ]";
    }

}
