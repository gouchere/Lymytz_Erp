/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

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
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_mutualiste")
@NamedQueries({
    @NamedQuery(name = "YvsMutMutualiste.findAll", query = "SELECT y FROM YvsMutMutualiste y WHERE y.employe.agence.societe = :societe ORDER BY y.employe.nom, y.employe.prenom"),
    @NamedQuery(name = "YvsMutMutualiste.findByActif", query = "SELECT y FROM YvsMutMutualiste y WHERE y.employe.agence.societe = :societe AND y.actif = :actif ORDER BY y.employe.nom, y.employe.prenom"),
    @NamedQuery(name = "YvsMutMutualiste.findByMutuelle", query = "SELECT y FROM YvsMutMutualiste y WHERE y.mutuelle = :mutuelle AND y.employe.actif=true ORDER BY y.employe.matricule, y.employe.nom ASC, y.employe.prenom"),
    @NamedQuery(name = "YvsMutMutualiste.findByMutuelleActif", query = "SELECT y FROM YvsMutMutualiste y WHERE y.mutuelle = :mutuelle AND y.actif = :actif ORDER BY y.employe.matricule, y.employe.nom"),
    @NamedQuery(name = "YvsMutMutualiste.findByMutuelleActifAssistance", query = "SELECT y FROM YvsMutMutualiste y WHERE y.mutuelle = :mutuelle AND y.actif = :actif AND y.assistance = :assistance ORDER BY y.employe.matricule, y.employe.nom"),
    @NamedQuery(name = "YvsMutMutualiste.findByEmployeMutuelle", query = "SELECT y FROM YvsMutMutualiste y WHERE y.mutuelle = :mutuelle AND y.employe=:employe"),
    @NamedQuery(name = "YvsMutMutualiste.findById", query = "SELECT y FROM YvsMutMutualiste y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutMutualiste.findByUsers", query = "SELECT y FROM YvsMutMutualiste y WHERE y.employe.nom LIKE :code OR y.employe.prenom LIKE :code OR y.employe.matricule LIKE :code AND y.employe.actif=true AND y.mutuelle=:mutuelle"),
    @NamedQuery(name = "YvsMutMutualiste.findByOneUsers", query = "SELECT y FROM YvsMutMutualiste y WHERE y.employe.codeUsers = :users AND y.employe.actif=true AND y.mutuelle=:mutuelle AND y.employe.codeUsers.actif=true"),
    @NamedQuery(name = "YvsMutMutualiste.findByEmploye", query = "SELECT y FROM YvsMutMutualiste y WHERE y.employe.nom LIKE :code OR y.employe.prenom LIKE :code OR y.employe.matricule LIKE :code AND y.employe.actif=true AND y.mutuelle=:mutuelle"),
    @NamedQuery(name = "YvsMutMutualiste.findByDateAdhesion", query = "SELECT y FROM YvsMutMutualiste y WHERE y.dateAdhesion = :dateAdhesion ORDER BY y.employe.nom, y.employe.prenom")})
public class YvsMutMutualiste implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_mutualiste_id_seq", name = "yvs_mut_mutualiste_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_mutualiste_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_adhesion")
    @Temporal(TemporalType.DATE)
    private Date dateAdhesion;
    @Column(name = "actif")
    private Boolean actif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant_epargne")
    private Double montantEpargne;
    @Column(name = "assistance")
    private Boolean assistance;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne
    private YvsGrhEmployes employe;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "mutualiste")
    private List<YvsMutPosteEmploye> postes;
    @OneToMany(mappedBy = "mutualiste")
    private List<YvsMutCompte> comptes;

    @Transient
    private YvsMutPosteEmploye posteEmploye = new YvsMutPosteEmploye();
    @Transient
    private boolean selectActif;
    @Transient
    private boolean epargneOk;
    @Transient
    private boolean new_;
    @Transient
    private double montantCredit;
    @Transient
    private double montantTotalAssurance;
    @Transient
    private double montantTotalEpargne;
    @Transient
    private double couvertureAvalise;

    public YvsMutMutualiste() {
        postes = new ArrayList<>();
        comptes = new ArrayList<>();
    }

    public YvsMutMutualiste(Long id) {
        this();
        this.id = id;
    }

    public YvsMutMutualiste(Long id, YvsGrhEmployes employe) {
        this(id);
        this.employe = employe;
    }

    public YvsMutPosteEmploye getPosteEmploye() {
        posteEmploye = new YvsMutPosteEmploye();
        if (getPostes() != null) {
            for (YvsMutPosteEmploye p : getPostes()) {
                if (p.getActif()) {
                    posteEmploye = p;
                    break;
                }
            }
        }
        return posteEmploye;
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

    public void setPosteEmploye(YvsMutPosteEmploye posteEmploye) {
        this.posteEmploye = posteEmploye;
    }

    public double getMontantCredit() {
        return montantCredit;
    }

    public void setMontantCredit(double montantCredit) {
        this.montantCredit = montantCredit;
    }

    public double getMontantTotalEpargne() {
        return montantTotalEpargne;
    }

    public void setMontantTotalEpargne(double montantTotalEpargne) {
        this.montantTotalEpargne = montantTotalEpargne;
    }

    public double getMontantTotalAssurance() {
        return montantTotalAssurance;
    }

    public void setMontantTotalAssurance(double montantTotalAssurance) {
        this.montantTotalAssurance = montantTotalAssurance;
    }

    public double getCouvertureAvalise() {
        return this.couvertureAvalise;
    }

    public void setCouvertureAvalise_(DaoInterfaceLocal dao) {
        Double d = (Double) dao.loadObjectByNameQueries("YvsMutAvaliseCredit.findMontantAvalise_", new String[]{"mutualiste"}, new Object[]{this});
        couvertureAvalise = d != null ? d : 0;
    }

    public void setCouvertureAvalise(double couvertureAvalise) {
        this.couvertureAvalise = couvertureAvalise;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Double getMontantEpargne() {
        return montantEpargne != null ? montantEpargne : 0;
    }

    public void setMontantEpargne(Double montantEpargne) {
        this.montantEpargne = montantEpargne;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(Date dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public List<YvsMutCompte> getComptes() {
        return comptes;
    }

    public void setComptes(List<YvsMutCompte> comptes) {
        this.comptes = comptes;
    }

    public String displayCompte() {
        String st = "";
        for (YvsMutCompte c : comptes) {
            if (!st.isEmpty()) {
                st += ";" + c.getReference();
            } else {
                st = c.getReference();
            }
        }
        return st;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public List<YvsMutPosteEmploye> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsMutPosteEmploye> postes) {
        this.postes = postes;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }

    public Boolean getAssistance() {
        return assistance != null ? assistance : false;
    }

    public YvsMutCompte getCompteEpargneObligatoire() {        
        for (YvsMutCompte c : comptes) {
            if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_EPARGNE)) {
                return c;
            }
        }
        return null;
    }

    public YvsMutCompte getCompteAAssuranceObligatoire() {
        for (YvsMutCompte c : comptes) {
            if (c.getTypeCompte().getNature().equals(Constantes.MUT_TYPE_COMPTE_ASSURANCE)) {
                return c;
            }
        }
        return null;
    }

    public boolean isEpargneOk() {
        return epargneOk;
    }

    public void setEpargneOk(boolean epargneOk) {
        this.epargneOk = epargneOk;
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
        if (!(object instanceof YvsMutMutualiste)) {
            return false;
        }
        YvsMutMutualiste other = (YvsMutMutualiste) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutMutualiste[ id=" + id + " ]";
    }

}
