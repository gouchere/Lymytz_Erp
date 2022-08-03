/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.statistique;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_stat_grh_etat")
@NamedQueries({
    @NamedQuery(name = "YvsStatGrhEtat.findAll", query = "SELECT y FROM YvsStatGrhEtat  y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsStatGrhEtat.findById", query = "SELECT y FROM YvsStatGrhEtat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatGrhEtat.findByLibelle", query = "SELECT y FROM YvsStatGrhEtat y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsStatGrhEtat.findByCode", query = "SELECT y FROM YvsStatGrhEtat y WHERE y.code = :code AND y.societe=:societe")})
public class YvsStatGrhEtat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_etat_id_seq", name = "yvs_stat_etat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_etat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "libelle")
    private String libelle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "etat")
    private List<YvsStatGrhTauxContribution> tauxContributions;
    @OneToMany(mappedBy = "etat")
    private List<YvsStatGRhElementDipe> elements;
    @OneToMany(mappedBy = "etat")
    private List<YvsStatGrhGrilleDipe> grillesDipe;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsStatGrhEtat() {
        elements = new ArrayList<>();
        grillesDipe = new ArrayList<>();
        tauxContributions = new ArrayList<>();
    }

    public YvsStatGrhEtat(Long id) {
        this();
        this.id = id;
    }

    public YvsStatGrhEtat(String code) {
        this();
        this.code = code;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<YvsStatGrhTauxContribution> getTauxContributions() {
        return tauxContributions;
    }

    public void setTauxContributions(List<YvsStatGrhTauxContribution> tauxContributions) {
        this.tauxContributions = tauxContributions;
    }

    public List<YvsStatGrhGrilleDipe> getGrillesDipe() {
        return grillesDipe;
    }

    public void setGrillesDipe(List<YvsStatGrhGrilleDipe> grillesDipe) {
        this.grillesDipe = grillesDipe;
    }

    public List<YvsStatGRhElementDipe> getElements() {
        return elements;
    }

    public void setElements(List<YvsStatGRhElementDipe> elements) {
        this.elements = elements;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
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
        if (!(object instanceof YvsStatGrhEtat)) {
            return false;
        }
        YvsStatGrhEtat other = (YvsStatGrhEtat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.statistique.YvsStatGrhEtat[ id=" + id + " ]";
    }

}
