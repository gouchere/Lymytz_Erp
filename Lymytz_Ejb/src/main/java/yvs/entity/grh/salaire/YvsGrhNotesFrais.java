/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.salaire;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

/**
 *
 * @author user1
 */
@Entity
@Table(name = "yvs_grh_notes_frais")
@NamedQueries({
    @NamedQuery(name = "YvsGrhNotesFrais.findAll", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.employe.agence = :agence"),
    @NamedQuery(name = "YvsGrhNotesFrais.findById", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhNotesFrais.findByDateNote", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.dateNote = :dateNote"),
    @NamedQuery(name = "YvsGrhNotesFrais.findByDescription", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.description = :description"),
    @NamedQuery(name = "YvsGrhNotesFrais.findByStatut", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsGrhNotesFrais.findBySupp", query = "SELECT y FROM YvsGrhNotesFrais y WHERE y.supp = :supp")})
public class YvsGrhNotesFrais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_notes_frais_id_seq", name = "yvs_grh_notes_frais_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_notes_frais_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_note")
    @Temporal(TemporalType.DATE)
    private Date dateNote;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "statut")
    private String statut;
    @Column(name = "supp")
    private Boolean supp;
    @OneToMany(mappedBy = "yvsGrhNotesFrais")
    private List<YvsGrhDepenseNote> yvsGrhDepenseNoteList;
    @JoinColumn(name = "centre_depense", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhCentreDepense centreDepense;
    @JoinColumn(name = "employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes employe;

    public YvsGrhNotesFrais() {
    }

    public YvsGrhNotesFrais(Long id) {
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

    public Date getDateNote() {
        return dateNote;
    }

    public void setDateNote(Date dateNote) {
        this.dateNote = dateNote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public List<YvsGrhDepenseNote> getYvsGrhDepenseNoteList() {
        return yvsGrhDepenseNoteList;
    }

    public void setYvsGrhDepenseNoteList(List<YvsGrhDepenseNote> yvsGrhDepenseNoteList) {
        this.yvsGrhDepenseNoteList = yvsGrhDepenseNoteList;
    }

    public YvsGrhCentreDepense getCentreDepense() {
        return centreDepense;
    }

    public void setCentreDepense(YvsGrhCentreDepense centreDepense) {
        this.centreDepense = centreDepense;
    }

    public YvsGrhEmployes getEmploye() {
        return employe;
    }

    public void setEmploye(YvsGrhEmployes employe) {
        this.employe = employe;
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
        if (!(object instanceof YvsGrhNotesFrais)) {
            return false;
        }
        YvsGrhNotesFrais other = (YvsGrhNotesFrais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.salaire.YvsGrhNotesFrais[ id=" + id + " ]";
    }

}
