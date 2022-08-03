/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.grh.recrutments.YvsGrhCandidats;
import yvs.entity.grh.recrutments.YvsGrhEntretienCandidat;
import yvs.entity.grh.recrutments.YvsGrhParamQuestionnaire;
import yvs.entity.grh.recrutments.YvsGrhReponseEntretien;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Employe;
import yvs.grh.recrutement.EntretienCandidat;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedEntretien extends Managed<EntretienCandidat, YvsBaseCaisse> implements Serializable {

    private EntretienCandidat model = new EntretienCandidat(), selectModel;
    private List<QuestionsEntretien> questions;
    private List<Candidats> candidats;
    private List<QuestionModel> reponses;
    private List<EntretienCandidat> listEntretiens;
    private List<EntretienCandidat> sessionsEntretien;

    private String lieuEntretien;
    private Date dateEntretien;
    private Employe examinateur = new Employe();

    private String newQuestion;

    public ManagedEntretien() {
        questions = new ArrayList<>();
        candidats = new ArrayList<>();
        reponses = new ArrayList<>();
        listEntretiens = new ArrayList<>();
        sessionsEntretien = new ArrayList<>();
    }

    public List<EntretienCandidat> getSessionsEntretien() {
        return sessionsEntretien;
    }

    public void setSessionsEntretien(List<EntretienCandidat> sessionsEntretien) {
        this.sessionsEntretien = sessionsEntretien;
    }

    public EntretienCandidat getModel() {
        return model;
    }

    public void setModel(EntretienCandidat model) {
        this.model = model;
    }

    public List<QuestionsEntretien> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionsEntretien> questions) {
        this.questions = questions;
    }

    public List<Candidats> getCandidats() {
        return candidats;
    }

    public void setCandidats(List<Candidats> candidats) {
        this.candidats = candidats;
    }

    public List<QuestionModel> getReponses() {
        return reponses;
    }

    public void setReponses(List<QuestionModel> reponses) {
        this.reponses = reponses;
    }

    public List<EntretienCandidat> getListEntretiens() {
        return listEntretiens;
    }

    public void setListEntretiens(List<EntretienCandidat> listEntretiens) {
        this.listEntretiens = listEntretiens;
    }

    public String getNewQuestion() {
        return newQuestion;
    }

    public void setNewQuestion(String newQuestion) {
        this.newQuestion = newQuestion;
    }

    public EntretienCandidat getSelectModel() {
        return selectModel;
    }

    public void setSelectModel(EntretienCandidat selectModel) {
        this.selectModel = selectModel;
    }

    public String getLieuEntretien() {
        return lieuEntretien;
    }

    public void setLieuEntretien(String lieuEntretien) {
        this.lieuEntretien = lieuEntretien;
    }

    public Date getDateEntretien() {
        return dateEntretien;
    }

    public void setDateEntretien(Date dateEntretien) {
        this.dateEntretien = dateEntretien;
    }

    public Employe getExaminateur() {
        return examinateur;
    }

    public void setExaminateur(Employe examinateur) {
        this.examinateur = examinateur;
    }

    private boolean containsCandidat(Candidats d) {
        for (EntretienCandidat e : sessionsEntretien) {
            if (e.getCandidat().equals(d)) {
                return true;
            }
        }
        return false;
    }

    public void loadFormulaire(SelectEvent ev) {
        Candidats candidat = (Candidats) ev.getObject();
        if (!containsCandidat(candidat)) {
            champ = new String[]{"societe"};
            val = new Object[]{currentUser.getAgence().getSociete()};
            questions = UtilGrh.buildQuestionnaire(dao.loadNameQueries("YvsGrhParamQuestionnaire.findAll", champ, val));
            DynaFormRow row;
            model.setCandidat(candidat);
            RubriquesQuestionnaire rubrique = (!questions.isEmpty()) ? questions.get(0).getRubrique() : null;
            if (rubrique != null) {
                row = model.createRegularRow();
                row.addControl(rubrique.getLibelleRubrique(), "separator", 1, 1);
            }
            for (QuestionsEntretien qe : questions) {
                if (rubrique != null) {
                    if (!rubrique.equals(qe.getRubrique())) {
                        row = model.createRegularRow();
                        row.addControl(qe.getRubrique().getLibelleRubrique(), "separator", 1, 1);
                    }
                }
                switch (qe.getTypeReponse()) {
                    case "A":
                        row = model.createRegularRow();
//                    lab = row.addLabel(qe.getQuestion());
                        row.addControl(qe.getQuestion() + " ? ", "text", 1, 1);
                        row.addControl(new QuestionModel(qe.getId(), qe.getQuestion(), "A" + qe.getId(), true), "textarea", 2, 1);
//                    lab.setForControl(control);
//                    row.addControl(control, 2, 1);
                        break;
                    case "B": //question à chois unique (Bouton radio)
                        row = model.createRegularRow();
                        row.addControl(qe.getQuestion() + " ? ", "text", 1, 1);
                        List<SelectItem> choix = new ArrayList<>();
                        for (String st : qe.getReponses()) {
                            choix.add(new SelectItem(st, st));
                        }
                        row.addControl(new QuestionModel(qe.getId(), qe.getQuestion(), (qe.getReponses().isEmpty()) ? "" : qe.getReponses().get(0), false, choix), "radiochoice", 2, 1);
                        break;
                    case "C": //question boolenne (checkbox)
                        int i = 1000;
                        int col = 0;
                        row = model.createRegularRow();
                        row.addControl(qe.getQuestion() + " ? ", "text", 1, 1);
                        row.addControl(new QuestionModel(qe.getId(), qe.getQuestion(), "C" + i--, false, false), "booleanchoice", col, 1);
                        break;
                    case "D": //question choix multiple (manyMenu)
                        row = model.createRegularRow();
                        row.addControl(qe.getQuestion() + " ? ", "text", 1, 1);
                        List<SelectItem> choix_ = new ArrayList<>();
                        for (String st : qe.getReponses()) {
                            choix_.add(new SelectItem(st, st));
                        }
                        row.addControl(new QuestionModel(qe.getId(), qe.getQuestion(), (qe.getReponses().isEmpty()) ? "" : qe.getReponses().get(0), false, choix_), "selectMany", 2, 1);
                        break;
                }
                rubrique = qe.getRubrique();
            }
            sessionsEntretien.add(model);
        }
        model = new EntretienCandidat();
    }

    public void OpenToAddNewQuestion(EntretienCandidat model) {
        selectModel = model;
    }

    public void closeTabEntretien(EntretienCandidat model) {
        sessionsEntretien.remove(model);
    }

    public void addNewQuestionModel() {
        if (selectModel != null && newQuestion != null) {
            YvsGrhParamQuestionnaire qu = new YvsGrhParamQuestionnaire();
            qu.setAuthor(currentUser);
            qu.setNumeroQuestion(1);
            qu.setQuestion(newQuestion);
            qu.setTypeReponse("A");
            qu.setRubrique(null);
            qu = (YvsGrhParamQuestionnaire) dao.save1(qu);
            selectModel.setId(selectModel.getCandidat().getId());
            DynaFormRow row = sessionsEntretien.get(sessionsEntretien.indexOf(selectModel)).createRegularRow();
            row.addControl(newQuestion + " ? ", "text", 1, 1);
            row.addControl(new QuestionModel(qu.getId(), newQuestion, "A" + qu.getId(), true), "textarea", 2, 1);
        }
        update("formulaire_entretien:" + sessionsEntretien.indexOf(selectModel) + ":dynaForm");
//        update("dynaForm");
        getInfoMessage("Question added !");
    }

    public String submitForm(EntretienCandidat en) {
//        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
//        boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.addCallbackParam("isValid", !hasErrors);
        QuestionModel q;
        YvsGrhEntretienCandidat entretien = saveNewSessionEntretien(en);        
        if (entretien != null) {
            en.setId(entretien.getId());
            YvsGrhReponseEntretien entityRep;
            for (DynaFormControl control : en.getControls()) {
                if (control.getData() instanceof QuestionModel) {
                    q = (QuestionModel) control.getData();
                    entityRep = new YvsGrhReponseEntretien();
                    entityRep.setAuthor(currentUser);
                    entityRep.setEntretien(entretien);
                    entityRep.setNoteReponse(q.getNote());
                    entityRep.setQuestion(new YvsGrhParamQuestionnaire(q.getIdQuestion()));
                    if (q.getValues() == null) {
                        entityRep.setReponseQuestion(q.getValue().toString());
                    } else {
                        String res = "";
                        for (Object v : q.getValues()) {
                            res = res + (String) v + " ; ";
                        }
                        entityRep.setReponseQuestion(res);
                    }
                    entityRep = (YvsGrhReponseEntretien) dao.save1(entityRep);
                    q.setId(entityRep.getId());
                }
            }
            succes();
        }
        return null;
    }

    private YvsGrhEntretienCandidat saveNewSessionEntretien(EntretienCandidat en) {
        if (!listEntretiens.contains(en)) {
            en.setDate(dateEntretien);
            en.setExaminateur(examinateur);
            en.setLieu(lieuEntretien);
            en.setSave(true);
            YvsGrhEntretienCandidat entity = new YvsGrhEntretienCandidat();
            entity.setAuthor(currentUser);
            entity.setLieu(lieuEntretien);
            entity.setCandidat(new YvsGrhCandidats(en.getCandidat().getId()));
            entity.setDateEntretien(dateEntretien);
            //entity.setExaminateur(new YvsGrhEmployes());
            entity.setHeure(new Date());
            entity = (YvsGrhEntretienCandidat) dao.save1(entity);
            en.setId(entity.getId());
//            listEntretiens.add(0, en);
            return entity;
        }
        return null;
    }

    @Override
    public boolean controleFiche(EntretienCandidat bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public EntretienCandidat recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(EntretienCandidat bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadAll() {
        //charge les candidats
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        candidats = UtilGrh.buildCandidat(dao.loadNameQueries("YvsGrhCandidats.findAll", champ, val));
        //charge les entretients déjà réalisé
        listEntretiens = UtilGrh.buildEntretient(dao.loadNameQueries("YvsGrhEntretienCandidat.findAll", champ, val));
    }
}
