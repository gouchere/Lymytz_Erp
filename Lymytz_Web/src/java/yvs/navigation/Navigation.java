/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.navigation;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author GOUCHERE YVES
 */
@SessionScoped
@ManagedBean(name = "navigue")
public class Navigation implements Serializable {
//Modules

    private boolean home = true;
    private boolean donnees;
    private boolean user;
    private boolean produits;
    private boolean tiers;
    private boolean achat;
    private boolean vente;
    private boolean finance;
    private boolean grh;
    private boolean stock;
    private boolean production;
    private boolean statistique;
    private boolean parametrage;
    private boolean moduleCompl;
    private boolean groupe;
//pages
    private String pageProduits = "PageDeGarde";
//Sous menus Paramétrage
    private boolean accParam;
    private boolean societe;
    private boolean agence;
    private boolean entrepot;
    private boolean catC;
    private boolean export;
    private boolean modelR;
    private boolean importe;
    private boolean th;
    private boolean workflow;
    private boolean taxes;
    private boolean uniteDeVente;
    private boolean trancheVal;
    private boolean Dictionnaire;
    private boolean document;
    private boolean compte;
    private boolean natureCompte;
    private boolean secteur, jour_feries;
//sous menu utilisateur
    private boolean accUser;
    private boolean groupUser;
    private boolean newUser;
    private boolean espacePerso;
//produit
    private boolean accProduit;
    private boolean groupeProduits;
    private boolean newProduits;
    private boolean service;
    private boolean gesCatC;
    private boolean gesCatT;
    private boolean gesCodeBarre;
    private boolean ristourne;
    private boolean comission;
    private boolean statProduit;
//Tiers
    private boolean accTiers;
    private boolean newTiers;
    private boolean client;
    private boolean fournisseur;
    private boolean representant;
    private boolean personel;
    private boolean statTiers;
//Achat 
    private boolean accAchat;
    private boolean machat;
    private boolean homeAppro, statAppro;
    //sous menu achat
    private boolean cmdeAchat;
    private boolean livraisonAchat;
    private boolean retourAchat;
    private boolean reglement;
    private boolean avoirAchat;
    private boolean planifieAchat;
//VENTE
    private boolean accVente;
    private boolean mvente;
    private boolean sav, statVente;
    private boolean accComerce; //accueil commerce
//GRH
    private boolean accGrh, homePaie, homeOther;
    private boolean newEmplyee;
    private boolean newConge;
    private boolean paramGrh;
    private boolean pgrhQualif, pgrhService, pgrhCategorie, pgrhEchelon, pgrhCategorieE, pgrSecteur;
    private boolean gesPersonnel;
    private boolean eltSalaire, grh1 = true, grh2;
    /**
     * *
     */
    private boolean accFinance;
    private boolean accStock;
    private boolean accMc;
    private boolean accProduction;
    private boolean accStatistique;
    /**
     * STOCK
     */
    private boolean mvtStock, inventaire, statStock, viewStock, viewMvtStock;
    /**
     * Messagerie
     */
    private boolean messagerie;
    /**
     * Titres des page
     */
    private String titleProduit = "Produits/Service";
    private String titleParam = "Paramétrage/Service";
    private Stack<Menus> pileForward;
    private Stack<Menus> pileNext;
    private String module;
    private String menu;
    private String page;
    private byte callNavigation = 0; //marqueur du nombre d'appel de la méthode de navigation

    public Navigation() {
        pileForward = new Stack<>();
        pileNext = new Stack<>();
    }

    public boolean isHome() {
        return home;
    }

    public boolean isFinance() {
        return finance;
    }

    public boolean isGrh() {
        return grh;
    }

    public boolean isParametrage() {
        return parametrage;
    }

    public boolean isProduction() {
        return production;
    }

    public boolean isProduits() {
        return produits;
    }

    public boolean isStatistique() {
        return statistique;
    }

    public boolean isStock() {
        return stock;
    }

    public boolean isTiers() {
        return tiers;
    }

    public boolean isUser() {
        return user;
    }

    public boolean isDonnees() {
        return donnees;
    }

    public String getPageProduits() {
        return pageProduits;
    }

    public void setPageProduits(String pageProduits) {
        this.pageProduits = pageProduits;
    }

    public boolean isDictionnaire() {
        return Dictionnaire;
    }

    public boolean isCompte() {
        return compte;
    }

    public boolean isNatureCompte() {
        return natureCompte;
    }

    public boolean isSecteur() {
        return secteur;
    }

    public boolean isJour_feries() {
        return jour_feries;
    }

    public boolean isAgence() {
        return agence;
    }

    public boolean isCatC() {
        return catC;
    }

    public boolean isExport() {
        return export;
    }

    public boolean isImporte() {
        return importe;
    }

    public boolean isEntrepot() {
        return entrepot;
    }

    public boolean isSociete() {
        return societe;
    }

    public boolean isTaxes() {
        return taxes;
    }

    public boolean isTrancheVal() {
        return trancheVal;
    }

    public boolean isUniteDeVente() {
        return uniteDeVente;
    }

    public boolean isComission() {
        return comission;
    }

    public boolean isEspacePerso() {
        return espacePerso;
    }

    public boolean isGesCatC() {
        return gesCatC;
    }

    public boolean isGesCatT() {
        return gesCatT;
    }

    public boolean isGesCodeBarre() {
        return gesCodeBarre;
    }

    public boolean isGroupUser() {
        return groupUser;
    }

    public boolean isGroupeProduits() {
        return groupeProduits;
    }

    public boolean isModelR() {
        return modelR;
    }

    public boolean isNewProduits() {
        return newProduits;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public boolean isRistourne() {
        return ristourne;
    }

    public boolean isService() {
        return service;
    }

    public boolean isStatProduit() {
        return statProduit;
    }

    public boolean isClient() {
        return client;
    }

    public boolean isFournisseur() {
        return fournisseur;
    }

    public boolean isNewTiers() {
        return newTiers;
    }

    public boolean isPersonel() {
        return personel;
    }

    public boolean isRepresentant() {
        return representant;
    }

    public boolean isStatTiers() {
        return statTiers;
    }

    public boolean isAchat() {
        return achat;
    }

    public boolean isCmdeAchat() {
        return cmdeAchat;
    }

    public boolean isLivraisonAchat() {
        return livraisonAchat;
    }

    public boolean isReglement() {
        return reglement;
    }

    public boolean isAvoirAchat() {
        return avoirAchat;
    }

    public boolean isPlanifieAchat() {
        return planifieAchat;
    }

    public boolean isRetourAchat() {
        return retourAchat;
    }

    public boolean isAccTiers() {
        return accTiers;
    }

    public boolean isAccStock() {
        return accStock;
    }

    public boolean isAccAchat() {
        return accAchat;
    }

    public boolean isAccVente() {
        return accVente;
    }

    public boolean isAccParam() {
        return accParam;
    }

    public boolean isMachat() {
        return machat;
    }

    public boolean isHomeAppro() {
        return homeAppro;
    }

    public boolean isStatAppro() {
        return statAppro;
    }

    public boolean isGroupe() {
        return groupe;
    }

    public boolean isModuleCompl() {
        return moduleCompl;
    }

    public boolean isMvente() {
        return mvente;
    }

    public boolean isAccProduit() {
        return accProduit;
    }

    public boolean isAccUser() {
        return accUser;
    }

    public String getTitleProduit() {
        return titleProduit;
    }

    public void setTitleProduit(String titleProduit) {
        this.titleProduit = titleProduit;
    }

    public String getTitleParam() {
        return titleParam;
    }

    public void setTitleParam(String titleParam) {
        this.titleParam = titleParam;
    }

    public boolean isDocument() {
        return document;
    }

    public boolean isSav() {
        return sav;
    }

    public boolean isStatVente() {
        return statVente;
    }

    public boolean isVente() {
        return vente;
    }

    public boolean isAccComerce() {
        return accComerce;
    }

    public boolean isAccFinance() {
        return accFinance;
    }

    public boolean isAccGrh() {
        return accGrh;
    }

    public boolean isAccMc() {
        return accMc;
    }

    public boolean isAccProduction() {
        return accProduction;
    }

    public boolean isAccStatistique() {
        return accStatistique;
    }

    public boolean isEltSalaire() {
        return eltSalaire;
    }

    public boolean isNewConge() {
        return newConge;
    }

    public boolean isNewEmplyee() {
        return newEmplyee;
    }

    public boolean isTh() {
        return th;
    }

    public boolean isWorkflow() {
        return workflow;
    }

    public boolean isMvtStock() {
        return mvtStock;
    }

    public void setMvtStock(boolean mvtStock) {
        this.mvtStock = mvtStock;
    }

    public boolean isInventaire() {
        return inventaire;
    }

    public boolean isStatStock() {
        return statStock;
    }

    public boolean isViewStock() {
        return viewStock;
    }

    public boolean isViewMvtStock() {
        return viewMvtStock;
    }

    public boolean isMessagerie() {
        return messagerie;
    }

    public boolean isGrh1() {
        return grh1;
    }

    public boolean isGrh2() {
        return grh2;
    }

    public boolean isParamGrh() {
        return paramGrh;
    }

    public boolean isPgrhQualif() {
        return pgrhQualif;
    }

    public boolean isPgrhCategorie() {
        return pgrhCategorie;
    }

    public boolean isPgrhService() {
        return pgrhService;
    }

    public boolean isPgrhCategorieE() {
        return pgrhCategorieE;
    }

    public boolean isPgrhEchelon() {
        return pgrhEchelon;
    }

    public boolean isGesPersonnel() {
        return gesPersonnel;
    }

    public boolean isPgrSecteur() {
        return pgrSecteur;
    }

    public String choixModule(boolean user, boolean produit, boolean tiers, boolean commerce, boolean comptabilite, boolean grh, boolean stock, boolean production, boolean stat, boolean param) {
        return null;
    }

    public boolean isHomePaie() {
        return homePaie;
    }

    public boolean isHomeOther() {
        return homeOther;
    }

//    private void displayAcc(boolean module){
//        accCommerce=false;
//        acc
//    }
    public String goToHome() {

        return null;
    }

    public void choixSubMenuParam(String page, boolean societe, boolean agence, boolean entrepot, boolean catC, boolean export, boolean modelR, boolean imp, boolean taxe, boolean unitV, boolean tranche, boolean dico, boolean compte, boolean ncompte, boolean doc) {

        accParam = false;
//        RequestContext.getCurrentInstance().execute("refresh(0)");
//        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("Expired", "0");
//        return null;
    }

    public String choixSubMenuUser(String page, boolean groupe, boolean users, boolean perso) {

//        FacesContext.getCurrentInstance().getExternalContext().setResponseHeader("refresh", );
        return null;
    }

    public String choixSubMenuProduit(String page, boolean groupe, boolean produit, boolean service, boolean catC, boolean catT, boolean codeBarre, boolean ristourne, boolean comission, boolean stat) {
        RequestContext.getCurrentInstance().update("BLOC-PROD");
        return null;
    }

    public String choixSubMenuTiers(String page, boolean tiers, boolean client, boolean fseur, boolean repr, boolean personel, boolean stat) {
//        this.pageProduits = page; 

        return null;
    }

    public void miseTiersAfalse() {
    }

    public String choixSubMenuCommerce(String page, boolean achat) {

        return null;
    }

    public String choixSubMenuCommerceAchat(String page, boolean cmde, boolean achat, boolean avoir, boolean retour, boolean reglement, boolean livraison, boolean plan) {
        this.pageProduits = page;

        return null;
    }

    /**
     * *parcours tous les attribut de la classe en affectant false aux
     * propriétés, sauf celles portant le nom passé en paramètre
     *
     * @param page
     * @param module
     * @param menu
     * @return
     */
    /*navigation à deux niveaux*/
    public String navigationMenu(String page, String module, String menu) {
        return navigue(page, module, "", menu, true);
    }
    /*navigation à trois niveaux*/

    public String navigationMenu(String page, String module, String sMenu, String menu) {
        return navigue(page, module, sMenu, menu, true);
    }

    private String navigue(String page, String module, String smenu, String menu, boolean empile) {
        Class<Navigation> cl = Navigation.class;
        Field[] listChamp = cl.getDeclaredFields();
        for (Field f : listChamp) {
            if (f.getType().getName().toLowerCase().equals("boolean")) {
                f.setAccessible(true);
                try {
                    if (f.getName().equals(module) || f.getName().equals(menu) || f.getName().equals(smenu)) {
                        f.setBoolean(this, true);
                    } else {
                        f.setBoolean(this, false);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Navigation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (empile) {
            if (pileForward.size() != 10 && callNavigation != 0) {
                pileForward.push(new Menus(this.module, this.menu, this.page));
                this.module = module;
                this.menu = menu;
                this.page = page;
            }
            if (callNavigation == 0) {
//                pileForward.push(new Menus("home", "home", ""));
                callNavigation++;
                this.module = "home";
                this.menu = "home";
                this.page = "";
            }
        }
//        RequestContext.getCurrentInstance().update("scrol-pan");
        return null;
    }

    public Stack<Menus> getPileForward() {
        return pileForward;
    }

    public Stack<Menus> getPileNext() {
        return pileNext;
    }

    public String forward() {
        Menus m = pileForward.pop();
        pileNext.push(m);
        return navigue(m.getPage(), m.getModule(), "", m.getMenu(), false);  //le paramètre false signale qu'on ne doit pas empiler cette navigation
    }

    public String next() {
        Menus m = pileNext.pop();
        pileForward.push(m);
        return navigue(m.getPage(), m.getModule(), "", m.getMenu(), false);  //le paramètre false signale qu'on ne doit pas empiler cette navigation
    }

    /**
     * ouverture du chat
     */
    public void openChat() {
        Map<String, Object> options = new HashMap<>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        RequestContext.getCurrentInstance().addCallbackParam("chat", options);
//        RequestContext.getCurrentInstance().openDialog("chat", options, null);
    }
    
    
    //gestion des vues dans un tabview
      List<HistoriquePages> model= new ArrayList<>();
      public List<HistoriquePages> getModel() {
        return model;
    }

    public void setModel(List<HistoriquePages> model) {
        this.model = model;
    }
    public void addTab(){
//        model.add(new HistoriquePages(menu, menu));
    }
}
