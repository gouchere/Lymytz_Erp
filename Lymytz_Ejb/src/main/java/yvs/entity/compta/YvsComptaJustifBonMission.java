/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient; import com.fasterxml.jackson.annotation.JsonIgnore;import yvs.entity.compta.divers.YvsComptaBonProvisoire;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_compta_justif_bon_mission")
@NamedQueries({
    @NamedQuery(name = "YvsComptaJustifBonMission.findAll", query = "SELECT y FROM YvsComptaJustifBonMission y"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findById", query = "SELECT y FROM YvsComptaJustifBonMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findOne", query = "SELECT y FROM YvsComptaJustifBonMission y WHERE y.pieceMission = :mission AND y.piece = :piece"),
    @NamedQuery(name = "YvsComptaJustifBonMission.getTotalBon", query = "SELECT SUM(y.piece.montant) FROM YvsComptaJustifBonMission y WHERE y.pieceMission.mission = :mission AND y.piece.statut!=:statut"),
    @NamedQuery(name = "YvsComptaJustifBonMission.getTotalBonPaye", query = "SELECT SUM(y.piece.montant) FROM YvsComptaJustifBonMission y WHERE y.pieceMission.mission = :mission AND y.piece.statut!=:statut AND y.piece.statutPaiement=:statutP"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findByMission", query = "SELECT y FROM YvsComptaJustifBonMission y WHERE y.pieceMission.mission = :mission"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findIdBonByMission", query = "SELECT y.id FROM YvsComptaJustifBonMission y WHERE y.pieceMission.mission = :mission"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findByDateUpdate", query = "SELECT y FROM YvsComptaJustifBonMission y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComptaJustifBonMission.findByDateSave", query = "SELECT y FROM YvsComptaJustifBonMission y WHERE y.dateSave = :dateSave")})
public class YvsComptaJustifBonMission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_justif_bon_mission_id_seq", name = "yvs_compta_justif_bon_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_justif_bon_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "mission", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaissePieceMission pieceMission;
    @JoinColumn(name = "piece", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaBonProvisoire piece;

    public YvsComptaJustifBonMission() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaJustifBonMission(Long id) {
        this();
        this.id = id;
    }

    public YvsComptaJustifBonMission(YvsComptaCaissePieceMission mission, YvsComptaBonProvisoire piece) {
        this();
        this.pieceMission = mission;
        this.piece = piece;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsComptaCaissePieceMission getMission() {
        return pieceMission;
    }

    public void setMission(YvsComptaCaissePieceMission mission) {
        this.pieceMission = mission;
    }

    @XmlTransient  @JsonIgnore
    public YvsComptaBonProvisoire getPiece() {
        return piece;
    }

    public void setPiece(YvsComptaBonProvisoire piece) {
        this.piece = piece;
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
        if (!(object instanceof YvsComptaJustifBonMission)) {
            return false;
        }
        YvsComptaJustifBonMission other = (YvsComptaJustifBonMission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaJustifBonMission[ id=" + id + " ]";
    }

}
