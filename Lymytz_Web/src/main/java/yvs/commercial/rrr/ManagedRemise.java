/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.rrr;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.UtilCom;
import yvs.dao.Options;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.util.Constantes;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedRemise extends Managed<Remise, YvsBaseCaisse> implements Serializable {

    private Remise remise = new Remise();
    private YvsComRemise selectRemise;
    private List<YvsComRemise> remises;
//    private List<String> referencesRemise;

    private List<YvsComGrilleRemise> grilles;
    private YvsComGrilleRemise selectGrille;
    private GrilleRabais rabais = new GrilleRabais();

    private String tabIds, tabIds_grille;

    public ManagedRemise() {
        remises = new ArrayList<>();
        grilles = new ArrayList<>();
    }

    public YvsComRemise getSelectRemise() {
        return selectRemise;
    }

    public void setSelectRemise(YvsComRemise selectRemise) {
        this.selectRemise = selectRemise;
    }

    public List<YvsComGrilleRemise> getGrilles() {
        return grilles;
    }

    public void setGrilles(List<YvsComGrilleRemise> grilles) {
        this.grilles = grilles;
    }

    public YvsComGrilleRemise getSelectGrille() {
        return selectGrille;
    }

    public void setSelectGrille(YvsComGrilleRemise selectGrille) {
        this.selectGrille = selectGrille;
    }

    public String getTabIds_grille() {
        return tabIds_grille;
    }

    public void setTabIds_grille(String tabIds_grille) {
        this.tabIds_grille = tabIds_grille;
    }

    public GrilleRabais getRabais() {
        return rabais;
    }

    public void setRabais(GrilleRabais rabais) {
        this.rabais = rabais;
    }

    public Remise getRemise() {
        return remise;
    }

    public void setRemise(Remise planRemise) {
        this.remise = planRemise;
    }

    public List<YvsComRemise> getRemises() {
        return remises;
    }

    public void setRemises(List<YvsComRemise> remises) {
        this.remises = remises;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    @Override
    public void loadAll() {
        loadAllRemise();
    }

    public void loadAllRemise() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        remises = dao.loadNameQueries("YvsComRemise.findAll", champ, val);
    }

    public void loadAllRemiseActif(boolean actif) {
        remises = dao.loadNameQueries("YvsComRemise.findByActif", new String[]{"societe", "actif"}, new Object[]{currentAgence.getSociete(), actif});
    }

    public void loadAllgrille(YvsComRemise y) {
        champ = new String[]{"remise"};
        val = new Object[]{y};
        grilles = dao.loadNameQueries("YvsComGrilleRemise.findByRemise", champ, val);
        update("data_grille_remise");
    }

    public YvsComRemise buildRemise(Remise y) {
        YvsComRemise r = new YvsComRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setRefRemise(y.getReference());
            r.setActif(y.isActif());
            r.setPermanent(y.isPermanent());
            r.setDateDebut((y.getDateDebut() != null) ? y.getDateDebut() : new Date());
            r.setDateFin((y.getDateFin() != null) ? y.getDateFin() : new Date());
            r.setSociete(currentAgence.getSociete());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
            r.setDescription(y.getDescription());
            r.setNew_(true);
        }
        return r;
    }

    public YvsComGrilleRemise buildGrilleRemise(GrilleRabais y) {
        YvsComGrilleRemise r = new YvsComGrilleRemise();
        if (y != null) {
            r.setId(y.getId());
            r.setMontantMaximal(y.getMontantMaximal());
            r.setMontantMinimal(y.getMontantMinimal());
            r.setMontantRemise(y.getMontantRabais());
            r.setBase((y.getBase() != null) ? y.getBase() : Constantes.BASE_CA);
            r.setNatureMontant((y.getNatureMontant() != null) ? y.getNatureMontant() : Constantes.NATURE_MTANT);
            r.setRemise(selectRemise);
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                r.setAuthor(currentUser);
            }
        }
        return r;
    }

    @Override
    public Remise recopieView() {
        remise.setNew_(true);
        return remise;
    }

    public GrilleRabais recopieViewGrille() {
        if (rabais.isUnique()) {
            rabais.setMontantMinimal(0);
            rabais.setMontantMaximal(Double.MAX_VALUE);
        }
        rabais.setNew_(true);
        return rabais;
    }

    @Override
    public boolean controleFiche(Remise bean) {
        if (bean.getReference() == null || bean.getReference().trim().equals("")) {
            getErrorMessage("vous devez entrer la reference");
            return false;
        }
        return true;
    }

    public boolean controleFicheGrille(GrilleRabais bean) {
        if (!remise.isUpdate()) {
            saveNew();
        }
        if (bean.getMontantMaximal() > 0 ? bean.getMontantMaximal() < bean.getMontantMinimal() : false) {
            getErrorMessage("le montant minimal ne peut pas etre superieur au montant maximal");
            return false;
        }
        //vérifions  l'ajoout de montant max deux fois
        for (YvsComGrilleRemise g : grilles) {
            if (g.getId() != bean.getId()) {
                if (g.getMontantMaximal() >= Constantes.MAX_DOUBLE) {
                    getErrorMessage("Cette tranche est déjà couverte par la tranche " + g.getMontantMinimal() + " - " + g.getMontantMaximal());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void populateView(Remise bean) {
        cloneObject(remise, bean);
        selectRemise = buildRemise(bean);
    }

    public void populateViewGrille(GrilleRabais bean) {
        cloneObject(rabais, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche_();
        update("blog_form_remise");
    }

    public void resetFiche_() {
        resetFiche(remise);
        grilles.clear();
        tabIds = "";
        selectRemise = new YvsComRemise();
        resetFicheGrille();
    }

    public void resetFicheGrille() {
        resetFiche(rabais);
        rabais.setBase(Constantes.BASE_QTE);
        rabais.setNatureMontant(Constantes.NATURE_TAUX);
        selectGrille = new YvsComGrilleRemise();
        tabIds_grille = "";
    }

    @Override
    public boolean saveNew() {
        String action = remise.isUpdate() ? "Modification" : "Insertion";
        try {
            Remise bean = recopieView();
            if (controleFiche(bean)) {
                selectRemise = buildRemise(bean);
                selectRemise.setDateUpdate(new Date());
                if (!bean.isUpdate()) {
                    selectRemise.setId(null);
                    selectRemise.setDateSave(new Date());
                    selectRemise = (YvsComRemise) dao.save1(selectRemise);
                    remise.setId(selectRemise.getId());
                }

                if (bean.getCodeAcces() != null ? bean.getCodeAcces().trim().length() > 0 : false) {
                    selectRemise.setCodeAcces(returnCodeAcces(bean.getCodeAcces()));
                } else {
                    selectRemise.setCodeAcces(null);
                }
                dao.update(selectRemise);

                int idx = remises.indexOf(selectRemise);
                if (idx > -1) {
                    remises.set(idx, selectRemise);
                } else {
                    remises.add(0, selectRemise);
                }

                succes();
                remise.setUpdate(true);
                actionOpenOrResetAfter(this);
                update("data_remise");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewAll() {
        try {
            Remise bean = recopieView();
            if (controleFiche(bean)) {
                YvsComRemise entity = buildRemise(bean);
                entity.setDateUpdate(new Date());
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity.setDateSave(new Date());
                    entity = (YvsComRemise) dao.save1(entity);
                    bean.setId(entity.getId());
                    ManagedRemise m = (ManagedRemise) giveManagedBean(ManagedRemise.class);
                    if (m != null) {
                        m.getRemises().add(0, entity);
                    }
                }
                bean.setUpdate(true);

                GrilleRabais g = recopieViewGrille();
                YvsComGrilleRemise en = buildGrilleRemise(g);
                en.setRemise(entity);
                en.setId(null);
                en.setDateSave(new Date());
                en.setDateUpdate(new Date());
                en = (YvsComGrilleRemise) dao.save1(en);
                grilles.add(en);

                succes();
                resetFiche();
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error>>>> ", ex);
            return false;
        }
        return true;
    }

    public boolean saveNewGrille() {
        String action = rabais.isUpdate() ? "Modification" : "Insertion";
        try {
            GrilleRabais bean = recopieViewGrille();
            if (controleFicheGrille(bean)) {
                YvsComGrilleRemise ent = buildGrilleRemise(bean);
                ent.setDateUpdate(new Date());
                if (!bean.isUpdate()) {
                    ent.setId(null);
                    ent.setDateSave(new Date());
                    ent = (YvsComGrilleRemise) dao.save1(ent);
                    bean.setId(ent.getId());
                    bean.setUpdate(true);
                    grilles.add(0, ent);
                } else {
                    dao.update(ent);
                    bean.setUpdate(true);
                    if (grilles.contains(ent)) {
                        grilles.set(grilles.indexOf(ent), ent);
                    }
                }
                succes();
                resetFicheGrille();
                update("data_grille_remise");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Lymytz Error>>>> ", ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((tabIds != null) ? !tabIds.equals("") : false) {
                List<Long> l = decomposeIdSelection(tabIds);
                List<YvsComRemise> list = new ArrayList<>();
                YvsComRemise bean;
                for (Long ids : l) {
                    bean = remises.get(ids.intValue());
                    bean.setAuthor(currentUser);
                    bean.setDateUpdate(new Date());
                    list.add(bean);
                    dao.delete(bean);
                }
                remises.removeAll(list);
                succes();
                resetFiche();
                update("data_remise");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Lymytz Error>>> ", ex);
        }
    }

    public void deleteBean_(YvsComRemise y) {
        selectRemise = y;
    }

    public void deleteBean_() {
        try {
            if (selectRemise != null) {
                dao.delete(selectRemise);
                remises.remove(selectRemise);

                succes();
                resetFiche();
                update("data_remise");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Lymytz Error>>> ", ex);
        }
    }

    public void deleteBeanGrille() {
        try {
            if ((tabIds_grille != null) ? !tabIds_grille.equals("") : false) {
                String[] tab = tabIds_grille.split("-");
                for (String ids : tab) {
                    long id = Long.valueOf(ids);
                    YvsComGrilleRemise bean = grilles.get(grilles.indexOf(new YvsComGrilleRemise(id)));
                    dao.delete(new YvsComGrilleRemise(bean.getId()));
                    grilles.remove(bean);
                }
                succes();
                resetFicheGrille();
                update("data_grille_remise");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Lymytz Error>>> ", ex);
        }
    }

    public void deleteBeanGrille_(YvsComGrilleRemise y) {
        selectGrille = y;
    }

    public void deleteBeanGrille_() {
        try {
            if (selectGrille != null) {
                dao.delete(selectGrille);
                grilles.remove(selectGrille);

                succes();
                resetFicheGrille();
                update("data_grille_remise");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppresion Impossible !");
            getException("Lymytz Error>>> ", ex);
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComRemise bean = (YvsComRemise) ev.getObject();
            if (!acces(bean.getCodeAcces())) {
                openNotAccesByCode();
                return;
            }
            populateView(UtilCom.buildBeanRemise(bean));
            loadAllgrille(bean);
            update("blog_form_remise");
        }
    }

    public void loadOnView_(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComRemise bean = (YvsComRemise) ev.getObject();
            populateView(UtilCom.buildBeanRemise(bean));
            loadAllgrille(bean);
            update("form_remise");
            update("data_grille_remise");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche_();
        update("form_remise");
        update("data_grille_remise");
    }

    public void loadOnViewGrille(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComGrilleRemise bean = (YvsComGrilleRemise) ev.getObject();
            populateViewGrille(UtilCom.buildBeanGrilleRemise(bean));
            tabIds = grilles.indexOf(bean) + "";
            update("form_grille_remise");
        }
    }

    public void unLoadOnViewGrille(UnselectEvent ev) {
        resetFicheGrille();
        update("form_grille_remise");
    }

    public void activeRemise(YvsComRemise bean) {
        if (bean != null) {
            bean.setActif(!bean.getActif());
            remises.get(remises.indexOf(bean)).setActif(bean.getActif());
            String rq = "UPDATE yvs_com_remise SET actif=" + bean.getActif() + " WHERE id=?";
            Options[] param = new Options[]{new Options(bean.getId(), 1)};
            dao.requeteLibre(rq, param);
        }
        update("data_remise");
    }

    public void activeUnique() {
        if (rabais.isUnique()) {
            rabais.setMontantMinimal(0);
            rabais.setMontantMaximal(Double.MAX_VALUE);
        } else {
            rabais.setMontantMinimal(0);
            rabais.setMontantMaximal(0);
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> completeRemise(String query) {
        List<String> filteredThemes = new ArrayList<>();
        for (YvsComRemise t : remises) {
            if (t.getRefRemise().toLowerCase().startsWith(query.toLowerCase())) {
                filteredThemes.add(t.getRefRemise());
            }
        }
        return filteredThemes;
    }

    public void onReferenceRemiseSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            String t = (String) ev.getObject();
            onSelectRemise(t);
        }
    }

    public void onReferenceRemiseBlur() {
        if (remise.isUpdate()) {
            onSelectRemise(remise.getReference());
        }
    }

    public void onSelectRemise(String reference) {
        champ = new String[]{"societe", "reference"};
        val = new Object[]{currentAgence.getSociete(), reference};
        nameQueri = "YvsComRemise.findByReference_";
        List<YvsComRemise> lr = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (lr != null ? !lr.isEmpty() : false) {
            YvsComRemise r = lr.get(0);
            cloneObject(remise, UtilCom.buildBeanRemise(r));
            remise.setUpdate(true);
            grilles.addAll(r.getGrilles());
            getWarningMessage("Cette remise existe déja");
        } else {
            grilles.clear();
            remise.setUpdate(false);
        }
    }
}
