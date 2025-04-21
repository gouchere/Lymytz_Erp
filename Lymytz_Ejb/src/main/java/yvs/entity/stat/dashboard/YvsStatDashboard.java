/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.stat.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.Util;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_stat_dashboard")
@NamedQueries({
    @NamedQuery(name = "YvsStatDashboard.findAll", query = "SELECT y FROM YvsStatDashboard y ORDER BY y.libelle"),
    @NamedQuery(name = "YvsStatDashboard.findById", query = "SELECT y FROM YvsStatDashboard y WHERE y.id = :id ORDER BY y.libelle"),
    @NamedQuery(name = "YvsStatDashboard.findByLibelle", query = "SELECT y FROM YvsStatDashboard y WHERE y.libelle = :libelle ORDER BY y.libelle"),
    @NamedQuery(name = "YvsStatDashboard.findByCode", query = "SELECT y FROM YvsStatDashboard y WHERE y.code = :code ORDER BY y.libelle"),
    @NamedQuery(name = "YvsStatDashboard.findByDateSave", query = "SELECT y FROM YvsStatDashboard y WHERE y.dateSave = :dateSave ORDER BY y.libelle"),
    @NamedQuery(name = "YvsStatDashboard.findByDateUpdate", query = "SELECT y FROM YvsStatDashboard y WHERE y.dateUpdate = :dateUpdate ORDER BY y.libelle")})
public class YvsStatDashboard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @SequenceGenerator(sequenceName = "yvs_stat_dashboard_id_seq", name = "yvs_stat_dashboard_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_stat_dashboard_id_seq_name", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "groupe")
    private String groupe;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;

    public YvsStatDashboard() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsStatDashboard(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getGroupe() {
        return Util.asString(groupe) ? groupe : "";
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
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
        if (!(object instanceof YvsStatDashboard)) {
            return false;
        }
        YvsStatDashboard other = (YvsStatDashboard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.stat.dashboard.YvsStatDashboard[ id=" + id + " ]";
    }

}
