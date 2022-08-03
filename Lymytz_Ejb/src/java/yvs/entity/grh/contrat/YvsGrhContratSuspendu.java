/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_contrat_suspendu")
@NamedQueries({
    @NamedQuery(name = "YvsGrhContratSuspendu.findAll", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.contrat.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhContratSuspendu.countAll", query = "SELECT COUNT(y) FROM YvsGrhContratSuspendu y WHERE y.contrat.employe.agence.societe=:societe AND y.dateEffet BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratSuspendu.countByAgence", query = "SELECT COUNT(y) FROM YvsGrhContratSuspendu y WHERE y.contrat.employe.agence=:agence AND y.dateEffet BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratSuspendu.countByContrat", query = "SELECT COUNT(y) FROM YvsGrhContratSuspendu y WHERE y.contrat=:contrat AND y.dateEffet BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratSuspendu.countByEmploye", query = "SELECT COUNT(y) FROM YvsGrhContratSuspendu y WHERE y.contrat.employe=:employe AND y.dateEffet BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findById", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findByMotif", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.motif = :motif"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findByDateEffet", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.dateEffet = :dateEffet"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findByDureePreavis", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.dureePreavis = :dureePreavis"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findByAnciennete", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.anciennete = :anciennete"),
    @NamedQuery(name = "YvsGrhContratSuspendu.findByUnitePeriodePreavis", query = "SELECT y FROM YvsGrhContratSuspendu y WHERE y.unitePeriodePreavis = :unitePeriodePreavis")})
public class YvsGrhContratSuspendu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_contrat_suspendu_id_seq", name = "yvs_grh_contrat_suspendu_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_contrat_suspendu_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "motif")
    private String motif;
    @Column(name = "date_effet")
    @Temporal(TemporalType.DATE)
    private Date dateEffet;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "duree_preavis")
    private Double dureePreavis;
    @Column(name = "anciennete")
    private Double anciennete;
    @Size(max = 2147483647)
    @Column(name = "unite_periode_preavis")
    private String unitePeriodePreavis;
    @JoinColumn(name = "contrat", referencedColumnName = "id")
    @OneToOne
    private YvsGrhContratEmps contrat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_save")
    private Date dateSave;
    @OneToMany(mappedBy = "contratSuspendu")
    private List<YvsGrhElementsIndemnite> indemnites;

    public YvsGrhContratSuspendu() {
    }

    public YvsGrhContratSuspendu(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Date getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(Date dateEffet) {
        this.dateEffet = dateEffet;
    }

    public Double getDureePreavis() {
        return dureePreavis;
    }

    public void setDureePreavis(Double dureePreavis) {
        this.dureePreavis = dureePreavis;
    }

    public Double getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(Double anciennete) {
        this.anciennete = anciennete;
    }

    public String getUnitePeriodePreavis() {
        return unitePeriodePreavis;
    }

    public void setUnitePeriodePreavis(String unitePeriodePreavis) {
        this.unitePeriodePreavis = unitePeriodePreavis;
    }

    public YvsGrhContratEmps getContrat() {
        return contrat;
    }

    public void setContrat(YvsGrhContratEmps contrat) {
        this.contrat = contrat;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public List<YvsGrhElementsIndemnite> getIndemnites() {
        return indemnites;
    }

    public void setIndemnites(List<YvsGrhElementsIndemnite> indemnites) {
        this.indemnites = indemnites;
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
        if (!(object instanceof YvsGrhContratSuspendu)) {
            return false;
        }
        YvsGrhContratSuspendu other = (YvsGrhContratSuspendu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.contrat.YvsGrhContratSuspendu[ id=" + id + " ]";
    }

}
