/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

import java.io.Serializable;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.commercial.client.YvsComElementAddContratsClient;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_type_cout")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeCout.findAll", query = "SELECT y FROM YvsGrhTypeCout y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsGrhTypeCout.findById", query = "SELECT y FROM YvsGrhTypeCout y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeCout.findByModule", query = "SELECT y FROM YvsGrhTypeCout y WHERE y.societe = :societe AND (y.moduleCout = :module OR y.moduleCout='L')"),
    @NamedQuery(name = "YvsGrhTypeCout.findByModules", query = "SELECT y FROM YvsGrhTypeCout y WHERE y.societe = :societe AND y.moduleCout IN :module"),
    @NamedQuery(name = "YvsGrhTypeCout.findByLibelle", query = "SELECT y FROM YvsGrhTypeCout y WHERE y.libelle = :libelle")})
public class YvsGrhTypeCout extends YvsEntity implements Serializable {

    @OneToMany(mappedBy = "typeCout")
    private List<YvsComElementAddContratsClient> yvsComElementAddContratsClientList;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_cout_id_seq", name = "yvs_grh_type_cout_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_cout_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "module_cout")
    private String moduleCout;
    @Column(name = "augmentation")
    private Boolean augmentation;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @Transient
    private double montant;
    @Transient
    private double montantReste;
    @Transient
    private double coef;
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;

    public YvsGrhTypeCout() {
    }

    public YvsGrhTypeCout(Long id) {
        this.id = id;
    }

    public YvsGrhTypeCout(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getAugmentation() {
        return augmentation != null ? augmentation : true;
    }

    public void setAugmentation(Boolean augmentation) {
        this.augmentation = augmentation;
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

    public String getModuleCout() {
        return moduleCout != null ? (moduleCout.trim().length() > 0 ? moduleCout : "L") : "L";
    }

    public void setModuleCout(String moduleCout) {
        this.moduleCout = moduleCout;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public double getMontantReste() {
        return montantReste;
    }

    public void setMontantReste(double montantReste) {
        this.montantReste = montantReste;
    }

    public double getCoef() {
        return coef;
    }

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
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
        if (!(object instanceof YvsGrhTypeCout)) {
            return false;
        }
        YvsGrhTypeCout other = (YvsGrhTypeCout) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhTypeCout[ id=" + id + " ]";
    }

    public List<YvsComElementAddContratsClient> getYvsComElementAddContratsClientList() {
        return yvsComElementAddContratsClientList;
    }

    public void setYvsComElementAddContratsClientList(List<YvsComElementAddContratsClient> yvsComElementAddContratsClientList) {
        this.yvsComElementAddContratsClientList = yvsComElementAddContratsClientList;
    }

}
