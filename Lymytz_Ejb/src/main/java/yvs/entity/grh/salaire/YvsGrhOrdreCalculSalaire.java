/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
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
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_ordre_calcul_salaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findAll", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.societe=:societe ORDER BY y.debutMois ASC"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findOrderNonPaye", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.societe=:societe AND y.cloture=false ORDER BY y.debutMois ASC"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findById", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByReference", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.reference = :reference AND y.societe=:societe"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByMonth", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.finMois = :finMois"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByDateJour", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.dateJour = :dateJour"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByDateExecution", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.dateExecution = :dateExecution"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByHeureExecution", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.heureExecution = :heureExecution"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByContainDate", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.societe=:societe AND (:date BETWEEN y.dateDebutTraitement AND y.dateFinTraitement)"),
    @NamedQuery(name = "YvsGrhOrdreCalculSalaire.findByRealise", query = "SELECT y FROM YvsGrhOrdreCalculSalaire y WHERE y.realise = :realise")})
public class YvsGrhOrdreCalculSalaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_ordre_calcul_salaire_id_seq", name = "yvs_grh_ordre_calcul_salaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_ordre_calcul_salaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_jour")
    @Temporal(TemporalType.DATE)
    private Date dateJour;
    @Column(name = "date_execution")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExecution;
    @Column(name = "heure_execution")
    @Temporal(TemporalType.TIME)
    private Date heureExecution;
    @Column(name = "realise")
    private Boolean realise;
    @Column(name = "debut_mois")
    @Temporal(TemporalType.DATE)
    private Date debutMois;
    @Column(name = "fin_mois")
    @Temporal(TemporalType.DATE)
    private Date finMois;
    @Column(name = "date_cloture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCloture;
    @Column(name = "reference")
    private String reference;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "cloture")
    private Boolean cloture;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "date_debut_traitement")
    @Temporal(TemporalType.DATE)
    private Date dateDebutTraitement;
    @Column(name = "date_fin_traitement")
    @Temporal(TemporalType.DATE)
    private Date dateFinTraitement;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @OneToMany(mappedBy = "planification")
    private List<YvsGrhPlanifSalaireContrat> contrats;
    @OneToMany(mappedBy = "entete")
    private List<YvsGrhBulletins> bulletins;
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean select;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean errorComptabilise;

    public YvsGrhOrdreCalculSalaire() {
    }

    public YvsGrhOrdreCalculSalaire(Long id) {
        this.id = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }

    public Date getDateExecution() {
        return dateExecution;
    }

    public void setDateExecution(Date dateExecution) {
        this.dateExecution = dateExecution;
    }

    public Date getHeureExecution() {
        return heureExecution;
    }

    public void setHeureExecution(Date heureExecution) {
        this.heureExecution = heureExecution;
    }

    public Boolean getRealise() {
        return realise;
    }

    public void setRealise(Boolean realise) {
        this.realise = realise;
    }

    public List<YvsGrhPlanifSalaireContrat> getContrats() {
        return contrats;
    }

    public void setContrats(List<YvsGrhPlanifSalaireContrat> contrats) {
        this.contrats = contrats;
    }

    public Date getDebutMois() {
        return debutMois;
    }

    public void setDebutMois(Date debutMois) {
        this.debutMois = debutMois;
    }

    public Date getFinMois() {
        return finMois;
    }

    public void setFinMois(Date finMois) {
        this.finMois = finMois;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getCloture() {
        return cloture != null ? cloture : false;
    }

    public void setCloture(Boolean cloture) {
        this.cloture = cloture;
    }

    public Date getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(Date dateCloture) {
        this.dateCloture = dateCloture;
    }

    public Date getDateDebutTraitement() {
        return dateDebutTraitement;
    }

    public void setDateDebutTraitement(Date dateDebutTraitement) {
        this.dateDebutTraitement = dateDebutTraitement;
    }

    public Date getDateFinTraitement() {
        return dateFinTraitement;
    }

    public void setDateFinTraitement(Date dateFinTraitement) {
        this.dateFinTraitement = dateFinTraitement;
    }

    public List<YvsGrhBulletins> getBulletins() {
        return bulletins;
    }

    public void setBulletins(List<YvsGrhBulletins> bulletins) {
        this.bulletins = bulletins;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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
        if (!(object instanceof YvsGrhOrdreCalculSalaire)) {
            return false;
        }
        YvsGrhOrdreCalculSalaire other = (YvsGrhOrdreCalculSalaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire[ id=" + id + " ]";
    }

}
