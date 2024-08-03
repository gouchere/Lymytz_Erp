/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_formation_emps")
@NamedQueries({
    @NamedQuery(name = "YvsFormationEmps.findAll", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsFormationEmps.findById", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.id = :id"),
    @NamedQuery(name = "YvsFormationEmps.findByDateFormation", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.dateFormation = :dateFormation"),
    @NamedQuery(name = "YvsFormationEmps.findByDate", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.dateDebut<=:date AND y.dateFin>=:date AND y.employe.agence.societe=:societe"),
    @NamedQuery(name = "YvsFormationEmps.findByDateDebut", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsFormationEmps.findByDateFin", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.dateFin = :dateFin"),
    @NamedQuery(name = "YvsFormationEmps.findByEmploye", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.employe = :employe"),
    @NamedQuery(name = "YvsFormationEmps.findByEmployesDate", query = "SELECT y.employe FROM YvsGrhFormationEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.formation.societe=:societe"),
    @NamedQuery(name = "YvsGrhFormationEmps.findByOneEmployeDate", query = "SELECT y.employe FROM YvsGrhFormationEmps y WHERE (:date BETWEEN y.dateDebut AND y.dateFin) AND y.employe=:employe"),
    @NamedQuery(name = "YvsFormationEmps.findByObtentionDiplome", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.obtentionDiplome = :obtentionDiplome"),
    @NamedQuery(name = "YvsFormationEmps.findByValider", query = "SELECT y FROM YvsGrhFormationEmps y WHERE y.valider = :valider")})
public class YvsGrhFormationEmps implements Serializable {

    @OneToMany(mappedBy = "yvsFormationEmps")
    private List<YvsCoutsFormation> couts;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_formation_emps_id_seq", name = "yvs_formation_emps_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_formation_emps_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "date_formation")
    @Temporal(TemporalType.DATE)
    private Date dateFormation;
    @Column(name = "valider")
    private Boolean valider;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "obtention_diplome")
    private Boolean obtentionDiplome;
    @JoinColumn(name = "formation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhFormation formation;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;
    @JoinColumn(name = "lieu", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire lieu;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhFormationEmps() {
    }

    public YvsGrhFormationEmps(Long id) {
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
        this.dateFormation = dateFormation;
    }

    public Boolean getValider() {
        return valider;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
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

    public Boolean getObtentionDiplome() {
        return obtentionDiplome;
    }

    public void setObtentionDiplome(Boolean obtentionDiplome) {
        this.obtentionDiplome = obtentionDiplome;
    }

    public YvsGrhFormation getFormation() {
        return formation;
    }

    public void setFormation(YvsGrhFormation formation) {
        this.formation = formation;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    public YvsDictionnaire getLieu() {
        return lieu;
    }

    public void setLieu(YvsDictionnaire lieu) {
        this.lieu = lieu;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsGrhFormationEmps)) {
            return false;
        }
        YvsGrhFormationEmps other = (YvsGrhFormationEmps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsFormationEmps[ id=" + id + " ]";
    }

    public List<YvsCoutsFormation> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsCoutsFormation> couts) {
        this.couts = couts;
    }

}
