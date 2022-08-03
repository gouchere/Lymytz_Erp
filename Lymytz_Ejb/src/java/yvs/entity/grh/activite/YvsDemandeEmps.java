/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

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
import yvs.entity.grh.personnel.YvsGrhEmployes;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_demande_emps")
@NamedQueries({
    @NamedQuery(name = "YvsDemandeEmps.findAll", query = "SELECT y FROM YvsDemandeEmps y WHERE y.emetteur.agence = :agence"),
    @NamedQuery(name = "YvsDemandeEmps.findById", query = "SELECT y FROM YvsDemandeEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsDemandeEmps.findByDateDemande", query = "SELECT y FROM YvsDemandeEmps y WHERE y.dateDemande = :dateDemande"),
    @NamedQuery(name = "YvsDemandeEmps.findByObjet", query = "SELECT y FROM YvsDemandeEmps y WHERE y.objet = :objet"),
    @NamedQuery(name = "YvsDemandeEmps.findByMessage", query = "SELECT y FROM YvsDemandeEmps y WHERE y.message = :message"),
    @NamedQuery(name = "YvsDemandeEmps.findByAccuse", query = "SELECT y FROM YvsDemandeEmps y WHERE y.accuse = :accuse"),
    @NamedQuery(name = "YvsDemandeEmps.findByReponse", query = "SELECT y FROM YvsDemandeEmps y WHERE y.reponse = :reponse"),
    @NamedQuery(name = "YvsDemandeEmps.findByPieceJointe", query = "SELECT y FROM YvsDemandeEmps y WHERE y.pieceJointe = :pieceJointe"),
    @NamedQuery(name = "YvsDemandeEmps.findByDateDebut", query = "SELECT y FROM YvsDemandeEmps y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsDemandeEmps.findByDateFin", query = "SELECT y FROM YvsDemandeEmps y WHERE y.dateFin = :dateFin")})
public class YvsDemandeEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_demande_id_seq", name = "yvs_demande_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_demande_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_demande")
    @Temporal(TemporalType.DATE)
    private Date dateDemande;
    @Size(max = 2147483647)
    @Column(name = "objet")
    private String objet;
    @Size(max = 2147483647)
    @Column(name = "message")
    private String message;
    @Column(name = "accuse")
    private Boolean accuse;
    @Size(max = 2147483647)
    @Column(name = "reponse")
    private String reponse;
    @Size(max = 2147483647)
    @Column(name = "piece_jointe")
    private String pieceJointe;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @JoinColumn(name = "type_demande", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsTypeDemande typeDemande;
    @JoinColumn(name = "emetteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes emetteur;
    @JoinColumn(name = "destinataire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes destinataire;

    public YvsDemandeEmps() {
    }

    public YvsDemandeEmps(Long id) {
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

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getAccuse() {
        return accuse;
    }

    public void setAccuse(Boolean accuse) {
        this.accuse = accuse;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public YvsTypeDemande getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(YvsTypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }

    public YvsGrhEmployes getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(YvsGrhEmployes emetteur) {
        this.emetteur = emetteur;
    }

    public YvsGrhEmployes getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(YvsGrhEmployes destinataire) {
        this.destinataire = destinataire;
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
        if (!(object instanceof YvsDemandeEmps)) {
            return false;
        }
        YvsDemandeEmps other = (YvsDemandeEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.dao.YvsDemandeEmps[ id=" + id + " ]";
    }

}
