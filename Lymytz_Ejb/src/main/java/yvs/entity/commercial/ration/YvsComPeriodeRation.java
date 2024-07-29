/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.ration;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_com_periode_ration")
@NamedQueries({
    @NamedQuery(name = "YvsComPeriodeRation.findAll", query = "SELECT y FROM YvsComPeriodeRation y"),
    @NamedQuery(name = "YvsComPeriodeRation.findById", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComPeriodeRation.findByReferencePeriode", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.referencePeriode = :referencePeriode"),
    @NamedQuery(name = "YvsComPeriodeRation.findByDateDebut", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.dateDebut = :dateDebut"),
    @NamedQuery(name = "YvsComPeriodeRation.findByContentDate", query = "SELECT y FROM YvsComPeriodeRation y WHERE :date BETWEEN y.dateDebut AND y.fin AND y.societe=:societe"),
    @NamedQuery(name = "YvsComPeriodeRation.findByFin", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.fin = :fin"),
    @NamedQuery(name = "YvsComPeriodeRation.findByHeureDebut", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.heureDebut = :heureDebut"),
    @NamedQuery(name = "YvsComPeriodeRation.findByHeureFin", query = "SELECT y FROM YvsComPeriodeRation y WHERE y.heureFin = :heureFin")})
public class YvsComPeriodeRation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_periode_ration_id_seq", name = "yvs_com_periode_ration_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_periode_ration_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "reference_periode")
    private String referencePeriode;
    @Column(name = "abbreviation")
    private String abbreviation;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @Column(name = "cloturer")
    private Boolean cloturer;

    public YvsComPeriodeRation() {
    }

    public YvsComPeriodeRation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencePeriode() {
        return referencePeriode;
    }

    public void setReferencePeriode(String referencePeriode) {
        this.referencePeriode = referencePeriode;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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
        if (!(object instanceof YvsComPeriodeRation)) {
            return false;
        }
        YvsComPeriodeRation other = (YvsComPeriodeRation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.ration.YvsComPeriodeRation[ id=" + id + " ]";
    }

}
