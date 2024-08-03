/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.ext;

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
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_ext_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsExtFournisseur.findAll", query = "SELECT y FROM YvsExtFournisseur y"),
    @NamedQuery(name = "YvsExtFournisseur.findById", query = "SELECT y FROM YvsExtFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsExtFournisseur.findByCodeExterne", query = "SELECT y FROM YvsExtFournisseur y WHERE y.codeExterne = :codeExterne"),
    @NamedQuery(name = "YvsExtFournisseur.findByDateSave", query = "SELECT y FROM YvsExtFournisseur y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsExtFournisseur.findByFournisseur", query = "SELECT y FROM YvsExtFournisseur y WHERE y.fournisseur = :fournisseur"),
    @NamedQuery(name = "YvsExtFournisseur.findByDateUpdate", query = "SELECT y FROM YvsExtFournisseur y WHERE y.dateUpdate = :dateUpdate")})
public class YvsExtFournisseur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_ext_fournisseur_id_seq", name = "yvs_ext_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_ext_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_externe")
    private String codeExterne;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;

    public YvsExtFournisseur() {
    }

    public YvsExtFournisseur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeExterne() {
        return codeExterne;
    }

    public void setCodeExterne(String codeExterne) {
        this.codeExterne = codeExterne;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
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
        if (!(object instanceof YvsExtFournisseur)) {
            return false;
        }
        YvsExtFournisseur other = (YvsExtFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.ext.YvsExtFournisseur[ id=" + id + " ]";
    }
    
}
