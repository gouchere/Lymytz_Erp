/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import yvs.dao.Options;
import yvs.entity.communication.YvsMsgConversation;
import yvs.entity.communication.YvsMsgGroupeMessage;
import yvs.entity.communication.YvsMsgCarnetAdresse;
import yvs.entity.communication.YvsMsgDestinataire;
import yvs.entity.communication.YvsMsgFiltreMessage;
import yvs.entity.users.YvsMsgDiffusionContact;
import yvs.entity.users.YvsMsgGroupeDiffusion;
import yvs.entity.users.YvsUsers;
import yvs.init.Initialisation;
import yvs.parametrage.ParametreServeurMail;
import yvs.theme.Theme;
import yvs.theme.ThemeService;
import yvs.users.AdresseProfessionnel;
import yvs.users.CarnetAdresse;
import yvs.users.Users;
import yvs.users.UtilUsers;
import yvs.util.Compress;
import yvs.util.Managed;
import yvs.util.Util;

/**
 *
 * @author LYMYTZ
 */
@ManagedBean
@SessionScoped
public class ManagedConversation extends Managed<Conversation, YvsMsgConversation> implements Serializable {

    private final Logger logger=Logger.getLogger(ManagedConversation.class.getName());
    @ManagedProperty(value = "#{conversation}")
    private Conversation conversation;
    private Conversation conversationSelect = new Conversation();
    private Destinataire destination = new Destinataire(), destinataireSelect = new Destinataire();
    @ManagedProperty(value = "#{themeService}")
    private ThemeService themeService;
    @ManagedProperty(value = "#{filtreMail}")
    private FiltreMail filtreMail;
    private List<Conversation> listMailSend, listMailDraft, listMailCorb, listSelectMessages, listMailSearch;
    private List<Destinataire> listMailIndox, listSelectDestination, listMailFolder, listDestinationMesssage;
    private boolean viewListe = true, optionTransfert, updateMessage, selectMessage, optionUpdate, viewListUser = true, onLecture, viewHistorique, onPieceJointe,
            inCorbeille, inInbox = true, inDraft, inSend, inContact, inDossier, inSousInbox, inSearch, selectMessagerie, selectGroupDossier, selectGroupContact,
            selectAllInbox, selectAllSend, selectAllDraft, selectAllCorb, selectAllSearch, selectContact, selectAllFolder, selectDossier, selectRecepteur, repondre,
            selectMembreDiffusion, viewCcInterne, viewCcExterne, clickCcInter, selectCarnetAdresse, AdressExtCC;
    private String destinatairesExtern, ccDestExterne, ccDestInterne;
    private String piecejointeDownload, eltSearch = "all", emetteurSearch, destinataireSearch, objetSearch, contenuSearch;
    private YvsUsers usersEmetteurSearch, usersDestinataireSearch;
    private Date dateEnvoiSearch;
    private List<Users> listUsers, listSelectUsers, listCcDestInterne, listUsersDiffusion, listSelectUsersDiffusion;
    public List<String[]> listPiecesJointe;
    private List<DiffusionContact> listSelectDiffusionContact;
    private List<GroupeDiffusion> listGroupDiffusion, listSelectGroupDiffusion;
    private List<GroupeMessage> listGroupMessage, listSelectGroupMessage, listParentGroupMessage;
    private List<Theme> listSelectThemes, listCcSelectThemes, allThemes;
    private GroupeDiffusion groupe = new GroupeDiffusion();
    private GroupeMessage groupeM = new GroupeMessage(), parentGroupMessage = new GroupeMessage();
    private UploadedFile file0, file1, file2, file3, file4;
    private TreeNode menu = new DefaultTreeNode(new MenuMessagerie(0, "Menu", "Menu"), null);
    private int nbrPieceJointe = 0;
    private List<CarnetAdresse> carnetAdresses, carnetAdressesSelect;
    ParametreServeurMail serveur = new ParametreServeurMail();
    AdresseProfessionnel adresse = new AdresseProfessionnel();
    MimeMultipart piecesJointes = new MimeMultipart();
    YvsMsgConversation parent = new YvsMsgConversation();
    YvsMsgConversation entityMessage;
    YvsMsgGroupeDiffusion entityGroupDiffusion;
    YvsMsgGroupeMessage entityGroupMessage;

    public ManagedConversation() {
        listMailSearch = new ArrayList<>();
        listDestinationMesssage = new ArrayList<>();
        listCcDestInterne = new ArrayList<>();
        listSelectDestination = new ArrayList<>();
        listPiecesJointe = new ArrayList<>();
        listMailFolder = new ArrayList<>();
        listGroupMessage = new ArrayList<>();
        listParentGroupMessage = new ArrayList<>();
        listSelectGroupMessage = new ArrayList<>();
        listMailIndox = new ArrayList<>();
        listMailSend = new ArrayList<>();
        listMailDraft = new ArrayList<>();
        listMailCorb = new ArrayList<>();
        listSelectMessages = new ArrayList<>();
        listUsers = new ArrayList<>();
        listSelectUsers = new ArrayList<>();
        listGroupDiffusion = new ArrayList<>();
        listSelectGroupDiffusion = new ArrayList<>();
        listUsersDiffusion = new ArrayList<>();
        listSelectUsersDiffusion = new ArrayList<>();
        listSelectDiffusionContact = new ArrayList<>();
        listCcSelectThemes = new ArrayList<>();
        listSelectThemes = new ArrayList<>();
        carnetAdresses = new ArrayList<>();
        carnetAdressesSelect = new ArrayList<>();
        allThemes = new ArrayList<>();
    }

    public YvsUsers getUsersEmetteurSearch() {
        return usersEmetteurSearch;
    }

    public YvsUsers getUsersDestinataireSearch() {
        return usersDestinataireSearch;
    }

    public void setUsersEmetteurSearch(YvsUsers usersEmetteurSearch) {
        this.usersEmetteurSearch = usersEmetteurSearch;
    }

    public void setUsersDestinataireSearch(YvsUsers usersDestinataireSearch) {
        this.usersDestinataireSearch = usersDestinataireSearch;
    }

    public String getEmetteurSearch() {
        return emetteurSearch;
    }

    public void setEmetteurSearch(String emetteurSearch) {
        this.emetteurSearch = emetteurSearch;
    }

    public String getDestinataireSearch() {
        return destinataireSearch;
    }

    public void setDestinataireSearch(String destinataireSearch) {
        this.destinataireSearch = destinataireSearch;
    }

    public String getObjetSearch() {
        return objetSearch;
    }

    public void setObjetSearch(String objetSearch) {
        this.objetSearch = objetSearch;
    }

    public String getContenuSearch() {
        return contenuSearch;
    }

    public void setContenuSearch(String contenuSearch) {
        this.contenuSearch = contenuSearch;
    }

    public FiltreMail getFiltreMail() {
        return filtreMail;
    }

    public void setFiltreMail(FiltreMail filtreMail) {
        this.filtreMail = filtreMail;
    }

    public Date getDateEnvoiSearch() {
        return dateEnvoiSearch;
    }

    public void setDateEnvoiSearch(Date dateEnvoiSearch) {
        this.dateEnvoiSearch = dateEnvoiSearch;
    }

    public List<Conversation> getListMailSearch() {
        return listMailSearch;
    }

    public void setListMailSearch(List<Conversation> listMailSearch) {
        this.listMailSearch = listMailSearch;
    }

    public String getEltSearch() {
        return eltSearch;
    }

    public void setEltSearch(String eltSearch) {
        this.eltSearch = eltSearch;
    }

    public boolean isSelectAllSearch() {
        return selectAllSearch;
    }

    public void setSelectAllSearch(boolean selectAllSearch) {
        this.selectAllSearch = selectAllSearch;
    }

    public boolean isInSearch() {
        return inSearch;
    }

    public void setInSearch(boolean inSearch) {
        this.inSearch = inSearch;
    }

    public boolean isAdressExtCC() {
        return AdressExtCC;
    }

    public void setAdressExtCC(boolean AdressExtCC) {
        this.AdressExtCC = AdressExtCC;
    }

    public boolean isSelectCarnetAdresse() {
        return selectCarnetAdresse;
    }

    public void setSelectCarnetAdresse(boolean selectCarnetAdresse) {
        this.selectCarnetAdresse = selectCarnetAdresse;
    }

    public List<CarnetAdresse> getCarnetAdressesSelect() {
        return carnetAdressesSelect;
    }

    public void setCarnetAdressesSelect(List<CarnetAdresse> carnetAdressesSelect) {
        this.carnetAdressesSelect = carnetAdressesSelect;
    }

    public List<CarnetAdresse> getCarnetAdresses() {
        return carnetAdresses;
    }

    public void setCarnetAdresses(List<CarnetAdresse> carnetAdresses) {
        this.carnetAdresses = carnetAdresses;
    }

    public boolean isOptionTransfert() {
        return optionTransfert;
    }

    public void setOptionTransfert(boolean optionTransfert) {
        this.optionTransfert = optionTransfert;
    }

    public List<Destinataire> getListDestinationMesssage() {
        return listDestinationMesssage;
    }

    public void setListDestinationMesssage(List<Destinataire> listDestinationMesssage) {
        this.listDestinationMesssage = listDestinationMesssage;
    }

    public Conversation getConversationSelect() {
        return conversationSelect;
    }

    public void setConversationSelect(Conversation conversationSelect) {
        this.conversationSelect = conversationSelect;
    }

    public Destinataire getDestinataireSelect() {
        return destinataireSelect;
    }

    public void setDestinataireSelect(Destinataire destinataireSelect) {
        this.destinataireSelect = destinataireSelect;
    }

    public boolean isClickCcInter() {
        return clickCcInter;
    }

    public void setClickCcInter(boolean clickCcInter) {
        this.clickCcInter = clickCcInter;
    }

    public String getCcDestInterne() {
        return ccDestInterne;
    }

    public void setCcDestInterne(String ccDestInterne) {
        this.ccDestInterne = ccDestInterne;
    }

    public List<Theme> getListCcSelectThemes() {
        return listCcSelectThemes;
    }

    public void setListCcSelectThemes(List<Theme> listCcSelectThemes) {
        this.listCcSelectThemes = listCcSelectThemes;
    }

    public String getCcDestExterne() {
        return ccDestExterne;
    }

    public void setCcDestExterne(String ccDestExterne) {
        this.ccDestExterne = ccDestExterne;
    }

    public List<Users> getListCcDestInterne() {
        return listCcDestInterne;
    }

    public void setListCcDestInterne(List<Users> listCcDestInterne) {
        this.listCcDestInterne = listCcDestInterne;
    }

    public boolean isViewCcInterne() {
        return viewCcInterne;
    }

    public void setViewCcInterne(boolean viewCcInterne) {
        this.viewCcInterne = viewCcInterne;
    }

    public boolean isViewCcExterne() {
        return viewCcExterne;
    }

    public void setViewCcExterne(boolean viewCcExterne) {
        this.viewCcExterne = viewCcExterne;
    }

    public List<Destinataire> getListSelectDestination() {
        return listSelectDestination;
    }

    public void setListSelectDestination(List<Destinataire> listSelectDestination) {
        this.listSelectDestination = listSelectDestination;
    }

    public Destinataire getDestination() {
        return destination;
    }

    public void setDestination(Destinataire destination) {
        this.destination = destination;
    }

    public boolean isInSousInbox() {
        return inSousInbox;
    }

    public void setInSousInbox(boolean inSousInbox) {
        this.inSousInbox = inSousInbox;
    }

    public boolean isRepondre() {
        return repondre;
    }

    public void setRepondre(boolean repondre) {
        this.repondre = repondre;
    }

    public boolean isSelectMembreDiffusion() {
        return selectMembreDiffusion;
    }

    public void setSelectMembreDiffusion(boolean selectMembreDiffusion) {
        this.selectMembreDiffusion = selectMembreDiffusion;
    }

    public boolean isSelectRecepteur() {
        return selectRecepteur;
    }

    public void setSelectRecepteur(boolean selectRecepteur) {
        this.selectRecepteur = selectRecepteur;
    }

    public List<String[]> getListPiecesJointe() {
        return listPiecesJointe;
    }

    public void setListPiecesJointe(List<String[]> listPiecesJointe) {
        this.listPiecesJointe = listPiecesJointe;
    }

    public String getPiecejointeDownload() {
        return piecejointeDownload;
    }

    public void setPiecejointeDownload(String piecejointeDownload) {
        this.piecejointeDownload = piecejointeDownload;
    }

    public int getNbrPieceJointe() {
        return nbrPieceJointe;
    }

    public void setNbrPieceJointe(int nbrPieceJointe) {
        this.nbrPieceJointe = nbrPieceJointe;
    }

    public boolean isSelectAllFolder() {
        return selectAllFolder;
    }

    public void setSelectAllFolder(boolean selectAllFolder) {
        this.selectAllFolder = selectAllFolder;
    }

    public boolean isSelectDossier() {
        return selectDossier;
    }

    public void setSelectDossier(boolean selectDossier) {
        this.selectDossier = selectDossier;
    }

    public List<Destinataire> getListMailFolder() {
        return listMailFolder;
    }

    public void setListMailFolder(List<Destinataire> listMailFolder) {
        this.listMailFolder = listMailFolder;
    }

    public boolean isSelectGroupDossier() {
        return selectGroupDossier;
    }

    public void setSelectGroupDossier(boolean selectGroupDossier) {
        this.selectGroupDossier = selectGroupDossier;
    }

    public boolean isSelectGroupContact() {
        return selectGroupContact;
    }

    public void setSelectGroupContact(boolean selectGroupContact) {
        this.selectGroupContact = selectGroupContact;
    }

    public boolean isInDossier() {
        return inDossier;
    }

    public void setInDossier(boolean inDossier) {
        this.inDossier = inDossier;
    }

    public GroupeMessage getParentGroupMessage() {
        return parentGroupMessage;
    }

    public void setParentGroupMessage(GroupeMessage parentGroupMessage) {
        this.parentGroupMessage = parentGroupMessage;
    }

    public List<GroupeMessage> getListParentGroupMessage() {
        return listParentGroupMessage;
    }

    public void setListParentGroupMessage(List<GroupeMessage> listParentGroupMessage) {
        this.listParentGroupMessage = listParentGroupMessage;
    }

    public GroupeMessage getGroupeM() {
        return groupeM;
    }

    public void setGroupeM(GroupeMessage groupeM) {
        this.groupeM = groupeM;
    }

    public List<GroupeMessage> getListGroupMessage() {
        return listGroupMessage;
    }

    public void setListGroupMessage(List<GroupeMessage> listGroupMessage) {
        this.listGroupMessage = listGroupMessage;
    }

    public List<GroupeMessage> getListSelectGroupMessage() {
        return listSelectGroupMessage;
    }

    public void setListSelectGroupMessage(List<GroupeMessage> listSelectGroupMessage) {
        this.listSelectGroupMessage = listSelectGroupMessage;
    }

    public void setOnPieceJointe(boolean onPieceJointe) {
        this.onPieceJointe = onPieceJointe;
    }

    public boolean isOnPieceJointe() {
        return onPieceJointe;
    }

    public List<Theme> getAllThemes() {
        return allThemes;
    }

    public UploadedFile getFile0() {
        return file0;
    }

    public void setFile0(UploadedFile file0) {
        this.file0 = file0;
    }

    public UploadedFile getFile1() {
        return file1;
    }

    public void setFile1(UploadedFile file1) {
        this.file1 = file1;
    }

    public UploadedFile getFile2() {
        return file2;
    }

    public void setFile2(UploadedFile file2) {
        this.file2 = file2;
    }

    public UploadedFile getFile3() {
        return file3;
    }

    public void setFile3(UploadedFile file3) {
        this.file3 = file3;
    }

    public UploadedFile getFile4() {
        return file4;
    }

    public void setFile4(UploadedFile file4) {
        this.file4 = file4;
    }

    public void setAllThemes(List<Theme> allThemes) {
        this.allThemes = allThemes;
    }

    public List<Theme> getListSelectThemes() {
        return listSelectThemes;
    }

    public void setListSelectThemes(List<Theme> listSelectThemes) {
        this.listSelectThemes = listSelectThemes;
    }

    public ThemeService getThemeService() {
        return themeService;
    }

    public void setThemeService(ThemeService themeService) {
        this.themeService = themeService;
    }

    public boolean isSelectContact() {
        return selectContact;
    }

    public void setSelectContact(boolean selectContact) {
        this.selectContact = selectContact;
    }

    public boolean isSelectAllSend() {
        return selectAllSend;
    }

    public void setSelectAllSend(boolean selectAllSend) {
        this.selectAllSend = selectAllSend;
    }

    public boolean isSelectAllDraft() {
        return selectAllDraft;
    }

    public void setSelectAllDraft(boolean selectAllDraft) {
        this.selectAllDraft = selectAllDraft;
    }

    public boolean isSelectAllCorb() {
        return selectAllCorb;
    }

    public void setSelectAllCorb(boolean selectAllCorb) {
        this.selectAllCorb = selectAllCorb;
    }

    public TreeNode getMenu() {
        return menu;
    }

    public boolean isSelectAllInbox() {
        return selectAllInbox;
    }

    public void setSelectAllInbox(boolean selectAllInbox) {
        this.selectAllInbox = selectAllInbox;
    }

    public void setMenu(TreeNode menu) {
        this.menu = menu;
    }

    public List<Users> getListSelectUsersDiffusion() {
        return listSelectUsersDiffusion;
    }

    public void setListSelectUsersDiffusion(List<Users> listSelectUsersDiffusion) {
        this.listSelectUsersDiffusion = listSelectUsersDiffusion;
    }

    public List<DiffusionContact> getListSelectDiffusionContact() {
        return listSelectDiffusionContact;
    }

    public void setListSelectDiffusionContact(List<DiffusionContact> listSelectDiffusionContact) {
        this.listSelectDiffusionContact = listSelectDiffusionContact;
    }

    public List<Users> getListUsersDiffusion() {
        return listUsersDiffusion;
    }

    public void setListUsersDiffusion(List<Users> listUsersDiffusion) {
        this.listUsersDiffusion = listUsersDiffusion;
    }

    public boolean isSelectMessagerie() {
        return selectMessagerie;
    }

    public void setSelectMessagerie(boolean selectMessagerie) {
        this.selectMessagerie = selectMessagerie;
    }

    public boolean isViewHistorique() {
        return viewHistorique;
    }

    public void setViewHistorique(boolean viewHistorique) {
        this.viewHistorique = viewHistorique;
    }

    public boolean isInContact() {
        return inContact;
    }

    public void setInContact(boolean inContact) {
        this.inContact = inContact;
    }

    public GroupeDiffusion getGroupe() {
        return groupe;
    }

    public void setGroupe(GroupeDiffusion groupe) {
        this.groupe = groupe;
    }

    public List<GroupeDiffusion> getListSelectGroupDiffusion() {
        return listSelectGroupDiffusion;
    }

    public void setListSelectGroupDiffusion(List<GroupeDiffusion> listSelectGroupDiffusion) {
        this.listSelectGroupDiffusion = listSelectGroupDiffusion;
    }

    public List<GroupeDiffusion> getListGroupDiffusion() {
        return listGroupDiffusion;
    }

    public void setListGroupDiffusion(List<GroupeDiffusion> listGroupDiffusion) {
        this.listGroupDiffusion = listGroupDiffusion;
    }

    public String getDestinatairesExtern() {
        return destinatairesExtern;
    }

    public void setDestinatairesExtern(String destinatairesExtern) {
        this.destinatairesExtern = destinatairesExtern;
    }

    public boolean isViewListUser() {
        return viewListUser;
    }

    public void setViewListUser(boolean viewListUser) {
        this.viewListUser = viewListUser;
    }

    public List<Users> getListSelectUsers() {
        return listSelectUsers;
    }

    public void setListSelectUsers(List<Users> listSelectUsers) {
        this.listSelectUsers = listSelectUsers;
    }

    public List<Conversation> getListMailSend() {
        return listMailSend;
    }

    public void setListMailSend(List<Conversation> listMailSend) {
        this.listMailSend = listMailSend;
    }

    public List<Conversation> getListMailDraft() {
        return listMailDraft;
    }

    public void setListMailDraft(List<Conversation> listMailDraft) {
        this.listMailDraft = listMailDraft;
    }

    public List<Conversation> getListMailCorb() {
        return listMailCorb;
    }

    public void setListMailCorb(List<Conversation> listMailCorb) {
        this.listMailCorb = listMailCorb;
    }

    public boolean isInSend() {
        return inSend;
    }

    public void setInSend(boolean inSend) {
        this.inSend = inSend;
    }

    public boolean isInInbox() {
        return inInbox;
    }

    public boolean isInDraft() {
        return inDraft;
    }

    public void setInDraft(boolean inDraft) {
        this.inDraft = inDraft;
    }

    public void setInInbox(boolean inInbox) {
        this.inInbox = inInbox;
    }

    public boolean isOnLecture() {
        return onLecture;
    }

    public void setOnLecture(boolean onLecture) {
        this.onLecture = onLecture;
    }

    public List<Conversation> getListSelectMessages() {
        return listSelectMessages;
    }

    public boolean isInCorbeille() {
        return inCorbeille;
    }

    public void setInCorbeille(boolean inCorbeille) {
        this.inCorbeille = inCorbeille;
    }

    public void setListSelectMessages(List<Conversation> listSelectMessages) {
        this.listSelectMessages = listSelectMessages;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public List<Destinataire> getListMailIndox() {
        return listMailIndox;
    }

    public void setListMailIndox(List<Destinataire> listMailIndox) {
        this.listMailIndox = listMailIndox;
    }

    public boolean isViewListe() {
        return viewListe;
    }

    public void setViewListe(boolean viewListe) {
        this.viewListe = viewListe;
    }

    public boolean isUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(boolean updateMessage) {
        this.updateMessage = updateMessage;
    }

    public boolean isSelectMessage() {
        return selectMessage;
    }

    public void setSelectMessage(boolean selectMessage) {
        this.selectMessage = selectMessage;
    }

    public boolean isOptionUpdate() {
        return optionUpdate;
    }

    public void setOptionUpdate(boolean optionUpdate) {
        this.optionUpdate = optionUpdate;
    }

    public List<Users> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<Users> listUsers) {
        this.listUsers = listUsers;
    }

    public YvsMsgFiltreMessage buildFiltreMail(FiltreMail f) {
        YvsMsgFiltreMessage r = new YvsMsgFiltreMessage();
        r.setBase((!"".equals(f.getBase())) ? f.getBase() : "E");
        r.setId(f.getId());
        r.setValeur(f.getValeur());
        if ((f.getGroupeMessage() != null) ? f.getGroupeMessage().getId() != 0 : false) {
            r.setGroupeMessage(new YvsMsgGroupeMessage(f.getGroupeMessage().getId()));
        }
        r.setUsers(currentUser.getUsers());
        return r;
    }

    public YvsMsgGroupeDiffusion buildGroupeDiffusion(GroupeDiffusion g) {
        YvsMsgGroupeDiffusion r = new YvsMsgGroupeDiffusion();
        if (g != null) {
            r.setId(g.getId());
            r.setLibelle(g.getLibelle());
            r.setReference(g.getReference());
            r.setUsers(currentUser.getUsers());
        }
        return r;
    }

    public YvsMsgGroupeMessage buildGroupeMessage(GroupeMessage g) {
        YvsMsgGroupeMessage r = new YvsMsgGroupeMessage();
        if (g != null) {
            r.setId(g.getId());
            r.setLibelle(g.getLibelle());
            r.setReference(g.getReference());
            r.setUsers(currentUser.getUsers());
            if ((parentGroupMessage != null) ? parentGroupMessage.getId() != 0 : false) {
                r.setParent(new YvsMsgGroupeMessage(parentGroupMessage.getId()));
            }
        }
        return r;
    }

    public YvsMsgDestinataire buildDestinataire(Destinataire c) {
        YvsMsgDestinataire r = new YvsMsgDestinataire();
        if (c != null) {
            r.setAccuse(c.isAccuse());
            r.setDateReception((c.getDateReception() != null) ? c.getDateReception() : new Date());
            r.setHeureReception((c.getHeureReception() != null) ? c.getHeureReception() : new Date());
            r.setSupp(c.isSupp());
            r.setExterne(c.isExterne());
            r.setSendAccuse(c.isSendAccuse());
            if (c.isExterne()) {
                r.setAdresseExterne(c.getAdresseExterne());
            } else {
                r.setDestinataire(new YvsUsers(c.getDestinataire().getId()));
            }
            r.setCopie(c.isCopie());
            r.setId(c.getId());
            r.setDelete(c.isDelete());
            r.setMessage(entityMessage);
            if ((c.getGroupe() != null) ? c.getGroupe().getId() != 0 : false) {
                r.setGroupeMessage(new YvsMsgGroupeMessage(c.getGroupe().getId()));
            }
        }
        return r;
    }

    public YvsMsgConversation buildConversation(Conversation c) {
        YvsMsgConversation r = new YvsMsgConversation();
        if (c != null) {
            r.setId(c.getId());
            if (!isSelectMessage()) {
                r.setReference(c.getObjet() + "_" + Util.giveFileName());
            }
            r.setContenu(c.getContenu());
            r.setDateEnvoi((c.getDateEnvoi() != null) ? c.getDateEnvoi() : new Date());
            r.setHeureEnvoi((c.getHeureEnvoi() != null) ? c.getHeureEnvoi() : new Date());
            r.setObjet(c.getObjet());
            r.setSupp(c.isSupp());
            r.setPriorite(c.getPriorite());
            r.setEnvoyer(c.isEnvoyer());
            r.setExterne(false);
            r.setAccuse(false);
            r.setEmetteur(new YvsUsers(c.getEmetteur().getId()));
            r.setDelet(c.isDelete());
            if ((parent != null) ? parent.getId() != null : false) {
                r.setReponse(parent);
            }
            r.setPieceJointe(c.getPieceJointe());
        }
        return r;
    }

    public YvsMsgCarnetAdresse buildCarnetAdresse(CarnetAdresse c) {
        YvsMsgCarnetAdresse r = new YvsMsgCarnetAdresse();
        if (c != null) {
            r.setId(c.getId());
            r.setAdresse(c.getAdresse());
            r.setNom(c.getNom());
            r.setUsers(currentUser.getUsers());
        }
        return r;
    }

    @Override
    public boolean controleFiche(Conversation bean) {
        if (bean.getObjet() == null || bean.getObjet().equals("")) {
            getErrorMessage("Vous devez entrez l'objet");
            return false;
        }
        if (bean.getContenu() == null || bean.getContenu().equals("")) {
            getErrorMessage("Vous devez entrez le contenu");
            return false;
        }
        if (bean.isEnvoyer()) {
            if (listSelectUsers.isEmpty() && (destinatairesExtern == null || "".equals(destinatairesExtern))) {
                getErrorMessage("Vous devez specifier un recepteur");
                return false;
            }
        }
        if (bean.getReference() == null || "".equals(bean.getReference())) {
            conversation.setReference(bean.getObjet() + "_" + Util.giveFileName());
        }
        return true;
    }

    public boolean controleFiche_(Conversation bean) {
        if (bean.getObjet() == null || bean.getObjet().equals("")) {
            getErrorMessage("Vous devez entrez l'objet");
            return false;
        }
        if (bean.getReference() == null || "".equals(bean.getReference())) {
            conversation.setReference(bean.getObjet() + "_" + Util.giveFileName());
        }
        return true;
    }

    @Override
    public void deleteBean() {
        boolean correct_ = false;
        if (!listSelectMessages.isEmpty()) {
            for (Conversation c : listSelectMessages) {
                c.setDelete(true);
                dao.update(buildConversation(c));
                listMailCorb.remove(c);
            }
            correct_ = true;
        }
        if (!listSelectDestination.isEmpty()) {
            for (Destinataire c : listSelectDestination) {
                c.setDelete(true);
                dao.update(buildDestinataire(c));
                listMailCorb.remove(c.getMessage());
            }
            correct_ = true;
        }
        if (correct_) {
            resetFiche();
            succes();
        }
        update("body_messagerie");
    }

    public void onDraftBean(boolean supp) {
        boolean correct_ = false;
        if (supp) {
            if (isInInbox()) {
                String rq = "UPDATE yvs_msg_destinataire SET supp = true WHERE id=?";
                for (Destinataire c : listSelectDestination) {
                    Options[] param = new Options[]{new Options(c.getId(), 1)};
                    dao.requeteLibre(rq, param);
                    listMailIndox.remove(c);
                    correct_ = true;
                }
            }
            if (isInSend()) {
                String rq = "UPDATE yvs_msg_conversation SET supp = true WHERE id=?";
                for (Conversation c : listSelectMessages) {
                    Options[] param = new Options[]{new Options(c.getId(), 1)};
                    dao.requeteLibre(rq, param);
                    listMailSend.remove(c);
                    correct_ = true;
                }
            }
            if (isInDraft()) {
                String rq = "UPDATE yvs_msg_conversation SET supp = true WHERE id=?";
                for (Conversation c : listSelectMessages) {
                    Options[] param = new Options[]{new Options(c.getId(), 1)};
                    dao.requeteLibre(rq, param);
                    listMailDraft.remove(c);
                    correct_ = true;
                }
            }
            if (isInDossier()) {
                String rq = "UPDATE yvs_msg_destinataire SET supp = true WHERE id=?";
                for (Destinataire c : listSelectDestination) {
                    Options[] param = new Options[]{new Options(c.getId(), 1)};
                    dao.requeteLibre(rq, param);
                    listMailFolder.remove(c);
                    correct_ = true;
                }
            }
        } else {
            String rq = "UPDATE yvs_msg_conversation SET supp = false WHERE id=?";
            for (Conversation c : listSelectMessages) {
                Options[] param = new Options[]{new Options(c.getId(), 1)};
                dao.requeteLibre(rq, param);
                listMailCorb.remove(c);
                correct_ = true;
            }
            rq = "UPDATE yvs_msg_destinataire SET supp = false WHERE id=?";
            for (Destinataire c : listSelectDestination) {
                Options[] param = new Options[]{new Options(c.getId(), 1)};
                dao.requeteLibre(rq, param);
                listMailCorb.remove(c.getMessage());
                correct_ = true;
            }
        }
        if (correct_) {
            resetFiche();
            succes();
            update("body_messagerie");
        }
    }

    @Override
    public void updateBean() {
        setViewListe(false);
        setOnLecture(false);
        setSelectMessage(false);
        setOptionUpdate(false);
        setRepondre(true);
        if (listSelectThemes == null) {
            listSelectThemes = new ArrayList<>();
        }
        for (Destinataire bean : conversation.getListDestinataire()) {
            listSelectUsers.add(bean.getDestinataire());
            listUsers.get(listUsers.indexOf(bean.getDestinataire())).setSelectActif(true);
            Theme t = new Theme((int) bean.getDestinataire().getId(), bean.getDestinataire().getNomUsers(), bean.getDestinataire().getNomUsers());
            listSelectThemes.add(t);
        }
        List<String[]> list = buildListPieceJointe(conversation.getPieceJointe());
        conversation.setDateEnvoi(new Date());
        conversation.setHeureEnvoi(new Date());
        setNbrPieceJointe(list.size());
        update("head_messagerie");
        update("body_messagerie");
    }

    public void updateBeanGroupeDiffusion() {
        setSelectGroupContact(true);
        groupe = UtilUsers.buildBeanGroupeDiffusion((YvsMsgGroupeDiffusion) dao.loadOneByNameQueries("YvsMsgGroupeDiffusion.findById", new String[]{"id"}, new Object[]{groupe.getId()}));
        openDialog("dlgCreateGroupDiffusion");
        update("body_dlg_groupe_diffusion");
    }

    public void updateBeanGroupeMessage() {
        setSelectGroupDossier(true);
        YvsMsgGroupeMessage entity = (YvsMsgGroupeMessage) dao.loadOneByNameQueries("YvsMsgGroupeMessage.findById", new String[]{"id"}, new Object[]{groupeM.getId()});
        groupeM = UtilUsers.buildBeanGroupeMessage(entity);
        buildListParentGroupeMessage(entity);
        if ((entity.getParent() != null) ? entity.getParent().getId() != 0 : false) {
            parentGroupMessage = UtilUsers.buildBeanGroupeMessage(entity.getParent());
        } else {
            resetFiche(parentGroupMessage);
            parentGroupMessage.getListDestinataire().clear();
            parentGroupMessage.getListDossier().clear();
        }
        openDialog("dlgCreateGroupMessage");
        update("body_dlg_groupe_message");
    }

    public void onLectureMessage() {
        setSelectMessage(false);
        setOptionUpdate(false);
        setOnLecture(true);
        if (isInInbox() && !destination.isAccuse()) {
            destination.setDateReception(new Date());
            destination.setHeureReception(new Date());
            destination.setAccuse(true);
            destination.setCopie(destination.isCopie());
            saveLecture();
        }
        update("head_messagerie");
        update("body_messagerie");
    }

    public void onReponseMessage() {
        setOnLecture(false);
        setViewListe(false);
        setUpdateMessage(false);
        setRepondre(true);
        Conversation temp = new Conversation();
        cloneObject(temp, conversation);
        destination.setDestinataire(temp.getEmetteur());
        conversation.setObjet("Re:" + temp.getObjet());
        conversation.setContenu(null);
        conversation.setDateEnvoi(new Date());
        conversation.setHeureEnvoi(new Date());
        for (Users u : listUsers) {
            listUsers.get(listUsers.indexOf(u)).setSelectActif(false);
        }
        if (listUsers.contains(temp.getEmetteur())) {
            listUsers.get(listUsers.indexOf(temp.getEmetteur())).setSelectActif(true);
        }
        Theme t = new Theme((int) temp.getEmetteur().getId(), temp.getEmetteur().getNomUsers(), temp.getEmetteur().getNomUsers());
        if (listSelectThemes == null) {
            listSelectThemes = new ArrayList<>();
        }
        listSelectThemes.add(t);
        listSelectUsers.clear();
        listSelectUsers.add(temp.getEmetteur());
        parent = buildConversation(temp);
        update("head_messagerie");
        update("body_messagerie");
    }

    public void showHistorique() {
        setViewHistorique(true);
        update("panel_read_message");
    }

    public void hideHistorique() {
        setViewHistorique(false);
        update("panel_read_message");
    }

    @Override
    public Conversation recopieView() {
        Conversation r = new Conversation();
        r.setId(conversation.getId());
        r.setContenu(conversation.getContenu());
        r.setDateEnvoi((conversation.getDateEnvoi() != null) ? conversation.getDateEnvoi() : new Date());
        r.setHeureEnvoi((conversation.getHeureEnvoi() != null) ? conversation.getHeureEnvoi() : new Date());
        r.setObjet(conversation.getObjet());
        r.setEnvoyer(conversation.isEnvoyer());
        r.setEmetteur(conversation.getEmetteur());
        r.setPriorite(conversation.getPriorite());
        r.setPieceJointe(conversation.getPieceJointe());
        r.setAccuse(false);
        r.setDelete(false);
        return r;
    }

    public FiltreMail recopieViewFiltreMail() {
        FiltreMail r = new FiltreMail();
        r.setBase(filtreMail.getBase());
        r.setId(filtreMail.getId());
        r.setValeur(filtreMail.getValeur());
        if ((filtreMail.getGroupeMessage() != null) ? filtreMail.getGroupeMessage().getId() != 0 : false) {
            r.setGroupeMessage(filtreMail.getGroupeMessage());
        }
        return r;
    }

    public Destinataire recopieViewDestination() {
        Destinataire r = new Destinataire();
        r.setId(destination.getId());
        r.setAccuse(destination.isAccuse());
        r.setDateReception((destination.getDateReception() != null) ? destination.getDateReception() : new Date());
        r.setHeureReception((destination.getHeureReception() != null) ? destination.getHeureReception() : new Date());
        r.setDestinataire(destination.getDestinataire());
        r.setExterne(destination.isExterne());
        r.setCopie(destination.isCopie());
        r.setAdresseExterne(destination.getAdresseExterne());
        r.setMessage(conversation);
        r.setGroupe(destination.getGroupe());
        r.setDelete(false);
        return r;
    }

    public void changeViewUser() {
        setViewListUser(!viewListUser);
        update("head_dlg_contacts");
        update("body_dlg_contacts");
    }

    public void buildDestinataires() {
        destinatairesExtern = null;
        if (!isClickCcInter()) {
            if (listSelectThemes == null) {
                listSelectThemes = new ArrayList<>();
            } else {
                listSelectThemes.clear();
            }
            for (Users u : listSelectUsers) {
                Theme t1 = new Theme((int) u.getId(), u.getNomUsers(), u.getNomUsers());
                listSelectThemes.add(t1);
            }
        } else {
            if (listCcSelectThemes == null) {
                listCcSelectThemes = new ArrayList<>();
            } else {
                listCcSelectThemes.clear();
            }
            for (Users u : listCcDestInterne) {
                Theme t1 = new Theme((int) u.getId(), u.getNomUsers(), u.getNomUsers());
                listCcSelectThemes.add(t1);
            }
        }
        update("panel_update");
    }

    public void buildDestinatairesExterne() {
        if (!carnetAdressesSelect.isEmpty()) {
            if (isAdressExtCC()) {
                for (CarnetAdresse c : carnetAdressesSelect) {
                    String[] list = buildListDestinataire(ccDestExterne);
                    boolean trouv_ = false;
                    for (String t : list) {
                        if (t.equals(c.getAdresse())) {
                            trouv_ = true;
                            break;
                        }
                    }
                    if (!trouv_) {
                        if (ccDestExterne == null || "".equals(ccDestExterne)) {
                            ccDestExterne = c.getAdresse();
                        } else {
                            ccDestExterne = ccDestExterne + "; " + c.getAdresse();
                        }
                    }
                }
                update("txt_list_cc_destinataire_ext");
            } else {
                for (CarnetAdresse c : carnetAdressesSelect) {
                    String[] list = buildListDestinataire(destinatairesExtern);
                    boolean trouv_ = false;
                    for (String t : list) {
                        if (t.equals(c.getAdresse())) {
                            trouv_ = true;
                            break;
                        }
                    }
                    if (!trouv_) {
                        if (destinatairesExtern == null || "".equals(destinatairesExtern)) {
                            destinatairesExtern = c.getAdresse();
                        } else {
                            destinatairesExtern = destinatairesExtern + "; " + c.getAdresse();
                        }
                    }
                    update("txt_list_destinataire_ext");
                }
            }
            resetPageCarnetAdresse();
        }
    }

    public void selectOnViewUser(Users bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            listUsers.get(listUsers.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (!isClickCcInter()) {
                if (listSelectUsers.contains(bean)) {
                    listSelectUsers.remove(bean);
                } else {
                    listSelectUsers.add(bean);
                }
                if (listSelectUsers.isEmpty()) {
                    if (listSelectThemes != null) {
                        listSelectThemes.clear();
                    }
                }
                setSelectRecepteur(!listSelectUsers.isEmpty());
            } else {
                if (listCcDestInterne.contains(bean)) {
                    listCcDestInterne.remove(bean);
                } else {
                    listCcDestInterne.add(bean);
                }
                if (listCcDestInterne.isEmpty()) {
                    if (listCcSelectThemes != null) {
                        listCcSelectThemes.clear();
                    }
                }
                setSelectRecepteur(!listCcDestInterne.isEmpty());
            }
            update("form_dlg_contacts_00");
            update("footer_dlg_contacts");
        }
    }

    public void selectOnViewCarnetAdresse(CarnetAdresse bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            carnetAdresses.get(carnetAdresses.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (carnetAdressesSelect.contains(bean)) {
                carnetAdressesSelect.remove(bean);
            } else {
                carnetAdressesSelect.add(bean);
            }
            setSelectCarnetAdresse(!carnetAdressesSelect.isEmpty());
            update("footer_dlg_carnetAdress");
        }
    }

    public void selectOnViewDiffusion(Users bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            listUsersDiffusion.get(listUsersDiffusion.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectUsersDiffusion.contains(bean)) {
                listSelectUsersDiffusion.remove(bean);
            } else {
                listSelectUsersDiffusion.add(bean);
            }
            setSelectMembreDiffusion(!listSelectUsersDiffusion.isEmpty());
            update("footer_dlg_contacts_00");
        }
    }

    public void loadOnViewUsers(SelectEvent ev) {
        if (ev != null) {
            Users bean = (Users) ev.getObject();
            selectOnViewUser(bean);
            update("form_dlg_contacts_00");
        }
    }

    public void unLoadOnViewUsers(UnselectEvent ev) {
        if (ev != null) {
            Users bean = (Users) ev.getObject();
            selectOnViewUser(bean);
            update("form_dlg_contacts_00");
        }
    }

    public void selectOnViewGroupe(GroupeDiffusion bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            listGroupDiffusion.get(listGroupDiffusion.indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectGroupDiffusion.contains(bean)) {
                listSelectGroupDiffusion.remove(bean);
                for (DiffusionContact g : bean.getDiffusionContactList()) {
                    if (listSelectUsers.contains(g.getUser())) {
                        listSelectUsers.remove(g.getUser());
                        listUsers.get(listUsers.indexOf(g.getUser())).setSelectActif(false);
                    }
                }
            } else {
                listSelectGroupDiffusion.add(bean);
                for (DiffusionContact g : bean.getDiffusionContactList()) {
                    if (!listSelectUsers.contains(g.getUser())) {
                        listSelectUsers.add(g.getUser());
                        listUsers.get(listUsers.indexOf(g.getUser())).setSelectActif(true);
                    }
                }
            }
            update("body_dlg_contacts");
            update("footer_dlg_contacts");
        }
    }

    public void populateViewGroupDiffusion(GroupeDiffusion bean) {// 
        if ((bean != null) ? bean.getId() != 0 : false) {
            cloneObject(groupe, bean);
            if (!groupe.isPublics()) {
                entityGroupDiffusion = buildGroupeDiffusion(groupe);
            }
        }
    }

    public void populateViewGroupMessage(GroupeMessage bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            resetPageGroupeMessage(bean);
            bean.setSelectActif(!bean.isSelectActif());
            if (bean.isSelectActif()) {
                cloneObject(groupeM, bean);
                setSelectGroupDossier(true);
                entityGroupMessage = buildGroupeMessage(groupeM);
            } else {
                resetFicheGroupeMessage();
                entityGroupMessage = new YvsMsgGroupeMessage();
            }
            update("data_dossier");
        }
    }

    public void populateViewContact(DiffusionContact bean) {
        if ((bean != null) ? bean.getId() != 0 : false) {
            bean.setSelectActif(!bean.isSelectActif());
            groupe.getDiffusionContactList().get(groupe.getDiffusionContactList().indexOf(bean)).setSelectActif(bean.isSelectActif());
            if (listSelectDiffusionContact.contains(bean)) {
                listSelectDiffusionContact.remove(bean);
            } else {
                listSelectDiffusionContact.add(bean);
            }
            setSelectContact(!listSelectDiffusionContact.isEmpty());
            update("data_contact");
        }
    }

    public void loadOnViewContact(SelectEvent ev) {
        if (ev != null) {
            DiffusionContact bean = (DiffusionContact) ev.getObject();
            populateViewContact(bean);
        }
    }

    public void unLoadOnViewContact(UnselectEvent ev) {
        if (ev != null) {
            DiffusionContact bean = (DiffusionContact) ev.getObject();
            populateViewContact(bean);
        }
    }

    public void resetPageGroupeDiffusion(GroupeDiffusion bean) {
        for (GroupeDiffusion g : listGroupDiffusion) {
            if (!g.equals(bean)) {
                listGroupDiffusion.get(listGroupDiffusion.indexOf(g)).setSelectActif(false);
            }
        }
    }

    public void resetPageGroupeMessage(GroupeMessage bean) {
        for (GroupeMessage g : listGroupMessage) {
            if (!g.equals(bean)) {
                listGroupMessage.get(listGroupMessage.indexOf(g)).setSelectActif(false);
            }
        }
    }

    public void resetPageGroupeDiffusion() {
        for (GroupeDiffusion g : listGroupDiffusion) {
            listGroupDiffusion.get(listGroupDiffusion.indexOf(g)).setSelectActif(false);
        }
    }

    public void resetPageGroupeMessage() {
        for (GroupeMessage g : listGroupMessage) {
            listGroupMessage.get(listGroupMessage.indexOf(g)).setSelectActif(false);
        }
    }

    @Override
    public void populateView(Conversation bean) {
        cloneObject(conversation, bean);
        if ((conversation.getPieceJointe() != null) ? !"".equals(conversation.getPieceJointe()) : false) {
            listPiecesJointe = buildListPieceJointe(conversation.getPieceJointe());
        }
        filtreMail.setValeur(conversation.getEmetteur().getNomUsers());
        entityMessage = buildConversation(conversation);
    }

    public void populateViewDestinataire(Destinataire bean) {
        cloneObject(destination, bean);
        update("split_marquer_menu");
        if ((destination.getDestinataire() != null) ? destination.getDestinataire().getId() != 0 : false) {
            listSelectUsers.add(destination.getDestinataire());
        }
        populateView(destination.getMessage());
    }

    @Override
    public void selectOnView(Conversation bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailIndox.get(listMailIndox.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectMessages.contains(bean)) {
            listSelectMessages.remove(bean);
        } else {
            listSelectMessages.add(bean);
        }
        if (listSelectMessages.isEmpty()) {
            resetFiche();
        } else {
            populateView(listSelectMessages.get(listSelectMessages.size() - 1));
        }
        setSelectMessage(!listSelectMessages.isEmpty());
        setOptionTransfert(listSelectMessages.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewInbox(Destinataire bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailIndox.get(listMailIndox.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectDestination.contains(bean)) {
            listSelectDestination.remove(bean);
        } else {
            listSelectDestination.add(bean);
        }
        if (listSelectDestination.isEmpty()) {
            resetFiche();
            selectAllInbox = false;
            update("data_messageRecu:chk_selectAllInbox");
        } else {
            if (listSelectDestination.size() == listMailIndox.size()) {
                selectAllInbox = true;
                update("data_messageRecu:chk_selectAllInbox");
            } else {
                selectAllInbox = false;
                update("data_messageRecu:chk_selectAllInbox");
            }
            populateViewDestinataire(listSelectDestination.get(listSelectDestination.size() - 1));
        }
        setSelectMessage(!listSelectDestination.isEmpty());
        setOptionTransfert(listSelectDestination.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewFolder(Destinataire bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailFolder.get(listMailFolder.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectDestination.contains(bean)) {
            listSelectDestination.remove(bean);
        } else {
            listSelectDestination.add(bean);
        }
        if (listSelectDestination.isEmpty()) {
            resetFiche();
            selectAllFolder = false;
            update("data_dossier:chk_selectAllFolder");
        } else {
            if (listSelectDestination.size() == listMailFolder.size()) {
                selectAllFolder = true;
                update("data_dossier:chk_selectAllFolder");
            } else {
                selectAllFolder = false;
                update("data_dossier:chk_selectAllFolder");
            }
            populateViewDestinataire(listSelectDestination.get(listSelectDestination.size() - 1));
        }
        setSelectMessage(!listSelectDestination.isEmpty());
        setOptionTransfert(listSelectDestination.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewSend(Conversation bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailSend.get(listMailSend.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectMessages.contains(bean)) {
            listSelectMessages.remove(bean);
        } else {
            listSelectMessages.add(bean);
        }
        if (listSelectMessages.isEmpty()) {
            resetFiche();
            selectAllSend = false;
            update("data_messageSend:chk_selectAllSend");
        } else {
            if (listSelectMessages.size() == listMailSend.size()) {
                selectAllSend = true;
                update("data_messageSend:chk_selectAllSend");
            } else {
                selectAllSend = false;
                update("data_messageSend:chk_selectAllSend");
            }
            populateView(listSelectMessages.get(listSelectMessages.size() - 1));
        }
        setSelectMessage(!listSelectMessages.isEmpty());
        setOptionTransfert(listSelectMessages.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewDraft(Conversation bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailDraft.get(listMailDraft.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (listSelectMessages.contains(bean)) {
            listSelectMessages.remove(bean);
        } else {
            listSelectMessages.add(bean);
        }
        if (listSelectMessages.isEmpty()) {
            resetFiche();
            selectAllDraft = false;
            update("data_messageBrouillon:chk_selectAllDraft");
        } else {
            if (listSelectMessages.size() == listMailDraft.size()) {
                selectAllDraft = true;
                update("data_messageBrouillon:chk_selectAllDraft");
            } else {
                selectAllDraft = false;
                update("data_messageBrouillon:chk_selectAllDraft");
            }
            populateView(listSelectMessages.get(listSelectMessages.size() - 1));
        }
        setSelectMessage(!listSelectMessages.isEmpty());
        setOptionUpdate(listSelectMessages.size() == 1);
        setOptionTransfert(listSelectMessages.size() == 1);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewCorb(Conversation bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailCorb.get(listMailCorb.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (bean.isConversation()) {
            if (listSelectMessages.contains(bean)) {
                listSelectMessages.remove(bean);
            } else {
                listSelectMessages.add(bean);
            }
            if (!listSelectMessages.isEmpty()) {
                populateView(listSelectMessages.get(listSelectMessages.size() - 1));
            }
        } else {
            if ((bean.getListDestinataire() != null) ? !bean.getListDestinataire().isEmpty() : false) {
                Destinataire d = bean.getListDestinataire().get(0);
                d.setMessage(bean);
                if (!listSelectDestination.contains(d)) {
                    listSelectDestination.add(d);
                } else {
                    listSelectDestination.remove(d);
                }
            }
            if (!listSelectDestination.isEmpty()) {
                populateViewDestinataire(listSelectDestination.get(listSelectDestination.size() - 1));
            }
        }
        if (listSelectMessages.size() + listSelectDestination.size() < 1) {
            resetFiche();
            selectAllCorb = false;
            update("data_messageSupprimer:chk_selectAllCorb");
        } else if (listSelectMessages.size() + listSelectDestination.size() == listMailCorb.size()) {
            selectAllCorb = true;
            update("data_messageSupprimer:chk_selectAllCorb");
        } else {
            selectAllCorb = false;
            update("data_messageSupprimer:chk_selectAllCorb");
        }
        setSelectMessage(listSelectMessages.size() + listSelectDestination.size() > 0);
        setOptionTransfert(listSelectMessages.size() + listSelectDestination.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void selectOnViewSearch(Conversation bean) {
        bean.setSelectActif(!bean.isSelectActif());
        listMailSearch.get(listMailSearch.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (bean.isConversation()) {
            if (listSelectMessages.contains(bean)) {
                listSelectMessages.remove(bean);
            } else {
                listSelectMessages.add(bean);
            }
            if (!listSelectMessages.isEmpty()) {
                populateView(listSelectMessages.get(listSelectMessages.size() - 1));
            }
        } else {
            if ((bean.getListDestinataire() != null) ? !bean.getListDestinataire().isEmpty() : false) {
                Destinataire d = bean.getListDestinataire().get(0);
                d.setMessage(bean);
                if (!listSelectDestination.contains(d)) {
                    listSelectDestination.add(d);
                } else {
                    listSelectDestination.remove(d);
                }
            }
            if (!listSelectDestination.isEmpty()) {
                populateViewDestinataire(listSelectDestination.get(listSelectDestination.size() - 1));
            }
        }
        if (listSelectMessages.size() + listSelectDestination.size() < 1) {
            resetFiche();
            selectAllSearch = false;
            update("data_messageSearch:chk_selectAllSearch");
        } else if (listSelectMessages.size() + listSelectDestination.size() == listMailCorb.size()) {
            selectAllSearch = true;
            update("data_messageSearch:chk_selectAllSearch");
        } else {
            selectAllSearch = false;
            update("data_messageSearch:chk_selectAllSearch");
        }
        setSelectMessage(listSelectMessages.size() + listSelectDestination.size() > 0);
        setOptionTransfert(listSelectMessages.size() + listSelectDestination.size() == 1);
        setOptionUpdate(false);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
    }

    public void loadOnViewParentGroupeMessage(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            GroupeMessage bean = (GroupeMessage) ev.getObject();
            String rq;
            for (Destinataire d : listSelectDestination) {
                if ((bean != null) ? bean.getId() > 0 : false) {
                    rq = "UPDATE yvs_msg_destinataire SET groupe_message = " + bean.getId() + " WHERE id=?";
                    if (listMailFolder.contains(d)) {
                        listMailFolder.remove(d);
                    }
                    if (listMailIndox.contains(d)) {
                        listMailIndox.remove(d);
                    }
                } else {
                    rq = "UPDATE yvs_msg_destinataire SET groupe_message = null WHERE id=?";
                    if (listMailFolder.contains(d)) {
                        listMailFolder.remove(d);
                    }
                }
                Options[] param = new Options[]{new Options(d.getId(), 1)};
                dao.requeteLibre(rq, param);
            }
            update("panel_messagerie_01");
        }
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null) {
            Destinataire bean = (Destinataire) ev.getObject();
            populateViewDestinataire(bean);
            onLectureMessage();
            update("data_messageRecu");
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null) {
            Destinataire bean = (Destinataire) ev.getObject();
            selectOnViewInbox(bean);
            update("data_messageRecu");
        }

    }

    public void loadOnViewFolder(SelectEvent ev) {
        if (ev != null) {
            Destinataire bean = (Destinataire) ev.getObject();
            populateViewDestinataire(bean);
            onLectureMessage();
            update("data_dossier");
        }
    }

    public void unLoadOnViewFolder(UnselectEvent ev) {
        if (ev != null) {
            Destinataire bean = (Destinataire) ev.getObject();
            selectOnViewFolder(bean);
            update("data_dossier");
        }
    }

    public void loadOnViewSend(SelectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            populateView(bean);
            onLectureMessage();
            update("data_messageSend");
        }
    }

    public void unLoadOnViewSend(UnselectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            selectOnViewSend(bean);
            update("data_messageSend");
        }
    }

    public void loadOnViewDraft(SelectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            populateView(bean);
            onLectureMessage();
            update("data_messageBrouillon");
        }
    }

    public void unLoadOnViewDraft(UnselectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            selectOnViewDraft(bean);
            update("data_messageBrouillon");
        }
    }

    public void loadOnViewCorb(SelectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            populateView(bean);
            onLectureMessage();
            update("data_messageSupprimer");
        }
    }

    public void unLoadOnViewCorb(UnselectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            selectOnViewCorb(bean);
            update("data_messageSupprimer");
        }
    }

    public void loadOnViewSearch(SelectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            populateView(bean);
            onLectureMessage();
            update("data_messageSearch");
        }
    }

    public void unLoadOnViewSearch(UnselectEvent ev) {
        if (ev != null) {
            Conversation bean = (Conversation) ev.getObject();
            selectOnViewCorb(bean);
            update("data_messageSearch");
        }
    }

    public Destinataire getFullDestinataire(YvsMsgDestinataire d) {
        Destinataire r = UtilUsers.buildBeanDestinataire(d);
        r.setMessage(UtilUsers.buildBeanConversation(d.getMessage()));
        return r;
    }

    public List<Destinataire> getFullListDestinataire(List<YvsMsgDestinataire> list) {
        List<Destinataire> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgDestinataire e : list) {
                r.add(getFullDestinataire(e));
            }
        }
        return r;
    }

    public Conversation getConversationByDestinataire(Destinataire d) {
        Conversation bean = d.getMessage();
        bean.setConversation(false);
        bean.setIdDestinataire(d.getId());
        bean.setNomDestinataire(d.getSendTo());
        bean.getListDestinataire().clear();
        bean.getListDestinataire().add(d);
        return bean;
    }

    @Override
    public void loadAll() {
        listMailIndox.clear();
        champ = new String[]{"destinataire", "supp"};
        val = new Object[]{currentUser.getUsers(), false};
        listMailIndox = getFullListDestinataire(dao.loadNameQueries("YvsMsgDestinataire.findByDestinataire", champ, val));
        update("data_messageRecu");
    }

    public void loadAllInboxDel() {
        champ = new String[]{"destinataire", "supp"};
        val = new Object[]{currentUser.getUsers(), true};
        List<Destinataire> list = getFullListDestinataire(dao.loadNameQueries("YvsMsgDestinataire.findByDelete", champ, val));
        for (Destinataire d : list) {
            listMailCorb.add(getConversationByDestinataire(d));
        }
    }

    public void loadAllDraft() {
        listMailDraft.clear();
        champ = new String[]{"emetteur", "envoyer"};
        val = new Object[]{currentUser.getUsers(), false};
        listMailDraft = UtilUsers.buildBeanListConversation(dao.loadNameQueries("YvsMsgConversation.findByEnvoyer", champ, val));
    }

    public void loadAllSend() {
        listMailSend.clear();
        champ = new String[]{"emetteur", "envoyer"};
        val = new Object[]{currentUser.getUsers(), true};
        listMailSend = UtilUsers.buildBeanListConversation(dao.loadNameQueries("YvsMsgConversation.findByEnvoyer", champ, val));
    }

    public void loadAllDelete() {
        listMailCorb.clear();
        champ = new String[]{"emetteur", "supp"};
        val = new Object[]{currentUser.getUsers(), true};
        listMailCorb = UtilUsers.buildBeanListConversation(dao.loadNameQueries("YvsMsgConversation.findByEmetteur", champ, val));
        loadAllInboxDel();
    }

    public void loadAllAccuse(boolean accuse) {
//        listMailIndox.clear();
//        champ = new String[]{"accuse", "destinataire"};
//        val = new Object[]{accuse, currentUser.getUsers()};
//        listMailIndox = getFullListDestinataire(dao.loadNameQueries("YvsMsgDestinataire.findByAccuse", champ, val));
//        update("data_messageRecu");
    }

    public void loadAllUsers() {
        listUsers.clear();
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        listUsers = UtilUsers.buildBeanListUsers(dao.loadNameQueries("YvsUsers.findBySociete", champ, val));
        onConstructAutoUser();
    }

    public void loadAllUsersChat() {
        listUsers.clear();
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        listUsers = UtilUsers.buildBeanListUsers(dao.loadNameQueries("YvsUsers.findBySociete", champ, val));
        if (currentUser != null) {
            listUsers.remove(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        }
    }

    public void loadAllUsersDiffusion() {
        listUsersDiffusion.clear();
        champ = new String[]{"societe", "actif"};
        val = new Object[]{currentAgence.getSociete(), true};
        listUsersDiffusion = UtilUsers.buildBeanListUsers(dao.loadNameQueries("YvsUsers.findBySociete", champ, val));
        for (DiffusionContact c : groupe.getDiffusionContactList()) {
            for (Users u : listUsersDiffusion) {
                if (u.getId() == c.getUser().getId()) {
                    listUsersDiffusion.get(listUsersDiffusion.indexOf(u)).setSelectActif(true);
                    listUsersDiffusion.get(listUsersDiffusion.indexOf(u)).setViewActif(true);
                    break;
                }
            }
        }
    }

    public void loadAllGroupeMessage() {
        listGroupMessage.clear();
        champ = new String[]{"users"};
        val = new Object[]{currentUser.getUsers()};
        List<YvsMsgGroupeMessage> list = buildListYvsGroupMessage(dao.loadNameQueries("YvsMsgGroupeMessage.findByUsers", champ, val));
        for (YvsMsgGroupeMessage ent : list) {
            listGroupMessage.add(buildGroupMessage(ent));
        }
    }

    public void loadAllParentGroupMessage() {
        listParentGroupMessage.clear();
//        listParentGroupMessage = UtilUsers.buildBeanListGroupeMessage(currentUser.getUsers().getYvsGroupeMessageList());
    }

    public void loadAllCarnetAdresse() {
        carnetAdresses.clear();
//        carnetAdresses = UtilUsers.buildBeanListCarnetAdresse(currentUser.getUsers().getYvsMsgCarnetAdresseList());
    }

    public List<GroupeDiffusion> loadAllGroupeDiffusionPublic() {
        champ = new String[]{"societe"};
        val = new Object[]{currentAgence.getSociete()};
        return UtilUsers.buildBeanListGroupeDiffusion(dao.loadNameQueries("YvsMsgGroupeDiffusion.findBySociete", champ, val));
    }

    public int loadCountAlert() {
        champ = new String[]{"destinataire", "accuse"};
        val = new Object[]{currentUser.getUsers(), false};
        List<YvsMsgDestinataire> list = dao.loadNameQueries("YvsMsgDestinataire.findAllAlert", champ, val);
        return list.size();
    }

    public List<YvsMsgGroupeMessage> buildListYvsGroupMessage(List<YvsMsgGroupeMessage> list) {
        List<YvsMsgGroupeMessage> list1 = new ArrayList<>();
        for (YvsMsgGroupeMessage g : list) {
            if ((g.getParent() != null) ? g.getParent().getId() == 0 : true) {
                list1.add(g);
            }
        }
        return list1;
    }

    public GroupeMessage buildGroupMessage(YvsMsgGroupeMessage ent) {
        GroupeMessage bean = new GroupeMessage();
        if (ent != null) {
            bean = UtilUsers.buildBeanGroupeMessage(ent);
//            if ((ent.getYvsGroupeMessageList() != null) ? !ent.getYvsGroupeMessageList().isEmpty() : false) {
//                for (YvsMsgGroupeMessage g : ent.getYvsGroupeMessageList()) {
//                    bean.getListDossier().add(buildGroupMessage(g));
//                }
//            }
        }
        return bean;
    }

    @Override
    public void resetPage() {
        resetPageCorb(false);
        resetPageInbox(false);
        resetPageSend(false);
        resetPageDraft(false);
        setRepondre(false);
        listSelectMessages.clear();
        listSelectDestination.clear();
        listSelectUsers.clear();
        setSelectMessage(false);
        for (Users u : listUsers) {
            listUsers.get(listUsers.indexOf(u)).setSelectActif(false);
        }
        for (GroupeDiffusion u : listGroupDiffusion) {
            listGroupDiffusion.get(listGroupDiffusion.indexOf(u)).setSelectActif(false);
        }
    }

    @Override
    public void resetFiche() {
        resetFiche(conversation);
        conversation.setEmetteur(new Users());
        conversation.setListDestinataire(new ArrayList<Destinataire>());
        nbrPieceJointe = 0;
        parent = new YvsMsgConversation();
        destinatairesExtern = null;
        if (listSelectThemes != null) {
            listSelectThemes.clear();
        }
        selectAllCorb = false;
        selectAllDraft = false;
        selectAllFolder = false;
        selectAllInbox = false;
        selectAllSend = false;
        setUpdateMessage(false);
        setOnLecture(false);
        resetFicheDestinataire();
        resetPage();
        ccDestInterne = null;
        update("panel_write_message");
        update("head_messagerie");
    }

    public void resetFicheDestinataire() {
        resetFiche(destination);
        destination.setDestinataire(new Users());
        destination.setMessage(new Conversation());
    }

    public void resetPageCarnetAdresse() {
        carnetAdressesSelect.clear();
        setSelectCarnetAdresse(false);
        for (CarnetAdresse c : carnetAdresses) {
            carnetAdresses.get(carnetAdresses.indexOf(c)).setSelectActif(false);
        }

    }

    public void onNewFiche() {
        setUpdateMessage(false);
    }

    @Override
    public boolean saveNew() {
        String action = (updateMessage) ? "Modification" : "Insertion";
        try {
            Conversation bean = recopieView();
            if (controleFiche(bean)) {
                entityMessage = buildConversation(bean);
                if (!isUpdateMessage()) {
                    entityMessage.setId(null);
                    entityMessage = (YvsMsgConversation) dao.save1(entityMessage);
                    bean.setId(entityMessage.getId());
                    conversation.setId(bean.getId());
                    saveNewDestinataire(bean);
                } else {
                    YvsMsgConversation c = (YvsMsgConversation) dao.loadOneByNameQueries("YvsMsgConversation.findById", new String[]{"id"}, new Object[]{entityMessage.getId()});
                    if ((c.getReponse() != null) ? c.getReponse().getId() != 0 : false) {
                        entityMessage.setReponse(c.getReponse());
                    }
                    dao.update(entityMessage);
//                    for (YvsMsgDestinataire d : c.getYvsMsgDestinataireList()) {
//                        if (!d.getExterne()) {
//                            Users u = UtilUsers.buildBeanUsers(d.getDestinataire());
//                            if (!d.getCopie()) {
//                                if (listSelectUsers.contains(u)) {
//                                    listSelectUsers.remove(u);
//                                }
//                            } else {
//                                if (listCcDestInterne.contains(u)) {
//                                    listCcDestInterne.remove(u);
//                                }
//                            }
//                        } else {
//                            if (d.getCopie()) {
//                                String ec = d.getAdresseExterne();
//                                String[] listCcExternc = buildListDestinataire(ccDestExterne);
//                                ccDestExterne = null;
//                                for (int i = 0; i < listCcExternc.length; i++) {
//                                    if (!ec.equals(listCcExternc[i])) {
//                                        if (ccDestExterne == null || "".equals(ccDestExterne)) {
//                                            ccDestExterne = listCcExternc[i];
//                                        } else {
//                                            ccDestExterne = ccDestExterne + "; " + listCcExternc[i];
//                                        }
//                                    }
//                                }
//                            } else {
//                                String e = d.getAdresseExterne();
//                                String[] tab_destinataire = buildListDestinataire(destinatairesExtern);
//                                destinatairesExtern = null;
//                                for (int i = 0; i < tab_destinataire.length; i++) {
//                                    if (!e.equals(tab_destinataire[i])) {
//                                        if (destinatairesExtern == null || "".equals(destinatairesExtern)) {
//                                            destinatairesExtern = tab_destinataire[i];
//                                        } else {
//                                            destinatairesExtern = destinatairesExtern + "; " + tab_destinataire[i];
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                    saveNewDestinataire(bean);
                    listMailDraft.remove(bean);
                }
                setUpdateMessage(true);
                return true;
            }
            return false;
        } catch (Exception ex) {
            getErrorMessage(action + " Impossible !");
            logger.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void saveNewDestinataire(Conversation conv) {
        Destinataire bean = recopieViewDestination();
        String[] champ = new String[]{"users"};
        Object[] val;
        if ((listSelectUsers != null) ? !listSelectUsers.isEmpty() : false) {
            for (Users u : listSelectUsers) {
                bean.setExterne(false);
                bean.setDestinataire(u);
                bean.setCopie(false);
                bean.setAccuse(false);
                YvsMsgDestinataire entity = buildDestinataire(bean);
                val = new Object[]{new YvsUsers(u.getId())};
                List<YvsMsgFiltreMessage> list = dao.loadNameQueries("YvsMsgFiltreMessage.findByUser", champ, val);
                if ((list != null) ? !list.isEmpty() : false) {
                    boolean trouv_ = false;
                    for (YvsMsgFiltreMessage f : list) {
                        switch (f.getBase()) {
                            case "O":
                                if (f.getValeur().equals(conv.getObjet())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            case "P":
                                if (f.getValeur().equals(conv.getPriorite())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            case "E":
                                if (f.getValeur().equals(currentUser.getUsers().getNomUsers())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            default:
                                trouv_ = false;
                                break;
                        }
                        if (trouv_) {
                            break;
                        }
                    }
                }
                entity.setId(null);
                entity = (YvsMsgDestinataire) dao.save1(entity);
                bean.setId(entity.getId());
                destination.setId(entity.getId());
                if (!conv.isEnvoyer()) {
                    conv.setDestinataires(conv.getDestinataires() + "; " + u.getNomUsers());
                }
            }
        }
        if ((listCcDestInterne != null) ? !listCcDestInterne.isEmpty() : false) {
            for (Users u : listCcDestInterne) {
                bean.setExterne(false);
                bean.setDestinataire(u);
                bean.setCopie(true);
                bean.setAccuse(false);
                YvsMsgDestinataire entity = buildDestinataire(bean);
                val = new Object[]{new YvsUsers(u.getId())};
                List<YvsMsgFiltreMessage> list = dao.loadNameQueries("YvsMsgFiltreMessage.findByUser", champ, val);
                if ((list != null) ? !list.isEmpty() : false) {
                    boolean trouv_ = false;
                    for (YvsMsgFiltreMessage f : list) {
                        switch (f.getBase()) {
                            case "O":
                                if (f.getValeur().equals(conv.getObjet())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            case "P":
                                if (f.getValeur().equals(conv.getPriorite())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            case "E":
                                if (f.getValeur().equals(conv.getEmetteur().getNomUsers())) {
                                    entity.setGroupeMessage(f.getGroupeMessage());
                                    trouv_ = true;
                                }
                                break;
                            default:
                                trouv_ = false;
                                break;
                        }
                        if (trouv_) {
                            break;
                        }
                    }
                }
                entity.setId(null);
                entity = (YvsMsgDestinataire) dao.save1(entity);
                bean.setId(entity.getId());
                destination.setId(entity.getId());
                if (!conv.isEnvoyer()) {
                    conv.setDestinataires(conv.getDestinataires() + "; " + u.getNomUsers());
                }
            }
        }
        String[] listCcExtern = buildListDestinataire(ccDestExterne);
        loadAllCarnetAdresse();
        if ((listCcExtern != null) ? listCcExtern.length > 0 : false) {
            for (String destinataire : listCcExtern) {
                bean.setExterne(true);
                bean.setCopie(true);
                bean.setAccuse(false);
                bean.setAdresseExterne(destinataire);
                YvsMsgDestinataire entity = buildDestinataire(bean);
                entity.setId(null);
                entity = (YvsMsgDestinataire) dao.save1(entity);
                bean.setId(entity.getId());
                conversation.setId(entity.getId());
                if (!conv.isEnvoyer()) {
                    conv.setDestinataires(conv.getDestinataires() + "; " + destinataire);
                }
                boolean trouv_ = false;
                for (CarnetAdresse c : carnetAdresses) {
                    if (c.getAdresse().equals(destinataire)) {
                        trouv_ = true;
                        break;
                    }
                }
                if (!trouv_) {
                    CarnetAdresse c = new CarnetAdresse(destinataire);
                    YvsMsgCarnetAdresse ent = buildCarnetAdresse(c);
                    ent.setId(null);
                    ent = (YvsMsgCarnetAdresse) dao.save1(ent);
//                    currentUser.getUsers().getYvsMsgCarnetAdresseList().add(ent);
                }
            }
        }
        String[] tab_destinataire = buildListDestinataire(destinatairesExtern);
        if ((tab_destinataire != null) ? tab_destinataire.length > 0 : false) {
            for (String destinataire : tab_destinataire) {
                bean.setExterne(true);
                bean.setCopie(false);
                bean.setAccuse(false);
                bean.setAdresseExterne(destinataire);
                YvsMsgDestinataire entity = buildDestinataire(bean);
                entity.setId(null);
                entity = (YvsMsgDestinataire) dao.save1(entity);
                bean.setId(entity.getId());
                conversation.setId(entity.getId());
                if (!conv.isEnvoyer()) {
                    conv.setDestinataires(conv.getDestinataires() + "; " + destinataire);
                }
                boolean trouv_ = false;
                for (CarnetAdresse c : carnetAdresses) {
                    if (c.getAdresse().equals(destinataire)) {
                        trouv_ = true;
                        break;
                    }
                }
                if (!trouv_) {
                    CarnetAdresse c = new CarnetAdresse(destinataire);
                    YvsMsgCarnetAdresse ent = buildCarnetAdresse(c);
                    ent.setId(null);
                    ent = (YvsMsgCarnetAdresse) dao.save1(ent);
//                    currentUser.getUsers().getYvsMsgCarnetAdresseList().add(ent);
                }
            }
        }
    }

    public void saveMessage(boolean envoyer) {
        conversation.setSupp(false);
        conversation.setEnvoyer(envoyer);
        conversation.setEmetteur(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        conversation.setPriorite((conversation.getPriorite() != null) ? (!"".equals(conversation.getPriorite())) ? (!"''".equals(conversation.getPriorite())) ? conversation.getPriorite() : "N" : "N" : "N");
        if (saveNew()) {
            succes();
            update("head_messagerie");
            update("body_messagerie");
        }
    }

    public boolean saveLecture() {
        Destinataire bean = recopieViewDestination();
        YvsMsgDestinataire entity = buildDestinataire(bean);
        dao.update(entity);
        if (!conversation.isAccuse() && !destination.isSendAccuse()) {
            YvsMsgConversation entite = getAccuseReception();
            entity.setId(null);
            entite = (YvsMsgConversation) dao.save1(entite);
            entityMessage = entite;
            bean.setAccuse(false);
            bean.setDestinataire(conversation.getEmetteur());
            bean.setDateReception(new Date());
            bean.setHeureReception(new Date());
            bean.setCopie(false);
            entity = buildDestinataire(bean);
            String[] champ = new String[]{"users"};
            Object[] val = new Object[]{entity.getDestinataire()};
            List<YvsMsgFiltreMessage> list = dao.loadNameQueries("YvsMsgFiltreMessage.findByUser", champ, val);
            if ((list != null) ? !list.isEmpty() : false) {
                boolean trouv_ = false;
                for (YvsMsgFiltreMessage f : list) {
                    switch (f.getBase()) {
                        case "O":
                            if (f.getValeur().equals(entite.getObjet())) {
                                entity.setGroupeMessage(f.getGroupeMessage());
                                trouv_ = true;
                            }
                            break;
                        case "P":
                            if (f.getValeur().equals(entite.getPriorite())) {
                                entity.setGroupeMessage(f.getGroupeMessage());
                                trouv_ = true;
                            }
                            break;
                        case "E":
                            if (f.getValeur().equals(entite.getEmetteur().getNomUsers())) {
                                entity.setGroupeMessage(f.getGroupeMessage());
                                trouv_ = true;
                            }
                            break;
                        default:
                            trouv_ = false;
                            break;
                    }
                    if (trouv_) {
                        break;
                    }
                }
            }
            entity.setId(null);
            dao.save1(entity);
        }
        String rq = "UPDATE yvs_msg_destinataire SET send_accuse = true WHERE id=?";
        Options[] param = new Options[]{new Options(bean.getId(), 1)};
        dao.requeteLibre(rq, param);
        destination.setSendAccuse(true);
        return true;
    }

    public YvsMsgConversation getAccuseReception() {
        YvsMsgConversation entite = new YvsMsgConversation();
        entite.setContenu("J'ai bien recu votre message....");
        entite.setObjet("Accusé de lecture");
        entite.setEmetteur(currentUser.getUsers());
        entite.setDateEnvoi(new Date());
        entite.setHeureEnvoi(new Date());
        entite.setReference(entite.getObjet() + "_" + System.currentTimeMillis());
        entite.setDelet(false);
        entite.setEnvoyer(true);
        entite.setExterne(false);
        entite.setPriorite("N");
        entite.setSupp(false);
        entite.setReponse(entityMessage);
        entite.setAccuse(true);
        return entite;
    }

    public void onOngletChange(boolean inbox, boolean send, boolean draft, boolean corbeille, boolean contact, boolean dossier, boolean search) {
        setViewListe(true);
        setOnLecture(false);
        setInContact(contact);
        setInCorbeille(corbeille);
        setInDraft(draft);
        setInInbox(inbox);
        setInSend(send);
        setInDossier(dossier);
        setInSearch(search);
        if (inbox || send || draft || corbeille) {
            onSelectMenu(true, false, false);
        } else {
            onSelectMenu(false, contact, dossier);
        }
        resetFiche();
    }

    @Override
    public void changeView() {
        setViewListe(!viewListe);
        resetFiche();
        update("head_messagerie");
        update("body_messagerie");
    }

    public void openDlgDelete() {
        if (isInCorbeille()) {
            openDialog("dlgConfirmDeleteD");
        } else {
            openDialog("dlgConfirmDelete");
        }
    }

    public void resetFicheGroupeDiffusion() {
        resetFiche(groupe);
        groupe.setDiffusionContactList(new ArrayList<DiffusionContact>());
        groupe.setUser(new Users(currentUser.getId()));
        setSelectContact(false);
        resetPageGroupeDiffusion();
//        update("data_contact");
    }

    public void resetFicheGroupeMessage() {
        resetFiche(groupeM);
        groupeM.setListDestinataire(new ArrayList<Destinataire>());
        groupeM.setListDossier(new ArrayList<GroupeMessage>());
        resetFiche(parentGroupMessage);
        parentGroupMessage.setListDestinataire(new ArrayList<Destinataire>());
        parentGroupMessage.setListDossier(new ArrayList<GroupeMessage>());
        setSelectDossier(false);
        resetPageGroupeMessage();
//        update("data_contact");
    }

    public void saveNewGroupeDiffusion() {
        GroupeDiffusion bean = new GroupeDiffusion();
        bean.setLibelle(groupe.getLibelle());
        bean.setReference(groupe.getReference());
        bean.setPublics(false);
        if (bean.getReference() != null && !bean.getReference().equals("")) {
            bean.setId(groupe.getId());
            entityGroupDiffusion = buildGroupeDiffusion(bean);
            if (!isSelectContact()) {
                entityGroupDiffusion.setId(null);
                entityGroupDiffusion = (YvsMsgGroupeDiffusion) dao.save1(entityGroupDiffusion);
                bean.setId(entityGroupDiffusion.getId());
                groupe.setId(entityGroupDiffusion.getId());
                listGroupDiffusion.add(bean);
//                currentUser.getUsers().getYvsGroupeDiffusionList().add(entityGroupDiffusion);
                TreeNode group = new DefaultTreeNode(new MenuMessagerie(bean.getId(), bean.getReference(), "Contact", "mail_contact.png", "advanced.png", "supprimer", bean.getLibelle()), contact);
            } else {
                dao.update(entityGroupDiffusion);
//                currentUser.getUsers().getYvsGroupeDiffusionList().set(currentUser.getUsers().getYvsGroupeDiffusionList().indexOf(entityGroupDiffusion), entityGroupDiffusion);
                listGroupDiffusion.set(listGroupDiffusion.indexOf(groupe), bean);
                TreeNode group = new DefaultTreeNode(new MenuMessagerie(bean.getId(), bean.getReference(), "Contact", "mail_contact.png", "advanced.png", "supprimer", bean.getLibelle()), null);
                contact.getChildren().set(contact.getChildren().indexOf(new DefaultTreeNode(new MenuMessagerie(groupe.getId(), "Contact"), null)), group);
            }
            setSelectMessagerie(true);
            succes();
            update("panel_messagerie_00");
        } else {
            getErrorMessage("Vous devez entrer la designation");
        }
    }

    public TreeNode searchTreeNode(TreeNode p, String nameTest) {
        MenuMessagerie m = (MenuMessagerie) p.getData();
        if (m.getLibelle().equals(nameTest)) {
            return p;
        } else if (!p.getChildren().isEmpty()) {
            for (TreeNode t : p.getChildren()) {
                TreeNode c = searchTreeNode(t, nameTest);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }

    public void saveNewGroupeMessage() {
        GroupeMessage bean = new GroupeMessage();
        bean.setLibelle(groupeM.getLibelle());
        bean.setReference(groupeM.getReference());
        if (bean.getReference() != null && !bean.getReference().equals("")) {
            bean.setId(groupeM.getId());
            entityGroupMessage = buildGroupeMessage(bean);
            if (!isSelectDossier()) {
                entityGroupDiffusion.setId(null);
                entityGroupMessage = (YvsMsgGroupeMessage) dao.save1(entityGroupMessage);
                bean.setId(entityGroupMessage.getId());
                groupe.setId(entityGroupMessage.getId());
//                currentUser.getUsers().getYvsGroupeMessageList().add(entityGroupMessage);
                if ((parentGroupMessage != null) ? parentGroupMessage.getId() == 0 : true) {
                    listGroupMessage.add(bean);
                }
            } else {
                dao.update(entityGroupMessage);
//                currentUser.getUsers().getYvsGroupeMessageList().set(currentUser.getUsers().getYvsGroupeMessageList().indexOf(entityGroupMessage), entityGroupMessage);
                if ((parentGroupMessage != null) ? parentGroupMessage.getId() == 0 : true) {
                    listGroupMessage.add(bean);
                }
            }
            createMenu();
            resetFicheGroupeMessage();
            setSelectMessagerie(true);
            succes();
            update("body_dlg_groupe_message");
            update("panel_messagerie_00");
        } else {
            getErrorMessage("Vous devez entrer la designation");
        }
    }

    public void addContactInGroup() {
        for (Users u : listSelectUsersDiffusion) {
            YvsMsgDiffusionContact entity = new YvsMsgDiffusionContact();
            entity.setActif(true);
            entity.setUsers(new YvsUsers(u.getId()));
            entity.setGroupeDiffusion(entityGroupDiffusion);
            entity.setId(null);
            entity = (YvsMsgDiffusionContact) dao.save1(entity);
            DiffusionContact bean = UtilUsers.buildBeanDiffusionContact(entity);
            bean.setUser(u);
            listGroupDiffusion.get(listGroupDiffusion.indexOf(groupe)).getDiffusionContactList().add(bean);
            groupe.getDiffusionContactList().add(bean);
        }
        listSelectUsersDiffusion.clear();
        succes();
        update("data_contact");
    }
    String[] tab_menu = new String[]{"Boite de réception", "Envoyés", "Brouillons", "Corbeille", "Groupes de diffusion", "Non Lu", "Lu"};
    TreeNode contact = null, dossier = null;

    public void createMenu() {
        loadAllGroupeMessage();
        loadAllParentGroupMessage();
//        listGroupDiffusion = UtilUsers.buildBeanListGroupeDiffusion(currentUser.getUsers().getYvsGroupeDiffusionList());
        menu = new DefaultTreeNode(new MenuMessagerie(0, "Menus", "Menu"), null);
        //Les menus        
        TreeNode inbox = new DefaultTreeNode(new MenuMessagerie(1, "Boite de réception", "Mail", "mail_inbox.png", "delete.png", "Vider"), menu);
        TreeNode send = new DefaultTreeNode(new MenuMessagerie(2, "Envoyés", "Mail", "mail_send.png", "delete.png", "Vider"), menu);
        TreeNode draft = new DefaultTreeNode(new MenuMessagerie(3, "Brouillons", "Mail", "mail_draft.png", "delete.png", "Vider"), menu);
        TreeNode corbeill = new DefaultTreeNode(new MenuMessagerie(4, "Corbeille", "Mail", "mail_corb.png", "delete.png", "Vider"), menu);
        contact = new DefaultTreeNode(new MenuMessagerie(0, "Groupes de diffusion", "Groupe Contact", "mail_group_contact.png", "mail_add_contact.png", "Créer un nouveau"), menu);
        //Les groupes de diffusion public
        TreeNode publics = new DefaultTreeNode(new MenuMessagerie(5, "Groupes public", "Groupe Contact Public", "mail_group_diffusion.png", ""), menu);
        for (GroupeDiffusion g : loadAllGroupeDiffusionPublic()) {
            TreeNode t = new DefaultTreeNode(new MenuMessagerie(g.getId(), g.getReference(), "Contact Public", "mail_group_perso.png", "mail_send_all.png", "Envoyer message au groupe", g.getLibelle()), publics);
        }
        dossier = new DefaultTreeNode(new MenuMessagerie(0, "Dossiers", "Groupe Dossier", "mail_group_folder.png", "mail_add_folder.png", "Créer un nouveau"), menu);
        //Les inbox
        TreeNode inboxLu = new DefaultTreeNode(new MenuMessagerie(1, "Non Lu", "Inbox", "mail_lu.png", "delete.png", "Vider"), inbox);
        TreeNode inboxNonLu = new DefaultTreeNode(new MenuMessagerie(2, "Lu", "Inbox", "mail_non_lu.png", "delete.png", "Vider"), inbox);
        //Les groupes de diffusion privee
        for (GroupeDiffusion g : listGroupDiffusion) {
            TreeNode group = new DefaultTreeNode(new MenuMessagerie(g.getId(), g.getReference(), "Contact", "mail_contact.png", "advanced.png", "Option", g.getLibelle()), contact);
        }
        //Les dossiers
        for (GroupeMessage g : listGroupMessage) {
            TreeNode group = buildNodeChildren(g, dossier);
        }
        TreeNode search = new DefaultTreeNode(new MenuMessagerie(6, "Recherche", "Recherche Message", "mail_search.png", ""), menu);
        TreeNode searchDirect = new DefaultTreeNode(new MenuMessagerie(0, "Recherche Direct", "Recherche Direct", "mail_search.png", ""), search);
        TreeNode searchFilter = new DefaultTreeNode(new MenuMessagerie(1, "Recherche Filtrer", "Recherche Filter", "mail_search.png", ""), search);
    }

    public TreeNode buildNodeChildren(GroupeMessage bean, TreeNode parent) {
        TreeNode node = null;
        if (bean != null) {
            node = new DefaultTreeNode(new MenuMessagerie(bean.getId(), bean.getReference(), "Dossier", "mail_folder.png", "mail_folder_main.png", "Option", bean.getLibelle()), parent);
            if ((bean.getListDossier() != null) ? !bean.getListDossier().isEmpty() : false) {
                for (GroupeMessage g : bean.getListDossier()) {
                    node.getChildren().add(buildNodeChildren(g, node));
                }
            }
        }
        return node;
    }

    public void loadOnViewMenu(NodeSelectEvent ev) {
        if (ev.getTreeNode() != null) {
            DefaultTreeNode node = (DefaultTreeNode) ev.getTreeNode();
            MenuMessagerie main = (MenuMessagerie) node.getData();
            switch (main.getData()) {
                case "Mail":
                    if (main.getId() == 1) {
                        loadAll();
                        setInSousInbox(false);
                        onOngletChange(true, false, false, false, false, false, false);
                    } else if (main.getId() == 2) {
                        loadAllSend();
                        onOngletChange(false, true, false, false, false, false, false);
                    } else if (main.getId() == 3) {
                        loadAllDraft();
                        onOngletChange(false, false, true, false, false, false, false);
                    } else if (main.getId() == 4) {
                        loadAllDelete();
                        onOngletChange(false, false, false, true, false, false, false);
                    }
                    update("panel_messagerie_01");
                    update("head_messagerie");
                    break;
                case "Inbox":
                    if (main.getId() == 1) {
                        loadAllAccuse(false);
                        setInSousInbox(true);
                        onOngletChange(true, false, false, false, false, false, false);
                    } else if (main.getId() == 2) {
                        loadAllAccuse(true);
                        setInSousInbox(true);
                        onOngletChange(true, false, false, false, false, false, false);
                    }
                    update("panel_messagerie_01");
                    update("head_messagerie");
                    break;
                case "Groupe Contact":
                    break;
                case "Groupe Dossier":
                    break;
                case "Contact":
                    selectOnViewGroupeDiffusion(main.getId());
                    onOngletChange(false, false, false, false, true, false, false);
                    update("panel_messagerie_01");
                    update("head_messagerie");
                    break;
                case "Contact Public":
                    selectOnViewGroupeDiffusion(main.getId());
                    onOngletChange(false, false, false, false, true, false, false);
                    update("panel_messagerie_01");
                    update("head_messagerie");
                    break;
                case "Dossier":
                    selectOnViewGroupeMessage(main.getId());
                    onOngletChange(false, false, false, false, false, true, false);
                    update("panel_messagerie_01");
                    update("head_messagerie");
                    break;
                case "Recherche Message":
                    createTableTemp();
                    openDialog("dlgFiltreSearchMail");
                    break;
                case "Recherche Direct":
                    openDialog("dlgSearchMail");
                    break;
                case "Recherche Filter":
                    createTableTemp();
                    openDialog("dlgFiltreSearchMail");
                    break;
                default:
                    break;
            }
        }
    }

    public void selectOnViewGroupeDiffusion(int id) {
        resetFicheGroupeDiffusion();
        String[] champ = new String[]{"id"};
        Object[] val = new Object[]{id};
        GroupeDiffusion bean = UtilUsers.buildBeanGroupeDiffusion((YvsMsgGroupeDiffusion) dao.loadOneByNameQueries("YvsMsgGroupeDiffusion.findById", champ, val));
        if ((bean != null) ? bean.getId() != 0 : false) {
            populateViewGroupDiffusion(bean);
        }
        update("body_dlg_contacts");
        update("footer_dlg_contacts");
    }

    public void selectOnViewGroupeDiffusionG(GroupeDiffusion bean) {
        listGroupDiffusion.get(listGroupDiffusion.indexOf(bean)).setSelectActif(bean.isSelectActif());
        if (!isClickCcInter()) {
            if (bean.isSelectActif()) {
                for (DiffusionContact d : bean.getDiffusionContactList()) {
                    if (!listSelectUsers.contains(d.getUser())) {
                        listSelectUsers.add(d.getUser());
                        listUsers.get(listUsers.indexOf(d.getUser())).setSelectActif(true);
                    }
                }
            } else {
                for (DiffusionContact d : bean.getDiffusionContactList()) {
                    if (listSelectUsers.contains(d.getUser())) {
                        listSelectUsers.remove(d.getUser());
                        listUsers.get(listUsers.indexOf(d.getUser())).setSelectActif(false);
                    }
                }
            }
            if (listSelectUsers.isEmpty()) {
                if (listSelectThemes != null) {
                    listSelectThemes.clear();
                }
                resetFicheGroupeDiffusion();
                entityGroupDiffusion = new YvsMsgGroupeDiffusion();
            } else {
                populateViewGroupDiffusion(bean);
            }
            setSelectRecepteur(!listSelectUsers.isEmpty());
            setSelectGroupContact(!listSelectUsers.isEmpty());
        } else {
            if (bean.isSelectActif()) {
                for (DiffusionContact d : bean.getDiffusionContactList()) {
                    if (!listCcDestInterne.contains(d.getUser())) {
                        listCcDestInterne.add(d.getUser());
                        listUsers.get(listUsers.indexOf(d.getUser())).setSelectActif(true);
                    }
                }
            } else {
                for (DiffusionContact d : bean.getDiffusionContactList()) {
                    if (listCcDestInterne.contains(d.getUser())) {
                        listCcDestInterne.remove(d.getUser());
                        listUsers.get(listUsers.indexOf(d.getUser())).setSelectActif(false);
                    }
                }
            }
            if (listCcDestInterne.isEmpty()) {
                if (listCcSelectThemes != null) {
                    listCcSelectThemes.clear();
                }
                resetFicheGroupeDiffusion();
                entityGroupDiffusion = new YvsMsgGroupeDiffusion();
            } else {
                populateViewGroupDiffusion(bean);
            }
            setSelectRecepteur(!listCcDestInterne.isEmpty());
            setSelectGroupContact(!listCcDestInterne.isEmpty());
        }
        update("body_dlg_contacts");
        update("footer_dlg_contacts");
    }

    public void selectOnViewGroupeMessage(int id) {
        listMailFolder.clear();
        String[] champ = new String[]{"id"};
        Object[] val = new Object[]{id};
        YvsMsgGroupeMessage ent = (YvsMsgGroupeMessage) dao.loadOneByNameQueries("YvsMsgGroupeMessage.findByIdAndParent", champ, val);
        GroupeMessage bean = UtilUsers.buildBeanGroupeMessage(ent);
        populateViewGroupMessage(bean);
//        for (YvsMsgDestinataire d : ent.getYvsMsgDestinataireList()) {
//            if (!d.getSupp()) {
//                listMailFolder.add(getFullDestinataire(d));
//            }
//        }
    }

    public void onSelectMenu(boolean menu, boolean contact, boolean dossier) {
        setSelectGroupDossier(dossier);
        setSelectMessagerie(menu);
        setSelectGroupContact(contact);
    }

    public void onSelectDossier(boolean dossier) {
        setSelectDossier(dossier);
        setSelectGroupDossier(!dossier);
    }

    public void onSelectContact(boolean contact) {
        setSelectContact(contact);
        setSelectGroupContact(!contact);
    }

    public void chooseMenuBoolean(MenuMessagerie main) {
        switch (main.getData()) {
            case "Mail":
                if (main.getId() == 1) {
                    onOngletChange(true, false, false, false, false, false, false);
                } else if (main.getId() == 2) {
                    onOngletChange(false, true, false, false, false, false, false);
                } else if (main.getId() == 3) {
                    onOngletChange(false, false, true, false, false, false, false);
                } else if (main.getId() == 4) {
                    onOngletChange(false, false, false, true, false, false, false);
                }
                break;
            case "Inbox":
                if (main.getId() == 1) {
                    onOngletChange(true, false, false, false, false, false, false);
                } else if (main.getId() == 2) {
                    onOngletChange(true, false, false, false, false, false, false);
                }
                break;
            case "Contact":
                onOngletChange(false, false, false, false, true, false, false);
                onSelectContact(true);
                setSelectDossier(false);
                setSelectGroupDossier(false);
                break;
            case "Dossier":
                onOngletChange(false, false, false, false, false, true, false);
                onSelectDossier(true);
                setSelectContact(false);
                setSelectGroupContact(false);
                break;
            case "Groupe Contact":
                onOngletChange(false, false, false, false, true, false, false);
                onSelectContact(false);
                setSelectDossier(false);
                setSelectGroupDossier(false);
                break;
            case "Groupe Dossier":
                onOngletChange(false, false, false, false, false, true, false);
                onSelectDossier(false);
                setSelectContact(false);
                setSelectGroupContact(false);
                break;
            case "Contact Public":
                onClickSendToAll(main.getId());
                setSelectMessagerie(false);
                setSelectGroupContact(false);
                setSelectGroupDossier(false);
                setSelectContact(false);
                setSelectDossier(false);
                break;
            default:
                break;
        }
    }

    public void cloneDestinatairesInMessages() {
        for (Destinataire d : listSelectDestination) {
            Conversation bean = getConversationByDestinataire(d);
            listSelectMessages.add(bean);
        }
    }

    public void viderBox() {
        if (isInCorbeille()) {
            listSelectMessages.addAll(listMailCorb);
            openDialog("dlgConfirmDeleteD");
        } else {
            if (isInInbox()) {
                listSelectDestination.addAll(listMailIndox);
                cloneDestinatairesInMessages();
            }
            if (isInSend()) {
                listSelectMessages.addAll(listMailSend);
            }
            if (isInDraft()) {
                listSelectMessages.addAll(listMailDraft);
            }
            openDialog("dlgConfirmDelete");
        }
    }

    public void clearBoxOrGroupe(MenuMessagerie menu) {
        chooseMenuBoolean(menu);
        if (isSelectGroupContact()) {
            resetFicheGroupeDiffusion();
            openDialog("dlgCreateGroupDiffusion");
        } else if (isSelectMessagerie()) {
            viderBox();
        } else if (isSelectGroupDossier()) {
            resetFicheGroupeMessage();
            openDialog("dlgCreateGroupMessage");
        } else if (isSelectContact()) {
            groupe = new GroupeDiffusion(menu.getId());
            openDialog("dlgConfirmActionG");
        } else if (isSelectDossier()) {
            groupeM = new GroupeMessage(menu.getId());
            groupeM.setReference(menu.getLibelle());
            openDialog("dlgConfirmActionF");
        }
    }

    public void deleteBeanGroupeDiffusion() {
        try {
            dao.delete(new YvsMsgGroupeDiffusion(groupe.getId()));
            listGroupDiffusion.remove(groupe);
//            currentUser.getUsers().getYvsGroupeDiffusionList().remove(buildGroupeDiffusion(groupe));
            List<TreeNode> list = new ArrayList<>();
            list.addAll(contact.getChildren());
            for (TreeNode t : list) {
                MenuMessagerie m = (MenuMessagerie) t.getData();
                if (m.getId() == groupe.getId()) {
                    contact.getChildren().remove(t);
                }
            }
            entityGroupDiffusion = new YvsMsgGroupeDiffusion();
            onOngletChange(true, false, false, false, false, false, false);
            update("panel_messagerie_01");
            update("panel_messagerie_00");
            succes();
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void deleteBeanGroupeMessage() {
        try {
            dao.delete(new YvsMsgGroupeMessage(groupeM.getId()));
            listParentGroupMessage.remove(groupeM);
            listGroupMessage.remove(groupeM);
            List<TreeNode> list = new ArrayList<>();
            TreeNode tree = searchTreeNode(dossier, groupeM.getReference()).getParent();
            list.addAll(tree.getChildren());
            for (TreeNode t : list) {
                MenuMessagerie m = (MenuMessagerie) t.getData();
                if (m.getId() == groupeM.getId()) {
                    tree.getChildren().remove(t);
                }
            }
            onOngletChange(true, false, false, false, false, false, false);
            update("panel_messagerie_01");
            update("panel_messagerie_00");
            succes();
        } catch (Exception ex) {
            getErrorMessage("Suppression Impossible !");
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void resetPageFolder(boolean etat) {
        for (Destinataire c : listMailFolder) {
            listMailFolder.get(listMailFolder.indexOf(c)).setSelectActif(etat);
            if (etat) {
                if (!listSelectDestination.contains(c)) {
                    listSelectDestination.add(c);
                }
            } else {
                if (listSelectDestination.contains(c)) {
                    listSelectDestination.remove(c);
                }
            }
        }
    }

    public void resetPageInbox(boolean etat) {
        for (Destinataire c : listMailIndox) {
            listMailIndox.get(listMailIndox.indexOf(c)).setSelectActif(etat);
            if (etat) {
                if (!listSelectDestination.contains(c)) {
                    listSelectDestination.add(c);
                }
            } else {
                if (listSelectDestination.contains(c)) {
                    listSelectDestination.remove(c);
                }
            }
        }
    }

    public void resetPageSend(boolean etat) {
        for (Conversation c : listMailSend) {
            listMailSend.get(listMailSend.indexOf(c)).setSelectActif(etat);
            if (etat) {
                if (!listSelectMessages.contains(c)) {
                    listSelectMessages.add(c);
                }
            } else {
                if (listSelectMessages.contains(c)) {
                    listSelectMessages.remove(c);
                }
            }
        }
    }

    public void resetPageDraft(boolean etat) {
        for (Conversation c : listMailDraft) {
            listMailDraft.get(listMailDraft.indexOf(c)).setSelectActif(etat);
            if (etat) {
                if (!listSelectMessages.contains(c)) {
                    listSelectMessages.add(c);
                }
            } else {
                if (listSelectMessages.contains(c)) {
                    listSelectMessages.remove(c);
                }
            }
        }
    }

    public void resetPageCorb(boolean etat) {
        for (int i = 0; i < listMailCorb.size(); i++) {
            Conversation c = listMailCorb.get(i);
            listMailCorb.get(i).setSelectActif(etat);
            if (c.isConversation()) {
                if (etat) {
                    if (!listSelectMessages.contains(c)) {
                        listSelectMessages.add(c);
                    }
                } else {
                    if (listSelectMessages.contains(c)) {
                        listSelectMessages.remove(c);
                    }
                }
            } else {
                if ((c.getListDestinataire() != null) ? !c.getListDestinataire().isEmpty() : false) {
                    Destinataire d = c.getListDestinataire().get(0);
                    d.setMessage(c);
                    if (etat) {
                        if (!listSelectDestination.contains(d)) {
                            listSelectDestination.add(d);
                        }
                    } else {
                        if (listSelectDestination.contains(d)) {
                            listSelectDestination.remove(d);
                        }
                    }
                }
            }
        }
    }

    public void resetPageSearch(boolean etat) {
        for (int i = 0; i < listMailSearch.size(); i++) {
            Conversation c = listMailSearch.get(i);
            listMailSearch.get(i).setSelectActif(etat);
            if (c.isConversation()) {
                if (etat) {
                    if (!listSelectMessages.contains(c)) {
                        listSelectMessages.add(c);
                    }
                } else {
                    if (listSelectMessages.contains(c)) {
                        listSelectMessages.remove(c);
                    }
                }
            } else {
                if ((c.getListDestinataire() != null) ? !c.getListDestinataire().isEmpty() : false) {
                    Destinataire d = c.getListDestinataire().get(0);
                    d.setMessage(c);
                    if (etat) {
                        if (!listSelectDestination.contains(d)) {
                            listSelectDestination.add(d);
                        }
                    } else {
                        if (listSelectDestination.contains(d)) {
                            listSelectDestination.remove(d);
                        }
                    }
                }
            }
        }
    }

    public void changeEtatSearch() {
        resetPageSearch(selectAllSearch);
        setSelectMessage(listSelectMessages.size() + listSelectDestination.size() > 0);
        setOptionUpdate(false);
        setOptionTransfert(listSelectMessages.size() + listSelectDestination.size() == 1);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
        update("data_messageSearch");
    }

    public void changeEtatCorb() {
        resetPageCorb(selectAllCorb);
        setSelectMessage(listSelectMessages.size() + listSelectDestination.size() > 0);
        setOptionUpdate(false);
        setOptionTransfert(listSelectMessages.size() + listSelectDestination.size() == 1);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
        update("data_messageSupprimer");
    }

    public void changeEtatDraft() {
        resetPageDraft(selectAllDraft);
        setSelectMessage(!listSelectMessages.isEmpty());
        setOptionUpdate(listSelectMessages.size() == 1);
        setUpdateMessage(isSelectMessage());
        setOptionTransfert(false);
        update("head_messagerie");
        update("data_messageBrouillon");
    }

    public void changeEtatSend() {
        resetPageSend(selectAllSend);
        setSelectMessage(!listSelectMessages.isEmpty());
        setOptionUpdate(false);
        setOptionTransfert(listSelectMessages.size() == 1);
        setUpdateMessage(isSelectMessage());
        update("head_messagerie");
        update("data_messageSend");
    }

    public void changeEtatInbox() {
        resetPageInbox(selectAllInbox);
        setSelectMessage(!listSelectDestination.isEmpty());
        setOptionUpdate(false);
        setOptionTransfert(listSelectDestination.size() == 1);
        setUpdateMessage(isSelectMessage());
        destination.setAccuse(isSelectMessage() ? listSelectDestination.get(listSelectDestination.size() - 1).isAccuse() : false);
        update("head_messagerie");
        update("data_messageRecu");
    }

    public void changeEtatFolder() {
        resetPageFolder(selectAllFolder);
        setSelectMessage(!listSelectDestination.isEmpty());
        setOptionUpdate(false);
        setOptionTransfert(listSelectDestination.size() == 1);
        setUpdateMessage(isSelectMessage());
        destination.setAccuse(isSelectMessage() ? listSelectDestination.get(listSelectDestination.size() - 1).isAccuse() : false);
        update("head_messagerie");
        update("data_dossier");
    }

    public void deleteBeanContact() {
        if (!listSelectDiffusionContact.isEmpty()) {
            for (DiffusionContact c : listSelectDiffusionContact) {
                dao.delete(new YvsMsgDiffusionContact(c.getId()));
                groupe.getDiffusionContactList().remove(c);
            }
            listSelectDiffusionContact.clear();
            setSelectContact(false);
            succes();
            update("data_contact");
        }
    }

    public String[] buildListDestinataire(String text) {
        String[] list = null;
        if ((text != null) ? text.trim() != null && !"".equals(text.trim()) : false) {
            list = text.replace(" ", "").split(";");
        }
        return list;
    }

    public void onConstructAutoUser() {
        List<Theme> list = new ArrayList<>();
        int index = 0;
        for (Users u : listUsers) {
            Theme t = new Theme((int) u.getId(), u.getNomUsers(), u.getNomUsers());
            list.add(t);
            index += 1;
        }
        themeService.setThemes(list);
    }

    public List<Theme> completeTheme(String query) {
        allThemes = themeService.getThemes();
        List<Theme> filteredThemes = new ArrayList<>();
        for (Theme t : allThemes) {
            if (t.getName().toLowerCase().startsWith(query.toLowerCase())) {
                Users u = new Users(t.getId());
                u.setNomUsers(t.getName());
                if (!listSelectUsers.contains(u)) {
                    filteredThemes.add(t);
                }
            }
        }
        return filteredThemes;
    }

    public void addUsersInListSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Theme theme = (Theme) ev.getObject();
            if ((theme != null) ? theme.getId() > -1 : false) {
                Users u = new Users(theme.getId());
                u.setNomUsers(theme.getName());
                listSelectUsers.add(u);
                if (listUsers.contains(u)) {
                    listUsers.get(listUsers.indexOf(u)).setSelectActif(true);
                }
            }
        }
    }

    public void delUsersInListSelect(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Theme theme = (Theme) ev.getObject();
            if ((theme != null) ? theme.getId() > -1 : false) {
                Users u = new Users(theme.getId());
                u.setNomUsers(theme.getName());
                listSelectUsers.remove(u);
                if (listUsers.contains(u)) {
                    listUsers.get(listUsers.indexOf(u)).setSelectActif(false);
                }
            }
        }
    }

    public void addCcUsersInListSelect(SelectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Theme theme = (Theme) ev.getObject();
            if ((theme != null) ? theme.getId() > -1 : false) {
                Users u = new Users(theme.getId());
                u.setNomUsers(theme.getName());
                listCcDestInterne.add(u);
            }
        }
    }

    public void delCcUsersInListSelect(UnselectEvent ev) {
        if ((ev != null) ? ev.getObject() != null : false) {
            Theme theme = (Theme) ev.getObject();
            if ((theme != null) ? theme.getId() > -1 : false) {
                Users u = new Users(theme.getId());
                u.setNomUsers(theme.getName());
                listCcDestInterne.remove(u);
            }
        }
    }

    public void openDlgContact(boolean etat) {
        setClickCcInter(etat);
        if (!isRepondre()) {
            listSelectUsers.clear();
            listCcDestInterne.clear();
            for (Users u : listUsers) {
                listUsers.get(listUsers.indexOf(u)).setSelectActif(false);
            }
            for (GroupeDiffusion u : listGroupDiffusion) {
                listGroupDiffusion.get(listGroupDiffusion.indexOf(u)).setSelectActif(false);
            }
        }
        openDialog("dlgContacts");
        update("body_dlg_contacts");
    }

    public void openDlgPieceJointe() {
        if (controleFiche_(conversation)) {
            file0 = new DefaultUploadedFile();
            file1 = new DefaultUploadedFile();
            file2 = new DefaultUploadedFile();
            file3 = new DefaultUploadedFile();
            file4 = new DefaultUploadedFile();
            openDialog("addPieceJointe");
            update("body_dlg_piece_jointe");
        }
    }

    public void openDlgCarnetAdresse(boolean copie) {
        setAdressExtCC(copie);
        openDialog("dlgCarnetAdresse");
        resetPageCarnetAdresse();
        update("data_carnet_adresse_00");
    }

    public void onCreatePieceJointe() throws MessagingException, IOException {
        conversation.setEmetteur(UtilUsers.buildBeanUsers(currentUser.getUsers()));
        String chemin = Initialisation.getCheminMailUser(conversation);
        String repDest = Initialisation.getCheminResource() + "" + chemin.substring(Initialisation.getCheminAllDoc().length(), chemin.length());
        if ((file0 != null) ? (file0.getFileName() != null && !"".equals(file0.getFileName())) ? file0.getInputstream() != null : false : false) {
            Util.copySVGFile(file0.getFileName(), chemin + "" + Initialisation.FILE_SEPARATOR, file0.getInputstream());
            Util.copyFileS(file0.getFileName(), repDest + "" + Initialisation.FILE_SEPARATOR, file0.getInputstream());
            if (!"".equals(conversation.getPieceJointe()) && conversation.getPieceJointe() != null) {
                conversation.setPieceJointe(conversation.getPieceJointe() + ";" + repDest + Initialisation.FILE_SEPARATOR + file0.getFileName());
            } else {
                conversation.setPieceJointe(repDest + Initialisation.FILE_SEPARATOR + file0.getFileName());
            }
            nbrPieceJointe += 1;
        }
        if ((file1 != null) ? (file1.getFileName() != null && !"".equals(file1.getFileName())) ? file1.getInputstream() != null : false : false) {
            Util.copySVGFile(file1.getFileName(), chemin + "" + Initialisation.FILE_SEPARATOR, file1.getInputstream());
            Util.copyFileS(file1.getFileName(), repDest + "" + Initialisation.FILE_SEPARATOR, file1.getInputstream());
            if (!"".equals(conversation.getPieceJointe()) && conversation.getPieceJointe() != null) {
                conversation.setPieceJointe(conversation.getPieceJointe() + ";" + repDest + Initialisation.FILE_SEPARATOR + file1.getFileName());
            } else {
                conversation.setPieceJointe(repDest + Initialisation.FILE_SEPARATOR + file1.getFileName());
            }
            nbrPieceJointe += 1;
        }
        if ((file2 != null) ? (file2.getFileName() != null && !"".equals(file2.getFileName())) ? file2.getInputstream() != null : false : false) {
            Util.copySVGFile(file2.getFileName(), chemin + "" + Initialisation.FILE_SEPARATOR, file2.getInputstream());
            Util.copyFileS(file2.getFileName(), repDest + "" + Initialisation.FILE_SEPARATOR, file2.getInputstream());
            if (!"".equals(conversation.getPieceJointe()) && conversation.getPieceJointe() != null) {
                conversation.setPieceJointe(conversation.getPieceJointe() + ";" + repDest + Initialisation.FILE_SEPARATOR + file2.getFileName());
            } else {
                conversation.setPieceJointe(repDest + Initialisation.FILE_SEPARATOR + file2.getFileName());
            }
            nbrPieceJointe += 1;
        }
        if ((file3 != null) ? (file3.getFileName() != null && !"".equals(file3.getFileName())) ? file3.getInputstream() != null : false : false) {
            Util.copySVGFile(file3.getFileName(), chemin + "" + Initialisation.FILE_SEPARATOR, file3.getInputstream());
            Util.copyFileS(file3.getFileName(), repDest + "" + Initialisation.FILE_SEPARATOR, file3.getInputstream());
            if (!"".equals(conversation.getPieceJointe()) && conversation.getPieceJointe() != null) {
                conversation.setPieceJointe(conversation.getPieceJointe() + ";" + repDest + Initialisation.FILE_SEPARATOR + file3.getFileName());
            } else {
                conversation.setPieceJointe(repDest + Initialisation.FILE_SEPARATOR + file3.getFileName());
            }
            nbrPieceJointe += 1;
        }
        if ((file4 != null) ? (file4.getFileName() != null && !"".equals(file4.getFileName())) ? file4.getInputstream() != null : false : false) {
            Util.copySVGFile(file4.getFileName(), chemin + "" + Initialisation.FILE_SEPARATOR, file4.getInputstream());
            Util.copyFileS(file3.getFileName(), repDest + "" + Initialisation.FILE_SEPARATOR, file3.getInputstream());
            if (!"".equals(conversation.getPieceJointe()) && conversation.getPieceJointe() != null) {
                conversation.setPieceJointe(conversation.getPieceJointe() + ";" + repDest + Initialisation.FILE_SEPARATOR + file4.getFileName());
            } else {
                conversation.setPieceJointe(repDest + Initialisation.FILE_SEPARATOR + file4.getFileName());
            }
            nbrPieceJointe += 1;
        }
        update("lab_piece_jointe_mail");
    }

    public void onErasePieceJointe() {
        nbrPieceJointe = 0;
        conversation.setPieceJointe(null);
        update("lab_piece_jointe_mail");
    }

    public void downloadAllPieceJointeS(Destinataire bean) {
        downloadAllPieceJointe(bean.getMessage());
    }

    public void downloadAllPieceJointe(Conversation bean) {
        String[] tab_piece_jointe = Util.buildStringToList(bean.getPieceJointe(), ";");
        if ((tab_piece_jointe.length > 1)) {
            String chemin = Initialisation.getCheminMailUser(bean) + Initialisation.FILE_SEPARATOR;
            List<File> files = new ArrayList<>();
            for (String piece_jointe : tab_piece_jointe) {
                files.add(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath(piece_jointe)));
            }
            Compress.addFile(chemin + bean.getReference().replace(" ", "_") + ".zip", files);
            downloadPieceJointe(chemin + bean.getReference().replace(" ", "_") + ".zip");
        } else {
            downloadPieceJointe(tab_piece_jointe[0]);
        }
    }

    public void downloadPieceJointe(String bean) {
        Util.getDownloadFile(bean, "piece_jointe");
    }

    public void chooseParentGroupMessage_(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                parentGroupMessage = listParentGroupMessage.get(listParentGroupMessage.indexOf(new GroupeMessage(id)));
            } else if (id == -1) {
                openDialog("dlgParentGroupeMessage");
            } else if (id == -2) {
                parentGroupMessage = new GroupeMessage();
            }
        }
    }

    public void chooseParentGroupMessage(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            int id = (int) ev.getNewValue();
            if (id > 0) {
                parentGroupMessage = listParentGroupMessage.get(listParentGroupMessage.indexOf(new GroupeMessage(id)));
                openDialog("dlgConfirmDeplacer");
            } else if (id == -1) {
                openDialog("dlgParentGroupeMessage");
            } else if (id == -2) {
                parentGroupMessage = new GroupeMessage();
                openDialog("dlgConfirmDeplacer");
            }
        }
    }

    public void deplacerMessage() {
        loadOnViewParentGroupeMessage(new SelectEvent(new SelectOneMenu(), new BehaviorBase(), parentGroupMessage));
    }

    public void transferMessage() {
        setUpdateMessage(false);
        setViewListe(false);
        setOnLecture(false);
        setSelectMessage(false);
        setOptionUpdate(false);
        update("head_messagerie");
        update("body_messagerie");
    }

    public void marquerMessage() {
        if ((listSelectDestination != null) ? !listSelectDestination.isEmpty() : false) {
            for (Destinataire c : listSelectDestination) {
                c.setAccuse(!c.isAccuse());
                String rq = "UPDATE yvs_msg_destinataire SET accuse = " + c.isAccuse() + " WHERE id=?";
                Options[] param = new Options[]{new Options(c.getId(), 1)};
                dao.requeteLibre(rq, param);
                listMailIndox.get(listMailIndox.indexOf(c)).setAccuse(c.isAccuse());
                if (isInSousInbox()) {
                    listMailIndox.remove(c);
                }
            }
            destination.setAccuse(!destination.isAccuse());
            update("panel_messagerie_01");
        }
        update("head_messagerie");
    }

    public List<String[]> buildListPieceJointe(String piecesJointes) {
        List<String[]> list = new ArrayList<>();
        if ((piecesJointes != null) ? !"".equals(piecesJointes) : false) {
            String[] l = piecesJointes.split(";");
            for (String s : l) {
                File f = new File(s);
                String[] l1 = new String[]{f.getName(), s};
                list.add(l1);
            }
        }
        return list;
    }

    public void openDlgAddMembreDiffusion() {
        setSelectMembreDiffusion(false);
        openDialog("dlgContacts_01");
        update("body_dlg_contacts_00");
        update("footer_dlg_contacts_00");
    }

    public void showCcInterne() {
        listCcDestInterne.clear();
        listCcSelectThemes = new ArrayList<>();
        setViewCcInterne(!isViewCcInterne());
        update("panel_write_message");
    }

    public void showCcExterne() {
        ccDestExterne = null;
        setViewCcExterne(!isViewCcExterne());
        update("panel_write_message");
    }

    public void onCloseDialogGroupeMessage() {
        loadAllParentGroupMessage();
        resetFicheGroupeMessage();
    }

    public void buildListParentGroupeMessage(YvsMsgGroupeMessage entity) {
        if ((entity != null) ? entity.getId() != 0 : false) {
            GroupeMessage bean = UtilUsers.buildBeanGroupeMessage(entity);
            if (listParentGroupMessage.contains(bean)) {
                listParentGroupMessage.remove(bean);
            }
//            if ((entity.getYvsGroupeMessageList() != null) ? !entity.getYvsGroupeMessageList().isEmpty() : false) {
//                for (YvsMsgGroupeMessage g : entity.getYvsGroupeMessageList()) {
//                    buildListParentGroupeMessage(g);
//                }
//            }
        }
    }

    public void deleteDestinataire(boolean inbox, boolean folder) {
        if (inbox) {
            listMailIndox.remove(destinataireSelect);
            destinataireSelect.setSupp(true);
        }
        if (folder) {
            listMailFolder.remove(destinataireSelect);
            destinataireSelect.setSupp(true);
        }
        entityMessage = buildConversation(destinataireSelect.getMessage());
        dao.update(buildDestinataire(destinataireSelect));
        destinataireSelect = null;
        entityMessage = new YvsMsgConversation();
        succes();
        update("panel_messagerie_01");
    }

    public void deleteConversation(boolean send, boolean draft, boolean corb, boolean search) {
        if (draft) {
            listMailDraft.remove(conversationSelect);
            conversationSelect.setSupp(true);
            dao.update(buildConversation(conversationSelect));
        }
        if (send) {
            listMailSend.remove(conversationSelect);
            conversationSelect.setSupp(true);
            dao.update(buildConversation(conversationSelect));
        }
        if (corb) {
            listMailCorb.remove(conversationSelect);
            if (conversationSelect.isConversation()) {
                conversationSelect.setDelete(true);
                dao.update(buildConversation(conversationSelect));
            } else {
                Destinataire bean = conversationSelect.getListDestinataire().get(0);
                entityMessage = buildConversation(conversationSelect);
                bean.setDelete(true);
                dao.update(buildDestinataire(bean));
            }
        }
        if (search) {
            listMailSearch.remove(conversationSelect);
            if (conversationSelect.isConversation()) {
                conversationSelect.setDelete(true);
                dao.update(buildConversation(conversationSelect));
            } else {
                Destinataire bean = conversationSelect.getListDestinataire().get(0);
                entityMessage = buildConversation(conversationSelect);
                bean.setDelete(true);
                dao.update(buildDestinataire(bean));
            }
        }
        conversationSelect = null;
        entityMessage = new YvsMsgConversation();
        succes();
        update("panel_messagerie_01");
    }

    public void restaureOne() {
        listMailSearch.remove(conversationSelect);
        if (conversationSelect.isConversation()) {
            conversationSelect.setSupp(false);
            dao.update(buildConversation(conversationSelect));
        } else {
            Destinataire bean = conversationSelect.getListDestinataire().get(0);
            entityMessage = buildConversation(conversationSelect);
            bean.setSupp(false);
            dao.update(buildDestinataire(bean));
        }
        conversationSelect = null;
        entityMessage = new YvsMsgConversation();
        succes();
        update("panel_messagerie_01");
    }

    public void marquerDestinataire() {
        destinataireSelect.setAccuse(!destinataireSelect.isAccuse());
        entityMessage = buildConversation(destinataireSelect.getMessage());
        dao.update(buildDestinataire(destinataireSelect));
        destinataireSelect = null;
        entityMessage = new YvsMsgConversation();
        succes();
        update("panel_messagerie_01");
    }

    public void viewDetailMessage() {
        listDestinationMesssage.clear();
        listDestinationMesssage.addAll(conversationSelect.getListDestinataire());
        conversationSelect = null;
        openDialog("viewDetailMessage");
        update("data_messageDest");
    }

    public void onClickSendToAll(int id) {
        setViewListe(false);
        resetFiche();
        GroupeDiffusion bean = UtilUsers.buildBeanGroupeDiffusion((YvsMsgGroupeDiffusion) dao.loadOneByNameQueries("YvsMsgGroupeDiffusion.findById", new String[]{"id"}, new Object[]{id}));
        for (DiffusionContact c : bean.getDiffusionContactList()) {
            if (listSelectThemes == null) {
                listSelectThemes = new ArrayList<>();
            }
            Theme t = new Theme((int) c.getUser().getId(), c.getUser().getNomUsers(), c.getUser().getNomUsers());
            if (!listSelectThemes.contains(t)) {
                listSelectThemes.add(t);
            }
            if (!listSelectUsers.contains(c.getUser())) {
                listSelectUsers.add(c.getUser());
            }
        }
        update("head_messagerie");
        update("body_messagerie");
    }

    public void onKeyUpDestinataireExterne(boolean copie) {
        if (copie) {
            if ((ccDestExterne != null) ? ccDestExterne.length() > 0 : false) {
                if (ccDestExterne.charAt(ccDestExterne.length() - 1) == ';') {
                    String[] list = buildListDestinataire(ccDestExterne);
                    String email = list[list.length - 1];
                    if (!Util.onEmail(email)) {
                        ccDestExterne = null;
                        for (int i = 0; i < list.length; i++) {
                            if (!email.equals(list[i])) {
                                if (ccDestExterne == null || "".equals(ccDestExterne)) {
                                    ccDestExterne = list[i];
                                } else {
                                    ccDestExterne = ccDestExterne + "; " + list[i];
                                }
                            }
                        }
                        update("txt_list_cc_destinataire_ext");
                    }
                }
            }
        } else {
            if ((destinatairesExtern != null) ? destinatairesExtern.length() > 0 : false) {
                if (destinatairesExtern.charAt(destinatairesExtern.length() - 1) == ';') {
                    String[] list = buildListDestinataire(destinatairesExtern);
                    String email = list[list.length - 1];
                    if (!Util.onEmail(email)) {
                        destinatairesExtern = null;
                        for (int i = 0; i < list.length; i++) {
                            if (!email.equals(list[i])) {
                                if (destinatairesExtern == null || "".equals(destinatairesExtern)) {
                                    destinatairesExtern = list[i];
                                } else {
                                    destinatairesExtern = destinatairesExtern + "; " + list[i];
                                }
                            }
                        }
                        update("txt_list_destinataire_ext");
                    }
                }
            }
        }
    }

    public void onBlurDestinataireExterne(boolean copie) {
        if (copie) {
            if ((ccDestExterne != null) ? ccDestExterne.length() > 0 : false) {
                String[] list = buildListDestinataire(ccDestExterne);
                String email = list[list.length - 1];
                if (!Util.onEmail(email)) {
                    ccDestExterne = null;
                    for (int i = 0; i < list.length; i++) {
                        if (!email.equals(list[i])) {
                            if (ccDestExterne == null || "".equals(ccDestExterne)) {
                                ccDestExterne = list[i];
                            } else {
                                ccDestExterne = ccDestExterne + "; " + list[i];
                            }
                        }
                    }
                    update("txt_list_cc_destinataire_ext");
                }
            }
        } else {
            if ((destinatairesExtern != null) ? destinatairesExtern.length() > 0 : false) {
                String[] list = buildListDestinataire(destinatairesExtern);
                String email = list[list.length - 1];
                if (!Util.onEmail(email)) {
                    destinatairesExtern = null;
                    for (int i = 0; i < list.length; i++) {
                        if (!email.equals(list[i])) {
                            if (destinatairesExtern == null || "".equals(destinatairesExtern)) {
                                destinatairesExtern = list[i];
                            } else {
                                destinatairesExtern = destinatairesExtern + "; " + list[i];
                            }
                        }
                    }
                    update("txt_list_destinataire_ext");
                }
            }
        }
    }

    public void deleteCarnet() {
        if (!carnetAdressesSelect.isEmpty()) {
            for (CarnetAdresse c : carnetAdressesSelect) {
                dao.delete(new YvsMsgCarnetAdresse(c.getId()));
                carnetAdresses.remove(c);
//                currentUser.getUsers().getYvsMsgCarnetAdresseList().remove(new YvsMsgCarnetAdresse(c.getId()));
            }
            succes();
            update("data_carnet_adresse_00");
        }
    }
    String[] champ;
    Object[] val;
    String query;
    boolean ismsg;

    public void onSearchMail() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        switch (eltSearch) {
            case "inbox":
                getQueryOfSearch(false);
                if (query != null && champ != null && val != null) {
                    if (!ismsg) {
                        listMailIndox.clear();
                        listMailIndox = getFullListDestinataire(dao.loadNameQueries(query, champ, val));
                        onOngletChange(true, false, false, false, false, false, false);
                    } else {
                        getErrorMessage("Les paramètres entrés sont incorrects !");
                    }
                }
                break;
            case "send":
                getQueryOfSearch(false);
                if (query != null && champ != null && val != null) {
                    listMailSend.clear();
                    if (ismsg) {
                        listMailSend = UtilUsers.buildBeanListConversation(dao.loadNameQueries(query, champ, val));
                    } else {
                        List<Destinataire> list = getFullListDestinataire(dao.loadNameQueries(query, champ, val));
                        for (Destinataire d : list) {
                            listMailSend.add(getConversationByDestinataire(d));
                        }
                    }
                    onOngletChange(false, true, false, false, false, false, false);
                }
                break;
            case "draft":
                getQueryOfSearch(false);
                if (query != null && champ != null && val != null) {
                    if (ismsg) {
                        listMailDraft.clear();
                        listMailDraft = UtilUsers.buildBeanListConversation(dao.loadNameQueries(query, champ, val));
                        onOngletChange(false, false, true, false, false, false, false);
                    } else {
                        getErrorMessage("Les paramètres entrés sont incorrects !");
                    }
                }
                break;
            case "corb":
                getQueryOfSearch(false);
                if (query != null && champ != null && val != null) {
                    listMailCorb.clear();
                    if (ismsg) {
                        listMailCorb = UtilUsers.buildBeanListConversation(dao.loadNameQueries(query, champ, val));
                    } else {
                        List<Destinataire> list = getFullListDestinataire(dao.loadNameQueries(query, champ, val));
                        for (Destinataire d : list) {
                            listMailCorb.add(getConversationByDestinataire(d));
                        }
                    }
                    onOngletChange(false, false, false, true, false, false, false);
                }
                break;
            case "all":
                getQueryOfSearch(true);
                if (query != null && champ != null && val != null) {
                    listMailSearch.clear();
                    if (ismsg) {
                        listMailSearch = UtilUsers.buildBeanListConversation(dao.loadNameQueries(query, champ, val));
                    } else {
                        List<Destinataire> list = getFullListDestinataire(dao.loadNameQueries(query, champ, val));
                        for (Destinataire d : list) {
                            listMailSearch.add(getConversationByDestinataire(d));
                        }
                    }
                    onOngletChange(false, false, false, false, false, false, true);
                }
                break;
        }
        query = null;
        champ = null;
        val = null;
        ismsg = false;
        update("head_messagerie");
        update("panel_messagerie_01");
    }

    private void getQueryOfSearch(boolean all) {
        if (dateEnvoiSearch == null
                && (emetteurSearch == null || "".equals(emetteurSearch))
                && (destinataireSearch == null || "".equals(destinataireSearch))
                && (objetSearch == null || "".equals(objetSearch))
                && (contenuSearch == null || "".equals(contenuSearch))) {
            query = null;
            champ = null;
            val = null;
            ismsg = false;
            return;
        }
        if (!all) {
            if (!"inbox".equals(eltSearch)) {
                if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurDateEnvoi";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurObjet";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, objetSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurContenu";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurObjetDateEnvoi";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "objet", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, objetSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurContenuDateEnvoi";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "contenu", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, "%" + contenuSearch + "%", dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurObjetContenu";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "objet", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, objetSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgConversation.findByEmetteurObjetContenuDateEnvoi";
                    ismsg = true;
                    champ = new String[]{"supp", "envoyer", "emetteur", "objet", "dateEnvoi", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), "send".equals(eltSearch), currentUser, objetSearch, dateEnvoiSearch, "%" + contenuSearch + "%"};
                }
            } else if (!"draft".equals(eltSearch)) {
                if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireObjet";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, objetSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireObjetDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "dateEnvoi", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, dateEnvoiSearch, objetSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "dateEnvoi", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, dateEnvoiSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireObjetContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "objet", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, objetSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireObjetContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "dateEnvoi", "objet", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, dateEnvoiSearch, objetSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteur";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersEmetteurSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurObjet";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, objetSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurObjetDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "objet", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, objetSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "contenu", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurObjetContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "contenu", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch != null && !"".equals(emetteurSearch))
                        && (destinataireSearch == null || "".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByDestinataireEmetteurObjetContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "destinataire", "emetteur", "contenu", "objet", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataire";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireObjet";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, objetSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "contenu"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%"};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch == null || "".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireObjetDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "objet", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, objetSearch, dateEnvoiSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch == null || "".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "contenu", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", dateEnvoiSearch};
                } else if (dateEnvoiSearch == null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireObjetContenu";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "contenu", "objet"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch};
                } else if (dateEnvoiSearch != null
                        && (emetteurSearch == null || "".equals(emetteurSearch))
                        && (destinataireSearch != null && !"".equals(destinataireSearch))
                        && (objetSearch != null && !"".equals(objetSearch))
                        && (contenuSearch != null && !"".equals(contenuSearch))) {
                    query = "YvsMsgDestinataire.findByEmetteurDestinataireObjetContenuDateEnvoi";
                    ismsg = false;
                    champ = new String[]{"supp", "emetteur", "destinataire", "contenu", "objet", "dateEnvoi"};
                    val = new Object[]{"corb".equals(eltSearch), currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch, dateEnvoiSearch};
                }
            }
        } else {
            if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurDateEnvoi";
                ismsg = true;
                champ = new String[]{"emetteur", "dateEnvoi"};
                val = new Object[]{currentUser, dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null || !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurObjet";
                ismsg = true;
                champ = new String[]{"emetteur", "objet"};
                val = new Object[]{currentUser, objetSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurContenu";
                ismsg = true;
                champ = new String[]{"emetteur", "contenu"};
                val = new Object[]{currentUser, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurObjetDateEnvoi";
                ismsg = true;
                champ = new String[]{"emetteur", "objet", "dateEnvoi"};
                val = new Object[]{currentUser, objetSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurContenuDateEnvoi";
                ismsg = true;
                champ = new String[]{"emetteur", "contenu", "dateEnvoi"};
                val = new Object[]{currentUser, "%" + contenuSearch + "%", dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurObjetContenu";
                ismsg = true;
                champ = new String[]{"emetteur", "objet", "contenu"};
                val = new Object[]{currentUser, objetSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgConversation.findByAllEmetteurObjetContenuDateEnvoi";
                ismsg = true;
                champ = new String[]{"emetteur", "objet", "dateEnvoi", "contenu"};
                val = new Object[]{currentUser, objetSearch, dateEnvoiSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "dateEnvoi"};
                val = new Object[]{currentUser, dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireObjet";
                ismsg = false;
                champ = new String[]{"destinataire", "objet"};
                val = new Object[]{currentUser, objetSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireContenu";
                ismsg = false;
                champ = new String[]{"destinataire", "contenu"};
                val = new Object[]{currentUser, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireObjetDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "dateEnvoi", "objet"};
                val = new Object[]{currentUser, dateEnvoiSearch, objetSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "dateEnvoi", "contenu"};
                val = new Object[]{currentUser, dateEnvoiSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireObjetContenu";
                ismsg = false;
                champ = new String[]{"destinataire", "objet", "contenu"};
                val = new Object[]{currentUser, objetSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireObjetContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "dateEnvoi", "objet", "contenu"};
                val = new Object[]{currentUser, dateEnvoiSearch, objetSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteur";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur"};
                val = new Object[]{currentUser, usersEmetteurSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjet";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "objet"};
                val = new Object[]{currentUser, usersDestinataireSearch, objetSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurContenu";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "contenu"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "objet", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, objetSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "contenu", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetContenu";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "contenu", "objet"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch != null && !"".equals(emetteurSearch))
                    && (destinataireSearch == null || "".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllDestinataireEmetteurObjetContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"destinataire", "emetteur", "contenu", "objet", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataire";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire"};
                val = new Object[]{currentUser, usersDestinataireSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireDateEnvoi";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjet";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "objet"};
                val = new Object[]{currentUser, usersDestinataireSearch, objetSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireContenu";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "contenu"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%"};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch == null || "".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetDateEnvoi";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "objet", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, objetSearch, dateEnvoiSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch == null || "".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "contenu", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", dateEnvoiSearch};
            } else if (dateEnvoiSearch == null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetContenu";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "contenu", "objet"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch};
            } else if (dateEnvoiSearch != null
                    && (emetteurSearch == null || "".equals(emetteurSearch))
                    && (destinataireSearch != null && !"".equals(destinataireSearch))
                    && (objetSearch != null && !"".equals(objetSearch))
                    && (contenuSearch != null && !"".equals(contenuSearch))) {
                query = "YvsMsgDestinataire.findByAllEmetteurDestinataireObjetContenuDateEnvoi";
                ismsg = false;
                champ = new String[]{"emetteur", "destinataire", "contenu", "objet", "dateEnvoi"};
                val = new Object[]{currentUser, usersDestinataireSearch, "%" + contenuSearch + "%", objetSearch, dateEnvoiSearch};
            }
        }
    }

    public void onCreateFilter() {
        FiltreMail bean = recopieViewFiltreMail();
        if (!"".equals(bean.getValeur())) {
            dao.save1(buildFiltreMail(bean));
            resetFiche(filtreMail);
            filtreMail.setGroupeMessage(new GroupeMessage());
            succes();
            listSelectDestination.clear();
        }
    }

    public void chooseBaseFilterMail(ValueChangeEvent ev) {
        if ((ev != null) ? ev.getNewValue() != null : false) {
            String value = (String) ev.getNewValue();
            switch (value) {
                case "E":
                    filtreMail.setValeur(conversation.getEmetteur().getNomUsers());
                    break;
                case "O":
                    filtreMail.setValeur(conversation.getObjet());
                    break;
                case "P":
                    filtreMail.setValeur(conversation.getPriorite());
                    break;
                default:
                    break;
            }
        }
    }

    public void onSearchUserEmetteur() {
        if ((emetteurSearch != null) ? emetteurSearch.length() > 0 : false) {
            champ = new String[]{"nomUsers"};
            val = new Object[]{emetteurSearch};
            usersEmetteurSearch = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByNom", champ, val);
            if ((usersEmetteurSearch != null) ? usersEmetteurSearch.getId() == 0 : true) {
                emetteurSearch = "";
                getErrorMessage("Emetteur inconnu !");
            } else {
                getInfoMessage("Emetteur " + usersEmetteurSearch.getNomUsers());
            }
        }
    }

    public void onSearchUserDestinataire() {
        if ((destinataireSearch != null) ? destinataireSearch.length() > 0 : false) {
            String[] champ = new String[]{"nomUsers"};
            Object[] val = new Object[]{destinataireSearch};
            usersDestinataireSearch = (YvsUsers) dao.loadOneByNameQueries("YvsUsers.findByNom", champ, val);
            if ((usersDestinataireSearch != null) ? usersDestinataireSearch.getId() == 0 : true) {
                destinataireSearch = "";
                getErrorMessage("Destinataire inconnu !");
            } else {
                getInfoMessage("Destinataire " + usersDestinataireSearch.getNomUsers());
            }
        }
    }
    boolean firstSearch = true, tmpExist = false;
    Connection conn;

    public void chooseSector() {
        if (dropTableTmpExist("temp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase())) {
            createTableSuit();
            firstSearch = true;
        }
    }

    public void createTableTemp() {
        if (!tmpExist) {
            try {
                conn = (Connection) ds.getConnection();
                try (Statement stm = (Statement) conn.createStatement()) {
                    if (dropTableTmpExist("tmp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase())) {
                        String create = "CREATE TEMPORARY TABLE tmp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " ON COMMIT PRESERVE ROWS AS SELECT "
                                + "c.id as id_conv, "
                                + "c.objet, "
                                + "c.contenu, "
                                + "c.date_envoi, "
                                + "c.heure_envoi, "
                                + "c.emetteur, "
                                + "u.nom_users as nom_users_conv, "
                                + "c.reponse, "
                                + "c.supp as supp_conv, "
                                + "c.envoyer, "
                                + "c.reference, "
                                + "c.piece_jointe, "
                                + "c.delet, "
                                + "c.priorite, "
                                + "c.adresse_externe as adresse_externe_conv, "
                                + "c.externe as externe_conv, "
                                + "c.accuse as accuse_conv, "
                                + "d.id as id_dest, "
                                + "d.message, "
                                + "d.destinataire, "
                                + "u1.nom_users as nom_users_dest, "
                                + "d.accuse as accuse_dest, "
                                + "d.date_reception, "
                                + "d.heure_reception, "
                                + "d.copie, "
                                + "d.externe as externe_dest, "
                                + "d.adresse_externe as adresse_externe_dest, "
                                + "d.groupe_message, "
                                + "d.supp as supp_dest, "
                                + "d.send_accuse, "
                                + "d.delete "
                                + "FROM yvs_msg_conversation c INNER JOIN yvs_msg_destinataire d ON c.id = d.message "
                                + "INNER JOIN yvs_users u ON u.id = c.emetteur INNER JOIN yvs_users u1 ON u1.id = d.destinataire "
                                + "WHERE c.emetteur = " + currentUser.getId() + " OR d.destinataire = " + currentUser.getId();
                        stm.execute(create);
                        chooseSector();
                    }
                }
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(ManagedConversation.class.getName()).log(Level.SEVERE, null, ex);
            }
            tmpExist = true;
        }
    }

    public void createTableSuit() {
        try (Statement stm = (Statement) conn.createStatement()) {
            String create = "CREATE TEMPORARY TABLE temp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " (";
            String columns = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'tmp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + "'";
            try (ResultSet rst = (ResultSet) stm.executeQuery(columns)) {
                while (rst.next()) {
                    create += rst.getString("column_name") + " " + rst.getString("data_type") + ",";
                }
            }
            create = create.substring(0, create.length() - 1);
            create += ");";
            stm.execute(create);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fullTableSuit(String condition) {
        String columns = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'tmp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + "'";
        String req = "SELECT * FROM tmp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " WHERE " + condition;
        try (Statement stm = (Statement) conn.createStatement()) {
            int rows = 0;
            try (ResultSet c = (ResultSet) stm.executeQuery(columns)) {
                while (c.next()) {
                    rows += 1;
                }
            }
            String[][] cols = new String[2][rows];
            int row = 0;
            try (ResultSet col = (ResultSet) stm.executeQuery(columns)) {
                while (col.next()) {
                    cols[0][row] = col.getString("column_name");
                    cols[1][row] = col.getString("data_type");
                    row += 1;
                }
            }
            List<String> reqs = new ArrayList<>();
            try (ResultSet rst = (ResultSet) stm.executeQuery(req)) {
                while (rst.next()) {
                    String insert = "INSERT INTO temp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " VALUES (";
                    for (int i = 0; i < rows; i++) {
                        if (null != cols[1][i]) {
                            switch (cols[1][i]) {
                                case "double precision":
                                case "real":
                                case "double":
                                    insert += rst.getDouble(cols[0][i]) + ",";
                                    break;
                                case "long":
                                case "bigint":
                                case "bigserial":
                                    insert += rst.getLong(cols[0][i]) + ",";
                                    break;
                                case "int":
                                case "integer":
                                case "serial":
                                    insert += rst.getInt(cols[0][i]) + ",";
                                    break;
                                case "boolean":
                                    insert += "'" + ((rst.getString(cols[0][i]) != null) ? rst.getString(cols[0][i]) : "f") + "',";
                                    break;
                                default:
                                    insert += "'" + ((rst.getString(cols[0][i]) != null) ? rst.getString(cols[0][i]).replace("'", "''") : "") + "',";
                                    break;
                            }
                        }
                    }
                    insert = insert.substring(0, insert.length() - 1);
                    insert += ");";
                    reqs.add(insert);
                }
            }
            for (String insrt : reqs) {
                stm.execute(insrt);
            }
            firstSearch = false;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filterRowTableSuit(String condition) {
        try (Statement stm = (Statement) conn.createStatement()) {
            String delete = "DELETE FROM temp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " where " + condition;
            stm.execute(delete);

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean dropTableTmpExist(String table) {
        try (Statement stm = (Statement) conn.createStatement()) {
            String drop = "DROP TABLE IF EXISTS " + table;
            stm.execute(drop);
            return true;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void freeRessource() {
        try (Statement stm = (Statement) conn.createStatement()) {
            String discard = "DISCARD TEMP";
            stm.execute(discard);

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Destinataire> returnDestinatairesByJdbc(String condition) {
        String req = "SELECT * FROM temp_" + currentUser.getUsers().getCodeUsers().replace(".", "").toLowerCase() + " WHERE delet = false AND delete = false " + condition;
        List<Destinataire> list = new ArrayList<>();
        try (Statement stm = (Statement) conn.createStatement()) {
            try (ResultSet rst = (ResultSet) stm.executeQuery(req)) {
                Conversation conv;
                Destinataire dest;
                while (rst.next()) {
                    conv = new Conversation();
                    conv.setId(rst.getInt("id_conv"));
                    conv.setContenu(rst.getString("contenu"));
                    conv.setDateEnvoi(rst.getDate("date_envoi"));
                    conv.setHeureEnvoi(rst.getDate("heure_envoi"));
                    conv.setObjet(rst.getString("objet"));
                    conv.setReference(rst.getString("reference"));
                    conv.setAdresseExterne(rst.getString("adresse_externe_conv"));
                    conv.setEnvoyer(rst.getBoolean("envoyer"));
                    conv.setExterne(rst.getBoolean("externe_conv"));
                    conv.setEmetteur(new Users(rst.getLong("emetteur"), rst.getString("nom_users_conv")));
                    conv.setPriorite(rst.getString("priorite"));
                    conv.setPieceJointe(rst.getString("piece_jointe"));
                    conv.setAccuse(rst.getBoolean("accuse_conv"));
                    conv.setSupp(rst.getBoolean("supp_conv"));
                    conv.setDelete(rst.getBoolean("delet"));

                    dest = new Destinataire();
                    dest.setId(rst.getInt("id_dest"));
                    dest.setAccuse(rst.getBoolean("accuse_dest"));
                    dest.setDateReception(rst.getDate("date_reception"));
                    dest.setHeureReception(rst.getDate("heure_reception"));
                    dest.setDestinataire(new Users(rst.getLong("destinataire"), rst.getString("nom_users_dest")));
                    dest.setExterne(rst.getBoolean("externe_dest"));
                    dest.setCopie(rst.getBoolean("copie"));
                    dest.setAdresseExterne(rst.getString("adresse_externe_dest"));
                    dest.setMessage(conv);
                    dest.setGroupe(new GroupeMessage(rst.getInt("groupe_message")));
                    dest.setDelete(rst.getBoolean("delete"));
                    dest.setSendAccuse(rst.getBoolean("send_accuse"));
                    if (dest.isExterne()) {
                        dest.setSendTo(dest.getAdresseExterne());
                    } else {
                        dest.setSendTo(dest.getDestinataire().getNomUsers());
                    }
                    list.add(dest);

                }
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ManagedConversation.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void onFilterMailByEmetteur() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        if (firstSearch) {
            String condition = "emetteur = " + usersEmetteurSearch.getId() + "";
            fullTableSuit(condition);
        } else {
            String condition = "emetteur != " + usersEmetteurSearch.getId() + "";
            filterRowTableSuit(condition);
        }
        String condition = "";
        switch (eltSearch) {
            case "inbox":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true";
                listMailIndox = returnDestinatairesByJdbc(condition);
                onOngletChange(true, false, false, false, false, false, false);
                break;
            case "send":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                List<Destinataire> list0 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list0) {
                    listMailSend.add(getConversationByDestinataire(d));
                }
                listMailSend.clear();
                onOngletChange(false, true, false, false, false, false, false);
                break;
            case "draft":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = false";
                listMailDraft.clear();
                List<Destinataire> list1 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list1) {
                    listMailDraft.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, true, false, false, false, false);
                break;
            case "corb":
                condition = "AND supp_conv = true OR supp_dest = true";
                listMailCorb.clear();
                List<Destinataire> list2 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list2) {
                    listMailCorb.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, true, false, false, false);
                break;
            case "all":
                listMailSearch.clear();
                List<Destinataire> list3 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list3) {
                    listMailSearch.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, false, false, false, true);
                break;
        }
        update("head_messagerie");
        update("panel_messagerie_01");
    }

    public void onFilterMailByDestinataire() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        if (firstSearch) {
            String condition = "destinataire = " + usersDestinataireSearch.getId() + "";
            fullTableSuit(condition);
        } else {
            String condition = "destinataire != " + usersDestinataireSearch.getId() + "";
            filterRowTableSuit(condition);
        }
        String condition = "";
        switch (eltSearch) {
            case "inbox":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                listMailIndox = returnDestinatairesByJdbc(condition);
                onOngletChange(true, false, false, false, false, false, false);
                break;
            case "send":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                List<Destinataire> list0 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list0) {
                    listMailSend.add(getConversationByDestinataire(d));
                }
                listMailSend.clear();
                onOngletChange(false, true, false, false, false, false, false);
                break;
            case "draft":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = false";
                listMailDraft.clear();
                List<Destinataire> list1 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list1) {
                    listMailDraft.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, true, false, false, false, false);
                break;
            case "corb":
                condition = "AND supp_conv = true OR supp_dest = true";
                listMailCorb.clear();
                List<Destinataire> list2 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list2) {
                    listMailCorb.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, true, false, false, false);
                break;
            case "all":
                listMailSearch.clear();
                List<Destinataire> list3 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list3) {
                    listMailSearch.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, false, false, false, true);
                break;
        }
        update("head_messagerie");
        update("panel_messagerie_01");
    }

    public void onFilterMailByObjet() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        if (firstSearch) {
            String condition = "objet = '" + objetSearch + "'";
            fullTableSuit(condition);
        } else {
            String condition = "objet != '" + objetSearch + "'";
            filterRowTableSuit(condition);
        }
        String condition = "";
        switch (eltSearch) {
            case "inbox":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                listMailIndox = returnDestinatairesByJdbc(condition);
                onOngletChange(true, false, false, false, false, false, false);
                break;
            case "send":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                List<Destinataire> list0 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list0) {
                    listMailSend.add(getConversationByDestinataire(d));
                }
                listMailSend.clear();
                onOngletChange(false, true, false, false, false, false, false);
                break;
            case "draft":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = false";
                listMailDraft.clear();
                List<Destinataire> list1 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list1) {
                    listMailDraft.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, true, false, false, false, false);
                break;
            case "corb":
                condition = "AND supp_conv = true OR supp_dest = true";
                listMailCorb.clear();
                List<Destinataire> list2 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list2) {
                    listMailCorb.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, true, false, false, false);
                break;
            case "all":
                listMailSearch.clear();
                List<Destinataire> list3 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list3) {
                    listMailSearch.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, false, false, false, true);
                break;
        }
        update("head_messagerie");
        update("panel_messagerie_01");
    }

    public void onFilterMailByContenu() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        if (firstSearch) {
            String condition = "contenu like '%" + contenuSearch + "%'";
            fullTableSuit(condition);
        } else {
            String condition = "contenu not like '%" + contenuSearch + "%'";
            filterRowTableSuit(condition);
        }
        String condition = "";
        switch (eltSearch) {
            case "inbox":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                listMailIndox = returnDestinatairesByJdbc(condition);
                onOngletChange(true, false, false, false, false, false, false);
                break;
            case "send":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                List<Destinataire> list0 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list0) {
                    listMailSend.add(getConversationByDestinataire(d));
                }
                listMailSend.clear();
                onOngletChange(false, true, false, false, false, false, false);
                break;
            case "draft":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = false";
                listMailDraft.clear();
                List<Destinataire> list1 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list1) {
                    listMailDraft.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, true, false, false, false, false);
                break;
            case "corb":
                condition = "AND supp_conv = true OR supp_dest = true";
                listMailCorb.clear();
                List<Destinataire> list2 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list2) {
                    listMailCorb.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, true, false, false, false);
                break;
            case "all":
                listMailSearch.clear();
                List<Destinataire> list3 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list3) {
                    listMailSearch.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, false, false, false, true);
                break;
        }
        update("head_messagerie");
        update("panel_messagerie_01");
    }

    public void onFilterMailByDateEnvoi() {
        setEltSearch(("".equals(eltSearch)) ? "all" : getEltSearch());
        if (firstSearch) {
            String condition = "date_envoi = '" + dateEnvoiSearch + "'";
            fullTableSuit(condition);
        } else {
            String condition = "date_envoi != '" + dateEnvoiSearch + "'";
            filterRowTableSuit(condition);
        }
        String condition = "";
        switch (eltSearch) {
            case "inbox":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                listMailIndox = returnDestinatairesByJdbc(condition);
                onOngletChange(true, false, false, false, false, false, false);
                break;
            case "send":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = true ";
                List<Destinataire> list0 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list0) {
                    listMailSend.add(getConversationByDestinataire(d));
                }
                listMailSend.clear();
                onOngletChange(false, true, false, false, false, false, false);
                break;
            case "draft":
                condition = "AND supp_conv = false AND supp_dest = false AND envoyer = false";
                listMailDraft.clear();
                List<Destinataire> list1 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list1) {
                    listMailDraft.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, true, false, false, false, false);
                break;
            case "corb":
                condition = "AND supp_conv = true OR supp_dest = true";
                listMailCorb.clear();
                List<Destinataire> list2 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list2) {
                    listMailCorb.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, true, false, false, false);
                break;
            case "all":
                listMailSearch.clear();
                List<Destinataire> list3 = returnDestinatairesByJdbc(condition);
                for (Destinataire d : list3) {
                    listMailSearch.add(getConversationByDestinataire(d));
                }
                onOngletChange(false, false, false, false, false, false, true);
                break;
        }
        update("head_messagerie");
        update("panel_messagerie_01");
    }
}
