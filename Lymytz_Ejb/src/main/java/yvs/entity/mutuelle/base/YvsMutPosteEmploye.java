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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_poste_employe")
@NamedQueries({
    @NamedQuery(name = "YvsMutPosteEmploye.findAll", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutPosteEmploye.findById", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByDateOccupation", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.dateOccupation = :dateOccupation AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByOccupationSociete", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.poste.id IN :postes AND y.dateOccupation <= :date AND y.actif = true "
            + " ORDER BY y.mutualiste.employe.matricule, y.mutualiste.employe.nom,  y.mutualiste.employe.prenom"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByMutualistePoste", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste = :mutualiste AND y.poste = :poste"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByCurrentMutualiste", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste = :mutualiste AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByCurrentEmploye", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste.employe = :employe AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByCurrentUsers", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste.employe.codeUsers = :users AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByMutualisteActif", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.mutualiste = :mutualiste AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findIfCanVote", query = "SELECT y.poste.canVoteCredit FROM YvsMutPosteEmploye y WHERE y.mutualiste = :mutualiste AND y.actif =true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findByActif", query = "SELECT y FROM YvsMutPosteEmploye y WHERE y.actif = :actif"),

    @NamedQuery(name = "YvsMutPosteEmploye.findSizeCanVote", query = "SELECT COUNT(y) FROM YvsMutPosteEmploye y WHERE y.poste.canVoteCredit = true AND y.poste.mutuelle = :mutuelle AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findPosteByCurrentMutualiste", query = "SELECT y.poste FROM YvsMutPosteEmploye y WHERE y.mutualiste = :mutualiste AND y.actif = true"),
    @NamedQuery(name = "YvsMutPosteEmploye.findPosteByCurrentUsers", query = "SELECT y.poste FROM YvsMutPosteEmploye y WHERE y.mutualiste.employe.codeUsers = :users AND y.actif = true")})
public class YvsMutPosteEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_poste_employe_id_seq", name = "yvs_mut_poste_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_poste_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_occupation")
    @Temporal(TemporalType.DATE)
    private Date dateOccupation;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "poste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutPoste poste;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsMutPosteEmploye() {
    }

    public YvsMutPosteEmploye(Long id) {
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

    public Date getDateOccupation() {
        return dateOccupation;
    }

    public void setDateOccupation(Date dateOccupation) {
        this.dateOccupation = dateOccupation;
    }

    public Boolean getActif() {
        return actif!=null?actif:false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsMutPoste getPoste() {
        return poste;
    }

    public void setPoste(YvsMutPoste poste) {
        this.poste = poste;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
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
        if (!(object instanceof YvsMutPosteEmploye)) {
            return false;
        }
        YvsMutPosteEmploye other = (YvsMutPosteEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutPosteEmploye[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
