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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.client.YvsComptaPhaseReglementCreditClient;
import yvs.entity.compta.saisie.YvsComptaContentJournalReglementCreditClient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_reglement_credit_client")
@NamedQueries({
    @NamedQuery(name = "YvsComptaReglementCreditClient.findAll", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.caisse.journal.agence.societe = :societe ORDER BY y.dateReg DESC"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findById", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findByValeur", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.valeur = :valeur ORDER BY y.dateReg DESC"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findByDateReg", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.dateReg = :dateReg ORDER BY y.dateReg DESC"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findByHeureReg", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.heureReg = :heureReg ORDER BY y.dateReg DESC"),

    @NamedQuery(name = "YvsComptaReglementCreditClient.findIdByCreditStatut", query = "SELECT y.id FROM YvsComptaReglementCreditClient y WHERE y.credit = :credit AND y.statut = :statut"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findByCreditStatut", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.credit = :credit AND y.statut = :statut ORDER BY y.dateReg DESC"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findByCredit", query = "SELECT y FROM YvsComptaReglementCreditClient y WHERE y.credit = :credit ORDER BY y.dateReg DESC"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findSumByClient", query = "SELECT SUM(y.valeur) FROM YvsComptaReglementCreditClient y WHERE y.credit.client = :client AND y.statut = 'P'"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findSumByClientDates", query = "SELECT SUM(y.valeur) FROM YvsComptaReglementCreditClient y WHERE y.credit.client = :client AND y.statut = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComptaReglementCreditClient.findSumByClientDatesAgence", query = "SELECT SUM(y.valeur) FROM YvsComptaReglementCreditClient y WHERE y.credit.journal.agence = :agence AND y.credit.client = :client AND y.statut = 'P' AND y.datePaiement BETWEEN :dateDebut AND :dateFin")})
public class YvsComptaReglementCreditClient implements Serializable {

    @OneToMany(mappedBy = "reglement")
    private List<YvsComptaContentJournalReglementCreditClient> yvsComptaContentJournalReglementCreditClientList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_reglement_credit_client_id_seq", name = "yvs_compta_reglement_credit_client_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_reglement_credit_client_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "numero")
    private String numero;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_paiement")
    @Temporal(TemporalType.DATE)
    private Date datePaiement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "date_reg")
    @Temporal(TemporalType.DATE)
    private Date dateReg;
    @Column(name = "heure_reg")
    @Temporal(TemporalType.TIME)
    private Date heureReg;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "credit", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCreditClient credit;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;

    @OneToOne(mappedBy = "reglement")
    private YvsComptaContentJournalReglementCreditClient pieceContenu;

    @OneToMany(mappedBy = "reglement")
    private List<YvsComptaPhaseReglementCreditClient> phasesReglement;
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean comptabilised;

    public YvsComptaReglementCreditClient() {
        phasesReglement = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaReglementCreditClient(Long id) {
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
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
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

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero != null ? numero : "";
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getValeur() {
        return valeur != null ? valeur : 0;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Date getDateReg() {
        return dateReg != null ? dateReg : new Date();
    }

    public void setDateReg(Date dateReg) {
        this.dateReg = dateReg;
    }

    public Date getHeureReg() {
        return heureReg != null ? heureReg : new Date();
    }

    public void setHeureReg(Date heureReg) {
        this.heureReg = heureReg;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComptaCreditClient getCredit() {
        return credit;
    }

    public void setCredit(YvsComptaCreditClient credit) {
        this.credit = credit;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_ATTENTE : Constantes.STATUT_DOC_ATTENTE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
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

    public List<YvsComptaPhaseReglementCreditClient> getPhasesReglement() {
        return phasesReglement;
    }

    public void setPhasesReglement(List<YvsComptaPhaseReglementCreditClient> phasesReglement) {
        this.phasesReglement = phasesReglement;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    @XmlTransient  @JsonIgnore
    public YvsComptaContentJournalReglementCreditClient getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalReglementCreditClient pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
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
        if (!(object instanceof YvsComptaReglementCreditClient)) {
            return false;
        }
        YvsComptaReglementCreditClient other = (YvsComptaReglementCreditClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaReglementCreditClient[ id=" + id + " ]";
    }

    public int getPhaseValide() {
        int nb = 0;
        for (YvsComptaPhaseReglementCreditClient vm : getPhasesReglement()) {
            if (vm.getPhaseOk()) {
                nb++;
            }
        }
        return nb++;
    }

    public String getLibphases() {
        return "Etp. " + getPhaseValide() + " / " + getPhasesReglement().size();
    }

    public List<YvsComptaContentJournalReglementCreditClient> getYvsComptaContentJournalReglementCreditClientList() {
        return yvsComptaContentJournalReglementCreditClientList;
    }

    public void setYvsComptaContentJournalReglementCreditClientList(List<YvsComptaContentJournalReglementCreditClient> yvsComptaContentJournalReglementCreditClientList) {
        this.yvsComptaContentJournalReglementCreditClientList = yvsComptaContentJournalReglementCreditClientList;
    }

}
