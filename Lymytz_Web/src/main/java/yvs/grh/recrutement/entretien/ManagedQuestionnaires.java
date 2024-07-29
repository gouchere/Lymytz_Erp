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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.recrutments.YvsGrhParamQuestionnaire;
import yvs.entity.grh.recrutments.YvsGrhRubriquesQuestionnaire;
import yvs.grh.UtilGrh;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedQuestionnaires extends Managed<QuestionsEntretien, YvsBaseCaisse> implements Serializable {

    private RubriquesQuestionnaire rubrique = new RubriquesQuestionnaire();
    private boolean updateRubrique;
    private List<RubriquesQuestionnaire> rubriquesQuestionnaires;
    @ManagedProperty(value = "#{questionsEntretien}")
    private QuestionsEntretien question;
    private boolean updateQuestion;
    private String newReponse;

    public ManagedQuestionnaires() {
        rubriquesQuestionnaires = new ArrayList<>();
    }

    public QuestionsEntretien getQuestion() {
        return question;
    }

    public void setQuestion(QuestionsEntretien question) {
        this.question = question;
    }

    public RubriquesQuestionnaire getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriquesQuestionnaire rubrique) {
        this.rubrique = rubrique;
    }

    public List<RubriquesQuestionnaire> getRubriquesQuestionnaires() {
        return rubriquesQuestionnaires;
    }

    public void setRubriquesQuestionnaires(List<RubriquesQuestionnaire> rubriquesQuestionnaires) {
        this.rubriquesQuestionnaires = rubriquesQuestionnaires;
    }

    public String getNewReponse() {
        return newReponse;
    }

    public void setNewReponse(String newReponse) {
        this.newReponse = newReponse;
    }

    public void addReponse() {
        if (question.getTypeReponse().equals("B") || question.getTypeReponse().equals("C")) {
            if (question.getTypeReponse().equals("B") && question.getReponses().size() < 2) {
                question.getReponses().add(newReponse);
                newReponse = "";
            } else {
                getErrorMessage("Ce type de réponse suggère deux choix uniquement !");
            }
            if (question.getTypeReponse().equals("C")) {
                question.getReponses().add(newReponse);
                newReponse = "";
            }
        }
    }

    public void loadAllRubrique() {
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        rubriquesQuestionnaires = UtilGrh.buildRubriqueQuestionnaire(dao.loadNameQueries("YvsGrhRubriquesQuestionnaire.findAll", champ, val));
    }

    public void addRubriqueQuestionnaire() {
        if (rubrique.getCodeRubrique() == null || rubrique.getLibelleRubrique() == null) {
            getErrorMessage("Le code et le libellé de la rubrique sont obligatoire !");
            return;
        }
        YvsGrhRubriquesQuestionnaire entity = new YvsGrhRubriquesQuestionnaire();
        entity.setAuthor(currentUser);
        entity.setCodeRubrique(rubrique.getCodeRubrique());
        entity.setLibelleRubrique(rubrique.getLibelleRubrique());
        if (!updateRubrique) {
            entity = (YvsGrhRubriquesQuestionnaire) dao.save1(entity);
            rubrique.setId(entity.getId());
            rubriquesQuestionnaires.add(0, rubrique);
        } else {
            entity.setId(rubrique.getId());
            dao.update(entity);
            rubrique.setId(entity.getId());
            rubriquesQuestionnaires.set(rubriquesQuestionnaires.indexOf(rubrique), rubrique);
        }
        rubrique = new RubriquesQuestionnaire();
        succes();
    }

    public void addQuestionEntretien() {
        if (controleFiche(question)) {
            YvsGrhParamQuestionnaire qu = new YvsGrhParamQuestionnaire();
            qu.setAuthor(currentUser);
            qu.setNumeroQuestion(question.getNumeroQuestion());
            qu.setQuestion(question.getQuestion());
            int i = 0;
            String ch = "";
            for (String r : question.getReponses()) {
                if (i < question.getReponses().size()) {
                    ch = ch + (r + " ; ");
                } else {
                    ch = ch + r;
                }
                i++;
            }
            qu.setReponses(ch);
            qu.setTypeReponse(question.getTypeReponse());
            qu.setRubrique(new YvsGrhRubriquesQuestionnaire(question.getRubrique().getId()));
            if (!updateQuestion) {
                qu = (YvsGrhParamQuestionnaire) dao.save1(qu);
                question.setId(qu.getId());
                QuestionsEntretien q = new QuestionsEntretien();
                cloneObject(q, question);
                rubriquesQuestionnaires.get(rubriquesQuestionnaires.indexOf(q.getRubrique())).getQuestions().add(q);
            } else {
                qu.setId(question.getId());
                dao.update(qu);
                int idx = rubriquesQuestionnaires.get(rubriquesQuestionnaires.indexOf(question.getRubrique())).getQuestions().indexOf(question);
                QuestionsEntretien q = new QuestionsEntretien();
                cloneObject(q, question);
                rubriquesQuestionnaires.get(rubriquesQuestionnaires.indexOf(question.getRubrique())).getQuestions().set(idx, q);
            }
            succes();
        }
    }

    public void deleteRubrique(RubriquesQuestionnaire r) {
        if (!r.getQuestions().isEmpty()) {
            YvsGrhParamQuestionnaire q;
            for (QuestionsEntretien qb : r.getQuestions()) {
                q = new YvsGrhParamQuestionnaire(qb.getId());
                q.setAuthor(currentUser);
                dao.delete(q);
            }
        }
        YvsGrhRubriquesQuestionnaire ru = new YvsGrhRubriquesQuestionnaire(r.getId());
        ru.setAuthor(currentUser);
        dao.delete(ru);
        rubriquesQuestionnaires.remove(r);
        succes();
    }

    public void deleteQuestion(QuestionsEntretien qb) {
        YvsGrhParamQuestionnaire q = new YvsGrhParamQuestionnaire(qb.getId());
        q.setAuthor(currentUser);
        dao.delete(q);
        rubriquesQuestionnaires.get(rubriquesQuestionnaires.indexOf(qb.getRubrique())).getQuestions().remove(qb);
        succes();

    }

    @Override
    public void resetFiche() {
        resetFiche(question);
        question.setRubrique(new RubriquesQuestionnaire());
        question.getReponses().clear();
        updateQuestion = false;
        question.setTypeReponse("A");
    }

    @Override
    public boolean controleFiche(QuestionsEntretien bean) {
        if (bean.getRubrique().getId() < 0) {
            getErrorMessage("Vous devez choisir une rubrique");
            return false;
        }
        if ((bean.getQuestion() != null) ? bean.getQuestion().trim().equals("") : true) {
            getErrorMessage("Vous devez choisir une rubrique");
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public QuestionsEntretien recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(QuestionsEntretien bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        QuestionsEntretien q = (QuestionsEntretien) ev.getObject();
        cloneObject(question, q);
        updateQuestion = true;
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
