/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_base_radical_compte")
@NamedQueries({
    @NamedQuery(name = "YvsBaseRadicalCompte.findAll", query = "SELECT y FROM YvsBaseRadicalCompte y"),
    @NamedQuery(name = "YvsBaseRadicalCompte.findAllBySociete", query = "SELECT y FROM YvsBaseRadicalCompte y WHERE y.natureCompte.societe=:societe AND y.natureCompte.actif=true"),
    @NamedQuery(name = "YvsBaseRadicalCompte.findById", query = "SELECT y FROM YvsBaseRadicalCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseRadicalCompte.findByRadical", query = "SELECT y FROM YvsBaseRadicalCompte y WHERE y.radical = :radical"),
    @NamedQuery(name = "YvsBaseRadicalCompte.findNatureByRadical", query = "SELECT DISTINCT y.natureCompte FROM YvsBaseRadicalCompte y WHERE y.natureCompte.societe = :societe AND y.radical = :radical"),
    @NamedQuery(name = "YvsBaseRadicalCompte.findNatureLikeRadical", query = "SELECT DISTINCT y.natureCompte FROM YvsBaseRadicalCompte y WHERE y.natureCompte.societe = :societe AND y.radical LIKE :radical")})
public class YvsBaseRadicalCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_base_radical_compte_id_seq")
    @SequenceGenerator(sequenceName = "yvs_base_radical_compte_id_seq", allocationSize = 1, name = "yvs_base_radical_compte_id_seq")
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "radical")
    private String radical;
    @JoinColumn(name = "nature", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseNatureCompte natureCompte;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsBaseRadicalCompte() {
    }

    public YvsBaseRadicalCompte(Long id) {
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

    public String getRadical() {
        return radical;
    }

    public void setRadical(String radical) {
        this.radical = radical;
    }

    public YvsBaseNatureCompte getNatureCompte() {
        return natureCompte;
    }

    public void setNatureCompte(YvsBaseNatureCompte natureCompte) {
        this.natureCompte = natureCompte;
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
        if (!(object instanceof YvsBaseRadicalCompte)) {
            return false;
        }
        YvsBaseRadicalCompte other = (YvsBaseRadicalCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsRadicalCompte[ id=" + id + " ]";
    }
}
