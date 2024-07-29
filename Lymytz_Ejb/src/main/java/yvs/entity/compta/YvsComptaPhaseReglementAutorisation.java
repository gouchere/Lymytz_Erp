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
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_phase_reglement_autorisation")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhaseReglementAutorisation.findAll", query = "SELECT y FROM YvsComptaPhaseReglementAutorisation y"),
    @NamedQuery(name = "YvsComptaPhaseReglementAutorisation.findById", query = "SELECT y FROM YvsComptaPhaseReglementAutorisation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhaseReglementAutorisation.findByCanValide", query = "SELECT y FROM YvsComptaPhaseReglementAutorisation y WHERE y.canValide = :canValide")})
public class YvsComptaPhaseReglementAutorisation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_reglement_autorisation_id_seq", name = "yvs_compta_phase_reglement_autorisation_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_reglement_autorisation_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "can_valide")
    private Boolean canValide;
    @Column(name = "can_notify")
    private Boolean canNotify;
    @JoinColumn(name = "etape_valide", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaPhaseReglement etapeValide;
    @JoinColumn(name = "niveau_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsNiveauAcces niveauAcces;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComptaPhaseReglementAutorisation() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPhaseReglementAutorisation(Long id) {
        this();
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

    public Boolean getCanValide() {
        return canValide != null ? canValide : false;
    }

    public void setCanValide(Boolean canValide) {
        this.canValide = canValide;
    }

    public YvsComptaPhaseReglement getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(YvsComptaPhaseReglement etapeValide) {
        this.etapeValide = etapeValide;
    }

    public YvsNiveauAcces getNiveauAcces() {
        return niveauAcces;
    }

    public void setNiveauAcces(YvsNiveauAcces niveauAcces) {
        this.niveauAcces = niveauAcces;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getCanNotify() {
        return canNotify != null ? canNotify : false;
    }

    public void setCanNotify(Boolean canNotify) {
        this.canNotify = canNotify;
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
        if (!(object instanceof YvsComptaPhaseReglementAutorisation)) {
            return false;
        }
        YvsComptaPhaseReglementAutorisation other = (YvsComptaPhaseReglementAutorisation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsComptaPhaseReglementAutorisation[ id=" + id + " ]";
    }

}
