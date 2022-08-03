/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date; 
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_plan_commission")
@NamedQueries({
    @NamedQuery(name = "YvsComPlanCommission.findAll", query = "SELECT y FROM YvsComPlanCommission y WHERE y.societe = :societe ORDER BY y.reference"),
    @NamedQuery(name = "YvsComPlanCommission.findById", query = "SELECT y FROM YvsComPlanCommission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComPlanCommission.findByReference", query = "SELECT y FROM YvsComPlanCommission y WHERE y.societe = :societe AND y.reference = :reference"),
    @NamedQuery(name = "YvsComPlanCommission.findLikeReference", query = "SELECT y FROM YvsComPlanCommission y WHERE y.societe = :societe AND UPPER(y.reference) LIKE UPPER(:reference) ORDER BY y.reference"),
    @NamedQuery(name = "YvsComPlanCommission.findByActif", query = "SELECT y FROM YvsComPlanCommission y WHERE y.societe = :societe AND y.actif = :actif ORDER BY y.reference")})
public class YvsComPlanCommission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_commission_id_seq", name = "yvs_com_commission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_commission_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Size(max = 2147483647)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "actif")
    private Boolean actif;
    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @OneToMany(mappedBy = "plan", fetch = FetchType.LAZY)
    private List<YvsComFacteurTaux> facteurs;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean update;

    public YvsComPlanCommission() {
        facteurs = new ArrayList<>();
    }

    public YvsComPlanCommission(Long id) {
        this();
        this.id = id;
    }

    public YvsComPlanCommission(Long id, String reference) {
        this(id);
        this.reference = reference;
    }

    public YvsComPlanCommission(Long id, String reference, String intitule) {
        this(id, reference);
        this.intitule = intitule;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
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
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getIntitule() {
        return intitule != null ? intitule : "";
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public List<YvsComFacteurTaux> getFacteurs() {
        return facteurs;
    }

    public void setFacteurs(List<YvsComFacteurTaux> facteurs) {
        this.facteurs = facteurs;
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
        if (!(object instanceof YvsComPlanCommission)) {
            return false;
        }
        YvsComPlanCommission other = (YvsComPlanCommission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.YvsComPlanCommission[ id=" + id + " ]";
    }

    }
