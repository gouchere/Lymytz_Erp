/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.grh.activite.YvsGrhCongeEmps;
import yvs.entity.grh.param.YvsGrhSerieCongeTechnique;
import yvs.entity.grh.param.YvsGrhTypeChomage;
import yvs.entity.grh.personnel.YvsGrhDetailsChomageTechnique;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.grh.parametre.SerieChomageTech;
import yvs.grh.parametre.ParamsTauxChomageTechnique;
import yvs.util.Managed;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedChomage extends Managed<SerieChomageTech, YvsGrhSerieCongeTechnique> implements Serializable {

    @ManagedProperty(value = "#{typeChomage}")
    private SerieChomageTech chomageEmps;
    private List<YvsGrhSerieCongeTechnique> listChomages;
    private boolean selectChomageEmps, updateChomageEmps;
    private String chaineSelectChomage;
    private YvsGrhDetailsChomageTechnique periode = new YvsGrhDetailsChomageTechnique();

    private List<YvsGrhTypeChomage> typesChomage;
    private ParamsTauxChomageTechnique typeChomage = new ParamsTauxChomageTechnique();

    private boolean initForm;

    public ManagedChomage() {
        listChomages = new ArrayList<>();
        typesChomage = new ArrayList<>();
    }

    public List<YvsGrhSerieCongeTechnique> getListChomages() {
        return listChomages;
    }

    public void setListChomages(List<YvsGrhSerieCongeTechnique> listChomages) {
        this.listChomages = listChomages;
    }

    public SerieChomageTech getChomageEmps() {
        return chomageEmps;
    }

    public void setChomageEmps(SerieChomageTech chomageEmps) {
        this.chomageEmps = chomageEmps;
    }

    public boolean isSelectChomageEmps() {
        return selectChomageEmps;
    }

    public void setSelectChomageEmps(boolean selectChomageEmps) {
        this.selectChomageEmps = selectChomageEmps;
    }

    public boolean isUpdateChomageEmps() {
        return updateChomageEmps;
    }

    public void setUpdateChomageEmps(boolean updateChomageEmps) {
        this.updateChomageEmps = updateChomageEmps;
    }

    public void setChaineSelectChomage(String chaineSelectChomage) {
        this.chaineSelectChomage = chaineSelectChomage;
    }

    public String getChaineSelectChomage() {
        return chaineSelectChomage;
    }

    public YvsGrhDetailsChomageTechnique getPeriode() {
        return periode;
    }

    public void setPeriode(YvsGrhDetailsChomageTechnique periode) {
        this.periode = periode;
    }

    public void saveNewTypeChomage() {
//        if (typeChomage.getLibelle() != null && typeChomage.getCode() != null) {
//            if (typeChomage.getCode().trim().length() < 3) {
//                YvsGrhTypeChomage tc = new YvsGrhTypeChomage();
//                tc.setLibelle(typeChomage.getLibelle());
//                tc.setCode(typeChomage.getCode());
//                tc.setActif(typeChomage.isActif());
//                tc.setDescription(typeChomage.getDescription());
//                tc.setAuthor(currentUser);
//                tc.setSociete(currentAgence.getSociete());
//                if (typeChomage.getId() <= 0) {
//                    tc.setId(null);
//                    tc = (YvsGrhTypeChomage) dao.save1(tc);
//                    typesChomage.add(0, tc);
//                } else {
//                    tc.setId(typeChomage.getId());
//                    dao.update(tc);
//                    int idx = typesChomage.indexOf(tc);
//                    if (idx >= 0) {
//                        typesChomage.set(idx, tc);
//                    }
//                }
//                succes();
//            } else {
//                getErrorMessage("Le code doit contenir au moins trois caractères !");
//            }
//        } else {
//            getErrorMessage("Le formulaire de création du type est incorrecte !", "vous devez préciser le code et le libellé !");
//        }
    }

    public void addParamsTaux() {
        //récupère les paramètres mensuel du type en cours
//        if (typeChomage.getId() > 0) {
//            typeChomage.setParamsTaux(dao.loadNameQueries("YvsGrhParamsTauxChomageTech.findByTypeCt", new String[]{"typeC"}, new Object[]{new YvsGrhTypeChomage(typeChomage.getId())}));
//            YvsGrhParamsTauxChomageTech p = new YvsGrhParamsTauxChomageTech();
//            p.setNumMois(typeChomage.getParamsTaux().hashCode() + 1);
//            p.setTaux(taux);
//            p.setTypeChomage(new YvsGrhTypeChomage(typeChomage.getId()));
//            typeChomage.getParamsTaux().add(p);
//        } else {
//            getErrorMessage("Aucun type n'a été selectionné, ou enregistré !");
//        }
    }

    public YvsGrhTypeChomage buildChomageEmps(ParamsTauxChomageTechnique c) {
        YvsGrhTypeChomage r = new YvsGrhTypeChomage();
        if (c != null) {
//            r.setId(c.getId());
            r.setActif(c.isActif());
            r.setDescription(c.getDescription());
//            r.setCode(c.getCode());
//            r.setLibelle(c.getLibelle());
//            r.setSociete(currentAgence.getSociete());
//            r.setEtat((c.getEtat() != ' ') ? c.getEtat() : 'R');
//            r.setAuthor(currentUser);
        }
        return r;
    }

    @Override
    public boolean controleFiche(SerieChomageTech bean) {
        if (bean.getDateDebut() == null) {
            getErrorMessage("Vous devez entrer la date de debut");
            return false;
        }
        if (bean.getDateFin()== null) {
            getErrorMessage("Vous devez entrer la date de fin");
            return false;
        }
//        if ((bean.getEmploye() == null) ? bean.getEmploye().getId() == 0 : false) {
//            getErrorMessage("Vous devez selectionner un employe");
//            return false;
//        }
//        if ((bean.getTypeChomage() == null) ? bean.getTypeChomage().getId() == 0 : false) {
//            getErrorMessage("Vous devez specifier le type de chomage");
//            return false;
//        }
        return true;
    }

    @Override
    public SerieChomageTech recopieView() {
        SerieChomageTech bean = new SerieChomageTech();
//        bean.setActif(chomageEmps.isActif());
//        bean.setDateChomage(chomageEmps.getDateChomage());
//        bean.setDateDebut(chomageEmps.getDateDebut());
//        bean.setDateFin(chomageEmps.getDateFin());
//        bean.setTypeChomage(chomageEmps.getTypeChomage());
        cloneObject(bean, chomageEmps);
        return bean;
    }

    @Override
    public void populateView(SerieChomageTech bean) {
        cloneObject(chomageEmps, bean);
    }

    public void populateViewEmploye(Employe bean) {
//        chomageEmps.getEmploye().setId(bean.getId());
//        chomageEmps.getEmploye().setNom(bean.getNom());
//        chomageEmps.getEmploye().setPrenom(bean.getPrenom());
//        chomageEmps.getEmploye().setMatricule(bean.getMatricule());
    }

    public void selectChomage(ParamsTauxChomageTechnique bean) {
//        listChomageEmps.get(listChomageEmps.indexOf(bean)).setSelectActif(bean.isSelectActif());
//        if (listSelectChomageEmps.contains(bean)) {
//            listSelectChomageEmps.remove(bean);
//        } else {
//            listSelectChomageEmps.add(bean);
//        }
//        if (listSelectChomageEmps.isEmpty()) {
//            resetFiche();
//        } else {
////            populateView(listSelectChomageEmps.get(listSelectChomageEmps.size() - 1));
//        }
        setUpdateChomageEmps(isSelectChomageEmps());
        update("body_chomage_emps_00");
        update("head_chomage_emps_00");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
//            entityChomage = (YvsGrhTypeChomage) ev.getObject();
//            cloneObject(chomageEmps, UtilGrh.buildBeanTypeChomage(entityChomage));
        }
    }

    public void loadOnViewEmploye(SelectEvent ev) {
//        if (ev != null) {
//            YvsGrhEmployes bean = (YvsGrhEmployes) ev.getObject();
//            if (!chomageEmps.getEmployes().contains(bean)) {
//                entityChomage = generedPeriodeChomage(entityChomage, bean);
//                if (entityChomage != null) {
//                    chomageEmps.getEmployes().add(bean);
//                } else {
//                    getErrorMessage("Le congé n'a pas pu être enregistrer !");
//                }
//            } else {
//                getErrorMessage("Employé déjà lié !");
//            }
//        }
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listChomages = dao.loadNameQueries("YvsGrhTypeChomage.findAll", champ, val);

    }

    public void loadAllSerieConge(boolean avancer) {
        ParametreRequete p = new ParametreRequete("y.typeCongeTech.societe", "societe", currentUser.getAgence().getSociete(), "=", "AND");
        paginator.addParam(p);
        paginator.executeDynamicQuery("YvsGrhSerieCongeTechnique", "y.dateDebut DESC", avancer, initForm, (int) imax, dao);
    }

    public void choixPeriodeChomageEmps(SelectEvent ev) {
        YvsGrhEmployes emp = (YvsGrhEmployes) ev.getObject();
        if (emp != null && chomageEmps.getId() > 0) {
            chomageEmps.setDetailsChamageEmp(dao.loadNameQueries("YvsGrhDetailsChomageTechnique.findDetailChomEmploye", new String[]{"employe", "chomage"}, new Object[]{emp, new YvsGrhTypeChomage(chomageEmps.getId())}));
        }
    }

    public void toogleActivePeriode(YvsGrhDetailsChomageTechnique pe) {
        if (pe != null) {
            pe.setActif(!pe.getActif());
            pe.setAuthor(currentUser);
            dao.update(pe);
        }
    }

    public void deletePeriodeChom(YvsGrhDetailsChomageTechnique pe) {
        if (pe != null) {
            pe.setAuthor(currentUser);
            try {
                dao.delete(pe);
                chomageEmps.getDetailsChamageEmp().remove(pe);
            } catch (Exception ex) {
                log.log(Level.SEVERE, null, ex);
                getErrorMessage("Impossible de supprimer !");
            }
        }
    }

    public void openViewUpdatePeriode(YvsGrhDetailsChomageTechnique pe) {
        periode = pe;
        openDialog("dlgEditPeriodChT");
    }

    public void saveNewPeriode() {
        if (periode != null) {
//            if (periode.getId() != null && periode.getConge() != null && periode.getTypeChomage() != null) {
//                if (periode.getDebutPeriode() != null && periode.getFinPeriode() != null) {
//                    if (periode.getDebutPeriode().before(periode.getFinPeriode()) || periode.getDebutPeriode().equals(periode.getFinPeriode())) {
//                        if (periode.getDuree() > 0) {
//                            periode.setAuthor(currentUser);
//                            dao.update(periode);
//                            chomageEmps.getDetailsChamageEmp().set(chomageEmps.getDetailsChamageEmp().indexOf(periode), periode);
//                        }
//                    } else {
//                        getErrorMessage("La période est incorrecte !");
//                    }
//                } else {
//                    getErrorMessage("La période est incorrecte !");
//                }
//            }
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(chomageEmps);
//        chomageEmps.setDateChomage(new Date());
//        chomageEmps.setDateDebut(new Date());
//        chomageEmps.setDateFin(new Date());
//        chomageEmps.getEmployes().clear();
    }

    @Override
    public void resetPage() {
    }

    @Override
    public boolean saveNew() {
//        try {
//            ParamsTauxChomageTechnique bean = recopieView();            
//            if (controleFiche(chomageEmps)) {
//                entityChomage = buildChomageEmps(chomageEmps);
//                if (chomageEmps.getId() <= 0) {
//                    entityChomage = (YvsGrhTypeChomage) dao.save1(entityChomage);
//                    chomageEmps.setId(entityChomage.getId());
//                    //générer les périodes et les taux par défaut                 
//                    listChomages.add(entityChomage);
//                } else {
//                    dao.update(entityChomage);
//                    listChomages.set(listChomages.indexOf(chomageEmps), entityChomage);
//                }
//                succes();
//            }
//            return true;
//        } catch (Exception ex) {
//            getErrorMessage("Impossible d'enregistrer cet élément !");
//            Logger.getLogger(ManagedChomage.class.getName()).log(Level.SEVERE, null, ex);
            return false;
//        }
    }

    private long id = -1000;
    private double taux;

    private YvsGrhTypeChomage generedPeriodeChomage(YvsGrhTypeChomage ch, YvsGrhEmployes emp) {
//        if (entityChomage != null) {
//            Calendar c = Calendar.getInstance();
//            c.setTime(ch.getDateDebut());
//            YvsGrhDetailsChomageTechnique det;
//            Date debut = ch.getDateDebut();
//            Date suite;
//            taux = 55;
//            YvsGrhCongeEmps conge = saveCongesEmploye(emp);
//            if (conge != null) {
//                int d;
//                while (debut.before(ch.getDateFin())) {
//                    det = new YvsGrhDetailsChomageTechnique();
//                    det.setActif(true);
//                    det.setConge(conge);
//                    det.setDebutPeriode(debut);
//                    d = c.get(Calendar.DAY_OF_MONTH);
//                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
//                    suite = (c.getTime().after(ch.getDateFin())) ? ch.getDateFin() : c.getTime();
//                    det.setDuree(c.get(Calendar.DAY_OF_MONTH) - d);
//                    det.setTaux(taux = taux - 5);
////                    det.setTypeChomage(ch);
//                    det.setFinPeriode(suite);
//                    det.setAuthor(currentUser);
//                    det = (YvsGrhDetailsChomageTechnique) dao.save1(det);
////                    ch.getDetailsChomage().add(det);
//                    c.add(Calendar.DAY_OF_MONTH, 1);
//                    debut = c.getTime();
//                }
//            }
//            return ch;
//        } else {
            return null;
//        }
    }

    public double calculDureeCt(SelectEvent ev) {
        if (periode != null) {
            if (periode.getDebutPeriode() != null && periode.getFinPeriode() != null) {
                periode.setDuree(Utilitaire.countDayBetweenDate(periode.getDebutPeriode(), periode.getFinPeriode()));
                return periode.getDuree();
            }
        }
        return 0;
    }

    @Override
    public void deleteBean() {
        boolean delete;
        if (chaineSelectChomage != null) {
            String numroLine[] = chaineSelectChomage.split("-");
            List<String> l = new ArrayList<>();
            try {
                int index;
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    dao.delete(new YvsGrhTypeChomage(listChomages.get(index).getId()));
                    listChomages.remove(index);
                    l.add(numroLine1);
                }
                chaineSelectChomage = "";
                succes();
                delete = true;

            } catch (NumberFormatException ex) {
                chaineSelectChomage = "";
                for (String numroLine1 : numroLine) {
                    if (!l.contains(numroLine1)) {
                        chaineSelectChomage += numroLine1 + "-";
                    }
                }
                delete = false;
            }
            if (!delete) {
                getErrorMessage("Impossible de supprimer cet élément");
            }
        }
    }

    @Override
    public void updateBean() {
        setSelectChomageEmps(false);
        update("body_chomage_emps_00");
        update("head_chomage_emps_00");
    }

    @Override
    public void changeView() {
        resetFiche();
        update("body_chomage_emps_00");
        update("head_chomage_emps_00");
    }

    public void chooseTypeContrat(ValueChangeEvent ev) {
        if (ev != null) {
            int id_ = (int) ev.getNewValue();
            if (id_ > 0) {
                ParamsTauxChomageTechnique bean = new ParamsTauxChomageTechnique(id_);
//                chomageEmps.getTypeChomage().setId(id);
//                chomageEmps.getTypeChomage().setLibelle(listChomages.get(listChomages.indexOf(bean)).getLibelle());
            } else if (id_ == -1) {
                openDialog("dlgTypeChomage");
            }
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {

    }

    public void removeEmploye(YvsGrhEmployes emp) {
        try {
            //suprime le lien entre congé et chomage technique en supprimant juste le congé lié
            if (chomageEmps.getId() > 0 && emp != null) {
                chomageEmps.setDetailsChamageEmp(dao.loadNameQueries("YvsGrhDetailsChomageTechnique.findDetailChomEmploye", new String[]{"employe", "chomage"}, new Object[]{emp, new YvsGrhTypeChomage(chomageEmps.getId())}));
                if (!chomageEmps.getDetailsChamageEmp().isEmpty()) {
                    YvsGrhCongeEmps co = chomageEmps.getDetailsChamageEmp().get(0).getConge();
                    co.setAuthor(currentUser);
                    dao.delete(co);
                    chomageEmps.getDetailsChamageEmp().clear();
//                    chomageEmps.getEmployes().remove(emp);
                }
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            getErrorMessage("Impossible de supprimer !");
        }
    }

    public void clotureChom() {
        if (chomageEmps.getId() > 0) {
//            YvsGrhTypeChomage t = listChomages.get(listChomages.indexOf(chomageEmps));
////            YvsGrhTypeChomage en = buildChomageEmps(t);
//            t.setEtat('C');
//            chomageEmps.setEtat('C');
//            dao.update(t);
        }
    }
}
