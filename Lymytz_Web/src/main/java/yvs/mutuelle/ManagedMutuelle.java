/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.mutuelle.YvsMutDefaultUseFor;
import yvs.entity.mutuelle.YvsMutMutuelle;
import yvs.entity.mutuelle.base.YvsMutCompte;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.mutuelle.base.YvsMutTypeCompte;
import yvs.entity.param.YvsAgences;
import yvs.init.Initialisation;
//import yvs.mutuelle.base.ManagedTypeCaisse;
import yvs.mutuelle.base.TypeCompte;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedMutuelle extends Managed<Mutuelle, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{mutuelle}")
    private Mutuelle mutuelle;
    private YvsMutMutuelle mutuelleSelect;
    private List<YvsMutMutuelle> mutuelles;

    private CaisseMutuelle caisse = new CaisseMutuelle();
    private List<YvsMutCaisse> caisses;
    private List<YvsGrhEmployes> employes;

    private Date dateAdhesion = new Date();
    private String tabIds = "", input_reset = "", tabIds_caisse = "", input_reset_caisse = "";
    private boolean updateMutuelle, updateCaisse;
    private boolean caissEpargne, caissCredit, caissAssurance, caissAssistance;
    YvsMutMutuelle entityMutuelle;

    public ManagedMutuelle() {
        mutuelles = new ArrayList<>();
        employes = new ArrayList<>();
        caisses = new ArrayList<>();
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public String getTabIds_caisse() {
        return tabIds_caisse;
    }

    public void setTabIds_caisse(String tabIds_caisse) {
        this.tabIds_caisse = tabIds_caisse;
    }

    public String getInput_reset_caisse() {
        return input_reset_caisse != null ? input_reset_caisse : "";
    }

    public void setInput_reset_caisse(String input_reset_caisse) {
        this.input_reset_caisse = input_reset_caisse;
    }

    public boolean isUpdateCaisse() {
        return updateCaisse;
    }

    public void setUpdateCaisse(boolean updateCaisse) {
        this.updateCaisse = updateCaisse;
    }

    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    public YvsMutMutuelle getMutuelleSelect() {
        return mutuelleSelect;
    }

    public void setMutuelleSelect(YvsMutMutuelle mutuelleSelect) {
        this.mutuelleSelect = mutuelleSelect;
    }

    public Date getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(Date dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset != null ? input_reset : "";
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateMutuelle() {
        return updateMutuelle;
    }

    public void setUpdateMutuelle(boolean updateMutuelle) {
        this.updateMutuelle = updateMutuelle;
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public List<YvsMutMutuelle> getMutuelles() {
        return mutuelles;
    }

    public void setMutuelles(List<YvsMutMutuelle> mutuelles) {
        this.mutuelles = mutuelles;
    }

    public CaisseMutuelle getCaisse() {
        return caisse;
    }

    public void setCaisse(CaisseMutuelle caisse) {
        this.caisse = caisse;
    }

    public boolean isCaissEpargne() {
        return caissEpargne;
    }

    public void setCaissEpargne(boolean caissEpargne) {
        this.caissEpargne = caissEpargne;
    }

    public boolean isCaissCredit() {
        return caissCredit;
    }

    public void setCaissCredit(boolean caissCredit) {
        this.caissCredit = caissCredit;
    }

    public boolean isCaissAssurance() {
        return caissAssurance;
    }

    public void setCaissAssurance(boolean caissAssurance) {
        this.caissAssurance = caissAssurance;
    }

    public boolean isCaissAssistance() {
        return caissAssistance;
    }

    public void setCaissAssistance(boolean caissAssistance) {
        this.caissAssistance = caissAssistance;
    }

    @Override
    public void loadAll() {
        loadAllMutuelle();
        getAgenceActuel();
        tabIds = "";
        input_reset = "";
        tabIds_caisse = "";
        input_reset_caisse = "";
    }

    public void getAgenceActuel() {
        if ((currentMutuel != null) ? currentMutuel.getId() > 0 : false) {
            setUpdateMutuelle(true);
        }
    }

    public void loadAllMutuelle() {
        mutuelles = dao.loadNameQueries("YvsMutMutuelle.findAll", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
    }

    public void loadAllEmploye() {
        if ((mutuelleSelect != null) ? (mutuelleSelect.getId() > 0) : false) {
            entityMutuelle = mutuelleSelect;
            employes = dao.loadNameQueries("YvsGrhEmployes.findByMutuelle", new String[]{"mutuelle"}, new Object[]{new YvsMutMutuelle(mutuelleSelect.getId())});
        }
    }

    public void loadCaisse(String type) {
        if (type != null ? type.trim().length() > 0 : false) {
            nameQueri = "YvsMutDefaultUseFor.findCaisseByType";
            champ = new String[]{"mutuelle", "type"};
            val = new Object[]{currentMutuel, type};
        } else {
            nameQueri = "YvsMutCaisse.findByMutuelle";
            champ = new String[]{"mutuelle"};
            val = new Object[]{currentMutuel};
        }
        caisses = dao.loadNameQueries(nameQueri, champ, val);
    }

    public YvsMutMutuelle buildMutuelle(Mutuelle y) {
        YvsMutMutuelle m = new YvsMutMutuelle();
        if (y != null) {
            m.setDateCreation((y.getDateCreation() != null) ? y.getDateCreation() : new Date());
            m.setDesignation(y.getDesignation());
            m.setCode(y.getCode());
            m.setId(y.getId());
            m.setMontantEpargne(y.getMontantEpargne());
            m.setMontantInscription(y.getMontantInscription());
            m.setMontantAssurance(y.getMontantAssurance());
            m.setDateSave(y.getDateSave());
            m.setDateUpdate(new Date());
            m.setLogo(y.getLogo());
            m.setSociete(currentAgence.getSociete());
            m.setAuthor(currentUser);
        }
        return m;
    }

    @Override
    public Mutuelle recopieView() {
        Mutuelle m = new Mutuelle();
        m.setDateCreation((mutuelle.getDateCreation() != null) ? mutuelle.getDateCreation() : new Date());
        m.setDesignation(mutuelle.getDesignation());
        m.setId(mutuelle.getId());
        m.setCode(mutuelle.getCode());
        m.setLogo(mutuelle.getLogo());
        m.setMontantEpargne(mutuelle.getMontantEpargne());
        m.setMontantAssurance(mutuelle.getMontantAssurance());
        m.setMontantInscription(mutuelle.getMontantInscription());
        return m;
    }

    @Override
    public boolean controleFiche(Mutuelle bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        return true;
    }

    @Override
    public boolean saveNew() {
        if (getInput_reset().equals("reset")) {
            setUpdateMutuelle(false);
            input_reset = "";
        }
        String action = isUpdateMutuelle() ? "Modification" : "Insertion";
        try {
            Mutuelle bean = recopieView();
            if (controleFiche(bean)) {
                entityMutuelle = buildMutuelle(bean);
                if (!isUpdateMutuelle()) {
                    entityMutuelle.setDateUpdate(new Date());
                    entityMutuelle.setDateSave(new Date());
                    entityMutuelle.setId(null);
                    entityMutuelle = (YvsMutMutuelle) dao.save1(entityMutuelle);
                    bean.setId(entityMutuelle.getId());
                    mutuelle.setId(entityMutuelle.getId());
                    mutuelles.add(entityMutuelle);
                } else {
                    entityMutuelle.setDateUpdate(new Date());
                    dao.update(entityMutuelle);
                    mutuelles.set(mutuelles.indexOf(entityMutuelle), entityMutuelle);
                }
                initialiseCaisseInteret();
                setUpdateMutuelle(true);
                if (Initialisation.INITIALISATION) {
                    currentMutuel = entityMutuelle;
                    if (currentAgence.getMutuelle() == null) {
                        currentAgence.setMutuelle(new YvsMutMutuelle());
                    }
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mutuel", entityMutuelle);
                    Initialisation.INITIALISATION = false;
                }
                update("data_mutuelle");
                succes();
                actionOpenOrResetAfter(this);
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void resetFiche() {
        resetFiche(mutuelle);
        mutuelle.setAgences(new ArrayList<YvsAgences>());
        mutuelle.setCaisses(new ArrayList<YvsMutCaisse>());
        setUpdateMutuelle(false);

        tabIds = "";
        input_reset = "";
        resetFicheCaisse();
        update("blog_form_mutuelle");
    }

    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdateMutuelle((ids != null) ? ids.length > 0 : false);
            if (isUpdateMutuelle()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutMutuelle bean = mutuelles.get(mutuelles.indexOf(new YvsMutMutuelle(id)));
                populateView(UtilMut.buildBeanMutuelle(bean));

                tabIds = "";
                input_reset = "";
                update("blog_form_mutuelle");
            }
        }
    }

    @Override
    public void populateView(Mutuelle bean) {
        cloneObject(mutuelle, bean);
        setUpdateMutuelle(true);
    }

    public void openTodeleteBean(YvsMutMutuelle y) {
        if (y != null ? y.getId() > 0 : false) {
            int index = mutuelles.indexOf(y);
            if (index > 1) {
                tabIds = "" + index;
            }

        }
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsMutMutuelle> list = new ArrayList<>();
                YvsMutMutuelle bean;
                for (Long ids : l) {
                    bean = mutuelles.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                mutuelles.removeAll(list);
                tabIds = "";
                resetFiche();
                succes();
                update("data_mutuelle");

            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    public void deleteBean(YvsMutMutuelle y) {
        try {
            if (y != null) {
                dao.delete(y);
                mutuelles.remove(y);

                resetFiche();
                succes();
                update("data_mutuelle");
                update("blog_form_mutuelle");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Lymytz Error...", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            entityMutuelle = (YvsMutMutuelle) ev.getObject();
            populateView(UtilMut.buildBeanMutuelle(entityMutuelle));
            tabIds = mutuelles.indexOf(entityMutuelle) + "";
            update("blog_form_mutuelle");
        }
    }

    public void getLogo() {

        update("logo_mut_");
    }

    public void initialiseMutualiste() {
        loadAllEmploye();
        if ((employes != null) ? !employes.isEmpty() : false) {
            Mutualiste m;
            for (YvsGrhEmployes e : employes) {
                if (e.getMutualiste() == null) {
                    m = new Mutualiste();
                    m.setDateAdhesion(dateAdhesion);
                    m.setEmploye(UtilMut.buildBeanEmploye(e));
                    YvsMutMutualiste en = saveNewMutualiste(m);
                    saveAllCompteMutualiste(en);
                }
            }
            succes();
        } else {
            getInfoMessage("la synchronisation est a jour");
        }
    }

    public YvsMutMutualiste buildMutualiste(Mutualiste y) {
        YvsMutMutualiste m = new YvsMutMutualiste();
        if (y != null) {
            m.setId(y.getId());
            m.setDateAdhesion((y.getDateAdhesion() != null) ? y.getDateAdhesion() : new Date());
            if ((y.getEmploye() != null) ? y.getEmploye().getId() != 0 : false) {
                m.setEmploye(new YvsGrhEmployes(y.getEmploye().getId()));
            }
            m.setMutuelle(entityMutuelle);
        }
        return m;
    }

    public YvsMutMutualiste saveNewMutualiste(Mutualiste bean) {
        try {
            YvsMutMutualiste entity = buildMutualiste(bean);
            entity.setId(null);
            entity = (YvsMutMutualiste) dao.save1(entity);
            entity.setAssistance(mutuelle.getParametres().getSouscriptionGeneral());
            bean.setId(entity.getId());
            return entity;
        } catch (Exception ex) {
            System.err.println("Error initialisation : " + ex.getMessage());
            getErrorMessage("Initialisation Impossible !");
            return null;
        }
    }

    public Compte recopieViewCompte(TypeCompte type) {
        Compte c = new Compte();
        c.setId(0);
        c.setReference("Compte " + type.getNature());
        c.setSolde(0);
        c.setType(type);
        return c;
    }

    public YvsMutCompte buildCompte(Compte y, YvsMutMutualiste ent) {
        YvsMutCompte c = new YvsMutCompte();
        if (y != null) {
            c.setId(y.getId());
            c.setReference(y.getReference());
            c.setSolde(y.getSolde());
            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
                c.setTypeCompte(new YvsMutTypeCompte(y.getType().getId()));
            }
            c.setMutualiste(ent);
        }
        return c;
    }

    public void saveAllCompteMutualiste(YvsMutMutualiste en) {
        String query = "YvsMutTypeCompte.findByType";
        String[] ch = new String[]{"type", "mutuelle"};
        Object[] v = new Object[]{"Epargne", currentMutuel};
        List<YvsMutTypeCompte> l = dao.loadNameQueries(query, ch, v);
        if ((l != null) ? !l.isEmpty() : false) {
            YvsMutTypeCompte y = l.get(0);
            if ((y != null) ? y.getId() > 0 : false) {
                dao.save(buildCompte(recopieViewCompte(UtilMut.buildBeanTypeCompte(y)), en));
            }
        }
//        v = new Object[]{"Credit", currentMutuel};
//        l = dao.loadNameQueries(query, ch, v);
//        if ((l != null) ? !l.isEmpty() : false) {
//            YvsMutTypeCompte y = l.get(0);
//            if ((y != null) ? y.getId() > 0 : false) {
//                dao.save(buildCompte(recopieViewCompte(UtilMut.buildBeanTypeCompte(y)), en));
//            }
//        }
        v = new Object[]{"Courant", currentMutuel};
        l = dao.loadNameQueries(query, ch, v);
        if ((l != null) ? !l.isEmpty() : false) {
            YvsMutTypeCompte y = l.get(0);
            if ((y != null) ? y.getId() > 0 : false) {
                dao.save(buildCompte(recopieViewCompte(UtilMut.buildBeanTypeCompte(y)), en));
            }
        }
//        v = new Object[]{"Interet", currentMutuel};
//        l = dao.loadNameQueries(query, ch, v);
//        if ((l != null) ? !l.isEmpty() : false) {
//            YvsMutTypeCompte y = l.get(0);
//            if ((y != null) ? y.getId() > 0 : false) {
//                dao.save(buildCompte(recopieViewCompte(UtilMut.buildBeanTypeCompte(y)), en));
//            }
//        }
    }

    public YvsMutTypeCompte buildTypeCaisse(TypeCompte y) {
        YvsMutTypeCompte t = new YvsMutTypeCompte();
        if (y != null) {
            t.setId(y.getId());
            t.setLibelle(y.getLibelle());
            t.setNature(y.getNature());
            if ((y.getMutuelle() != null) ? y.getMutuelle().getId() != 0 : false) {
                t.setMutuelle(new YvsMutMutuelle(y.getMutuelle().getId()));
            }
            t.setTypeCaisse(true);
        }
        return t;
    }

    public boolean controlFicheTypeCaisse(TypeCompte bean) {
        if (bean.getLibelle() == null || bean.getLibelle().equals("")) {
            getErrorMessage("Vous devez entrer le libelle !");
            return false;
        }
        return true;
    }

    public YvsMutCaisse buildCaisse(CaisseMutuelle y) {
        YvsMutCaisse c = new YvsMutCaisse();
        if (y != null) {
            c.setId(y.getId());
            c.setReferenceCaisse(y.getReference());
            c.setSolde(y.getSolde());
            c.setResponsable((y.getId() <= 0) ? null : UtilMut.buildMutualiste(y.getResponsable(), currentUser));
            c.setMutuelle(entityMutuelle);
            c.setAuthor(currentUser);
            c.setActif(y.isActif());
            c.setPrincipal(y.isPrincipale());
            c.setNew_(true);
            c.setDateSave(y.getDateSave());
            c.setDateUpdate(new Date());
        }
        return c;
    }

    public CaisseMutuelle recopieViewCaisse() {
        CaisseMutuelle c = new CaisseMutuelle();
        c.setId(caisse.getId());
        c.setReference(caisse.getReference());
        c.setSolde(caisse.getSolde());
        c.setResponsable(caisse.getResponsable());
        c.setNew_(true);
        c.setActif(caisse.isActif());
        c.setPrincipale(caisse.isPrincipale());
        c.setDateSave(caisse.getDateSave());
        return c;
    }

    public boolean controleFicheCaisse(CaisseMutuelle bean) {
        if (bean.getReference() == null) {
            getErrorMessage("Vous devez indiquer le code référence de cette caisse!");
            return false;
        }
        return true;
    }

    public void resetFicheCaisse() {
        resetFiche(caisse);
        caisse.setId(0);
        caisse.setReference("");
        caisse.setSelectActif(false);
        caisse.setSolde(0);
        caisse.setActif(true);
        caisse.setResponsable(new Mutualiste());
        setUpdateCaisse(false);
        tabIds_caisse = "";
        input_reset_caisse = "";
        update("blog_form_caisse_mutuelle");
    }

    public void populateViewCaisse(CaisseMutuelle bean) {
        cloneObject(caisse, bean);
        setUpdateCaisse(true);
        //calcul du solde
        caisse.setSolde(dao.getSoldeCaisseMut(bean.getId(), null, Constantes.STATUT_DOC_PAYER));
    }

    public void saveNewCaisse() {
        if (mutuelle.getId() > 0) {
            String action = caisse.getId() > 0 ? "Modification" : "Insertion";
            try {
                if (controleFicheCaisse(caisse)) {
                    YvsMutCaisse entity = buildCaisse(caisse);
                    entity.setDateUpdate(new Date());
                    entity.setAuthor(currentUser);
                    if (caisse.getId() <= 0) {
                        entity.setId(null);
                        entity.setDateSave(new Date());
                        entity = (YvsMutCaisse) dao.save1(entity);
                        caisse.setId(entity.getId());
                        mutuelle.getCaisses().add(0, entity);
                    } else {
                        dao.update(entity);
                        int idx = mutuelle.getCaisses().indexOf(entity);
                        if (idx >= 0) {
                            mutuelle.getCaisses().set(idx, entity);
                        }
                    }
                    //Enregistre les caisse par défaut
                    saveDefaultCaisse(entity);
                    mutuelle.getCaisses().get(mutuelle.getCaisses().indexOf(entity)).setDefaultActivites(caisse.getDefaultActivites());
                    succes();
                    resetFicheCaisse();
                    update("data_caisse_mutuelle");
                }
            } catch (Exception ex) {
                getException("Error " + action + " : " + ex.getMessage(), ex);
                getErrorMessage(action + " Impossible !");
            }
        } else {
            getErrorMessage("Vous devez dabord enregsitrer le mutuelle");
        }
    }

    public void saveDefaultCaisse(YvsMutCaisse caiss) {
        if (caiss.getId() > 0) {
            YvsMutDefaultUseFor u = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActivite", new String[]{"caisse", "activite"}, new Object[]{caiss, Constantes.MUT_NATURE_OP_EPARGNE});
            if (caissEpargne) {
                if (u == null) {
                    u = new YvsMutDefaultUseFor();
                    u.setActivite(Constantes.MUT_NATURE_OP_EPARGNE);
                    u.setAuthor(currentUser);
                    u.setCaisse(caiss);
                    u.setDateSave(new Date());
                    u.setDateUpdate(new Date());
                    u.setId(null);
                    dao.save(u);
                    caisse.getDefaultActivites().add(u);
                }
            } else {
                if (u != null) {
                    dao.delete(u);
                    caisse.getDefaultActivites().remove(u);
                }
            }
            u = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActivite", new String[]{"caisse", "activite"}, new Object[]{caiss, Constantes.MUT_ACTIVITE_CREDIT});
            if (caissCredit) {
                if (u == null) {
                    u = new YvsMutDefaultUseFor();
                    u.setActivite(Constantes.MUT_ACTIVITE_CREDIT);
                    u.setAuthor(currentUser);
                    u.setCaisse(caiss);
                    u.setDateSave(new Date());
                    u.setDateUpdate(new Date());
                    u.setId(null);
                    dao.save(u);
                    caisse.getDefaultActivites().add(u);
                }
            } else {
                if (u != null) {
                    dao.delete(u);
                    caisse.getDefaultActivites().remove(u);
                }
            }
            u = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActivite", new String[]{"caisse", "activite"}, new Object[]{caiss, Constantes.MUT_NATURE_OP_ASSISTANCE});
            if (caissAssistance) {
                if (u == null) {
                    u = new YvsMutDefaultUseFor();
                    u.setActivite(Constantes.MUT_NATURE_OP_ASSISTANCE);
                    u.setAuthor(currentUser);
                    u.setCaisse(caiss);
                    u.setDateSave(new Date());
                    u.setDateUpdate(new Date());
                    u.setId(null);
                    dao.save(u);
                    caisse.getDefaultActivites().add(u);
                } else {
                    if (u != null) {
                        dao.delete(u);
                        caisse.getDefaultActivites().remove(u);
                    }
                }
            }

            u = (YvsMutDefaultUseFor) dao.loadOneByNameQueries("YvsMutDefaultUseFor.findByActivite", new String[]{"caisse", "activite"}, new Object[]{caiss, Constantes.MUT_NATURE_OP_ASSURANCE});
            if (caissAssurance) {
                if (u == null) {
                    u = new YvsMutDefaultUseFor();
                    u.setActivite(Constantes.MUT_NATURE_OP_ASSURANCE);
                    u.setAuthor(currentUser);
                    u.setCaisse(caiss);
                    u.setDateSave(new Date());
                    u.setDateUpdate(new Date());
                    u.setId(null);
                    dao.save(u);
                    caisse.getDefaultActivites().add(u);
                } else {
                    if (u != null) {
                        dao.delete(u);
                        caisse.getDefaultActivites().remove(u);
                    }
                }
            }
        } else {
            getErrorMessage("Aucune caisse n'a été selectionné !");
        }
    }

    public void deleteBeanCaisse() {
        try {
            if ((tabIds_caisse != null) ? tabIds_caisse.length() > 0 : false) {
                String[] ids = tabIds_caisse.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutCaisse bean = mutuelle.getCaisses().get(mutuelle.getCaisses().indexOf(new YvsMutCaisse(id)));
                        try {
                            dao.delete(new YvsMutCaisse(bean.getId()));
                            mutuelle.getCaisses().remove(bean);
                        } catch (Exception e) {
                            getErrorMessage("Suppression Impossible !");
                            getException("Mutuelle Error...", e);
                        } finally {
                            resetFicheCaisse();
                        }
                    }
                    succes();
                    update("data_caisse_mutuelle");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mutuelle Error...", ex);
        } finally {
            resetFicheCaisse();
        }
    }

    public void openCaisseToDel(YvsMutCaisse y) {
        populateViewCaisse(UtilMut.buildBeanCaisse(y));
        openDialog("dlgConfirmDelCaisse");
    }

    public void deleteOneBeanCaisse() {
        try {
            if (caisse != null ? caisse.getId() > 0 : false) {
                YvsMutCaisse c = new YvsMutCaisse(caisse.getId());
                dao.delete(c);
                mutuelle.getCaisses().remove(c);
                resetFicheCaisse();
                succes();
                update("data_caisse_mutuelle");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Mutuelle Error...", ex);
        }
    }

    public void searchMutualiste(String matricule) {
        ManagedMutualiste service = (ManagedMutualiste) giveManagedBean(ManagedMutualiste.class);
        if (service != null && (matricule != null) ? !matricule.isEmpty() : false) {
            Mutualiste m = service.searchMutualiste(matricule, true);
            selectOneMutualiste(m);
        }
    }

    public void selectMutualiste(SelectEvent ev) {
        selectOneMutualiste(UtilMut.buildSimpleBeanMutualiste((YvsMutMutualiste) ev.getObject()));
    }

    private void selectOneMutualiste(Mutualiste m) {
        caisse.setResponsable(m);
        update("txt_search_mutualiste");
    }

    public void updateBeanCaisse() {
        tabIds_caisse = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tabIds");
        if ((tabIds_caisse != null) ? tabIds_caisse.length() > 0 : false) {
            String[] ids = tabIds_caisse.split("-");
            setUpdateCaisse((ids != null) ? ids.length > 0 : false);
            if (isUpdateCaisse()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutCaisse bean = mutuelle.getCaisses().get(mutuelle.getCaisses().indexOf(new YvsMutCaisse(id)));
                populateViewCaisse(UtilMut.buildBeanCaisse(bean));
            } else {
                resetFicheCaisse();
            }
        } else {
            resetFicheCaisse();
        }
        update("blog_form_caisse");
    }

    public void loadOnViewCaisse(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsMutCaisse y = (YvsMutCaisse) ev.getObject();
            populateViewCaisse(UtilMut.buildBeanCaisse(y));
            //active les caisses par defaut
            caissAssistance = caissAssurance = caissCredit = caissEpargne = false;
            for (YvsMutDefaultUseFor cd : y.getDefaultActivites()) {
                switch (cd.getActivite()) {
                    case Constantes.MUT_NATURE_OP_EPARGNE:
                        caissEpargne = true;
                        break;
                    case Constantes.MUT_ACTIVITE_CREDIT:
                        caissCredit = true;
                        break;
                    case Constantes.MUT_NATURE_OP_ASSISTANCE:
                        caissAssistance = true;
                        break;
                    case Constantes.MUT_NATURE_OP_ASSURANCE:
                        caissAssurance = true;
                        break;
                }
            }
            update("blog_form_caisse_mutuelle");
        }
    }

    public void unLoadOnViewCaisse(SelectEvent ev) {
        resetFicheCaisse();
        update("blog_form_caisse_mutuelle");
    }

    public void existExerice(Date date) {
        boolean exist = getExerciceOnDate(date);
        if (!exist) {
            openDialog("dlgConfirmCreate");
        }
    }

    private boolean getExerciceOnDate(Date date) {
        champ = new String[]{"societe", "dateJour"};
        val = new Object[]{currentAgence.getSociete(), date};
        YvsBaseExercice exo = (YvsBaseExercice) dao.loadOneByNameQueries("YvsBaseExercice.findActifByDate", champ, val);
        if ((exo != null) ? exo.getId() != 0 : false) {
            return true;
        }
        return false;
    }

//    private void existeMutuelle() {
//        if ((currentMutuel != null) ? currentMutuel.getId() == 0 : true) {
//            Initialisation.INITIALISATION = true;
//            try {
//                FacesContext.getCurrentInstance().getExternalContext().redirect("/LYMYTZ_MUTUELLE/pages/parametre/yvs_mutuelle.xhtml");
//                Initialisation.INITIALISATION = false;
//            } catch (IOException ex) {
//                Logger.getLogger(Initialisation.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            Initialisation.INITIALISATION = false;
//        }
//    }
//
//    private long synchroniseEmploye() {
//        String requete = "select public.synchronise_employe_mutuelle()";
//        Options param[] = new Options[]{};
//        return (Long) dao.callFonction1(requete, param);
//    }
//
//    private int synchroniseEmploye_() {
//        int cpt = 0;
//        List<YvsGrhEmployes> lE = dao.loadNameQueries("YvsGrhEmployes.findAlls", new String[]{"societe"}, new Object[]{currentAgence.getSociete()});
//        for (YvsGrhEmployes e : lE) {
//
//        }
//        return cpt;
//    }
    public void joindToAgence() {
        if ((mutuelleSelect != null) ? mutuelleSelect.getId() > 0 : false) {
            String rq = "UPDATE yvs_agences SET mutuelle=" + mutuelleSelect.getId() + " WHERE id=?";
            Options[] param = new Options[]{new Options(currentAgence.getId(), 1)};
            dao.requeteLibre(rq, param);
            for (YvsAgences ag : mutuelleSelect.getAgences()) {
                if (ag.equals(currentAgence)) {
                    getInfoMessage("Cet liason existe déjà !");
                } else {
                    mutuelleSelect.getAgences().add(currentAgence);
                }

            }
            currentMutuel = mutuelleSelect;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mutuel", currentMutuel);
            succes();
        }
    }

    public YvsMutTypeCompte searchTypeCaisseInteret() {
////        ManagedTypeCaisse s = (ManagedTypeCaisse) giveManagedBean(ManagedTypeCaisse.class);
////        if (s != null) {
////            for (YvsMutTypeCompte c : s.getTypes()) {
////                if (c.getNature().equals("Interet")) {
////                    return c;
////                }
////            }
////            TypeCaisse c = new TypeCaisse();
////            c.setLibelle("Interet");
////            c.setNature("Interet");
////            c.setMutuelle(mutuelle);
////            c.setTypeCaisse(true);
////
////            YvsMutTypeCompte en = buildTypeCaisse(c);
////            en = (YvsMutTypeCompte) dao.save1(en);
////            c.setId(en.getId());
////            s.getTypes().add(en);
//            return en;
//        }
        return null;
    }

    public void initialiseCaisseInteret() {
        boolean trouv = false;
//        for (YvsMutCaisse c : mutuelle.getCaisses()) {
//            if (c.getType().getNature().equals("Interet")) {
//                trouv = true;
//                break;
//            }
//        }
//        if (!trouv) {
//            CaisseMutuelle c = new CaisseMutuelle();
//            c.setReference("CPT Interet");
//            c.setSolde(0);
//            TypeCaisse t = UtilMut.buildBeanTypeCaisse(searchTypeCaisseInteret());
//            c.setType(t);
//
//            YvsMutCaisse en = buildCaisse(c);
//            en = (YvsMutCaisse) dao.save1(en);
//            c.setId(en.getId());
//            mutuelle.getCaisses().add(en);
//        }
//        update("data_caisse_mutuelle");
    }

//    public void chooseTypeCaisse() {
//        if (caisse.getType() != null ? caisse.getType().getId() > 0 : false) {
//            ManagedTypeCaisse s = (ManagedTypeCaisse) giveManagedBean(ManagedTypeCaisse.class);
//            if (s != null) {
//                int idx = s.getTypes().indexOf(new YvsMutTypeCompte(caisse.getType().getId()));
//                if (idx > -1) {
//                    YvsMutTypeCompte t = s.getTypes().get(idx);
//                    cloneObject(caisse.getType(), UtilMut.buildBeanTypeCaisse(t));
//                }
//            }
//        } else {
//            if (caisse.getType() != null ? caisse.getType().getId() < 0 : true) {
//                openDialog("dlgAddTypeCaisse");
//            }
//        }
//    }
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleUpload(FileUploadEvent ev) {
        String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminDocMut().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminDocMut().length());
        //répertoire destination de sauvegarge= C:\\lymytz\scte...
        String repDestSVG = Initialisation.getCheminDocMut();
        String file = Util.giveFileName() + "." + Util.getExtension(ev.getFile().getFileName());
        try {
            //copie dans le dossier de l'application
            Util.copyFile(file, repDest + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
            //copie dans le dossier de sauvegarde
            Util.copySVGFile(file, repDestSVG + "" + Initialisation.FILE_SEPARATOR, ev.getFile().getInputstream());
            File f = new File(new StringBuilder(repDest).append(file).toString());
            if (!f.exists()) {
                File doc = new File(repDest);
                if (!doc.exists()) {
                    doc.mkdirs();
                    f.createNewFile();
                } else {
                    f.createNewFile();
                }
            }
            mutuelle.setLogo(file);
            getInfoMessage("Logo chargé");
            update("logo_mut");

        } catch (IOException ex) {
            Logger.getLogger(ManagedMutuelle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
