/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_bulletin")
@NamedQueries({
    @NamedQuery(name = "YvsBulletin.findAll", query = "SELECT y FROM YvsBulletin y"),
    @NamedQuery(name = "YvsBulletin.findById", query = "SELECT y FROM YvsBulletin y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBulletin.findBySalaireHoraire", query = "SELECT y FROM YvsBulletin y WHERE y.salaireHoraire = :salaireHoraire"),
    @NamedQuery(name = "YvsBulletin.findByHoraire", query = "SELECT y FROM YvsBulletin y WHERE y.horaire = :horaire"),
    @NamedQuery(name = "YvsBulletin.findByHoraireHebdo", query = "SELECT y FROM YvsBulletin y WHERE y.horaireHebdo = :horaireHebdo"),
    @NamedQuery(name = "YvsBulletin.findByEmploye", query = "SELECT y FROM YvsBulletin y WHERE y.employe = :employe AND y.actif=true"),
    @NamedQuery(name = "YvsBulletin.countB", query = "SELECT COUNT(y) FROM YvsBulletin y WHERE y.employe = :employe AND y.actif=true AND y.horaire=:h AND y.horaireHebdo=:hh AND y.modeDePayement=:mdp AND y.salaireHoraire=:sh AND y.salaireMens=:sm"),
    @NamedQuery(name = "YvsBulletin.findByActif", query = "SELECT y FROM YvsBulletin y WHERE y.actif = :actif")})
public class YvsBulletin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_bulletin_id_seq")
    @SequenceGenerator(sequenceName = "yvs_bulletin_id_seq", allocationSize = 1, name = "yvs_bulletin_id_seq")
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
    @Column(name = "mode_de_payement")
    private String modeDePayement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salaire_horaire")
    private Double salaireHoraire;
    @Column(name = "salaire_mens")
    private Double salaireMens;
    @Column(name = "horaire")
    private Double horaire;
    @Column(name = "horaire_hebdo")
    private Double horaireHebdo;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsBulletin() {
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

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModeDePayement() {
        return modeDePayement;
    }

    public void setModeDePayement(String modeDePayement) {
        this.modeDePayement = modeDePayement;
    }

    public Double getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(Double salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public Double getSalaireMens() {
        return salaireMens;
    }

    public void setSalaireMens(Double salaireMens) {
        this.salaireMens = salaireMens;
    }

    public Double getHoraire() {
        return horaire;
    }

    public void setHoraire(Double horaire) {
        this.horaire = horaire;
    }

    public Double getHoraireHebdo() {
        return horaireHebdo;
    }

    public void setHoraireHebdo(Double horaireHebdo) {
        this.horaireHebdo = horaireHebdo;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final YvsBulletin other = (YvsBulletin) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsBulletin[ id=" + id + " ]";
    }

}
