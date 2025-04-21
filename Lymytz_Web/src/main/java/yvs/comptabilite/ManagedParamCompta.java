/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.comptabilite;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.Comptes;
import yvs.base.compta.Journaux;
import yvs.base.compta.ManagedCompte;
import yvs.base.compta.UtilCompta;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsComptaParametre;
import yvs.util.Managed;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedParamCompta extends Managed<ParametreCompta, YvsComptaParametre> implements Serializable {

    private ParametreCompta param = new ParametreCompta();

    public ManagedParamCompta() {
    }

    public ParametreCompta getParam() {
        return param;
    }

    public void setParam(ParametreCompta param) {
        this.param = param;
    }

    @Override
    public void loadAll() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        nameQueri = "YvsComptaParametre.findAll";
        List<YvsComptaParametre> l = dao.loadNameQueries(nameQueri, champ, val, 0, 1);
        if (l != null ? !l.isEmpty() : false) {
            param = UtilCompta.buildBeanParametre(l.get(0));
        }
    }

    @Override
    public boolean controleFiche(ParametreCompta bean) {
        if (bean.getTailleCompte() < 4) {
            getErrorMessage("La taille du compte ne peut pas etre inferieur a 4");
            return false;
        }
        if (bean.isDecimalArrondi() && bean.getModeArrondi() == null) {
            getErrorMessage("Vous devez precisez le mode d'arrondi");
            return false;
        }
        if (bean.isDecimalArrondi() && bean.getMultipleArrondi() < 1) {
            getErrorMessage("Vous devez precisez le multiple d'arrondi");
            return false;
        }
        return true;
    }

    @Override
    public ParametreCompta recopieView() {
        return param;
    }

    @Override
    public void populateView(ParametreCompta bean) {
        cloneObject(param, bean);
    }

    @Override
    public void resetFiche() {
        resetFiche(param);
        param.setCompteBeneficeReport(new Comptes());
        param.setComptePerteReport(new Comptes());
        param.setJournalReport(new Journaux());
    }

    @Override
    public boolean saveNew() {
        String action = param.getId() > 0 ? "Modification" : "Insertion";
        try {
            ParametreCompta p = recopieView();
            if (controleFiche(p)) {
                YvsComptaParametre y = UtilCompta.buildParametre(p, currentAgence.getSociete(), currentUser);
                if (p.getId() > 0) {
                    dao.update(y);
                } else {
                    y.setId(null);
                    y = (YvsComptaParametre) dao.save1(y);
                    param.setId(y.getId());
                }
                succes();
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " impossible");
            getException(action + " impossible", ex);
            return false;
        }
    }

    @Override
    public void deleteBean() {
        if (param != null ? param.getId() > 0 : false) {
            dao.delete(new YvsComptaParametre(param.getId()));
            resetFiche();
            succes();
        }
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComptaParametre y = (YvsComptaParametre) ev.getObject();
            populateView(UtilCompta.buildBeanParametre(y));
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void searchCompteBenefice() {
        String numCompte = param.getCompteBeneficeReport().getNumCompte();
        param.getCompteBeneficeReport().setId(0);
        param.getCompteBeneficeReport().setIntitule("");
        if (numCompte != null ? numCompte.trim().length() > 0 : false) {
            param.getCompteBeneficeReport().setError(true);
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(numCompte);
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() == 1) {
                        param.getCompteBeneficeReport().setError(false);
                        cloneObject(param.getCompteBeneficeReport(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        openDialog("dlgListCompteBR");
                        update("data_comptes_benefice_report");
                    }
                }
            }
        }
    }

    public void searchComptePerte() {
        String numCompte = param.getComptePerteReport().getNumCompte();
        param.getComptePerteReport().setId(0);
        param.getComptePerteReport().setIntitule("");
        if (numCompte != null ? numCompte.trim().length() > 0 : false) {
            param.getComptePerteReport().setError(true);
            ManagedCompte service = (ManagedCompte) giveManagedBean(ManagedCompte.class);
            if (service != null) {
                service.findCompteByNum(numCompte);
                if (service.getListComptes() != null ? !service.getListComptes().isEmpty() : false) {
                    if (service.getListComptes().size() == 1) {
                        param.getComptePerteReport().setError(false);
                        cloneObject(param.getComptePerteReport(), UtilCompta.buildBeanCompte(service.getListComptes().get(0)));
                    } else {
                        openDialog("dlgListComptePR");
                        update("data_comptes_perte_report");
                    }
                }
            }
        }
    }

    public void loadOnViewCompteBenefice(SelectEvent ev) {
        if (ev != null) {
            cloneObject(param.getCompteBeneficeReport(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
        }
    }

    public void loadOnViewComptePerte(SelectEvent ev) {
        if (ev != null) {
            cloneObject(param.getComptePerteReport(), UtilCompta.buildSimpleBeanCompte(((YvsBasePlanComptable) ev.getObject())));
        }
    }
}
