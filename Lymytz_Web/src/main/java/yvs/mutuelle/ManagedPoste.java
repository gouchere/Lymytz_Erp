/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle;

import yvs.mutuelle.base.Poste;
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
import yvs.entity.mutuelle.base.YvsMutPoste;
import yvs.entity.mutuelle.base.YvsMutPrimePoste;
import yvs.entity.mutuelle.base.YvsMutTypePrime;
import yvs.mutuelle.base.PrimePoste;
import yvs.mutuelle.base.TypePrime;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedPoste extends Managed<Poste, YvsBaseCaisse> implements Serializable {
    
    @ManagedProperty(value = "#{poste}")
    private Poste poste;
    private YvsMutPoste posteSelect;
    private List<YvsMutPoste> postes;
    
    private PrimePoste prime = new PrimePoste();
    private TypePrime type = new TypePrime();
    private List<TypePrime> types;
    
    private String tabIds, input_reset, tabIds_prime, input_reset_prime;
    private boolean updatePoste, updatePrime;
    
    public ManagedPoste() {
        types = new ArrayList<>();
        postes = new ArrayList<>();
    }
    
    public TypePrime getType() {
        return type;
    }
    
    public void setType(TypePrime type) {
        this.type = type;
    }
    
    public String getTabIds_prime() {
        return tabIds_prime;
    }
    
    public void setTabIds_prime(String tabIds_prime) {
        this.tabIds_prime = tabIds_prime;
    }
    
    public String getInput_reset_prime() {
        return input_reset_prime;
    }
    
    public void setInput_reset_prime(String input_reset_prime) {
        this.input_reset_prime = input_reset_prime;
    }
    
    public boolean isUpdatePrime() {
        return updatePrime;
    }
    
    public void setUpdatePrime(boolean updatePrime) {
        this.updatePrime = updatePrime;
    }
    
    public YvsMutPoste getPosteSelect() {
        return posteSelect;
    }
    
    public void setPosteSelect(YvsMutPoste posteSelect) {
        this.posteSelect = posteSelect;
    }
    
    public PrimePoste getPrime() {
        return prime;
    }
    
    public void setPrime(PrimePoste prime) {
        this.prime = prime;
    }
    
    public List<TypePrime> getTypes() {
        return types;
    }
    
    public void setTypes(List<TypePrime> types) {
        this.types = types;
    }
    
    public Poste getPoste() {
        return poste;
    }
    
    public void setPoste(Poste poste) {
        this.poste = poste;
    }
    
    public List<YvsMutPoste> getPostes() {
        return postes;
    }
    
    public void setPostes(List<YvsMutPoste> postes) {
        this.postes = postes;
    }
    
    public boolean isUpdatePoste() {
        return updatePoste;
    }
    
    public void setUpdatePoste(boolean updatePoste) {
        this.updatePoste = updatePoste;
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
    
    @Override
    public void loadAll() {
        
        loadAllPoste();
        tabIds = "";
        tabIds_prime = "";
    }
    
    public void loadAllTypePrime() {
        List<YvsMutTypePrime> l = dao.loadNameQueries("YvsMutTypePrime.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
        types = UtilMut.buildBeanListTypePrime(l);
        update("form_prime_poste");
    }
    
    public void loadAllPoste() {
        postes = dao.loadNameQueries("YvsMutPoste.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }
    
    public YvsMutPoste buildPoste(Poste y) {
        YvsMutPoste p = new YvsMutPoste();
        if (y != null) {
            p.setId(y.getId());
            p.setDesignation(y.getDesignation());
            p.setDescription(y.getDescription());
            p.setCanVoteCredit(y.isCanVoteCredit());
            p.setMutuelle(currentMutuel);
            p.setAuthor(currentUser);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
        }
        return p;
    }
    
    @Override
    public Poste recopieView() {
        Poste p = new Poste();
        p.setId(poste.getId());
        p.setDesignation(poste.getDesignation());
        p.setDescription(poste.getDescription());
        p.setMontantPrime(poste.getMontantPrime());
        poste.setMutuelle(UtilMut.buildBeanMutuelle(currentMutuel));
        p.setCanVoteCredit(poste.isCanVoteCredit());
        p.setMutuelle(poste.getMutuelle());
        p.setDateSave(poste.getDateSave());
        return p;
    }
    
    @Override
    public boolean controleFiche(Poste bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation!");
            return false;
        }
        if (currentMutuel != null ? currentMutuel.getId() < 1 : true) {
            getErrorMessage("Vous devez etre connecté dans une mutuelle");
            return false;
        }
        return true;
    }
    
    @Override
    public void resetFiche() {
        resetFiche(poste);
        poste.setMutuelle(new Mutuelle());
        setUpdatePoste(false);
        tabIds = "";
        tabIds_prime = "";
        input_reset = "";
        input_reset_prime = "";
    }
    
    @Override
    public boolean saveNew() {
        if (input_reset != null && input_reset.equals("reset")) {
            setUpdatePoste(false);
            input_reset = "";
        }
        String action = isUpdatePoste() ? "Modification" : "Insertion";
        try {
            Poste bean = recopieView();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutPoste entyPoste = buildPoste(bean);
                if (!isUpdatePoste()) {
                    entyPoste.setDateSave(new Date());
                    entyPoste.setId(null);
                    entyPoste = (YvsMutPoste) dao.save1(entyPoste);
                    bean.setId(entyPoste.getId());
                    poste.setId(entyPoste.getId());
                    postes.add(0, entyPoste);
                } else {
                    dao.update(entyPoste);
                    postes.set(postes.indexOf(entyPoste), entyPoste);
                }
                succes();
                resetFiche();
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
            if ((tabIds != null) ? tabIds.length() > 0 : false) {
                String[] ids = tabIds.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutPoste bean = postes.get(postes.indexOf(new YvsMutPoste(id)));
                        dao.delete(new YvsMutPoste(bean.getId()));
                        postes.remove(bean);
                    }
                    resetFiche();
                    succes();
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }
    
    public void deleteBean(YvsMutPoste y) {
        try {
            if (y != null) {
                dao.delete(y);
                postes.remove(y);
                
                resetFiche();
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }
    
    @Override
    public void updateBean() {
        if ((tabIds != null) ? tabIds.length() > 0 : false) {
            String[] ids = tabIds.split("-");
            setUpdatePoste((ids != null) ? ids.length > 0 : false);
            if (isUpdatePoste()) {
                long id = Integer.valueOf(ids[ids.length - 1]);
                YvsMutPoste bean = postes.get(postes.indexOf(new YvsMutPoste(id)));
                populateView(UtilMut.buildBeanPoste(bean));
                tabIds = "";
                input_reset = "";
                update("blog_form_poste_mutuelle_");
            }
        }
    }
    
    @Override
    public void populateView(Poste bean) {
        cloneObject(poste, bean);
        setUpdatePoste(true);
    }
    
    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutPoste bean = (YvsMutPoste) ev.getObject();
            populateView(UtilMut.buildBeanPoste(bean));
        }
    }
    
    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }
    
    public void showPrime() {
        //chare les primes du poste
        poste.getPrimes().clear();
        loadAllTypePrime();
        if ((posteSelect != null) ? posteSelect.getId() != 0 : false) {
            posteSelect.setPrimes(dao.loadNameQueries("YvsMutPrimePoste.findByPoste", new String[]{"poste"}, new Object[]{posteSelect}));
            cloneObject(poste, UtilMut.buildBeanPoste(posteSelect));
        }
        update("form_prime_poste");
        update("data_prime_poste");
    }
    
    public YvsMutPrimePoste buildPrimePoste(PrimePoste y) {
        YvsMutPrimePoste p = new YvsMutPrimePoste();
        if (y != null) {
            p.setId(y.getId());
            p.setMontant(y.getMontant());
            if ((y.getType() != null) ? y.getType().getId() != 0 : false) {
                p.setType(new YvsMutTypePrime(y.getType().getId(), y.getType().getDesignation()));
            }
            if ((posteSelect != null) ? posteSelect.getId() != 0 : false) {
                p.setPoste(new YvsMutPoste(posteSelect.getId()));
            }
            p.setAuthor(currentUser);
            p.setDateSave(y.getDateSave());
            p.setDateUpdate(new Date());
        }
        return p;
    }
    
    public PrimePoste recopieViewPrimePoste() {
        PrimePoste p = new PrimePoste();
        p.setId(prime.getId());
        p.setMontant(prime.getMontant());
        if ((prime.getType() != null) ? prime.getType().getId() != 0 : false) {
            int idx = types.indexOf(prime.getType());
            if (idx > -1) {
                prime.setType(types.get(idx));
            }
        }
        p.setType(prime.getType());
        return p;
    }
    
    public boolean controleFichePrimePoste(PrimePoste bean) {
        if ((bean.getType() != null) ? bean.getType().getId() == 0 : true) {
            getErrorMessage("Vous devez specifier le type");
            return false;
        }
        if (bean.getMontant() > bean.getType().getMontantMaximal()) {
            getErrorMessage("Le montant de la prime ne peut etre superieur au montant maximal du type");
            return false;
        }
        return true;
    }
    
    public void resetFichePrimePoste() {
        resetFiche(prime);
        prime.setType(new TypePrime());
        setUpdatePrime(false);
        tabIds_prime = "";
        input_reset_prime = "";
    }
    
    public void populateViewPrimePoste(PrimePoste bean) {
        cloneObject(prime, bean);
        setUpdatePrime(true);
    }
    
    public boolean saveNewPrimePoste() {
        if (input_reset_prime != null && input_reset_prime.equals("reset")) {
            setUpdatePrime(false);
            input_reset_prime = "";
        }
        String action = isUpdatePrime() ? "Modification" : "Insertion";
        try {
            PrimePoste bean = recopieViewPrimePoste();
            bean.setNew_(true);
            if (controleFichePrimePoste(bean)) {
                YvsMutPrimePoste entity = buildPrimePoste(bean);
                if (!isUpdatePrime()) {
                    entity = (YvsMutPrimePoste) dao.save1(entity);
                    bean.setId(entity.getId());
                    prime.setId(entity.getId());
                    poste.getPrimes().add(0, entity);
                    poste.setMontantPrime(poste.getMontantPrime() + entity.getMontant());
                } else {
                    dao.update(entity);
                    poste.getPrimes().set(poste.getPrimes().indexOf(entity), entity);
                    double montant = poste.getPrimes().get(poste.getPrimes().indexOf(entity)).getMontant();
                    poste.setMontantPrime(poste.getMontantPrime() + entity.getMontant() - montant);
                }
                int idx = postes.indexOf(new YvsMutPoste(poste.getId()));
                if (idx > -1) {
                    postes.get(idx).setMontantPrime(poste.getMontantPrime());
                    postes.get(idx).setPrimes(poste.getPrimes());
                }
                resetFichePrimePoste();
                succes();
                update("blog_form_prime_poste");
                update("data_prime_poste");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }
    
    public void deleteBeanPrimePoste() {
        try {
            if ((tabIds_prime != null) ? tabIds_prime.length() > 0 : false) {
                String[] ids = tabIds_prime.split("-");
                if ((ids != null) ? ids.length > 0 : false) {
                    for (String s : ids) {
                        long id = Integer.valueOf(s);
                        YvsMutPrimePoste bean = poste.getPrimes().get(poste.getPrimes().indexOf(new YvsMutPrimePoste(id)));
                        dao.delete(new YvsMutPrimePoste(bean.getId()));
                        poste.getPrimes().remove(bean);
                    }
                    resetFichePrimePoste();
                    succes();
                    update("data_prime_poste");
                }
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }
    
    public void deleteBeanPrimePoste(YvsMutPrimePoste y) {
        try {
            if (y != null) {
                dao.delete(y);
                poste.getPrimes().remove(y);
                int idx = postes.indexOf(new YvsMutPoste(poste.getId()));
                if (idx > -1) {
                    postes.get(idx).getPrimes().remove(y);
                }
                resetFiche();
                succes();
            }
        } catch (NumberFormatException ex) {
            getErrorMessage("Suppression Impossible !");
            System.err.println("error suppression gamme article : " + ex.getMessage());
        }
    }
    
    public void loadOnViewPrimePoste(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutPrimePoste bean = (YvsMutPrimePoste) ev.getObject();
            populateViewPrimePoste(UtilMut.buildBeanPrimePoste(bean));
            update("blog_form_prime_poste");
        }
    }
    
    public void unLoadOnViewPrimePoste(UnselectEvent ev) {
        resetFichePrimePoste();
        update("blog_form_prime_poste");
    }
    
    public YvsMutTypePrime buildTypePrime(TypePrime y) {
        YvsMutTypePrime t = new YvsMutTypePrime();
        if (y != null) {
            t.setDesignation(y.getDesignation());
            t.setId(y.getId());
            t.setMontantMaximal(y.getMontantMaximal());
            t.setNatureMontant(y.getNatureMontant());
            t.setMutuelle(currentMutuel);
            t.setAuthor(currentUser);
            t.setDateSave(y.getDateSave());
            t.setDateUpdate(new Date());
        }
        return t;
    }
    
    public TypePrime recopieViewTypePrime() {
        TypePrime t = new TypePrime();
        t.setDesignation(type.getDesignation());
        t.setId(type.getId());
        t.setMontantMaximal(type.getMontantMaximal());
        t.setNatureMontant(type.getNatureMontant());
        return t;
    }
    
    public boolean controleFiche(TypePrime bean) {
        if (bean.getDesignation() == null || bean.getDesignation().equals("")) {
            getErrorMessage("Vous devez entrer la designation");
            return false;
        }
        return true;
    }
    
    public void resetFicheTypePrime() {
        resetFiche(type);
    }
    
    public void saveNewTypePrime() {
        try {
            TypePrime bean = recopieViewTypePrime();
            bean.setNew_(true);
            if (controleFiche(bean)) {
                YvsMutTypePrime entyPoste = buildTypePrime(bean);
                entyPoste = (YvsMutTypePrime) dao.save1(entyPoste);
                bean.setId(entyPoste.getId());
                prime.setType(bean);
                types.add(0, bean);
                succes();
                resetFicheTypePrime();
                update("type_prime_poste");
                update("blog_form_type_prime_");
            }
        } catch (Exception ex) {
            System.err.println("Error Insertion : " + ex.getMessage());
            getErrorMessage("Insertion Impossible !");
        }
    }
    
    public void chooseNatureMontant() {
        if ((type != null) ? type.getNatureMontant() != null : false) {
            switch (type.getNatureMontant()) {
                case "Fixe":
                    type.setSuffixeMontant("Fcfa");
                    break;
                case "Pourcentage":
                    type.setSuffixeMontant("% Salaire");
                    break;
                default:
                    break;
            }
        }
    }
    
}
