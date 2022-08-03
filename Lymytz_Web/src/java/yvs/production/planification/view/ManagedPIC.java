/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;
import yvs.entity.compta.YvsBaseCaisse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.produits.FamilleArticle;
import yvs.production.UtilProd;
import yvs.entity.production.base.YvsBaseTypeCoefficient;
import yvs.entity.production.base.YvsProdCoefficientVariationPic;
import yvs.entity.production.planification.YvsProdDetailPic;
import yvs.entity.production.planification.YvsProdPeriodePlan;
import yvs.entity.production.planification.YvsProdPlanification;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.production.planification.PeriodePlanification;
import yvs.production.planification.Planification;
import yvs.produits.ajustement.Lineaire;
import yvs.util.Constantes;
import yvs.util.Managed;
import yvs.util.PaginatorResult;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class ManagedPIC extends Managed<Planification, YvsBaseCaisse> implements Serializable {

    private List<FamilleProduction> listDataFamille;
    private List<FamilleProduction> listDataHorizonP = new ArrayList<>(); //donné de vente de l'horizon précédent
    private List<PeriodePlanification> headers;
    private List<PeriodePlanification> headersP; //liste des périodes de l'horizon opposé
    private boolean displayPdp;
    @ManagedProperty(value = "#{planification}")
    private Planification planification;
    private List<Planification> listPlanification, listPlanification_;
    private boolean updatePic;

    private List<CoordoneeTablePic> coordonnees;

    public ManagedPIC() {
        headers = new ArrayList<>();
        headersP = new ArrayList<>();
        listDataFamille = new ArrayList<>();
        listFamille = new ArrayList<>();
        listFamille_ = new ArrayList<>();
        listTypeCoeficient = new ArrayList<>();
        listCoeficient = new ArrayList<>();
        listPlanification = new ArrayList<>();
        listPlanification_ = new ArrayList<>();
        coordonnees = new ArrayList<>();
    }

    public List<CoordoneeTablePic> getCoordonnees() {
        return coordonnees;
    }

    public void setCoordonnees(List<CoordoneeTablePic> coordonnees) {
        this.coordonnees = coordonnees;
    }

    public List<PeriodePlanification> getHeaders() {
        return headers;
    }

    public void setHeaders(List<PeriodePlanification> headers) {
        this.headers = headers;
    }

    public List<FamilleProduction> getListDataFamille() {
        return listDataFamille;
    }

    public void setListDataFamille(List<FamilleProduction> listDataFamille) {
        this.listDataFamille = listDataFamille;
    }

    public boolean isDisplayPdp() {
        return displayPdp;
    }

    public void setDisplayPdp(boolean displayPdp) {
        this.displayPdp = displayPdp;
    }

    public Planification getPlanification() {
        return planification;
    }

    public void setPlanification(Planification planification) {
        this.planification = planification;
    }

    public List<PeriodePlanification> getHeadersP() {
        return headersP;
    }

    public void setHeadersP(List<PeriodePlanification> headersP) {
        this.headersP = headersP;
    }

    public List<Planification> getListPlanification() {
        return listPlanification;
    }

    public void setListPlanification(List<Planification> listPlanification) {
        this.listPlanification = listPlanification;
    }

    @Override
    public void update(String id) {
        super.update(id); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isUpdatePic() {
        return updatePic;
    }

    public void setUpdatePic(boolean updatePic) {
        this.updatePic = updatePic;
    }

    /**
     * *
     * Début planification *
     */
    private List<FamilleArticle> listFamille, listFamille_;
    private String chaineSelect;
    private String chaineSelectFamille;
    private String searchFamille;
    private boolean disNextFamille, disPrevFamille;
    private Date debutHorizonPrecedent; //arde la date de début del'horizon oposé à celui de l'actuel PIC.
    private CoeficientPlanif newCoeficient = new CoeficientPlanif();
    private TypeCoeficient newTypeCoef = new TypeCoeficient();
    private List<CoeficientPlanif> listCoeficient;
    private List<TypeCoeficient> listTypeCoeficient;

    public String getChaineSelect() {
        return chaineSelect;
    }

    public void setChaineSelect(String chaineSelect) {
        this.chaineSelect = chaineSelect;
    }

    public List<FamilleArticle> getListFamille() {
        return listFamille;
    }

    public void setListFamille(List<FamilleArticle> listFamille) {
        this.listFamille = listFamille;
    }

    public List<FamilleArticle> getListFamille_() {
        return listFamille_;
    }

    public void setListFamille_(List<FamilleArticle> listFamille_) {
        this.listFamille_ = listFamille_;
    }

    public String getChaineSelectFamille() {
        return chaineSelectFamille;
    }

    public void setChaineSelectFamille(String chaineSelectFamille) {
        this.chaineSelectFamille = chaineSelectFamille;
    }

    public boolean isDisNextFamille() {
        return disNextFamille;
    }

    public void setDisNextFamille(boolean disNextFamille) {
        this.disNextFamille = disNextFamille;
    }

    public boolean isDisPrevFamille() {
        return disPrevFamille;
    }

    public void setDisPrevFamille(boolean disPrevFamille) {
        this.disPrevFamille = disPrevFamille;
    }

    public CoeficientPlanif getNewCoeficient() {
        return newCoeficient;
    }

    public void setNewCoeficient(CoeficientPlanif newCoeficient) {
        this.newCoeficient = newCoeficient;
    }

    public List<CoeficientPlanif> getListCoeficient() {
        return listCoeficient;
    }

    public void setListCoeficient(List<CoeficientPlanif> listCoeficient) {
        this.listCoeficient = listCoeficient;
    }

    public List<TypeCoeficient> getListTypeCoeficient() {
        return listTypeCoeficient;
    }

    public void setListTypeCoeficient(List<TypeCoeficient> listTypeCoeficient) {
        this.listTypeCoeficient = listTypeCoeficient;
    }

    public TypeCoeficient getNewTypeCoef() {
        return newTypeCoef;
    }

    public void setNewTypeCoef(TypeCoeficient newTypeCoef) {
        this.newTypeCoef = newTypeCoef;
    }

    public void loadAllFamille(boolean avancer, boolean init) {
        listFamille.clear();
        listFamille_.clear();
        nameQueriCount = "YvsBaseFamilleArticle.findCountAll";
        nameQueri = "YvsBaseFamilleArticle.findAll";
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        listFamille = UtilProd.buildBeanListFamilleArticle(loadFamilleArt(avancer, init));
        listFamille_.addAll(listFamille);
    }

    private List<YvsBaseFamilleArticle> loadFamilleArt(boolean avancer, boolean init) {
        if (!init) {
            if (avancer) {
                setFirtResult(getFirtResult() + getNbMax());
            } else {
                setFirtResult(getFirtResult() - getNbMax());
            }
            if (getTotalPage() == getCurrentPage()) {
                setFirtResult(0);
            }
        } else {
            setFirtResult(0);
        }
        PaginatorResult<YvsBaseFamilleArticle> pa = new PaginatorResult<>();
        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
        setDisNextFamille(pa.isDisNext());
        setDisPrevFamille(pa.isDisPrev());
        setTotalPage(pa.getNbPage());
        setCurrentPage(pa.getCurrentPage());
        return pa.getResult();
    }

    public double givePrevVenteByPeriode(FamilleArticle famille, PeriodePlanification periode) {
        return (Math.round(Math.abs(new Random().nextInt(100000))));
    }

    public void calculPic() {
        //calcul le calendrier du pic en fonction de l'horizon
        calculPeriode();
        //récupère les données de vente sur l'horizon précédent par famille et par periode. on va basé le calcul prévisionnel la dessus      
        //on se sert des familles et des périodes pour calculer les prévisions de vente et de production  
        loadDataHorizonP(buildFamille(), headersP);
        loadDataPrevisonnel();

    }

    public void callCalculPeriode(SelectEvent ev) {
        planification.setDateDebut((Date) ev.getObject());
        calculPeriode();
    }

    private boolean controleForm() {
        if (planification.getHorizon() <= 0) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer l'horizon");
            return false;
        }
        if (planification.getAmplitude() <= 0) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer l'amplitude");
            return false;
        }
        if (planification.getPeriode() == null) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer la période");
            return false;
        }
        if (planification.getDateDebut() == null) {
            getErrorMessage("Formulaire incorrecte", "vous devez indiquer la date de début de la planification");
            return false;
        }
        return true;
    }

    private void calculPeriode() {
        if (controleForm()) {
            PeriodePlanification p, pp;
            Calendar cal = Calendar.getInstance();
            cal.setTime(planification.getDateDebut());
            //calcul le début de l'horizon précédent: la fin de l'horizon précédent est le début de l'horizon en cours
            Calendar chp = Calendar.getInstance();
            chp.setTime(planification.getDateDebut());
            int periode = 0, coef = 0;
            switch (planification.getPeriode()) {
                case "Trimestre":
                    //le nombre de moi de l'horizon / 
                    cal.add(Calendar.MONTH, planification.getHorizon() * 3);
                    chp.add(Calendar.MONTH, -planification.getHorizon() * 3);
                    debutHorizonPrecedent = chp.getTime();
                    planification.setDateFin(cal.getTime());
                    periode = Calendar.MONTH;
                    coef = 3;
                    break;
                case "Mois":
                    cal.add(Calendar.MONTH, planification.getHorizon());
                    planification.setDateFin(cal.getTime());
                    periode = Calendar.MONTH;
                    System.err.println("---- Periode " + periode);
                    chp.add(Calendar.MONTH, -planification.getHorizon());
                    debutHorizonPrecedent = chp.getTime();
                    coef = 1;
                    break;
                case "Semaine":
                    cal.add(Calendar.WEEK_OF_YEAR, planification.getHorizon());
                    planification.setDateFin(cal.getTime());
                    chp.add(Calendar.WEEK_OF_YEAR, -planification.getHorizon());
                    debutHorizonPrecedent = chp.getTime();
                    periode = Calendar.WEEK_OF_YEAR;
                    coef = 1;
                    break;
                default:
                    break;
            }
            headers.clear();
            headersP.clear();
            //si l'amplitude =1 et la période est le Mois:
//            if (planification.getPeriode().equals("Mois") && planification.getAmplitude() == 1) {
//                buildMonthOfYear(chp);
//                return;
//            }
            cal.setTime(planification.getDateDebut());
            chp.setTime(planification.getDateDebut());
            chp.add(periode, -(coef * planification.getAmplitude()));
            for (int i = 1; i <= planification.getHorizon(); i = i + planification.getAmplitude()) {
                p = new PeriodePlanification();
                p.setDateDebut(cal.getTime());
                if (!planification.getPeriode().equals("Semaine")) {
                    p.setReference(formatMonthYear.format(p.getDateDebut()));
                } else {
                    p.setReference(formatMonth_.format(p.getDateDebut()));
                }
                cal.add(periode, coef * planification.getAmplitude());
                p.setDateFin(cal.getTime());
                p.setId(i);
                headers.add(p);
                pp = new PeriodePlanification(p);
                pp.setDateDebut(chp.getTime());
                if (!planification.getPeriode().equals("Semaine")) {
                    pp.setReference(formatMonthYear.format(pp.getDateDebut()));
                } else {
                    pp.setReference(formatMonth_.format(pp.getDateDebut()));
                }
                chp.add(periode, -(coef * planification.getAmplitude()));
                pp.setDateFin(chp.getTime());
                headersP.add(0, pp);
            }

        }
    }

//    private void buildMonthOfYear(Calendar horizoonP) {
//        PeriodePlanification p;
//        Calendar c = Calendar.getInstance();
//        c.setTime(planification.getDateDebut());
//        int numMois;
//        for (int i = 1; i <= planification.getHorizon(); i++) {
//            p = new PeriodePlanification();
//            p.setDateDebut(c.getTime());
//            c.add(Calendar.MONTH, 1);
//            p.setDateFin(c.getTime());
//            p.setIndicatif(i);
//            numMois = c.get(Calendar.MONTH);
//            switch (numMois) {
//                case 0:
//                    p.setReference("Janv.");
//                    break;
//                case 1:
//                    p.setReference("Fev.");
//                    break;
//                case 2:
//                    p.setReference("Mar.");
//                    break;
//                case 3:
//                    p.setReference("Avr.");
//                    break;
//                case 4:
//                    p.setReference("Mai.");
//                    break;
//                case 5:
//                    p.setReference("Jui.");
//                    break;
//                case 6:
//                    p.setReference("Juil.");
//                    break;
//                case 7:
//                    p.setReference("Aou.");
//                    break;
//                case 8:
//                    p.setReference("Sep.");
//                    break;
//                case 9:
//                    p.setReference("Oct.");
//                    break;
//                case 10:
//                    p.setReference("Nov.");
//                    break;
//                case 11:
//                    p.setReference("Dec.");
//                    break;
//                default:
//                    numMois = 0;
//                    break;
//            }
//            headers.add(p);
//            p.setDateDebut(horizoonP.getTime());
//            horizoonP.add(Calendar.MONTH, 1);
//            p.setDateFin(horizoonP.getTime());
//            headersP.add(p);
//        }
//    }
    public List<FamilleProduction> getListDataHorizonP() {
        return listDataHorizonP;
    }

    public void setListDataHorizonP(List<FamilleProduction> listDataHorizonP) {
        this.listDataHorizonP = listDataHorizonP;
    }

    private void loadDataHorizonP(List<FamilleArticle> listF, List<PeriodePlanification> listPeriode) {
        //pour chaque  famille et à chaque période,        
        FamilleProduction ligne;
        listDataHorizonP.clear();
        List<DataColone2D> l;
        for (FamilleArticle f : listF) {
            ligne = new FamilleProduction();
            l = ligne.getFabriqueColone().getColone("2D");
            l.addAll(getDataHorizon(f, listPeriode));
            ligne.setColones(l);
            ligne.setFamille(f);
            listDataHorizonP.add(ligne);
        }
    }

    private ObjectData getOneData(FamilleArticle fa, PeriodePlanification p) {
        ObjectData obj = new ObjectData();
        obj.setId_(fa.getId() + "_" + p.getIndicatif());
        obj.setValue(Math.round(Math.abs(new Random().nextInt(100000))));
        return obj;
    }

    private List<DataColone2D> getDataHorizon(FamilleArticle fa, List<PeriodePlanification> lp) {
        List<DataColone2D> result = new ArrayList<>();
        DataColone2D col;
        for (PeriodePlanification p : lp) {
            col = new DataColone2D();
            col.setData(getOneData(fa, p));
            result.add(col);
        }
        return result;
    }

    //cette méthode doit parcourir la liste des périodes et alimenter pour chaque période trois liste (vente, stock, production)
    private void getDataPrevisionVente(FamilleProduction fa, List<PeriodePlanification> lp) {
        listVente.clear();
        listStock.clear();
        listProduction.clear();
        ObjectData data;
        List<DataColone2D> l = new ArrayList<>(fa.getColones()); //liste des ventes sur les périodes de l'horizon h_
        int numPeriode = 1;
        for (PeriodePlanification p : lp) {
            //prévision de vente:              
            switch (planification.getMethode()) {
                case 1: //points extremes
                    double x1 = 1,
                     x2 = headersP.size(),
                     y1 = l.get(0).getData().getValue(),
                     y2 = l.get(l.size() - 1).getData().getValue(),
                     x = headersP.size() + numPeriode;
                    double vente = Math.round(Math.abs(Lineaire.givePrevisionY(y1, y2, x1, x2, x)));
                    data = new ObjectData(vente, "V-" + fa.getFamille().getId() + "_" + numPeriode);
                    data.setMontantBase(vente);
                    listVente.add(new DataColone2D(data));
                    double stock = giveStock(fa.getFamille(), p);
                    listStock.add(new DataColone2D(new ObjectData(stock, "S-" + fa.getFamille().getId() + "_" + numPeriode)));
                    listProduction.add(new DataColone2D(new ObjectData(((vente - stock) > 0) ? vente - stock : 0, "P-" + fa.getFamille().getId() + "_" + numPeriode)));
                    //ajoute la nouvelle prévision à la liste des prévisions
                    l.add(new DataColone2D(new ObjectData(vente, "V-" + fa.getFamille().getId() + "_" + numPeriode)));
                    break;
                case 2: //Moindre carré
                    break;
                case 3: //point moyen ou méthode de meyer
                    break;
                case 4: //variation saisonière
                    break;
                default:
                    break;
            }
            numPeriode++;
        }

    }

    private List<DataColone2D> listVente = new ArrayList<>();
    private List<DataColone2D> listStock = new ArrayList<>();
    private List<DataColone2D> listProduction = new ArrayList<>();

    private List<PeriodePlanification> periodePrevision;

    private void loadDataPrevisonnel() {
        periodePrevision = new ArrayList<>();
        periodePrevision.addAll(headers);
        periodePrevision.addAll(headersP);
        FamilleProduction ligne = new FamilleProduction();
        DataLigne2D ligne2D_1, ligne2D_2, ligne2D_3;
        List<DataLigne2D> l = ligne.getFabriqueColone().getColone("L2D");
        listDataFamille.clear();
        for (FamilleProduction f : listDataHorizonP) { //on retrouve lors de ce parcours les famille et le vente précédente par période
            //fabriquer la liste dataFamille. chaque famille d'article contient 3 listes de colonnes: 1=Vente, 2= stock, 3= Production
            ligne = new FamilleProduction();
            ligne2D_1 = new DataLigne2D("Ventes");
            ligne2D_2 = new DataLigne2D("Stocks");
            ligne2D_3 = new DataLigne2D("Productions");
            getDataPrevisionVente(f, headers);  //charge les données prévisionnel
            ligne2D_1.getListData().addAll(listVente);
            ligne2D_2.getListData().addAll(listStock);
            ligne2D_3.getListData().addAll(listProduction);
            l.add(ligne2D_1);
            l.add(ligne2D_2);
            l.add(ligne2D_3);
            ligne.setColones(l);
            ligne.setFamille(f.getFamille());
            listDataFamille.add(ligne);
//             System.err.println("---- " + ((DataLigne2D)(ligne.getColones().get(0))).getLabelDetail());
//             System.err.println(listVente.size()+"  ---- " + ((DataLigne2D)(ligne.getColones().get(0))).getListData().size());
            l = ligne.getFabriqueColone().getColone("L2D");
        }
    }

    private double giveStock(FamilleArticle f, PeriodePlanification p) {
        //calcul le stock des produits de la famille donnée en paramètre
        return 100;
    }

    private List<FamilleArticle> buildFamille() {
        List<FamilleArticle> lf = new ArrayList<>();
        if (chaineSelectFamille != null) {
            String numroLine[] = chaineSelectFamille.split("-");
            int index;
            try {
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                    lf.add(listFamille.get(index));
                }
            } catch (IllegalArgumentException ex) {
                getErrorMessage("Erreur !");
            }
            chaineSelectFamille = "";
        }
        return lf;
    }

    public void chooseOneDataPic(int x, int y, int z) {
        listCoeficient.clear();
        if (listCoeficient.isEmpty()) {
            FamilleProduction f = listDataFamille.get(x);
            DataLigne2D type = (DataLigne2D) f.getColones().get(y);
            DataColone2D periode = type.getListData().get(z);
            listCoeficient.addAll(periode.getData().getListCoeficient());
        }

    }
    /*Gérer les types de coefficients et les coeficient*/

    public void addTypeCoeficient() {
        if (newTypeCoef.getLibele() != null) {
            YvsBaseTypeCoefficient en = new YvsBaseTypeCoefficient();
            en.setLibelle(newTypeCoef.getLibele());
            en.setDescription(newTypeCoef.getDescription());
            en.setSociete(currentAgence.getSociete());
            en.setAuthor(currentUser);
            en.setId(null);
            en = (YvsBaseTypeCoefficient) dao.save1(en);
            newTypeCoef.setId(en.getId());
            listTypeCoeficient.add(newTypeCoef);
            newTypeCoef = new TypeCoeficient();
        } else {
            getErrorMessage("Veuillez indiquer la valeur du libelé !");
        }
    }

    public void loadAllTypeCoef() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        List<YvsBaseTypeCoefficient> l = dao.loadNameQueries("YvsBaseTypeCoefficient.findAll", champ, val);
        TypeCoeficient tc;
        listTypeCoeficient.clear();
        for (YvsBaseTypeCoefficient bt : l) {
            tc = new TypeCoeficient(bt.getId(), bt.getLibelle());
            tc.setDescription(bt.getDescription());
            listTypeCoeficient.add(tc);
        }

    }

    public void addNewCoeficientToPic() {
        if (newCoeficient.getType().getId() != 0 && newCoeficient.getValue() != 0) {
            newCoeficient.setType(listTypeCoeficient.get(listTypeCoeficient.indexOf(newCoeficient.getType())));
            listCoeficient.add(newCoeficient);
            newCoeficient = new CoeficientPlanif();
        } else {
            if (newCoeficient.getType().getId() == 0) {
                getErrorMessage("Formulaire incorrect !", "Veuillez choisir le type de coeficient");
            } else {
                getErrorMessage("Formulaire incorrect !", "Veuillez indiquer une valeur");
            }
        }
    }

    private List<Long> listCoefDelete = new ArrayList<>();

    public void removeCoefToList(int numLine) {
        CoeficientPlanif c = listCoeficient.get(numLine);
        if (c.getId() != 0) {
            listCoefDelete.add(c.getId());
        }
        if (c.isApply()) {
            //on rétablit le coefficient sur la valeur
            String[] tab = chaineSelect.trim().split("-");
            int idxFam = 0, idxTyp = 0, idxPeriod = 0;
            if (tab != null) {
                if (tab.length != 3) {
                    getErrorMessage("Votre selection est ambigue !");
                } else {
                    idxFam = Integer.parseInt(tab[0]);
                    idxTyp = Integer.parseInt(tab[1]);
                    idxPeriod = Integer.parseInt(tab[2]);
                    FamilleProduction f = listDataFamille.get(idxFam);
                    DataLigne2D section = (DataLigne2D) f.getColones().get(idxTyp);
                    DataColone2D periode = section.getListData().get(idxPeriod);
                    double valeur = periode.getData().getValue();
                    if (!c.isApply()) {
                        valeur = valeur - (valeur * c.getValue() / 100);
                        c.setApply(true);
                    }
                    periode.getData().getListCoeficient().remove(c);
                    periode.getData().setValue(valeur);
                }
            }
        }
        listCoeficient.remove(numLine);
    }

    public void bindListCoefToDetails() {
        if (chaineSelect != null) {
            String[] tab = chaineSelect.trim().split("-");
            int idxFam = 0, idxTyp = 0, idxPeriod = 0;
            if (tab != null) {
                if (tab.length != 3) {
                    getErrorMessage("Votre selection est ambigue !");
                } else {
                    idxFam = Integer.parseInt(tab[0]);
                    idxTyp = Integer.parseInt(tab[1]);
                    idxPeriod = Integer.parseInt(tab[2]);
                    if (!listCoeficient.isEmpty()) {
                        FamilleProduction f = listDataFamille.get(idxFam);
                        DataLigne2D section = (DataLigne2D) f.getColones().get(idxTyp);
                        DataColone2D periode = section.getListData().get(idxPeriod);
                        double valeur = periode.getData().getMontantBase();
                        double soeCoef = 0, soeVal = 0;
                        for (CoeficientPlanif c : listCoeficient) {
                            if (!c.isApply()) {
                                c.setApply(true);
                                periode.getData().getListCoeficient().add(c);
                            }
                            if (c.isPercent()) {
                                soeCoef += c.getValue();
                            } else {
                                soeVal += c.getValue();
                            }
                        }
                        valeur = valeur + (valeur * soeCoef / 100) + soeVal;
                        periode.getData().setValue(valeur);
                    }
                    CoordoneeTablePic c = new CoordoneeTablePic(idxFam, idxTyp, idxPeriod);
                    if (!coordonnees.contains(c)) {
                        coordonnees.add(c);
                    }
                }
            }
        }
    }

    private long saveNewPic(boolean suivre) {
        YvsProdPlanification planif = UtilProd.buildPlanification(planification);
        if (suivre) {
            planif.setPlanAJour(new YvsProdPlanification(planification.getId()));
        } else {
            planif.setId(null);
        }
        planif.setTypePlan("PIC");
        planif.setSociete(currentAgence.getSociete());
        planif.setAuthor(currentUser);
        planif.setId(null);
        planif = (YvsProdPlanification) dao.save1(planif);
        planification.setId(planif.getId());
        Planification new_ = new Planification();
        cloneObject(new_, planification);
        listPlanification.add(0, new_);
        //save période
        YvsProdPeriodePlan period;
        int cptP = 0; //compteur période
        YvsProdDetailPic vent;
        DataLigne2D vente;
        DataLigne2D stock;
        DataLigne2D production;
        for (PeriodePlanification pe : headers) {
            //sauvegarde la période
            pe.setPlan(planification);
            period = UtilProd.buildPeriodePlan(pe);
            period.setId(null);
            period.setAuthor(currentUser);
            period = (YvsProdPeriodePlan) dao.save1(period);
            pe.setId(period.getId());
            for (FamilleProduction f : listDataFamille) {
                vente = (DataLigne2D) f.getColones().get(0);
                stock = (DataLigne2D) f.getColones().get(1);
                production = (DataLigne2D) f.getColones().get(2);
                vent = UtilProd.buildDataPic(vente.getListData().get(cptP).getData(), new YvsProdPeriodePlan(pe.getId()), buildFamille(f));
                vent.setId(null);
                vent.setTypeVal(Constantes.PROD_VENTE);
                vent.setAuthor(currentUser);
                vent = (YvsProdDetailPic) dao.save1(vent);
                //Enregistre les coefficients pour la vente
                saveCoefficient(vente.getListData().get(cptP).getData().getListCoeficient(), vent);
                vente.getListData().get(cptP).getData().setId(vent.getId());
                YvsProdDetailPic stoc = UtilProd.buildDataPic(stock.getListData().get(cptP).getData(), new YvsProdPeriodePlan(pe.getId()), buildFamille(f));
                stoc.setId(null);
                stoc.setTypeVal(Constantes.PROD_STOCK);
                stoc.setAuthor(currentUser);
                stoc = (YvsProdDetailPic) dao.save1(stoc);
                //Enregistre les coefficients pour le stock
                saveCoefficient(stock.getListData().get(cptP).getData().getListCoeficient(), stoc);
                stock.getListData().get(cptP).getData().setId_("S_" + stoc.getId());
                YvsProdDetailPic prod = UtilProd.buildDataPic(production.getListData().get(cptP).getData(), new YvsProdPeriodePlan(pe.getId()), buildFamille(f));
                prod.setId(null);
                prod.setTypeVal(Constantes.PROD_PRODUCTION);
                prod.setAuthor(currentUser);
                prod = (YvsProdDetailPic) dao.save1(prod);
                //Enregistre les coefficients pour la production
                saveCoefficient(production.getListData().get(cptP).getData().getListCoeficient(), prod);
                production.getListData().get(cptP).getData().setId(prod.getId());
            }
            cptP++;
        }
        succes();
        return planif.getId();
    }

    public void savePic(boolean suivre) {

        //save PIC and Periode
        if (!updatePic) {
            if (controleForm() && !listDataFamille.isEmpty()) {
                saveNewPic(false);
            }
        } else {
            if (!suivre) { //le boolean suivre sigifie qu'on enregistre un autre pic en lui ratachant le pic courant comme prévision parente!
                updatePic();
                succes();
            } else {
                if (controleForm() && !listDataFamille.isEmpty()) {
                    saveNewPic(true);
                }

            }
        }
    }

    private void saveCoefficient(List<CoeficientPlanif> l, YvsProdDetailPic detail) {
        YvsProdCoefficientVariationPic co;
        for (CoeficientPlanif c : l) {
            co = new YvsProdCoefficientVariationPic();
            co.setDetailPic(detail);
            co.setType(new YvsBaseTypeCoefficient(c.getType().getId()));
            co.setId(null);
            co.setAuthor(currentUser);
            co.setValeur(c.getValue());
            co.setPercent(c.isPercent());
            co = (YvsProdCoefficientVariationPic) dao.save1(co);
            c.setId(co.getId());
        }
    }

    private void updatePic() {
        //modifier l'entête du PIC        
        if (!coordonnees.isEmpty()) {
            FamilleProduction fa;
            DataLigne2D rubrique;
            DataColone2D periode;
            YvsProdDetailPic entity;
            YvsProdCoefficientVariationPic coef;
            for (CoordoneeTablePic co : coordonnees) {
                fa = listDataFamille.get(co.getIndexFamille());
                rubrique = (DataLigne2D) fa.getColones().get(co.getIndexRubrique());
                periode = rubrique.getListData().get(co.getIndexVal());
                entity = new YvsProdDetailPic(periode.getData().getId());
                entity.setFamille(new YvsBaseFamilleArticle(fa.getFamille().getId()));
                entity.setPeriode(new YvsProdPeriodePlan(periode.getId()));
                entity.setTypeVal(rubrique.getLabelDetail());
                entity.setValeur(periode.getData().getValue());
                entity.setAuthor(currentUser);
                dao.update(entity);
                //modfie aussi les coefficients
                if (periode.getData().getListCoeficient() != null) {
                    for (CoeficientPlanif cf : periode.getData().getListCoeficient()) {
                        coef = new YvsProdCoefficientVariationPic(cf.getId());
                        coef.setAuthor(currentUser);
                        coef.setDetailPic(entity);
                        coef.setPercent(cf.isPercent());
                        coef.setType(new YvsBaseTypeCoefficient(cf.getType().getId()));
                        coef.setValeur(cf.getValue());
                        if (cf.isAlreadySave()) {
                            //on modifie juste le coefficient                        
                            dao.update(cf);
                        } else {
                            //on crée le coefficicent
                            coef.setId(null);
                            dao.save(coef);
                        }
                    }
                }
            }
            coordonnees.clear();
        }
    }

    private YvsBaseFamilleArticle buildFamille(FamilleProduction f) {
        return new YvsBaseFamilleArticle(f.getFamille().getId());
    }

    /*Fin Gérer les types de coefficients*/
    /*Charger les données du PIC*/
    private boolean disNextPic, disPrevPic;

    public boolean isDisNextPic() {
        return disNextPic;
    }

    public boolean isDisPrevPic() {
        return disPrevPic;
    }

    public void setDisNextPic(boolean disNextPic) {
        this.disNextPic = disNextPic;
    }

    public void setDisPrevPic(boolean disPrevPic) {
        this.disPrevPic = disPrevPic;
    }

    private List<YvsProdPlanification> listEntityPlanif;

    public void loadAllPic(boolean avancer, boolean init) {
        //charge les planification
        champ = new String[]{"societe", "typePlan"};
        val = new Object[]{currentAgence.getSociete(), "PIC"};
        listPlanification.clear();
        listPlanification_.clear();
        nameQueriCount = "YvsProdPlanification.findByTypeC";
        nameQueri = "YvsProdPlanification.findByType";
        listEntityPlanif = loadPlanification(avancer, init);
        listPlanification = UtilProd.buildBeanListPlanification(listEntityPlanif);
        listPlanification_.addAll(listPlanification);
    }

    private List<YvsProdPlanification> loadPlanification(boolean avancer, boolean init) {
        if (!init) {
            if (avancer) {
                setFirtResult(getFirtResult() + getNbMax());
            } else {
                setFirtResult(getFirtResult() - getNbMax());
            }
            if (getTotalPage() == getCurrentPage()) {
                setFirtResult(0);
            }
        } else {
            setFirtResult(0);
        }
        PaginatorResult<YvsProdPlanification> pa = new PaginatorResult<>();
        pa = pa.loadResult(nameQueriCount, nameQueri, champ, val, getFirtResult(), getNbMax(), dao);
        setDisNextPic(pa.isDisNext());
        setDisPrevPic(pa.isDisPrev());
        setTotalPage(pa.getNbPage());
        setCurrentPage(pa.getCurrentPage());
        return pa.getResult();
    }

    private void loadOnePic(int numLine) {
        YvsProdPlanification plan = listEntityPlanif.get(numLine);
        //charger les périodes
        headers = UtilProd.buildBeanListPeriodePlanification(plan.getYvsProdPeriodePlanList());
        populateView(buildBeanPlanification(plan));
        //charge l'horizon précédent aec ses données        
        //charger les détails
        List<YvsProdDetailPic> detailsPic;
        FamilleProduction fp;
        DataLigne2D vente;
        DataLigne2D prod;
        DataLigne2D stock;
        int index = -1;
        champ = new String[]{"periode"};
        listDataFamille = new ArrayList<>();
        FamilleArticle fa;
        for (YvsProdPeriodePlan p : plan.getYvsProdPeriodePlanList()) {
            //chage la liste des détails par périodes  
//            val = new Object[]{p};
            detailsPic = p.getYvsProdDetailPicList();
//            detailsPic = dao.loadNameQueries("YvsProdDetailPic.findByPeriode", champ, val);
            for (YvsProdDetailPic de : detailsPic) {
                fp = new FamilleProduction();
                fp.setId(de.getFamille().getId());
                fa = UtilProd.buildBeanFamilleArticle(de.getFamille());
                fp.setFamille(fa);
                if (!listDataFamille.contains(fp)) {
                    fp = new FamilleProduction(fa);
                    fp.setId(de.getFamille().getId());
                    fp.setColones(new FabriqueColonne().getColone("L2D"));
                    vente = new DataLigne2D(Constantes.PROD_VENTE);
                    prod = new DataLigne2D(Constantes.PROD_PRODUCTION);
                    stock = new DataLigne2D(Constantes.PROD_STOCK);
                    fp.getColones().add(vente);
                    fp.getColones().add(prod);
                    fp.getColones().add(stock);
                    listDataFamille.add(fp);
                } else {
                    index = listDataFamille.indexOf(fp);
                    fp = listDataFamille.get(index);
                }
                ObjectData obj;
                obj = new ObjectData(de.getId(), de.getValeur(), de.getValeur(), de.getTypeVal().substring(0, 1) + de.getId());
                if (de.getYvsProdCoefficientVariationPicList() != null) {
                    List<CoeficientPlanif> lcf = new ArrayList<>();
                    for (YvsProdCoefficientVariationPic c : de.getYvsProdCoefficientVariationPicList()) {
                        CoeficientPlanif cf = new CoeficientPlanif(c.getId(), c.getValeur(), new TypeCoeficient(c.getType().getId(), c.getType().getLibelle()));
                        cf.setAlreadySave(true);
                        cf.setPercent(c.isPercent());
                        lcf.add(cf);
                        obj.setListCoeficient(lcf);
                    }
                }
                switch (de.getTypeVal()) {
                    case Constantes.PROD_VENTE:
                        ((DataLigne2D) fp.getColones().get(0)).getListData().add(new DataColone2D(de.getPeriode().getId(), obj));
                        break;
                    case Constantes.PROD_STOCK:
                        ((DataLigne2D) fp.getColones().get(1)).getListData().add(new DataColone2D(de.getPeriode().getId(), obj));
                        break;
                    case Constantes.PROD_PRODUCTION:
                        ((DataLigne2D) fp.getColones().get(2)).getListData().add(new DataColone2D(de.getPeriode().getId(), obj));
                        break;
                }
//                ((DataLigne2D) fp.getColones().get(0)).getListData().add(new DataColone2D(de.getId(), new ObjectData(de.getValeurVente(), "V" + de.getId())));
//                ((DataLigne2D) fp.getColones().get(1)).getListData().add(new DataColone2D(de.getId(), new ObjectData(de.getValeur(), "P" + de.getId())));
//                ((DataLigne2D) fp.getColones().get(2)).getListData().add(new DataColone2D(de.getId(), new ObjectData(de.getValeurStock(), "S" + de.getId())));
            }
        }
    }

    public void openOnePic() {
        if (chaineSelect != null) {
            String numroLine[] = chaineSelect.split("-");
            int index = 0;
            try {
                for (String numroLine1 : numroLine) {
                    index = Integer.parseInt(numroLine1);
                }
                loadOnePic(index);
            } catch (IllegalArgumentException ex) {
                getErrorMessage("Erreur !");
            }
            updatePic = true;
        }

    }
    /**
     * *
     * Fin planification *
     */
    ObjectData data_;
    private DataLigne2D detailData;
    private DataLigne2D detailData1;
    private DataLigne2D detailData2;

    public DataLigne2D loadData() {
//        detailData = new DataLigne2D("Vente");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(10));
//            data_.setId("V_" + id_ + "_" + i);
//            detailData.getListData().add(data_);
//        }
//        detailData1 = new DataLigne2D("Production");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(10));
//            data_.setId("P_" + id_ + "_" + i);
//            detailData1.getListData().add(data_);
//        }
//        detailData2 = new DataLigne2D("Stocks");
//        for (int i = 0; i <= 11; i++) {
//            data_ = new ObjectData(i + new Random().nextInt(10));
//            data_.setId("S_" + id_ + "_" + i);
//            detailData2.getListData().add(data_);
//        }
        return detailData;
    }
    List<DataLigne2D> listDataPic;
    int id_;

    public void loadFamille() {
        listDataFamille.clear();
        listDataPic = new ArrayList<>();
        FamilleProduction pf;
        for (int i = 0; i <= 4; i++) {
            pf = new FamilleProduction();
            pf.setFamille(new FamilleArticle(i, "FA" + i, "Famille" + i, ""));
            loadData();
//            pf.getDetails().add(detailData);
//            pf.getDetails().add(detailData1);
//            pf.getDetails().add(detailData2);
            listDataFamille.add(pf);
            id_++;
        }
    }

    public void updateData(int indexF, int indexType, int indexVal) {
        FamilleProduction f = listDataFamille.get(indexF);
        DataLigne2D section = (DataLigne2D) f.getColones().get(indexType);
        DataColone2D periode = section.getListData().get(indexVal);
        periode.getData().setMontantBase(periode.getData().getValue());
        if (updatePic) {
            CoordoneeTablePic c = new CoordoneeTablePic(indexF, indexType, indexVal);
            if (!coordonnees.contains(c)) {
                coordonnees.add(c);
            }
        }
    }

    private Planification buildBeanPlanification(YvsProdPlanification pl) {
        Planification p = new Planification(pl.getId());
        p.setAmplitude(pl.getAmplitude());
        p.setDateDebut(pl.getDateDebut());
        p.setDateFin(pl.getDateFin());
        p.setDatePlan(pl.getDatePlanification());
        p.setDateReference(pl.getDatePlanification());
        p.setHorizon(pl.getHorizon());
        p.setPeriode(pl.getPeriode());
        p.setReference(pl.getReference());
        p.setSelectActif(false);
        p.setListRevision(buildBeanPlanification_(pl.getYvsProdPlanificationList()));
        return p;
    }

    private List<Planification> buildBeanPlanification_(List<YvsProdPlanification> lpl) {
        List<Planification> re = new ArrayList<>();
        for (YvsProdPlanification pl : lpl) {
            Planification p = new Planification(pl.getId());
            p.setAmplitude(pl.getAmplitude());
            p.setDateDebut(pl.getDateDebut());
            p.setDateFin(pl.getDateFin());
            p.setDatePlan(pl.getDatePlanification());
            p.setDateReference(pl.getDatePlanification());
            p.setHorizon(pl.getHorizon());
            p.setPeriode(pl.getPeriode());
            p.setReference(pl.getReference());
            p.setSelectActif(false);
            re.add(p);
        }
        return re;
    }

    @Override
    public void resetFiche() {
        resetFiche(planification);
        listDataFamille.clear();
        updatePic = false;
    }

    @Override
    public boolean controleFiche(Planification bean) {

        return true;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Planification recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Planification pl) {
        planification.setId(pl.getId());
        planification.setAmplitude(pl.getAmplitude());
        planification.setDateDebut(pl.getDateDebut());
        planification.setDateFin(pl.getDateFin());
        planification.setDatePlan(pl.getDatePlan());
        planification.setHorizon(pl.getHorizon());
        planification.setPeriode(pl.getPeriode());
        planification.setReference(pl.getReference());
        planification.setSelectActif(false);
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
    public void loadAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
