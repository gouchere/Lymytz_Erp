/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.achat;

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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaNotifReglementDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalAcompteFournisseur;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_acompte_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAll", query = "SELECT y FROM YvsComptaAcompteFournisseur y"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.countAll", query = "SELECT COUNT(y) FROM YvsComptaAcompteFournisseur y"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findById", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByIds", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.id IN :ids ORDER BY y.montant ASC"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByMontant", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByDateAcompte", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.dateAcompte = :dateAcompte"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByNumRefrence", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.numRefrence = :numRefrence"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByCommentaire", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.commentaire = :commentaire"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByStatut", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByDateUpdate", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByDateSave", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByNumPiece", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.numRefrence LIKE :numeroPiece AND y.fournisseur.tiers.societe=:societe ORDER BY y.numRefrence DESC"),

    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByModeDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.model = :model AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByMode", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.model = :model AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByVendeurDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.caisse.responsable.codeUsers = :vendeur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByVendeur", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.caisse.responsable.codeUsers = :vendeur AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByAcompteFournisseurDates", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByAcompteFournisseur", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P'"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAcompteByFournisseurDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAcompteByFournisseurDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.caisse.journal.agence = :agence AND y.fournisseur = :fournisseur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAcompteByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByFournisseurDates", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P' AND y.dateAcompte BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findAvanceByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = 'P'"),

    @NamedQuery(name = "YvsComptaAcompteFournisseur.findByFournisseur", query = "SELECT y FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur ORDER BY y.dateAcompte DESC"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.countByParent", query = "SELECT COUNT(y) FROM YvsComptaAcompteFournisseur y WHERE y.parent=:piece"),
    @NamedQuery(name = "YvsComptaAcompteFournisseur.findSumByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaAcompteFournisseur y WHERE y.fournisseur = :fournisseur AND y.statut = :statut")})
public class YvsComptaAcompteFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_acompte_fournisseur_id_seq", name = "yvs_compta_acompte_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_acompte_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "statut")
    private Character statut;
    @Column(name = "nature")
    private Character nature;
    @Column(name = "statut_notif")
    private Character statutNotif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "repartir_automatique")
    private Boolean repartirAutomatique;
    @Column(name = "comptabilise")
    private Boolean comptabilise;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaAcompteFournisseur parent;

    @OneToOne(mappedBy = "acompte", fetch = FetchType.LAZY)
    private YvsComptaContentJournalAcompteFournisseur pieceContenu;

    @OneToMany(mappedBy = "acompte",fetch = FetchType.LAZY)
    private List<YvsComptaNotifReglementAchat> notifs;
    @OneToMany(mappedBy = "acompteFournisseur", fetch = FetchType.LAZY)
    private List<YvsComptaNotifReglementDocDivers> yvsComptaNotifReglementDocDiversList;
    @OneToMany(mappedBy = "pieceAchat", fetch = FetchType.LAZY)
    private List<YvsComptaPhaseAcompteAchat> phasesReglement;
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
    private double reste, resteUnBind;

    public YvsComptaAcompteFournisseur() {
        notifs = new ArrayList<>();
        phasesReglement = new ArrayList<>();
    }

    public YvsComptaAcompteFournisseur(Long id) {
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

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
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

    public Date getDateAcompte() {
        return dateAcompte != null ? dateAcompte : new Date();
    }

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

    public Boolean getRepartirAutomatique() {
        return repartirAutomatique != null ? repartirAutomatique : false;
    }

    public void setRepartirAutomatique(Boolean repartirAutomatique) {
        this.repartirAutomatique = repartirAutomatique;
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

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public List<YvsComptaNotifReglementAchat> getNotifs() {
        return notifs;
    }

    public void setNotifs(List<YvsComptaNotifReglementAchat> notifs) {
        this.notifs = notifs;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public double getResteUnBind() {
        resteUnBind = getMontant();
        for (YvsComptaNotifReglementAchat r : notifs) {
            resteUnBind -= r.getPieceAchat().getMontant();
        }
        return resteUnBind;
    }

    public void setResteUnBind(double resteUnBind) {
        this.resteUnBind = resteUnBind;
    }

    public double getReste() {
        reste = getMontant();
        for (YvsComptaNotifReglementAchat r : notifs) {
            if (r.getPieceAchat().getStatutPiece().equals(Constantes.STATUT_DOC_PAYER)) {
                reste -= r.getPieceAchat().getMontant();
            }
        }
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
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

    public List<YvsComptaPhaseAcompteAchat> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhaseAcompteAchat> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    public YvsComptaContentJournalAcompteFournisseur getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalAcompteFournisseur pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaAcompteFournisseur getParent() {
        return parent;
    }

    public void setParent(YvsComptaAcompteFournisseur parent) {
        this.parent = parent;
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
        if (!(object instanceof YvsComptaAcompteFournisseur)) {
            return false;
        }
        YvsComptaAcompteFournisseur other = (YvsComptaAcompteFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.achat.YvsComptaAcompteFournisseur[ id=" + id + " ]";
    }

    public boolean IsLier() {
        if (notifs != null ? !notifs.isEmpty() : false) {
            return true;
        }
        return false;
    }

    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhaseAcompteAchat vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

    public double getResteUnBind(long idPiece) {
        resteUnBind = getMontant();
        for (YvsComptaNotifReglementAchat r : notifs) {
            if (r.getPieceAchat().getId() != idPiece) {
                resteUnBind -= r.getPieceAchat().getMontant();
            }
        }
        return resteUnBind;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaNotifReglementDocDivers> getYvsComptaNotifReglementDocDiversList() {
        return yvsComptaNotifReglementDocDiversList;
    }

    public void setYvsComptaNotifReglementDocDiversList(List<YvsComptaNotifReglementDocDivers> yvsComptaNotifReglementDocDiversList) {
        this.yvsComptaNotifReglementDocDiversList = yvsComptaNotifReglementDocDiversList;
    }

}
