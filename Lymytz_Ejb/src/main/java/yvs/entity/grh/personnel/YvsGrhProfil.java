/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.personnel;

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
import yvs.entity.grh.param.YvsGrhGradeEmploye;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_profil")
@NamedQueries({
    @NamedQuery(name = "YvsGrhProfil.findAll", query = "SELECT y FROM YvsGrhProfil y JOIN FETCH y.grade WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhProfil.findAllActif", query = "SELECT y FROM YvsGrhProfil y JOIN FETCH y.grade WHERE y.author.agence.societe=:societe AND y.actif=true"),
    @NamedQuery(name = "YvsGrhProfil.findByStatutProfil", query = "SELECT y FROM YvsGrhProfil y  JOIN FETCH y.grade WHERE y.statutProfil = :statutProfil"),
    @NamedQuery(name = "YvsGrhProfil.findByActif", query = "SELECT y FROM YvsGrhProfil y  JOIN FETCH y.grade WHERE y.actif = :actif"),
    @NamedQuery(name = "YvsGrhProfil.findById", query = "SELECT y FROM YvsGrhProfil y  JOIN FETCH y.grade WHERE y.id = :id")})
public class YvsGrhProfil implements Serializable {

    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_profil_id_seq", name = "yvs_grh_profil_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_profil_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    private static final long serialVersionUID = 1L;
    @Column(name = "statut_profil")
    private String statutProfil;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "grade", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhGradeEmploye grade;

    public YvsGrhProfil() {
    }

    public YvsGrhProfil(Long id) {
        this.id = id;
    }

    public YvsGrhProfil(Long id, String statutProfil) {
        this(id);
        this.statutProfil = statutProfil;
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

    public String getStatutProfil() {
        return statutProfil;
    }

    public String giveStatutProfil() {
        String re = null;
        if (statutProfil != null) {
            switch (statutProfil) {
                case "PE":
                    re = "PERMANENT";
                    break;
                case "TEP":
                    re = "TEMPORAIRE";
                    break;
                case "ST":
                    re = "STAGIAIRE";
                    break;
                case "Tacheron":
                    re = "TACHERON";
                    break;
                default:
                    re = "";
            }
        }
        return re;
    }

    public void setStatutProfil(String statutProfil) {
        this.statutProfil = statutProfil;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhGradeEmploye getGrade() {
        return grade;
    }

    public void setGrade(YvsGrhGradeEmploye grade) {
        this.grade = grade;
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
        if (!(object instanceof YvsGrhProfil)) {
            return false;
        }
        YvsGrhProfil other = (YvsGrhProfil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhProfil[ id=" + id + " ]";
    }

    }
