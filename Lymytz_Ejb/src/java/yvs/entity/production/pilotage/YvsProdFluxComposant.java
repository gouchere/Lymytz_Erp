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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_flux_composant")
@NamedQueries({
    @NamedQuery(name = "YvsProdFluxComposant.findAll", query = "SELECT y FROM YvsProdFluxComposant y"),
    @NamedQuery(name = "YvsProdFluxComposant.findById", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdFluxComposant.findSomMargeByComposant", query = "SELECT SUM(COALESCE(y.margeQte,0)) FROM YvsProdFluxComposant y WHERE y.composant=:composant AND y.sens=:sens"),
    @NamedQuery(name = "YvsProdFluxComposant.findByQuantite", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.quantite = :quantite"),
    @NamedQuery(name = "YvsProdFluxComposant.findSumQuantiteCompossant", query = "SELECT SUM(y.quantite) FROM YvsProdFluxComposant y WHERE y.composant = :composant"),
    @NamedQuery(name = "YvsProdFluxComposant.findSumCompossantNotIn", query = "SELECT SUM(y.quantite) FROM YvsProdFluxComposant y WHERE y.composant = :composant AND y.operation.statutOp != 'T'"),
    @NamedQuery(name = "YvsProdFluxComposant.findSumByComposantStatut", query = "SELECT SUM((y.quantite * y.tauxComposant)/100) FROM YvsProdFluxComposant y WHERE y.composant = :composant AND y.operation.statutOp = :statut"),
    @NamedQuery(name = "YvsProdFluxComposant.findByComposant", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.composant = :composant"),
    @NamedQuery(name = "YvsProdFluxComposant.findByOpération", query = "SELECT y FROM YvsProdFluxComposant y JOIN FETCH y.composant JOIN FETCH y.composant.article JOIN FETCH y.composant.unite.unite WHERE y.operation = :operation ORDER BY y.composant.ordre"),
    @NamedQuery(name = "YvsProdFluxComposant.findOne", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.operation = :operation AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdFluxComposant.findByOrdre", query = "SELECT y FROM YvsProdFluxComposant y JOIN FETCH y.operation JOIN FETCH y.composant JOIN FETCH y.composant.article JOIN FETCH y.composant.unite JOIN FETCH y.composant.unite.unite "
            + " WHERE y.operation.ordreFabrication=:ordre"),
    @NamedQuery(name = "YvsProdFluxComposant.findByIdOf", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.operation.ordreFabrication = :ordre OR y.composant.ordreFabrication=:ordre"),
    @NamedQuery(name = "YvsProdFluxComposant.findBySens", query = "SELECT y FROM YvsProdFluxComposant y WHERE y.sens = :sens")})
public class YvsProdFluxComposant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(sequenceName = "yvs_prod_flux_composant_id_seq", name = "yvs_prod_flux_composant_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_flux_composant_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantite")
    private Double quantite;
    @Column(name = "quantite_perdue")
    private Double quantitePerdue;
    @Column(name = "taux_composant")
    private Double tauxComposant;
    @Column(name = "marge_qte")
    private Double margeQte ;
    @Column(name = "sens")
    private Character sens;
    @Column(name = "type_cout")
    private Character typeCout;
    @Column(name = "suivi")
    private Boolean suivi;//indique si c'est la valeur de  se composant qui indique le niveau d'évolution de l'opération    
    @Column(name = "type_suivi")
    private String typeSuivi;   //Quantitatif ou Qualitatif 
    @Column(name = "coeficient_valeur")
    private Double coeficientValeur;
    @Column(name = "ordre")
    private Integer ordre;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "date_save")
    private Date dateSave;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "date_update")
    private Date dateUpdate;


    @JoinColumn(name = "operation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOperationsOF operation;
    @JoinColumn(name = "composant", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdComposantOF composant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;
    
    @OneToMany(mappedBy = "composant",fetch = FetchType.LAZY)
    private List<YvsProdOfIndicateurSuivi> indicateursSuivis;
    @OneToMany(mappedBy = "composant",fetch = FetchType.LAZY)
    private List<YvsProdOfSuiviFlux> listeSuiviFlux;

    @Transient
    private boolean inListByDefault;

    public YvsProdFluxComposant() {
        listeSuiviFlux = new ArrayList<>();
    }

    public YvsProdFluxComposant(Long id) {
        this.id = id;
        listeSuiviFlux = new ArrayList<>();
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Character getSens() {
        return sens != null ? String.valueOf(sens).trim().length() > 0 ? sens : 'S' : 'S';
    }

    public void setSens(Character sens) {
        this.sens = sens;
    }

    public YvsProdOperationsOF getOperation() {
        return operation;
    }

    public void setOperation(YvsProdOperationsOF operation) {
        this.operation = operation;
    }

    public YvsProdComposantOF getComposant() {
        return composant;
    }

    public void setComposant(YvsProdComposantOF composant) {
        this.composant = composant;
    }

    public boolean isInListByDefault() {
        return inListByDefault;
    }

    public void setInListByDefault(boolean inListByDefault) {
        this.inListByDefault = inListByDefault;
    }

    public Double getTauxComposant() {
        return tauxComposant != null ? tauxComposant : 1;
    }

    public void setTauxComposant(Double tauxComposant) {
        this.tauxComposant = tauxComposant;
    }

    /**
     * Porte une valeur si suivi est actif*
     */
    public String getTypeSuivi() {
        return typeSuivi;
    }

    public void setSuivi(Boolean suivi) {
        this.suivi = suivi;
    }

    public Boolean getSuivi() {
        return suivi != null ? suivi : false;
    }

    public void setTypeSuivi(String typeSuivi) {
        this.typeSuivi = typeSuivi;
    }

    public double getQuantiteResteFlux() {
        double re = 0;
        for (YvsProdOfSuiviFlux s : listeSuiviFlux) {
            re += s.getQuantite();
        }
        return getQuantite() - re;
    }

    public double getQuantiteFlux() {
        double re = 0;
        for (YvsProdOfSuiviFlux s : listeSuiviFlux) {
            re += s.getQuantite();
        }
        return re;
    }

    public Double getMargeQte() {
        return margeQte != null ? margeQte : 0;
    }

    public void setMargeQte(Double margeQte) {
        this.margeQte = margeQte;
    }

    public Double getCoeficientValeur() {
        return coeficientValeur != null ? coeficientValeur : 1d;
    }

    public void setCoeficientValeur(Double coeficientValeur) {
        this.coeficientValeur = coeficientValeur;
    }

    public Character getTypeCout() {
        return typeCout != null ? typeCout : 'P'; //par défaut on emploie un type proportionnelle
    }

    public void setTypeCout(Character typeCout) {
        this.typeCout = typeCout;
    }

    public Double getQuantitePerdue() {
        return quantitePerdue != null ? quantitePerdue : 0;
    }

    public void setQuantitePerdue(Double quantitePerdue) {
        this.quantitePerdue = quantitePerdue;
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

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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
        if (!(object instanceof YvsProdFluxComposant)) {
            return false;
        }
        YvsProdFluxComposant other = (YvsProdFluxComposant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdFluxComposant[ id=" + id + " ]";
    }

    public List<YvsProdOfIndicateurSuivi> getIndicateursSuivis() {
        return indicateursSuivis;
    }

    public void setIndicateursSuivis(List<YvsProdOfIndicateurSuivi> indicateursSuivis) {
        this.indicateursSuivis = indicateursSuivis;
    }

    public List<YvsProdOfSuiviFlux> getListeSuiviFlux() {
        return listeSuiviFlux;
    }

    public void setListeSuiviFlux(List<YvsProdOfSuiviFlux> listeSuiviFlux) {
        this.listeSuiviFlux = listeSuiviFlux;
    }

}
