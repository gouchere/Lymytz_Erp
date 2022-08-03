/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
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
import javax.persistence.Transient;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_table_conversion")
@NamedQueries({
    @NamedQuery(name = "YvsBaseTableConversion.findAll", query = "SELECT y FROM YvsBaseTableConversion y"),
    @NamedQuery(name = "YvsBaseTableConversion.findById", query = "SELECT y FROM YvsBaseTableConversion y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseTableConversion.findByUnite", query = "SELECT y FROM YvsBaseTableConversion y WHERE y.unite=:unite"),
    @NamedQuery(name = "YvsBaseTableConversion.findByEquivalent", query = "SELECT y FROM YvsBaseTableConversion y WHERE y.uniteEquivalent=:unite"),
    @NamedQuery(name = "YvsBaseTableConversion.findByTauxChange", query = "SELECT y FROM YvsBaseTableConversion y WHERE y.tauxChange = :tauxChange"),
    
    @NamedQuery(name = "YvsBaseTableConversion.findUniteCorrespondance", query = "SELECT y FROM YvsBaseTableConversion y WHERE y.unite = :unite AND y.uniteEquivalent=:uniteE"),
    @NamedQuery(name = "YvsBaseTableConversion.coundUniteCorrespondance", query = "SELECT COUNT(y) FROM YvsBaseTableConversion y WHERE y.unite = :unite AND y.uniteEquivalent = :equivalent"),

    @NamedQuery(name = "YvsBaseTableConversion.findTauxByUniteCorrespondance", query = "SELECT y.tauxChange FROM YvsBaseTableConversion y WHERE y.unite = :unite AND y.uniteEquivalent=:uniteE"),
    @NamedQuery(name = "YvsBaseTableConversion.findByType", query = "SELECT y.uniteEquivalent FROM YvsBaseTableConversion y WHERE (y.unite.type = :type OR y.uniteEquivalent.type = :type) AND y.unite.societe = :societe"),
    @NamedQuery(name = "YvsBaseTableConversion.findEquivalentByUnite", query = "SELECT y.uniteEquivalent FROM YvsBaseTableConversion y WHERE y.unite = :unite"),
    @NamedQuery(name = "YvsBaseTableConversion.findUniteByEquivalent", query = "SELECT y.unite FROM YvsBaseTableConversion y WHERE y.uniteEquivalent = :unite")})
public class YvsBaseTableConversion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_table_conversion_id_seq", name = "yvs_prod_table_conversion_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_table_conversion_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "taux_change")
    private Double tauxChange;
    @JoinColumn(name = "unite_equivalent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure uniteEquivalent;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseUniteMesure unite;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean change;
    @Transient
    private boolean equivalence;

    public YvsBaseTableConversion() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsBaseTableConversion(Long id) {
        this();
        this.id = id;
    }

    public YvsBaseTableConversion(YvsBaseUniteMesure unite, YvsBaseUniteMesure uniteEquivalent) {
        this();
        this.uniteEquivalent = uniteEquivalent;
        this.unite = unite;
    }

    public YvsBaseTableConversion(YvsBaseUniteMesure unite, YvsBaseUniteMesure uniteEquivalent, double tauxChange) {
        this();
        this.uniteEquivalent = uniteEquivalent;
        this.unite = unite;
        this.tauxChange = tauxChange;
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

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public boolean isEquivalence() {
        equivalence = false;
        if ((unite != null ? (unite.getId() != null ? unite.getId() > 0 : false) : false) && (uniteEquivalent != null ? (uniteEquivalent.getId() != null ? uniteEquivalent.getId() > 0 : false) : false)) {
            if (uniteEquivalent.getEquivalences() != null ? !uniteEquivalent.getEquivalences().isEmpty() : false) {
                for (YvsBaseTableConversion e : uniteEquivalent.getEquivalences()) {
                    if (e.getUniteEquivalent() != null ? e.getUniteEquivalent().equals(unite) : false) {
                        equivalence = true;
                        break;
                    }
                }
            }
        }
        return equivalence;
    }

    public void setEquivalence(boolean equivalence) {
        this.equivalence = equivalence;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTauxChange() {
        return tauxChange != null ? tauxChange : 0.0;
    }

    public void setTauxChange(Double tauxChange) {
        this.tauxChange = tauxChange;
    }

    public YvsBaseUniteMesure getUniteEquivalent() {
        return uniteEquivalent;
    }

    public void setUniteEquivalent(YvsBaseUniteMesure uniteEquivalent) {
        this.uniteEquivalent = uniteEquivalent;
    }

    public YvsBaseUniteMesure getUnite() {
        return unite;
    }

    public void setUnite(YvsBaseUniteMesure unite) {
        this.unite = unite;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final YvsBaseTableConversion other = (YvsBaseTableConversion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.prod.YvsBaseTableConversion[ id=" + id + " ]";
    }

    public String toValue() {
        return (unite != null ? (unite.getId() != null ? unite.getReference() : "--") : "--")
                + " - " + (uniteEquivalent != null ? (uniteEquivalent.getId() != null ? uniteEquivalent.getReference() : "--") : "--")
                + " = " + getTauxChange();
    }

}
