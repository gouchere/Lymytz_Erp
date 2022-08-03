/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class QuestionsEntretien implements Serializable {

    private long id;
    private RubriquesQuestionnaire rubrique = new RubriquesQuestionnaire();
    private int numeroQuestion;
    private String question;
    private String typeReponse = "A"; 
/*  A déterminer (Réponse ouverte dont la pertinance est donnée lors de l'examination des résultats) 
    B Réponses à choix unique (radioButton) 
    C: choix multiple (manyMenu)
    D: choix booleen */
    private List<String> reponses;

    public QuestionsEntretien() {
        reponses = new ArrayList<>();
    }

    public QuestionsEntretien(int numeroQuestion, String question, String typeReponse) {
        this.numeroQuestion = numeroQuestion;
        this.question = question;
        this.typeReponse = typeReponse;
        reponses = new ArrayList<>();
    }

    public List<String> getReponses() {
        return reponses;
    }

    public void setReponses(List<String> reponses) {
        this.reponses = reponses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumeroQuestion() {
        return numeroQuestion;
    }

    public void setNumeroQuestion(int numeroQuestion) {
        this.numeroQuestion = numeroQuestion;
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

    public RubriquesQuestionnaire getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriquesQuestionnaire rubrique) {
        this.rubrique = rubrique;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final QuestionsEntretien other = (QuestionsEntretien) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
