/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.entity.mutuelle.YvsMutParametre;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean(name = "managedParametrage")
@SessionScoped
public class ManagedParametre extends Managed<Parametre, YvsBaseCaisse> implements Serializable {

    @ManagedProperty(value = "#{parametrage}")
    private Parametre parametre;
    private List<YvsMutParametre> parametres;

    private String tabIds, input_reset;
    private boolean updateParametre;

    public ManagedParametre() {
        parametres = new ArrayList<>();
    }

    public Parametre getParametre() {
        return parametre;
    }

    public void setParametre(Parametre parametre) {
        this.parametre = parametre;
    }

    public List<YvsMutParametre> getParametres() {
        return parametres;
    }

    public void setParametres(List<YvsMutParametre> parametres) {
        this.parametres = parametres;
    }

    public String getTabIds() {
        return tabIds;
    }

    public void setTabIds(String tabIds) {
        this.tabIds = tabIds;
    }

    public String getInput_reset() {
        return input_reset;
    }

    public void setInput_reset(String input_reset) {
        this.input_reset = input_reset;
    }

    public boolean isUpdateParametre() {
        return updateParametre;
    }

    public void setUpdateParametre(boolean updateParametre) {
        this.updateParametre = updateParametre;
    }

    @Override
    public void loadAll() {
        loadAllParametre();
        tabIds = "";
    }

    public void loadAllParametre() {
        parametres = dao.loadNameQueries("YvsMutParametre.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        if ((parametres != null) ? !parametres.isEmpty() : false) {
            YvsMutParametre bean = parametres.get(0);
            populateView(UtilMut.buildBeanParametre(bean));
        }
    }

    public YvsMutParametre buildParametre(Parametre y) {
        YvsMutParametre t = new YvsMutParametre();
        if (y != null) {
            t.setId(y.getId());
            t.setDureeMembre(y.getDureeMembre());
            t.setQuotiteCessible(y.getQuotiteCessible());
            t.setPeriodeSalaireMoyen(y.getPeriodeSalaireMoyen());
            t.setSouscriptionGeneral(y.isSouscriptionGeneral());
            t.setRetainsEpargne(y.isRetainsEpargne());
            t.setMonnaie(y.getMonnaie());
            t.setValidCreditByVote(y.isValidCreditByVote());
            t.setDureeEtudeCredit(y.getDureeEtudeCredit());
            t.setTauxVoteValidCreditCorrect(y.getTauxVoteValidCreditCorrect());
            t.setTauxVoteValidCreditIncorrect(y.getTauxVoteValidCreditIncorrect());
            t.setTauxCouvertureCredit(y.getTauxCouvertureCredit());
            t.setPaiementParCompteStrict(y.isPaiementParCompteStrict());
            t.setAcceptRetraitEpargne(y.isAcceptRetraitEpargne());
            t.setMutuelle(currentMutuel);
            t.setAuthor(currentUser);
            t.setDebutEpargne(y.getDebutEpargne());
            t.setFinEpargne(y.getFinEpargne());
            t.setRetardSaisieEpargne(y.getRetardSaisieEpargne());
            t.setCreditRetainsInteret(y.isCreditRetainsInteret());
            t.setCapaciteEndettement(y.getCapaciteEndettement());
            t.setBaseCapaciteEndettement(y.getBaseCapaciteEndettement());
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }

    @Override
    public Parametre recopieView() {
        Parametre t = new Parametre();
        t.setId(parametre.getId());
        t.setDureeMembre(parametre.getDureeMembre());
        t.setQuotiteCessible(parametre.getQuotiteCessible());
        t.setDebutMois(parametre.getDebutMois());
        t.setPeriodeSalaireMoyen(parametre.getPeriodeSalaireMoyen());
        t.setSouscriptionGeneral(parametre.isSouscriptionGeneral());
        t.setRetainsEpargne(parametre.isRetainsEpargne());
        t.setMonnaie(parametre.getMonnaie());
        t.setValidCreditByVote(parametre.isValidCreditByVote());
        t.setDureeEtudeCredit(parametre.getDureeEtudeCredit());
        t.setTauxVoteValidCreditCorrect(parametre.getTauxVoteValidCreditCorrect());
        t.setTauxVoteValidCreditIncorrect(parametre.getTauxVoteValidCreditIncorrect());
        t.setTauxCouvertureCredit(parametre.getTauxCouvertureCredit());
        t.setPaiementParCompteStrict(parametre.isPaiementParCompteStrict());
        t.setAcceptRetraitEpargne(parametre.isAcceptRetraitEpargne());
        t.setDebutEpargne(parametre.getDebutEpargne());
        t.setFinEpargne(parametre.getFinEpargne());
        t.setRetardSaisieEpargne(parametre.getRetardSaisieEpargne());
        t.setCreditRetainsInteret(parametre.isCreditRetainsInteret());
         t.setCapaciteEndettement(parametre.getCapaciteEndettement());
            t.setBaseCapaciteEndettement(parametre.getBaseCapaciteEndettement());
        return t;
    }

    @Override
    public boolean controleFiche(Parametre bean) {
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }

    @Override
    public void populateView(Parametre bean) {
        cloneObject(parametre, bean);
        setUpdateParametre(true);
    }

    @Override
    public void resetFiche() {
        resetFiche(parametre);
        tabIds = "";
        input_reset = "";
        setUpdateParametre(false);
    }

    @Override
    public boolean saveNew() {
        String action = isUpdateParametre() ? "Modification" : "Insertion";
        try {
            Parametre bean = recopieView();
            if (controleFiche(bean)) {
                YvsMutParametre entity = buildParametre(bean);
                if (!isUpdateParametre()) {
                    entity.setId(null);
                    entity = (YvsMutParametre) dao.save1(entity);
                    bean.setId(entity.getId());
                    parametre.setId(entity.getId());
                    currentMutuel.setParamsMutuelle(entity);
                } else {
                    dao.update(entity);
                    currentMutuel.setParamsMutuelle(entity);
                }
                setUpdateParametre(true);
                succes();
                update("form_parametrage");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    @Override
    public void deleteBean() {
        try {
            if ((parametre != null) ? parametre.getId() > 0 : false) {
                dao.delete(new YvsMutParametre(parametre.getId()));
                parametres.remove(parametre);
                currentMutuel.setParamsMutuelle(null);
                resetFiche();
                succes();
                update("form_parametrage");
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Parametre bean = (Parametre) ev.getObject();
            populateView(bean);
            update("form_parametrage");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("form_parametrage");
    }

}
