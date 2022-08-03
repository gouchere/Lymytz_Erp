/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.compta.saisie.YvsComptaContentJournalAbonnementDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_abonement_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findAll", query = "SELECT y FROM YvsComptaAbonementDocDivers y"),
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findById", query = "SELECT y FROM YvsComptaAbonementDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findByEcheance", query = "SELECT y FROM YvsComptaAbonementDocDivers y WHERE y.echeance = :echeance"),
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findByValeur", query = "SELECT y FROM YvsComptaAbonementDocDivers y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findByLibelle", query = "SELECT y FROM YvsComptaAbonementDocDivers y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComptaAbonementDocDivers.findByDocDivers", query = "SELECT y FROM YvsComptaAbonementDocDivers y WHERE y.docDivers = :docDivers ORDER BY y.echeance"),

    @NamedQuery(name = "YvsComptaAbonementDocDivers.findCompteByDocDivers", query = "SELECT DISTINCT y.plan.compte FROM YvsComptaAbonementDocDivers y WHERE y.docDivers = :docDivers ORDER BY y.echeance")})
public class YvsComptaAbonementDocDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_abonement_doc_divers_id_seq", name = "yvs_compta_abonement_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_abonement_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "echeance")
    @Temporal(TemporalType.DATE)
    private Date echeance;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPlanAbonnement plan;

    @OneToOne(mappedBy = "abonnement")
    private YvsComptaContentJournalAbonnementDivers pieceContenu;

    @Transient
    private List<YvsComptaContentJournal> contenus;
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean unComptabiliseAll = true;

    public YvsComptaAbonementDocDivers() {
        contenus = new ArrayList<>();
    }

    public YvsComptaAbonementDocDivers(Long id) {
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public Double getValeur() {
        return valeur != null ? valeur : 0;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isUnComptabiliseAll() {
        return unComptabiliseAll;
    }

    public void setUnComptabiliseAll(boolean unComptabiliseAll) {
        this.unComptabiliseAll = unComptabiliseAll;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaPlanAbonnement getPlan() {
        return plan;
    }

    public void setPlan(YvsComptaPlanAbonnement plan) {
        this.plan = plan;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalAbonnementDivers getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalAbonnementDivers pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentJournal> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComptaContentJournal> contenus) {
        this.contenus = contenus;
    }

    @XmlTransient
    @JsonIgnore
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
        if (!(object instanceof YvsComptaAbonementDocDivers)) {
            return false;
        }
        YvsComptaAbonementDocDivers other = (YvsComptaAbonementDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaAbonementDocDivers[ id=" + id + " ]";
    }

}
