/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Transient;
import yvs.entity.param.YvsGrhSecteurs;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_grh_convention_collective")
@NamedQueries({
    @NamedQuery(name = "YvsConventionCollective.findAll", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.categorie.societe = :societe ORDER BY y.categorie.categorie , y.echelon.echelon"),
    @NamedQuery(name = "YvsConventionCollective.findBySecteur", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.yvsSecteurs = :secteur ORDER BY y.categorie.categorie , y.echelon.echelon DESC"),
    @NamedQuery(name = "YvsConventionCollective.findByCategorie", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.yvsSecteurs = :secteur AND y.categorie = :categorie ORDER BY y.echelon.degre ASC"),
    @NamedQuery(name = "YvsGrhConventionCollective.findCategorieBySecteur", query = "SELECT DISTINCT y.categorie FROM YvsGrhConventionCollective y WHERE y.yvsSecteurs = :secteur AND y.actif=true AND y.categorie.actif=true ORDER BY y.categorie.degre ASC"),
    @NamedQuery(name = "YvsConventionCollective.findByCE", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.categorie= :categorie AND y.echelon=:echelon AND y.yvsSecteurs=:secteur"),
    @NamedQuery(name = "YvsConventionCollective.findBySalaireMin", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.salaireMin = :salaireMin"),
    @NamedQuery(name = "YvsConventionCollective.findByCatEchMin", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.categorie=:categorie AND y.echelon.degre=1 AND y.echelon.societe=:societe"),
    @NamedQuery(name = "YvsConventionCollective.findEchelon", query = "SELECT y.echelon FROM YvsGrhConventionCollective y WHERE y.categorie=:categorie"),
    @NamedQuery(name = "YvsConventionCollective.findBySalaireHoraireMin", query = "SELECT y FROM YvsGrhConventionCollective y WHERE y.salaireHoraireMin = :salaireHoraireMin")})
public class YvsGrhConventionCollective implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_convention_collective_id_seq", name = "yvs_convention_collective_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_convention_collective_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "supp")
    private Boolean supp = false;
    @Column(name = "actif")
    private Boolean actif = true;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salaire_min")
    private Double salaireMin;
    @Column(name = "salaire_horaire_min")
    private Double salaireHoraireMin;
    
    @JoinColumn(name = "secteur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhSecteurs yvsSecteurs;
    @JoinColumn(name = "echellon", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEchelons echelon;
    @JoinColumn(name = "categorie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCategorieProfessionelle categorie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;

    public YvsGrhConventionCollective() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsGrhConventionCollective(Long id) {
        this();
        this.id = id;
    }

    public YvsGrhConventionCollective(Long id, YvsGrhEchelons echelon, YvsGrhCategorieProfessionelle categorie) {
        this(id);
        this.echelon = echelon;
        this.categorie = categorie;
    }

    public YvsGrhConventionCollective(Long id, Double salaireMin, Double salaireHoraireMin) {
        this(id);
        this.salaireMin = salaireMin;
        this.salaireHoraireMin = salaireHoraireMin;
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

    public Double getSalaireMin() {
        return salaireMin != null ? salaireMin : 0;
    }

    public void setSalaireMin(Double salaireMin) {
        this.salaireMin = salaireMin;
    }

    public Double getSalaireHoraireMin() {
        return (salaireHoraireMin != null) ? salaireHoraireMin : 0;
    }

    public void setSalaireHoraireMin(Double salaireHoraireMin) {
        this.salaireHoraireMin = salaireHoraireMin;
    }

    public YvsGrhSecteurs getYvsSecteurs() {
        return yvsSecteurs;
    }

    public void setYvsSecteurs(YvsGrhSecteurs yvsSecteurs) {
        this.yvsSecteurs = yvsSecteurs;
    }

    public YvsGrhEchelons getEchelon() {
        return echelon;
    }

    public void setEchelon(YvsGrhEchelons echelon) {
        this.echelon = echelon;
    }

    public YvsGrhCategorieProfessionelle getCategorie() {
        return categorie;
    }

    public void setCategorie(YvsGrhCategorieProfessionelle categorie) {
        this.categorie = categorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsGrhConventionCollective other = (YvsGrhConventionCollective) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsConventionCollective{" + "id=" + id + '}';
    }

}
