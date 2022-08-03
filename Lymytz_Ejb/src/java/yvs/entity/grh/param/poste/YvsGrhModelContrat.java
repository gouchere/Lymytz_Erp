/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.param.poste;

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
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_model_contrat")
@NamedQueries({
    @NamedQuery(name = "YvsGrhModelContrat.findAll", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsGrhModelContrat.findById", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhModelContrat.findByIntitule", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsGrhModelContrat.findBySalaireBaseHoraire", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.salaireBaseHoraire = :salaireBaseHoraire"),
    @NamedQuery(name = "YvsGrhModelContrat.findBySalaireBaseMensuel", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.salaireBaseMensuel = :salaireBaseMensuel"),
    @NamedQuery(name = "YvsGrhModelContrat.findByCongeAcquis", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.congeAcquis = :congeAcquis"),
    @NamedQuery(name = "YvsGrhModelContrat.findByMajorationConge", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.majorationConge = :majorationConge"),
    @NamedQuery(name = "YvsGrhModelContrat.findByDateSave", query = "SELECT y FROM YvsGrhModelContrat y WHERE y.dateSave = :dateSave")})
public class YvsGrhModelContrat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_model_contrat_id_seq", name = "yvs_grh_model_contrat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_model_contrat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salaire_base_horaire")
    private Double salaireBaseHoraire;
    @Column(name = "salaire_base_mensuel")
    private Double salaireBaseMensuel;
    @Column(name = "conge_acquis")
    private Double congeAcquis;
    @Column(name = "majoration_conge")
    private Double majorationConge;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "preavis", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhPreavis preavis;

    @OneToMany(mappedBy = "model")
    private List<YvsGrhModelPrimePoste> primes;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsGrhModelContrat() {
        primes = new ArrayList<>();
    }

    public YvsGrhModelContrat(Long id) {
        this.id = id;
        primes = new ArrayList<>();
    }

    public YvsGrhModelContrat(Long id, String intitule) {
        this.id = id;
        this.intitule = intitule;
        primes = new ArrayList<>();
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule != null ? intitule : "";
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Double getSalaireBaseHoraire() {
        return salaireBaseHoraire != null ? salaireBaseHoraire : 0;
    }

    public void setSalaireBaseHoraire(Double salaireBaseHoraire) {
        this.salaireBaseHoraire = salaireBaseHoraire;
    }

    public Double getSalaireBaseMensuel() {
        return salaireBaseMensuel != null ? salaireBaseMensuel : 0;
    }

    public void setSalaireBaseMensuel(Double salaireBaseMensuel) {
        this.salaireBaseMensuel = salaireBaseMensuel;
    }

    public Double getCongeAcquis() {
        return congeAcquis != null ? congeAcquis : 0;
    }

    public void setCongeAcquis(Double congeAcquis) {
        this.congeAcquis = congeAcquis;
    }

    public Double getMajorationConge() {
        return majorationConge != null ? majorationConge : 0;
    }

    public void setMajorationConge(Double majorationConge) {
        this.majorationConge = majorationConge;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsGrhModelPrimePoste> getPrimes() {
        return primes;
    }

    public void setPrimes(List<YvsGrhModelPrimePoste> primes) {
        this.primes = primes;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhPreavis getPreavis() {
        return preavis;
    }

    public void setPreavis(YvsGrhPreavis preavis) {
        this.preavis = preavis;
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
        if (!(object instanceof YvsGrhModelContrat)) {
            return false;
        }
        YvsGrhModelContrat other = (YvsGrhModelContrat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.param.poste.YvsGrhModelContrat[ id=" + id + " ]";
    }

    }
