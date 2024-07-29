/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.communication;

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
import yvs.entity.users.YvsUsers;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_msg_conversation")
@NamedQueries({
    @NamedQuery(name = "YvsMsgConversation.findAll", query = "SELECT y FROM YvsMsgConversation y ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findById", query = "SELECT y FROM YvsMsgConversation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMsgConversation.findByObjet", query = "SELECT y FROM YvsMsgConversation y WHERE y.objet = :objet AND y.supp =false AND y.delet =false AND y.accuse =false"),
    @NamedQuery(name = "YvsMsgConversation.findByContenu", query = "SELECT y FROM YvsMsgConversation y WHERE y.contenu like :contenu AND y.supp =false AND y.delet =false AND y.accuse =false"),
    @NamedQuery(name = "YvsMsgConversation.findByDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.dateEnvoi = :dateEnvoi AND y.supp =false AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByHeureEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.heureEnvoi = :heureEnvoi AND y.supp =false AND y.delet =false AND y.accuse =false"),
    @NamedQuery(name = "YvsMsgConversation.findBySupp", query = "SELECT y FROM YvsMsgConversation y WHERE y.supp = :supp AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByReponse", query = "SELECT y FROM YvsMsgConversation y WHERE y.reponse = :reponse ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteur", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.supp =:supp AND y.delet =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEnvoyer", query = "SELECT y FROM YvsMsgConversation y WHERE y.envoyer = :envoyer AND y.emetteur = :emetteur AND y.supp =false AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByReference", query = "SELECT y FROM YvsMsgConversation y WHERE y.reference = :reference AND y.supp =false AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByPieceJointe", query = "SELECT y FROM YvsMsgConversation y WHERE y.pieceJointe = :pieceJointe AND y.supp =false AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByDelete", query = "SELECT y FROM YvsMsgConversation y WHERE y.delet = :delet"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurObjetContenu", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.contenu like :contenu AND y.objet = :objet AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurContenu", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.contenu like :contenu AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurObjet", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.objet = :objet AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurContenuDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.contenu like :contenu AND y.dateEnvoi = :dateEnvoi AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.dateEnvoi = :dateEnvoi AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurObjetDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.objet = :objet AND y.dateEnvoi = :dateEnvoi AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByAllEmetteurObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.contenu like :contenu AND y.objet = :objet AND y.dateEnvoi = :dateEnvoi AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),

    @NamedQuery(name = "YvsMsgConversation.findByEmetteurObjetContenu", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.contenu like :contenu AND y.objet = :objet AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurContenu", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.contenu like :contenu AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurObjet", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.objet = :objet AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurContenuDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.contenu like :contenu AND y.dateEnvoi = :dateEnvoi AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.dateEnvoi = :dateEnvoi AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurObjetDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.objet = :objet AND y.dateEnvoi = :dateEnvoi AND y.supp = :supp AND y.envoyer = :envoyer AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgConversation.findByEmetteurObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgConversation y WHERE y.emetteur = :emetteur AND y.envoyer = :envoyer AND y.contenu like :contenu AND y.objet = :objet AND y.dateEnvoi = :dateEnvoi AND y.supp = :supp AND y.delet =false AND y.accuse =false ORDER BY y.dateEnvoi DESC,y.heureEnvoi DESC")})
public class YvsMsgConversation implements Serializable {

    @Size(max = 2147483647)
    @Column(name = "adresse_externe")
    private String adresseExterne;
    @Column(name = "externe")
    private Boolean externe;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_conversation_id_seq", name = "yvs_conversation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_conversation_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "objet")
    private String objet;
    @Size(max = 2147483647)
    @Column(name = "contenu")
    private String contenu;
    @Column(name = "date_envoi")
    @Temporal(TemporalType.DATE)
    private Date dateEnvoi;
    @Column(name = "heure_envoi")
    @Temporal(TemporalType.TIME)
    private Date heureEnvoi;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "envoyer")
    private Boolean envoyer;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "piece_jointe")
    private String pieceJointe;
    @Size(max = 2147483647)
    @Column(name = "priorite")
    private String priorite;
    @Column(name = "delet")
    private Boolean delet;
    @Column(name = "accuse")
    private Boolean accuse;
    @JoinColumn(name = "emetteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers emetteur;
    @JoinColumn(name = "reponse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgConversation reponse;

    public YvsMsgConversation() {
    }

    public YvsMsgConversation(Integer id) {
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

    public Integer getId() {
        return id;
    }

    public Boolean getAccuse() {
        return accuse;
    }

    public void setAccuse(Boolean accuse) {
        this.accuse = accuse;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public String getPriorite() {
        return priorite;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Date getHeureEnvoi() {
        return heureEnvoi;
    }

    public void setHeureEnvoi(Date heureEnvoi) {
        this.heureEnvoi = heureEnvoi;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getEnvoyer() {
        return envoyer;
    }

    public void setEnvoyer(Boolean envoyer) {
        this.envoyer = envoyer;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Boolean getDelet() {
        return delet;
    }

    public void setDelet(Boolean delet) {
        this.delet = delet;
    }

    public YvsUsers getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(YvsUsers emetteur) {
        this.emetteur = emetteur;
    }

    public YvsMsgConversation getReponse() {
        return reponse;
    }

    public void setReponse(YvsMsgConversation reponse) {
        this.reponse = reponse;
    }

    public String getAdresseExterne() {
        return adresseExterne;
    }

    public void setAdresseExterne(String adresseExterne) {
        this.adresseExterne = adresseExterne;
    }

    public Boolean getExterne() {
        return externe;
    }

    public void setExterne(Boolean externe) {
        this.externe = externe;
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
        if (!(object instanceof YvsMsgConversation)) {
            return false;
        }
        YvsMsgConversation other = (YvsMsgConversation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsMsgConversation[ id=" + id + " ]";
    }

}
