/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_activite_type")
@NamedQueries({
    @NamedQuery(name = "YvsMutActiviteType.findAll", query = "SELECT y FROM YvsMutActiviteType y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutActiviteType.findByMutuelle", query = "SELECT y FROM YvsMutActiviteType y WHERE y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutActiviteType.findById", query = "SELECT y FROM YvsMutActiviteType y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutActiviteType.findByLibelle", query = "SELECT y FROM YvsMutActiviteType y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsMutActiviteType.findByDescription", query = "SELECT y FROM YvsMutActiviteType y WHERE y.description = :description")})
public class YvsMutActiviteType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_activite_type_id_seq", name = "yvs_mut_activite_type_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_activite_type_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "typeContibution")
    private List<YvsMutTauxContribution> contributions;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutActiviteType() {
        contributions = new ArrayList<>();
    }

    public YvsMutActiviteType(Long id) {
        this();
        this.id = id;
    }

    public YvsMutActiviteType(Long id, String libelle) {
        this(id);
        this.libelle = libelle;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsMutTauxContribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<YvsMutTauxContribution> contributions) {
        this.contributions = contributions;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
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
        if (!(object instanceof YvsMutActiviteType)) {
            return false;
        }
        YvsMutActiviteType other = (YvsMutActiviteType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutTypeContribution[ id=" + id + " ]";
    }

}
