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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalCreditClient;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_credit_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCreditClient.findAll", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.client.tiers.societe = :societe ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findById", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.id = :id ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByIds", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.id IN :ids ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByNumReference", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.numReference = :numReference ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByNumPiece", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.numReference LIKE :numeroPiece AND y.client.tiers.societe = :societe ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByMotif", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.motif = :motif ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByDateCredit", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.dateCredit = :dateCredit ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findByMontant", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.montant = :montant ORDER BY y.dateCredit DESC"),

    @NamedQuery(name = "YvsComptaCaissePieceVente.findByCreanceClientDates", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.client = :client AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findByCreanceClient", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.client = :client"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findCreanceByClientDates", query = "SELECT SUM(y.montant) FROM YvsComptaCreditClient y WHERE y.client = :client AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findCreanceByClientDatesAgence", query = "SELECT SUM(y.montant) FROM YvsComptaCreditClient y WHERE y.journal.agence = :agence AND y.client = :client AND y.dateCredit BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaCaissePieceVente.findCreanceByClient", query = "SELECT SUM(y.montant) FROM YvsComptaCreditClient y WHERE y.client = :client"),

    @NamedQuery(name = "YvsComptaCreditClient.findByClient", query = "SELECT y FROM YvsComptaCreditClient y WHERE y.client = :client ORDER BY y.dateCredit DESC"),
    @NamedQuery(name = "YvsComptaCreditClient.findSumByClient", query = "SELECT SUM(y.montant) FROM YvsComptaCreditClient y WHERE y.client = :client")})
public class YvsComptaCreditClient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_credit_client_id_seq", name = "yvs_compta_credit_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_credit_client_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "num_reference")
    private String numReference;
    @Size(max = 2147483647)
    @Column(name = "motif")
    private String motif;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_credit")
    @Temporal(TemporalType.DATE)
    private Date dateCredit;
    @Column(name = "statut")
    private Character statut;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient client;
    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;
    @JoinColumn(name = "type_credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCredit;

    @OneToOne(mappedBy = "credit")
    private YvsComptaContentJournalCreditClient pieceContenu;

    @OneToMany(mappedBy = "credit")
    private List<YvsComptaReglementCreditClient> reglements;
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

    public YvsComptaCreditClient() {
        reglements = new ArrayList<>();
    }

    public YvsComptaCreditClient(Long id) {
        this();
        this.id = id;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<YvsComptaReglementCreditClient> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaReglementCreditClient> reglements) {
        this.reglements = reglements;
    }

    public double getResteUnBind() {
        resteUnBind = getMontant();
        for (YvsComptaReglementCreditClient r : reglements) {
            resteUnBind -= r.getValeur();
        }
        return resteUnBind;
    }

    public double getResteUnBind(long idPiece) {
        resteUnBind = getMontant();
        for (YvsComptaReglementCreditClient r : reglements) {
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
        for (YvsComptaReglementCreditClient r : reglements) {
            if (r.getStatut().equals(Constantes.STATUT_DOC_PAYER)) {
                reste -= r.getValeur();
            }
        }
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public char getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(char statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
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

    @XmlTransient  @JsonIgnore
    public YvsComptaContentJournalCreditClient getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalCreditClient pieceContenu) {
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
        if (!(object instanceof YvsComptaCreditClient)) {
            return false;
        }
        YvsComptaCreditClient other = (YvsComptaCreditClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaCreditClient[ id=" + id + " ]";
    }

    public double creance() {
        resteUnBind = getMontant();
        reste = getMontant();
        statutPaiement = Constantes.STATUT_DOC_ATTENTE;
        for (YvsComptaReglementCreditClient r : reglements) {
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
