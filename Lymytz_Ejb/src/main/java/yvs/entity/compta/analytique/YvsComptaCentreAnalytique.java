/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.analytique;

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
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.YvsComptaAffectationGenAnal;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_centre_analytique")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCentreAnalytique.findAll", query = "SELECT y FROM YvsComptaCentreAnalytique y  JOIN FETCH y.plan WHERE y.plan.societe=:societe ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findAllActif", query = "SELECT y FROM YvsComptaCentreAnalytique y JOIN FETCH y.plan WHERE y.plan.societe=:societe AND y.actif=true ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findById", query = "SELECT y FROM YvsComptaCentreAnalytique y WHERE y.id = :id ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findByCodeRef", query = "SELECT y FROM YvsComptaCentreAnalytique y JOIN FETCH y.plan WHERE y.codeRef = :codeRef AND y.plan = :plan ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findByActif", query = "SELECT y FROM YvsComptaCentreAnalytique y JOIN FETCH y.plan WHERE y.actif = :actif AND y.plan.societe = :societe ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findByActifNoId", query = "SELECT y FROM YvsComptaCentreAnalytique y JOIN FETCH y.plan WHERE y.actif = :actif AND y.plan.societe = :societe AND y.id != :id ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findByDesignation", query = "SELECT y FROM YvsComptaCentreAnalytique y WHERE y.designation = :designation ORDER BY y.codeRef"),
    @NamedQuery(name = "YvsComptaCentreAnalytique.findByDescription", query = "SELECT y FROM YvsComptaCentreAnalytique y WHERE y.description = :description ORDER BY y.codeRef")})
public class YvsComptaCentreAnalytique implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_centre_analytique_id_seq", name = "yvs_compta_centre_analytique_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_centre_analytique_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "designation")
    private String designation;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "niveau")
    private String niveau;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 2147483647)
    @Column(name = "type_centre")
    private String typeCentre;
    @JoinColumn(name = "unite_oeuvre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteOeuvre;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPlanAnalytique plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "centre", fetch = FetchType.LAZY)
    private List<YvsComptaAffectationGenAnal> affectations;
    @OneToMany(mappedBy = "secondaire", fetch = FetchType.LAZY)
    private List<YvsComptaRepartitionAnalytique> secondaires;
    @OneToMany(mappedBy = "principal", fetch = FetchType.LAZY)
    private List<YvsComptaRepartitionAnalytique> repartitions;

    @Transient
    private boolean new_;
    @Transient
    private double coeficient;
    @Transient
    private long idAffectation = -1;
    @Transient
    private Date dateSave_=new Date();

    public YvsComptaCentreAnalytique() {
        affectations = new ArrayList<>();
        repartitions = new ArrayList<>();
        secondaires = new ArrayList<>();
    }

    public YvsComptaCentreAnalytique(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaCentreAnalytique(Long id, String codeRef, String designation) {
        this(id);
        this.designation = designation;
        this.codeRef = codeRef;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<YvsComptaAffectationGenAnal> getAffectations() {
        return affectations;
    }

    public void setAffectations(List<YvsComptaAffectationGenAnal> affectations) {
        this.affectations = affectations;
    }

    public List<YvsComptaRepartitionAnalytique> getSecondaires() {
        return secondaires;
    }

    public void setSecondaires(List<YvsComptaRepartitionAnalytique> secondaires) {
        this.secondaires = secondaires;
    }

    public List<YvsComptaRepartitionAnalytique> getRepartitions() {
        return repartitions;
    }

    public void setRepartitions(List<YvsComptaRepartitionAnalytique> repartitions) {
        this.repartitions = repartitions;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getTypeCentre() {
        return typeCentre != null ? typeCentre.trim().length() > 0 ? typeCentre : "P" : "P";
    }

    public void setTypeCentre(String typeCentre) {
        this.typeCentre = typeCentre;
    }

    public YvsBaseUniteMesure getUniteOeuvre() {
        return uniteOeuvre;
    }

    public void setUniteOeuvre(YvsBaseUniteMesure uniteOeuvre) {
        this.uniteOeuvre = uniteOeuvre;
    }

    public YvsComptaPlanAnalytique getPlan() {
        return plan;
    }

    public void setPlan(YvsComptaPlanAnalytique plan) {
        this.plan = plan;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public double getCoeficient() {
        return coeficient;
    }

    public void setCoeficient(double coeficient) {
        this.coeficient = coeficient;
    }

    public long getIdAffectation() {
        return idAffectation;
    }

    public void setIdAffectation(long idAffectation) {
        this.idAffectation = idAffectation;
    }

    public void setDateSave_(Date dateSave_) {
        this.dateSave_ = dateSave_;
    }

    public Date getDateSave_() {
        return dateSave_;
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
        if (!(object instanceof YvsComptaCentreAnalytique)) {
            return false;
        }
        YvsComptaCentreAnalytique other = (YvsComptaCentreAnalytique) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.analytique.YvsComptaCentreAnalytique[ id=" + id + " ]";
    }

}
