/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.mutuelle.operation;

import static com.lowagie.text.pdf.PdfName.op;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.mutuelle.YvsMutCaisse;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.mutuelle.base.YvsMutTiers;
import yvs.entity.mutuelle.credit.YvsMutReglementCredit;
import yvs.entity.mutuelle.echellonage.YvsMutReglementMensualite;
import yvs.entity.mutuelle.operation.YvsMutAbonnementDepense;
import yvs.entity.mutuelle.operation.YvsMutMouvementCaisse;
import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import yvs.entity.param.YvsSocietes;
import yvs.mutuelle.CaisseMutuelle;
import yvs.mutuelle.Mutuelle;
import yvs.mutuelle.UtilMut;
import yvs.mutuelle.base.Tiers;
import yvs.grh.bean.Employe;
import yvs.mutuelle.credit.ReglementCreditMut;
import yvs.mutuelle.echellonage.ReglementMensualite;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.ParametreRequete;

/**
 *
 * @author lymytz
 */
@ManagedBean
@SessionScoped
public class ManagedOperationCaisse extends Managed<OperationCaisse, YvsMutMouvementCaisse> implements Serializable {

    @ManagedProperty(value = "#{operationCaisse}")
    private OperationCaisse operationCaisse;
    private YvsMutMouvementCaisse selectOperation;
    private Tiers source = new Tiers();

    private OperationCompte operationCompte = new OperationCompte();
    private ReglementCreditMut paiementCredit = new ReglementCreditMut();
    private ReglementMensualite mensualite = new ReglementMensualite();

    private List<YvsMutMouvementCaisse> operations;
    private List<YvsMutCaisse> caisses;
    private List<YvsMutTiers> tiers;
    private List<YvsGrhEmployes> employes;

    private String tabIds, input_reset;

    private boolean dateSearch;
    private Date debutSearch = new Date(), finSearch = new Date();
    private String typeSource, natureFind;
    private Long caisseFind;

    private String view;

    private List<OperationCaisse> abonnements;
    private long nbPeriode;
    private String typeVal = "T";
    private boolean allExo;
    private double valeur;

    private String operationMontant="=";
    private double montantSearch, montant2Search;

    public ManagedOperationCaisse() {
        caisses = new ArrayList<>();
        operations = new ArrayList<>();
        tiers = new ArrayList<>();
        employes = new ArrayList<>();
        abonnements = new ArrayList<>();
    }

    public String getTypeVal() {
        return typeVal;
    }

    public void setTypeVal(String typeVal) {
        this.typeVal = typeVal;
    }

    public boolean isAllExo() {
        return allExo;
    }

    public void setAllExo(boolean allExo) {
        this.allExo = allExo;
    }

    public String getTypeSource() {
        return typeSource;
    }

    public void setTypeSource(String typeSource) {
        this.typeSource = typeSource;
    }

    public boolean isDateSearch() {
        return dateSearch;
    }

    public void setDateSearch(boolean dateSearch) {
        this.dateSearch = dateSearch;
    }

    public Date getDebutSearch() {
        return debutSearch;
    }

    public void setDebutSearch(Date debutSearch) {
        this.debutSearch = debutSearch;
    }

    public Date getFinSearch() {
        return finSearch;
    }

    public void setFinSearch(Date finSearch) {
        this.finSearch = finSearch;
    }

    public List<YvsGrhEmployes> getEmployes() {
        return employes;
    }

    public void setEmployes(List<YvsGrhEmployes> employes) {
        this.employes = employes;
    }

    public Tiers getSource() {
        return source;
    }

    public void setSource(Tiers source) {
        this.source = source;
    }

    public List<YvsMutTiers> getTiers() {
        return tiers;
    }

    public void setTiers(List<YvsMutTiers> tiers) {
        this.tiers = tiers;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
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

    public OperationCaisse getOperationCaisse() {
        return operationCaisse;
    }

    public void setOperationCaisse(OperationCaisse operationCaisse) {
        this.operationCaisse = operationCaisse;
    }

    public List<YvsMutMouvementCaisse> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsMutMouvementCaisse> operations) {
        this.operations = operations;
    }

    public List<YvsMutCaisse> getCaisses() {
        return caisses;
    }

    public void setCaisses(List<YvsMutCaisse> caisses) {
        this.caisses = caisses;
    }

    public String getNatureFind() {
        return natureFind;
    }

    public void setNatureFind(String natureFind) {
        this.natureFind = natureFind;
    }

    public Long getCaisseFind() {
        return caisseFind;
    }

    public void setCaisseFind(Long caisseFind) {
        this.caisseFind = caisseFind;
    }

    public YvsMutMouvementCaisse getSelectOperation() {
        return selectOperation;
    }

    public void setSelectOperation(YvsMutMouvementCaisse selectOperation) {
        this.selectOperation = selectOperation;
    }

    public OperationCompte getOperationCompte() {
        return operationCompte;
    }

    public void setOperationCompte(OperationCompte operationCompte) {
        this.operationCompte = operationCompte;
    }

    public ReglementCreditMut getPaiementCredit() {
        return paiementCredit;
    }

    public void setPaiementCredit(ReglementCreditMut paiementCredit) {
        this.paiementCredit = paiementCredit;
    }

    public ReglementMensualite getMensualite() {
        return mensualite;
    }

    public void setMensualite(ReglementMensualite mensualite) {
        this.mensualite = mensualite;
    }

    public List<OperationCaisse> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<OperationCaisse> abonnements) {
        this.abonnements = abonnements;
    }

    public long getNbPeriode() {
        return nbPeriode;
    }

    public void setNbPeriode(long nbPeriode) {
        this.nbPeriode = nbPeriode;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public String getOperationMontant() {
        return operationMontant;
    }

    public void setOperationMontant(String operationMontant) {
        this.operationMontant = operationMontant;
    }

    public double getMontantSearch() {
        return montantSearch;
    }

    public void setMontantSearch(double montantSearch) {
        this.montantSearch = montantSearch;
    }

    public double getMontant2Search() {
        return montant2Search;
    }

    public void setMontant2Search(double montant2Search) {
        this.montant2Search = montant2Search;
    }

    @Override
    public void loadAll() {
        loadAllCaisse();
        loadAllOperation(true, true);
        loadAllTiers("Externe");
//        if (mutuelle < 1) {
//            mutuelle = currentMutuel != null ? (currentMutuel.getId() != null ? currentMutuel.getId() : 0) : 0;
//        }
    }

    public void loadAllCaisse() {
        caisses = dao.loadNameQueries("YvsMutCaisse.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public void loadAllEmploye() {
        employes = dao.loadNameQueries("YvsGrhEmployes.findByMutuelle", new String[]{"mutuelle"}, new Object[]{currentMutuel});
    }

    public void loadAllOperation(boolean avance, boolean init) {
        paginator.addParam(new ParametreRequete("y.caisse.mutuelle", "mutuelle", currentMutuel, "=", "AND"));
        operations = paginator.executeDynamicQuery("YvsMutMouvementCaisse", "y.dateMvt DESC", avance, init, (int) imax, dao);
        update("data_operation_caisse");
    }

    public void loadAllOperationByMutuelle(Mutuelle bean) {
        if (bean != null ? bean.getId() > 0 : false) {
//            mutuelle = bean.getId();
            loadAllOperation(true, true);
        }
    }

    public void paginer(boolean next) {
        loadAllOperation(next, false);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev); //To change body of generated methods, choose Tools | Templates.
        loadAllOperation(true, true);
    }

    public void loadAllTiers(String type) {
        tiers = dao.loadNameQueries("YvsMutTiers.findByMutuelleType", new String[]{"mutuelle", "typeTiers"}, new Object[]{currentMutuel, type});
    }

    public YvsMutMouvementCaisse buildOperationCaisse(OperationCaisse y) {
        YvsMutMouvementCaisse o = new YvsMutMouvementCaisse();
        if (y != null) {
            o.setDateMvt((y.getDateOperation() != null) ? y.getDateOperation() : new Date());
            o.setId(y.getId());
            o.setMontant(y.getMontant());
            o.setCaissier(currentUser.getUsers());
            o.setDateSave(y.getDateSave());
            o.setMouvement(y.getSensOperation());
            o.setNameTiers(y.getSource().getNom_Prenom());
            o.setNumero(y.getReferenceOperation());
            o.setReferenceExterne(y.getReferenceExterne());
            o.setStatutPiece(Constantes.STATUT_DOC_PAYER);
            o.setInSoldeCaisse(y.isInSoldeMutuelle());
            if (y.getSource().getId() > 0) {
                o.setTiersExterne(new YvsMutTiers(y.getSource().getId()));
            } else {
                o.setTiersExterne(null);
            }
            o.setTableExterne(null);
            if ((y.getCaisse() != null) ? y.getCaisse().getId() > 0 : false) {
                int idx = caisses.indexOf(new YvsMutCaisse(y.getCaisse().getId()));
                if (idx >= 0) {
                    o.setCaisse(caisses.get(idx));
                }
            }
            o.setAuthor(currentUser);
            o.setNote(y.getCommentaire());
        }
        return o;
    }

    @Override
    public OperationCaisse recopieView() {
        OperationCaisse o = new OperationCaisse();
        o.setDateOperation((operationCaisse.getDateOperation() != null) ? operationCaisse.getDateOperation() : new Date());
        o.setId(operationCaisse.getId());
        o.setMontant(operationCaisse.getMontant());
        o.setCommentaire(operationCaisse.getCommentaire());
        o.setHeureOperation((operationCaisse.getHeureOperation() != null) ? operationCaisse.getHeureOperation() : new Date());
//        o.setAutomatique(operationCaisse.isAutomatique(setSensOperation   o.setNature((opgetSensOperationisse.getNature() != nulgetSensOperationationCaisse.getNature() : "Depot");
        if ((operationCaisse.getCaisse() != null) ? operationCaisse.getCaisse().getId() != 0 : false) {
            operationCaisse.setCaisse(UtilMut.buildBeanCaisse((caisses.get(caisses.indexOf(new YvsMutCaisse(operationCaisse.getCaisse().getId()))))));
        }
        String name = currentMutuel.getDesignation();
        operationCaisse.getCaisse().setProprietaire(name);
        o.setCaisse(operationCaisse.getCaisse());
        o.setSource(operationCaisse.getSource());
        o.setAbonner(operationCaisse.isAbonner());
        o.setSensOperation(operationCaisse.getSensOperation());
        o.setInSoldeMutuelle(operationCaisse.isInSoldeMutuelle());
        o.setNew_(true);
        return o;
    }

    @Override
    public boolean controleFiche(OperationCaisse bean) {
        if ((bean.getCaisse() == null) ? true : bean.getCaisse().getId() <= 0) {
            getErrorMessage("Vous devez specifier la caisse !");
            return false;
        }
        if (bean.getMontant() <= 0) {
            getErrorMessage("Vous devez entrer un montant valide !");
            return false;
        }
        if (bean.getDateOperation() == null) {
            getErrorMessage("Vous devez entrer la date de l'opération!");
            return false;
        }
        if (bean.getCommentaire() == null) {
            getErrorMessage("Vous devez préciser une description pour cette transaction !");
            return false;
        }
        return true;
    }

    @Override
    public void resetFiche() {
        operationCaisse.setDateOperation(new Date());
        operationCaisse.setId(0);
        operationCaisse.setNature(null);
        operationCaisse.setSensOperation(Constantes.MUT_SENS_OP_RETRAIT);
        operationCaisse.setCaisse(new CaisseMutuelle());
        operationCaisse.setCommentaire("");
        operationCaisse.setHeureOperation(new Date());
        operationCaisse.setAutomatique(false);
        operationCaisse.setSource(new Tiers());
        operationCaisse.setMontant(0);
        operationCompte = new OperationCompte();
        paiementCredit = new ReglementCreditMut();
        mensualite = new ReglementMensualite();
        abonnements.clear();
        tabIds = "";
        input_reset = "";
        update("table_view_abonnement");
        update("blog_form_operation_caisse");
    }

    @Override
    public void populateView(OperationCaisse bean) {
        operationCaisse.setDateOperation((bean.getDateOperation() != null) ? bean.getDateOperation() : new Date());
        operationCaisse.setId(bean.getId());
        operationCaisse.setMontant(bean.getMontant());
        operationCaisse.setSensOperation(bean.getSensOperation());
        operationCaisse.setNature(bean.getNature());
        operationCaisse.setCaisse(bean.getCaisse());
        operationCaisse.setCommentaire(bean.getCommentaire());
        operationCaisse.setHeureOperation(bean.getHeureOperation());
        operationCaisse.setAutomatique(bean.isAutomatique());
        operationCaisse.setSource(bean.getSource());
        operationCaisse.setReferenceExterne(bean.getReferenceExterne());
        operationCaisse.setIdExterne(bean.getIdExterne());
        operationCaisse.setTableExterne(bean.getTableExterne());
        operationCompte = new OperationCompte();
        paiementCredit = new ReglementCreditMut();
        mensualite = new ReglementMensualite();
        if (bean.getIdExterne() > 0 && bean.getTableExterne() != null) {
            switch (bean.getTableExterne()) {
                case Constantes.MUT_TABLE_SRC_OPERATION_COMPTE:
                    YvsMutOperationCompte op = (YvsMutOperationCompte) dao.loadOneByNameQueries("YvsMutOperationCompte.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                    operationCompte = UtilMut.buildBeanOperationCompte(op);
                    break;
                case Constantes.MUT_TABLE_SRC_REGLEMENT_CREDIT:
                    YvsMutReglementCredit credit = (YvsMutReglementCredit) dao.loadOneByNameQueries("YvsMutReglementCredit.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                    paiementCredit = UtilMut.buildBeanReglementCredit(credit);
                    break;
                case Constantes.MUT_TABLE_SRC_REGLEMENT_MENSUALITE:
                    YvsMutReglementMensualite mens = (YvsMutReglementMensualite) dao.loadOneByNameQueries("YvsMutReglementMensualite.findById", new String[]{"id"}, new Object[]{bean.getIdExterne()});
                    mensualite = UtilMut.buildBeanReglementMensualite(mens);
                    break;
            }
        }
        update("blog_form_operation_caisse");
    }
    private boolean continu = false;

    @Override
    public boolean saveNew() {
        String action = operationCaisse.getId() > 0 ? "Modification" : "Insertion";
        try {
            OperationCaisse bean = recopieView();
            if (controleFiche(bean)) {
                if (bean.isAbonner() && operationCaisse.getId() <= 0 && !continu && bean.getSensOperation().equals(Constantes.MUT_SENS_OP_RETRAIT)) {
                    openDialog("dlgAbonner");
                    return false;
                }
                selectOperation = buildOperationCaisse(bean);
                selectOperation.setAuthor(currentUser);
                selectOperation.setDateUpdate(new Date());
                if (operationCaisse.getId() <= 0) {
                    selectOperation.setDateSave(new Date());
                    selectOperation.setId(null);
                    selectOperation = (YvsMutMouvementCaisse) dao.save1(selectOperation);
                    bean.setId(selectOperation.getId());
                    operationCaisse.setId(selectOperation.getId());
                    operations.add(0, selectOperation);
                } else {
                    dao.update(selectOperation);
                    int idx = operations.indexOf(selectOperation);
                    if (idx >= 0) {
                        operations.set(idx, selectOperation);
                    }
                }
                succes();
                actionOpenOrResetAfter(this);
                update("data_operation_caisse");
            }
        } catch (Exception ex) {
            getException("Error " + action + " : " + ex.getMessage(), ex);
            getErrorMessage(action + " Impossible !");
        }
        return true;
    }

    public void saveOperationsAbonne() {
        YvsMutAbonnementDepense ab;
        continu = true;
        Long nb = (Long) dao.loadObjectByNameQueries("YvsMutAbonnementDepense.countByMouvement", new String[]{"mouvement"}, new Object[]{selectOperation});
        if (nb != null ? nb <= 0 : true) {
            if (saveNew()) {
                for (OperationCaisse op : abonnements) {
                    ab = new YvsMutAbonnementDepense();
                    ab.setAuthor(currentUser);
                    ab.setDateRetrait(op.getDateOperation());
                    ab.setDateSave(new Date());
                    ab.setDateUpdate(new Date());
                    ab.setMouvementCaisse(selectOperation);
                    ab.setTypeVal(typeVal);
                    ab.setValeur(op.getMontant());
                    ab.setId(null);
                    dao.save1(ab);
                }
                continu = false;
            }
        } else {
            getErrorMessage("Cette ligne de dépense est déjà abonnée !");
        }
        update("data_operation_caisse");
    }

    public void deleteAbonnement() {
        if (operationCaisse.getId() > 0) {
            try {
                String rq = "DELETE FROM yvs_mut_abonnement_depense WHERE mouvement_caisse=?";
                dao.requeteLibre(rq, new Options[]{new Options(operationCaisse.getId(), 1)});
                abonnements.clear();
                update("table_view_abonnement");
            } catch (Exception ex) {
                getException("Suppression impossible !", ex);
                getErrorMessage("Suppression impossible !");
            }
        }
    }

    public void choixDureeAbonnement(ValueChangeEvent ev) {
        if (ev != null) {
            Boolean b = (Boolean) ev.getNewValue();
            if (b) {
                nbPeriode = (Long) dao.loadObjectByNameQueries("YvsMutPeriodeExercice.countPeriodeSuivant", new String[]{"date", "societe"}, new Object[]{operationCaisse.getDateOperation(), currentAgence.getSociete()});
                valeur = operationCaisse.getMontant() / nbPeriode;
            }
        }
    }

    @Override
    public void deleteBean() {
        //
    }

    public void deleteBeanOperation() {
        if ((selectOperation != null) ? (selectOperation.getId() != null) ? selectOperation.getId() > 0 : false : false) {
            if (selectOperation.getIdExterne() <= 0) {
                try {
                    dao.delete(selectOperation);
                    operations.remove(selectOperation);
                    resetFiche();
                    succes();
                    update("data_operation_caisse");
                    update("blog_form_operation_caisse");
                } catch (Exception ex) {
                    getErrorMessage("Suppression impossible !");
                    getException("Mutuelle error>>>", ex);
                }
            } else {
                getErrorMessage("Vous ne pouvez supprimer cette ligne de mouvement de caisse", "Veillez agir sur la ligne source");
            }
        } else {
            getErrorMessage("Aucune selection n'a été trouvé !");
        }
    }

    public void deleteBean(YvsMutMouvementCaisse y) {
        if (y != null) {
            selectOperation = y;
            populateView(UtilMut.buildBeanOperationCaisse(y));
            openDialog("dlgDelLine");
        }
    }

    @Override
    public void updateBean() {
//        
    }

    public void loadOnViewEmploye(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsGrhEmployes bean_ = (YvsGrhEmployes) ev.getObject();
            Employe bean = UtilMut.buildBeanEmploye(bean_);
            Tiers t = buildTiersByEmploye(bean);
            cloneObject(source, t);
            source.setEmploye(bean);
            source.setId(0);
            source.setIdExterne(bean.getId());
            operationCaisse.setSource(source);
            update("txt_nom_source_operation_caisse");
        }
    }

    public Tiers buildTiersByEmploye(Employe y) {
        Tiers t = new Tiers();
        if (y != null) {
            Employe e = y;
            if (e != null) {
                t.setAdresse(e.getVilleNaissance().getTitre());
                t.setNom(e.getNom());
                t.setPrenom(e.getPrenom());
                t.setIdExterne(y.getId());
            }
        }
        return t;
    }

    public void loadOnViewTiersExterne(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutTiers bean_ = (YvsMutTiers) ev.getObject();
            Tiers bean = UtilMut.buildBeanTiers(bean_);
            cloneObject(source, bean);
            operationCaisse.setSource(bean);
            update("txt_nom_source_operation_caisse");
        }
    }

    public void loadOnViewTiersSociete(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutTiers bean_ = (YvsMutTiers) ev.getObject();
            System.err.println(" ---- Tiers " + bean_.getNom());
            Tiers bean = UtilMut.buildBeanTiers(bean_);
            bean.setNom(bean.getRaisonSociale());
            cloneObject(source, bean);
            operationCaisse.setSource(bean);
            update("txt_nom_source_operation_caisse");
        }
    }

    public YvsMutTiers buildTiers(Tiers y) {
        YvsMutTiers t = new YvsMutTiers();
        if (y != null) {
            t.setAdresse(y.getAdresse());
            t.setId(y.getId());
            t.setNom(y.getNom());
            t.setPrenom(y.getPrenom());
            t.setRaisonSociale(y.getRaisonSociale());
            t.setTelephone(y.getTelephone());
            t.setTypeTiers(y.getTypeTiers());
            t.setIdExterne(y.getIdExterne());
            t.setMutuelle(currentMutuel);
            t.setActif(y.isActif());
            t.setDateSave(y.getDateSave());
        }
        return t;
    }

    public Tiers recopieViewTiers() {
        Tiers t = new Tiers();
        t.setAdresse(source.getAdresse());
        t.setId(source.getId());
        t.setNom(source.getNom());
        t.setPrenom(source.getPrenom());
        t.setRaisonSociale(source.getRaisonSociale());
        t.setTelephone(source.getTelephone());
        t.setTypeTiers(source.getTypeTiers());
        t.setIdExterne(source.getIdExterne());
        t.setActif(source.isActif());
        return t;
    }

    public boolean controleFicheTiers(Tiers bean) {
        if ((bean.getNom() == null || bean.getNom().equals("")) && (bean.getRaisonSociale() == null || bean.getRaisonSociale().equals(""))) {
            getErrorMessage("Vous devez entrer le nom");
            return false;
        }
        return true;
    }

    public void resetFicheTiers() {
        resetFiche(source);
        source.setEmploye(new Employe());
    }

    public Tiers saveNewtiers(String type, boolean succes) {
        Tiers bean = recopieViewTiers();
        if (controleFicheTiers(bean)) {
            bean.setNew_(true);
            bean.setTypeTiers(type);
            bean.setNom((bean.getNom() == null) ? bean.getRaisonSociale() : bean.getNom());
            bean.setRaisonSociale((bean.getRaisonSociale() != null) ? bean.getRaisonSociale() : bean.getNom());
            YvsMutTiers entity = buildTiers(bean);
            entity.setDateUpdate(new Date());
                    entity.setId(null);
            entity = (YvsMutTiers) dao.save1(entity);
            bean.setId(entity.getId());
            source.setId(entity.getId());
            tiers.add(0, entity);
            resetFicheTiers();
            if (succes) {
                succes();
            }
            return bean;
        }
        return null;
    }

    public void importTiersSociete() {
        List<YvsSocietes> l = dao.loadNameQueries("YvsSocietes.findAll", new String[]{}, new Object[]{});
        for (YvsSocietes s : l) {
            YvsMutTiers mt = new YvsMutTiers();
            mt.setAdresse(s.getAdressSiege());
            mt.setIdExterne(s.getId());
            mt.setMutuelle(currentMutuel);
            mt.setNom(s.getName());
            mt.setRaisonSociale(s.getName());
            mt.setTelephone(s.getTel());
            mt.setTypeTiers("Societe");
            mt.setAuthor(currentUser);
            mt.setDateSave(new Date());
            mt.setDateUpdate(new Date());
                    mt.setId(null);
            mt = (YvsMutTiers) dao.save1(mt);
            mt.setNew_(true);
            tiers.add(mt);
        }
        succes();
        update("data_tiers_societe");
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsMutMouvementCaisse bean = (YvsMutMouvementCaisse) ev.getObject();
            populateView(UtilMut.buildBeanOperationCaisse(bean));
            selectOperation = bean;
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
        update("blog_form_operation_caisse");
    }

    public void openDeleteTiers(YvsMutTiers tiers, boolean del) {
        Tiers t = UtilMut.buildBeanTiers(tiers);
        cloneObject(source, t);
        if (del) {
            openDialog("");
        }
        update("form_create_tiers_societe");
    }

    public void deleteTiers() {
        if (source.getId() > 0) {
            YvsMutTiers mt = new YvsMutTiers(source.getId());
            dao.delete(mt);
            succes();
            tiers.remove(mt);
            update("data_tiers_societe");
        }
    }

    public void chooseNatureOp(ValueChangeEvent ev) {
        String str = (String) ev.getNewValue();
        if (!str.equals(Constantes.MUT_SENS_OP_RETRAIT)) {
            operationCaisse.setAbonner(false);
        }
    }

    public void chooseSource(ValueChangeEvent ev) {
        if (ev != null) {
            String str = (String) ev.getNewValue();
            switch (str) {
                case "Employe":
                    openDialog("dlgEmploye");
                    break;
                case "Societe":
                    openDialog("dlgTiersSociete");
                    break;
            }
        }
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateMvt", "dates", null, "BETWEEN", "AND");
        if (dateSearch) {
            p = new ParametreRequete("y.dateMvt", "dates", debutSearch, " BETWEEN ", "AND");
            p.setOtherObjet(finSearch);
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void addParamTypeSource(String nameTiers) {
        ParametreRequete p = new ParametreRequete("UPPER(y.nameTiers)", "typeTiers", null, "=", "AND");
        if (nameTiers != null ? nameTiers.trim().length() > 0 : false) {
            p.setObjet(nameTiers.trim().toUpperCase());
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void addParamMontant() {
        ParametreRequete p = new ParametreRequete("y.montant", "montant", null, operationMontant, "AND");
        if (montantSearch > 0 || montant2Search > 0) {
            if (!operationMontant.equals("BETWEEN")) {
                p.setObjet(montantSearch);
            } else {
                p.setObjet(montantSearch);
                p.setOtherObjet(montant2Search);
            }
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void addParamCaisse(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.caisse", "caisse", null, "=", "AND");
        if (ev.getNewValue() != null ? (long) ev.getNewValue() > 0 : false) {
            p.setObjet(new YvsMutCaisse((Long) ev.getNewValue()));
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public void changeOperateur(ValueChangeEvent ev) {
        operationMontant = (String) ev.getNewValue();
        addParamMontant();
    }

    public void addParamNature(ValueChangeEvent ev) {
        ParametreRequete p = new ParametreRequete("y.mouvement", "mouvement", null, "=", "AND");
        if (ev.getNewValue() != null) {
            p.setObjet((String) ev.getNewValue());
        }
        paginator.addParam(p);
        loadAllOperation(true, true);
    }

    public double findSoldeCaisse(long idCaisse) {
        return dao.getSoldeCaisseMut(idCaisse, null, Constantes.STATUT_DOC_PAYER);
    }

    public void applyAbonnement() {
        abonnements.clear();
        double reste = (!typeVal.equals("T")) ? operationCaisse.getMontant() : 100;
        double m = arrondi(reste / ((nbPeriode > 0) ? nbPeriode : 1));
        Calendar now = Calendar.getInstance();
        now.setTime(operationCaisse.getDateOperation());
        OperationCaisse op;
        for (int i = 1; i <= nbPeriode; i++) {
            op = new OperationCaisse(-i);
            op.setDateOperation(now.getTime());
            if (reste >= m) {
                op.setMontant(m);
            } else {
                op.setMontant(reste);
            }
            reste = reste - m;
            now.add(Calendar.MONTH, 1);
            abonnements.add(op);
        }
        if (reste > 0) {
            op = new OperationCaisse(-nbPeriode++);
            op.setDateOperation(now.getTime());
            op.setMontant(arrondi(reste));
            abonnements.add(op);
        }

    }

    public void openManagedAbonnement(YvsMutMouvementCaisse mvt) {
        abonnements.clear();
        List<YvsMutAbonnementDepense> l = dao.loadNameQueries("YvsMutAbonnementDepense.findByMouvement", new String[]{"mouvement"}, new Object[]{mvt});
        selectOperation = mvt;
        populateView(UtilMut.buildBeanOperationCaisse(selectOperation));
        OperationCaisse op;
        for (YvsMutAbonnementDepense ab : l) {
            op = new OperationCaisse(ab.getId());
            op.setDateOperation(ab.getDateRetrait());
            op.setMontant(ab.getValeur());
            op.setNature(ab.getTypeVal());
            abonnements.add(op);
        }
        openDialog("dlgAbonner");
        update("table_view_abonnement");
    }
}
