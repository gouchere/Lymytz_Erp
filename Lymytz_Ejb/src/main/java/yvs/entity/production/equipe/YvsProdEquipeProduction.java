/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.equipe;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_prod_equipe_production")
@NamedQueries({
    @NamedQuery(name = "YvsProdEquipeProduction.findAll", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.site.agence.societe=:societe"),
    @NamedQuery(name = "YvsProdEquipeProduction.findBySite", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.site = :site ORDER BY y.reference"),
    @NamedQuery(name = "YvsProdEquipeProduction.findByAgence", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.site.agence=:agence ORDER BY y.reference"),
    @NamedQuery(name = "YvsProdEquipeProduction.findById", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdEquipeProduction.findByReference", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsProdEquipeProduction.findByNom", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.nom = :nom"),
    @NamedQuery(name = "YvsProdEquipeProduction.findByPrincipal", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.principal = :principal"),
    @NamedQuery(name = "YvsProdEquipeProduction.findByActif", query = "SELECT y FROM YvsProdEquipeProduction y WHERE y.actif = :actif")})
public class YvsProdEquipeProduction implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_equipe_production_id_seq", name = "yvs_prod_equipe_production_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_equipe_production_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Column(name = "principal")
    private Boolean principal;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "site", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdSiteProduction site;
    @JoinColumn(name = "chef_equipe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes chefEquipe;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "equipeProduction")
    private List<YvsProdMembresEquipe> employeEquipeList;
    @Transient
    private boolean selectActif;

    public YvsProdEquipeProduction() {
    }

    public YvsProdEquipeProduction(Long id) {
        this.id = id;
    }

    public YvsProdEquipeProduction(Long id, String nom) {
        this.id = id;
        this.nom = nom;
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
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsProdSiteProduction getSite() {
        return site;
    }

    public void setSite(YvsProdSiteProduction site) {
        this.site = site;
    }

    public YvsGrhEmployes getChefEquipe() {
        return chefEquipe;
    }

    public void setChefEquipe(YvsGrhEmployes chefEquipe) {
        this.chefEquipe = chefEquipe;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient
    public List<YvsProdMembresEquipe> getEmployeEquipeList() {
        return employeEquipeList;
    }

    public void setEmployeEquipeList(List<YvsProdMembresEquipe> employeEquipeList) {
        this.employeEquipeList = employeEquipeList;
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
        if (!(object instanceof YvsProdEquipeProduction)) {
            return false;
        }
        YvsProdEquipeProduction other = (YvsProdEquipeProduction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.equipe.YvsProdEquipeProduction[ id=" + id + " ]";
    }
}
