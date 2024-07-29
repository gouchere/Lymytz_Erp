/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_detail_grille_frai_mission")
@NamedQueries({
    @NamedQuery(name = "YvsGrhDetailGrilleFraiMission.findAll", query = "SELECT y FROM YvsGrhDetailGrilleFraiMission y"),
    @NamedQuery(name = "YvsGrhDetailGrilleFraiMission.findByIntituleCout", query = "SELECT y FROM YvsGrhDetailGrilleFraiMission y WHERE y.typeCout.libelle=:libelle AND y.grilleMission=:grille"),
    @NamedQuery(name = "YvsGrhDetailGrilleFraiMission.findByTypeCout", query = "SELECT y FROM YvsGrhDetailGrilleFraiMission y WHERE y.typeCout = :typeCout"),
    @NamedQuery(name = "YvsGrhDetailGrilleFraiMission.findByGrille", query = "SELECT y FROM YvsGrhDetailGrilleFraiMission y WHERE y.grilleMission = :grille")})
public class YvsGrhDetailGrilleFraiMission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_detail_grille_frai_mission_id_seq", name = "yvs_grh_detail_grille_frai_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_detail_grille_frai_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "montant_prevu")
    private Double montantPrevu;
    @JoinColumn(name = "grille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhGrilleMission grilleMission;
    @JoinColumn(name = "type_cout", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTypeCout typeCout;
    @Column(name = "proportionel_duree")
    private Boolean proportionelDuree;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhDetailGrilleFraiMission() {
    }

    public YvsGrhDetailGrilleFraiMission(Long id) {
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

    public Double getMontantPrevu() {
        return montantPrevu;
    }

    public void setMontantPrevu(Double montantPrevu) {
        this.montantPrevu = montantPrevu;
    }

    public YvsGrhGrilleMission getGrilleMission() {
        return grilleMission;
    }

    public void setGrilleMission(YvsGrhGrilleMission grilleMission) {
        this.grilleMission = grilleMission;
    }

    public YvsGrhTypeCout getTypeCout() {
        return typeCout;
    }

    public void setTypeCout(YvsGrhTypeCout typeCout) {
        this.typeCout = typeCout;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getProportionelDuree() {
        return proportionelDuree != null ? proportionelDuree : false;
    }

    public void setProportionelDuree(Boolean proportionelDuree) {
        this.proportionelDuree = proportionelDuree;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final YvsGrhDetailGrilleFraiMission other = (YvsGrhDetailGrilleFraiMission) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "YvsGrhDetailGrilleFraiMission{" + "id=" + id + '}';
    }

}
