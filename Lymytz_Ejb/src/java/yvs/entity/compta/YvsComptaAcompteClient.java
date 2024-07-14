/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteClient;
import yvs.entity.compta.vente.YvsComptaPhaseAcompteVente;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_acompte_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaAcompteClient.findAll", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.client.tiers.societe = :societe ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.countAll", query = "SELECT COUNT(y) FROM YvsComptaAcompteClient y WHERE y.client.tiers.societe = :societe"),
    @NamedQuery(name = "YvsComptaAcompteClient.findById", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.id = :id ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByIds", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.id IN :ids ORDER BY y.montant ASC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByMontant", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.montant = :montant ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByDateAcompte", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.dateAcompte = :dateAcompte ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByNumRefrence", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.numRefrence = :numRefrence ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByCommentaire", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.commentaire = :commentaire ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findByNumPiece", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.numRefrence LIKE :numeroPiece AND y.client.tiers.societe=:societe ORDER BY y.numRefrence DESC"),

    @NamedQuery(name = "YvsComptaAcompteClient.findAvanceByModeDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.model = :model AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteClient.findAvanceByMode", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.model = :model AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByVendeurDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.caisse.responsable.codeUsers = :vendeur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByVendeur", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.caisse.responsable.codeUsers = :vendeur AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findByAcompteClientDates", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByAcompteClient", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAcompteByClientDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAcompteByClientDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.caisse.journal.agence = :agence AND y.client = :client AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAcompteByClient", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByClientDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findAvanceByClient", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = 'P'"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.countByParent", query = "SELECT COUNT(y) FROM YvsComptaCaissePieceVente y WHERE y.parent=:piece"),

    @NamedQuery(name = "YvsComptaAcompteClient.findByClient", query = "SELECT y FROM YvsComptaAcompteClient y WHERE y.client = :client ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteClient.findSumByClient", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteClient y WHERE y.client = :client AND y.statut = :statut")})
public class YvsComptaAcompteClient extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_acompte_client_id_seq", name = "yvs_compta_acompte_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_acompte_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_acompte")
    @Temporal(TemporalType.DATE)
    private Date dateAcompte;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    @Size(max = 2147483647)
    @Column(name = "num_refrence")
    private String numRefrence;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "statut_notif")
    private Character statutNotif;
    @Column(name = "repartir_automatique")
    private Boolean repartirAutomatique;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model = new YvsBaseModeReglement();
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient client;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "parent", referencedColumnName = "id")

    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaAcompteClient parent;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToOne(mappedBy = "acompte", fetch = FetchType.LAZY)
    private YvsComptaContentJournalAcompteClient pieceContenu;
    
    @OneToMany(mappedBy = "pieceVente", fetch = FetchType.LAZY)
    private List<YvsComptaPhaseAcompteVente> phasesReglement;

    @Transient
    private List<YvsComptaNotifReglementVente> notifs;
    @Transient
    private List<YvsComptaNotifReglementDocDivers> notifsDivers;

    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private Double reste, resteUnBind;

    public YvsComptaAcompteClient() {
        notifs = new ArrayList<>();
        notifsDivers = new ArrayList<>();
        phasesReglement = new ArrayList<>();
    }

    public YvsComptaAcompteClient(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getDatePaiement() {
        return datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getReferenceExterne() {
        return referenceExterne;
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getDateAcompte() {
        return dateAcompte != null ? dateAcompte : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateAcompte(Date dateAcompte) {
        this.dateAcompte = dateAcompte;
    }

    public String getNumRefrence() {
        return numRefrence != null ? numRefrence : "";
    }

    public void setNumRefrence(String numRefrence) {
        this.numRefrence = numRefrence;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getRepartirAutomatique() {
        return repartirAutomatique != null ? repartirAutomatique : false;
    }

    public void setRepartirAutomatique(Boolean repartirAutomatique) {
        this.repartirAutomatique = repartirAutomatique;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public Double getResteUnBind() {
        return resteUnBind != null ? resteUnBind : 0D;
    }

    public void setResteUnBind(Double resteUnBind) {
        this.resteUnBind = resteUnBind;
    }

    public Double getReste() {
        return reste != null ? reste : 0D;
    }

    public void setReste(Double reste) {
        this.reste = reste;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaNotifReglementVente> getNotifs() {
        return notifs;
    }

    public void setNotifs(List<YvsComptaNotifReglementVente> notifs) {
        this.notifs = notifs;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaNotifReglementDocDivers> getNotifsDivers() {
        return notifsDivers;
    }

    public void setNotifsDivers(List<YvsComptaNotifReglementDocDivers> notifsDivers) {
        this.notifsDivers = notifsDivers;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public Character getNature() {
        return nature != null ? String.valueOf(nature).trim().length() > 0 ? nature : 'A' : 'A';
    }

    public void setNature(Character nature) {
        this.nature = nature;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Character getStatutNotif() {
        return statutNotif != null ? String.valueOf(statutNotif).trim().length() > 0 ? statutNotif : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatutNotif(Character statutNotif) {
        this.statutNotif = statutNotif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<YvsComptaPhaseAcompteVente> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhaseAcompteVente> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalAcompteClient getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalAcompteClient pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaAcompteClient getParent() {
        return parent;
    }

    public void setParent(YvsComptaAcompteClient parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComptaAcompteClient)) {
            return false;
        }
        YvsComptaAcompteClient other = (YvsComptaAcompteClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaAcompteClient[ id=" + id + " ]";
    }

    public boolean IsLier() {
        if (notifs != null ? !notifs.isEmpty() : false) {
            return true;
        }
        return false;
    }

    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhaseAcompteVente vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

}
