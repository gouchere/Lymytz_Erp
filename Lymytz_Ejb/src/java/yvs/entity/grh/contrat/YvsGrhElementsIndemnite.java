/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_elements_indemnite")
@NamedQueries({
    @NamedQuery(name = "YvsGrhElementsIndemnite.findAll", query = "SELECT y FROM YvsGrhElementsIndemnite y"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findById", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findByLibelle", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findByBase", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.base = :base"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findByRubrique", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.rubrique = :rubrique"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findByTaux", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsGrhElementsIndemnite.findByQuantite", query = "SELECT y FROM YvsGrhElementsIndemnite y WHERE y.quantite = :quantite")})
public class YvsGrhElementsIndemnite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_elements_indemnite_id_seq", name = "yvs_grh_elements_indemnite_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_elements_indemnite_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "base")
    private Double base;
    @Column(name = "taux")
    private Double taux;
    @Column(name = "quantite")
    private Double quantite;
    @JoinColumn(name = "contrat_suspendu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhContratSuspendu contratSuspendu;
    @Column(name = "retenue")
    private Boolean retenue;
    @JoinColumn(name = "rubrique", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRubriqueIndemnite rubrique;

    public YvsGrhElementsIndemnite() {
    }

    public YvsGrhElementsIndemnite(Long id) {
        this.id = id;
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

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public void setRubrique(YvsGrhRubriqueIndemnite rubrique) {
        this.rubrique = rubrique;
    }

    public YvsGrhRubriqueIndemnite getRubrique() {
        return rubrique;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public YvsGrhContratSuspendu getContratSuspendu() {
        return contratSuspendu;
    }

    public void setContratSuspendu(YvsGrhContratSuspendu contratSuspendu) {
        this.contratSuspendu = contratSuspendu;
    }

    public void setRetenue(Boolean retenue) {
        this.retenue = retenue;
    }

    public Boolean getRetenue() {
        return retenue != null ? retenue : false;
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
        if (!(object instanceof YvsGrhElementsIndemnite)) {
            return false;
        }
        YvsGrhElementsIndemnite other = (YvsGrhElementsIndemnite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhElementsIndemnite[ id=" + id + " ]";
    }

}
