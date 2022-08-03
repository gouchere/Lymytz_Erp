/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.base;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_base_modele_reference")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModeleReference.findAll", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.societe = :societe ORDER BY y.element.designation"),
    @NamedQuery(name = "YvsBaseModeleReference.findByElement_", query = "SELECT COUNT(y) FROM YvsBaseModeleReference y WHERE y.societe = :societe AND y.element=:document "),
    @NamedQuery(name = "YvsBaseModeleReference.findByElement", query = "SELECT y FROM YvsBaseModeleReference y JOIN FETCH y.element WHERE y.societe = :societe AND LOWER(y.element.designation) = LOWER(:designation) "),
    @NamedQuery(name = "YvsBaseModeleReference.findByModule", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.societe = :societe AND y.module = :module ORDER BY y.element.designation"),
    @NamedQuery(name = "YvsBaseModeleReference.findById", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModeleReference.findByPrefix", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.prefix = :prefix AND y.societe = :societe"),
    @NamedQuery(name = "YvsBaseModeleReference.findByJour", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsBaseModeleReference.findByMois", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.mois = :mois"),
    @NamedQuery(name = "YvsBaseModeleReference.findByAnnee", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.annee = :annee"),
    @NamedQuery(name = "YvsBaseModeleReference.findByTaille", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.taille = :taille"),
    @NamedQuery(name = "YvsBaseModeleReference.findBySeparateur", query = "SELECT y FROM YvsBaseModeleReference y WHERE y.separateur = :separateur")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsBaseModeleReference extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_modele_reference_id_seq", name = "yvs_base_modele_reference_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_modele_reference_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "prefix")
    private String prefix;
    @Column(name = "jour")
    private Boolean jour;
    @Column(name = "mois")
    private Boolean mois;
    @Column(name = "annee")
    private Boolean annee;
    @Column(name = "taille")
    private Integer taille;
    @Column(name = "separateur")
    private Character separateur;
    @Column(name = "module")
    private String module;
    @Column(name = "element_code")
    private String elementCode;
    @Column(name = "code_point")
    private Boolean codePoint;
    @Column(name = "longueur_code_point")
    private Integer longueurCodePoint;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "element", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseElementReference element;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    
    @Transient
    private boolean new_;
    @Transient
    private boolean selectActif;
    @Transient
    private String apercu;

    @Transient
    private String code;

    public YvsBaseModeleReference() {
    }

    public YvsBaseModeleReference(Long id) {
        this.id = id;
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

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public String getApercu() {
        Calendar cal = Calendar.getInstance();
        String inter = ((getPrefix() != null) ? getPrefix() : "");
        if (getCodePoint()) {
            String code = ".X";
            for (int i = 1; i < getLongueurCodePoint(); i++) {
                code += "X";
            }
            inter += code;
        }
        inter += getSeparateur();
        if (getJour()) {
            if (cal.get(Calendar.DATE) > 9) {
                inter += Integer.toString(cal.get(Calendar.DATE));
            }
            if (cal.get(Calendar.DATE) < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.DATE)));
            }
        }
        if (getMois()) {
            if (cal.get(Calendar.MONTH) + 1 > 9) {
                inter += Integer.toString(cal.get(Calendar.MONTH) + 1);
            }
            if (cal.get(Calendar.MONTH) + 1 < 10) {
                inter += ("0" + Integer.toString(cal.get(Calendar.MONTH) + 1));
            }
        }
        if (getAnnee()) {
            inter += Integer.toString(cal.get(Calendar.YEAR)).substring(2);
        }
        inter += getSeparateur();
        for (int i = 0; i < getTaille(); i++) {
            inter += "0";
        }
        setApercu(inter);
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getJour() {
        return jour != null ? jour : false;
    }

    public String getModule() {
        return module != null ? module.trim().length() > 0 ? module : "COM" : "COM";
    }

    public void setModule(String module) {
        this.module = module;
    }

    public void setJour(Boolean jour) {
        this.jour = jour;
    }

    public Boolean getMois() {
        return mois != null ? mois : false;
    }

    public void setMois(Boolean mois) {
        this.mois = mois;
    }

    public Boolean getAnnee() {
        return annee != null ? annee : false;
    }

    public void setAnnee(Boolean annee) {
        this.annee = annee;
    }

    public Integer getTaille() {
        return taille != null ? taille : 3;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    public Character getSeparateur() {
        return separateur;
    }

    public void setSeparateur(Character separateur) {
        this.separateur = separateur;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsBaseElementReference getElement() {
        return element;
    }

    public void setElement(YvsBaseElementReference element) {
        this.element = element;
    }

    public String getElementCode() {
        return elementCode != null ? elementCode : "";
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public Boolean getCodePoint() {
        return codePoint != null ? codePoint : false;
    }

    public void setCodePoint(Boolean codePoint) {
        this.codePoint = codePoint;
    }

    public Integer getLongueurCodePoint() {
        return longueurCodePoint != null ? longueurCodePoint : 3;
    }

    public void setLongueurCodePoint(Integer longueurCodePoint) {
        this.longueurCodePoint = longueurCodePoint;
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
        if (!(object instanceof YvsBaseModeleReference)) {
            return false;
        }
        YvsBaseModeleReference other = (YvsBaseModeleReference) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseModeleReference[ id=" + id + " ]";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
