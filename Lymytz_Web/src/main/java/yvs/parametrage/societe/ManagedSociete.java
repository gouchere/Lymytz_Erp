/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsModuleActive;
import yvs.entity.param.YvsSocietesInfosVente;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsModule;
import yvs.grh.UtilGrh;
import yvs.grh.bean.ManagedTypeCout;
import yvs.init.Initialisation;
import yvs.parametrage.agence.ManagedAgence;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.dico.ManagedDico;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean()
public class ManagedSociete extends Managed<Societe, YvsSocietes> implements Serializable {
    
    @ManagedProperty(value = "#{societe}")
    private Societe societe;
    private YvsSocietes entity;
    private List<YvsSocietes> listSociete, listSelectSociete;
    private boolean updateSociete, selectSociete, vueListe = true, optionUpdate, superAdmin;
    private String chaineSelectSociete;
    private List<YvsModuleActive> modules;
    
    public ManagedSociete() {
        listSociete = new ArrayList<>();
        listSelectSociete = new ArrayList<>();
        modules = new ArrayList<>();
    }
    
    public YvsSocietes getEntity() {
        return entity;
    }
    
    public void setEntity(YvsSocietes entity) {
        this.entity = entity;
    }
    
    public List<YvsModuleActive> getModules() {
        return modules;
    }
    
    public void setModules(List<YvsModuleActive> modules) {
        this.modules = modules;
    }
    
    public String getChaineSelectSociete() {
        return chaineSelectSociete;
    }
    
    public void setChaineSelectSociete(String chaineSelectSociete) {
        this.chaineSelectSociete = chaineSelectSociete;
    }
    
    public boolean isSuperAdmin() {
        return superAdmin;
    }
    
    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }
    
    public List<YvsSocietes> getListSociete() {
        return listSociete;
    }
    
    public void setListSociete(List<YvsSocietes> listSociete) {
        this.listSociete = listSociete;
    }
    
    public List<YvsSocietes> getListSelectSociete() {
        return listSelectSociete;
    }
    
    public void setListSelectSociete(List<YvsSocietes> listSelectSociete) {
        this.listSelectSociete = listSelectSociete;
    }
    
    public boolean isUpdateSociete() {
        return updateSociete;
    }
    
    public void setUpdateSociete(boolean updateSociete) {
        this.updateSociete = updateSociete;
    }
    
    public boolean isSelectSociete() {
        return selectSociete;
    }
    
    public void setSelectSociete(boolean selectSociete) {
        this.selectSociete = selectSociete;
    }
    
    public boolean isVueListe() {
        return vueListe;
    }
    
    public void setVueListe(boolean vueListe) {
        this.vueListe = vueListe;
    }
    
    public boolean isOptionUpdate() {
        return optionUpdate;
    }
    
    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }
    
    public Societe getSociete() {
        return societe;
    }
    
    public void setSociete(Societe societe) {
        this.societe = societe;
    }
    
    @Override
    public void populateView(Societe bean) {
        cloneObject(societe, bean);
    }
    
    @Override
    public void onSelectObject(YvsSocietes y) {
        entity = y;
        updateSociete = true;
        ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
        if (m != null) {
            m.loadVilles(y.getPays());
        }
        populateView(UtilSte.buildBeanSociete(y));
        ManagedInfosSupplSociete w = (ManagedInfosSupplSociete) giveManagedBean(ManagedInfosSupplSociete.class);
        if (w != null) {
            w.setList(y.getSupplementaires());
        }
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            YvsSocietes bean = (YvsSocietes) ev.getObject();
            onSelectObject(bean);
            chaineSelectSociete = listSociete.indexOf(bean) + "";
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        
    }
    
    @Override
    public void loadAll() {
        champ = new String[]{};
        val = new Object[]{};
        listSociete = dao.loadNameQueries("YvsSocietes.findAll", champ, val);
    }
    
    private int buildDroit() {
        return 2;
    }
    
    public void loadByGroupe() {
        int action = buildDroit();
        
        champ = new String[]{};
        val = new Object[]{};
        nameQueri = "YvsSocietes.findAll";
        if (action == 2) {
            if (currentUser.getUsers().getAccesMultiSociete() && (currentAgence.getSociete().getGroupe() != null ? currentAgence.getSociete().getGroupe().getId() > 0 : false)) {
                champ = new String[]{"groupe"};
                val = new Object[]{currentAgence.getSociete().getGroupe()};
                nameQueri = "YvsSocietes.findBySameGroupe";
            } else {
                champ = new String[]{"id"};
                val = new Object[]{currentAgence.getSociete().getId()};
                nameQueri = "YvsSocietes.findById";
            }
        }
        listSociete = dao.loadNameQueries(nameQueri, champ, val);
    }
    
    public void loadAllAgence(long societe) {
        ManagedAgence w = (ManagedAgence) giveManagedBean(ManagedAgence.class);
        if (w != null) {
            w.loadAllAgence(societe);
        }
    }
    
    public void valideUploadImage() {
        saveNew();
    }
    
    public void handleFileUpload(FileUploadEvent ev) {
        if (ev != null) {
            String repDest = Initialisation.getCheminResource() + "" + Initialisation.getCheminLogos().substring(Initialisation.getCheminAllDoc().length(), Initialisation.getCheminLogos().length());
            //répertoire destination de sauvegarge= C:\\lymytz\scte...
            String repDestSVG = Initialisation.getCheminLogos();
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
                societe.setLogo(file);
                getInfoMessage("Charger !");
                update("logo_ste");
                
            } catch (IOException ex) {
                Logger.getLogger(ManagedSociete.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static String photo(Societe u) {
        if (u != null) {
            ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
            String path = ext.getRealPath("resources/lymytz/documents/logos_doc/") + Initialisation.FILE_SEPARATOR + u.getLogo();
            if (new File(path).exists()) {
                return u.getLogo();
            }
        }
        return "default.png";
    }
    
    @Override
    public void updateBean() {
//        setVueListe(false);
//        setSelectSociete(false);
//        update("body_societe");
//        update("entete_societe");
    }
    
    @Override
    public void deleteBean() {
        if (chaineSelectSociete != null) {
            List<Long> l = decomposeIdSelection(chaineSelectSociete);
            List<YvsSocietes> list = new ArrayList<>();
            YvsSocietes bean;
            try {
                for (Long ids : l) {
                    bean = listSociete.get(ids.intValue());
                    bean.setDateUpdate(new Date());
                    bean.setAuthor(currentUser);
                    list.add(bean);
                    dao.delete(bean);
                }
                chaineSelectSociete = "";
                succes();
                listSociete.removeAll(list);
            } catch (Exception ex) {
                chaineSelectSociete = "";
                getMessage("Impossible de terminer cette opération ! des élément de votre sélection doivent être encore en liaison", FacesMessage.SEVERITY_ERROR);
            }
        }
    }
    
    @Override
    public Societe recopieView() {
        return societe;
    }
    
    @Override
    public boolean controleFiche(Societe bean) {
        if (bean.getCodeAbreviation() == null || bean.getCodeAbreviation().equals("")) {
            getErrorMessage("Vous devez indiquer l'abbreviation");
            return false;
        }
        if (bean.getRaisonSocial() == null || bean.getRaisonSocial().equals("")) {
            getErrorMessage("Vous devez indiquer la raison social de cette nouvelle Société");
            return false;
        }
        if (bean.isVenteOnline()) {
            if (bean.getInfosVente().getTypeLivraison() != null ? bean.getInfosVente().getTypeLivraison().getId() < 1 : true) {
                getErrorMessage("Vous devez précisez le type de cout pour les frais de livraison");
                return false;
            }
        }
        return true;
    }
    
    public void presaveSociete() {
        if (societe.getCodeAbreviation() != null && updateSociete == false) {
            YvsSocietes entitySociete = UtilSte.buildSociete(societe, currentUser);
            entitySociete.setId(null);
            entitySociete = (YvsSocietes) dao.save1(entitySociete);
            societe.setId(entitySociete.getId());
            listSociete.add(entitySociete);
            updateSociete = true;
        }
    }
    
    @Override
    public boolean saveNew() {
        try {
            Societe ste = recopieView();
            if (controleFiche(ste)) {
                entity = UtilSte.buildSociete(ste, currentUser);
                if (!isUpdateSociete()) {
                    entity.setId(null);
                    entity.setAuthor(currentUser);
                    entity = (YvsSocietes) dao.save1(currentAgence.getSociete());
                    societe.setId(ste.getId());
                    listSociete.add(entity);
                } else {
                    dao.update(entity);
                    int idx = listSociete.indexOf(entity);
                    if (idx > -1) {
                        listSociete.set(idx, entity);
                    }
                }
                if (ste.isVenteOnline()) {
                    YvsSocietesInfosVente y = UtilSte.buildInfosVente(ste.getInfosVente(), entity, currentUser);
                    if (ste.getInfosVente().getId() < 1) {
                        y = (YvsSocietesInfosVente) dao.save1(y);
                        societe.getInfosVente().setId(y.getId());
                    } else {
                        dao.update(y);
                    }
                    entity.setInfosVente(y);
                }
                if (!isSuperAdmin()) {
                    currentAgence.setSociete(entity);
                }
                setUpdateSociete(true);
                succes();
                update("form_societe_00");
            }
            return true;
        } catch (Exception ex) {
            getErrorMessage((isUpdateSociete() ? "Modification" : "Insertion") + " n'a pas pu s'executer");
            Logger.getLogger(ManagedSociete.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public void resetFiche() {
        resetFiche(societe);
        societe.setVille(new Dictionnaire());
        societe.setPays(new Dictionnaire());
        societe.setInfosVente(new InfosVenteSociete());
        updateSociete = false;
    }
    
    public void choosePays() {
        if (societe.getPays() != null ? societe.getPays().getId() > 0 : false) {
            update("txt_pays_ville_");
            societe.setVille(new Dictionnaire());
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.choosePays(societe.getPays().getId());
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(societe.getPays(), d);
                }
            }
        } else if (societe.getPays() != null ? societe.getPays().getId() == -1 : false) {
            openDialog("dlgnewPays");
        }
    }
    
    public void chooseVille() {
        if (societe.getVille() != null ? societe.getVille().getId() > 0 : false) {
            ManagedDico m = (ManagedDico) giveManagedBean("Mdico");
            if (m != null) {
                Dictionnaire d = m.chooseVille(societe.getPays(), societe.getVille().getId());
                if (d != null ? d.getId() > 0 : false) {
                    cloneObject(societe.getVille(), d);
                }
            }
        } else if (societe.getVille() != null ? societe.getVille().getId() == -1 : false) {
            openDialog("dlgNewVille");
        }
    }
    
    public void chooseTypeCout() {
        if (societe.getInfosVente().getTypeLivraison() != null ? societe.getInfosVente().getTypeLivraison().getId() > 0 : false) {
            ManagedTypeCout m = (ManagedTypeCout) giveManagedBean(ManagedTypeCout.class);
            if (m != null) {
                int idx = m.getTypes().indexOf(new YvsGrhTypeCout(societe.getInfosVente().getTypeLivraison().getId()));
                if (idx > -1) {
                    YvsGrhTypeCout y = m.getTypes().get(idx);
                    societe.getInfosVente().setTypeLivraison(UtilGrh.buildBeanTypeCout(y));
                }
            }
        }
    }
    
    public void confirmSave() {
        saveNew();
    }
    
    public void onVerifyDroit() {
        setSuperAdmin(Initialisation.USERISSUPERADMIN);
        setVueListe(isSuperAdmin());
        if (!isSuperAdmin()) {
            onSelectObject(currentAgence.getSociete());
        } else {
            onSelectObject(listSociete != null ? listSociete.size() > 0 ? listSociete.get(0) : currentAgence.getSociete() : currentAgence.getSociete());
        }
    }
    
    public void buildModuleActive(YvsSocietes entity) {
        modules.clear();
        if (entity != null ? entity.getId() < 1 : true) {
            return;
        }
        String query = "SELECT m.id, m.libelle, m.description, a.id, COALESCE(a.actif, FALSE) as actif  FROM yvs_module m LEFT JOIN yvs_module_active a ON m.id = a.module AND a.societe = ? ";
        List<Object[]> result = dao.loadListBySqlQuery(query, new Options[]{new Options(entity.getId(), 1)});
        if (result != null ? !result.isEmpty() : false) {
            YvsModule m;
            YvsModuleActive a;
            for (Object[] data : result) {
                m = new YvsModule((Integer) data[0]);
                m.setLibelle((String) data[1]);
                m.setDescription((String) data[2]);
                a = new YvsModuleActive(data[3] != null ? (Integer) data[3] : YvsModuleActive.ids--);
                a.setActif((Boolean) data[4]);
                a.setAuthor(currentUser);
                a.setModule(m);
                a.setSociete(entity);
                modules.add(a);
            }
        }
    }
    
    public void activeModule(YvsModuleActive m) {
        if (m != null) {
            int index = modules.indexOf(m);
            if (m.getId() > 0) {
                dao.update(m);
            } else {
                m.setId(null);
                m = (YvsModuleActive) dao.save1(m);
            }
            modules.set(index, m);
            succes();
        }
    }
}
