/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.recrutments;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_reponse_entretien")
@NamedQueries({
    @NamedQuery(name = "YvsGrhReponseEntretien.findAll", query = "SELECT y FROM YvsGrhReponseEntretien y"),
    @NamedQuery(name = "YvsGrhReponseEntretien.findById", query = "SELECT y FROM YvsGrhReponseEntretien y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhReponseEntretien.findByNoteReponse", query = "SELECT y FROM YvsGrhReponseEntretien y WHERE y.noteReponse = :noteReponse"),
    @NamedQuery(name = "YvsGrhReponseEntretien.findByReponseQuestion", query = "SELECT y FROM YvsGrhReponseEntretien y WHERE y.reponseQuestion = :reponseQuestion")})
public class YvsGrhReponseEntretien implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_reponse_entretien_id_seq", name = "yvs_grh_reponse_entretien_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_reponse_entretien_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "note_reponse")
    private Integer noteReponse;
    @Size(max = 2147483647)
    @Column(name = "reponse_question")
    private String reponseQuestion;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "question", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhParamQuestionnaire question;
    @JoinColumn(name = "entretien", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEntretienCandidat entretien;

    public YvsGrhReponseEntretien() {
    }

    public YvsGrhReponseEntretien(Long id) {
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

    public Integer getNoteReponse() {
        return noteReponse;
    }

    public void setNoteReponse(Integer noteReponse) {
        this.noteReponse = noteReponse;
    }

    public String getReponseQuestion() {
        return reponseQuestion;
    }

    public void setReponseQuestion(String reponseQuestion) {
        this.reponseQuestion = reponseQuestion;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsGrhParamQuestionnaire getQuestion() {
        return question;
    }

    public void setQuestion(YvsGrhParamQuestionnaire question) {
        this.question = question;
    }

    public YvsGrhEntretienCandidat getEntretien() {
        return entretien;
    }

    public void setEntretien(YvsGrhEntretienCandidat entretien) {
        this.entretien = entretien;
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
        if (!(object instanceof YvsGrhReponseEntretien)) {
            return false;
        }
        YvsGrhReponseEntretien other = (YvsGrhReponseEntretien) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhReponseEntretien[ id=" + id + " ]";
    }

}
