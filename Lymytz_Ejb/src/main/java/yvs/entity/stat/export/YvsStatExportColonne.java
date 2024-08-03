/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.stat.export;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_stat_export_colonne")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsStatExportColonne.findAll", query = "SELECT y FROM YvsStatExportColonne y WHERE y.etat = :etat ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.findById", query = "SELECT y FROM YvsStatExportColonne y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatExportColonne.findByColonne", query = "SELECT y FROM YvsStatExportColonne y WHERE y.colonne = :colonne ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.findByLibelle", query = "SELECT y FROM YvsStatExportColonne y WHERE y.libelle = :libelle ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.findByVisible", query = "SELECT y FROM YvsStatExportColonne y WHERE y.visible = :visible AND y.etat = :etat ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.countByVisible", query = "SELECT COUNT(y) FROM YvsStatExportColonne y WHERE y.visible = :visible AND y.etat = :etat"),
    @NamedQuery(name = "YvsStatExportColonne.findByIntegrer", query = "SELECT y FROM YvsStatExportColonne y WHERE y.integrer = :integrer AND y.etat = :etat ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.countByIntegrer", query = "SELECT COUNT(y) FROM YvsStatExportColonne y WHERE y.integrer = :integrer AND y.etat = :etat"),
    @NamedQuery(name = "YvsStatExportColonne.findByContrainte", query = "SELECT y FROM YvsStatExportColonne y WHERE y.contrainte = :contrainte AND y.etat = :etat ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.countByContrainte", query = "SELECT COUNT(y) FROM YvsStatExportColonne y WHERE y.contrainte = :contrainte AND y.etat = :etat"),
    @NamedQuery(name = "YvsStatExportColonne.findByTableName", query = "SELECT y FROM YvsStatExportColonne y WHERE y.tableName = :tableName ORDER BY y.ordre"),
    @NamedQuery(name = "YvsStatExportColonne.findOneByEtat", query = "SELECT y FROM YvsStatExportColonne y WHERE y.tableName = :tableName AND y.colonne = :colonne AND y.etat = :etat ORDER BY y.ordre"),

    @NamedQuery(name = "YvsStatExportColonne.findTableLiee", query = "SELECT DISTINCT(y.tableNameLiee) FROM YvsStatExportColonne y WHERE y.etat = :etat ORDER BY y.tableNameLiee"),
    @NamedQuery(name = "YvsStatExportColonne.findTableNotLiee", query = "SELECT DISTINCT(y.tableName) FROM YvsStatExportColonne y WHERE y.etat = :etat AND y.tableName != y.tableNameLiee ORDER BY y.tableNameLiee")})
public class YvsStatExportColonne implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_export_colonne_id_seq", name = "yvs_stat_export_colonne_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_export_colonne_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "ordre")
    private Integer ordre;
    @Size(max = 2147483647)
    @Column(name = "colonne")
    private String colonne;
    @Size(max = 2147483647)
    @Column(name = "defaut_valeur")
    private String defautValeur;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "integrer")
    private Boolean integrer;
    @Size(max = 2147483647)
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "contrainte")
    private Boolean contrainte;
    @Size(max = 2147483647)
    @Column(name = "colonne_liee")
    private String colonneLiee;
    @Size(max = 2147483647)
    @Column(name = "table_name_liee")
    private String tableNameLiee;
    @Column(name = "colonne_date")
    private Boolean colonneDate;
    @Size(max = 2147483647)
    @Column(name = "format_date")
    private String formatDate;
    @Column(name = "order_by")
    private Character orderBy;
    @Column(name = "sens_contrainte")
    private Character sensContrainte;
    @JoinColumn(name = "etat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsStatExportEtat etat;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsStatExportColonne() {
    }

    public YvsStatExportColonne(Integer id) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getSensContrainte() {
        return sensContrainte != null ? String.valueOf(sensContrainte).trim().length() > 0 ? sensContrainte : 'N' : 'N';
    }

    public void setSensContrainte(Character sensContrainte) {
        this.sensContrainte = sensContrainte;
    }

    public Character getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Character orderBy) {
        this.orderBy = orderBy;
    }

    public String getFormatDate() {
        return formatDate != null ? formatDate.trim().length() > 0 ? formatDate : "dd-MM-yyyy" : "dd-MM-yyyy";
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }

    public Boolean getColonneDate() {
        return colonneDate != null ? colonneDate : false;
    }

    public void setColonneDate(Boolean colonneDate) {
        this.colonneDate = colonneDate;
    }

    public String getDefautValeur() {
        return defautValeur;
    }

    public void setDefautValeur(String defautValeur) {
        this.defautValeur = defautValeur;
    }

    public Integer getOrdre() {
        return ordre != null ? ordre : 0;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getColonne() {
        return colonne != null ? colonne : "";
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getLibelle() {
        return libelle != null ? libelle : "";
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getIntegrer() {
        return integrer != null ? integrer : false;
    }

    public void setIntegrer(Boolean integrer) {
        this.integrer = integrer;
    }

    public Boolean getVisible() {
        return visible != null ? visible : false;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getTableName() {
        return tableName != null ? tableName : "";
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public YvsStatExportEtat getEtat() {
        return etat;
    }

    public void setEtat(YvsStatExportEtat etat) {
        this.etat = etat;
    }

    public Boolean getContrainte() {
        return contrainte != null ? contrainte : false;
    }

    public void setContrainte(Boolean contrainte) {
        this.contrainte = contrainte;
    }

    public String getColonneLiee() {
        return colonneLiee != null ? colonneLiee : "";
    }

    public void setColonneLiee(String colonneLiee) {
        this.colonneLiee = colonneLiee;
    }

    public String getTableNameLiee() {
        return tableNameLiee != null ? tableNameLiee : "";
    }

    public void setTableNameLiee(String tableNameLiee) {
        this.tableNameLiee = tableNameLiee;
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsStatExportColonne)) {
            return false;
        }
        YvsStatExportColonne other = (YvsStatExportColonne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.stat.export.YvsStatExportColonne[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
