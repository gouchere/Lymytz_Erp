/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.DaoGeneric;
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_dictionnaire") 
@NamedQueries({
    @NamedQuery(name = "YvsDictionnaire.find", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findAll", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT "
            + "JOIN FETCH d.parent ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByActif", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.actif = :actif AND d.libele IS NOT NULL ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByVenteOnline", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.societe.venteOnline = :venteOnline AND d.actif = :actif AND d.libele IS NOT NULL ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findById", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.id = :id"),

    @NamedQuery(name = "YvsDictionnaire.findByActifNotIds", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.actif = :actif AND d.libele IS NOT NULL AND d.id NOT IN :ids ORDER BY d.libele"),

    @NamedQuery(name = "YvsDictionnaire.findByNotId", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.id != :id AND d.titre = :titre AND d.actif=true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByNotParent", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.parent IS NULL ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findAllByParent", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.parent = :parent ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByParent", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.parent = :parent AND d.actif = true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findPaysOneSociete", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = :titre AND d.actif=true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findPays", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = 'Pays' ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findVilles", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = 'Villes' ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findSecteurs", query = "SELECT DISTINCT d FROM YvsDictionnaire d JOIN FETCH d.parent WHERE d.titre = 'Secteurs' ORDER BY d.parent.libele, d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findVilleOnePays", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = 'Villes' AND d.parent = :parent AND d.actif=true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findByTitre", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = :titre AND d.actif=true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findAllByTitre", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = :titre ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findLibele", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE (d.titre = :titre OR d.titre = :titre2) AND d.actif=true ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findSecteurByParent", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.titre = 'Secteurs' AND d.parent = :parent ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findBylib", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE d.libele = :lib ORDER BY d.libele"),
    @NamedQuery(name = "YvsDictionnaire.findLikelibele", query = "SELECT DISTINCT d FROM YvsDictionnaire d LEFT JOIN FETCH d.fils LEFT JOIN FETCH d.parent WHERE (UPPER(d.libele) LIKE UPPER(:libelle) OR UPPER(d.abreviation) LIKE UPPER(:libelle)) ORDER BY d.libele")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsDictionnaire extends YvsEntity implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_dictionnaire_id_seq")
    @SequenceGenerator(sequenceName = "yvs_dictionnaire_id_seq", allocationSize = 1, name = "yvs_dictionnaire_id_seq")
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "libele")
    private String libele;  //Nom du pays ou de la ville
    @Size(max = 255)
    @Column(name = "titre")
    private String titre;   //Ville ou pays
    @Column(name = "abreviation")
    private String abreviation;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private YvsDictionnaire parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<YvsDictionnaire> fils;

    @Transient
    private boolean new_;

    @Transient
    private YvsDictionnaireInformations information;
    @Transient
    private boolean select;

    public YvsDictionnaire() {
        fils = new ArrayList<>();
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsDictionnaire(Long id) {
        this();
        this.id = id;
    }

    public YvsDictionnaire(String titre, String libele) {
        this();
        this.titre = titre;
        this.libele = libele;
    }

    public YvsDictionnaire(Long id, String libele) {
        this(id);
        this.libele = libele;
    }

    public YvsDictionnaire(Long id, String libele, YvsDictionnaire parent) {
        this(id, libele);
        this.parent = parent;
    }

    public YvsDictionnaire(long id, String libele, String titre) {
        this(id, libele);
        this.titre = titre;
    }

    public YvsDictionnaire(Long id, String libele, String abreviation, String titre) {
        this(id, libele, titre);
        this.abreviation = abreviation;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public String getTitre() {
        return titre != null ? titre.trim().length() > 0 ? titre : Constantes.PAYS : Constantes.PAYS;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLibele() {
        return libele != null ? libele : "";
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    @XmlTransient
    @JsonIgnore
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

    public Boolean getSupp() {
        return supp != null ? supp : false;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getAbreviation() {
        return abreviation != null ? abreviation : "";
    }

    public YvsDictionnaire getParent() {
        return parent;
    }

    public void setParent(YvsDictionnaire parent) {
        this.parent = parent;
    }

    @XmlTransient
    @JsonIgnore
    public YvsDictionnaireInformations getInformation() {
        return information;
    }

    public void setInformation(YvsDictionnaireInformations information) {
        this.information = information;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsDictionnaire> getFils() {
        return fils;
    }

    public void setFils(List<YvsDictionnaire> fils) {
        this.fils = fils;
    }

    public void save() {
        DaoGeneric dao = new DaoGeneric();
        dao.save(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final YvsDictionnaire other = (YvsDictionnaire) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libele;
    }

    @Override
    public int compareTo(Object o) {
        if (!libele.equals(((YvsDictionnaire) o).libele)) {
            return libele.compareTo(((YvsDictionnaire) o).libele);
        }
        return id.compareTo(((YvsDictionnaire) o).id);
    }
}
