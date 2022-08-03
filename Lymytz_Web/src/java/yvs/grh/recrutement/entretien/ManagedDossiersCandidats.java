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
import yvs.entity.grh.param.YvsGrhDomainesQualifications;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.personnel.YvsSpecialiteDiplomes;
import yvs.entity.grh.recrutments.YvsGrhCandidats;
import yvs.entity.grh.recrutments.YvsGrhDiplomesCandidat;
import yvs.entity.grh.recrutments.YvsGrhLanguesCandidats;
import yvs.entity.grh.recrutments.YvsGrhQualificationCandidat;
import yvs.entity.param.YvsDictionnaire;
import yvs.grh.UtilGrh;
import yvs.grh.bean.Diplomes;
import yvs.grh.bean.DomainesQualifications;
import yvs.grh.bean.Langues;
import yvs.parametrage.poste.PosteDeTravail;
import yvs.grh.bean.Qualification;
import yvs.grh.recrutement.DiplomeCandidat;
import yvs.grh.recrutement.SpecialitesDiplomeCandidat;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.societe.UtilSte;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedDossiersCandidats extends Managed<Candidats, YvsBaseCaisse> implements Serializable {

    private List<Candidats> dossiersCandidats, selectionsCandidats;
    private List<PosteDeTravail> postes;
    private List<YvsDictionnaire> listePays;
    private List<YvsDictionnaire> villes;
    private List<Langues> langues;
    private List<SpecialitesDiplomeCandidat> listFormations;
    private List<DomainesQualificationCandidat> listCompetences;
    private List<Qualification> listQualification;
    private List<Diplomes> listDiplomes;
    @ManagedProperty(value = "#{dossiersCandidat}")
    private Candidats candidat;
    private boolean updateCandidat;
    private QualificationCandidat competence = new QualificationCandidat(), newQualification = new QualificationCandidat();
    private DiplomeCandidat formation = new DiplomeCandidat(), newDiplome = new DiplomeCandidat();

    private SpecialitesDiplomeCandidat newSpecialite = new SpecialitesDiplomeCandidat();
    private DomainesQualifications newDomaineQualification = new DomainesQualifications();

    private LangueCandidat langue = new LangueCandidat();

    public ManagedDossiersCandidats() {
        dossiersCandidats = new ArrayList<>();
        postes = new ArrayList<>();
        listePays = new ArrayList<>();
        villes = new ArrayList<>();
        langues = new ArrayList<>();
        listFormations = new ArrayList<>();
        listCompetences = new ArrayList<>();
        listQualification = new ArrayList<>();
        listDiplomes = new ArrayList<>();
    }

    public List<Diplomes> getListDiplomes() {
        return listDiplomes;
    }

    public void setListDiplomes(List<Diplomes> listDiplomes) {
        this.listDiplomes = listDiplomes;
    }

    public List<Qualification> getListQualification() {
        return listQualification;
    }

    public void setListQualification(List<Qualification> listQualification) {
        this.listQualification = listQualification;
    }

    public LangueCandidat getLangue() {
        return langue;
    }

    public void setLangue(LangueCandidat langue) {
        this.langue = langue;
    }

    public List<Candidats> getSelectionsCandidats() {
        return selectionsCandidats;
    }

    public void setSelectionsCandidats(List<Candidats> selectionsCandidats) {
        this.selectionsCandidats = selectionsCandidats;
    }

    public List<SpecialitesDiplomeCandidat> getListFormations() {
        return listFormations;
    }

    public void setListFormations(List<SpecialitesDiplomeCandidat> listFormations) {
        this.listFormations = listFormations;
    }

    public List<DomainesQualificationCandidat> getListCompetences() {
        return listCompetences;
    }

    public void setListCompetences(List<DomainesQualificationCandidat> listCompetences) {
        this.listCompetences = listCompetences;
    }

    public QualificationCandidat getCompetence() {
        return competence;
    }

    public void setCompetence(QualificationCandidat competence) {
        this.competence = competence;
    }

    public DiplomeCandidat getFormation() {
        return formation;
    }

    public void setFormation(DiplomeCandidat formation) {
        this.formation = formation;
    }

    public Candidats getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidats candidat) {
        this.candidat = candidat;
    }

    public List<Candidats> getDossiersCandidats() {
        return dossiersCandidats;
    }

    public void setDossiersCandidats(List<Candidats> dossiersCandidats) {
        this.dossiersCandidats = dossiersCandidats;
    }

    public List<PosteDeTravail> getPostes() {
        return postes;
    }

    public void setPostes(List<PosteDeTravail> postes) {
        this.postes = postes;
    }

    public List<YvsDictionnaire> getListePays() {
        return listePays;
    }

    public void setListePays(List<YvsDictionnaire> listePays) {
        this.listePays = listePays;
    }

    public List<YvsDictionnaire> getVilles() {
        return villes;
    }

    public void setVilles(List<YvsDictionnaire> villes) {
        this.villes = villes;
    }

    public List<Langues> getLangues() {
        return langues;
    }

    public void setLangues(List<Langues> langues) {
        this.langues = langues;
    }

    public QualificationCandidat getNewQualification() {
        return newQualification;
    }

    public void setNewQualification(QualificationCandidat newQualification) {
        this.newQualification = newQualification;
    }

    public DiplomeCandidat getNewDiplome() {
        return newDiplome;
    }

    public void setNewDiplome(DiplomeCandidat newDiplome) {
        this.newDiplome = newDiplome;
    }

    public SpecialitesDiplomeCandidat getNewSpecialite() {
        return newSpecialite;
    }

    public void setNewSpecialite(SpecialitesDiplomeCandidat newSpecialite) {
        this.newSpecialite = newSpecialite;
    }

    public DomainesQualifications getNewDomaineQualification() {
        return newDomaineQualification;
    }

    public void setNewDomaineQualification(DomainesQualifications newDomaineQualification) {
        this.newDomaineQualification = newDomaineQualification;
    }

    public void addNewTelephone() {
        if (candidat.getTelephone() != null && !candidat.getTelephones().contains(candidat.getTelephone())) {
            candidat.getTelephones().add(candidat.getTelephone());
        } else {
            getErrorMessage("Format incorrect !");
        }
    }

    public void addNewAdresseMail() {
        if (candidat.getEmail() != null && !candidat.getEmails().contains(candidat.getEmail())) {
            candidat.getEmails().add(candidat.getEmail());
        } else {
            getErrorMessage("Format incorrect !");
        }
    }

    public void addLangue() {
        if (langue.getId() > 0) {
            LangueCandidat cl = buildLangueC(langue);
            if (cl == null) {
                langue.setLangue(langues.get(langues.indexOf(new Langues(langue.getId()))).getLangue());
                langue.setSave(false);
                candidat.getLangues().add(langue);
            } else {
                langue.setIdLangueCandidat(cl.getIdLangueCandidat());
                candidat.getLangues().set(candidat.getLangues().indexOf(langue), langue);
            }
            langue = new LangueCandidat();
        } else {
            getErrorMessage("Veuillez selectionné une langue de la liste");
        }
    }

    private LangueCandidat buildLangueC(LangueCandidat lc) {
        for (LangueCandidat l : candidat.getLangues()) {
            if (l.getLangue().equals(lc.getLangue())) {
                if (l.getIdLangueCandidat() > 0) {
                    return l;
                } else {
                    l.setIdLangueCandidat(-100);
                    return l;
                }
            }
        }
        return null;
    }

    public void removeTelephone(String tel) {
        candidat.getTelephones().remove(tel);
    }

    public void removeEmail(String em) {
        candidat.getEmails().remove(em);
    }

    public void removeLangue(LangueCandidat l) {
        if (!l.isSave()) {
            dao.delete(new YvsGrhLanguesCandidats((long) l.getIdLangueCandidat()));
        }
        candidat.getLangues().remove(l);
    }

    @Override
    public boolean controleFiche(Candidats bean) {
        if (bean.getPoste().getId() <= 0) {
            getErrorMessage("Vous devez renseigner le poste de candidature !");
            return false;
        }
        if (bean.getNom() == null) {
            getErrorMessage("Vous devez renseigner le nom du candidat !");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (controleFiche(candidat)) {
            YvsGrhCandidats ca = UtilGrh.buildCandidat(candidat);
            ca.setAuthor(currentUser);
            if (!updateCandidat) {
                ca.setId(null);
                ca = (YvsGrhCandidats) dao.save1(ca);
                candidat.setId(ca.getId());
                Candidats doss = new Candidats();
                cloneObject(doss, candidat);
                doss.setPoste(postes.get(postes.indexOf(doss.getPoste())));
                doss.setVille(UtilSte.buildBeanDictionnaire(villes.get(villes.indexOf(doss.getVille()))));
                doss.setLieuNaissance(UtilSte.buildBeanDictionnaire(villes.get(villes.indexOf(doss.getLieuNaissance()))));
                doss.setPaysOrigine(UtilSte.buildBeanDictionnaire(listePays.get(listePays.indexOf(doss.getPaysOrigine()))));
                dossiersCandidats.add(0, doss);
            } else {
                ca.setId(candidat.getId());
                dao.update(ca);
                Candidats doss = new Candidats();
                cloneObject(doss, candidat);
                doss.setPoste(postes.get(postes.indexOf(doss.getPoste())));
                doss.setVille(UtilSte.buildBeanDictionnaire(villes.get(villes.indexOf(doss.getVille()))));
                doss.setLieuNaissance(UtilSte.buildBeanDictionnaire(villes.get(villes.indexOf(doss.getLieuNaissance()))));
                doss.setPaysOrigine(UtilSte.buildBeanDictionnaire(listePays.get(listePays.indexOf(doss.getPaysOrigine()))));
                dossiersCandidats.set(dossiersCandidats.indexOf(candidat), doss);
            }
            if (!candidat.getLangues().isEmpty()) {
                 YvsGrhLanguesCandidats lc;
                for (LangueCandidat l : candidat.getLangues()) {
                   lc = UtilGrh.buildLangue(l);
                    lc.setAuthor(currentUser);
                    lc.setCandidat(ca);
                    if (!l.isSave()) {
                        lc = (YvsGrhLanguesCandidats) dao.save1(lc);
                        l.setIdLangueCandidat(lc.getId());
                    } else {
                        dao.update(lc);
                    }
                }
            }
        }
        succes();
        return true;
    }

    public void createNewDomaineQualification() {
        if (newDomaineQualification.getTitreDomaine() != null) {
            YvsGrhDomainesQualifications sp = new YvsGrhDomainesQualifications();
            sp.setAuthor(currentUser);
            sp.setTitreDomaine(newDomaineQualification.getTitreDomaine());
            sp = (YvsGrhDomainesQualifications) dao.save1(sp);
            newDomaineQualification.setId(sp.getId());
//            listCompetences.add(newDomaineQualification);
            newDomaineQualification = new DomainesQualifications();
        } else {
            getErrorMessage("non présent dans la liste !");
        }
    }

    public void createNewQualification() {
        if (competence.getQualification().getCodeInterne() != null && competence.getQualification().getIntitule() != null) {
//            YvsQualifications qal = UtilGrh.buildQualificationCandidat(competence);
//            qal.setAuthor(currentUser);
//            qal.setSupp(false);
//            qal = (YvsQualifications) dao.save1(qal);
//            newQualification.setId(qal.getId());
            if (listCompetences.contains(newQualification.getQualification().getDomaine())) {
//                listCompetences.get(listCompetences.indexOf(newQualification.getQualification().getDomaine())).getQualification().add(competence);
            }
        }
    }

    public void addQualificationCandidat() {
        if (candidat.getId() > 0) {
            if (competence.getQualification().getId() > 0) {
                DomainesQualifications domain = listQualification.get(listQualification.indexOf(competence.getQualification())).getDomaine();
                DomainesQualificationCandidat domCandidat = new DomainesQualificationCandidat(domain.getId(), domain.getTitreDomaine());
                YvsGrhQualificationCandidat entity = UtilGrh.buildQualificationCandidat(competence);
                entity.setId(null);
                entity.setAuthor(currentUser);
                entity.setCandidat(new YvsGrhCandidats(candidat.getId()));
                entity = (YvsGrhQualificationCandidat) dao.save1(entity);
                competence.setId(entity.getId());
                competence.setQualification(listQualification.get(listQualification.indexOf(competence.getQualification())));
                if (!candidat.getCompetences().contains(domCandidat)) {
                    domCandidat.getQualifications().add(competence);
                    candidat.getCompetences().add(0, domCandidat);
                } else {
                    domCandidat.getQualifications().add(0, competence);
//                    candidat.getCompetences().set(candidat.getCompetences().indexOf(domCandidat), domCandidat);
                    candidat.getCompetences().get(candidat.getCompetences().indexOf(domCandidat)).getQualifications().add(0, competence);
                }
                competence = new QualificationCandidat();
                succes();
            } else {
                getErrorMessage("Aucune qualification n'a été selectionné !");
            }
        } else {
            getErrorMessage("Veuillez d'abord enregistrer le candidat !");
        }
    }

    public void removeQualification(QualificationCandidat c) {
        dao.delete(new YvsGrhQualificationCandidat(c.getId()));
        candidat.getCompetences().get(candidat.getCompetences().indexOf(new DomainesQualificationCandidat(c.getQualification().getDomaine().getId()))).getQualifications().remove(c);
        if (candidat.getCompetences().get(candidat.getCompetences().indexOf(new DomainesQualificationCandidat(c.getQualification().getDomaine().getId()))).getQualifications().isEmpty()) {
            candidat.getCompetences().remove(new DomainesQualificationCandidat(c.getQualification().getDomaine().getId()));
        }
        succes();
    }

    public void removeFormation(DiplomeCandidat d) {
        dao.delete(new YvsGrhDiplomesCandidat(d.getIdDiplomeCandidat()));
        candidat.getFormations().get(candidat.getFormations().indexOf(new SpecialitesDiplomeCandidat(d.getSpecialite().getId()))).getDiplomes().remove(d);
        if (candidat.getFormations().get(candidat.getFormations().indexOf(new SpecialitesDiplomeCandidat(d.getSpecialite().getId()))).getDiplomes().isEmpty()) {
            candidat.getFormations().remove(d.getSpecialite());
        }
        succes();
    }

    public void createNewSpecialiteDiplome() {
        if (newSpecialite.getCodeInterne() != null && newSpecialite.getDesignation() != null) {
            YvsSpecialiteDiplomes sp = new YvsSpecialiteDiplomes();
            sp.setAuthor(currentUser);
            sp.setTitreSpecialite(newSpecialite.getDesignation());
            sp.setCodeInterne(newSpecialite.getCodeInterne());
            sp = (YvsSpecialiteDiplomes) dao.save1(sp);
            newSpecialite.setId(sp.getId());
            listFormations.add(newSpecialite);
            newSpecialite = new SpecialitesDiplomeCandidat();
        }
    }

    public void createNewDiplome() {
        if (newDiplome.getSpecialite().getId() > 0) {
            if (newDiplome.getDiplome().getCodeInterne() != null && newDiplome.getDiplome().getDesignation() != null) {
//                YvsDiplomes dip = UtilGrh.buildDiplome(newDiplome);
//                dip.setId(null);
//                dip.setActif(newDiplome.isActif());
//                dip.setAuthor(currentUser);
//                dip.setSupp(false);
                if (listFormations.contains(newDiplome.getSpecialite())) {
                    listFormations.get(listFormations.indexOf(newDiplome.getSpecialite()));
                } else {
                    getErrorMessage("non présent dans la liste !");
                }
            } else {
                getErrorMessage("Le code interne du diplôme et la désignation sont obligatoire !");
            }
        }
    }

    public void addFormationCandidat() {
        if (candidat.getId() > 0) {
            if (formation.getDiplome().getId() > 0) {
                Diplomes d = listDiplomes.get(listDiplomes.indexOf(new Diplomes(formation.getDiplome().getId())));
////                SpecialitesDiplomeCandidat specialite = d.getSpecialite();
////                SpecialitesDiplomeCandidat sp = new SpecialitesDiplomeCandidat(specialite.getId(), specialite.getDesignation());
////                YvsGrhQualificationCandidat entity = UtilGrh.buildQualificationCandidat(competence);
//                YvsGrhDiplomesCandidat entity = UtilGrh.buildFormationCandidat(formation);
//                entity.setId(null);
//                entity.setAuthor(currentUser);
//                entity.setCandidat(new YvsGrhCandidats(candidat.getId()));
//                entity = (YvsGrhDiplomesCandidat) dao.save1(entity);
//                formation.setIdDiplomeCandidat(entity.getId());
//                formation.setDiplome(d);
//                if (!candidat.getFormations().contains(sp)) {
//                    sp.getDiplomes().add(formation);
//                    candidat.getFormations().add(0, sp);
//                } else {
//                    sp.getDiplomes().add(0, formation);
////                    candidat.getFormations().set(candidat.getFormations().indexOf(sp), sp);
//                    candidat.getFormations().get(candidat.getFormations().indexOf(sp)).getDiplomes().add(0, formation);
//                }

                succes();
            } else {
                getErrorMessage("Aucun diplôme n'a été selectionné !");
            }
        } else {
            getErrorMessage("Veuillez d'abord enregistrer le candidat !");
        }
        formation = new DiplomeCandidat();
    }

    @Override
    public void deleteBean() {

    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Candidats recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Candidats bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            cloneObject(candidat, (Candidats) ev.getObject());
            updateCandidat = true;
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadAll() {
        champ = new String[]{"titre"};
        val = new Object[]{Constantes.T_PAYS};
        listePays =(dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val));
        val = new Object[]{Constantes.T_VILLES};
        villes = (dao.loadNameQueries("YvsDictionnaire.findByTitre", champ, val));
        champ = new String[]{"societe"};
        val = new Object[]{currentUser.getAgence().getSociete()};
        List<YvsGrhPosteDeTravail> lp = dao.loadNameQueries("YvsPosteDeTravail.findAll", champ, val);
        postes = UtilGrh.buildBeanListPoste(lp);
        langues = UtilGrh.buildLangues(dao.loadNameQueries("YvsLangues.findAll", champ, val));
        dossiersCandidats = UtilGrh.buildCandidat(dao.loadNameQueries("YvsGrhCandidats.findAll", champ, val));
        listCompetences = UtilGrh.buildBeanDomaineQualificationCandidat(dao.loadNameQueries("YvsGrhDomainesQualifications.findAll", champ, val));
        listQualification = UtilGrh.buildBeanQualification(dao.loadNameQueries("YvsQualifications.findAll", champ, val));
        listDiplomes = UtilGrh.buildDiplome(dao.loadNameQueries("YvsDiplomes.findAll", champ, val));
    }

    @Override
    public void resetFiche() {
        resetFiche(candidat);
        candidat.getTelephones().clear();
        candidat.getEmails().clear();
        candidat.getCompetences().clear();
        candidat.getFormations().clear();
        candidat.setActif(true);
        updateCandidat = false;
    }
}
