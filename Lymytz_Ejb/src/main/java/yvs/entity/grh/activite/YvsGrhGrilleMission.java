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
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_grille_mission")
@NamedQueries({
    @NamedQuery(name = "YvsGrilleMission.findAll", query = "SELECT y FROM YvsGrhGrilleMission y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsGrilleMission.countAll", query = "SELECT COUNT(y) FROM YvsGrhGrilleMission y WHERE y.societe=:societe"),
    @NamedQuery(name = "YvsGrilleMission.findById", query = "SELECT y FROM YvsGrhGrilleMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrilleMission.findById", query = "SELECT y FROM YvsGrhGrilleMission y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrilleMission.findByActif", query = "SELECT y FROM YvsGrhGrilleMission y LEFT JOIN FETCH y.numCompte WHERE y.actif = :actif AND y.societe=:societe")})
public class YvsGrhGrilleMission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grille_mission_id_seq", name = "yvs_grille_mission_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grille_mission_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "categorie")
    private String categorie;
    
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "num_compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable numCompte;
    
    @OneToMany(mappedBy = "grilleMission")
    private List<YvsGrhDetailGrilleFraiMission> detailsFraisMission;
    

    public YvsGrhGrilleMission() {
    }

    public YvsGrhGrilleMission(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
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

    public YvsBasePlanComptable getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(YvsBasePlanComptable numCompte) {
        this.numCompte = numCompte;
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
        if (!(object instanceof YvsGrhGrilleMission)) {
            return false;
        }
        YvsGrhGrilleMission other = (YvsGrhGrilleMission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.activite.YvsGrilleMission[ id=" + id + " ]";
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<YvsGrhDetailGrilleFraiMission> getDetailsFraisMission() {
        return detailsFraisMission;
    }

    public void setDetailsFraisMission(List<YvsGrhDetailGrilleFraiMission> detailsFraisMission) {
        this.detailsFraisMission = detailsFraisMission;
    }

    }
