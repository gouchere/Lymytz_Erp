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
@Table(name = "yvs_msg_destinataire")
@NamedQueries({
    @NamedQuery(name = "YvsMsgDestinataire.findAll", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findById", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.id = :id AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByMessage", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.message = :conversation AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByCountAllAccuse", query = "SELECT count(y) FROM YvsMsgDestinataire y WHERE y.accuse = :accuse AND y.delete =false AND y.destinataire = :destinataire AND y.supp =false AND y.message.envoyer =true AND y.message.accuse = false"),
    @NamedQuery(name = "YvsMsgDestinataire.findAllAlert", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.accuse = :accuse AND y.delete =false AND y.destinataire = :destinataire AND y.supp =false AND y.message.envoyer =true AND y.message.priorite = 'SU'"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllAccuse", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.accuse = :accuse AND y.delete =false AND y.destinataire = :destinataire AND y.supp =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAccuse", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.accuse = :accuse AND y.delete =false AND y.destinataire = :destinataire AND y.supp =false AND y.message.envoyer =true AND y.groupeMessage is null ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDateReception", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.dateReception = :dateReception AND y.delete =false AND y.supp =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByHeureReception", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.heureReception = :heureReception AND y.delete =false AND y.supp =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findBySuppEmetteur", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.supp = :supp AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findBySuppRecepteur", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.supp = :supp AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByGroupeMessage", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.groupeMessage = :groupeMessage AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByCopie", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.copie = :copie AND y.supp =false AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByExterne", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.externe = :externe AND y.supp =false AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataire", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true AND y.groupeMessage is null ORDER BY y.message.dateEnvoi DESC,y.message.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDelete", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.message.dateEnvoi DESC,y.message.heureEnvoi DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAdresseExterne", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.adresseExterne = :adresseExterne AND y.supp =false AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataire", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByEmetteurDestinataireObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteur", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByDestinataireEmetteurObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.supp =:supp AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.contenu like :contenu AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataire", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false ORDER BY y.dateReception DESC,y.heureReception DESC"),

    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteur", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjet", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetContenu", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC"),
    @NamedQuery(name = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetContenuDateEnvoi", query = "SELECT y FROM YvsMsgDestinataire y WHERE y.destinataire = :destinataire AND y.message.emetteur = :emetteur AND y.message.objet = :objet AND y.message.contenu like :contenu AND y.message.dateEnvoi = :dateEnvoi AND y.delete =false AND y.message.envoyer =true ORDER BY y.dateReception DESC,y.heureReception DESC")})
public class YvsMsgDestinataire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_msg_destinataire_id_seq", name = "yvs_msg_destinataire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_msg_destinataire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "accuse")
    private Boolean accuse;
    @Column(name = "date_reception")
    @Temporal(TemporalType.DATE)
    private Date dateReception;
    @Column(name = "heure_reception")
    @Temporal(TemporalType.TIME)
    private Date heureReception;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "send_accuse")
    private Boolean sendAccuse;
    @Column(name = "copie")
    private Boolean copie;
    @Column(name = "externe")
    private Boolean externe;
    @Column(name = "delete")
    private Boolean delete;
    @Size(max = 2147483647)
    @Column(name = "adresse_externe")
    private String adresseExterne;
    @JoinColumn(name = "groupe_message", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgGroupeMessage groupeMessage;
    @JoinColumn(name = "destinataire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers destinataire;
    @JoinColumn(name = "message", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMsgConversation message;

    public YvsMsgDestinataire() {
    }

    public YvsMsgDestinataire(Integer id) {
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

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public YvsMsgGroupeMessage getGroupeMessage() {
        return groupeMessage;
    }

    public Boolean getSendAccuse() {
        return sendAccuse;
    }

    public void setSendAccuse(Boolean sendAccuse) {
        this.sendAccuse = sendAccuse;
    }

    public void setGroupeMessage(YvsMsgGroupeMessage groupeMessage) {
        this.groupeMessage = groupeMessage;
    }

    public Boolean getAccuse() {
        return accuse;
    }

    public void setAccuse(Boolean accuse) {
        this.accuse = accuse;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public Date getHeureReception() {
        return heureReception;
    }

    public void setHeureReception(Date heureReception) {
        this.heureReception = heureReception;
    }

    public Boolean getCopie() {
        return copie;
    }

    public void setCopie(Boolean copie) {
        this.copie = copie;
    }

    public Boolean getExterne() {
        return externe;
    }

    public void setExterne(Boolean externe) {
        this.externe = externe;
    }

    public String getAdresseExterne() {
        return adresseExterne;
    }

    public void setAdresseExterne(String adresseExterne) {
        this.adresseExterne = adresseExterne;
    }

    public YvsUsers getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(YvsUsers destinataire) {
        this.destinataire = destinataire;
    }

    public YvsMsgConversation getMessage() {
        return message;
    }

    public void setMessage(YvsMsgConversation message) {
        this.message = message;
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
        if (!(object instanceof YvsMsgDestinataire)) {
            return false;
        }
        YvsMsgDestinataire other = (YvsMsgDestinataire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.communication.YvsMsgDestinataire[ id=" + id + " ]";
    }

}
