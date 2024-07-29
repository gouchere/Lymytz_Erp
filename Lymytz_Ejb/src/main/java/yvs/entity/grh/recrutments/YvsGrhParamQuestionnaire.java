/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.recrutments;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_grh_param_questionnaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhParamQuestionnaire.findAll", query = "SELECT y FROM YvsGrhParamQuestionnaire y WHERE y.author.agence.societe=:societe ORDER BY y.rubrique.id, y.numeroQuestion"),
    @NamedQuery(name = "YvsGrhParamQuestionnaire.findById", query = "SELECT y FROM YvsGrhParamQuestionnaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhParamQuestionnaire.findByQuestion", query = "SELECT y FROM YvsGrhParamQuestionnaire y WHERE y.question = :question"),
    @NamedQuery(name = "YvsGrhParamQuestionnaire.findByTypeReponse", query = "SELECT y FROM YvsGrhParamQuestionnaire y WHERE y.typeReponse = :typeReponse"),
    @NamedQuery(name = "YvsGrhParamQuestionnaire.findByReponses", query = "SELECT y FROM YvsGrhParamQuestionnaire y WHERE y.reponses = :reponses")})
public class YvsGrhParamQuestionnaire implements Serializable {


    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_param_questionnaire_id_seq", name = "yvs_param_questionnaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_param_questionnaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "numero_question")
    private Integer numeroQuestion;
    @Size(max = 2147483647)
    @Column(name = "question")
    private String question;
    @Size(max = 2147483647)
    @Column(name = "type_reponse")
    private String typeReponse;
    @Size(max = 2147483647)
    @Column(name = "reponses")
    private String reponses;
    @JoinColumn(name = "rubrique", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhRubriquesQuestionnaire rubrique;

    public YvsGrhParamQuestionnaire() {
    }

    public YvsGrhParamQuestionnaire(Long id) {
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTypeReponse() {
        return typeReponse;
    }

    public void setTypeReponse(String typeReponse) {
        this.typeReponse = typeReponse;
    }

    public String getReponses() {
        return reponses;
    }

    public void setReponses(String reponses) {
        this.reponses = reponses;
    }

    public YvsGrhRubriquesQuestionnaire getRubrique() {
        return rubrique;
    }

    public void setRubrique(YvsGrhRubriquesQuestionnaire rubrique) {
        this.rubrique = rubrique;
    }

    public Integer getNumeroQuestion() {
        return numeroQuestion;
    }

    public void setNumeroQuestion(Integer numeroQuestion) {
        this.numeroQuestion = numeroQuestion;
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
        if (!(object instanceof YvsGrhParamQuestionnaire)) {
            return false;
        }
        YvsGrhParamQuestionnaire other = (YvsGrhParamQuestionnaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhParamQuestionnaire[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
