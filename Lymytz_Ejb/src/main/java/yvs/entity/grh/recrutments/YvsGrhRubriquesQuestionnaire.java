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
@Table(name = "yvs_grh_rubriques_questionnaire")
@NamedQueries({
    @NamedQuery(name = "YvsGrhRubriquesQuestionnaire.findAll", query = "SELECT y FROM YvsGrhRubriquesQuestionnaire y WHERE y.author.agence.societe=:societe"),
    @NamedQuery(name = "YvsGrhRubriquesQuestionnaire.findById", query = "SELECT y FROM YvsGrhRubriquesQuestionnaire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhRubriquesQuestionnaire.findByCodeRubrique", query = "SELECT y FROM YvsGrhRubriquesQuestionnaire y WHERE y.codeRubrique = :codeRubrique"),
    @NamedQuery(name = "YvsGrhRubriquesQuestionnaire.findByLibelleRubrique", query = "SELECT y FROM YvsGrhRubriquesQuestionnaire y WHERE y.libelleRubrique = :libelleRubrique")})
public class YvsGrhRubriquesQuestionnaire implements Serializable {

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_rubriques_questionnaire_id_seq", name = "yvs_grh_rubriques_questionnaire_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_rubriques_questionnaire_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "code_rubrique")
    private String codeRubrique;
    @Size(max = 2147483647)
    @Column(name = "libelle_rubrique")
    private String libelleRubrique;
    @OneToMany(mappedBy = "rubrique")
    private List<YvsGrhParamQuestionnaire> yvsGrhParamQuestionnaireList;

    public YvsGrhRubriquesQuestionnaire() {
    }

    public YvsGrhRubriquesQuestionnaire(Long id) {
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

    public String getCodeRubrique() {
        return codeRubrique;
    }

    public void setCodeRubrique(String codeRubrique) {
        this.codeRubrique = codeRubrique;
    }

    public String getLibelleRubrique() {
        return libelleRubrique;
    }

    public void setLibelleRubrique(String libelleRubrique) {
        this.libelleRubrique = libelleRubrique;
    }

    public List<YvsGrhParamQuestionnaire> getYvsGrhParamQuestionnaireList() {
        return yvsGrhParamQuestionnaireList;
    }

    public void setYvsGrhParamQuestionnaireList(List<YvsGrhParamQuestionnaire> yvsGrhParamQuestionnaireList) {
        this.yvsGrhParamQuestionnaireList = yvsGrhParamQuestionnaireList;
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
        if (!(object instanceof YvsGrhRubriquesQuestionnaire)) {
            return false;
        }
        YvsGrhRubriquesQuestionnaire other = (YvsGrhRubriquesQuestionnaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.recrutments.YvsGrhRubriquesQuestionnaire[ id=" + id + " ]";
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

}
