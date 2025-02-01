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
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditFournisseur;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_credit_fournisseur")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCreditFournisseur.findAll", query = "SELECT y FROM YvsComptaCreditFournisseur y ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findById", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.id = :id ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByIds", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.id IN :ids ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByNumReference", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.numReference = :numReference ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByNumPiece", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.numReference LIKE :numeroPiece AND y.fournisseur.tiers.societe = :societe ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByMotif", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.motif = :motif ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByDateCredit", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.dateCredit = :dateCredit ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByMontant", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.montant = :montant ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByDateUpdate", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.dateUpdate = :dateUpdate ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByDateSave", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.dateSave = :dateSave ORDER BY y.dateCredit DESC"),

    @NamedQuery(name = "YvsComptaCreditFournisseur.findByCreanceFournisseurDates", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur AND y.dateCredit BETWEEN :dateDebut AND :dateFin ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findByCreanceFournisseur", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findCreanceByFournisseurDates", query = "SELECT SUM(y.montant) FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findCreanceByFournisseurDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaCreditFournisseur y WHERE y.journal.agence = :agence AND y.fournisseur = :fournisseur AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findCreanceByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur"),

    @NamedQuery(name = "YvsComptaCreditFournisseur.findByFournisseur", query = "SELECT y FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditFournisseur.findSumByFournisseur", query = "SELECT SUM(y.montant) FROM YvsComptaCreditFournisseur y WHERE y.fournisseur = :fournisseur")})
public class YvsComptaCreditFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_credit_fournisseur_id_seq", name = "yvs_compta_credit_fournisseur_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_credit_fournisseur_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "num_reference")
    private String numReference;
    @Size(max = 2147483647)
    @Column(name = "motif")
    private String motif;
    @Column(name = "date_credit")
    @Temporal(TemporalType.DATE)
    private Date dateCredit;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseFournisseur fournisseur;
    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;
    @JoinColumn(name = "type_credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCredit;

    @OneToOne(mappedBy = "credit")
    private YvsComptaContentJournalCreditFournisseur pieceContenu;

    @OneToMany(mappedBy = "credit")
    private List<YvsComptaReglementCreditFournisseur> reglements;

    @Transient
    private char statutPaiement = Constantes.STATUT_DOC_ATTENTE;
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

    public YvsComptaCreditFournisseur() {
        reglements = new ArrayList<>();
    }

    public YvsComptaCreditFournisseur(Long id) {
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

    public String getNumReference() {
        return numReference != null ? numReference : "";
    }

    public void setNumReference(String numReference) {
        this.numReference = numReference;
    }

    public String getMotif() {
        return motif != null ? motif : "";
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateCredit() {
        return dateCredit != null ? dateCredit : new Date();
    }

    public void setDateCredit(Date dateCredit) {
        this.dateCredit = dateCredit;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
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

    public YvsBaseFournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(YvsBaseFournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public YvsGrhTypeCout getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(YvsGrhTypeCout typeCredit) {
        this.typeCredit = typeCredit;
    }

    public List<YvsComptaReglementCreditFournisseur> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaReglementCreditFournisseur> reglements) {
        this.reglements = reglements;
    }

    public char getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public double getResteUnBind() {
        resteUnBind = getMontant();
        for (YvsComptaReglementCreditFournisseur r : reglements) {
            resteUnBind -= r.getValeur();
        }
        return resteUnBind;
    }

    public double getResteUnBind(long idPiece) {
        resteUnBind = getMontant();
        for (YvsComptaReglementCreditFournisseur r : reglements) {
            if (r.getId() != idPiece) {
                resteUnBind -= r.getValeur();
            }
        }
        return resteUnBind;
    }

    public void setResteUnBind(double resteUnBind) {
        this.resteUnBind = resteUnBind;
    }

    public double getReste() {
        reste = getMontant();
        for (YvsComptaReglementCreditFournisseur r : reglements) {
            if (r.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                reste -= r.getValeur();
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

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalCreditFournisseur getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalCreditFournisseur pieceContenu) {
        this.pieceContenu = pieceContenu;
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
        if (!(object instanceof YvsComptaCreditFournisseur)) {
            return false;
        }
        YvsComptaCreditFournisseur other = (YvsComptaCreditFournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.achat.YvsComptaCreditFournisseur[ id=" + id + " ]";
    }

    public double creance() {
        resteUnBind = getMontant();
        reste = getMontant();
        statutPaiement = Constantes.STATUT_DOC_ATTENTE;
        for (YvsComptaReglementCreditFournisseur r : reglements) {
            if (r.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                reste -= r.getValeur();
                statutPaiement = Constantes.STATUT_DOC_ENCOUR;
            }
            resteUnBind -= r.getValeur();
        }
        if (reste <= 0) {
            statutPaiement = Constantes.STATUT_DOC_PAYER;
        }
        return reste;
    }

}
