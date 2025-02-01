/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.creneau;

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
import yvs.entity.param.YvsSocietes;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_type_creneau_horaire")
@NamedQueries({
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findAll", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.societe = :societe ORDER BY y.heureDebut"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findByCritere", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.societe = :societe AND y.critere = :critere ORDER BY y.heureDebut"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findById", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findByReference", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findByHeureFin", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findByHeureDebut", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsComTypeCreneauHoraire.findByActif", query = "SELECT y FROM YvsComTypeCreneauHoraire y WHERE y.actif = :actif")})
public class YvsComTypeCreneauHoraire implements Serializable {

//    @OneToMany(mappedBy = "typeCreno")
//    private List<YvsComCreneauDepot> yvsComCreneauList;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_type_creneau_horaire_id_seq", name = "yvs_com_type_creneau_horaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_type_creneau_horaire_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Column(name = "critere")
    private String critere;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsComTypeCreneauHoraire() {
    }

    public YvsComTypeCreneauHoraire(Long id) {
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

    public String getCritere() {
        return critere;
    }

    public void setCritere(String critere) {
        this.critere = critere;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsComTypeCreneauHoraire)) {
            return false;
        }
        YvsComTypeCreneauHoraire other = (YvsComTypeCreneauHoraire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComTypeCreneauHoraire[ id=" + id + " ]";
    }
//
//    public List<YvsComCreneauDepot> getYvsComCreneauList() {
//        return yvsComCreneauList;
//    }
//
//    public void setYvsComCreneauList(List<YvsComCreneauDepot> yvsComCreneauList) {
//        this.yvsComCreneauList = yvsComCreneauList;
//    }

}
