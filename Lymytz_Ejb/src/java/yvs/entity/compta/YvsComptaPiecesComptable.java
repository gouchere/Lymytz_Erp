/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.YvsEntity;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.divers.YvsComptaCaissePieceCoutDivers;
import yvs.entity.compta.saisie.YvsComptaContentAnalytique;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_pieces_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPiecesComptable.findAll", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.journal.agence = :societe"),
    @NamedQuery(name = "YvsComptaPiecesComptable.countAll", query = "SELECT COUNT(y) FROM YvsComptaPiecesComptable y WHERE y.journal.agence = :societe"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findById", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findByNumPiece", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.numPiece LIKE :numeroPiece AND y.journal.agence.societe = :societe ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findByJournal", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.journal =:journal ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findByJournalDates", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.journal =:journal AND y.datePiece BETWEEN :dateDebut AND :dateFin ORDER BY y.numPiece DESC"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findByDatePiece", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.datePiece = :datePiece"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findByDateSaisie", query = "SELECT y FROM YvsComptaPiecesComptable y WHERE y.dateSaisie = :dateSaisie"),
    @NamedQuery(name = "YvsComptaPiecesComptable.findFirstDate", query = "SELECT y.datePiece FROM YvsComptaPiecesComptable y WHERE y.journal.agence.societe = :societe ORDER BY y.datePiece"),})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComptaPiecesComptable extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_pieces_comptable_id_seq", name = "yvs_compta_pieces_comptable_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_pieces_comptable_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "date_piece")
    @Temporal(TemporalType.DATE)
    private Date datePiece;
    @Column(name = "date_saisie")
    @Temporal(TemporalType.DATE)
    private Date dateSaisie;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "extourne")
    private Boolean extourne;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;
    @JoinColumn(name = "exercice", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseExercice exercice;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaModeleSaisie model;
    @Transient
    private List<YvsComptaContentJournal> contentsPiece;
    @Transient
    private List<YvsComptaCaissePieceCoutDivers> piecesCout;
    @Transient
    private List<YvsComptaContentJournal> contentsCredit;
    @Transient
    private List<YvsComptaContentJournal> contentsDebit;
    @Transient
    private List<YvsComptaContentAnalytique> contentsAnalytique;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private Double solde;
    @Transient
    private double total;
    @Transient
    private double couts;
    @Transient
    private Double credits;
    @Transient
    private Double debits;
    @Transient
    private String compte;
    @Transient
    private String intitule;

    public YvsComptaPiecesComptable() {
        contentsPiece = new ArrayList<>();
        contentsDebit = new ArrayList<>();
        contentsCredit = new ArrayList<>();
        piecesCout = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPiecesComptable(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaPiecesComptable(String compte, String intitule) {
        this(null, compte, intitule);
    }

    public YvsComptaPiecesComptable(Long id, String compte, String intitule) {
        this(id);
        this.compte = compte;
        this.intitule = intitule;
    }

    public YvsComptaPiecesComptable(List<YvsComptaContentJournal> contentsPiece) {
        this();
        this.contentsPiece = contentsPiece;
    }

    public YvsComptaPiecesComptable(YvsComptaPiecesComptable p) {
        this();
        this.id = p.id;
        this.dateUpdate = p.dateUpdate;
        this.dateSave = p.dateSave;
        this.numPiece = p.numPiece;
        this.datePiece = p.datePiece;
        this.dateSaisie = p.dateSaisie;
        this.statutPiece = p.statutPiece;
        this.author = p.author;
        this.journal = p.journal;
        this.exercice = p.exercice;
        this.model = p.model;
        this.new_ = p.new_;
        this.select = p.select;
        this.solde = p.solde;
        this.total = p.total;
        this.couts = p.couts;
        this.credits = p.credits;
        this.debits = p.debits;
        this.compte = p.compte;
        this.intitule = p.intitule;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
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

    public String getNumPiece() {
        return numPiece != null ? numPiece : "";
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public Date getDatePiece() {
        return datePiece;
    }

    public void setDatePiece(Date datePiece) {
        this.datePiece = datePiece;
    }

    public Date getDateSaisie() {
        return dateSaisie;
    }

    public void setDateSaisie(Date dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public Character getStatutPiece() {
        return statutPiece != null ? (statutPiece != ' ' ? statutPiece : Constantes.STATUT_EDITABLE) : Constantes.STATUT_EDITABLE;
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isNew_() {
        return new_;
    }

    public double getCouts() {
        return couts;
    }

    public void setCouts(double couts) {
        this.couts = couts;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Boolean getExtourne() {
        return extourne != null ? extourne : false;
    }

    public void setExtourne(Boolean extourne) {
        this.extourne = extourne;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Double getCredits() {
        return credits != null ? credits : 0D;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Double getDebits() {
        return debits != null ? debits : 0D;
    }

    public void setDebits(Double debits) {
        this.debits = debits;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public Double getSolde() {
        return solde != null ? solde : (getDebits() - getCredits());
    }

    public YvsComptaModeleSaisie getModel() {
        return model;
    }

    public void setModel(YvsComptaModeleSaisie model) {
        this.model = model;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public YvsBaseExercice getExercice() {
        return exercice;
    }

    public void setExercice(YvsBaseExercice exercice) {
        this.exercice = exercice;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentJournal> getContentsPiece() {
        return contentsPiece;
    }

    public void setContentsPiece(List<YvsComptaContentJournal> contentsPiece) {
        this.contentsPiece = contentsPiece;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentJournal> getContentsCredit() {
        return contentsCredit;
    }

    public void setContentsCredit(List<YvsComptaContentJournal> contentsCredit) {
        this.contentsCredit = contentsCredit;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentJournal> getContentsDebit() {
        return contentsDebit;
    }

    public void setContentsDebit(List<YvsComptaContentJournal> contentsDebit) {
        this.contentsDebit = contentsDebit;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaCaissePieceCoutDivers> getPiecesCout() {
        return piecesCout;
    }

    public void setPiecesCout(List<YvsComptaCaissePieceCoutDivers> piecesCout) {
        this.piecesCout = piecesCout;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentAnalytique> getContentsAnalytique() {
        return contentsAnalytique;
    }

    public void setContentsAnalytique(List<YvsComptaContentAnalytique> contentsAnalytique) {
        this.contentsAnalytique = contentsAnalytique;
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
        if (!(object instanceof YvsComptaPiecesComptable)) {
            return false;
        }
        YvsComptaPiecesComptable other = (YvsComptaPiecesComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaPiecesComptable[ id=" + id + " ]";
    }

    public boolean canEditable() {
        return getStatutPiece() == Constantes.STATUT_ATTENTE || getStatutPiece() == Constantes.STATUT_EDITABLE;
    }

    public boolean canDelete() {
        return getStatutPiece() == Constantes.STATUT_ATTENTE || getStatutPiece() == Constantes.STATUT_EDITABLE || getStatutPiece() == Constantes.STATUT_SUSPENDU || getStatutPiece() == Constantes.STATUT_ANNULE;
    }

    public double summaryGroup(String groupe, String value, String type) {
        double sum = 0;
        for (YvsComptaContentJournal c : contentsPiece) {
            if (groupe.equals("T")) {
                if (c.getCompteTiers() != null ? c.getCompteTiers().equals(value) : true) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            } else if (groupe.equals("C")) {
                if (c.getCompteGeneral() != null ? c.getCompteGeneral().getNumCompte().equals(value) : true) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            } else {
                if (c.getNumPiece().equals(value)) {
                    if (type.equals("C")) {
                        sum += c.getCredit();
                    } else {
                        sum += c.getDebit();
                    }
                }
            }
        }
        return sum;
    }

    public static Double getSolde(List<YvsComptaContentJournal> list) {
        double credit = 0;
        double debit = 0;
        for (YvsComptaContentJournal y : list) {
            debit += y.getDebit();
            credit += y.getCredit();
        }
        double solde = debit - credit;
        return solde;
    }
}
