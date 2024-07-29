/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.validation.constraints.Size;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_phase_reglement")
@NamedQueries({
    @NamedQuery(name = "YvsComptaPhaseReglement.findAll", query = "SELECT y FROM YvsComptaPhaseReglement y"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findById", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByMode", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.modeReglement = :mode"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findPrecByMode", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.modeReglement = :mode AND y.numeroPhase < :numero AND y.actif=true ORDER BY y.numeroPhase DESC"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByModeEmission", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.modeReglement = :mode AND y.forEmission = :emission AND y.actif=true"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByPhase", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.phase = :phase"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByReglementOk", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.reglementOk = :reglementOk AND y.actif=true"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByNumeroPhase", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.numeroPhase = :numeroPhase AND y.actif=true"),
    @NamedQuery(name = "YvsComptaPhaseReglement.findByActif", query = "SELECT y FROM YvsComptaPhaseReglement y WHERE y.actif = :actif")})
public class YvsComptaPhaseReglement implements Serializable, Comparator<YvsComptaPhaseReglement> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_phase_reglement_id_seq", name = "yvs_compta_phase_reglement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_phase_reglement_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "phase")
    private String phase;
    @Column(name = "reglement_ok")
    private Boolean reglementOk;
    @Column(name = "action_in_banque")
    private Boolean actionInBanque;
    @Column(name = "numero_phase")
    private Integer numeroPhase;
    @Column(name = "piece_ok")
    private Boolean pieceOk;
    @Column(name = "for_emission")
    private Boolean forEmission;
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "code_comptable")
    private String codeComptable;   //code alphanumérique qui nous permetra de faire le lien avec un code spécifique d'une banque 
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "mode_reglement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement modeReglement;

    @OneToMany(mappedBy = "etapeValide")
    private List<YvsComptaPhaseReglementAutorisation> autorisations;

    public YvsComptaPhaseReglement() {
        autorisations = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsComptaPhaseReglement(Long id) {
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Boolean getReglementOk() {
        return reglementOk != null ? reglementOk : false;
    }

    public void setReglementOk(Boolean reglementOk) {
        this.reglementOk = reglementOk;
    }

    public Boolean getActionInBanque() {
        return actionInBanque != null ? actionInBanque : false;
    }

    public void setActionInBanque(Boolean actionInBanque) {
        this.actionInBanque = actionInBanque;
    }

    public Integer getNumeroPhase() {
        return numeroPhase != null ? numeroPhase : 0;
    }

    public void setNumeroPhase(Integer numeroPhase) {
        this.numeroPhase = numeroPhase;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsBaseModeReglement getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(YvsBaseModeReglement modeReglement) {
        this.modeReglement = modeReglement;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCodeComptable() {
        return codeComptable;
    }

    public void setCodeComptable(String codeComptable) {
        this.codeComptable = codeComptable;
    }

    public List<YvsComptaPhaseReglementAutorisation> getAutorisations() {
        return autorisations;
    }

    public void setAutorisations(List<YvsComptaPhaseReglementAutorisation> autorisations) {
        this.autorisations = autorisations;
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
        if (!(object instanceof YvsComptaPhaseReglement)) {
            return false;
        }
        YvsComptaPhaseReglement other = (YvsComptaPhaseReglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaPhaseReglement[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsComptaPhaseReglement o1, YvsComptaPhaseReglement o2) {
        return o1.getNumeroPhase().compareTo(o2.getNumeroPhase());
    }

    public Boolean getPieceOk() {
        return pieceOk;
    }

    public void setPieceOk(Boolean pieceOk) {
        this.pieceOk = pieceOk;
    }

    public Boolean getForEmission() {
        return forEmission != null ? forEmission : false;
    }

    public void setForEmission(Boolean forEmission) {
        this.forEmission = forEmission;
    }

}
