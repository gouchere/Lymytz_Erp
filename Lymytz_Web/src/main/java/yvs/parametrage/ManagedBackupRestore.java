/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseElementReference;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModeReglementBanque;
import yvs.entity.base.YvsBaseModeReglementInformations;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.commercial.YvsComParametre;
import yvs.entity.commercial.YvsComParametreAchat;
import yvs.entity.commercial.YvsComParametreStock;
import yvs.entity.commercial.YvsComParametreVente;
import yvs.entity.commercial.commission.YvsComFacteurTaux;
import yvs.entity.commercial.commission.YvsComPlanCommission;
import yvs.entity.commercial.commission.YvsComTrancheTaux;
import yvs.entity.commercial.commission.YvsComTypeGrille;
import yvs.entity.commercial.rrr.YvsComGrilleRabais;
import yvs.entity.commercial.rrr.YvsComGrilleRemise;
import yvs.entity.commercial.rrr.YvsComGrilleRistourne;
import yvs.entity.commercial.rrr.YvsComPlanRistourne;
import yvs.entity.commercial.rrr.YvsComRabais;
import yvs.entity.commercial.rrr.YvsComRemise;
import yvs.entity.commercial.rrr.YvsComRistourne;
import yvs.entity.compta.YvsBaseNatureCompte;
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.YvsComptaParametre;
import yvs.entity.compta.YvsComptaPlanAbonnement;
import yvs.entity.compta.analytique.YvsComptaCentreAnalytique;
import yvs.entity.compta.analytique.YvsComptaPlanAnalytique;
import yvs.entity.grh.activite.YvsGrhObjetsMission;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.contrat.YvsGrhTypeElementAdditionel;
import yvs.entity.grh.param.YvsCalendrier;
import yvs.entity.grh.param.YvsGrhCategorieProfessionelle;
import yvs.entity.grh.param.YvsGrhConventionCollective;
import yvs.entity.grh.param.YvsGrhEchelons;
import yvs.entity.grh.param.YvsJoursFeries;
import yvs.entity.grh.param.YvsJoursOuvres;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.param.poste.YvsGrhDepartement;
import yvs.entity.grh.param.poste.YvsGrhPosteDeTravail;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.grh.salaire.YvsGrhCategorieElement;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhRubriqueBulletin;
import yvs.entity.grh.salaire.YvsGrhStructureSalaire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsDictionnaireInformations;
import yvs.entity.param.YvsGrhPlanPrelevement;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.production.YvsProdParametre;
import yvs.entity.production.base.YvsProdComposantNomenclature;
import yvs.entity.production.base.YvsProdGammeArticle;
import yvs.entity.production.base.YvsProdGammeSite;
import yvs.entity.production.base.YvsProdNomenclature;
import yvs.entity.production.base.YvsProdNomenclatureSite;
import yvs.entity.production.base.YvsProdOperationsGamme;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.production.base.YvsProdSiteProduction;
import yvs.entity.production.equipe.YvsProdEquipeProduction;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.entity.users.YvsNiveauAcces;
import yvs.init.Initialisation;
import static yvs.util.Constantes.NEW_LINE;
import static yvs.util.Constantes.NEW_LINE_END;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedBackupRestore extends Managed<Serializable, Serializable> implements Serializable {

    private boolean backupWithSub = true, backupWithSubParam = true, backupWithSubBase = true, backupWithSubCom = true, backupWithSubCompta = true, backupWithSubProd = true, backupWithSubRh = true, backupWithSubMut = true, backupWithSubProj = true, backupWithSubCrm = true;

    private boolean useJson = true;

    private List<Value> parametragesSelect, parametrages = new ArrayList<Value>() {
        {
            add(new Value("societe", "Informations sur la socièté", "Informations sur la socièté"));
            add(new Value("agence", "Informations sur les agences", "Informations sur les agences"));
            add(new Value("model_numerotation", "Données relatives aux modèles de numérotation", "Données relatives aux modèles de numérotation"));
            add(new Value("dictionnaire", "Données relatives aux dictionnaires", "Données relatives aux dictionnaires"));
            add(new Value("niveau_acces", "Données relatives aux niveaux d'accès", "Données relatives aux niveaux d'accès"));
            add(new Value("template_tiers", "Données relatives aux template tiers", "Données relatives aux template tiers"));
        }
    };
    private List<Value> donneesBaseSelect, donneesBase = new ArrayList<Value>() {
        {
            add(new Value("base_parametrage", "Paramètres de base", "Paramètres de base"));
            add(new Value("classe_statistique", "Données relatives aux classes statistique", "Données relatives aux classes statistique"));
            add(new Value("categorie_comptable", "Données relatives aux catégories comptable", "Données relatives aux catégories comptable"));
            add(new Value("unite_mesure", "Données relatives aux unités de mesure", "Données relatives aux unités de mesure"));
            add(new Value("taxe", "Données relatives aux taxes", "Données relatives aux taxes"));
            add(new Value("groupe_article", "Données relatives aux groupes articles", "Données relatives aux groupes articles"));
            add(new Value("famille_article", "Données relatives aux familles articles", "Données relatives aux familles articles"));
            add(new Value("template_article", "Données relatives aux templates articles", "Données relatives aux templates articles"));
            add(new Value("article", "Données relatives aux articles", "Données relatives aux articles"));
            add(new Value("code_barre", "Données relatives aux codes barre", "Données relatives aux codes barre"));
            add(new Value("tranche_horaire", "Données relatives aux tranches horaire", "Données relatives aux tranches horaire"));
            add(new Value("fournisseur", "Données relatives aux fournisseurs", "Données relatives aux fournisseurs"));
            add(new Value("type_cout", "Données relatives aux types de coût", "Données relatives aux types de coût"));
            add(new Value("type_od", "Données relatives aux types d'opération diverse", "Données relatives aux types d'opération diverse"));
            add(new Value("exercice", "Données relatives aux exercices comptable", "Données relatives aux exercices comptable"));
            add(new Value("code_acces", "Données relatives aux codes d'accès", "Données relatives aux codes d'accès"));
        }
    };
    private List<Value> commercialesSelect, commerciales = new ArrayList<Value>() {
        {
            add(new Value("com_parametrage", "Paramètres de la gestion commerciale", "Paramètres de la gestion commerciale"));
            add(new Value("mode_paiement", "Données relatives aux modes de paiement", "Données relatives aux modes de paiement"));
            add(new Value("model_reglement", "Données relatives aux modèles de reglement", "Données relatives aux modèles de reglement"));
            add(new Value("plan_commission", "Données relatives aux plans de commission", "Données relatives aux plans de commission"));
            add(new Value("plan_ristourne", "Données relatives aux plans de ristourne", "Données relatives aux plans de ristourne"));
            add(new Value("plan_remise", "Données relatives aux plans de remise", "Données relatives aux plans de remise"));
            add(new Value("plan_rabais", "Données relatives aux plans de rabais et bonus", "Données relatives aux plans de rabais et bonus"));

        }
    };
    private List<Value> productionsSelect, productions = new ArrayList<Value>() {
        {
            add(new Value("prod_parametrage", "Paramètres de la production", "Paramètres de la production"));
            add(new Value("site_production", "Données relatives aux sites de production", "Données relatives aux sites de production"));
            add(new Value("nomenclature", "Données relatives aux nomenclature", "Données relatives aux nomenclature"));
            add(new Value("gamme_article", "Données relatives aux gammes d'article", "Données relatives aux gammes d'article"));
            add(new Value("poste_charge", "Données relatives aux postes de charge", "Données relatives aux postes de charge"));
            add(new Value("equipe_production", "Données relatives aux équipes de production", "Données relatives aux équipes de production"));

        }
    };
    private List<Value> comptabilitesSelect, comptabilites = new ArrayList<Value>() {
        {
            add(new Value("compta_parametrage", "Paramètres de la comptabilité", "Paramètres de la comptabilité"));
            add(new Value("nature_comptable", "Données relatives aux natures comptable", "Données relatives aux natures des comptes de la comptabilité"));
            add(new Value("plan_comptable", "Données relatives aux comptes du plan comptable", "Données relatives aux comptes du plan comptable"));
            add(new Value("journaux", "Données relatives aux journaux comptable", "Données relatives aux journaux comptable"));
            add(new Value("plan_abonnement", "Données relatives aux plans d'abonnement", "Données relatives aux plans d'abonnement"));
            add(new Value("centre_analytique", "Données relatives aux centres analytique", "Données relatives aux centres analytique"));
        }
    };
    private List<Value> humainesSelect, humaines = new ArrayList<Value>() {
        {
            add(new Value("rh_parametrage", "Paramètres de la gestion des relations humaine", "Paramètres de la gestion des relations humaine"));
            add(new Value("calendrier", "Données relatives aux calendriers de travail", "Données relatives aux calendriers de travail"));
            add(new Value("objet_mission", "Données relatives aux objets de mission", "Données relatives aux objets de mission"));
            add(new Value("type_prime", "Données relatives aux types de prime", "Données relatives aux types de prime"));
            add(new Value("type_retenue", "Données relatives aux types de retenue", "Données relatives aux types de retenue"));
            add(new Value("plan_prelevement", "Données relatives aux plans de prelèvement", "Données relatives aux plans de prelèvement"));
            add(new Value("categorie_profesionnelle", "Données relatives aux catégories profesionnelle", "Données relatives aux catégories profesionnelle"));
            add(new Value("echellon_profesionnelle", "Données relatives aux échellons profesionnelle", "Données relatives aux échellons profesionnelle"));
            add(new Value("convention_collective", "Données relatives aux conventions collective", "Données relatives aux conventions collective"));
            add(new Value("jour_ferie", "Données relatives aux jours fériés", "Données relatives aux jours fériés"));
            add(new Value("departement", "Données relatives aux départements", "Données relatives aux départements"));
            add(new Value("poste_travail", "Données relatives aux postes de travail", "Données relatives aux postes de travail"));
            add(new Value("regle_salaire", "Données relatives aux règles de salaire", "Données relatives aux règles de salaire"));
            add(new Value("structure_salaire", "Données relatives aux structures de salaire", "Données relatives aux structures de salaire"));
        }
    };
    private List<Value> mutuellesSelect, mutuelles = new ArrayList<Value>() {
        {
            add(new Value("mut_parametrage", "Paramètres de la mutuelle", "Paramètres de la mutuelle"));

        }
    };
    private List<Value> projetsSelect, projets = new ArrayList<Value>() {
        {
            add(new Value("proj_parametrage", "Paramètres de la gestion des projets", "Paramètres de la gestion des projets"));

        }
    };
    private List<Value> relationsSelect, relations = new ArrayList<Value>() {
        {
            add(new Value("crm_parametrage", "Paramètres de la gestion des relations clients", "Paramètres de la gestion des relations clients"));

        }
    };

    private Value selectValue = new Value();

    private String module = null;

    public ManagedBackupRestore() {
        parametragesSelect = new ArrayList<>(parametrages);
        donneesBaseSelect = new ArrayList<>(donneesBase);
        commercialesSelect = new ArrayList<>(commerciales);
        productionsSelect = new ArrayList<>(productions);
        comptabilitesSelect = new ArrayList<>(comptabilites);
        humainesSelect = new ArrayList<>(humaines);
        mutuellesSelect = new ArrayList<>(mutuelles);
        projetsSelect = new ArrayList<>(projets);
        relationsSelect = new ArrayList<>(relations);
    }

    public boolean isBackupWithSub() {
        return backupWithSub;
    }

    public void setBackupWithSub(boolean backupWithSub) {
        this.backupWithSub = backupWithSub;
    }

    public boolean isBackupWithSubParam() {
        return backupWithSubParam;
    }

    public void setBackupWithSubParam(boolean backupWithSubParam) {
        this.backupWithSubParam = backupWithSubParam;
    }

    public boolean isBackupWithSubBase() {
        return backupWithSubBase;
    }

    public void setBackupWithSubBase(boolean backupWithSubBase) {
        this.backupWithSubBase = backupWithSubBase;
    }

    public boolean isBackupWithSubCom() {
        return backupWithSubCom;
    }

    public void setBackupWithSubCom(boolean backupWithSubCom) {
        this.backupWithSubCom = backupWithSubCom;
    }

    public boolean isBackupWithSubCompta() {
        return backupWithSubCompta;
    }

    public void setBackupWithSubCompta(boolean backupWithSubCompta) {
        this.backupWithSubCompta = backupWithSubCompta;
    }

    public boolean isBackupWithSubProd() {
        return backupWithSubProd;
    }

    public void setBackupWithSubProd(boolean backupWithSubProd) {
        this.backupWithSubProd = backupWithSubProd;
    }

    public boolean isBackupWithSubRh() {
        return backupWithSubRh;
    }

    public void setBackupWithSubRh(boolean backupWithSubRh) {
        this.backupWithSubRh = backupWithSubRh;
    }

    public boolean isBackupWithSubMut() {
        return backupWithSubMut;
    }

    public void setBackupWithSubMut(boolean backupWithSubMut) {
        this.backupWithSubMut = backupWithSubMut;
    }

    public boolean isBackupWithSubProj() {
        return backupWithSubProj;
    }

    public void setBackupWithSubProj(boolean backupWithSubProj) {
        this.backupWithSubProj = backupWithSubProj;
    }

    public boolean isBackupWithSubCrm() {
        return backupWithSubCrm;
    }

    public void setBackupWithSubCrm(boolean backupWithSubCrm) {
        this.backupWithSubCrm = backupWithSubCrm;
    }

    public List<Value> getParametrages() {
        return parametrages;
    }

    public void setParametrages(List<Value> parametrages) {
        this.parametrages = parametrages;
    }

    public List<Value> getParametragesSelect() {
        return parametragesSelect;
    }

    public void setParametragesSelect(List<Value> parametragesSelect) {
        this.parametragesSelect = parametragesSelect;
    }

    public List<Value> getDonneesBase() {
        return donneesBase;
    }

    public void setDonneesBase(List<Value> donneesBase) {
        this.donneesBase = donneesBase;
    }

    public List<Value> getDonneesBaseSelect() {
        return donneesBaseSelect;
    }

    public void setDonneesBaseSelect(List<Value> donneesBaseSelect) {
        this.donneesBaseSelect = donneesBaseSelect;
    }

    public List<Value> getCommerciales() {
        return commerciales;
    }

    public void setCommerciales(List<Value> commerciales) {
        this.commerciales = commerciales;
    }

    public List<Value> getCommercialesSelect() {
        return commercialesSelect;
    }

    public void setCommercialesSelect(List<Value> commercialesSelect) {
        this.commercialesSelect = commercialesSelect;
    }

    public List<Value> getProductions() {
        return productions;
    }

    public void setProductions(List<Value> productions) {
        this.productions = productions;
    }

    public List<Value> getProductionsSelect() {
        return productionsSelect;
    }

    public void setProductionsSelect(List<Value> productionsSelect) {
        this.productionsSelect = productionsSelect;
    }

    public List<Value> getComptabilites() {
        return comptabilites;
    }

    public void setComptabilites(List<Value> comptabilites) {
        this.comptabilites = comptabilites;
    }

    public List<Value> getComptabilitesSelect() {
        return comptabilitesSelect;
    }

    public void setComptabilitesSelect(List<Value> comptabilitesSelect) {
        this.comptabilitesSelect = comptabilitesSelect;
    }

    public List<Value> getHumaines() {
        return humaines;
    }

    public void setHumaines(List<Value> humaines) {
        this.humaines = humaines;
    }

    public List<Value> getHumainesSelect() {
        return humainesSelect;
    }

    public void setHumainesSelect(List<Value> humainesSelect) {
        this.humainesSelect = humainesSelect;
    }

    public List<Value> getMutuelles() {
        return mutuelles;
    }

    public void setMutuelles(List<Value> mutuelles) {
        this.mutuelles = mutuelles;
    }

    public List<Value> getMutuellesSelect() {
        return mutuellesSelect;
    }

    public void setMutuellesSelect(List<Value> mutuellesSelect) {
        this.mutuellesSelect = mutuellesSelect;
    }

    public List<Value> getProjets() {
        return projets;
    }

    public void setProjets(List<Value> projets) {
        this.projets = projets;
    }

    public List<Value> getProjetsSelect() {
        return projetsSelect;
    }

    public void setProjetsSelect(List<Value> projetsSelect) {
        this.projetsSelect = projetsSelect;
    }

    public List<Value> getRelations() {
        return relations;
    }

    public void setRelations(List<Value> relations) {
        this.relations = relations;
    }

    public List<Value> getRelationsSelect() {
        return relationsSelect;
    }

    public void setRelationsSelect(List<Value> relationsSelect) {
        this.relationsSelect = relationsSelect;
    }

    public Value getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(Value selectValue) {
        this.selectValue = selectValue;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            setSelectValue((Value) ev.getObject());
            update("value-description");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        setSelectValue(new Value());
        update("value-description");
    }

    private Constructor getConstructor(Type type) {
        Constructor[] constructors = (Constructor[]) ((Class) type).getDeclaredConstructors();
        Constructor constructor = null;
        for (Constructor cstr : constructors) {
            //Only if default constructor
            if (cstr.getParameterAnnotations().length == 0) {
                constructor = (Constructor) cstr;
                break;
            }
        }
        return constructor;
    }

    private Object getValueFromJson(Field field, JsonElement element) {
        try {
            if (field.getType().equals(BigDecimal.class)) {
                return element.getAsBigDecimal();
            } else if (field.getType().equals(BigInteger.class)) {
                return element.getAsBigInteger();
            } else if (field.getType().equals(Boolean.class)) {
                return element.getAsBoolean();
            } else if (field.getType().equals(Byte.class)) {
                return element.getAsByte();
            } else if (field.getType().equals(Character.class)) {
                return element.getAsCharacter();
            } else if (field.getType().equals(Double.class)) {
                return element.getAsDouble();
            } else if (field.getType().equals(Float.class)) {
                return element.getAsFloat();
            } else if (field.getType().equals(Integer.class)) {
                return element.getAsInt();
            } else if (field.getType().equals(Long.class)) {
                return element.getAsLong();
            } else if (field.getType().equals(Number.class)) {
                return element.getAsNumber();
            } else if (field.getType().equals(Short.class)) {
                return element.getAsShort();
            } else if (field.getType().equals(String.class)) {
                return element.getAsString();
            } else if (field.getType().equals(Date.class)) {
                Annotation annotation = field.getAnnotation(Temporal.class);
                if (annotation != null ? annotation instanceof Temporal : false) {
                    TemporalType value = ((Temporal) annotation).value();
                    if (value.equals(TemporalType.DATE)) {
                        return formatDateReverse.parse(element.getAsString());
                    } else if (value.equals(TemporalType.TIME)) {
                        return formatHeure.parse(element.getAsString());
                    } else {
                        return formatDateTimeReverse.parse(element.getAsString());
                    }
                }
            } else if (!field.getType().isPrimitive()) {
                Constructor constructor = getConstructor(field.getType());
                if (constructor != null) {
                    try {
                        Object instance = constructor.newInstance();
                        if (instance != null) {
                            Method setId = null;
                            try {
                                setId = ((Class) field.getType()).getMethod("setId", new Class[]{Long.class});
                                if (setId != null) {
                                    setId.invoke(instance, element.getAsLong());
                                }
                            } catch (IllegalArgumentException | NoSuchMethodException | SecurityException exx) {
                                try {
                                    setId = ((Class) field.getType()).getMethod("setId", new Class[]{Integer.class});
                                    if (setId != null) {
                                        setId.invoke(instance, element.getAsInt());
                                    }
                                } catch (IllegalArgumentException | NoSuchMethodException | SecurityException ex) {
                                    Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        return instance;
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
                        Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BackupFile getBackupFile(JsonObject object) {
        BackupFile result = null;
        try {
            result = new BackupFile();

            JsonArray array;
            JsonObject value;
            JsonElement element;
            Class<?> fieldClass;
            Object instance, lists;
            Constructor constructor;
            Method add;

            for (Field field : BackupFile.class.getDeclaredFields()) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                if (Collection.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    lists = field.get(result);
                    if (lists == null) {
                        continue;
                    }
                    fieldClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (fieldClass != null) {
                        constructor = getConstructor(fieldClass);
                        if (constructor != null) {
                            array = object.getAsJsonArray(field.getName());
                            if (array == null) {
                                continue;
                            }
                            add = ((Class) field.getType()).getMethod("add", new Class[]{Object.class});
                            if (add == null) {
                                continue;
                            }
                            for (int i = 0; i < array.size(); i++) {
                                instance = constructor.newInstance();
                                if (instance != null) {
                                    value = array.get(i).getAsJsonObject();
                                    if (value == null) {
                                        continue;
                                    }
                                    for (Field f : fieldClass.getDeclaredFields()) {
                                        if (f.getName().equals("serialVersionUID")) {
                                            continue;
                                        }
                                        f.setAccessible(true);
                                        element = value.get(f.getName());
                                        if (element != null ? !element.isJsonNull() : false) {
                                            f.set(instance, getValueFromJson(f, element));
                                        }
                                    }
                                    add.invoke(lists, instance);
                                }
                            }
                        }
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void onRestore(FileUploadEvent ev) {
        try {
            if (ev != null) {
                String destination = Initialisation.getCheminResource() + "" + Initialisation.FILE_SEPARATOR + "temp.json";
                try {
                    File file = Util.convertInputStreamToFile(destination, ev.getFile().getInputstream());

                    String response = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    JsonParser parser = new JsonParser();
                    JsonObject object = (JsonObject) parser.parse(response);// response will be the json String
                    BackupFile backup = getBackupFile(object);
                    if (backup != null) {
                        System.err.println("backup.getYvsAgences : " + backup.getYvsAgences());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (JsonSyntaxException ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getResultForQuery(String directory, String query) {
        String result = null;
        try {
            if (!asString(directory)) {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(Initialisation.getCheminDownload());
                File dir = new File(path, "backup_" + System.currentTimeMillis());
                dir.setWritable(true);
                dir.mkdirs();
                directory = dir.getAbsolutePath() + Initialisation.FILE_SEPARATOR;
            }
            if (asString(directory) && asString(query)) {
                String script = "SELECT public.execute_backup(?, ?)";
                Object data = dao.loadObjectBySqlQuery(script, new Options[]{new Options(directory, 1), new Options(query, 2)});
                if (data != null) {
                    result = (boolean) data ? directory : null;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String getResultForQuery(String fileName, String table, String query) {
        String result = null;
        try {
            if (asString(query)) {
                String path = Initialisation.getCheminSave();
                String xmls = "<table xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
                String xmls_end = "</table>";
                String row = "<row>";
                String row_end = "</row>";
                String script = "SELECT XMLSERIALIZE(DOCUMENT query_to_xml(?, false, false, '') AS TEXT)";
                Object data = dao.loadObjectBySqlQuery(script, new Options[]{new Options(query, 1)});
                if (data != null) {
                    String value = data.toString();
                    if (asString(value)) {
//                        value = value.replaceAll("\"<yvs", "<yvs");
                        value = value.replace(xmls, "");
                        value = value.replace(xmls_end, "");
                        value = value.replace(row, "<" + table + ">");
                        value = value.replace(row_end, "</" + table + ">");
                        value = value.replace(NEW_LINE_END, "");
                        value = value.replace(NEW_LINE, "");
                        try {
                            File file = new File(path, (asString(fileName) ? new File(fileName).getName() : "backup_" + System.currentTimeMillis() + ".xml"));
                            boolean newFile = false;
                            if (!file.exists()) {
                                file.createNewFile();
                                newFile = true;
                            }
                            try (FileOutputStream out = new FileOutputStream(file, true)) {
                                try (PrintWriter print = new PrintWriter(new OutputStreamWriter(out, "UTF-8"))) {
                                    if (newFile) {
                                        print.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                                        print.println("<BackupFile>");
                                    }
                                    print.println(value.trim());
                                }
                            }
                            result = file.getAbsolutePath();
                        } catch (IOException ex) {
                            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private File getFileJson(String fileName) {
        File file = null;
        try {
            File dir = new File(fileName);
            if (dir.exists() ? dir.isDirectory() : false) {
                file = new File(fileName, "backup_" + (dir.getName().replace("backup_", "")) + ".json");
                if (!file.exists()) {
                    file.createNewFile();
                }

                String ligne;
                String result = "{" + Initialisation.LINE_SEPARATOR;
                for (File f : dir.listFiles()) {
                    String attribut = "";
                    String name = f.getName();
                    String[] tabs = name.split("[.]");
                    for (String e : (tabs.length > 0 ? tabs[0] : name).split("_")) {
                        attribut += (e.substring(0, 1).toUpperCase() + e.substring(1, e.length()));
                    }
                    String extension = tabs[1];
                    if (extension.equals("csv")) {
                        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                            String[] header = null;
                            String[] data = null;
                            int cpt = 0;
                            while ((ligne = br.readLine()) != null) {
                                if (cpt == 0) {
                                    header = ligne.split(";");
                                    result += " \"" + attribut + "\":[" + Initialisation.LINE_SEPARATOR;
                                } else {
                                    data = new String[header.length];
                                    String line = "";
                                    int k = 0;
                                    for (int i = 0; i < ligne.length(); i++) {
                                        if (String.valueOf(ligne.charAt(i)).equals(";")) {
                                            data[k] = line.trim().equals("") ? null : (line.equals("\"\"") ? null : (line.equals("t") ? "true" : (line.equals("f") ? "false" : line.trim().replace("\"", ""))));
                                            line = "";
                                            k++;
                                            continue;
                                        }
                                        line += String.valueOf(ligne.charAt(i));
                                    }
                                    result += "     {" + Initialisation.LINE_SEPARATOR;
                                    for (int i = 0; i < header.length; i++) {
                                        attribut = header[i];
                                        if (header[i].contains("_")) {
                                            attribut = "";
                                            for (int j = 0; j < header[i].split("_").length; j++) {
                                                if (j == 0) {
                                                    attribut = header[i].split("_")[j];
                                                } else {
                                                    attribut += String.valueOf(header[i].split("_")[j].charAt(0)).toUpperCase() + header[i].split("_")[j].substring(1);
                                                }
                                            }
                                        }
                                        String value = (data.length > i ? data[i] : null);
                                        if (value != null) {
                                            if (value.contains(":") && value.contains(" ") && value.contains(".")) {
                                                value = value.substring(0, value.indexOf("."));
                                            }
                                            if (!asNumeric(value) && !asBoolean(value)) {
                                                value = "\"" + value + "\"";
                                            }
                                        }
                                        result += ("        \"" + attribut + "\":" + value + (i < header.length - 1 ? "," : "")) + Initialisation.LINE_SEPARATOR;
                                    }
                                    result += "     }," + Initialisation.LINE_SEPARATOR;
                                }
                                cpt += 1;
                            }
                            result += " ]," + Initialisation.LINE_SEPARATOR;
                        }
                    }
                }
                if (!result.equals("{" + Initialisation.LINE_SEPARATOR)) {
                    result += "}" + Initialisation.LINE_SEPARATOR;
                    String _old_ = "     }," + Initialisation.LINE_SEPARATOR + " ]";
                    String _new_ = "     }" + Initialisation.LINE_SEPARATOR + " ]";
                    result = result.replace(_old_, _new_);
                    _old_ = " ]," + Initialisation.LINE_SEPARATOR + "}";
                    _new_ = " ]" + Initialisation.LINE_SEPARATOR + "}";
                    result = result.replace(_old_, _new_);
                    _old_ = ":\"\"\"";
                    _new_ = ":null";
                    result = result.replace(_old_, _new_);
                    try (FileOutputStream out = new FileOutputStream(file, true)) {
                        try (PrintWriter print = new PrintWriter(new OutputStreamWriter(out, "UTF-8"))) {
                            print.println(result);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    public void onBackup() {
        onBackup(null);
    }

    public void onBackup(String module) {
        this.module = module;
        try {
            Long societe = currentAgence.getSociete().getId();
            String fileName = null;
            if (module != null ? module.equals("param") : true) {
//                fileName = onBackupParametres(fileName, societe);
            }
            if (module != null ? module.equals("base") : true) {
                fileName = onBackupDonneesBase(fileName, societe);
            }
            if (module != null ? module.equals("com") : true) {
//                fileName = onBackupCommerciales(fileName, societe);
            }
            if (module != null ? module.equals("prod") : true) {
//                fileName = onBackupProductions(fileName, societe);
            }
            if (module != null ? module.equals("compta") : true) {
//                fileName = onBackupComptabilites(fileName, societe);
            }
            if (module != null ? module.equals("rh") : true) {
//                fileName = onBackupHumaines(fileName, societe);
            }
            if (module != null ? module.equals("mut") : true) {
//                fileName = onBackupMutuelles(fileName, societe);
            }
            if (module != null ? module.equals("proj") : true) {
//                fileName = onBackupProjets(fileName, societe);
            }
            if (module != null ? module.equals("crm") : true) {
//                fileName = onBackupRelations(fileName, societe);
            }
            if (asString(fileName)) {
                File file = null;
                if (useJson) {
                    file = getFileJson(fileName);
                } else {
                    file = new File(fileName);
                    try (FileOutputStream out = new FileOutputStream(file, true)) {
                        try (PrintWriter print = new PrintWriter(new OutputStreamWriter(out, "UTF-8"))) {
                            print.println("</BackupFile>");
                        }
                    }
                }
                if (file != null) {
                    String destination = file.getName();
                    byte[] bytes = Util.read(file);
                    FacesContext faces = FacesContext.getCurrentInstance();
                    HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
                    if (useJson) {
                        response.setContentType("application/json");
                        response.setCharacterEncoding("utf-8");
                        response.addHeader("Access-Control-Allow-Origin", "*");
                    } else {
                        response.setContentType("text/xml");
                        response.setHeader("Content-type", "application/xhtml+xml");
                    }
                    response.addHeader("Content-disposition", "attachment;filename=" + destination);
                    response.setContentLength(bytes.length);
                    try {
                        response.getOutputStream().write(bytes);
                        response.getOutputStream().flush();
                    } catch (IOException ex) {
                        log.log(Level.SEVERE, null, ex);
                    }
                    faces.responseComplete();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String onBackupParametres(String fileName, Long societe) {
        String query = "";
        for (Value value : parametragesSelect) {
            if (value.getCode().equals("societe")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQuerySociete(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsSocietes.class.getSimpleName(), onQuerySociete(societe));
                }
            }
            if (value.getCode().equals("agence")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryAgence(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsAgences.class.getSimpleName(), onQueryAgence(societe));
                }
            }
            if (value.getCode().equals("model_numerotation")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryElementNumerotation(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseElementReference.class.getSimpleName(), onQueryElementNumerotation(societe));
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryModelNumerotation(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseModeleReference.class.getSimpleName(), onQueryModelNumerotation(societe));
                }
            }
            if (value.getCode().equals("dictionnaire")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryDictionnaire(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsDictionnaire.class.getSimpleName(), onQueryDictionnaire(societe));
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryDictionnaireInformation(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsDictionnaireInformations.class.getSimpleName(), onQueryDictionnaireInformation(societe));
                }
            }
            if (value.getCode().equals("niveau_acces")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryNiveauAcces(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsNiveauAcces.class.getSimpleName(), onQueryNiveauAcces(societe));
                }
            }
            if (value.getCode().equals("template_tiers")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQuerySociete(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseTiersTemplate.class.getSimpleName(), onQueryTemplateTiers(societe));
                }
            }
        }
        if (useJson) {
            fileName = getResultForQuery(fileName, query);
        }
        return fileName;
    }

    private String onQueryTemplateTiers(Long societe) {
        return "SELECT y.* FROM yvs_base_tiers_template y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryNiveauAcces(Long societe) {
        return "SELECT y.* FROM yvs_niveau_acces y WHERE y.societe = " + societe;
    }

    private String onQuerySociete(Long societe) {
        return "SELECT y.* FROM yvs_societes y WHERE y.id = " + societe;
    }

    private String onQueryAgence(Long societe) {
        return "SELECT y.* FROM yvs_agences y WHERE y.societe = " + societe;
    }

    private String onQueryElementNumerotation(Long societe) {
        return "SELECT y.* FROM yvs_base_element_reference y ";
    }

    private String onQueryModelNumerotation(Long societe) {
        return "SELECT y.* FROM yvs_base_modele_reference y WHERE y.societe = " + societe;
    }

    private String onQueryDictionnaire(Long societe) {
        return "SELECT y.* FROM yvs_dictionnaire y WHERE y.societe = " + societe;
    }

    private String onQueryDictionnaireInformation(Long societe) {
        return "SELECT y.* FROM yvs_dictionnaire_informations y WHERE y.societe = " + societe;
    }

    private String onBackupDonneesBase(String fileName, Long societe) {
        String query = "";
        if (donneesBaseSelect.contains(new Value("article")) || donneesBaseSelect.contains(new Value("code_barre"))) {
            if (!donneesBaseSelect.contains(new Value("famille_article"))) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryFamilleArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseFamilleArticle.class.getSimpleName(), onQueryFamilleArticle(societe));
                }
            }
            if (!donneesBaseSelect.contains(new Value("unite_mesure"))) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryUniteMesure(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseUniteMesure.class.getSimpleName(), onQueryUniteMesure(societe));
                }
            }
        }
        for (Value value : donneesBaseSelect) {
            if (value.getCode().equals("base_parametrage")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryBaseParametre(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseParametre.class.getSimpleName(), onQueryBaseParametre(societe));
                }
            }
            if (value.getCode().equals("classe_statistique")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryClasseStatistique(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseClassesStat.class.getSimpleName(), onQueryClasseStatistique(societe));
                }
            }
            if (value.getCode().equals("categorie_comptable")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryCategorieComptable(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseCategorieComptable.class.getSimpleName(), onQueryCategorieComptable(societe));
                }
            }
            if (value.getCode().equals("unite_mesure")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryUniteMesure(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseUniteMesure.class.getSimpleName(), onQueryUniteMesure(societe));
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTableConversion(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseTableConversion.class.getSimpleName(), onQueryTableConversion(societe));
                }
            }
            if (value.getCode().equals("taxe")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTaxe(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseTaxes.class.getSimpleName(), onQueryTaxe(societe));
                }
            }
            if (value.getCode().equals("groupe_article")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryGroupeArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseGroupesArticle.class.getSimpleName(), onQueryGroupeArticle(societe));
                }
            }
            if (value.getCode().equals("famille_article")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryFamilleArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseFamilleArticle.class.getSimpleName(), onQueryFamilleArticle(societe));
                }
            }
            if (value.getCode().equals("template_article")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTemplateArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseArticlesTemplate.class.getSimpleName(), onQueryTemplateArticle(societe));
                }
            }
            if (value.getCode().equals("tranche_horaire")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTrancheHoraire(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsGrhTrancheHoraire.class.getSimpleName(), onQueryTrancheHoraire(societe));
                }
            }
            if (value.getCode().equals("fournisseur")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTiersFournisseur(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseTiers.class.getSimpleName(), onQueryTiersFournisseur(societe));
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryFournisseur(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseFournisseur.class.getSimpleName(), onQueryFournisseur(societe));
                }
            }
            if (value.getCode().equals("type_cout")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTypeCout(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsGrhTypeCout.class.getSimpleName(), onQueryTypeCout(societe));
                }
            }
            if (value.getCode().equals("type_od")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryTypeOD(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseTypeDocDivers.class.getSimpleName(), onQueryTypeOD(societe));
                }
            }
            if (value.getCode().equals("exercice")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryExercice(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseExercice.class.getSimpleName(), onQueryExercice(societe));
                }
            }
            if (value.getCode().equals("code_acces")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryCodeAcces(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseCodeAcces.class.getSimpleName(), onQueryCodeAcces(societe));
                }
            }
            if (value.getCode().equals("article")) {
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseArticles.class.getSimpleName(), onQueryArticle(societe));
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryConditionnement(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseConditionnement.class.getSimpleName(), onQueryConditionnement(societe));
                }
                if (!donneesBaseSelect.contains(new Value("categorie_comptable"))) {
                    if (useJson) {
                        query += (query.equals("") ? "" : ";") + onQueryCategorieComptable(societe);
                    } else {
                        fileName = getResultForQuery(fileName, YvsBaseCategorieComptable.class.getSimpleName(), onQueryCategorieComptable(societe));
                    }
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryCategorieComptableArticle(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseArticleCategorieComptable.class.getSimpleName(), onQueryCategorieComptableArticle(societe));
                }
            }
            if (value.getCode().equals("code_barre")) {
                if (!donneesBaseSelect.contains(new Value("article"))) {
                    if (useJson) {
                        query += (query.equals("") ? "" : ";") + onQueryArticle(societe);
                    } else {
                        fileName = getResultForQuery(fileName, YvsBaseArticles.class.getSimpleName(), onQueryArticle(societe));
                    }
                    if (useJson) {
                        query += (query.equals("") ? "" : ";") + onQueryConditionnement(societe);
                    } else {
                        fileName = getResultForQuery(fileName, YvsBaseConditionnement.class.getSimpleName(), onQueryConditionnement(societe));
                    }
                }
                if (useJson) {
                    query += (query.equals("") ? "" : ";") + onQueryCodeBarre(societe);
                } else {
                    fileName = getResultForQuery(fileName, YvsBaseArticleCodeBarre.class.getSimpleName(), onQueryCodeBarre(societe));
                }
            }
        }
        if (useJson) {
            fileName = getResultForQuery(fileName, query);
        }
        return fileName;
    }

    private String onQueryCodeAcces(Long societe) {
        return "SELECT y.* FROM yvs_base_code_acces y WHERE y.societe = " + societe;
    }

    private String onQueryExercice(Long societe) {
        return "SELECT y.* FROM yvs_base_exercice y WHERE y.societe = " + societe;
    }

    private String onQueryTypeOD(Long societe) {
        return "SELECT y.* FROM yvs_base_type_doc_divers y WHERE y.societe = " + societe;
    }

    private String onQueryTypeCout(Long societe) {
        return "SELECT y.* FROM yvs_grh_type_cout y WHERE y.societe = " + societe;
    }

    private String onQueryTiersFournisseur(Long societe) {
        return "SELECT y.* FROM yvs_base_tiers y WHERE y.fournisseur IS TRUE AND y.societe = " + societe;
    }

    private String onQueryFournisseur(Long societe) {
        return "SELECT y.* FROM yvs_base_fournisseur y INNER JOIN yvs_base_tiers t ON y.tiers = t.id WHERE t.societe = " + societe;
    }

    private String onQueryTrancheHoraire(Long societe) {
        return "SELECT y.* FROM yvs_grh_tranche_horaire y WHERE y.societe = " + societe;
    }

    private String onQueryCodeBarre(Long societe) {
        return "SELECT y.* FROM yvs_base_article_code_barre y INNER JOIN yvs_base_conditionnement c ON y.conditionnement = c.id INNER JOIN yvs_base_articles a ON c.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryCategorieComptableArticle(Long societe) {
        return "SELECT y.* FROM yvs_base_article_categorie_comptable y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryConditionnement(Long societe) {
        return "SELECT y.* FROM yvs_base_conditionnement y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryArticle(Long societe) {
        return "SELECT y.* FROM yvs_base_articles y INNER JOIN yvs_base_famille_article f ON y.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryTemplateArticle(Long societe) {
        return "SELECT y.* FROM yvs_base_articles_template y WHERE y.societe = " + societe;
    }

    private String onQueryFamilleArticle(Long societe) {
        return "SELECT y.* FROM yvs_base_famille_article y WHERE y.societe = " + societe;
    }

    private String onQueryGroupeArticle(Long societe) {
        return "SELECT y.* FROM yvs_base_groupes_article y WHERE y.societe = " + societe;
    }

    private String onQueryTaxe(Long societe) {
        return "SELECT y.* FROM yvs_base_taxes y WHERE y.societe = " + societe;
    }

    private String onQueryTableConversion(Long societe) {
        return "SELECT y.* FROM yvs_base_table_conversion y INNER JOIN yvs_base_unite_mesure u ON y.unite = u.id WHERE u.societe = " + societe;
    }

    private String onQueryUniteMesure(Long societe) {
        return "SELECT y.* FROM yvs_base_unite_mesure y WHERE y.societe = " + societe;
    }

    private String onQueryCategorieComptable(Long societe) {
        return "SELECT y.* FROM yvs_base_categorie_comptable y WHERE y.societe = " + societe;
    }

    private String onQueryBaseParametre(Long societe) {
        return "SELECT y.* FROM yvs_base_parametre y WHERE y.societe = " + societe;
    }

    private String onQueryClasseStatistique(Long societe) {
        return "SELECT y.* FROM yvs_base_classes_stat y WHERE y.societe = " + societe;
    }

    private String onBackupCommerciales(String fileName, Long societe) {
        if (commercialesSelect.contains(new Value("plan_ristourne")) || commercialesSelect.contains(new Value("plan_rabais"))) {
            if (!donneesBaseSelect.contains(new Value("article"))) {
                if (!donneesBaseSelect.contains(new Value("famille_article"))) {
                    fileName = getResultForQuery(fileName, YvsBaseFamilleArticle.class.getSimpleName(), onQueryFamilleArticle(societe));
                }
                fileName = getResultForQuery(fileName, YvsBaseArticles.class.getSimpleName(), onQueryArticle(societe));
                if (!donneesBaseSelect.contains(new Value("unite_mesure"))) {
                    fileName = getResultForQuery(fileName, YvsBaseUniteMesure.class.getSimpleName(), onQueryUniteMesure(societe));
                }
                fileName = getResultForQuery(fileName, YvsBaseConditionnement.class.getSimpleName(), onQueryConditionnement(societe));
            }
        }
        for (Value value : commercialesSelect) {
            if (value.getCode().equals("com_parametrage")) {
                fileName = getResultForQuery(fileName, YvsComParametre.class.getSimpleName(), onQueryComParametre(societe));
                fileName = getResultForQuery(fileName, YvsComParametreVente.class.getSimpleName(), onQueryComParametreVente(societe));
                fileName = getResultForQuery(fileName, YvsComParametreAchat.class.getSimpleName(), onQueryComParametreAchat(societe));
                fileName = getResultForQuery(fileName, YvsComParametreStock.class.getSimpleName(), onQueryComParametreStock(societe));
            }
            if (value.getCode().equals("mode_paiement")) {
                fileName = getResultForQuery(fileName, YvsBaseModeReglement.class.getSimpleName(), onQueryModePaiement(societe));
                fileName = getResultForQuery(fileName, YvsBaseModeReglementBanque.class.getSimpleName(), onQueryModePaiementBanque(societe));
                fileName = getResultForQuery(fileName, YvsBaseModeReglementInformations.class.getSimpleName(), onQueryModePaiementInformations(societe));
            }
            if (value.getCode().equals("model_reglement")) {
                fileName = getResultForQuery(fileName, YvsBaseModelReglement.class.getSimpleName(), onQueryModelReglement(societe));
            }
            if (value.getCode().equals("plan_commission")) {
                fileName = getResultForQuery(fileName, YvsComPlanCommission.class.getSimpleName(), onQueryPlanCommission(societe));
                fileName = getResultForQuery(fileName, YvsComTypeGrille.class.getSimpleName(), onQueryTypeGrille(societe));
                fileName = getResultForQuery(fileName, YvsComFacteurTaux.class.getSimpleName(), onQueryFacteurTaux(societe));
                fileName = getResultForQuery(fileName, YvsComTrancheTaux.class.getSimpleName(), onQueryTrancheTaux(societe));
            }
            if (value.getCode().equals("plan_ristourne")) {
                fileName = getResultForQuery(fileName, YvsComPlanRistourne.class.getSimpleName(), onQueryPlanRistourne(societe));

                fileName = getResultForQuery(fileName, YvsComRistourne.class.getSimpleName(), onQueryRistourne(societe));
                fileName = getResultForQuery(fileName, YvsComGrilleRistourne.class.getSimpleName(), onQueryGrilleRistourne(societe));
            }
            if (value.getCode().equals("plan_remise")) {
                if (!donneesBaseSelect.contains(new Value("code_acces"))) {
                    fileName = getResultForQuery(fileName, YvsBaseCodeAcces.class.getSimpleName(), onQueryCodeAcces(societe));
                }
                fileName = getResultForQuery(fileName, YvsComRemise.class.getSimpleName(), onQueryRemise(societe));
                fileName = getResultForQuery(fileName, YvsComGrilleRemise.class.getSimpleName(), onQueryGrilleRemise(societe));
            }
            if (value.getCode().equals("plan_rabais")) {
                fileName = getResultForQuery(fileName, YvsComRabais.class.getSimpleName(), onQueryRabais(societe));
                fileName = getResultForQuery(fileName, YvsComGrilleRabais.class.getSimpleName(), onQueryGrilleRabais(societe));
            }
        }
        return fileName;
    }

    private String onQueryGrilleRabais(Long societe) {
        return "SELECT y.* FROM yvs_com_grille_rabais y INNER JOIN yvs_com_rabais r ON y.rabais = r.id INNER JOIN yvs_base_articles a On r.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryRabais(Long societe) {
        return "SELECT y.* FROM yvs_com_rabais y INNER JOIN yvs_base_articles a On y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryGrilleRemise(Long societe) {
        return "SELECT y.* FROM yvs_com_grille_remise y INNER JOIN yvs_com_remise r ON y.remise = r.id WHERE r.societe = " + societe;
    }

    private String onQueryRemise(Long societe) {
        return "SELECT y.* FROM yvs_com_remise y WHERE y.societe = " + societe;
    }

    private String onQueryGrilleRistourne(Long societe) {
        return "SELECT y.* FROM yvs_com_grille_ristourne y INNER JOIN yvs_com_ristourne r ON y.ristourne = r.id INNER JOIN yvs_com_plan_ristourne p ON r.plan = p.id WHERE p.societe = " + societe;
    }

    private String onQueryRistourne(Long societe) {
        return "SELECT y.* FROM yvs_com_ristourne y INNER JOIN yvs_com_plan_ristourne p ON y.plan = p.id WHERE p.societe = " + societe;
    }

    private String onQueryPlanRistourne(Long societe) {
        return "SELECT y.* FROM yvs_com_plan_ristourne y WHERE y.societe = " + societe;
    }

    private String onQueryTrancheTaux(Long societe) {
        return "SELECT y.* FROM yvs_com_tranche_taux y INNER JOIN yvs_com_type_grille t ON y.type_grille = t.id WHERE t.societe = " + societe;
    }

    private String onQueryFacteurTaux(Long societe) {
        return "SELECT y.* FROM yvs_com_facteur_taux y INNER JOIN yvs_com_plan_commission p ON y.plan = p.id WHERE p.societe = " + societe;
    }

    private String onQueryTypeGrille(Long societe) {
        return "SELECT y.* FROM yvs_com_type_grille y WHERE y.societe = " + societe;
    }

    private String onQueryPlanCommission(Long societe) {
        return "SELECT y.* FROM yvs_com_plan_commission y WHERE y.societe = " + societe;
    }

    private String onQueryModelReglement(Long societe) {
        return "SELECT y.* FROM yvs_base_model_reglement y WHERE y.societe = " + societe;
    }

    private String onQueryModePaiement(Long societe) {
        return "SELECT y.* FROM yvs_base_mode_reglement y WHERE y.societe = " + societe;
    }

    private String onQueryModePaiementInformations(Long societe) {
        return "SELECT y.* FROM yvs_base_mode_reglement_informations y INNER JOIN yvs_base_mode_reglement m ON y.mode = m.id WHERE m.societe = " + societe;
    }

    private String onQueryModePaiementBanque(Long societe) {
        return "SELECT y.* FROM yvs_base_mode_reglement_banque y INNER JOIN yvs_base_mode_reglement m ON y.mode = m.id WHERE m.societe = " + societe;
    }

    private String onQueryComParametre(Long societe) {
        return "SELECT y.* FROM yvs_com_parametre y WHERE y.societe = " + societe;
    }

    private String onQueryComParametreVente(Long societe) {
        return "SELECT y.* FROM yvs_com_parametre_vente y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryComParametreAchat(Long societe) {
        return "SELECT y.* FROM yvs_com_parametre_achat y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryComParametreStock(Long societe) {
        return "SELECT y.* FROM yvs_com_parametre_stock y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onBackupProductions(String fileName, Long societe) {
        if (productionsSelect.contains(new Value("nomenclature")) || productionsSelect.contains(new Value("gamme_article"))) {
            if (!donneesBaseSelect.contains(new Value("article"))) {
                if (!donneesBaseSelect.contains(new Value("famille_article"))) {
                    fileName = getResultForQuery(fileName, YvsBaseFamilleArticle.class.getSimpleName(), onQueryFamilleArticle(societe));
                }
                fileName = getResultForQuery(fileName, YvsBaseArticles.class.getSimpleName(), onQueryArticle(societe));
                if (!donneesBaseSelect.contains(new Value("unite_mesure"))) {
                    fileName = getResultForQuery(fileName, YvsBaseUniteMesure.class.getSimpleName(), onQueryUniteMesure(societe));
                }
                fileName = getResultForQuery(fileName, YvsBaseConditionnement.class.getSimpleName(), onQueryConditionnement(societe));
            }
        }
        if (productionsSelect.contains(new Value("nomenclature")) || productionsSelect.contains(new Value("gamme_article")) || productionsSelect.contains(new Value("poste_charge")) || productionsSelect.contains(new Value("equipe_production"))) {
            if (!productionsSelect.contains(new Value("site_production"))) {
                fileName = getResultForQuery(fileName, YvsProdSiteProduction.class.getSimpleName(), onQuerySiteProduction(societe));
            }
        }
        for (Value value : productionsSelect) {
            if (value.getCode().equals("prod_parametrage")) {
                fileName = getResultForQuery(fileName, YvsProdParametre.class.getSimpleName(), onQueryProdParametre(societe));
            }
            if (value.getCode().equals("site_production")) {
                fileName = getResultForQuery(fileName, YvsProdSiteProduction.class.getSimpleName(), onQuerySiteProduction(societe));
            }
            if (value.getCode().equals("nomenclature")) {
                fileName = getResultForQuery(fileName, YvsProdNomenclature.class.getSimpleName(), onQueryNomenclature(societe));
                fileName = getResultForQuery(fileName, YvsProdNomenclatureSite.class.getSimpleName(), onQueryNomenclatureSite(societe));
                fileName = getResultForQuery(fileName, YvsProdComposantNomenclature.class.getSimpleName(), onQueryComposantNomenclature(societe));
            }
            if (value.getCode().equals("gamme_article")) {
                fileName = getResultForQuery(fileName, YvsProdGammeArticle.class.getSimpleName(), onQueryGamme(societe));
                fileName = getResultForQuery(fileName, YvsProdGammeSite.class.getSimpleName(), onQueryGammeSite(societe));
                fileName = getResultForQuery(fileName, YvsProdOperationsGamme.class.getSimpleName(), onQueryOperationGamme(societe));
            }
            if (value.getCode().equals("poste_charge")) {
                if (!humainesSelect.contains(new Value("calendrier"))) {
                    fileName = getResultForQuery(fileName, YvsCalendrier.class.getSimpleName(), onQueryCalendrier(societe));
                }
                fileName = getResultForQuery(fileName, YvsProdPosteCharge.class.getSimpleName(), onQueryPosteCharge(societe));
            }
            if (value.getCode().equals("equipe_production")) {
                fileName = getResultForQuery(fileName, YvsProdEquipeProduction.class.getSimpleName(), onQueryEquipeProduction(societe));
            }
        }
        if (productionsSelect.contains(new Value("nomenclature")) && productionsSelect.contains(new Value("gamme_article"))) {
            fileName = getResultForQuery(fileName, YvsProdComposantOp.class.getSimpleName(), onQueryComposantOperation(societe));
        }
        return fileName;
    }

    private String onQueryEquipeProduction(Long societe) {
        return "SELECT y.* FROM yvs_prod_equipe_production y INNER JOIN yvs_prod_site_production s ON y.site = s.id INNER JOIN yvs_agences a ON s.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryPosteCharge(Long societe) {
        return "SELECT y.* FROM yvs_prod_poste_charge y INNER JOIN yvs_prod_site_production s ON y.site_production = s.id INNER JOIN yvs_agences a ON s.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryGammeSite(Long societe) {
        return "SELECT y.* FROM yvs_prod_gamme_site y INNER JOIN yvs_prod_site_production s ON y.site = s.id INNER JOIN yvs_agences a ON s.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryOperationGamme(Long societe) {
        return "SELECT y.* FROM yvs_prod_operations_gamme y INNER JOIN yvs_prod_gamme_article g ON y.gamme_article = g.id INNER JOIN yvs_base_articles a ON g.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryGamme(Long societe) {
        return "SELECT y.* FROM yvs_prod_gamme_article y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryNomenclatureSite(Long societe) {
        return "SELECT y.* FROM yvs_prod_nomenclature_site y INNER JOIN yvs_prod_site_production s ON y.site = s.id INNER JOIN yvs_agences a ON s.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryComposantOperation(Long societe) {
        return "SELECT y.* FROM yvs_prod_composant_op y INNER JOIN yvs_prod_composant_nomenclature c ON y.composant = c.id INNER JOIN yvs_prod_nomenclature n ON c.nomenclature = n.id INNER JOIN yvs_base_articles a ON n.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryComposantNomenclature(Long societe) {
        return "SELECT y.* FROM yvs_prod_composant_nomenclature y INNER JOIN yvs_prod_nomenclature n ON y.nomenclature = n.id INNER JOIN yvs_base_articles a ON n.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQueryNomenclature(Long societe) {
        return "SELECT y.* FROM yvs_prod_nomenclature y INNER JOIN yvs_base_articles a ON y.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = " + societe;
    }

    private String onQuerySiteProduction(Long societe) {
        return "SELECT y.* FROM yvs_prod_site_production y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryProdParametre(Long societe) {
        return "SELECT y.* FROM yvs_prod_parametre y WHERE y.societe = " + societe;
    }

    private String onBackupComptabilites(String fileName, Long societe) {
        for (Value value : comptabilitesSelect) {
            if (value.getCode().equals("compta_parametrage")) {
                fileName = getResultForQuery(fileName, YvsComptaParametre.class.getSimpleName(), onQueryComptaParametre(societe));
            }
            if (value.getCode().equals("nature_comptable")) {
                fileName = getResultForQuery(fileName, YvsBaseNatureCompte.class.getSimpleName(), onQueryNatureCompte(societe));
            }
            if (value.getCode().equals("plan_comptable")) {
                if (!comptabilitesSelect.contains(new Value("nature_comptable"))) {
                    fileName = getResultForQuery(fileName, YvsBaseNatureCompte.class.getSimpleName(), onQueryNatureCompte(societe));
                }
                fileName = getResultForQuery(fileName, YvsBasePlanComptable.class.getSimpleName(), onQueryPlanComptable(societe));
            }
            if (value.getCode().equals("journaux")) {
                if (!donneesBaseSelect.contains(new Value("code_acces"))) {
                    fileName = getResultForQuery(fileName, YvsBaseCodeAcces.class.getSimpleName(), onQueryCodeAcces(societe));
                }
                fileName = getResultForQuery(fileName, YvsComptaJournaux.class.getSimpleName(), onQueryJournaux(societe));
            }
            if (value.getCode().equals("plan_abonnement")) {
                if (!comptabilitesSelect.contains(new Value("plan_comptable"))) {
                    if (!comptabilitesSelect.contains(new Value("nature_comptable"))) {
                        fileName = getResultForQuery(fileName, YvsBaseNatureCompte.class.getSimpleName(), onQueryNatureCompte(societe));
                    }
                    fileName = getResultForQuery(fileName, YvsBasePlanComptable.class.getSimpleName(), onQueryPlanComptable(societe));
                }
                fileName = getResultForQuery(fileName, YvsComptaPlanAbonnement.class.getSimpleName(), onQueryPlanAbonnement(societe));
            }
            if (value.getCode().equals("centre_analytique")) {
                fileName = getResultForQuery(fileName, YvsComptaPlanAnalytique.class.getSimpleName(), onQueryPlanAnalytique(societe));
                fileName = getResultForQuery(fileName, YvsComptaCentreAnalytique.class.getSimpleName(), onQueryCentreAnalytique(societe));
            }
        }
        return fileName;
    }

    private String onQueryPlanAnalytique(Long societe) {
        return "SELECT y.* FROM yvs_compta_plan_analytique y WHERE y.societe = " + societe;
    }

    private String onQueryCentreAnalytique(Long societe) {
        return "SELECT y.* FROM yvs_compta_centre_analytique y INNER JOIN yvs_compta_plan_analytique p ON y.plan = p.id WHERE p.societe = " + societe;
    }

    private String onQueryPlanAbonnement(Long societe) {
        return "SELECT y.* FROM yvs_compta_plan_abonnement y WHERE y.societe = " + societe;
    }

    private String onQueryJournaux(Long societe) {
        return "SELECT y.* FROM yvs_compta_journaux y INNER JOIN yvs_agences a ON y.agence = a.id WHERE a.societe = " + societe;
    }

    private String onQueryPlanComptable(Long societe) {
        return "SELECT y.* FROM yvs_base_plan_comptable y INNER JOIN yvs_base_nature_compte n ON y.nature_compte = n.id WHERE n.societe = " + societe;
    }

    private String onQueryNatureCompte(Long societe) {
        return "SELECT y.* FROM yvs_base_nature_compte y WHERE y.societe = " + societe;
    }

    private String onQueryComptaParametre(Long societe) {
        return "SELECT y.* FROM yvs_compta_parametre y WHERE y.societe = " + societe;
    }

    private String onBackupHumaines(String fileName, Long societe) {
        if (humainesSelect.contains(new Value("objet_mission")) || humainesSelect.contains(new Value("type_prime")) || humainesSelect.contains(new Value("type_retenue"))) {
            if (!comptabilitesSelect.contains(new Value("plan_comptable"))) {
                if (!comptabilitesSelect.contains(new Value("nature_comptable"))) {
                    fileName = getResultForQuery(fileName, YvsBaseNatureCompte.class.getSimpleName(), onQueryNatureCompte(societe));
                }
                fileName = getResultForQuery(fileName, YvsBasePlanComptable.class.getSimpleName(), onQueryPlanComptable(societe));
            }
        }
        for (Value value : humainesSelect) {
            if (value.getCode().equals("rh_parametrage")) {
                fileName = getResultForQuery(fileName, YvsParametreGrh.class.getSimpleName(), onQueryRhParametre(societe));
            }
            if (value.getCode().equals("calendrier")) {
                fileName = getResultForQuery(fileName, YvsCalendrier.class.getSimpleName(), onQueryCalendrier(societe));
                fileName = getResultForQuery(fileName, YvsJoursOuvres.class.getSimpleName(), onQueryJourOuvree(societe));
            }
            if (value.getCode().equals("objet_mission")) {
                fileName = getResultForQuery(fileName, YvsGrhObjetsMission.class.getSimpleName(), onQueryObjetMission(societe));
            }
            if (value.getCode().equals("type_prime") || value.getCode().equals("type_retenue")) {
                fileName = getResultForQuery(fileName, YvsGrhTypeElementAdditionel.class.getSimpleName(), onQueryTypePrimeOrRetenue(societe));
            }
            if (value.getCode().equals("plan_prelevement")) {
                fileName = getResultForQuery(fileName, YvsGrhPlanPrelevement.class.getSimpleName(), onQueryPlanPrelevement(societe));
            }
            if (value.getCode().equals("categorie_profesionnelle")) {
                fileName = getResultForQuery(fileName, YvsGrhCategorieProfessionelle.class.getSimpleName(), onQueryCategorieProfessionnelle(societe));
            }
            if (value.getCode().equals("echellon_profesionnelle")) {
                fileName = getResultForQuery(fileName, YvsGrhEchelons.class.getSimpleName(), onQueryEchellonProfessionnelle(societe));
            }
            if (value.getCode().equals("convention_collective")) {
                if (!humainesSelect.contains(new Value("categorie_profesionnelle"))) {
                    fileName = getResultForQuery(fileName, YvsGrhCategorieProfessionelle.class.getSimpleName(), onQueryCategorieProfessionnelle(societe));
                }
                if (!humainesSelect.contains(new Value("echellon_profesionnelle"))) {
                    fileName = getResultForQuery(fileName, YvsGrhEchelons.class.getSimpleName(), onQueryEchellonProfessionnelle(societe));
                }
                fileName = getResultForQuery(fileName, YvsGrhConventionCollective.class.getSimpleName(), onQueryConventionCollective(societe));
            }
            if (value.getCode().equals("jour_ferie")) {
                fileName = getResultForQuery(fileName, YvsJoursFeries.class.getSimpleName(), onQueryJourFerie(societe));
            }
            if (value.getCode().equals("departement")) {
                fileName = getResultForQuery(fileName, YvsGrhDepartement.class.getSimpleName(), onQueryDepartement(societe));
            }
            if (value.getCode().equals("poste_travail")) {
                if (!humainesSelect.contains(new Value("departement"))) {
                    fileName = getResultForQuery(fileName, YvsGrhDepartement.class.getSimpleName(), onQueryDepartement(societe));
                }
                fileName = getResultForQuery(fileName, YvsGrhPosteDeTravail.class.getSimpleName(), onQueryPosteTravail(societe));
            }
            if (value.getCode().equals("structure_salaire")) {
                fileName = getResultForQuery(fileName, YvsGrhStructureSalaire.class.getSimpleName(), onQueryStructureSalaire(societe));
            }
            if (value.getCode().equals("regle_salaire")) {
                fileName = getResultForQuery(fileName, YvsGrhCategorieElement.class.getSimpleName(), onQueryCategorieSalaire(societe));
                fileName = getResultForQuery(fileName, YvsGrhRubriqueBulletin.class.getSimpleName(), onQueryRubriqueBulletin(societe));
                fileName = getResultForQuery(fileName, YvsGrhElementSalaire.class.getSimpleName(), onQueryRegleSalaire(societe));
            }

        }
        return fileName;
    }

    private String onQueryCategorieSalaire(Long societe) {
        return "SELECT y.* FROM yvs_grh_categorie_element y WHERE y.societe = " + societe;
    }

    private String onQueryRubriqueBulletin(Long societe) {
        return "SELECT y.* FROM yvs_grh_rubrique_bulletin y WHERE y.societe = " + societe;
    }

    private String onQueryRegleSalaire(Long societe) {
        return "SELECT y.* FROM yvs_grh_element_salaire y INNER JOIN yvs_grh_rubrique_bulletin r ON y.rubrique = r.id WHERE r.societe = " + societe;
    }

    private String onQueryStructureSalaire(Long societe) {
        return "SELECT y.* FROM yvs_grh_structure_salaire y WHERE y.societe = " + societe;
    }

    private String onQueryPosteTravail(Long societe) {
        return "SELECT y.* FROM yvs_grh_poste_de_travail y INNER JOIN yvs_grh_departement d ON y.departement = y.id WHERE d.societe = " + societe;
    }

    private String onQueryDepartement(Long societe) {
        return "SELECT y.* FROM yvs_grh_departement y WHERE y.societe = " + societe;
    }

    private String onQueryJourFerie(Long societe) {
        return "SELECT y.* FROM yvs_jours_feries y WHERE y.societe = " + societe;
    }

    private String onQueryConventionCollective(Long societe) {
        return "SELECT y.* FROM yvs_grh_convention_collective y INNER JOIN yvs_grh_echelons e On y.echellon = e.id WHERE e.societe = " + societe;
    }

    private String onQueryEchellonProfessionnelle(Long societe) {
        return "SELECT y.* FROM yvs_grh_echelons y WHERE y.societe = " + societe;
    }

    private String onQueryCategorieProfessionnelle(Long societe) {
        return "SELECT y.* FROM yvs_grh_categorie_professionelle y WHERE y.societe = " + societe;
    }

    private String onQueryPlanPrelevement(Long societe) {
        return "SELECT y.* FROM yvs_grh_plan_prelevement y WHERE y.societe = " + societe;
    }

    private String onQueryTypePrimeOrRetenue(Long societe) {
        return "SELECT y.* FROM yvs_grh_type_element_additionel y WHERE y.societe = " + societe;
    }

    private String onQueryObjetMission(Long societe) {
        return "SELECT y.* FROM yvs_grh_objets_mission y INNER JOIN yvs_base_plan_comptable p ON y.compte_charge = p.id INNER JOIN yvs_base_nature_compte n ON p.nature_compte = n.id WHERE n.societe = " + societe;
    }

    private String onQueryCalendrier(Long societe) {
        return "SELECT y.* FROM yvs_calendrier y WHERE y.societe = " + societe;
    }

    private String onQueryJourOuvree(Long societe) {
        return "SELECT y.* FROM yvs_jours_ouvres y INNER JOIN yvs_calendrier c ON y.calendrier = c.id WHERE c.societe = " + societe;
    }

    private String onQueryRhParametre(Long societe) {
        return "SELECT y.* FROM yvs_parametre_grh y WHERE y.societe = " + societe;
    }

    private String onBackupMutuelles(String fileName, Long societe) {
        for (Value value : mutuellesSelect) {

        }
        return fileName;
    }

    private String onBackupProjets(String fileName, Long societe) {
        for (Value value : projetsSelect) {

        }
        return fileName;
    }

    private String onBackupRelations(String fileName, Long societe) {
        for (Value value : relationsSelect) {

        }
        return fileName;
    }

    public class Value {

        String code;
        String label;
        String description;

        public Value() {
        }

        public Value(String code) {
            this.code = code;
        }

        public Value(String code, String label, String description) {
            this(code);
            this.label = label;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 13 * hash + Objects.hashCode(this.code);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Value other = (Value) obj;
            if (!Objects.equals(this.code, other.code)) {
                return false;
            }
            return true;
        }

    }

    public class EntityDeserializer<T> implements JsonDeserializer<T> {

        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            T instance = null;
            if (je.isJsonObject()) {
                String expression = je.toString();
                Object object = je.getAsJsonObject();
            } else {
                Constructor<T>[] constructors = (Constructor<T>[]) ((Class) type).getDeclaredConstructors();
                Constructor<T> constructor = null;
                for (Constructor cstr : constructors) {
                    //Only if default constructor
                    if (cstr.getParameterAnnotations().length == 0) {
                        constructor = (Constructor<T>) cstr;
                        break;
                    }
                }
                if (constructor != null) {
                    try {
                        instance = constructor.newInstance();
                        if (instance != null) {
                            Method setId = ((Class) type).getMethod("setId", new Class[]{Long.class});
                            if (setId != null) {
                                setId.invoke(instance, je.getAsLong());
                            }
                        }
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
                        Logger.getLogger(ManagedBackupRestore.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return instance;
        }

    }

}
