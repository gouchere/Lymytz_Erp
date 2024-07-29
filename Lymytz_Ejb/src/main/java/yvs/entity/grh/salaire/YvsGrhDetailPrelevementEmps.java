/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_detail_prelevement_emps")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findAll", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findById", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findForVenteByDates", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.datePrelevement BETWEEN :debut AND :fin AND y.retenue.contrat=:contrat AND y.retenue.piceReglement IS NOT NULL AND (y.statutReglement != 'S')"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByDatePrelevement", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.datePrelevement <=:fin AND y.retenue.contrat=:contrat AND (y.statutReglement='E' OR (y.statutReglement!='S' AND y.retenuFixe=true))"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByStatutReglement", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.statutReglement = :statutReglement"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.countByStatutReglement", query = "SELECT COUNT(y) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue=:retenue AND y.statutReglement = :statut"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findRetenuesEmps", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.contrat = :contrat ORDER BY y.datePrelevement DESC"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByReference", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findSuivant", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue = :retenue AND y.datePrelevement>=:date  AND y.statutReglement!=:etat ORDER BY y.datePrelevement ASC"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByRetenue", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue = :retenue ORDER BY y.datePrelevement ASC"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumRetenuInMonth", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.contrat=:contrat  AND (y.datePrelevement BETWEEN :date1 AND :date2) AND y.statutReglement!=:statut"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findRetenueRegle", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue = :retenue AND y.statutReglement=:statut"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByValeur", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.valeur = :valeur"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findByDocVente", query = "SELECT y FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.piceReglement.vente=:docVente"),

    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findTypeBySociete", query = "SELECT DISTINCT(y.retenue.typeElement) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.retenue.contrat.employe.agence.societe = :societe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findTypeByAgence", query = "SELECT DISTINCT(y.retenue.typeElement) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.retenue.contrat.employe.agence = :agence AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findTypeByEmploye", query = "SELECT DISTINCT(y.retenue.typeElement) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.retenue.contrat.employe = :employe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.findTypeByContract", query = "SELECT DISTINCT(y.retenue.typeElement) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.retenue.contrat = :contrat AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByTypeSociete", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement = :type AND y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe.agence.societe = :societe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByTypeAgence", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement = :type AND y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe.agence = :agence AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByTypeEmploye", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement = :type AND y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe = :employe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByTypeContrat", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement = :type AND y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat = :contrat AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),

    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumBySociete", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe.agence.societe = :societe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByAgence", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe.agence = :agence AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByEmploye", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat.employe = :employe AND y.datePrelevement BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhDetailPrelevementEmps.sumByContrat", query = "SELECT SUM(y.valeur) FROM YvsGrhDetailPrelevementEmps y WHERE y.retenue.typeElement.retenue=true AND y.retenue.statut != 'S' AND y.statutReglement = :statut AND y.retenue.contrat = :contrat AND y.datePrelevement BETWEEN :dateDebut AND :dateFin")})
public class YvsGrhDetailPrelevementEmps implements Serializable, Comparator<YvsGrhDetailPrelevementEmps> {
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valeur")
    private Double valeur;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_detail_prelevement_emps_id_seq", name = "yvs_grh_detail_prelevement_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_detail_prelevement_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_begin")
    @Temporal(TemporalType.DATE)
    private Date dateBegin;
    @Column(name = "date_prelevement")
    @Temporal(TemporalType.DATE)
    private Date datePrelevement;
    @Column(name = "statut_reglement")
    private Character statutReglement;
    @Size(max = 2147483647)
    @Column(name = "reference_retenu")
    private String reference;
    @JoinColumn(name = "plan_prelevement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPlanPrelevement planPrelevement;  //précise le plan utiliser pour obtenir cette mensualité
    @JoinColumn(name = "retenue", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhElementAdditionel retenue;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Column(name = "date_preleve")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date datePreleve;   //précise la date de prelèvement effectif
    @Column(name = "retenu_fixe")
    private Boolean retenuFixe;

    public YvsGrhDetailPrelevementEmps() {
    }

    public YvsGrhDetailPrelevementEmps(Long id) {
        this.id = id;
    }

    public YvsGrhDetailPrelevementEmps(YvsGrhDetailPrelevementEmps c) {
        this.id = c.id;
        this.datePrelevement = c.datePrelevement;
        this.statutReglement = c.statutReglement;
        this.reference = c.reference;
        this.valeur = c.valeur;
        this.planPrelevement = c.planPrelevement;
        this.retenue = c.retenue;
        this.author = c.author;
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

    public Date getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(Date datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Character getStatutReglement() {
        return statutReglement != null ? statutReglement : 'E';
    }

    public void setStatutReglement(Character statutReglement) {
        this.statutReglement = statutReglement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public YvsGrhPlanPrelevement getPlanPrelevement() {
        return planPrelevement;
    }

    public void setPlanPrelevement(YvsGrhPlanPrelevement planPrelevement) {
        this.planPrelevement = planPrelevement;
    }

    public YvsGrhElementAdditionel getRetenue() {
        return retenue;
    }

    public void setRetenue(YvsGrhElementAdditionel retenue) {
        this.retenue = retenue;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getDatePreleve() {
        return datePreleve;
    }

    public void setDatePreleve(Date datePreleve) {
        this.datePreleve = datePreleve;
    }

    public Boolean getRetenuFixe() {
        return retenuFixe != null ? retenuFixe : false;
    }

    public void setRetenuFixe(Boolean retenuFixe) {
        this.retenuFixe = retenuFixe;
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
        if (!(object instanceof YvsGrhDetailPrelevementEmps)) {
            return false;
        }
        YvsGrhDetailPrelevementEmps other = (YvsGrhDetailPrelevementEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsGrhDetailPrelevementEmps o1, YvsGrhDetailPrelevementEmps o2) {
        if (o1 != null && o2 != null) {
            return (int) (o1.getValeur().compareTo(o2.getValeur()));
        } else {
            if (o1 != null) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

}
