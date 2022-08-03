/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.presence;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_equipe_employe")
@NamedQueries({
    @NamedQuery(name = "YvsGrhEquipeEmploye.findAll", query = "SELECT y FROM YvsGrhEquipeEmploye y WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhEquipeEmploye.findById", query = "SELECT y FROM YvsGrhEquipeEmploye y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhEquipeEmploye.findByTitreEquipe", query = "SELECT y FROM YvsGrhEquipeEmploye y WHERE y.titreEquipe = :titreEquipe")})
public class YvsGrhEquipeEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_equipe_employe_id_seq", name = "yvs_grh_equipe_employe_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_equipe_employe_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "titre_equipe")
    private String titreEquipe;
    @OneToMany(mappedBy = "equipe")
    private List<YvsGrhEmployes> yvsGrhEmployesList;
    @Column(name = "groupe_service")
    private String groupeService;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;
    @Column(name = "actif")
    private Boolean actif;

    public YvsGrhEquipeEmploye() {
    }

    public YvsGrhEquipeEmploye(Integer id) {
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

    public String getTitreEquipe() {
        return titreEquipe;
    }

    public void setTitreEquipe(String titreEquipe) {
        this.titreEquipe = titreEquipe;
    }

    public List<YvsGrhEmployes> getYvsGrhEmployesList() {
        return yvsGrhEmployesList;
    }

    public void setYvsGrhEmployesList(List<YvsGrhEmployes> yvsGrhEmployesList) {
        this.yvsGrhEmployesList = yvsGrhEmployesList;
    }

    public String getGroupeService() {
        return groupeService;
    }

    public void setGroupeService(String groupeService) {
        this.groupeService = groupeService;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsGrhEquipeEmploye)) {
            return false;
        }
        YvsGrhEquipeEmploye other = (YvsGrhEquipeEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.presence.YvsGrhEquipeEmploye[ id=" + id + " ]";
    }

}
