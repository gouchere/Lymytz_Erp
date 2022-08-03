/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

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
@Table(name = "yvs_stat_grh_groupe_element")
@NamedQueries({
    @NamedQuery(name = "YvsStatGrhGroupeElement.findAll", query = "SELECT y FROM YvsStatGrhGroupeElement y"),
    @NamedQuery(name = "YvsStatGrhGroupeElement.findById", query = "SELECT y FROM YvsStatGrhGroupeElement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatGrhGroupeElement.findByLibelle", query = "SELECT y FROM YvsStatGrhGroupeElement y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsStatGrhGroupeElement.findByCodeGroupe", query = "SELECT y FROM YvsStatGrhGroupeElement y WHERE y.codeGroupe = :codeGroupe"),
    @NamedQuery(name = "YvsStatGrhGroupeElement.findByGroupeElement", query = "SELECT y FROM YvsStatGrhGroupeElement y WHERE y.groupeElement = :groupeElement")})
public class YvsStatGrhGroupeElement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_grh_groupe_element_id_seq", name = "yvs_stat_grh_groupe_element_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_grh_groupe_element_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "code_groupe")
    private String codeGroupe;
    @Column(name = "groupe_element")
    private Character groupeElement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsStatGrhGroupeElement() {
    }

    public YvsStatGrhGroupeElement(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCodeGroupe() {
        return codeGroupe;
    }

    public void setCodeGroupe(String codeGroupe) {
        this.codeGroupe = codeGroupe;
    }

    public Character getGroupeElement() {
        return groupeElement;
    }

    public void setGroupeElement(Character groupeElement) {
        this.groupeElement = groupeElement;
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
        if (!(object instanceof YvsStatGrhGroupeElement)) {
            return false;
        }
        YvsStatGrhGroupeElement other = (YvsStatGrhGroupeElement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsStatGrhGroupeElement[ id=" + id + " ]";
    }

}
