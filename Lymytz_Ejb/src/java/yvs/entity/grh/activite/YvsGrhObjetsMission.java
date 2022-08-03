/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.activite;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;import com.fasterxml.jackson.annotation.JsonBackReference; import com.fasterxml.jackson.annotation.JsonIgnore; import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_objets_mission")
@NamedQueries({
    @NamedQuery(name = "YvsGrhObjetsMission.findAll", query = "SELECT y FROM YvsGrhObjetsMission y"),
    @NamedQuery(name = "YvsGrhObjetsMission.findById", query = "SELECT y FROM YvsGrhObjetsMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhObjetsMission.findByTitre", query = "SELECT y FROM YvsGrhObjetsMission y WHERE y.titre = :titre"),
    @NamedQuery(name = "YvsGrhObjetsMission.findByDescription", query = "SELECT y FROM YvsGrhObjetsMission y WHERE y.description = :description"),
    @NamedQuery(name = "YvsGrhObjetsMission.findByActif", query = "SELECT y FROM YvsGrhObjetsMission y WHERE y.actif = :actif")})
public class YvsGrhObjetsMission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_objets_mission_id_seq", name = "yvs_grh_objets_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_objets_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "compte_charge", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compteCharge;

    @OneToMany(mappedBy = "objetMission")
    private List<YvsGrhObjetsMissionAnalytique> analytiques;

    public YvsGrhObjetsMission() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        analytiques = new ArrayList<>();
    }

    public YvsGrhObjetsMission(Integer id) {
        this();
        this.id = id;
    }

    public YvsGrhObjetsMission(Integer id, String titre) {
        this(id);
        this.titre = titre;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient  @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    @XmlTransient  @JsonIgnore
    public YvsBasePlanComptable getCompteCharge() {
        return compteCharge;
    }

    public void setCompteCharge(YvsBasePlanComptable compteCharge) {
        this.compteCharge = compteCharge;
    }

    @XmlTransient  @JsonIgnore
    public List<YvsGrhObjetsMissionAnalytique> getAnalytiques() {
        return analytiques;
    }

    public void setAnalytiques(List<YvsGrhObjetsMissionAnalytique> analytiques) {
        this.analytiques = analytiques;
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
        if (!(object instanceof YvsGrhObjetsMission)) {
            return false;
        }
        YvsGrhObjetsMission other = (YvsGrhObjetsMission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrhObjetsMission[ id=" + id + " ]";
    }

}
