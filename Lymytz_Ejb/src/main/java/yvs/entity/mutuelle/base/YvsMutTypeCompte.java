/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_type_compte")
@NamedQueries({
    @NamedQuery(name = "YvsMutTypeCompte.findAll", query = "SELECT y FROM YvsMutTypeCompte y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutTypeCompte.findById", query = "SELECT y FROM YvsMutTypeCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTypeCompte.findByLibelle", query = "SELECT y FROM YvsMutTypeCompte y WHERE y.libelle = :libelle AND y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutTypeCompte.findByMutuelle", query = "SELECT y FROM YvsMutTypeCompte y WHERE y.mutuelle = :mutuelle AND y.typeCaisse = :typeCaisse"),
    @NamedQuery(name = "YvsMutTypeCompte.findByType", query = "SELECT y FROM YvsMutTypeCompte y WHERE y.nature = :type AND y.mutuelle = :mutuelle")})
public class YvsMutTypeCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_type_compte_id_seq", name = "yvs_mut_type_compte_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_type_compte_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "type_compte")
    private String nature;
    @Column(name = "type_caisse")
    private Boolean typeCaisse;
    
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutTypeCompte() {
    }

    public YvsMutTypeCompte(Long id) {
        this();
        this.id = id;
    }

    public YvsMutTypeCompte(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
    }
    public YvsMutTypeCompte(Long id, String libelle, String nature) {
        this(id,libelle);
        this.nature = nature;
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

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public Boolean getTypeCaisse() {
        return typeCaisse;
    }

    public void setTypeCaisse(Boolean typeCaisse) {
        this.typeCaisse = typeCaisse;
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

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
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
        if (!(object instanceof YvsMutTypeCompte)) {
            return false;
        }
        YvsMutTypeCompte other = (YvsMutTypeCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutTypeCompte[ id=" + id + " ]";
    }

}
