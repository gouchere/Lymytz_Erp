/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

import java.io.Serializable;
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
import yvs.entity.production.pilotage.YvsProdContentModeleQuantitatif;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_valeurs_qualitative")
@NamedQueries({
    @NamedQuery(name = "YvsProdValeursQualitative.findAll", query = "SELECT y FROM YvsProdValeursQualitative y"),
    @NamedQuery(name = "YvsProdValeursQualitative.findById", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByValeurText", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.valeurText = :valeurText"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByCodeCouleur", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.codeCouleur = :codeCouleur"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByOrdre", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.ordre = :ordre"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByIndicateur", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.indicateur=:indicateur ORDER BY y.valeurQuantitative"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByDateSave", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsProdValeursQualitative.findByDateUpdate", query = "SELECT y FROM YvsProdValeursQualitative y WHERE y.dateUpdate = :dateUpdate")})
public class YvsProdValeursQualitative implements Serializable, Comparator<YvsProdValeursQualitative> {
    @OneToMany(mappedBy = "valeur")
    private List<YvsProdContentModeleQuantitatif> yvsProdContentModeleQuantitatifList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_valeurs_qualitative_id_seq", name = "yvs_prod_valeurs_qualitative_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_valeurs_qualitative_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "valeur_text")
    private String valeurText;
    @Column(name = "valeur_quantitative")
    private Double valeurQuantitative;
    @Size(max = 2147483647)
    @Column(name = "code_couleur")
    private String codeCouleur;
    @Column(name = "ordre")
    private Integer ordre;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;

    @JoinColumn(name = "indicateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdIndicateurOp indicateur;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsProdValeursQualitative() {
    }

    public YvsProdValeursQualitative(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
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

    public YvsProdIndicateurOp getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(YvsProdIndicateurOp indicateur) {
        this.indicateur = indicateur;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Double getValeurQuantitative() {
        return valeurQuantitative != null ? valeurQuantitative : 0;
    }

    public void setValeurQuantitative(Double valeurQuantitative) {
        this.valeurQuantitative = valeurQuantitative;
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
        if (!(object instanceof YvsProdValeursQualitative)) {
            return false;
        }
        YvsProdValeursQualitative other = (YvsProdValeursQualitative) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdValeursQualitative[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsProdValeursQualitative o1, YvsProdValeursQualitative o2) {
        if (o1 != null && o2 != null) {
            return (int) (o1.getValeurQuantitative().compareTo(o2.getValeurQuantitative()));
        } else {
            if (o1 != null) {
                return 1;
            } else if (o2 != null) {
                return -1;
            }
        }
        return 0;
    }

    public List<YvsProdContentModeleQuantitatif> getYvsProdContentModeleQuantitatifList() {
        return yvsProdContentModeleQuantitatifList;
    }

    public void setYvsProdContentModeleQuantitatifList(List<YvsProdContentModeleQuantitatif> yvsProdContentModeleQuantitatifList) {
        this.yvsProdContentModeleQuantitatifList = yvsProdContentModeleQuantitatifList;
    }

}
