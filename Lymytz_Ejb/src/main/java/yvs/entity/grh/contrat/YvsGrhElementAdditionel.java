/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.saisie.YvsComptaContentJournalRetenueSalaire;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueDocDivers;
import yvs.entity.grh.contrat.retenue.YvsGrhRetenueEcartStock;
import yvs.entity.grh.param.poste.YvsGrhModelPrimePoste;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.salaire.YvsGrhDetailPrelevementEmps;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_grh_element_additionel")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementAdditionel.findAll", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.typeElement.societe=:societe AND y.typeElement.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findAllC", query = "SELECT COUNT(y) FROM YvsGrhElementAdditionel y WHERE y.typeElement.societe=:societe AND y.typeElement.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByDescription", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.description = :description"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findAllRetenue", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.typeElement.retenue = true"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByMontantElement", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.montantElement = :montantElement"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByPlanifier", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.planifier = :planifier AND y.typeElement.societe=:societe AND y.typeElement.retenue = true"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByContrat", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.contrat=:contrat"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findById", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.id=:id"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByContrat_", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.contrat=:contrat AND y.typeElement.retenue=true"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findPrimeByContrat", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.contrat=:contrat AND y.typeElement.retenue=false"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findRetenuByEmploye", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE (upper(y.contrat.employe.nom) LIKE upper(:employe) OR upper(y.contrat.employe.prenom) LIKE upper(:employe) OR upper(y.contrat.employe.matricule) LIKE upper(:employe)) AND y.typeElement.retenue=true AND y.contrat.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByContratPlanifier", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.contrat=:contrat AND y.planifier = :planifier"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByTypeElt", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.typeElement=:typeE"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByDateElement", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.dateElement = :dateElement"),

    @NamedQuery(name = "YvsGrhElementAdditionel.findByDocVente", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.piceReglement.vente=:docVente"),
    @NamedQuery(name = "YvsGrhElementAdditionel.findByPieceVente", query = "SELECT y FROM YvsGrhElementAdditionel y WHERE y.piceReglement=:piece"),

    @NamedQuery(name = "YvsGrhElementAdditionel.sumByTypeSociete", query = "SELECT SUM(y.montantElement) FROM YvsGrhElementAdditionel y WHERE y.typeElement = :type AND y.contrat.employe.agence.societe = :societe AND y.statut != 'S' "),
    @NamedQuery(name = "YvsGrhElementAdditionel.sumByTypeAgence", query = "SELECT SUM(y.montantElement) FROM YvsGrhElementAdditionel y WHERE y.typeElement = :type AND y.contrat.employe.agence = :agence AND y.statut != 'S'"),
    @NamedQuery(name = "YvsGrhElementAdditionel.sumByTypeEmploye", query = "SELECT SUM(y.montantElement) FROM YvsGrhElementAdditionel y WHERE y.typeElement = :type AND y.contrat.employe = :employe AND y.statut != 'S'"),
    @NamedQuery(name = "YvsGrhElementAdditionel.sumByTypeContrat", query = "SELECT SUM(y.montantElement) FROM YvsGrhElementAdditionel y WHERE y.typeElement = :type AND y.contrat = :contrat AND y.statut != 'S'")})

public class YvsGrhElementAdditionel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_element_additionel_id_seq", name = "yvs_grh_element_additionel_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_element_additionel_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "statut")
    private Character statut;   //statut de la retenu E=En cours, S=suspendu, R=réglé
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "permanent")
    private Boolean permanent;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_element")
    private double montantElement;
    @Column(name = "planifier")
    private Boolean planifier;
    @Column(name = "date_element")
    @Temporal(TemporalType.DATE)
    private Date dateElement;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @JoinColumn(name = "plan_prelevement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPlanPrelevement planPrelevement;
    @JoinColumn(name = "type_element", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeElementAdditionel typeElement;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratEmps contrat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "pice_reglement", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceVente piceReglement;

    @OneToOne(mappedBy = "retenue")
    private YvsGrhRetenueDocDivers docDivers;
    @OneToOne(mappedBy = "retenue")
    private YvsGrhRetenueEcartStock ecartStock;
    @OneToOne(mappedBy = "retenue")
    private YvsGrhRetenueEcartStock ecartVente;
    @OneToOne(mappedBy = "retenue")
    private YvsComptaContentJournalRetenueSalaire pieceContenu;

    @OneToMany(mappedBy = "retenue", fetch = FetchType.LAZY)
    private List<YvsGrhDetailPrelevementEmps> retenues;
    @OneToMany(mappedBy = "retenue", fetch = FetchType.LAZY)
    private List<YvsGrhComposantsRetenue> listDetails;

    @Transient
    private List<YvsGrhDetailPrelevementEmps> listPrelevement;
    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private boolean suspendu;
    @Transient
    private double montantPlanifie;
    @Transient
    private double montantRegle;
    @Transient
    private double montantEncours;
    @Transient
    private double montantReste;
    @Transient
    private boolean errorComptabilise;

    public YvsGrhElementAdditionel() {
        retenues = new ArrayList<>();
        listPrelevement = new ArrayList<>();
    }

    public YvsGrhElementAdditionel(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhElementAdditionel(Long id, YvsGrhModelPrimePoste y) {
        this(id);
        this.typeElement = y.getTypeElement();
        this.dateDebut = y.getDateDebut();
        this.dateFin = y.getDateFin();
        this.permanent = y.getPermanent();
        this.montantElement = y.getMontantElement();
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

    public double getMontantReste() {
        montantReste = montantElement - getMontantRegle();
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public double getMontantRegle() {
        return montantRegle;
    }

    public List<YvsGrhDetailPrelevementEmps> getListPrelevement() {
        return listPrelevement;
    }

    public void setListPrelevement(List<YvsGrhDetailPrelevementEmps> listPrelevement) {
        this.listPrelevement = listPrelevement;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public double montantRegleByList() {
        montantRegle = 0;
        if (!getStatut().equals('S')) {
            for (YvsGrhDetailPrelevementEmps d : retenues) {
                if (d.getStatutReglement().equals('P')) {
                    montantRegle += d.getValeur();
                }
            }
        }
        return montantRegle;
    }

    public void setMontantRegle(double montantRegle) {
        this.montantRegle = montantRegle;
    }

    public double getMontantEncours() {
        return montantEncours;
    }

    public double montantEncoursByList() {
        montantEncours = 0;
        if (!getStatut().equals('S')) {
            for (YvsGrhDetailPrelevementEmps d : retenues) {
                if (d.getStatutReglement().equals('E')) {
                    montantEncours += d.getValeur();
                }
            }
        }
        return montantEncours;
    }

    public double getMontantPlanifie() {
        montantPlanifie = 0;
        if (!getStatut().equals('S')) {
            for (YvsGrhDetailPrelevementEmps d : retenues) {
                if (!d.getStatutReglement().equals('S')) {
                    montantPlanifie += d.getValeur();
                }
            }

        }
        return montantPlanifie;
    }

    public void setMontantEncours(double montantEncours) {
        this.montantEncours = montantEncours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontantElement() {
        return montantElement;
    }

    public void setMontantElement(double montantElement) {
        this.montantElement = montantElement;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getPlanifier() {
        return planifier != null ? planifier : false;
    }

    public void setPlanifier(Boolean planifier) {
        this.planifier = planifier;
    }

    public Date getDateElement() {
        return dateElement;
    }

    public void setDateElement(Date dateElement) {
        this.dateElement = dateElement;
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public YvsGrhTypeElementAdditionel getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(YvsGrhTypeElementAdditionel typeElement) {
        this.typeElement = typeElement;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public boolean isSuspendu() {
        return suspendu;
    }

    public void setSuspendu(boolean suspendu) {
        this.suspendu = suspendu;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Character getStatut() {
        return (statut != null) ? statut : 'E';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public List<YvsGrhDetailPrelevementEmps> getRetenues() {
        return retenues;
    }

    public void setRetenues(List<YvsGrhDetailPrelevementEmps> retenues) {
        this.retenues = retenues;
    }

    public YvsGrhPlanPrelevement getPlanPrelevement() {
        return planPrelevement;
    }

    public void setPlanPrelevement(YvsGrhPlanPrelevement planPrelevement) {
        this.planPrelevement = planPrelevement;
    }

    public List<YvsGrhComposantsRetenue> getListDetails() {
        return listDetails;
    }

    public void setListDetails(List<YvsGrhComposantsRetenue> listDetails) {
        this.listDetails = listDetails;
    }

    public YvsComptaCaissePieceVente getPiceReglement() {
        return piceReglement;
    }

    public void setPiceReglement(YvsComptaCaissePieceVente piceReglement) {
        this.piceReglement = piceReglement;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalRetenueSalaire getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalRetenueSalaire pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRetenueDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsGrhRetenueDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRetenueEcartStock getEcartStock() {
        return ecartStock;
    }

    public void setEcartStock(YvsGrhRetenueEcartStock ecartStock) {
        this.ecartStock = ecartStock;
    }

    @XmlTransient
    @JsonIgnore
    public YvsGrhRetenueEcartStock getEcartVente() {
        return ecartVente;
    }

    public void setEcartVente(YvsGrhRetenueEcartStock ecartVente) {
        this.ecartVente = ecartVente;
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
        if (!(object instanceof YvsGrhElementAdditionel)) {
            return false;
        }
        YvsGrhElementAdditionel other = (YvsGrhElementAdditionel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhElementAdditionel[ id=" + id + " ]";
    }

}
