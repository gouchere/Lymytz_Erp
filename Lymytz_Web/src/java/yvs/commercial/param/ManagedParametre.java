/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.ManagedCompte;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.UtilCom;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaJournaux;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedParametre extends ManagedCommercial<Parametre, YvsBaseCaisse> implements Serializable {

    private Parametre parametre = new Parametre();

    private ParametreCom achat = new ParametreCom();
    private ParametreCom vente = new ParametreCom();
    private ParametreCom stock = new ParametreCom();

    private String modelFactureVente;
    private String modelTicketVente;

    private Object managedForArticle;

    public ManagedParametre() {

    }

    public Object getManagedForArticle() {
        return managedForArticle;
    }

    public void setManagedForArticle(Object managedForArticle) {
        this.managedForArticle = managedForArticle;
    }

    public String getModelFactureVente() {
        return modelFactureVente;
    }

    public void setModelFactureVente(String modelFactureVente) {
        this.modelFactureVente = modelFactureVente;
    }

    public String getModelTicketVente() {
        return modelTicketVente;
    }

    public void setModelTicketVente(String modelTicketVente) {
        this.modelTicketVente = modelTicketVente;
    }

    public Parametre getParametre() {
        return parametre;
    }

    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }

    public ParametreCom getAchat() {
        return achat;
    }

    public void setAchat(ParametreCom achat) {
        this.achat = achat;
    }

    public ParametreCom getVente() {
        return vente;
    }

    public void setVente(ParametreCom vente) {
        this.vente = vente;
    }

    public ParametreCom getStock() {
        return stock;
    }

    public void setStock(ParametreCom stock) {
        this.stock = stock;
    }

    @Override
    public void loadAll() {
        loadParametre();
        loadAllCom();
    }

    public void loadAllCom() {
        loadParametreAchat();
        loadParametreVente();
        loadParametreStock();
    }

    public void loadParametre() {
        resetFiche();
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComParametre.findAll";
        List<YvsComParametre> l = dao.loadNameQueries(nameQueri, champ, val);
        if ((l != null) ? !l.isEmpty() : false) {
            parametre = UtilCom.buildBeanParametre(l.get(0));
        }
    }

    public void loadParametreAchat() {
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        nameQueri = "YvsComParametreAchat.findByAgence";
        List<YvsComParametreAchat> l = dao.loadNameQueries(nameQueri, champ, val);
        if ((l != null) ? !l.isEmpty() : false) {
            achat = UtilCom.buildBeanParametreAchat(l.get(0));
        }
    }

    public void loadParametreVente() {
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        nameQueri = "YvsComParametreVente.findByAgence";
        List<YvsComParametreVente> l = dao.loadNameQueries(nameQueri, champ, val);
        if ((l != null) ? !l.isEmpty() : false) {
            vente = UtilCom.buildBeanParametreVente(l.get(0));
        }
    }

    public void loadParametreStock() {
        champ = new String[]{"agence"};
        val = new Object[]{currentAgence};
        nameQueri = "YvsComParametreStock.findByAgence";
        List<YvsComParametreStock> l = dao.loadNameQueries(nameQueri, champ, val);
        if ((l != null) ? !l.isEmpty() : false) {
            stock = UtilCom.buildBeanParametreStock(l.get(0));
        }
    }

    public YvsComParametre buildParametre(Parametre y) {
        YvsComParametre p = new YvsComParametre();
        if (y != null) {
            p.setId(y.getId());
            p.setReglementAuto(y.isReglementAuto());
            p.setDocumentMouvAchat(y.getDocumentMouvAchat());
            p.setDocumentMouvVente(y.getDocumentMouvVente());
            p.setModeInventaire(y.getModeInventaire());
            p.setDocumentGenererFromEcart(y.getDocumentGenererFromEcart());
            p.setSeuilClient(y.getSeuilClient());
            p.setSeuilFsseur(y.getSeuilFsseur());
            p.setDureeInactiv(y.getDureeInactiv());
            p.setConverter(y.getConverter());
            p.setConverterCs(y.getConverterCs());
            p.setJourUsine(y.getJourUsine());
            p.setUseLotReception(y.isUseLotReception());
            p.setTauxMargeSur(y.getTauxMargeSur());
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            p.setDisplayAvgPuv(y.isDisplayAvgPuv());
            p.setDisplayPrixRevient(y.isDisplayPrixRevient());
            p.setDisplayResteALivrer(y.isDisplayResteALivrer());
            p.setSociete(currentAgence.getSociete());
            p.setAuthor(currentUser);
        }
        return p;
    }

    public YvsComParametreStock buildParametreStock(ParametreCom y) {
        YvsComParametreStock p = new YvsComParametreStock();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.isComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setDureeUpdate(y.getDureeUpdate());
            p.setDureeSaveRation(y.getDureeSaveRation());
            p.setTailleCodeRation(y.getTailleCodeRation());
            p.setActiveRation(y.isActiveRation());
            p.setPrintDocumentWhenValide(y.isPrintDocumentWhenValide());
            p.setCalculPr(y.isCalculPr());
            p.setMargeTimeFicheRation(y.getMargeTimeFicheRation());
            p.setAgence(currentAgence);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                p.setAuthor(currentUser);
            }
        }
        return p;
    }

    public YvsComParametreAchat buildParametreAchat(ParametreCom y) {
        YvsComParametreAchat p = new YvsComParametreAchat();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.isComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setPaieWithoutValide(y.isPaieWithoutValide());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setGenererFactureAuto(y.isGenererFactureAuto());
            p.setPrintDocumentWhenValide(y.isPrintDocumentWhenValide());
            p.setAgence(currentAgence);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                p.setAuthor(currentUser);
            }
            if (y.getJournal() != null ? y.getJournal().getId() > 0 : false) {
                p.setJournal(new YvsComptaJournaux(y.getJournal().getId(), y.getJournal().getCodejournal()));
            }
        }
        return p;
    }

    public YvsComParametreVente buildParametreVente(ParametreCom y) {
        YvsComParametreVente p = new YvsComParametreVente();
        if (y != null) {
            p.setId(y.getId());
            p.setComptabilisationAuto(y.isComptabilisationAuto());
            p.setComptabilisationMode(y.getComptabilisationMode());
            p.setPaieWithoutValide(y.isPaieWithoutValide());
            p.setJourAnterieur(y.getJourAnterieur());
            p.setNbFicheMax(y.getNbFicheMax());
            p.setGenererFactureAuto(y.isGenererFactureAuto());
            p.setModelFactureVente(y.getModelFactureVente());
            p.setSellLowerPr(y.isSellLowerPr());
            p.setLivreBcvWithoutPaye(y.isLivreBcvWithoutPaye());
            p.setGiveBonusInStatus(y.getGiveBonusInStatus());
            p.setLivraisonAuto(y.isLivraisonAuto());
            p.setPrintDocumentWhenValide(y.isPrintDocumentWhenValide());
            p.setAgence(currentAgence);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
            if (currentUser != null ? currentUser.getId() > 0 : false) {
                p.setAuthor(currentUser);
            }
            if (y.getJournal() != null ? y.getJournal().getId() > 0 : false) {
                p.setJournal(new YvsComptaJournaux(y.getJournal().getId(), y.getJournal().getCodejournal()));
            }
        }
        return p;
    }

    @Override
    public Parametre recopieView() {
        return parametre;
    }

    public ParametreCom recopieViewAchat() {
        return achat;
    }

    public ParametreCom recopieViewStock() {
        return stock;
    }

    public ParametreCom recopieViewVente() {
        return vente;
    }

    @Override
    public boolean controleFiche(Parametre bean) {

        return true;
    }

    public boolean controleFiche(ParametreCom bean) {

        return true;
    }

    @Override
    public void populateView(Parametre bean) {
        cloneObject(parametre, bean);
    }

    public void populateViewAchat(Parametre bean) {
        cloneObject(achat, bean);
    }

    public void populateViewVente(Parametre bean) {
        cloneObject(vente, bean);
    }

    public void populateViewStock(Parametre bean) {
        cloneObject(stock, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(parametre);
    }

    public void resetFicheAchat() {
        resetFiche(achat);
    }

    public void resetFicheVente() {
        resetFiche(vente);
    }

    public void resetFicheStock() {
        resetFiche(stock);
    }

    @Override
    public boolean saveNew() {
        String action = parametre.isUpdate() ? "Modification" : "Insertion";
        try {
            Parametre bean = recopieView();
            if (controleFiche(bean)) {
                YvsComParametre entity = buildParametre(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsComParametre) dao.save1(entity);
                    bean.setId(entity.getId());
                    bean.setUpdate(true);
                    parametre.setId(entity.getId());
                } else {
                    dao.update(entity);
                    bean.setUpdate(true);
                }
                succes();
                parametre.setUpdate(bean.isUpdate());
                update("blog_form_parametrage");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewAchat() {
        String action = achat.isUpdate() ? "Modification" : "Insertion";
        try {
            ParametreCom bean = recopieViewAchat();
            if (controleFiche(bean)) {
                YvsComParametreAchat entity = buildParametreAchat(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsComParametreAchat) dao.save1(entity);
                    bean.setId(entity.getId());
                    bean.setUpdate(true);
                    achat.setId(entity.getId());
                } else {
                    dao.update(entity);
                    bean.setUpdate(true);
                }
                succes();
                achat.setUpdate(bean.isUpdate());
//                update("blog_form_parametrage_achat");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewVente() {
        String action = vente.isUpdate() ? "Modification" : "Insertion";
        try {
            ParametreCom bean = recopieViewVente();
            if (controleFiche(bean)) {
                YvsComParametreVente entity = buildParametreVente(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsComParametreVente) dao.save1(entity);
                    bean.setId(entity.getId());
                    bean.setUpdate(true);
                    vente.setId(entity.getId());
                } else {
                    dao.update(entity);
                    bean.setUpdate(true);
                }
                succes();
                vente.setUpdate(bean.isUpdate());
//                update("blog_form_parametrage_vente");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    public boolean saveNewStock() {
        String action = stock.isUpdate() ? "Modification" : "Insertion";
        try {
            ParametreCom bean = recopieViewStock();
            if (controleFiche(bean)) {
                YvsComParametreStock entity = buildParametreStock(bean);
                if (!bean.isUpdate()) {
                    entity.setId(null);
                    entity = (YvsComParametreStock) dao.save1(entity);
                    bean.setId(entity.getId());
                    bean.setUpdate(true);
                    stock.setId(entity.getId());
                } else {
                    dao.update(entity);
                    bean.setUpdate(true);
                }
                succes();
                stock.setUpdate(bean.isUpdate());
                update("tabview_param_com:blog_form_parametrage_stock");
            }
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            getException("Error " + action + " : " + ex.getMessage(), ex);
            return false;
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((parametre != null) ? parametre.getId() > 0 : false) {
                dao.delete(new YvsComParametre(parametre.getId()));
                resetFiche();
                update("blog_form_parametrage");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanAchat() {
        try {
            if ((achat != null) ? achat.getId() > 0 : false) {
                dao.delete(new YvsComParametreAchat(achat.getId()));
                resetFicheAchat();
                update("blog_form_parametrage_achat");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanVente() {
        try {
            if ((vente != null) ? vente.getId() > 0 : false) {
                dao.delete(new YvsComParametreVente(vente.getId()));
                resetFicheVente();
                update("blog_form_parametrage_vente");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
    }

    public void deleteBeanStock() {
        try {
            if ((stock != null) ? stock.getId() > 0 : false) {
                dao.delete(new YvsComParametreStock(stock.getId()));
                resetFicheStock();
                update("blog_form_parametrage_stock");
            }
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            getException("Error Suppression : " + ex.getMessage(), ex);
        }
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
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadOnViewArticle(SelectEvent ev) {
        if (managedForArticle != null) {
            try {
                Method method = managedForArticle.getClass().getMethod("loadOnViewArticle", SelectEvent.class);
                method.invoke(managedForArticle, ev);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ManagedCompte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
