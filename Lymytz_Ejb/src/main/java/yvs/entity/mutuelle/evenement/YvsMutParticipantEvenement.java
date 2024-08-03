/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.evenement;

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
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_participant_evenement")
@NamedQueries({
    @NamedQuery(name = "YvsMutParticipantEvenement.findAll", query = "SELECT y FROM YvsMutParticipantEvenement y"),
    @NamedQuery(name = "YvsMutParticipantEvenement.findById", query = "SELECT y FROM YvsMutParticipantEvenement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutParticipantEvenement.findByActivite", query = "SELECT y FROM YvsMutParticipantEvenement y WHERE y.activite = :activite"),
    @NamedQuery(name = "YvsMutParticipantEvenement.findByEvenement", query = "SELECT y FROM YvsMutParticipantEvenement y WHERE y.activite.evenement = :evenement")})
public class YvsMutParticipantEvenement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_participant_evenement_id_seq", name = "yvs_mut_participant_evenement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_participant_evenement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "role_membre")
    private String roleMembre;
    @Column(name = "organisateur")
    private Boolean organisateur;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "activite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutActivite activite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutParticipantEvenement() {
    }

    public YvsMutParticipantEvenement(Long id) {
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getRoleMembre() {
        return roleMembre;
    }

    public void setRoleMembre(String roleMembre) {
        this.roleMembre = roleMembre;
    }

    public Boolean getOrganisateur() {
        return organisateur != null ? organisateur : false;
    }

    public void setOrganisateur(Boolean organisateur) {
        this.organisateur = organisateur;
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

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public YvsMutActivite getActivite() {
        return activite;
    }

    public void setActivite(YvsMutActivite activite) {
        this.activite = activite;
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
        if (!(object instanceof YvsMutParticipantEvenement)) {
            return false;
        }
        YvsMutParticipantEvenement other = (YvsMutParticipantEvenement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.evenement.YvsMutParticipantEvenement[ id=" + id + " ]";
    }

}
