/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_fiche_conditionnement")
@NamedQueries({
    @NamedQuery(name = "YvsProdFicheConditionnement.findAll", query = "SELECT y FROM YvsProdFicheConditionnement y"),
    @NamedQuery(name = "YvsProdFicheConditionnement.findById", query = "SELECT y FROM YvsProdFicheConditionnement y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdFicheConditionnement.findByDateConditionnement", query = "SELECT y FROM YvsProdFicheConditionnement y WHERE y.dateConditionnement = :dateConditionnement"),
    @NamedQuery(name = "YvsProdFicheConditionnement.findByQuantite", query = "SELECT y FROM YvsProdFicheConditionnement y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdFicheConditionnement.findByDateSave", query = "SELECT y FROM YvsProdFicheConditionnement y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdFicheConditionnement.findByDateUpdate", query = "SELECT y FROM YvsProdFicheConditionnement y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdFicheConditionnement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_fiche_conditionnement_id_seq", name = "yvs_prod_fiche_conditionnement_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_fiche_conditionnement_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_conditionnement")
    @Temporal(TemporalType.DATE)
    private Date dateConditionnement;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "numero")
    private String numero;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "nomenclature", referencedColumnName = "id")
    @ManyToOne
    private YvsProdNomenclature nomenclature;
    @JoinColumn(name = "depot", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseDepots depot;
    @OneToMany(mappedBy = "fiche")
    private List<YvsProdContenuConditionnement> contenus;

    @Transient
    private List<YvsProdConditionnementDeclaration> declarations;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdFicheConditionnement() {
        contenus = new ArrayList<>();
        declarations = new ArrayList<>();
    }

    public YvsProdFicheConditionnement(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero != null ? numero : "";
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateConditionnement() {
        return dateConditionnement != null ? dateConditionnement : new Date();
    }

    public void setDateConditionnement(Date dateConditionnement) {
        this.dateConditionnement = dateConditionnement;
    }

    public Character getStatut() {
        return statut != null ? String.valueOf(statut).trim().length() > 0 ? statut : Constantes.STATUT_DOC_EDITABLE : Constantes.STATUT_DOC_EDITABLE;
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public List<YvsProdContenuConditionnement> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsProdContenuConditionnement> contenus) {
        this.contenus = contenus;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdNomenclature getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(YvsProdNomenclature nomenclature) {
        this.nomenclature = nomenclature;
    }

    public YvsBaseDepots getDepot() {
        return depot;
    }

    public void setDepot(YvsBaseDepots depot) {
        this.depot = depot;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<YvsProdConditionnementDeclaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<YvsProdConditionnementDeclaration> declarations) {
        this.declarations = declarations;
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
        if (!(object instanceof YvsProdFicheConditionnement)) {
            return false;
        }
        YvsProdFicheConditionnement other = (YvsProdFicheConditionnement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdFicheConditionnement[ id=" + id + " ]";
    }

    public boolean canDelete() {
        return getStatut().equals(Constantes.STATUT_DOC_ATTENTE) || getStatut().equals(Constantes.STATUT_DOC_EDITABLE) || getStatut().equals(Constantes.STATUT_DOC_SUSPENDU) || getStatut().equals(Constantes.STATUT_DOC_ANNULE);
    }

}
