/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_of_indicateur_suivi")
@NamedQueries({
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findAll", query = "SELECT y FROM YvsProdOfIndicateurSuivi y"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findById", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findByQuantiteBorne", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.quantiteBorne = :quantiteBorne"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findByValeurText", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.valeurText = :valeurText"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findByCodeCouleur", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.codeCouleur = :codeCouleur"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findOne", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.codeCouleur = :couleur AND y.composant=:composant"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findByDateSave", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdOfIndicateurSuivi.findByDateUpdate", query = "SELECT y FROM YvsProdOfIndicateurSuivi y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdOfIndicateurSuivi implements Serializable, Comparator<YvsProdOfIndicateurSuivi> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_of_indicateur_suivi_id_seq", name = "yvs_prod_of_indicateur_suivi_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_of_indicateur_suivi_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "quantite_borne")
    private Double quantiteBorne;
    @Size(max = 2147483647)
    @Column(name = "valeur_text")
    private String valeurText;
    @Size(max = 2147483647)
    @Column(name = "code_couleur")
    private String codeCouleur;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "composant", referencedColumnName = "id")
    @ManyToOne
    private YvsProdFluxComposant composant;

    public YvsProdOfIndicateurSuivi() {
    }

    public YvsProdOfIndicateurSuivi(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantiteBorne() {
        return quantiteBorne;
    }

    public void setQuantiteBorne(Double quantiteBorne) {
        this.quantiteBorne = quantiteBorne;
    }

    public String getValeurText() {
        return valeurText;
    }

    public void setValeurText(String valeurText) {
        this.valeurText = valeurText;
    }

    public String getCodeCouleur() {
        return codeCouleur;
    }

    public void setCodeCouleur(String codeCouleur) {
        this.codeCouleur = codeCouleur;
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

    public YvsProdFluxComposant getComposant() {
        return composant;
    }

    public void setComposant(YvsProdFluxComposant composant) {
        this.composant = composant;
    }

    public double giveTauxEvolution() {
        double re = 0;
        if (composant != null) {
            re = (100 * composant.getQuantiteFlux() / composant.getQuantite());
            if (re > 100) {
                re = 100;
            }
        }
        return re;
    }

    public double giveTauxEvolutionSup() {
        double re = 0;
        if (composant != null) {
            re = (100 * composant.getQuantiteFlux() / composant.getQuantite());
            if (re > 100) {
                re = re - 100;
            }else re=0;
        }
        return re;
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
        if (!(object instanceof YvsProdOfIndicateurSuivi)) {
            return false;
        }
        YvsProdOfIndicateurSuivi other = (YvsProdOfIndicateurSuivi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdOfIndicateurSuivi[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsProdOfIndicateurSuivi o1, YvsProdOfIndicateurSuivi o2) {
        if (o1 != null && o2 != null) {
            return (int) (o1.getQuantiteBorne().compareTo(o2.getQuantiteBorne()));
        } else if (o1 != null) {
            return 1;
        } else {
            return 0;
        }
    }

}
