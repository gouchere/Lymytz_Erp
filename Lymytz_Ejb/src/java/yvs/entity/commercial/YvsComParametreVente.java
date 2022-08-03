/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial;

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
import javax.validation.constraints.Size;
import yvs.dao.YvsEntity;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_com_parametre_vente")
@NamedQueries({
    @NamedQuery(name = "YvsComParametreVente.findAll", query = "SELECT y FROM YvsComParametreVente y"),
    @NamedQuery(name = "YvsComParametreVente.findByAgence", query = "SELECT y FROM YvsComParametreVente y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreVente.findnbJourAnByAgence", query = "SELECT y.jourAnterieur FROM YvsComParametreVente y WHERE y.agence = :agence"),
    @NamedQuery(name = "YvsComParametreVente.findById", query = "SELECT y FROM YvsComParametreVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComParametreVente.findByLivreAuto", query = "SELECT y FROM YvsComParametreVente y WHERE y.comptabilisationAuto = :comptabilisationAuto"),
    @NamedQuery(name = "YvsComParametreVente.findByEtatMouvStock", query = "SELECT y FROM YvsComParametreVente y WHERE y.comptabilisationMode = :comptabilisationMode"),
    @NamedQuery(name = "YvsComParametreVente.findByJourAnterieur", query = "SELECT y FROM YvsComParametreVente y WHERE y.jourAnterieur = :jourAnterieur"),

    @NamedQuery(name = "YvsComParametreVente.findJournalByAgence", query = "SELECT y.journal FROM YvsComParametreVente y WHERE y.agence = :agence")})
public class YvsComParametreVente extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_parametre_vente_id_seq", name = "yvs_com_parametre_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_parametre_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "comptabilisation_auto")
    private Boolean comptabilisationAuto;
    @Column(name = "paie_without_valide")
    private Boolean paieWithoutValide;
    @Size(max = 2147483647)
    @Column(name = "comptabilisation_mode")
    private String comptabilisationMode;
    @Column(name = "generer_facture_auto")
    private Boolean genererFactureAuto;
    @Column(name = "livre_bcv_without_paye")
    private Boolean livreBcvWithoutPaye;
    @Column(name = "give_bonus_in_status")
    private String giveBonusInStatus;
    @Column(name = "sell_lower_pr")
    private Boolean sellLowerPr;
    @Column(name = "jour_anterieur")
    private Integer jourAnterieur;
    @Column(name = "model_facture_vente")
    private String modelFactureVente;
    @Column(name = "nb_fiche_max")
    private Integer nbFicheMax;// Utile pour déterminer le nombre max de fiches non cloturé qu'on ne dois pas pouvoir ouvrir à la fois
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "journal", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaJournaux journal;

    public YvsComParametreVente() {
    }

    public YvsComParametreVente(Long id) {
        this.id = id;
    }

    @Override
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

    public Boolean getSellLowerPr() {
        return sellLowerPr != null ? sellLowerPr : true;
    }

    public void setSellLowerPr(Boolean sellLowerPr) {
        this.sellLowerPr = sellLowerPr;
    }

    public String getModelFactureVente() {
        return modelFactureVente != null ? !modelFactureVente.trim().isEmpty() ? modelFactureVente : "facture_vente" : "facture_vente";
    }

    public void setModelFactureVente(String modelFactureVente) {
        this.modelFactureVente = modelFactureVente;
    }

    public Boolean getComptabilisationAuto() {
        return comptabilisationAuto != null ? comptabilisationAuto : false;
    }

    public void setComptabilisationAuto(Boolean comptabilisationAuto) {
        this.comptabilisationAuto = comptabilisationAuto;
    }

    public Boolean getGenererFactureAuto() {
        return genererFactureAuto != null ? genererFactureAuto : false;
    }

    public void setGenererFactureAuto(Boolean genererFactureAuto) {
        this.genererFactureAuto = genererFactureAuto;
    }

    public Boolean getLivreBcvWithoutPaye() {
        return livreBcvWithoutPaye != null ? livreBcvWithoutPaye : false;
    }

    public void setLivreBcvWithoutPaye(Boolean livreBcvWithoutPaye) {
        this.livreBcvWithoutPaye = livreBcvWithoutPaye;
    }

    public String getComptabilisationMode() {
        return comptabilisationMode != null ? comptabilisationMode.trim().length() > 0 ? comptabilisationMode : "D" : "D";
    }

    public void setComptabilisationMode(String comptabilisationMode) {
        this.comptabilisationMode = comptabilisationMode;
    }

    public String getGiveBonusInStatus() {
        return giveBonusInStatus != null ? giveBonusInStatus.trim().length() > 0 ? giveBonusInStatus : "R" : "R";
    }

    public void setGiveBonusInStatus(String giveBonusInStatus) {
        this.giveBonusInStatus = giveBonusInStatus;
    }

    public Integer getJourAnterieur() {
        return jourAnterieur != null ? jourAnterieur : 0;
    }

    public void setJourAnterieur(Integer jourAnterieur) {
        this.jourAnterieur = jourAnterieur;
    }

    public Boolean getPaieWithoutValide() {
        return paieWithoutValide != null ? paieWithoutValide : true;
    }

    public void setPaieWithoutValide(Boolean paieWithoutValide) {
        this.paieWithoutValide = paieWithoutValide;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public Integer getNbFicheMax() {
        return nbFicheMax != null ? nbFicheMax : 0;
    }

    public void setNbFicheMax(Integer nbFicheMax) {
        this.nbFicheMax = nbFicheMax;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
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
        if (!(object instanceof YvsComParametreVente)) {
            return false;
        }
        YvsComParametreVente other = (YvsComParametreVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComParametreVente[ id=" + id + " ]";
    }

}
