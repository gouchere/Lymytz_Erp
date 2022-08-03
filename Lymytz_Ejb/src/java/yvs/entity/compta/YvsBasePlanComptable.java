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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_base_plan_comptable")
@NamedQueries({
    @NamedQuery(name = "YvsBasePlanComptable.findAll", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte.societe=:societe ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findWithReport", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte.societe=:societe AND y.typeReport != 'AU' ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findAllC", query = "SELECT COUNT(y) FROM YvsBasePlanComptable y WHERE y.natureCompte.societe=:societe"),
    @NamedQuery(name = "YvsBasePlanComptable.findTrue", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte.societe=:societe AND y.actif = true ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findTrueC", query = "SELECT COUNT(y) FROM YvsBasePlanComptable y WHERE y.natureCompte.societe=:societe AND y.actif = true"),
    @NamedQuery(name = "YvsBasePlanComptable.findById", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanComptable.findNumeroById", query = "SELECT y.numCompte FROM YvsBasePlanComptable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBasePlanComptable.findByNature", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte = :natureCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findByTypeNature", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte.societe=:societe AND y.natureCompte.nature = :nature"),
    @NamedQuery(name = "YvsBasePlanComptable.findByOnline", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.natureCompte.societe = :societe AND y.venteOnline = :venteOnline"),
    @NamedQuery(name = "YvsBasePlanComptable.findByNumCompte", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.numCompte = :numCompte AND y.natureCompte.societe=:societe"),
    @NamedQuery(name = "YvsBasePlanComptable.findIdByNumCompte", query = "SELECT y.id FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE UPPER(y.numCompte) = :numCompte AND y.natureCompte.societe=:societe"),
    @NamedQuery(name = "YvsBasePlanComptable.findByNum", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE (upper(y.numCompte) LIKE upper(:numCompte) OR upper(y.intitule) LIKE upper(:numCompte) OR upper(y.abbreviation) LIKE upper(:numCompte)) AND y.natureCompte.societe=:societe ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findByNumC", query = "SELECT COUNT(y) FROM YvsBasePlanComptable y WHERE (upper(y.numCompte) LIKE upper(:numCompte) OR upper(y.intitule) LIKE upper(:numCompte) OR upper(y.abbreviation) LIKE upper(:numCompte)) AND y.natureCompte.societe=:societe"),
    @NamedQuery(name = "YvsBasePlanComptable.findLikeNum", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.numCompte LIKE :numCompte AND y.natureCompte.societe=:societe ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findLikeNumActif", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.numCompte LIKE :numCompte AND y.natureCompte.societe=:societe AND y.actif = true ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findByIntitule", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.intitule = :intitule ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findByCompteGeneral", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.compteGeneral = :compteGeneral ORDER BY y.numCompte"),
    @NamedQuery(name = "YvsBasePlanComptable.findBySaisieAnalytique", query = "SELECT y FROM YvsBasePlanComptable y LEFT JOIN FETCH y.compteGeneral JOIN FETCH y.natureCompte WHERE y.saisieAnalytique = :saisieAnalytique ORDER BY y.numCompte")})
public class YvsBasePlanComptable extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_plan_de_compte_id_seq", name = "yvs_compta_plan_de_compte_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_plan_de_compte_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Size(max = 2147483647)
    @Column(name = "num_compte")
    private String numCompte;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "saisie_analytique")
    private Boolean saisieAnalytique;
    @Column(name = "saisie_echeance")
    private Boolean saisieEcheance;
    @Column(name = "saisie_compte_tiers")
    private Boolean saisieCompteTiers;
    @Column(name = "lettrable")
    private Boolean lettrable;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "type_compte")
    private String typeCompte;
    @Size(max = 2147483647)
    @Column(name = "type_report")
    private String typeReport;
    @Size(max = 2147483647)
    @Column(name = "sens_compte")
    private String sensCompte;
    @Column(name = "vente_online")
    private Boolean venteOnline;

    @JoinColumn(name = "nature_compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseNatureCompte natureCompte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "compte_general", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private YvsBasePlanComptable compteGeneral;
    @Transient
    private boolean new_;

    public YvsBasePlanComptable() {
    }

    public YvsBasePlanComptable(Long id) {
        this.id = id;
    }

    public YvsBasePlanComptable(String numCompte) {
        this.numCompte = numCompte;
    }

    public YvsBasePlanComptable(Long id, String numCompte) {
        this.id = id;
        this.numCompte = numCompte;
    }

    public YvsBasePlanComptable(Long id, String numCompte, String intitule) {
        this.id = id;
        this.numCompte = numCompte;
        this.intitule = intitule;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCompte() {
        return numCompte != null ? numCompte : "";
    }

    public void setNumCompte(String numCompte) {
        this.numCompte = numCompte;
    }

    public String getIntitule() {
        return intitule != null ? intitule : "";
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Boolean getVenteOnline() {
        return venteOnline != null ? venteOnline : false;
    }

    public void setVenteOnline(Boolean venteOnline) {
        this.venteOnline = venteOnline;
    }

    public YvsBasePlanComptable getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(YvsBasePlanComptable compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public Boolean getSaisieAnalytique() {
        return saisieAnalytique != null ? saisieAnalytique : false;
    }

    public void setSaisieAnalytique(Boolean saisieAnalytique) {
        this.saisieAnalytique = saisieAnalytique;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getSaisieEcheance() {
        return saisieEcheance != null ? saisieEcheance : false;
    }

    public void setSaisieEcheance(Boolean saisieEcheance) {
        this.saisieEcheance = saisieEcheance;
    }

    public Boolean getSaisieCompteTiers() {
        return saisieCompteTiers != null ? saisieCompteTiers : false;
    }

    public void setSaisieCompteTiers(Boolean saisieCompteTiers) {
        this.saisieCompteTiers = saisieCompteTiers;
    }

    public Boolean getLettrable() {
        return lettrable != null ? lettrable : false;
    }

    public void setLettrable(Boolean lettrable) {
        this.lettrable = lettrable;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getAbbreviation() {
        return abbreviation != null ? abbreviation : "";
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public String getSensCompte() {
        return sensCompte;
    }

    public void setSensCompte(String sensCompte) {
        this.sensCompte = sensCompte;
    }

    public YvsBaseNatureCompte getNatureCompte() {
        return natureCompte;
    }

    public void setNatureCompte(YvsBaseNatureCompte natureCompte) {
        this.natureCompte = natureCompte;
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
        if (!(object instanceof YvsBasePlanComptable)) {
            return false;
        }
        YvsBasePlanComptable other = (YvsBasePlanComptable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsBasePlanComptable[ id=" + id + " ]";
    }

}
