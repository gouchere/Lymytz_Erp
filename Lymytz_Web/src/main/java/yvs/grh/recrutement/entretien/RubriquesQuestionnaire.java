/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp Elite 8300
 */
public class RubriquesQuestionnaire {

    private long id;
    private String codeRubrique;
    private String libelleRubrique;
    private List<QuestionsEntretien> questions;

    public RubriquesQuestionnaire() {
        questions = new ArrayList<>();
    }

    public RubriquesQuestionnaire(long id, String codeRubrique, String libelleRubrique) {
        this.id = id;
        this.codeRubrique = codeRubrique;
        this.libelleRubrique = libelleRubrique;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<QuestionsEntretien> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsEntretien> questions) {
        this.questions = questions;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RubriquesQuestionnaire other = (RubriquesQuestionnaire) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
