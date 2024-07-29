/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.stat.export;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_stat_export_etat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsStatExportEtat.findAll", query = "SELECT y FROM YvsStatExportEtat y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsStatExportEtat.findById", query = "SELECT y FROM YvsStatExportEtat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsStatExportEtat.findByCode", query = "SELECT y FROM YvsStatExportEtat y WHERE y.code = :code AND y.societe = :societe"),
    @NamedQuery(name = "YvsStatExportEtat.findByReference", query = "SELECT y FROM YvsStatExportEtat y WHERE y.reference = :reference AND y.societe = :societe"),
    @NamedQuery(name = "YvsStatExportEtat.findByLibelle", query = "SELECT y FROM YvsStatExportEtat y WHERE y.libelle = :libelle AND y.societe = :societe"),

    @NamedQuery(name = "YvsStatExportEtat.findReference", query = "SELECT y.reference FROM YvsStatExportEtat y WHERE y.code = :code AND y.societe = :societe")})
public class YvsStatExportEtat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_stat_export_etat_id_seq", name = "yvs_stat_export_etat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_export_etat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "file_name")
    private String fileName;
    @Size(max = 2147483647)
    @Column(name = "format")
    private String format;
    @Size(max = 2147483647)
    @Column(name = "table_principal")
    private String tablePrincipal;
    @Size(max = 2147483647)
    @Column(name = "colonne_principal")
    private String colonnePrincipal;
    @Size(max = 2147483647)
    @Column(name = "separateur")
    private String separateur;
    @Size(max = 2147483647)
    @Column(name = "reference")
    private String reference;
    @Column(name = "order_by")
    private String orderBy;
    @Column(name = "formule")
    private String formule;
    @Column(name = "type_formule")
    private Character typeFormule;
    @Column(name = "for_exportation")
    private Boolean forExportation;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private List<YvsStatExportColonne> colonnes;
    @Transient
    private boolean new_;
    @Transient
    private List<YvsStatExportColonne> integres;
    @Transient
    private List<YvsStatExportColonne> contraintes;
    @Transient
    private List<YvsStatExportColonne> visibles;
    @Transient
    private boolean select;

    public YvsStatExportEtat() {
        colonnes = new ArrayList<>();
        integres = new ArrayList<>();
        visibles = new ArrayList<>();
        contraintes = new ArrayList<>();
    }

    public YvsStatExportEtat(Integer id) {
        this.id = id;
        colonnes = new ArrayList<>();
        integres = new ArrayList<>();
        visibles = new ArrayList<>();
        contraintes = new ArrayList<>();
    }

    public YvsStatExportEtat(YvsStatExportEtat y) {
        this.id = y.id;
        this.dateUpdate = y.dateUpdate;
        this.dateSave = y.dateSave;
        this.code = y.code;
        this.libelle = y.libelle;
        this.fileName = y.fileName;
        this.format = y.format;
        this.tablePrincipal = y.tablePrincipal;
        this.colonnePrincipal = y.colonnePrincipal;
        this.separateur = y.separateur;
        this.reference = y.reference;
        this.orderBy = y.orderBy;
        this.formule = y.formule;
        this.typeFormule = y.typeFormule;
        this.forExportation = y.forExportation;
        this.societe = y.societe;
        this.author = y.author;
        this.colonnes = y.colonnes;
        this.new_ = y.new_;
        this.integres = y.integres;
        this.contraintes = y.contraintes;
        this.visibles = y.visibles;
        this.select = y.select;
    }

    public Boolean getForExportation() {
        return forExportation != null ? forExportation : true;
    }

    public void setForExportation(Boolean forExportation) {
        this.forExportation = forExportation;
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

    public Character getTypeFormule() {
        return typeFormule != null ? String.valueOf(typeFormule).trim().length() > 0 ? typeFormule : 'S' : 'S';
    }

    public void setTypeFormule(Character typeFormule) {
        this.typeFormule = typeFormule;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference != null ? reference : "";
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSeparateur() {
        return separateur != null ? separateur.trim().length() > 0 ? separateur : ";" : ";";
    }

    public void setSeparateur(String separateur) {
        this.separateur = separateur;
    }

    public String getTablePrincipal() {
        return tablePrincipal != null ? tablePrincipal.trim().length() > 0 ? tablePrincipal : "yvs_users_agences" : "yvs_users_agences";
    }

    public void setTablePrincipal(String tablePrincipal) {
        this.tablePrincipal = tablePrincipal;
    }

    public String getColonnePrincipal() {
        return colonnePrincipal != null ? colonnePrincipal.trim().length() > 0 ? colonnePrincipal : "author" : "author";
    }

    public void setColonnePrincipal(String colonnePrincipal) {
        this.colonnePrincipal = colonnePrincipal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public String getFormat() {
        return format != null ? format.trim().length() > 0 ? format : "TXT" : "TXT";
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileName() {
        return fileName != null ? fileName.trim().length() > 0 ? fileName : "file" : "file";
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    @XmlTransient  @JsonIgnore
    public List<YvsStatExportColonne> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<YvsStatExportColonne> colonnes) {
        this.colonnes = colonnes;
    }

    public List<YvsStatExportColonne> getIntegres() {
        integres.clear();
        for (YvsStatExportColonne c : colonnes) {
            if (c.getIntegrer()) {
                integres.add(c);
            }
        }
        return integres;
    }

    public void setIntegres(List<YvsStatExportColonne> integres) {
        this.integres = integres;
    }

    public List<YvsStatExportColonne> getVisibles() {
        visibles.clear();
        for (YvsStatExportColonne c : colonnes) {
            if (c.getVisible()) {
                visibles.add(c);
            }
        }
        return visibles;
    }

    public void setVisibles(List<YvsStatExportColonne> visibles) {
        this.visibles = visibles;
    }

    public List<YvsStatExportColonne> getContraintes() {
        contraintes.clear();
        for (YvsStatExportColonne c : colonnes) {
            if (c.getContrainte()) {
                contraintes.add(c);
            }
        }
        return contraintes;
    }

    public void setContraintes(List<YvsStatExportColonne> contraintes) {
        this.contraintes = contraintes;
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
        if (!(object instanceof YvsStatExportEtat)) {
            return false;
        }
        YvsStatExportEtat other = (YvsStatExportEtat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.stat.export.YvsStatExportEtat[ id=" + id + " ]";
    }

}
