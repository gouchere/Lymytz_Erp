/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

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
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.param.workflow.YvsWorkflowValidApprovissionnement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_fiche_approvisionnement")
@NamedQueries({
    @NamedQuery(name = "YvsComFicheApprovisionnement.findAll", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot.agence.societe = :societe ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.countAll", query = "SELECT COUNT(y) FROM YvsComFicheApprovisionnement y WHERE y.depot.agence.societe = :societe"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByIds", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.id in :ids"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByReference", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot.agence.societe = :societe AND y.reference LIKE :reference ORDER by y.reference DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByAgenceReference", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot.agence = :agence AND y.depot.agence.societe = :societe AND y.reference LIKE :reference ORDER by y.reference DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByDepotReference", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot = :depot AND y.depot.agence.societe = :societe AND y.reference LIKE :reference ORDER by y.reference DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByDepotDate", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot = :depot AND y.dateApprovisionnement = :dateApprovisionnement ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByDepotDateStatut", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot = :depot AND y.dateApprovisionnement = :dateApprovisionnement AND y.etat = :statut ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByDepotStatutAuto", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot = :depot AND y.auto = :auto AND y.etat = :statut ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByEtat", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot.agence.societe = :societe AND y.etat = :etat ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByNoEtat", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.depot.agence.societe = :societe AND y.etat != :etat ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findById", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.id = :id ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByDateApprovisionnement", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.dateApprovisionnement = :dateApprovisionnement ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC"),
    @NamedQuery(name = "YvsComFicheApprovisionnement.findByHeureApprovisionnement", query = "SELECT y FROM YvsComFicheApprovisionnement y WHERE y.heureApprovisionnement = :heureApprovisionnement ORDER BY y.dateApprovisionnement DESC, y.heureApprovisionnement DESC")})
public class YvsComFicheApprovisionnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_fiche_approvisionnement_id_seq", name = "yvs_com_fiche_approvisionnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_fiche_approvisionnement_id_seq_name", strategy = GenerationType.SEQUENCE)  
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_approvisionnement")
    @Temporal(TemporalType.DATE)
    private Date dateApprovisionnement;
    @Column(name = "heure_approvisionnement")
    @Temporal(TemporalType.TIME)
    private Date heureApprovisionnement;
    @Column(name = "etat")
    private String etat;
    @Column(name = "statut_terminer")
    private String statutTerminer;
    @Column(name = "auto")
    private Boolean auto;
    @Column(name = "reference")
    private String reference;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;

    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depot;
    @JoinColumn(name = "creneau", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComCreneauDepot creneau;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "fiche")
    private List<YvsComArticleApprovisionnement> articles;
    @OneToMany(mappedBy = "document")
    private List<YvsWorkflowValidApprovissionnement> etapesValidations;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private boolean int_;
    @Transient
    private String libEtapes;
    @Transient
    private String maDateSave;

    public YvsComFicheApprovisionnement() {
        articles = new ArrayList<>();
        etapesValidations = new ArrayList<>();
    }

    public YvsComFicheApprovisionnement(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsComFicheApprovisionnement(YvsBaseDepots depot) {
        this.depot = depot;
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

    public Boolean getAuto() {
        return auto != null ? auto : false;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public Date getDateCloturer() {
        return dateCloturer;
    }

    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsComCreneauDepot getCreneau() {
        return creneau;
    }

    public void setCreneau(YvsComCreneauDepot creneau) {
        this.creneau = creneau;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    public String getStatutTerminer() {
        return statutTerminer != null ? statutTerminer.trim().length() > 0 ? statutTerminer : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutTerminer(String statutTerminer) {
        this.statutTerminer = statutTerminer;
    }

    public String getEtat() {
        return etat != null ? etat.trim().length() > 0 ? etat : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public String getReference() {
        reference = reference != null ? reference : "";
        reference = reference.trim().length() > 0 ? reference : (getAuto() ? ("FICH AUTO (Depot : " + (getDepot() != null ? getDepot().getDesignation() : "EMPTY") + ")") : "");
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateApprovisionnement() {
        return dateApprovisionnement;
    }

    public void setDateApprovisionnement(Date dateApprovisionnement) {
        this.dateApprovisionnement = dateApprovisionnement;
    }

    public Date getHeureApprovisionnement() {
        return heureApprovisionnement;
    }

    public void setHeureApprovisionnement(Date heureApprovisionnement) {
        this.heureApprovisionnement = heureApprovisionnement;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsComArticleApprovisionnement> getArticles() {
        return articles;
    }

    public void setArticles(List<YvsComArticleApprovisionnement> articles) {
        this.articles = articles;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsWorkflowValidApprovissionnement> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidApprovissionnement> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
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
        if (!(object instanceof YvsComFicheApprovisionnement)) {
            return false;
        }
        YvsComFicheApprovisionnement other = (YvsComFicheApprovisionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComFicheApprovisionnement[ id=" + id + " ]";
    }

}
