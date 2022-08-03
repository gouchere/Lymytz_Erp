/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

import yvs.entity.grh.contrat.YvsGrhElementAdditionel;
import yvs.entity.grh.param.YvsCalendrier;
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
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.contrat.YvsGrhContratSuspendu;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_contrat_emps")
@NamedQueries({
    @NamedQuery(name = "YvsGrhContratEmps.findAll", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.employe.agence=:agence AND y.accesRestreint=false ORDER BY y.employe.nom"),
    @NamedQuery(name = "YvsGrhContratEmps.findAllScte", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.employe.agence.societe=:societe AND y.accesRestreint=false ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhContratEmps.countALLS", query = "SELECT COUNT(y) FROM YvsGrhContratEmps y WHERE y.employe.agence.societe = :societe AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.findById", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhContratEmps.findByTypeContrat", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.typeContrat = :typeContrat AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.findByDeteDebut", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsGrhContratEmps.findByDateFin", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsGrhContratEmps.findByFinEssaie", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.finEssaie = :finEssaie"),
    @NamedQuery(name = "YvsGrhContratEmps.findByReference", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.referenceContrat = :reference AND y.employe.agence = :agence"),
    @NamedQuery(name = "YvsGrhContratEmps.findByActifEmploye", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhContratEmps.findByEmploye", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.actif=true AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.findByEmploye_", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.actif=true AND y.employe.actif=true"),
    @NamedQuery(name = "YvsGrhContratEmps.findByEmployeName", query = "SELECT y FROM YvsGrhContratEmps y WHERE (upper(y.employe.nom) LIKE upper(:employe) OR upper(y.employe.prenom) LIKE upper(:employe) OR upper(y.employe.matricule) LIKE upper(:employe)) AND y.actif=true AND y.employe.agence.societe=:societe AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.countALL", query = "SELECT COUNT(y) FROM YvsGrhContratEmps y WHERE y.employe.agence = :agence AND y.actif=true AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.countC", query = "SELECT COUNT(y) FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.actif=true AND y.dateFin=:df AND y.dateDebut=:db AND y.typeContrat=:tc AND y.finEssaie=:fe AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.countD", query = "SELECT COUNT(y) FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.fichier=:fichier AND y.accesRestreint=false"),
    @NamedQuery(name = "YvsGrhContratEmps.findIdRestreint", query = "SELECT y.id FROM YvsGrhContratEmps y WHERE y.employe.agence.societe =:societe AND y.accesRestreint=true"),
    //@NamedQuery(name = "YvsGrhContratEmps.findContratNonPaye", query = "SELECT y FROM YvsGrhContratEmps y LEFT JOIN YvsGrhBulletins b WHERE y.actif=true AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhContratEmps.findSearch", query = "SELECT y FROM YvsGrhContratEmps y WHERE (y.employe.nom LIKE :codeUsers OR y.employe.prenom LIKE :codeUsers OR y.employe.matricule LIKE :codeUsers OR y.referenceContrat LIKE :codeUsers) AND y.actif=true AND y.employe.agence=:agence AND y.accesRestreint=false ORDER BY y.employe.prenom"),
    @NamedQuery(name = "YvsGrhContratEmps.findByActif", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.actif = true AND y.employe.agence.societe=:agence AND y.accesRestreint=false ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhContratEmps.findToExport", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.actif = true AND y.employe.agence.societe=:societe ORDER BY y.employe.nom ASC"),
    @NamedQuery(name = "YvsGrhContratEmps.findToExportByAgence", query = "SELECT y FROM YvsGrhContratEmps y WHERE y.actif = true AND y.employe.agence=:agence  ORDER BY y.employe.nom ASC"),

    @NamedQuery(name = "YvsGrhContratEmps.countNewByEmploye", query = "SELECT COUNT(y)FROM YvsGrhContratEmps y WHERE y.employe = :employe AND y.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratEmps.countNewByAgence", query = "SELECT COUNT(y)FROM YvsGrhContratEmps y WHERE y.employe.agence = :agence AND y.dateDebut BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratEmps.countNew", query = "SELECT COUNT(y)FROM YvsGrhContratEmps y WHERE y.employe.agence.societe = :societe AND y.dateDebut BETWEEN :dateDebut AND :dateFin")})
public class YvsGrhContratEmps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_contrat_emps_id_seq", name = "yvs_contrat_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_contrat_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "fin_essaie")
    @Temporal(TemporalType.DATE)
    private Date finEssaie;
    @Column(name = "fichier")
    private String fichier;
    @Column(name = "reference_contrat")
    private String referenceContrat;
    @Column(name = "duree_preavie")
    private Integer dureePreavie;
    @Column(name = "unite_preavis")
    private String unitePreavis;    //jour ou mois
    @Column(name = "type_tranche")
    private String typeTranche;
    @Column(name = "acces_restreint")
    private Boolean accesRestreint;
    @Column(name = "source_first_conge")
    private String sourceFirstConge;
    @Column(name = "date_first_conge")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateFirstConge;
    @Column(name = "contrat_principal")
    private Boolean contratPrincipal;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "statut")
    private String statut = "W";
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salaire_horaire")
    private Double salaireHoraire;
    @Column(name = "salaire_mensuel")
    private Double salaireMensuel;
    @Column(name = "conge_acquis")
    private Double congeAcquis;
    @Column(name = "horaire_hebdo")
    private Double horaireHebdo;
    @Column(name = "horaire_mensuel")
    private Double horaireMensuel;

    @OneToOne(mappedBy = "contrat", fetch = FetchType.LAZY)
    private YvsGrhContratSuspendu contratSuspendu;

    @JoinColumn(name = "type_contrat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsTypeContrat typeContrat;
    @JoinColumn(name = "structure_salaire", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhStructureSalaire structureSalaire;
    @JoinColumn(name = "mode_paiement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement modePaiement;
    @JoinColumn(name = "calendrier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsCalendrier calendrier;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "contrat", fetch = FetchType.LAZY)
    private List<YvsGrhElementAdditionel> primes;
    @OneToMany(mappedBy = "contrat", fetch = FetchType.LAZY)
    private List<YvsGrhBulletins> bulletins;

    @Transient
    private double montantRetenu;
    @Transient
    private double montantRetenuEncours;
    @Transient
    private double montantRetenuRegle;
    @Transient
    private double montantRetenuReste;
    @Transient
    private List<YvsGrhElementAdditionel> primesCumule;
    @Transient
    private List<YvsGrhElementAdditionel> retenus;
    @Transient
    private List<YvsGrhElementAdditionel> retenusCumule;
    @Transient
    private List<YvsGrhElementAdditionel> gains;
    @Transient
    private List<YvsGrhElementAdditionel> gainsCumule;

    public YvsGrhContratEmps() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        primes = new ArrayList<>();
        primesCumule = new ArrayList<>();
        gains = new ArrayList<>();
        gainsCumule = new ArrayList<>();
        retenus = new ArrayList<>();
        retenusCumule = new ArrayList<>();
        bulletins = new ArrayList<>();
    }

    public YvsGrhContratEmps(Long id) {
        this();
        this.id = id;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : "W" : "W";
    }

    public void setStatut(String statut) {
        this.statut = statut;
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

    public List<YvsGrhElementAdditionel> getPrimesCumule() {
        primesCumule.clear();
        return primesCumule;
    }

    public void setPrimesCumule(List<YvsGrhElementAdditionel> primesCumule) {
        this.primesCumule = primesCumule;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public List<YvsGrhElementAdditionel> getRetenus() {
        retenus.clear();
        retenusCumule.clear();
        YvsGrhElementAdditionel y;
        for (YvsGrhElementAdditionel e : getPrimes()) {
            if (e.getTypeElement() != null ? e.getTypeElement().getId() > 0 : false) {
                if (e.getTypeElement().getRetenue()) {
                    y = null;
                    for (YvsGrhElementAdditionel e_ : retenusCumule) {
                        if (e_.getTypeElement().equals(e.getTypeElement())) {
                            y = e_;
                            break;
                        }
                    }
                    if (y != null ? y.getId() > 0 : false) {
                        y.setMontantElement(y.getMontantElement() + e.getMontantElement());
                        y.setMontantEncours(y.getMontantEncours() + e.getMontantEncours());
                        y.setMontantRegle(y.getMontantRegle() + e.getMontantRegle());
                        y.setMontantReste(y.getMontantReste() + e.getMontantReste());
                        retenusCumule.set(retenusCumule.indexOf(y), y);
                    } else {
                        retenusCumule.add(e);
                    }
                    retenus.add(e);
                }
            }
        }
        return retenus;
    }

    public void setRetenus(List<YvsGrhElementAdditionel> retenus) {
        this.retenus = retenus;
    }

    public List<YvsGrhElementAdditionel> getRetenusCumule() {
        return retenusCumule;
    }

    public void setRetenusCumule(List<YvsGrhElementAdditionel> retenusCumule) {
        this.retenusCumule = retenusCumule;
    }

    public List<YvsGrhElementAdditionel> getGains() {
        gains.clear();
        gainsCumule.clear();
        YvsGrhElementAdditionel y;
        for (YvsGrhElementAdditionel e : getPrimes()) {
            if (e.getTypeElement() != null ? e.getTypeElement().getId() > 0 : false) {
                if (!e.getTypeElement().getRetenue()) {
                    y = null;
                    for (YvsGrhElementAdditionel e_ : gainsCumule) {
                        if (e_.getTypeElement().equals(e.getTypeElement())) {
                            y = e_;
                            break;
                        }
                    }
                    if (y != null ? y.getId() > 0 : false) {
                        y.setMontantElement(y.getMontantElement() + e.getMontantElement());
                        y.setMontantEncours(y.getMontantEncours() + e.getMontantEncours());
                        y.setMontantRegle(y.getMontantRegle() + e.getMontantRegle());
                        y.setMontantReste(y.getMontantReste() + e.getMontantReste());
                        gainsCumule.set(gainsCumule.indexOf(y), y);
                    } else {
                        gainsCumule.add(e);
                    }
                    gains.add(e);
                }
            }
        }
        return gains;
    }

    public void setGains(List<YvsGrhElementAdditionel> gains) {
        this.gains = gains;
    }

    public List<YvsGrhElementAdditionel> getGainsCumule() {
        return gainsCumule;
    }

    public void setGainsCumule(List<YvsGrhElementAdditionel> gainsCumule) {
        this.gainsCumule = gainsCumule;
    }

    public double getMontantRetenu() {
        montantRetenu = 0;
        for (YvsGrhElementAdditionel e : retenusCumule) {
            if (e.getTypeElement() != null ? e.getTypeElement().getId() > 0 : false) {
                if (!e.getStatut().equals('S') && e.getTypeElement().getRetenue()) {
                    montantRetenu += e.getMontantElement();
                }
            }
        }
        return montantRetenu;
    }

    public void setMontantRetenu(double montantRetenu) {
        this.montantRetenu = montantRetenu;
    }

    public double getMontantRetenuRegle() {
        montantRetenuRegle = 0;
        for (YvsGrhElementAdditionel e : retenusCumule) {
            if (e.getTypeElement() != null ? e.getTypeElement().getId() > 0 : false) {
                if (!e.getStatut().equals('S') && e.getTypeElement().getRetenue()) {
                    montantRetenuRegle += e.getMontantRegle();
                }
            }
        }
        return montantRetenuRegle;
    }

    public void setMontantRetenuRegle(double montantRetenuRegle) {
        this.montantRetenuRegle = montantRetenuRegle;
    }

    public double getMontantRetenuEncours() {
        montantRetenuEncours = 0;
        for (YvsGrhElementAdditionel e : retenusCumule) {
            if (e.getTypeElement() != null ? e.getTypeElement().getId() > 0 : false) {
                if (!e.getStatut().equals('S') && e.getTypeElement().getRetenue()) {
                    montantRetenuEncours += e.getMontantEncours();
                }
            }
        }
        return montantRetenuEncours;
    }

    public void setMontantRetenuEncours(double montantRetenuEncours) {
        this.montantRetenuEncours = montantRetenuEncours;
    }

    public double getMontantRetenuReste() {
        montantRetenuReste = getMontantRetenu() - getMontantRetenuRegle();
        return montantRetenuReste;
    }

    public void setMontantRetenuReste(double montantRetenuReste) {
        this.montantRetenuReste = montantRetenuReste;
    }

    public Double getCongeAcquis() {
        return congeAcquis;
    }

    public void setCongeAcquis(Double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getReferenceContrat() {
        return referenceContrat;
    }

    public void setReferenceContrat(String referenceContrat) {
        this.referenceContrat = referenceContrat;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getFinEssaie() {
        return finEssaie;
    }

    public void setFinEssaie(Date finEssaie) {
        this.finEssaie = finEssaie;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhContratSuspendu getContratSuspendu() {
        return contratSuspendu;
    }

    public void setContratSuspendu(YvsGrhContratSuspendu contratSuspendu) {
        this.contratSuspendu = contratSuspendu;
    }

    public String getTypeTranche() {
        return typeTranche;
    }

    public void setTypeTranche(String typeTranche) {
        this.typeTranche = typeTranche;
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
        if (!(object instanceof YvsGrhContratEmps)) {
            return false;
        }
        YvsGrhContratEmps other = (YvsGrhContratEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsContratEmps[ id=" + id + " ]";
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getSalaireHoraire() {
        return salaireHoraire != null ? salaireHoraire : 0d;
    }

    public void setSalaireHoraire(Double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public Double getSalaireMensuel() {
        return salaireMensuel != null ? salaireMensuel : 0;
    }

    public void setSalaireMensuel(Double salaireMensuel) {
        this.salaireMensuel = salaireMensuel;
    }

    public Double getHoraireHebdo() {
        return horaireHebdo != null ? horaireHebdo : 0;
    }

    public void setHoraireHebdo(Double horaireHebdo) {
        this.horaireHebdo = horaireHebdo;
    }

    public Double getHoraireMensuel() {
        return horaireMensuel != null ? horaireMensuel : 0;
    }

    public void setHoraireMensuel(Double horaireMensuel) {
        this.horaireMensuel = horaireMensuel;
    }

    public YvsTypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(YvsTypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }

    public YvsGrhStructureSalaire getStructureSalaire() {
        return structureSalaire;
    }

    public void setStructureSalaire(YvsGrhStructureSalaire structureSalaire) {
        this.structureSalaire = structureSalaire;
    }

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public String getSourceFirstConge() {
        return sourceFirstConge;
    }

    public void setSourceFirstConge(String sourceFirstConge) {
        this.sourceFirstConge = sourceFirstConge;
    }

    public Date getDateFirstConge() {
        return dateFirstConge;
    }

    public void setDateFirstConge(Date dateFirstConge) {
        this.dateFirstConge = dateFirstConge;
    }

    public List<YvsGrhBulletins> getBulletins() {
        return bulletins;
    }

    public void setBulletins(List<YvsGrhBulletins> bulletins) {
        this.bulletins = bulletins;
    }

    public YvsBaseModeReglement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(YvsBaseModeReglement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public List<YvsGrhElementAdditionel> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsGrhElementAdditionel> primes) {
        this.primes = primes;
    }

    public YvsCalendrier getCalendrier() {
        return calendrier;
    }

    public void setCalendrier(YvsCalendrier calendrier) {
        this.calendrier = calendrier;
    }

    public Integer getDureePreavie() {
        return dureePreavie;
    }

    public void setDureePreavie(Integer dureePreavie) {
        this.dureePreavie = dureePreavie;
    }

    public String getUnitePreavis() {
        return unitePreavis;
    }

    public void setUnitePreavis(String unitePreavis) {
        this.unitePreavis = unitePreavis;
    }

    public Boolean getAccesRestreint() {
        return accesRestreint != null ? accesRestreint : false;
    }

    public void setAccesRestreint(Boolean accesRestreint) {
        this.accesRestreint = accesRestreint;
    }

    public Boolean getContratPrincipal() {
        return contratPrincipal;
    }

    public void setContratPrincipal(Boolean contratPrincipal) {
        this.contratPrincipal = contratPrincipal;
    }

}
