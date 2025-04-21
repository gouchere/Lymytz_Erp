/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.creneau;

import yvs.commercial.*;
import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.commercial.creneau.YvsComCreneauDepot;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.grh.JoursOuvres;
import yvs.grh.UtilGrh;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.entrepot.Depots;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedCreneau extends Managed<Creneau, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{creneau}")
    private Creneau creneau;
    private List<YvsComCreneauDepot> creneaux, creneauxHist;
    private YvsComCreneauDepot selectCreneaux;

    private List<Depots> depots;
    private List<YvsJoursOuvres> jours;

    private TypeCreneau type = new TypeCreneau();
    private List<YvsGrhTrancheHoraire> types;

    private List<String> criteres;

    private String tabIds;
    private boolean updateCrenau;

    public ManagedCreneau() {
        creneaux = new ArrayList<>();
        criteres = new ArrayList<>();
        creneauxHist = new ArrayList<>();
        types = new ArrayList<>();
        jours = new ArrayList<>();
        depots = new ArrayList<>();
    }

    public YvsComCreneauDepot getSelectCreneaux() {
        return selectCreneaux;
    }

    public void setSelectCreneaux(YvsComCreneauDepot selectCreneaux) {
        this.selectCreneaux = selectCreneaux;
    }

    public List<YvsComCreneauDepot> getCreneauxHist() {
        return creneauxHist;
    }

    public void setCreneauxHist(List<YvsComCreneauDepot> creneauxHist) {
        this.creneauxHist = creneauxHist;
    }

    public TypeCreneau getType() {
        return type;
    }

    public void setType(TypeCreneau type) {
        this.type = type;
    }

    public List<Depots> getDepots() {
        return depots;
    }

    public void setDepots(List<Depots> depots) {
        this.depots = depots;
    }

    public List<YvsJoursOuvres> getJours() {
        return jours;
    }

    public void setJours(List<YvsJoursOuvres> jours) {
        this.jours = jours;
    }

    public List<YvsGrhTrancheHoraire> getTypes() {
        return types;
    }

    public void setTypes(List<YvsGrhTrancheHoraire> types) {
        this.types = types;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public List<YvsComCreneauDepot> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<YvsComCreneauDepot> creneaux) {
        this.creneaux = creneaux;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public boolean isUpdateCrenau() {
        return updateCrenau;
    }

    public void setUpdateCrenau(boolean updateCrenau) {
        this.updateCrenau = updateCrenau;
    }

    public List<String> getCriteres() {
        return criteres;
    }

    public void setCriteres(List<String> criteres) {
        this.criteres = criteres;
    }

    @Override
    public void loadAll() {
        loadAllCrenau();
        loadAllTypeCreneau();
        loadAllDepot();
        loadAllJour();
    }

    public void loadAllCrenau() {
        creneaux = dao.loadNameQueries("YvsComCreneauDepot.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllCreneauHist() {
        creneauxHist.clear();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        String query = "YvsComCreneauDepot.findAll";
        boolean c = false, e = false;
        if ((creneau.getDepot() != null) ? creneau.getDepot().getId() > 0 : false) {
            if ((creneau.getJour() != null) ? creneau.getJour().getId() > 0 : false) {
                if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
                    champ = new String[]{"depot", "jour", "type"};
                    val = new Object[]{new YvsBaseDepots(creneau.getDepot().getId()), new YvsJoursOuvres(creneau.getJour().getId()), new YvsGrhTrancheHoraire(creneau.getTranche().getId())};
                    query = "YvsComCreneauDepot.findByTypeJourDepot";
                    e = true;
                } else {
                    champ = new String[]{"depot", "jour"};
                    val = new Object[]{new YvsBaseDepots(creneau.getDepot().getId()), new YvsJoursOuvres(creneau.getJour().getId())};
                    query = "YvsComCreneauDepot.findByJourDepot";
                }
                c = true;
            } else {
                if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
                    champ = new String[]{"depot", "type"};
                    val = new Object[]{new YvsBaseDepots(creneau.getDepot().getId()), new YvsGrhTrancheHoraire(creneau.getTranche().getId())};
                    query = "YvsComCreneauDepot.findByTypeDepot";
                } else {
                    champ = new String[]{"depot"};
                    val = new Object[]{new YvsBaseDepots(creneau.getDepot().getId())};
                    query = "YvsComCreneauDepot.findByDepot";
                }
            }
        } else {
            if ((creneau.getJour() != null) ? creneau.getJour().getId() > 0 : false) {
                if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
                    champ = new String[]{"jour", "type"};
                    val = new Object[]{new YvsJoursOuvres(creneau.getJour().getId()), new YvsGrhTrancheHoraire(creneau.getTranche().getId())};
                    query = "YvsComCreneauDepot.findByTypeJour";
                } else {
                    champ = new String[]{"jour"};
                    val = new Object[]{new YvsJoursOuvres(creneau.getJour().getId())};
                    query = "YvsComCreneauDepot.findByJour";
                }
            } else {
                if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
                    champ = new String[]{"type"};
                    val = new Object[]{new YvsGrhTrancheHoraire(creneau.getTranche().getId())};
                    query = "YvsComCreneauDepot.findByType";
                }
            }
        }
        creneauxHist = dao.loadNameQueries(query, champ, val);

        if (c && !creneauxHist.isEmpty()) {
            creneau.setCritere_(creneauxHist.get(0).getCritere_());
            creneau.setCritere(creneau.getCritere_());
            loadAllTypeCreneau_();
            if (e) {
                getWarningMessage("Ce Créneau existe deja. vous passez en mode modification!");
                creneau.setId(creneauxHist.get(0).getId());
                setUpdateCrenau(true);
            } else {
                setUpdateCrenau(false);
            }
            update("select_critere");
            update("select_type_creneau");
        }

        update("data_creneau_hist");
    }

    public void loadAllTranche() {
        types = dao.loadNameQueries("YvsGrhTrancheHoraire.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllTypeCreneau() {
        types = dao.loadNameQueries("YvsGrhTrancheHoraire.findByCritere", new String[]{"societe", "critere"}, new Object[]{currentAgence.getSociete(), creneau.getCritere()});
        loadAllCritere();
    }

    public void loadAllTypeCreneau_() {
        types = dao.loadNameQueries("YvsGrhTrancheHoraire.findByCritere", new String[]{"societe", "critere"}, new Object[]{currentAgence.getSociete(), creneau.getCritere()});
    }

    public void loadAllCritere() {
        criteres.clear();
        for (YvsGrhTrancheHoraire bean : types) {
            boolean deja_ = false;
            if ((bean.getTypeJournee() != null) ? !bean.getTypeJournee().trim().equals("") : false) {
                for (String s : criteres) {
                    if (s.toLowerCase().equals(bean.getTypeJournee().toLowerCase())) {
                        deja_ = true;
                        break;
                    }
                }
                if (!deja_) {
                    criteres.add(bean.getTypeJournee());
                }
            }
        }
    }

    public void loadAllDepot() {
        depots.clear();
        List<YvsBaseDepots> l = dao.loadNameQueries("YvsBaseDepots.findByAgence", new String[]{"agence"}, new Object[]{currentAgence});
        depots = UtilCom.buildBeanListDepot(l);
    }

    public void loadAllJour() {
        jours.clear();
        YvsCalendrier y = (YvsCalendrier) dao.loadOneByNameQueries("YvsCalendrier.findByDefautSociete", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
        if ((y != null) ? y.getId() > 0 : false) {
            jours.addAll(y.getJoursOuvres());
        } else {
            getWarningMessage("Il n'existe pas de calendrier de la societe");
        }
    }

    @Override
    public Creneau recopieView() {
        Creneau c = new Creneau();
        c.setId(creneau.getId());
        c.setActif(creneau.isActif());
        c.setDepot(creneau.getDepot());
        c.setTranche(creneau.getTranche());
        c.setJour(creneau.getJour());
        c.setCritere(creneau.getCritere());
        c.setCritere_(creneau.getCritere_());
        c.setPermanent(creneau.isPermanent());
        c.setNew_(true);
        return c;
    }

    public TrancheHoraire recopieViewType() {
        TrancheHoraire t = new TrancheHoraire();
        t.setActif(true);
        t.setHeureDebut((type.getHeureDebut() != null) ? type.getHeureDebut() : new Date());
        t.setHeureFin((type.getHeureFin() != null) ? type.getHeureFin() : new Date());
        t.setId(type.getId());
        t.setTitre(type.getReference());
        t.setTypeJournee(type.getCritere());
        t.setNew_(true);
        return t;
    }

    @Override
    public boolean controleFiche(Creneau bean) {
        if ((bean.getDepot() != null) ? bean.getDepot().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le dépot");
            return false;
        }
        if ((bean.getJour() != null) ? bean.getJour().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le jour");
            return false;
        }
        if ((bean.getTranche() != null) ? bean.getTranche().getId() < 1 : true) {
            getErrorMessage("Vous devez specifier le type");
            return false;
        }
        if ((bean.getCritere_() != null) ? ((!bean.getCritere_().trim().equals("")) ? !bean.getCritere_().equals(bean.getCritere()) : false) : false) {
            getErrorMessage("Vous devez pas entrer un type dont le critére est different!");
            return false;
        }
        return true;
    }

    public boolean controleFicheType(TrancheHoraire bean) {
        if (bean.getTitre() == null || bean.getTitre().trim().equals("")) {
            getErrorMessage("Vous devez entrer la reference");
            return false;
        }
        if (bean.getHeureDebut() == null) {
            getErrorMessage("Vous devez entrer l'heure de debut");
            return false;
        }
        if (bean.getHeureFin() == null) {
            getErrorMessage("Vous devez entrer l'heure de fin");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Creneau bean) {
        cloneObject(creneau, bean);
        loadAllCreneauHist();
        setUpdateCrenau(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(creneau);
        creneau.setDepot(new Depots());
        creneau.setJour(new JoursOuvres());
        creneau.setTranche(new TrancheHoraire());
        tabIds = "";
        creneauxHist.clear();
        setUpdateCrenau(false);
        update("blog_form_creneau");
    }

    public void resetFicheType() {
        resetFiche(type);
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateCrenau() ? "Modification" : "Insertion";
        try {
            Creneau bean = recopieView();
            if (controleFiche(bean)) {
                YvsComCreneauDepot entity = UtilCom.buildCreneau(bean, currentUser, new YvsBaseDepots(bean.getDepot().getId()) );
                bean.setCritere(bean.getTranche().getTypeJournee());
                if (!isUpdateCrenau()) {
                    entity.setId(null);
                    entity = (YvsComCreneauDepot) dao.save1(entity);
                    creneau.setId(entity.getId());
                    creneaux.add(0, entity);
                    creneauxHist.add(0, entity);
                } else {
                    dao.update(entity);
                    creneaux.set(creneaux.indexOf(entity), entity);
                    if (creneauxHist.contains(entity)) {
                        creneauxHist.set(creneauxHist.indexOf(entity), entity);
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_creneau");
                update("blog_form_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewType() {
        try {
            TrancheHoraire bean = recopieViewType();
            if (controleFicheType(bean)) {
                YvsGrhTrancheHoraire entity = UtilGrh.buildTrancheHoraire(bean, currentUser);
                entity.setId(null);
                entity = (YvsGrhTrancheHoraire) dao.save1(entity);
                bean.setId(entity.getId());
                type.setId(entity.getId());
                creneau.setTranche(bean);
                types.add(0, entity);
                succes();
                loadAllCritere();
                resetFicheType();
                update("select_type_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            System.err.println("Error Insertion : " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                String[] tab = tabIds.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComCreneauDepot bean = creneaux.get(creneaux.indexOf(new YvsComCreneauDepot(id)));
                    dao.delete(new YvsComCreneauDepot(bean.getId()));
                    creneaux.remove(bean);
                    if (creneauxHist.contains(bean)) {
                        creneauxHist.remove(bean);
                    }
                }
                succes();
                update("data_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBean_(YvsComCreneauDepot y) {
        selectCreneaux = y;
    }

    public void deleteBean_() {
        try {
            if (selectCreneaux != null) {
                dao.delete(selectCreneaux);
                creneaux.remove(selectCreneaux);
                if (creneauxHist.contains(selectCreneaux)) {
                    creneauxHist.remove(selectCreneaux);
                }
                succes();
                update("data_creneau");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauDepot bean = (YvsComCreneauDepot) ev.getObject();
            populateView(UtilCom.buildBeanCreneau(bean));
            update("blog_form_creneau");
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComCreneauDepot bean = (YvsComCreneauDepot) ev.getObject();
            populateView(UtilCom.buildBeanCreneau(bean));
            update("form_creneau");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_creneau");
    }

    public void chooseDepot() {
        if ((creneau.getDepot() != null) ? creneau.getDepot().getId() > 0 : false) {
            Depots d = depots.get(depots.indexOf(creneau.getDepot()));
            cloneObject(creneau.getDepot(), d);
        }
        loadAllCreneauHist();
    }

    public void chooseType() {
        if ((creneau.getTranche() != null) ? creneau.getTranche().getId() > 0 : false) {
            YvsGrhTrancheHoraire d_ = types.get(types.indexOf(new YvsGrhTrancheHoraire(creneau.getTranche().getId())));
            TypeCreneau d = UtilCom.buildBeanTypeCrenau(d_);
            cloneObject(creneau.getTranche(), d);
        }
        loadAllCreneauHist();
    }

    public void chooseJour() {
        if ((creneau.getJour() != null) ? creneau.getJour().getId() > 0 : false) {
            YvsJoursOuvres d = jours.get(jours.indexOf(creneau.getJour()));
            cloneObject(creneau.getJour(), UtilGrh.buildBeanJoursOuvree(d));
        }
        loadAllCreneauHist();
    }

    public void chooseCritere() {
        if ((creneau.getCritere() != null) ? !creneau.getCritere().trim().equals("") : false) {
            creneau.getTranche().setId(0);
            loadAllTypeCreneau_();
        } else {
            loadAllTypeCreneau();
        }
    }

    public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for (String s : criteres) {
            if (s.toLowerCase().startsWith(query.toLowerCase())) {
                results.add(s);
            }
        }
        return results;
    }

    public void onCritereSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String d = (String) ev.getObject();
            type.setCritere(d);
        }
    }

    public void activeCreneau(YvsComCreneauDepot bean) {
        if (bean != null) {
            if (!bean.getActif() && !bean.getTranche().getActif()) {
                getErrorMessage("Vous ne pouvez pas activer ce créneau car son type est désactivé");
            } else {
                bean.setActif(!bean.getActif());
                String rq = "UPDATE yvs_com_creneau_depot SET actif=" + bean.getActif() + " WHERE id=?";
                Options[] param = new Options[]{new Options(bean.getId(), 1)};
                dao.requeteLibre(rq, param);
                creneaux.set(creneaux.indexOf(bean), bean);
            }
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
