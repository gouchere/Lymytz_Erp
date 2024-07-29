/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_type_evenement")
@NamedQueries({
    @NamedQuery(name = "YvsMutTypeEvenement.findAll", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutTypeEvenement.findById", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutTypeEvenement.findByMutuelle", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutTypeEvenement.findByDesignation", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.designation = :designation"),
    @NamedQuery(name = "YvsMutTypeEvenement.findByDescription", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.description = :description"),
    @NamedQuery(name = "YvsMutTypeEvenement.findByNombreParticipant", query = "SELECT y FROM YvsMutTypeEvenement y WHERE y.nombreParticipant = :nombreParticipant")})
public class YvsMutTypeEvenement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_type_evenement_id_seq", name = "yvs_mut_type_evenement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_type_evenement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "nombre_participant")
    private Integer nombreParticipant;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "lier_mutualiste")
    private Boolean lierMutualiste;
    @OneToMany(mappedBy = "typeEvenement")
    private List<YvsMutTauxContribution> contributions;
    @OneToMany(mappedBy = "type")
    private List<YvsMutEvenement> evenements;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutTypeEvenement() {
    }

    public YvsMutTypeEvenement(Long id) {
        this.id = id;
    }

    public YvsMutTypeEvenement(Long id, String designation) {
        this.id = id;
        this.designation = designation;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNombreParticipant() {
        return nombreParticipant;
    }

    public void setNombreParticipant(Integer nombreParticipant) {
        this.nombreParticipant = nombreParticipant;
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
        if (!(object instanceof YvsMutTypeEvenement)) {
            return false;
        }
        YvsMutTypeEvenement other = (YvsMutTypeEvenement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutTypeEvenement[ id=" + id + " ]";
    }

    public List<YvsMutEvenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<YvsMutEvenement> evenements) {
        this.evenements = evenements;
    }

    public Boolean getLierMutualiste() {
        return lierMutualiste;
    }

    public void setLierMutualiste(Boolean lierMutualiste) {
        this.lierMutualiste = lierMutualiste;
    }

    public List<YvsMutTauxContribution> getContributions() {
        return contributions;
    }

    public void setContributions(List<YvsMutTauxContribution> contributions) {
        this.contributions = contributions;
    }

}
