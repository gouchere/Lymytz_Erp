/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import lymytz.navigue.Navigations;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.UtilCompta;
import yvs.base.tiers.Tiers;
import yvs.commercial.ManagedCatCompt;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.ManagedModeReglement;
import yvs.commercial.UtilCom;
import yvs.commercial.client.Client;
import yvs.commercial.client.ManagedClient;
import yvs.commercial.creneau.CreneauUsers;
import yvs.commercial.depot.ManagedPointVente;
import yvs.commercial.depot.PointVente;
import yvs.comptabilite.ManagedSaisiePiece;
import yvs.comptabilite.caisse.ManagedVirement;
import yvs.comptabilite.tresorerie.PieceTresorerie;
import yvs.dao.Options;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.YvsComCommercialPoint;
import yvs.entity.commercial.client.YvsBaseCategorieClient;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.vente.YvsComContenuDocVente;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.commercial.vente.YvsComEnteteDocVente;
import yvs.entity.commercial.vente.YvsComMensualiteFactureVente;
import yvs.entity.compta.YvsBaseCaisse;
import yvs.entity.compta.YvsComptaCaissePieceVirement;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsers;
import yvs.grh.UtilGrh;
import yvs.grh.presence.TrancheHoraire;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.entrepot.Depots;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Constantes;
import yvs.util.PaginatorResult;
import yvs.util.ParametreRequete;
import yvs.util.Utilitaire;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedVente extends ManagedCommercial<EnteteDocVente, YvsComEnteteDocVente> implements Serializable {

    private EnteteDocVente entete = new EnteteDocVente();
    private List<YvsComEnteteDocVente> documents;
    private YvsComEnteteDocVente selectDoc;

    public PaginatorResult<YvsComDocVentes> p_document = new PaginatorResult<>();
    private DocVente docVente = new DocVente();
    private YvsComDocVentes selectVente = new YvsComDocVentes(), selectFacture = new YvsComDocVentes();
    private List<YvsComContenuDocVente> contenus;
    private List<YvsComMensualiteFactureVente> mensualites;
    private List<YvsUsers> vendeurs;
    private int position = 0, index = -1;
    private String saveOption = "U", msgOption = "";
    private List<String> typesDoc;

    private List<YvsBaseCaisse> caisseCible;
    private PieceTresorerie virement = new PieceTresorerie();

    private Boolean comptaSearch;
    private long nbrComptaSearch;
    private boolean form_edit = false;

    public ManagedVente() {
        documents = new ArrayList<>();
        vendeurs = new ArrayList<>();
        contenus = new ArrayList<>();
        mensualites = new ArrayList<>();
        caisseCible = new ArrayList<>();
        typesDoc = new ArrayList<>();
    }

    public List<YvsUsers> getVendeurs() {
        return vendeurs;
    }

    public void setVendeurs(List<YvsUsers> vendeurs) {
        this.vendeurs = vendeurs;
    }

    public long getNbrComptaSearch() {
        return nbrComptaSearch;
    }

    public void setNbrComptaSearch(long nbrComptaSearch) {
        this.nbrComptaSearch = nbrComptaSearch;
    }

    public YvsComDocVentes getSelectVente() {
        return selectVente;
    }

    public void setSelectVente(YvsComDocVentes selectVente) {
        this.selectVente = selectVente;
    }

    public List<YvsBaseCaisse> getCaisseCible() {
        return caisseCible;
    }

    public void setCaisseCible(List<YvsBaseCaisse> caisseCible) {
        this.caisseCible = caisseCible;
    }

    public PieceTresorerie getVirement() {
        return virement;
    }

    public void setVirement(PieceTresorerie virement) {
        this.virement = virement;
    }

    public Boolean getComptaSearch() {
        return comptaSearch;
    }

    public void setComptaSearch(Boolean comptaSearch) {
        this.comptaSearch = comptaSearch;
    }

    public YvsComDocVentes getSelectFacture() {
        return selectFacture;
    }

    public void setSelectFacture(YvsComDocVentes selectFacture) {
        this.selectFacture = selectFacture;
    }

    public String getMsgOption() {
        return msgOption;
    }

    public void setMsgOption(String msgOption) {
        this.msgOption = msgOption;
    }

    public String getSaveOption() {
        return saveOption;
    }

    public void setSaveOption(String saveOption) {
        this.saveOption = saveOption;
    }

    public List<YvsComMensualiteFactureVente> getMensualites() {
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureVente> mensualites) {
        this.mensualites = mensualites;
    }

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    public boolean isForm_edit() {
        return form_edit;
    }

    public void setForm_edit(boolean form_edit) {
        this.form_edit = form_edit;
    }

    public YvsComEnteteDocVente getSelectDoc() {
        return selectDoc;
    }

    public void setSelectDoc(YvsComEnteteDocVente selectDoc) {
        this.selectDoc = selectDoc;
    }

    public PaginatorResult<YvsComDocVentes> getP_document() {
        return p_document;
    }

    public void setP_document(PaginatorResult<YvsComDocVentes> p_document) {
        this.p_document = p_document;
    }

    public DocVente getDocVente() {
        return docVente;
    }

    public void setDocVente(DocVente docVente) {
        this.docVente = docVente;
    }

    public List<String> getTypesDoc() {
        if (typesDoc != null ? typesDoc.isEmpty() : true) {
            typesDoc = new ArrayList<String>() {
                {
                    add(Constantes.TYPE_FV);
                    add(Constantes.TYPE_BCV);
                }
            };
        }
        return typesDoc;
    }

    public void setTypesDoc(List<String> typesDoc) {
        this.typesDoc = typesDoc;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public EnteteDocVente getEntete() {
        return entete;
    }

    public void setEntete(EnteteDocVente entete) {
        this.entete = entete;
    }

    public List<YvsComEnteteDocVente> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComEnteteDocVente> documents) {
        this.documents = documents;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void doNothing() {
    }

    @Override
    public void loadAll() {
        loadAll(Constantes.TYPE_FV + "," + Constantes.TYPE_BCV);
    }

    public void loadEdit(boolean edit) {
        form_edit = edit;
        if (edit) {

        } else {
            loadAll();
        }
    }

    public void loadAll(String typeDoc) {
        if (typeDoc != null ? !typeDoc.trim().isEmpty() : false) {
            setTypesDoc(Arrays.asList(typeDoc.split(",")));
        }
        loadAll(true, true);
    }

    public void loadAll(boolean avance, boolean init) {
        ParametreRequete p;
        switch (buildDocByDroit(Constantes.TYPE_FV)) {
            case 1:  //charge tous les documents de la société
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", currentAgence.getSociete(), "=", "AND");
                paginator.addParam(p);
                break;
            case 2: //charge tous les documents de l'agence
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", currentAgence, "=", "AND");
                paginator.addParam(p);
                break;
            case 3: {//charge tous les document des points de vente où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{currentUser.getUsers(), (Utilitaire.getIniTializeDate(new Date()).getTime()), Constantes.getPreviewDate(new Date())});
                if (ids.isEmpty()) {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            }
            case 4: {//charge tous les document des depots où l'utilisateurs est responsable
                //cherche les points de vente de l'utilisateur rattaché au depot
                List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdsDepotByUsers", new String[]{"users"}, new Object[]{currentUser.getUsers()});
                if (currentUser.getUsers() != null) {
                    ids.addAll(dao.loadNameQueries("YvsBaseDepots.findIdByResponsable", new String[]{"responsable"}, new Object[]{currentUser.getUsers().getEmploye()}));
                }
                if (!ids.isEmpty()) {
                    ids = dao.loadNameQueries("YvsBasePointVenteDepot.findIdPointByDepot", new String[]{"ids"}, new Object[]{ids});
                    if (ids.isEmpty()) {
                        ids.add(-1L);
                    }
                } else {
                    ids.add(-1L);
                }
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.id", "ids", ids, " IN ", "AND");
                paginator.addParam(p);
                break;
            }
            default:    //charge les document de l'utilisateur connecté dans les restriction de date données
                p = new ParametreRequete("y.enteteDoc.creneau.users ", "users", currentUser.getUsers(), "=", "AND");
                paginator.addParam(p);
                break;

        }
        paginator.addParam(new ParametreRequete("y.typeDoc", "typeDoc", getTypesDoc(), "IN", "AND"));
        documents = paginator.executeDynamicQuery("DISTINCT(y.enteteDoc)", "DISTINCT(y.enteteDoc)", "YvsComDocVentes y", "y.enteteDoc.dateEntete DESC", avance, init, (int) imax, dao);
    }

    public void parcoursInAllResult(boolean avancer) {
        setOffset((avancer) ? (getOffset() + 1) : (getOffset() - 1));
        if (getOffset() < 0 || getOffset() >= (paginator.getNbPage() * getNbMax())) {
            setOffset(0);
        }
        List<YvsComEnteteDocVente> re = paginator.parcoursDynamicData("YvsComDocVentes", "DISTINCT(y.enteteDoc)", "y", "y.enteteDoc.dateEntete DESC", getOffset(), dao);
        if (!re.isEmpty()) {
            onSelectObject(re.get(0));
        }
    }

    public void loadHeaderByStatut(String statut) {
        setTypesDoc(Arrays.asList(new String[]{Constantes.TYPE_FV, Constantes.TYPE_BCV}));
        paginator.getParams().clear();
        statut_ = statut;
        addParamStatut();
    }

    public void init(boolean next) {
        if (form_edit) {
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                m.init(next);
            }
        } else {
            loadAll(next, false);
        }
    }

    @Override
    public boolean controleFiche(EnteteDocVente bean) {
        if (form_edit) {
            ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
            if (m != null) {
                return m.controleFiche(docVente);
            }
        } else {
            return true;
        }
        return false;
    }

    @Override
    public EnteteDocVente recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DocVente recopieViewFacture() {
        docVente.setTypeDoc(Constantes.TYPE_FV);
        return docVente;
    }

    @Override
    public void populateView(EnteteDocVente bean) {
//        bean.setTotalTTC(dao.loadCaEnteteVente(bean.getId()));
        cloneObject(entete, bean);
        if (entete.getDocuments() != null ? !entete.getDocuments().isEmpty() : false) {
            position = 0;
        } else {
            position = -1;
        }
        onSelectFacture(position);
        setMontantEntete(entete);
        update("blog_form_entete_vente");
    }

    public void populateView(DocVente bean) {
        cloneObject(docVente, bean);
        cloneObject(entete, bean.getEnteteDoc());
        docVente.getClient().setNom(bean.getClient().getNom_prenom());
        choosePoint();
        chooseDepot();
        ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
        if (m != null) {
            m.loadPointVenteByDroit(new YvsUsers(entete.getUsers().getId()));
        }
        setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
        update("blog_form_entete_vente");
    }

    @Override
    public void resetFiche() {
        resetFiche(entete);
        entete.setCrenauHoraire(new CreneauUsers());
        entete.setPoint(new PointVente());
        entete.setUsers(new Users());
        entete.setTranchePoint(new TrancheHoraire());
        entete.setTranche(new TrancheHoraire());
        entete.getDocuments().clear();

        docVente = new DocVente();
        selectDoc = null;
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onSelectDistant(YvsComEnteteDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            onSelectObject(y);
            Navigations n = (Navigations) giveManagedBean(Navigations.class);
            if (n != null) {
                n.naviguationView("Journaux de vente", "modGescom", "smenEnteteVente", true);
            }
        }
    }

    @Override
    public void onSelectObject(YvsComEnteteDocVente y) {
        selectDoc = y;
        y.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{y, Constantes.TYPE_FV}));
        populateView(UtilCom.buildBeanEnteteDocVente(y));
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        YvsComEnteteDocVente y = (YvsComEnteteDocVente) ev.getObject();
        onSelectObject(y);
    }

    public void loadOnViewFacture(SelectEvent ev) {
        YvsComDocVentes y = (YvsComDocVentes) ev.getObject();
        onSelectObjectFacture(y);
    }

    public void onSelectObjectFacture(YvsComDocVentes y) {
        selectVente = y;
        contenus = dao.loadNameQueries("YvsComContenuDocVente.findByDocVente", new String[]{"docVente"}, new Object[]{y});
        mensualites = dao.loadNameQueries("YvsComMensualiteFactureVente.findByFacture", new String[]{"facture"}, new Object[]{y});
        populateView(UtilCom.buildBeanDocVente(y));
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        resetFiche();
    }

    public void onSelectFacture(int index) {
        if (index > -1 && (entete != null ? entete.getId() > 0 : false)) {
            if (entete.getDocuments() != null ? (!entete.getDocuments().isEmpty() ? entete.getDocuments().size() > index : false) : false) {
                selectFacture = entete.getDocuments().get(index);
                docVente = UtilCom.buildBeanDocVente(selectFacture);
                setMontantTotalDoc(docVente, docVente.getContenus(), currentAgence.getSociete().getId(), null, null, dao);
            }
        }
    }

    public void onSelectEntete(int index) {
        if (index > -1) {
            if (getDocuments() != null ? (!getDocuments().isEmpty() ? getDocuments().size() > index : false) : false) {
                onSelectObject(getDocuments().get(index));
            }
        } else {
            entete = new EnteteDocVente();
        }
    }

    public void navigueOnViewFacture(boolean next) {
        if (entete.getDocuments() != null ? !entete.getDocuments().isEmpty() : false) {
            if (next) {
                position += 1;
                position = entete.getDocuments().size() > position ? position : entete.getDocuments().size();
            } else {
                position -= 1;
                position = position > 0 ? position : 0;
            }
        } else {
            position = -1;
        }
        onSelectFacture(position);
    }

    public void loadOnViewEntete(boolean next) {
        if (getDocuments() != null ? !getDocuments().isEmpty() : false) {
            if (next) {
                index += 1;
                index = getDocuments().size() > index ? index : getDocuments().size();
            } else {
                index -= 1;
                index = index > -1 ? index : -1;
            }
        } else {
            index = -1;
        }
        onSelectEntete(index);
    }

    @Override
    public void choosePaginator(ValueChangeEvent ev) {
        super.choosePaginator(ev);
        loadAll(true, true);
    }

    public void cloturer(YvsComEnteteDocVente y) {
        selectDoc = y;
        update("id_confirm_close_entete_fv");
    }

    public void cloturer() {
        if (selectDoc == null) {
            return;
        }

        selectDoc.setCloturer(!selectDoc.getCloturer());
        selectDoc.setDateCloturer(selectDoc.getCloturer() ? new Date() : null);
        selectDoc.setCloturerBy(selectDoc.getCloturer() ? currentUser.getUsers() : null);
        if (currentUser != null ? currentUser.getId() > 0 : false) {
            selectDoc.setAuthor(currentUser);
        }
        dao.update(selectDoc);
        if (documents.contains(selectDoc)) {
            documents.set(documents.indexOf(selectDoc), selectDoc);
            update("data_entete_vente");
        }
        // recalculer les prix sur la facture
        String query = "SELECT update_pr_article_(?,?,?,?)";
//        boolean re = dao.callFonctionBool(query, new Options[]{new Options(selectDoc.getAgence(), 1), new Options(selectDoc.getDateEntete(), 2), new Options(selectDoc.getDateEntete(), 3), new Options(mouvements.get(0).getConditionnement().getId(), 4)});
    }

    public void equilibre(YvsComEnteteDocVente y) {
        selectDoc = y;
        equilibre();
    }

    public void equilibre() {
        if ((selectDoc != null) ? (selectDoc.getId() != null ? selectDoc.getId() > 0 : false) : false) {
            dao.getEquilibreEnteteVente(selectDoc.getId());
            succes();
        }
    }

    @Override
    public void cleanEnteteVente() {
        if (!autoriser("fv_clean_header")) {
            openNotAcces();
            return;
        }
        super.cleanEnteteVente();
        loadAll(true, true);
    }

    public void clearParams() {
        societe_ = 0;
        agence_ = 0;
        codeVendeur_ = null;
        point_ = 0;
        tranche_ = 0;
        idsSearch = "";
        statut_ = null;
        statutLivre_ = null;
        statutRegle_ = null;
        date_ = false;
        dateDebut_ = new Date();
        dateFin_ = new Date();
        paginator.getParams().clear();
        loadAll(true, true);
    }

    public void addParamSociete(long societe_) {
        this.societe_ = societe_;
        addParamSociete();
    }

    public void addParamSociete() {
        ParametreRequete p;
        if (societe_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence.societe", "societe", new YvsSocietes(societe_));
            p.setOperation("=");
            p.setPredicat("AND");
            if (paginator.getParams().contains(new ParametreRequete("agence"))) {
                paginator.getParams().remove(new ParametreRequete("agence"));
            }
            if (paginator.getParams().contains(new ParametreRequete("depot"))) {
                paginator.getParams().remove(new ParametreRequete("depot"));
            }
        } else {
            p = new ParametreRequete("y.enteteDoc.author.agence.societe", "societe", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamAgence(long agence_) {
        this.agence_ = agence_;
        addParamAgence();
    }

    public void addParamAgence() {
        ParametreRequete p;
        if (agence_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point.agence", "agence", new YvsAgences(agence_));
            p.setOperation("=");
            p.setPredicat("AND");
            if (paginator.getParams().contains(new ParametreRequete("societe"))) {
                paginator.getParams().remove(new ParametreRequete("societe"));
            }
            if (paginator.getParams().contains(new ParametreRequete("depot"))) {
                paginator.getParams().remove(new ParametreRequete("depot"));
            }
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot.agence", "agence", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDepot(int depot_) {
        this.depot_ = depot_;
        addParamDepot();
    }

    public void addParamDepot() {
        ParametreRequete p;
        if (depot_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", new YvsBaseDepots(depot_));
            p.setOperation("=");
            p.setPredicat("AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.depot", "depot", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamPoint(long point_) {
        this.point_ = point_;
        addParamPoint();
    }

    public void choosePoint_(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                point_ = id;
                addParamPoint();
            } else {
                boolean pagine = false;
                boolean next = false;
                if (id == -1) {
                    pagine = true;
                    next = false;
                } else if (id == -2) {
                    pagine = true;
                    next = true;
                }
                if (pagine) {
                    ManagedPointVente m = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (m != null) {
                        m.loadPointVenteByDroit(next, false);
                    }
                }
                entete.getPoint().setId(0);
            }
        }
    }

    public void addParamPoint() {
        ParametreRequete p;
        if (point_ > 0) {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", new YvsBasePointVente(point_), "=", "AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.point", "point", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamTranche(long tranche_, boolean point) {
        this.tranche_ = tranche_;
        addParamTranche(point);
    }

    public void addParamTranche(boolean point) {
        ParametreRequete p;
        if (point) {
            if (tranche_ > 0) {
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_), "=", "AND");
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.creneauPoint.tranche", "tranche", null);
            }
        } else {
            if (tranche_ > 0) {
                p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", new YvsGrhTrancheHoraire(tranche_), "=", "AND");
            } else {
                p = new ParametreRequete("y.enteteDoc.creneau.creneauDepot.tranche", "tranche", null);
            }
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDate(boolean date_, Date dateDebut_, Date dateFin_) {
        this.date_ = date_;
        this.dateDebut_ = dateDebut_;
        this.dateFin_ = dateFin_;
        addParamDate();
    }

    public void addParamDate() {
        ParametreRequete p;
        if (date_) {
            p = new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", dateDebut_, dateFin_, "BETWEEN", "AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.dateEntete", "dateEntete", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamDates() {
        ParametreRequete p = new ParametreRequete("y.dateUpdate", "dateUpdate", null);
        if (date_up) {
            p = new ParametreRequete("y.dateUpdate", "dateUpdate", dateDebut_, dateFin_, "BETWEEN", "AND");
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamVendeur(String codeVendeur_) {
        this.codeVendeur_ = codeVendeur_;
        addParamVendeur();
    }

    public void addParamVendeur() {
        ParametreRequete p;
        if (codeVendeur_ != null ? codeVendeur_.trim().length() > 0 : false) {
            p = new ParametreRequete(null, "vendeur", codeVendeur_ + "%", "LIKE", "AND");
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.nomUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", "LIKE", "OR"));
            p.getOtherExpression().add(new ParametreRequete("UPPER(y.enteteDoc.creneau.users.codeUsers)", "vendeur", codeVendeur_.toUpperCase() + "%", "LIKE", "OR"));
        } else {
            p = new ParametreRequete("y.enteteDoc.creneau.users.codeUsers", "vendeur", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamStatut(String statut_) {
        this.statut_ = statut_;
        addParamStatut();
    }

    public void addParamStatut() {
        ParametreRequete p;
        if (statut_ != null ? statut_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.enteteDoc.etat", "statut", statut_, "=", "AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.etat", "statut", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamComptabilised() {
        ParametreRequete p = new ParametreRequete("coalesce(y.enteteDoc.comptabilise, false)", "comptabilise", comptaSearch, "=", "AND");
        if (comptaSearch != null) {
            String query = "SELECT COUNT(DISTINCT y.id) FROM yvs_compta_content_journal_entete_facture_vente c RIGHT JOIN yvs_com_entete_doc_vente y ON c.entete = y.id "
                    + "INNER JOIN yvs_com_creneau_horaire_users h ON y.creneau = h.id INNER JOIN yvs_com_creneau_point cp ON h.creneau_point = cp.id "
                    + "INNER JOIN yvs_base_point_vente p ON cp.point = p.id INNER JOIN yvs_agences a ON p.agence = a.id INNER JOIN yvs_com_doc_ventes d ON d.entete_doc = y.id "
                    + "WHERE d.type_doc = 'FV' AND y.etat = 'V' AND a.societe = ? AND c.id " + (comptaSearch ? "IS NOT NULL" : "IS NULL");
            Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1)};
            if (date_) {
                query += " AND y.date_entete BETWEEN ? AND ?";
                param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(dateDebut_, 2), new Options(dateFin_, 3)};
            }
            Long count = (Long) dao.loadObjectBySqlQuery(query, param);
            nbrComptaSearch = count != null ? count : 0;
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamStatutLivre(String statutLivre_) {
        this.statutLivre_ = statutLivre_;
        addParamStatutLivre();
    }

    public void addParamStatutLivre() {
        ParametreRequete p;
        if (statutLivre_ != null ? statutLivre_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.enteteDoc.statutLivre", "statutLivre", statutLivre_, "=", "AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.statutLivre", "statutLivre", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void addParamStatutRegle(String statutRegle_) {
        this.statutRegle_ = statutRegle_;
        addParamStatutRegle();
    }

    public void addParamStatutRegle() {
        ParametreRequete p;
        if (statutRegle_ != null ? statutRegle_.trim().length() > 0 : false) {
            p = new ParametreRequete("y.enteteDoc.statutRegle", "statutRegle", statutRegle_, "=", "AND");
        } else {
            p = new ParametreRequete("y.enteteDoc.statutRegle", "statutRegle", null);
        }
        paginator.addParam(p);
        loadAll(true, true);
    }

    public void verifyComptabilised(Boolean comptabilised) {
        loadAll(true, true);
        if (comptabilised != null) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                List<YvsComEnteteDocVente> list = new ArrayList<>();
                list.addAll(documents);
                for (YvsComEnteteDocVente y : list) {
                    y.setComptabilised(w.isComptabilise(y.getId(), Constantes.SCR_HEAD_VENTE));
                    if (comptabilised ? !y.isComptabilised() : y.isComptabilised()) {
                        documents.remove(y);
                    }
                }
            }
        }
        update("data_entete_vente");
    }

    public void annuler(boolean force, boolean suspend) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getCloturer()) {
                getErrorMessage("Validation impossible... Car ce journal est cloturé");
                return;
            }
            if (changeStatut_(suspend ? Constantes.ETAT_ANNULE : Constantes.ETAT_EDITABLE)) {
                selectDoc.setAuthor(currentUser);
                selectDoc.setCloturer(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setValiderBy(null);
                selectDoc.setDateValider(null);
                dao.update(selectDoc);
                entete.setValiderBy(new Users());
                succes();
                update("form_entete_vente");
            }
        }
    }

    public void valider(boolean force) {
        if (selectDoc != null ? selectDoc.getId() > 0 : false) {
            if (selectDoc.getCloturer()) {
                getErrorMessage("Validation impossible... Car ce journal est cloturé");
                return;
            }
            if (changeStatut_(Constantes.ETAT_VALIDE)) {
                selectDoc.setAuthor(currentUser);
                selectDoc.setCloturer(null);
                selectDoc.setDateCloturer(null);
                selectDoc.setValiderBy(currentUser.getUsers());
                selectDoc.setDateValider(new Date());
                dao.update(selectDoc);
                entete.setValiderBy(UtilUsers.buildBeanUsers(selectDoc.getValiderBy()));
                entete.setDateValider(new Date());
                succes();
                update("form_entete_vente");
            }
        }
    }

    public boolean changeStatut(String etat) {
        if (changeStatut_(etat)) {
            succes();
            return true;
        }
        return false;
    }

    private boolean changeStatut_(String etat) {
        if ((etat != null ? etat.trim().length() > 0 : false) && (entete != null ? entete.getId() > 0 : false)) {
            String rq = "UPDATE yvs_com_entete_doc_vente SET etat = '" + etat + "'  WHERE id=?";
            Options[] param = new Options[]{new Options(entete.getId(), 1)};
            dao.requeteLibre(rq, param);
            entete.setEtat(etat);
            int idx = documents.indexOf(new YvsComEnteteDocVente(entete.getId()));
            if (idx > -1) {
                documents.get(idx).setEtat(etat);
                update("data_entete_vente");
            }
            update("grp_btn_etat_entete_vente");
            return true;
        }
        return false;
    }

    public void setMontantEntete(EnteteDocVente bean) {
        if (bean != null ? bean.getId() > 0 : false) {
            bean.setMontantRemise(0);
            bean.setMontantTaxe(0);
            bean.setMontantRistourne(0);
            bean.setMontantCommission(0);
            bean.setMontantHT(0);
            bean.setMontantRemises(0);
            bean.setMontantCoutSup(0);
            bean.setMontantAvance(0);
            bean.setMontantTaxeR(0);
            bean.setMontantTTC(0);

            for (YvsComDocVentes y : bean.getDocuments()) {
                DocVente d = new DocVente(y.getId());
                setMontantTotalDoc(d);
                bean.setMontantRemise(bean.getMontantRemise() + d.getMontantRemise());
                bean.setMontantTaxe(bean.getMontantTaxe() + d.getMontantTaxe());
                bean.setMontantRistourne(bean.getMontantRistourne() + d.getMontantRistourne());
                bean.setMontantCommission(bean.getMontantCommission() + d.getMontantCommission());
                bean.setMontantTTC(bean.getMontantTTC() + d.getMontantTTC());
                bean.setMontantHT(bean.getMontantHT() + d.getMontantHT());
                bean.setMontantRemises(bean.getMontantRemises() + d.getMontantRemises());
                bean.setMontantCoutSup(bean.getMontantCoutSup() + d.getMontantCS());
                bean.setMontantAvance(bean.getMontantAvance() + d.getMontantAvance());
                bean.setMontantTaxeR(bean.getMontantTaxeR() + d.getMontantTaxeR());
            }
            update("blog_form_montant_entete_vente");
        }
    }

    public YvsComDocVentes reBuildNumero(YvsComDocVentes y, String type, boolean save, boolean msg) {
        if (y != null ? (y.getId() > 0
                ? (y.getEnteteDoc() != null ? (y.getEnteteDoc().getId() > 0
                ? (y.getEnteteDoc().getCreneau() != null ? (y.getEnteteDoc().getCreneau().getId() > 0
                ? (y.getEnteteDoc().getCreneau().getCreneauPoint() != null ? y.getEnteteDoc().getCreneau().getCreneauPoint().getId() > 0 : false)
                : false) : false) : false) : false) : false) : false) {
            String num = genererReference(giveNameType(type), y.getEnteteDoc().getDateEntete(), y.getEnteteDoc().getCreneau().getCreneauPoint().getPoint().getId(), Constantes.POINTVENTE);
            if (num != null ? num.trim().length() < 1 : true) {
                return y;
            }
            y.setNumDoc(num);
            y.setAuthor(currentUser);
            if (save) {
                dao.update(y);
            }
            if (msg) {
                succes();
            }
        }
        return y;
    }

    public YvsComDocVentes reBuildNumero(YvsComDocVentes y, boolean save, boolean msg) {
        return reBuildNumero(y, y.getTypeDoc(), save, msg);
    }

    public void reBuildAllNumero(EnteteDocVente e, String type, boolean save) {
        String query = "select y.id from yvs_com_doc_ventes y where y.type_doc IN (?) and y.entete_doc = ? and y.num_doc in (select d.num_doc from yvs_com_doc_ventes d where d.entete_doc = ? and d.num_doc = y.num_doc and d.id != y.id) order by y.id";
        List<Object> list = dao.loadListBySqlQuery(query, new Options[]{new Options(type, 1), new Options(e.getId(), 2), new Options(e.getId(), 3)});
        if (list != null ? !list.isEmpty() : false) {
            for (int i = 1; i < list.size(); i++) {
                YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{list.get(i)});
                reBuildNumero(y, save, false);
            }
            e.setDocuments(dao.loadNameQueries("YvsComDocVentes.findByTypeDocEntete", new String[]{"typeDoc", "societe", "entete"}, new Object[]{type, currentAgence.getSociete(), new YvsComEnteteDocVente(e.getId())}));
            int idx = documents.indexOf(new YvsComEnteteDocVente(e.getId()));
            if (idx > -1) {
                documents.get(idx).getDocuments().clear();
                documents.get(idx).getDocuments().addAll(e.getDocuments());
            }
            update("data_facture_vente");
            succes();
        }
    }

    public void reBuildAllNumero(boolean save) {
        reBuildAllNumero(entete, save);
    }

    public void reBuildAllNumero(EnteteDocVente e, boolean save) {
        String type = null;
        for (String t : getTypesDoc()) {
            if (type == null) {
                type = t;
            } else {
                type += "," + t;
            }
        }
        reBuildAllNumero(e, type, save);
    }

    public void reBuildAllNumero(YvsComEnteteDocVente e, boolean save) {
        reBuildAllNumero(UtilCom.buildBeanEnteteDocVente(e), save);
    }

    /*BEGIN UPDATE VENTE*/
    public void choosePoint(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            entete.getPoint().setId(id);
            choosePoint();
        }
    }

    public void choosePoint() {
        vendeurs.clear();
        YvsBasePointVente y = new YvsBasePointVente(entete.getPoint().getId());
        entete.getPoint().setListTranche(dao.loadNameQueries("YvsComCreneauPoint.findByDepot", new String[]{"point"}, new Object[]{y}));
        ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (m != null) {
            m.loadDepotByPoint(y);
            if (!m.getDepotsLivraison().contains(new YvsBaseDepots(docVente.getDepot().getId()))) {
                docVente.setDepot(new Depots());
            }
            //trouve le vendeur
            if (autoriser("fv_can_save_for_other")) {
                vendeurs = dao.loadNameQueries("YvsComCreneauHoraireUsers.findUsersByPoint", new String[]{"point"}, new Object[]{y});
            } else {
                vendeurs.add(currentUser.getUsers());
            }
        }
    }

    public void chooseTranche() {
        if (entete.getTranchePoint() != null ? entete.getTranchePoint().getId() > 0 : false) {
            for (YvsComCreneauPoint cp : entete.getPoint().getListTranche()) {
                if (cp.getTranche().getId().equals(entete.getTranchePoint().getId())) {
                    TrancheHoraire t = UtilCom.buildBeanTrancheHoraire(cp.getTranche());
                    cloneObject(entete.getTranchePoint(), t);
                    break;
                }
            }
        }
    }

    public void loadOnViewTranche(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) ev.getObject();
            docVente.setTranche(UtilGrh.buildTrancheHoraire(t));
            update("select_tranche_livraison_update_fv");
        }
    }

    public void loadOnViewTrancheEntete(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsGrhTrancheHoraire t = (YvsGrhTrancheHoraire) ev.getObject();
            entete.setTranchePoint(UtilGrh.buildTrancheHoraire(t));
            update("select_tranche_entete_update_fv_");
        }
    }

    public void chooseDate(SelectEvent ev) {

        entete.setDateEntete((Date) ev.getObject());
        actualiseValueContenu();
    }

    public void chooseDepot() {
        ManagedFactureVente m = (ManagedFactureVente) giveManagedBean(ManagedFactureVente.class);
        if (m != null) {
            if (m.chooseDepot(docVente.getDepot(), docVente.getDateLivraisonPrevu())) {
                if (!m.getTranches().contains(new YvsGrhTrancheHoraire(docVente.getTranche().getId()))) {
                    if (!currentPlanning.isEmpty() ? currentPlanning.get(0).getCreneauDepot() != null : false) {
                        if (!m.getTranches().contains(currentPlanning.get(0).getCreneauDepot().getTranche())) {
                            docVente.setTranche(new TrancheHoraire());
                        } else {
                            docVente.setTranche(UtilCom.buildBeanTrancheHoraire(currentPlanning.get(0).getCreneauDepot().getTranche()));
                        }
                    }
                }
            }
        }
    }

    public void loadOnViewClient(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            YvsComClient y = (YvsComClient) ev.getObject();
            chooseClient(UtilCom.buildBeanClient(y));
        }
    }

    public void chooseClient(Client d) {
        if ((d != null) ? d.getId() > 0 : false) {
            cloneObject(docVente.getClient(), d);
            cloneObject(docVente.getAdresse(), d.getTiers() != null ? d.getTiers().getSecteur_() : new Dictionnaire());
            if (d.getCategorieComptable() != null) {
                cloneObject(docVente.getCategorieComptable(), d.getCategorieComptable());
                update("select_categorie_comptable_update_vente");
            }
            if (docVente.getClient() != null ? docVente.getClient().getModel() != null : false) {
                docVente.setModeReglement(docVente.getClient().getModel());
            } else {
                YvsBaseCategorieClient c = currentCategorieClient(d);
                if (c != null ? c.getModel() != null : false) {
                    docVente.setModeReglement(UtilCom.buildBeanModelReglement(c.getModel()));
                } else {
                    getWarningMessage("Ce client n'a pas de catégorie client!");
                }
            }
            docVente.getClient().setNom(d.getNom_prenom());
            docVente.setNomClient(d.getNom_prenom());
            //choisir le code tiers à utiliser
            if (d.isSuiviComptable()) {
                docVente.setTiers(d);
            } else {
                //récupère le code tiers du commerciale ayant le point de vente
                if (entete.getPoint().getId() > 0) {
                    ManagedPointVente service = (ManagedPointVente) giveManagedBean(ManagedPointVente.class);
                    if (service != null) {
                        int idx = service.getPointsvente().indexOf(new YvsBasePointVente(entete.getPoint().getId()));
                        if (idx >= 0) {
                            List<YvsComCommercialPoint> l = service.getPointsvente().get(idx).getCommerciaux();
                            if (!l.isEmpty()) {
                                if (l.get(0).getCommercial().getTiers() != null) {
                                    if (l.get(0).getCommercial().getTiers().getClients() != null ? !l.get(0).getCommercial().getTiers().getClients().isEmpty() : false) {
                                        docVente.setTiers(UtilCom.buildBeanClient(l.get(0).getCommercial().getTiers().getClients().get(0)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            actualiseValueContenu();
            update("select_model_update_vente");
        }
    }

    public void searchClient() {
        String num = docVente.getClient().getNom();
        docVente.getClient().setId(0);
        docVente.getClient().setError(true);
        docVente.getClient().setTiers(new Tiers());
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            Client y = m.searchClient(num, true);
            if (m.getClients() != null ? !m.getClients().isEmpty() : false) {
                if (m.getClients().size() > 1) {
                    update("data_client_update_vente");
                } else {
                    chooseClient(y);
                }
                docVente.getClient().setError(false);
            }
        }
    }

    public void initClients() {
        ManagedClient m = (ManagedClient) giveManagedBean(ManagedClient.class);
        if (m != null) {
            m.initClients(docVente.getClient());
        }
        update("data_client_update_vente");
    }

    public void chooseCategorie() {
        if ((docVente.getCategorieComptable() != null) ? docVente.getCategorieComptable().getId() > 0 : false) {
            ManagedCatCompt m = (ManagedCatCompt) giveManagedBean(ManagedCatCompt.class);
            if (m != null) {
                int idx = m.getCategories().indexOf(new YvsBaseCategorieComptable(docVente.getCategorieComptable().getId()));
                if (idx > -1) {
                    YvsBaseCategorieComptable y = m.getCategories().get(idx);
                    CategorieComptable d = UtilCom.buildBeanCategorieComptable(y);
                    cloneObject(docVente.getCategorieComptable(), d);
                }
            }
            actualiseValueContenu();
        } else {
            docVente.setCategorieComptable(new CategorieComptable());
        }
    }

    public void chooseModel() {
        actualiseValueMensualite();
    }

    private void actualiseValueContenu() {
        double prix;
        double prixmin;
        double pr = 0;
        double remise;
        double ristourne = 0;
        double taxe;
        double prixTotal;

        for (YvsComContenuDocVente c : contenus) {
            prix = dao.getPuv(c.getArticle().getId(), c.getQuantite(), c.getPrix(), docVente.getClient().getId(), docVente.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), c.getConditionnement().getId());
            prixmin = dao.getPuvMin(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), entete.getDepot().getId(), entete.getPoint().getId(), entete.getDateEntete(), c.getConditionnement().getId());
            pr = dao.getPr(c.getArticle().getId(), entete.getDepot().getId(), 0, entete.getDateEntete(), c.getConditionnement().getId());
            remise = dao.getRemiseVente(c.getArticle().getId(), c.getQuantite(), prix, docVente.getClient().getId(), entete.getPoint().getId(), entete.getDateEntete(), c.getConditionnement().getId());
            taxe = saveAllTaxe(c, docVente, selectVente, docVente.getCategorieComptable().getId(), false);

            prixTotal = c.getQuantite() * (prix - c.getRabais()) - remise + (c.getArticle().getPuvTtc() ? 0 : taxe);

            if (c.getPrix() != prix) {
                c.setNew_(true);
            }
            c.setPrix(prix);
            if (c.getPuvMin() != prixmin) {
                c.setNew_(true);
            }
            c.setPuvMin(prixmin);
            if (c.getRemise() != remise) {
                c.setNew_(true);
            }
            c.setRemise(remise);
            if (c.getTaxe() != taxe) {
                c.setNew_(true);
            }
            c.setTaxe(taxe);
            c.setPrixTotal(prixTotal);
        }
        update("data_contenu_update_vente");

        setMontantTotalDoc(docVente, contenus, null, null);
        double total = dao.loadCaVente(docVente.getId());
        if (total != docVente.getMontantTotal()) {
            double diff = docVente.getMontantTotal() - total;
            if (mensualites != null ? !mensualites.isEmpty() : false) {
                YvsComMensualiteFactureVente e = mensualites.get(mensualites.size() - 1);
                e.setMontant(e.getMontant() + diff);
                e.setNew_(true);
                mensualites.set(mensualites.indexOf(e), e);
                update("data_mensualite_update_vente");
            }
        }
    }

    private void actualiseValueMensualite() {
        YvsComDocVentes y = (YvsComDocVentes) dao.loadOneByNameQueries("YvsComDocVentes.findById", new String[]{"id"}, new Object[]{docVente.getId()});
        YvsBaseModelReglement m = (YvsBaseModelReglement) dao.loadOneByNameQueries(("YvsBaseModelReglement.findById"), new String[]{"id"}, new Object[]{docVente.getModeReglement().getId()});
        mensualites = generatedEcheancierReg(y, m, docVente.getMontantTotal());
        update("data_mensualite_update_vente");
    }

    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y) {
        double ttc = dao.loadCaVente(y.getId());
        if (ttc > 0) {
            double total = (ttc - y.getMontantMensualite());
            return generatedEcheancierReg(y, y.getModelReglement(), total);
        } else {
            getErrorMessage("Cette facture n'a pas de contenu");
            return new ArrayList<>();
        }
    }

//    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, double total) {
//        return generatedEcheancierReg(y, y.getModelReglement(), total);
//    }
    public List<YvsComMensualiteFactureVente> generatedEcheancierReg(YvsComDocVentes y, YvsBaseModelReglement m, double total) {
        return dao.generatedEcheancierReg(y, m, total, currentUser, currentAgence.getSociete());
    }

    public void changeOption() {
        boolean regle = false, livre = false;
        if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE)) {
            getErrorMessage("Cette facture est déjà comptabilisée");
            return;
        }
        if (docVente.getStatut().equals(Constantes.ETAT_VALIDE)) {
            msgOption = "Cette facture est déja validée";
        }
        if (docVente.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
            livre = true;
            if (msgOption != null ? msgOption.trim().length() > 0 : false) {
                msgOption += ", elle est également livrée";
            } else {
                msgOption = "Cette facture est déja livrée";
            }
        }
        if (docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
            regle = true;
            if (msgOption != null ? msgOption.trim().length() > 0 : false) {
                msgOption += " et reglée";
            } else {
                msgOption = "Cette facture est déja reglée";
            }
        }
        switch (saveOption) {
            case "U":
                break;
            case "SS":
                if (regle) {
                    msgOption += ". Vous devez annuler les reglements avant d'effectuer cette action... "
                            + "Veuillez choisir le mode 'Modification avec synchronisation forcée' "
                            + " si vous voulez annuler les reglements automatiquement";
                }
                break;
            case "SF":
                if (regle) {
                    msgOption += ". Vous avez choisi d'annuler les reglements automatiquement";
                }
                break;
        }
        openDialog("dlgConfirm");
    }

    @Override
    public boolean saveNew() {
        try {
            boolean regle = false, livre = false;
            if (entete == null) {
                return false;
            }
            if (docVente == null) {
                return false;
            }
            if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE)) {
                getErrorMessage("Cette facture est déjà comptabilisée");
                return false;
            }
            if (docVente.getStatutLivre().equals(Constantes.ETAT_LIVRE)) {
                livre = true;
            }
            if (docVente.getStatutRegle().equals(Constantes.ETAT_REGLE)) {
                regle = true;
            }
            if (regle && saveOption.equals("SS")) {
                getErrorMessage("Vous ne pouvez pas effectuer cette action");
                return false;
            }
            if (!selectVente.getClient().getId().equals(docVente.getClient().getId())) {
                List<YvsComDocVentes> list = dao.loadNameQueries("YvsComDocVentes.findByParent", new String[]{"documentLie"}, new Object[]{new YvsComDocVentes(docVente.getId())});
                if (list != null ? !list.isEmpty() : false) {
                    getWarningMessage("Ce document est lié à d'autres documents");
//                    getErrorMessage("Vous ne pouvez pas modifier le client...car ce document est lié à d'autres documents");
//                    closeDialog("dlgSave");
//                    return false;
                }
            }
            YvsComEnteteDocVente e = dao.getEntete(new YvsUsers(entete.getUsers().getId()), new YvsBasePointVente(entete.getPoint().getId()), new YvsGrhTrancheHoraire(entete.getTranchePoint().getId()), entete.getDateEntete(), currentNiveau, currentAgence, currentUser);
            if (e != null ? e.getId() < 1 : true) {
                getErrorMessage(dao.getRESULT());
                return false;
            }
            YvsComDocVentes d = UtilCom.buildDocVente(docVente, currentUser);
            d.setEnteteDoc(e);
            dao.update(d);
            boolean continuer = false;
            if (saveOption.equals("SS")) {
                String query = "update yvs_compta_caisse_piece_vente set statut_piece = 'W' where vente = ?";
                dao.requeteLibre(query, new Options[]{new Options(docVente.getId(), 1)});
                continuer = true;
            } else if (saveOption.equals("SF")) {
                continuer = true;
            }
            if (continuer) {
                String query = "delete from yvs_com_contenu_doc_vente where doc_vente = ?";
                dao.requeteLibre(query, new Options[]{new Options(docVente.getId(), 1)});
                for (YvsComContenuDocVente y : contenus) {
                    y.setDocVente(d);
                    y.setId(null);
                    y = (YvsComContenuDocVente) dao.save1(y);
                    saveAllTaxe(y, docVente, selectVente, docVente.getCategorieComptable().getId(), true);
                }

                query = "delete from yvs_com_mensualite_facture_vente where facture  = ?";
                dao.requeteLibre(query, new Options[]{new Options(docVente.getId(), 1)});
                for (YvsComMensualiteFactureVente y : mensualites) {
                    y.setFacture(d);
                    y.setId(null);
                    y = (YvsComMensualiteFactureVente) dao.save1(y);
                }
            }
            closeDialog("dlgSave");
            closeDialog("dlgConfirm");
            succes();
            return true;
        } catch (Exception ex) {
            getException("Error", ex);
            return false;
        }
    }

    public boolean controleFicheEntete(EnteteDocVente bean) {
        if ((bean.getPoint() != null) ? bean.getPoint().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le point de vente!");
            return false;
        }
        if ((bean.getCrenauHoraire().getPersonnel() != null) ? bean.getCrenauHoraire().getPersonnel().getId() < 1 : true) {
            getErrorMessage("Vous devez selectionner le vendeur!");
            return false;
        }
        if (bean.getEtat().equals(Constantes.ETAT_CLOTURE)) {
            getErrorMessage("Cette journée est déjà cloturée");
            return false;
        }
//        if (bean.getId() < 1 && autoriser("fv_save_only_pv")) {
//            List<Long> ids = dao.loadNameQueries("YvsComCreneauHoraireUsers.findIdPointByUsers", new String[]{"users", "date", "hier"}, new Object[]{new YvsUsers(bean.getUsers().getId()), (Utilitaire.getIniTializeDate(bean.getDateEntete()).getTime()), Constantes.getPreviewDate(bean.getDateEntete())});
//            if (ids != null ? (ids.isEmpty() ? true : !ids.contains(bean.getPoint().getId())) : true) {
//                getErrorMessage("Ce vendeur n'est pas planifié au " + formatDate.format(bean.getDateEntete()));
//                return false;
//            }
//        }
//        if (!verifyDateVente(bean.getDateEntete(), bean.getId() > 0)) {
//            return false;
//        }
//        return writeInExercice(bean.getDateEntete());
        return true;
    }

    public YvsComEnteteDocVente saveNewEntete(EnteteDocVente bean, DocVente docVente, boolean copie) {
        YvsComEnteteDocVente y = new YvsComEnteteDocVente();
        try {
            if (controleFicheEntete(bean)) {
                if (!copie && docVente.getId() <= 0) {
                    y = dao.getEntete(new YvsUsers(bean.getUsers().getId()), new YvsBasePointVente(bean.getPoint().getId()), new YvsGrhTrancheHoraire(bean.getTranche().getId()), bean.getDateEntete(), currentNiveau, currentAgence, currentUser);
                } else {
                    y = dao.getEntete(new YvsUsers(bean.getUsers().getId()), new YvsBasePointVente(bean.getPoint().getId()), new YvsGrhTrancheHoraire(bean.getTranche().getId()), bean.getDateEntete(), currentNiveau, currentAgence, currentUser);
                }
                if (y != null ? y.getId() < 0 : true) {
                    getErrorMessage(dao.getRESULT());
                }
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error  >>> ", ex);
        }
        return y;
    }

    public YvsComEnteteDocVente saveNewEntete(DocVente docVente) {
        YvsComEnteteDocVente y = new YvsComEnteteDocVente();
        try {
            y = dao.getEntete(new YvsUsers(docVente.getEnteteDoc().getCrenauHoraire().getPersonnel().getId()),
                    new YvsBasePointVente(docVente.getEnteteDoc().getPoint().getId()),
                    null, docVente.getEnteteDoc().getDateEntete(), currentNiveau, currentAgence, currentUser);
            if (y != null ? y.getId() < 0 : true) {
                getErrorMessage(dao.getRESULT());
            }
        } catch (Exception ex) {
            getErrorMessage("Insertion Impossible !");
            getException("Lymytz Error  >>> ", ex);
        }
        return y;
    }

    @Override
    public void updateBean() {
        try {
            if (entete == null) {
                return;
            }
            if (docVente == null) {
                return;
            }
            if (dao.isComptabilise(docVente.getId(), Constantes.SCR_VENTE)) {
                getErrorMessage("Cette facture est déjà comptabilisée");
                return;
            }
            YvsComEnteteDocVente e = saveNewEntete(entete, docVente, false);
            if (e != null ? e.getId() < 1 : true) {
                return;
            }
            List<YvsComDocVentes> list = dao.loadNameQueries("YvsComDocVentes.findByEnteteTypeDoc", new String[]{"entete", "typeDoc"}, new Object[]{new YvsComEnteteDocVente(entete.getId()), Constantes.TYPE_FV});
            for (YvsComDocVentes d : list) {
                d.setEnteteDoc(e);
                dao.update(d);
            }
            succes();
        } catch (Exception ex) {
            getException("Error", ex);
        }
    }

    public void createVirement() {
        ManagedVirement w = (ManagedVirement) giveManagedBean(ManagedVirement.class);
        if (w != null) {
            virement.setEnteteDoc(new EnteteDocVente(selectDoc.getId()));
            virement.setDatePaiement(selectDoc.getDateEntete());
            virement.setDatePiece(selectDoc.getDateEntete());
            virement.setCaissier(new Users(selectDoc.getCreneau().getUsers().getId(), selectDoc.getCreneau().getUsers().getCodeUsers(), selectDoc.getCreneau().getUsers().getNomUsers()));
            YvsComptaCaissePieceVirement y = w.saveNew(virement);
            if (y != null ? y.getId() > 0 : false) {
                selectDoc.getVirements().add(y);
                succes();

                virement = new PieceTresorerie();
                virement.setDatePaiementPrevu(selectDoc.getDateEntete());
            }
        }
    }

    public void loadVirement(YvsComEnteteDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            selectDoc = y;
            selectDoc.setVirements(dao.loadNameQueries("YvsComptaNotifVersementVente.findVirementByHeader", new String[]{"enteteDoc"}, new Object[]{y}));
            selectDoc.setCaisses(dao.loadNameQueries("YvsComptaCaissePieceVente.findCaisseByEntete", new String[]{"enteteDoc"}, new Object[]{y}));
            if (selectDoc.getCaisses() != null ? !selectDoc.getCaisses().isEmpty() : false) {
                YvsBaseCaisse caisse = selectDoc.getCaisses().get(0);
                onSelectSource(caisse);
            }
            virement.setDatePaiementPrevu(selectDoc.getDateEntete());
            openDialog("dlgListVirement");
            update("form-virement_journal_vente");
        }
    }

    public void chooseSource(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            // trouve les caisses parent d'une caisse données
            long id = (long) ev.getNewValue();
            caisseCible.clear();
            if (id > 0) {
                int idx = selectDoc.getCaisses().indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    YvsBaseCaisse y = selectDoc.getCaisses().get(idx);
                    onSelectSource(y);
                }
            }
        }
    }

    public void onSelectSource(YvsBaseCaisse y) {
        if (y != null ? y.getId() > 0 : false) {
            virement.setCaisse(UtilCompta.buildBeanCaisse(y));
            caisseCible = dao.loadNameQueries("YvsBaseLiaisonCaisse.findCaisseCibleByCaisse", new String[]{"caisseSource"}, new Object[]{y});
        }
    }

    public void chooseCible(ValueChangeEvent ev) {
        if (ev.getNewValue() != null) {
            long id = (long) ev.getNewValue();
            if (id > 0) {
                int idx = caisseCible.indexOf(new YvsBaseCaisse((long) ev.getNewValue()));
                if (idx >= 0) {
                    virement.setOtherCaisse(UtilCompta.buildBeanCaisse(caisseCible.get(idx)));
                }
            }
        }
    }

    public void chooseModeReglement() {
        if (virement.getMode() != null ? virement.getMode().getId() > 0 : false) {
            ManagedModeReglement w = (ManagedModeReglement) giveManagedBean(ManagedModeReglement.class);
            if (w != null) {
                int idx = w.getModes().indexOf(new YvsBaseModeReglement((long) virement.getMode().getId()));
                if (idx > -1) {
                    YvsBaseModeReglement y = w.getModes().get(idx);
                    virement.setMode(UtilCompta.buildBeanModeReglement(y));
                }
            }
        }
    }

    public void comptabiliseByDate(Date dateDebut, Date dateFin) {
        ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
        if (w != null) {
            champ = new String[]{"societe", "statut", "dateDebut", "dateFin", "type"};
            val = new Object[]{currentAgence.getSociete(), Constantes.ETAT_VALIDE, dateDebut, dateFin, Constantes.TYPE_FV};
            List<YvsComEnteteDocVente> list = dao.loadNameQueries("YvsComDocVentes.findEnteteByStatutDates", champ, val);
            if (list != null ? !list.isEmpty() : false) {
                if (list.size() > 1000) {
                    getErrorMessage("Veuillez entrer une période plus petite");
                    return;
                }
                for (YvsComEnteteDocVente y : list) {
                    w.comptabiliserHeaderVente(y, false, false);
                }
                succes();
            }
        }
    }

    public boolean isComptabiliseBean(EnteteDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_HEAD_VENTE));
            }
            return y.isComptabilise();
        }
        return false;
    }

    public boolean isComptabilise(YvsComEnteteDocVente y) {
        if (y != null ? y.getId() > 0 : false) {
            ManagedSaisiePiece w = (ManagedSaisiePiece) giveManagedBean(ManagedSaisiePiece.class);
            if (w != null) {
                y.setComptabilise(w.isComptabilise(y.getId(), Constantes.SCR_HEAD_VENTE));
            }
            return y.getComptabilise();
        }
        return false;
    }

}
