/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdIndicateurOp;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_composant_op")
@NamedQueries({
    @NamedQuery(name = "YvsProdComposantOp.findAll", query = "SELECT y FROM YvsProdComposantOp y"),
    @NamedQuery(name = "YvsProdComposantOp.findById", query = "SELECT y FROM YvsProdComposantOp y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdComposantOp.findByOne", query = "SELECT y FROM YvsProdComposantOp y WHERE y.operation=:operation AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdComposantOp.findBySens", query = "SELECT y FROM YvsProdComposantOp y WHERE y.sens = :sens"),
    @NamedQuery(name = "YvsProdComposantOp.findByDateSave", query = "SELECT y FROM YvsProdComposantOp y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdComposantOp.findByDateUpdate", query = "SELECT y FROM YvsProdComposantOp y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProdComposantOp.findByOperation", query = "SELECT y FROM YvsProdComposantOp y JOIN FETCH y.composant JOIN FETCH y.composant.article "
            + " LEFT JOIN FETCH y.indicateurs WHERE y.operation = :operation"),
    @NamedQuery(name = "YvsProdComposantOp.findByRefOperation", query = "SELECT y FROM YvsProdComposantOp y JOIN FETCH y.composant JOIN FETCH y.composant.article "
            + "WHERE y.operation.codeRef = :operation AND y.operation.gammeArticle=:gamme"),
    @NamedQuery(name = "YvsProdComposantOp.findByRefOperationAndComp", query = "SELECT y FROM YvsProdComposantOp y JOIN FETCH y.composant JOIN FETCH y.composant.article "
            + "WHERE y.operation.codeRef = :operation AND y.composant.article=:composant"),
    @NamedQuery(name = "YvsProdComposantOp.findSumByComposant", query = "SELECT SUM(y.quantite) FROM YvsProdComposantOp y WHERE y.composant = :composant AND y.operation.gammeArticle=:gamme AND y.sens=:sens"),
    @NamedQuery(name = "YvsProdComposantOp.findSumByGammeComposant", query = "SELECT SUM(y.quantite) FROM YvsProdComposantOp y WHERE y.composant = :composant AND y.operation.gammeArticle = :gamme AND y.sens=:sens")
})
public class YvsProdComposantOp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_composant_op_id_seq", name = "yvs_prod_composant_op_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_composant_op_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "sens")
    private Character sens;
    @Column(name = "type_cout")
    private Character typeCout; //indique comment est evalué le coût des sous produit //Proportionnelle, total
    @Column(name = "quantite")
    private Double quantite;    //(En pourcentage de la quantité de composant indiqué à la nomenclature)
    @Column(name = "taux_perte")
    private Double tauxPerte;   //Taux de perte en pourcentage
    @Column(name = "marge_qte")
    private Double margeQte;    //(En pourcentage de la quantité de composant indiqué à la nomenclature)
    @Column(name = "marge_sup")
    private Double margeSup;    //(En pourcentage de la quantité de composant indiqué à la nomenclature)
    @Column(name = "coeficient_valeur")
    private Double coeficientValeur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JoinColumn(name = "operation", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOperationsGamme operation;
    @JoinColumn(name = "composant", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdComposantNomenclature composant;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToOne(mappedBy = "composantOp")
    private YvsProdIndicateurOp indicateurs;

    public YvsProdComposantOp() {
    }

    public YvsProdComposantOp(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getSens() {
        return sens != null ? sens : 'N';
    }

    public void setSens(Character sens) {
        this.sens = sens;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsProdOperationsGamme getOperation() {
        return operation;
    }

    public void setOperation(YvsProdOperationsGamme operation) {
        this.operation = operation;
    }

    public YvsProdComposantNomenclature getComposant() {
        return composant;
    }

    public void setComposant(YvsProdComposantNomenclature composant) {
        this.composant = composant;
    }

    public Double getQuantite() {
        return quantite != null ? quantite : 0;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public YvsProdIndicateurOp getIndicateurs() {
        return indicateurs;
    }

    public void setIndicateurs(YvsProdIndicateurOp indicateurs) {
        this.indicateurs = indicateurs;
    }

    public Double getMargeQte() {
        return margeQte != null ? margeQte : 0;
    }

    public void setMargeQte(Double margeQte) {
        this.margeQte = margeQte;
    }

    public Double getMargeSup() {
        return margeSup != null ? margeSup : 0;
    }

    public void setMargeSup(Double margeSup) {
        this.margeSup = margeSup;
    }

    public Double getCoeficientValeur() {
        return coeficientValeur != null ? coeficientValeur : 0;
    }

    public void setCoeficientValeur(Double coeficientValeur) {
        this.coeficientValeur = coeficientValeur;
    }

    public Double getTauxPerte() {
        return tauxPerte != null ? tauxPerte : 0;
    }

    public void setTauxPerte(Double tauxPerte) {
        this.tauxPerte = tauxPerte;
    }

    public Character getTypeCout() {
        return typeCout != null ? typeCout : 'P'; //par défaut on emploie un type proportionnelle
    }

    public void setTypeCout(Character typeCout) {
        this.typeCout = typeCout;
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
        if (!(object instanceof YvsProdComposantOp)) {
            return false;
        }
        YvsProdComposantOp other = (YvsProdComposantOp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdComposantOp[ id=" + id + " ]";
    }

}
