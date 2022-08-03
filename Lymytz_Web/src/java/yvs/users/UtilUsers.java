/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.CodeAcces;
import yvs.base.tiers.UtilTiers;
import yvs.commercial.CategoriePerso;
import yvs.commercial.commission.PlanCommission;
import yvs.commercial.UtilCom;
import yvs.entity.base.YvsBaseCodeAcces;
//import yvs.entity.communication.YvsAdresseProfessionnel;
import yvs.entity.communication.YvsMsgConversation;
import yvs.entity.communication.YvsMsgGroupeMessage;
import yvs.entity.communication.YvsMsgCarnetAdresse;
import yvs.entity.communication.YvsMsgDestinataire;
import yvs.entity.communication.YvsMsgFiltreMessage;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsMsgDiffusionContact;
import yvs.entity.users.YvsMsgGroupeDiffusion;
import yvs.entity.users.YvsModule;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsPageModule;
import yvs.entity.users.YvsRessourcesPage;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;
import yvs.entity.users.YvsUsersGrade;
import yvs.entity.users.YvsUsersMemo;
import yvs.parametrage.societe.Societe;
import yvs.parametrage.societe.UtilSte;
import yvs.parametrage.agence.UtilAgence;
import yvs.users.messagerie.Conversation;
import yvs.users.messagerie.Destinataire;
import yvs.users.messagerie.DiffusionContact;
import yvs.users.messagerie.FiltreMail;
import yvs.users.messagerie.GroupeDiffusion;
import yvs.users.messagerie.GroupeMessage;

/**
 *
 * @author lymytz
 */
public class UtilUsers {

    /**
     * *******DEBUT GESTION USER
     *
     **********
     * @param usr
     * @return
     */
    public static Users buildSimpleBeanUsers(YvsUsers usr) {
        Users b = new Users();
        if (usr != null) {
            b.setId(usr.getId());
            b.setAccesMultiAgence((usr.getAccesMultiAgence() == null) ? false : usr.getAccesMultiAgence());
            b.setAccesMultiSociete((usr.getAccesMultiSociete() == null) ? false : usr.getAccesMultiSociete());
            b.setActif((usr.getActif() == null) ? false : usr.getActif());
            b.setAleaMdp(usr.getAleaMdp());
            b.setCodeUsers(usr.getCodeUsers());
            b.setAbbreviation(usr.getAbbreviation());
            b.setSuperAdmin(false);
            b.setSupp((usr.getSupp() == null) ? true : usr.getSupp());
            b.setConnecte((usr.getConnecte() == null) ? false : usr.getConnecte());
            b.setNomUsers(usr.getNomUsers());
            b.setPasswordUser(usr.getPasswordUser());
            b.setPhoto(usr.getPhoto());
            b.setCivilite(usr.getCivilite());
            b.setError(usr.isError());
            b.setVenteOnline(usr.getVenteOnline());
            b.setConnectOnlinePlanning(usr.getConnectOnlinePlanning());
        }
        return b;
    }

    public static Users buildBeanUsers(YvsUsers usr) {
        Users b = buildSimpleBeanUsers(usr);
        if (usr != null) {
            b.setCommerciaux(usr.getCommercial());
            b.setAgence(UtilAgence.buildBeanAgence(usr.getAgence()));
            b.setNiveauAcces(buildBeanNiveauAcces(usr.getNiveauAcces()));
            b.setSociete(b.getNiveauAcces().getSociete());
            for (AdresseProfessionnel a : b.getListAdresse()) {
                if (a.isPrincipal()) {
                    b.setAdresse(a);
                    break;
                }
            }
//            b.setCarnetAdresse((usr.getYvsMsgCarnetAdresseList() != null) ? buildBeanListCarnetAdresse(usr.getYvsMsgCarnetAdresseList()) : new ArrayList<CarnetAdresse>());
//            b.setDossiers((usr.getYvsGroupeMessageList() != null) ? buildBeanListGroupeMessage(usr.getYvsGroupeMessageList()) : new ArrayList<GroupeMessage>());
            b.setCategorie(usr.getCategorie() != null ? UtilCom.buildBeanCategoriePerso(usr.getCategorie()) : new CategoriePerso());
            b.setPlanCommission(usr.getPlanCommission() != null ? UtilCom.buildBeanPlanCommission(usr.getPlanCommission()) : new PlanCommission());
            if (usr.getEmploye() != null) {
                b.setEmploye(usr.getEmploye().getNom_prenom());
                b.setTiers(UtilTiers.buildSimpleBeanTiers(usr.getEmploye().getCompteTiers()));
            }
            if (usr.getValidite() != null ? usr.getValidite().getId() > 0 : false) {
                b.setTemporaire(true);
                b.setDateExpiration(usr.getValidite().getDateExpiration());
            }
        }
        return b;
    }

    public static YvsUsers buildSimpleBeanUsers(Users usr) {
        YvsUsers b = null;
        if (usr != null) {
            b = new YvsUsers();
            b.setId(usr.getId());
            b.setAleaMdp(usr.getAleaMdp());
            b.setCodeUsers(usr.getCodeUsers());
            b.setAbbreviation(usr.getAbbreviation());
            b.setSuperAdmin(false);
            b.setNomUsers(usr.getNomUsers());
            b.setPasswordUser(usr.getPasswordUser());
            b.setPhoto(usr.getPhoto());
            b.setCivilite(usr.getCivilite());
            b.setError(usr.isError());
            b.setConnectOnlinePlanning(usr.isConnectOnlinePlanning());
        }
        return b;
    }

    public static List<Users> buildBeanListUsers(List<YvsUsers> e) {
        List<Users> l = new ArrayList<>();
        if (e != null) {
            for (YvsUsers i : e) {
                Users b = buildBeanUsers(i);
                l.add(b);
            }
        }
        return l;
    }

//    public static AdresseProfessionnel buildBeanAdresseProfessionnel(YvsAdresseProfessionnel a) {
//        AdresseProfessionnel r = new AdresseProfessionnel();
//        if (a != null) {
//            r.setAdresse(a.getAdresse());
//            r.setId(a.getId());
//            r.setPassword(a.getPassword());
//            r.setPrincipal((a.getPrincipal() != null) ? a.getPrincipal() : false);
//        }
//        return r;
//    }
//
//    public static List<AdresseProfessionnel> buildBeanListAdresseProfessionnel(List<YvsAdresseProfessionnel> e) {
//        List<AdresseProfessionnel> l = new ArrayList<>();
//        if (e != null) {
//            for (YvsAdresseProfessionnel i : e) {
//                AdresseProfessionnel b = buildBeanAdresseProfessionnel(i);
//                l.add(b);
//            }
//        }
//        return l;
//    }
    public static NiveauAcces buildBeanNiveauAcces(YvsNiveauAcces e) {
        NiveauAcces b = new NiveauAcces();
        if (e != null) {
            b.setId(e.getId());
            b.setActif((e.getActif() == null) ? false : e.getActif());
            b.setSupp((e.getSupp() == null) ? true : e.getSupp());
            b.setDescription(e.getDescription());
            b.setDesignation(e.getDesignation());
            b.setGrade(e.getGrade() != null ? e.getGrade().getId() : 0);
            b.setSuperAdmin((e.getSuperAdmin() == null) ? false : e.getSuperAdmin());
            b.setSociete((e.getSociete() != null) ? UtilSte.buildBeanSociete(e.getSociete()) : new Societe());
            b.setDateSave(e.getDateSave());
        }
        return b;
    }

    public static YvsNiveauAcces buildNiveauAcces(NiveauAcces e, YvsUsersAgence currentUsers, YvsSocietes currentScte) {
        YvsNiveauAcces b = new YvsNiveauAcces();
        if (e != null) {
            b.setId(e.getId());
            b.setActif(true);
            b.setSupp(false);
            b.setDescription(e.getDescription());
            b.setDesignation(e.getDesignation());
            if (e.getGrade() > 0) {
                b.setGrade(new YvsUsersGrade(e.getGrade()));
            }
            b.setSuperAdmin(e.isSuperAdmin());
            b.setSociete(currentScte);
            b.setAuthor(currentUsers);
            b.setDateSave(e.getDateSave());
            b.setDateUpdate(new Date());
        }
        return b;
    }

    public static List<NiveauAcces> buildBeanListNiveauAcces(List<YvsNiveauAcces> e) {
        List<NiveauAcces> l = new ArrayList<>();
        if (e != null) {
            for (YvsNiveauAcces i : e) {
                NiveauAcces b = buildBeanNiveauAcces(i);
                l.add(b);
            }
        }
        return l;
    }

    public static CarnetAdresse buildBeanCarnetAdresse(YvsMsgCarnetAdresse c) {
        CarnetAdresse r = new CarnetAdresse();
        if (c != null) {
            r.setId(c.getId());
            r.setAdresse(c.getAdresse());
            r.setNom(c.getNom());
        }
        return r;
    }

    public static List<CarnetAdresse> buildBeanListCarnetAdresse(List<YvsMsgCarnetAdresse> e) {
        List<CarnetAdresse> l = new ArrayList<>();
        if (e != null) {
            for (YvsMsgCarnetAdresse i : e) {
                CarnetAdresse b = buildBeanCarnetAdresse(i);
                l.add(b);
            }
        }
        return l;
    }

    /**
     * *******FIN GESTION USER**********
     *
     */
    /**
     * *******DEBUT GESTION ACCES
     *
     **********
     * @param m
     * @return
     */
    public static RessourcesPage buildBeanRessourcesPage(YvsRessourcesPage m) {
        RessourcesPage r = new RessourcesPage();
        if (m != null) {
            r.setDescription(m.getDescription());
            r.setId(m.getId());
            r.setLibelle(m.getLibelle());
            r.setReference(m.getReferenceRessource());
        }
        return r;
    }

    public static List<RessourcesPage> buildBeanListRessourcesPage(List<YvsRessourcesPage> list) {
        List<RessourcesPage> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsRessourcesPage m : list) {
                    RessourcesPage i = buildBeanRessourcesPage(m);
                    r.add(i);
                }
            }
        }
        return r;
    }

    public static PageModule buildBeanPageModule(YvsPageModule m) {
        PageModule r = new PageModule();
        if (m != null) {
            r.setDescription(m.getDescription());
            r.setId(m.getId());
            r.setLibelle(m.getLibelle());
            r.setReference(m.getReference());
            r.setRessourcesPageList((m.getYvsRessourcesPageList() != null) ? buildBeanListRessourcesPage(m.getYvsRessourcesPageList())
                    : new ArrayList<RessourcesPage>());
        }
        return r;
    }

    public static List<PageModule> buildBeanListPageModule(List<YvsPageModule> list) {
        List<PageModule> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsPageModule m : list) {
                    PageModule i = buildBeanPageModule(m);
                    r.add(i);
                }
            }
        }
        return r;
    }

    public static Modules buildBeanModule(YvsModule m) {
        Modules r = new Modules();
        if (m != null) {
            r.setId(m.getId());
            r.setDescription(m.getDescription());
            r.setLibelle(m.getLibelle());
            r.setReference(m.getReference());
            r.setPageModuleList((m.getYvsPageModuleList() != null) ? buildBeanListPageModule(m.getYvsPageModuleList())
                    : new ArrayList<PageModule>());
        }
        return r;
    }

    public static List<Modules> buildBeanListModules(List<YvsModule> list) {
        List<Modules> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsModule m : list) {
                    Modules i = buildBeanModule(m);
                    r.add(i);
                }
            }
        }
        return r;
    }

    /**
     * *******FIN GESTION ACCES**********
     *
     */
    /**
     * *******DEBUT GESTION CONVERSATION**********
     *
     * @param c
     * @return
     */
    public static Destinataire buildBeanDestinataire(YvsMsgDestinataire c) {
        Destinataire r = new Destinataire();
        if (c != null) {
            r.setAccuse((c.getAccuse() != null) ? c.getAccuse() : false);
            r.setDateReception((c.getDateReception() != null) ? c.getDateReception() : new Date());
            r.setHeureReception((c.getHeureReception() != null) ? c.getHeureReception() : new Date());
            r.setSupp((c.getSupp() != null) ? c.getSupp() : false);
            r.setDestinataire((c.getDestinataire() != null) ? buildBeanUsers(c.getDestinataire()) : new Users());
            r.setExterne((c.getExterne() != null) ? c.getExterne() : false);
            r.setAdresseExterne(c.getAdresseExterne());
            r.setCopie((c.getCopie() != null) ? c.getCopie() : false);
            r.setDelete((c.getDelete() != null) ? c.getDelete() : false);
            r.setId(c.getId());
            r.setSendAccuse((c.getSendAccuse() != null) ? c.getSendAccuse() : false);
            if (r.isExterne()) {
                r.setSendTo(r.getAdresseExterne());
            } else {
                r.setSendTo(r.getDestinataire().getNomUsers());
            }
            r.setGroupe((c.getGroupeMessage() != null) ? buildBeanGroupeMessage(c.getGroupeMessage()) : new GroupeMessage());
        }
        return r;
    }

    public static List<Destinataire> buildBeanListDestinataire(List<YvsMsgDestinataire> list) {
        List<Destinataire> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgDestinataire c : list) {
                Destinataire bean = buildBeanDestinataire(c);
                r.add(bean);
            }
        }
        return r;
    }

    public static Conversation buildBeanConversation(YvsMsgConversation c) {
        Conversation r = new Conversation();
        if (c != null) {
            r.setId(c.getId());
            r.setReference(c.getReference());
            r.setContenu(c.getContenu());
            r.setPriorite(c.getPriorite());
            r.setExterne((c.getExterne() != null) ? c.getExterne() : false);
            r.setAccuse((c.getAccuse() != null) ? c.getAccuse() : false);
            r.setAdresseExterne(c.getAdresseExterne());
            r.setDateEnvoi((c.getDateEnvoi() != null) ? c.getDateEnvoi() : new Date());
            r.setHeureEnvoi((c.getHeureEnvoi() != null) ? c.getHeureEnvoi() : new Date());
            r.setObjet(c.getObjet());
            r.setSupp((c.getSupp() != null) ? c.getSupp() : false);
            r.setEnvoyer((c.getEnvoyer() != null) ? c.getEnvoyer() : false);
            r.setDelete((c.getDelet() != null) ? c.getDelet() : false);
            r.setEmetteur((c.getEmetteur() != null) ? buildBeanUsers(c.getEmetteur()) : new Users());
            r.setPieceJointe(c.getPieceJointe());
//            if ((c.getYvsConversationList() != null) ? !c.getYvsConversationList().isEmpty() : false) {
//                r.setConversationList(buildBeanListConversation(c.getYvsConversationList()));
//            }
            r.setDestinataires_cc(null);
            int cpt = 0;
//            if ((c.getYvsMsgDestinataireList() != null) ? !c.getYvsMsgDestinataireList().isEmpty() : false) {
//                r.setListDestinataire(buildBeanListDestinataire(c.getYvsMsgDestinataireList()));
//                for (Destinataire d : r.getListDestinataire()) {
//                    if ((r.getDestinataires() != null) ? "".equals(r.getDestinataires()) : true) {
//                        r.setDestinataires(d.getSendTo());
//                    } else {
//                        r.setDestinataires(r.getDestinataires() + "; " + d.getSendTo());
//                    }
//                    if (d.isExterne()) {
//                        if ((r.getAdressesExternes() != null) ? "".equals(r.getAdressesExternes()) : true) {
//                            r.setAdressesExternes(d.getAdresseExterne());
//                        } else {
//                            r.setAdressesExternes(r.getAdressesExternes() + "; " + d.getAdresseExterne());
//                        }
//                    }
//                    if (d.isCopie()) {
//                        if ((r.getDestinataires_cc() != null) ? "".equals(r.getDestinataires_cc()) : true) {
//                            r.setDestinataires_cc(d.getSendTo());
//                        } else {
//                            r.setDestinataires_cc(r.getDestinataires_cc() + "; " + d.getSendTo());
//                        }
//                        if (d.isExterne()) {
//                            if ((r.getAdresseExterne_cc() != null) ? "".equals(r.getAdresseExterne_cc()) : true) {
//                                r.setAdresseExterne_cc(d.getSendTo());
//                            } else {
//                                r.setAdresseExterne_cc(r.getAdresseExterne_cc() + "; " + d.getSendTo());
//                            }
//                        }
//                    } else {
//                        cpt += 1;
//                        if ((r.getDestinataires_() != null) ? "".equals(r.getDestinataires_()) : true) {
//                            r.setDestinataires_(d.getSendTo());
//                        } else {
//                            r.setDestinataires_(r.getDestinataires_() + "; " + d.getSendTo());
//                        }
//                    }
//                }
//            }
            r.setSendMultiple(cpt > 1);
            r.setSendCopie((r.getDestinataires_cc() != null) ? !"".equals(r.getDestinataires_cc()) : false);
        }
        return r;
    }

    public static List<Conversation> buildBeanListConversation(List<YvsMsgConversation> list) {
        List<Conversation> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgConversation c : list) {
                r.add(buildBeanConversation(c));
            }
        }
        return r;
    }

    public static DiffusionContact buildBeanDiffusionContact(YvsMsgDiffusionContact d) {
        DiffusionContact r = new DiffusionContact();
        if (d != null) {
            r.setActif((d.getActif() != null) ? d.getActif() : true);
            r.setId(d.getId());
            r.setUser((d.getUsers() != null) ? buildBeanUsers(d.getUsers()) : new Users());
        }
        return r;
    }

    public static List<DiffusionContact> buildBeanListDiffusionContact(List<YvsMsgDiffusionContact> list) {
        List<DiffusionContact> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgDiffusionContact c : list) {
                r.add(buildBeanDiffusionContact(c));
            }
        }
        return r;
    }

    public static GroupeDiffusion buildBeanGroupeDiffusion(YvsMsgGroupeDiffusion g) {
        GroupeDiffusion r = new GroupeDiffusion();
        if (g != null) {
            r.setId(g.getId());
            r.setLibelle(g.getLibelle());
            r.setReference(g.getReference());
            r.setPublics((g.getPublics() != null) ? g.getPublics() : false);
            r.setSociete((g.getSociete() != null) ? UtilSte.buildBeanSociete(g.getSociete()) : new Societe());
            r.setUser((g.getUsers() != null) ? buildBeanUsers(g.getUsers()) : new Users());
            r.setDiffusionContactList((g.getYvsDiffusionContactList() != null) ? buildBeanListDiffusionContact(g.getYvsDiffusionContactList())
                    : new ArrayList<DiffusionContact>());
        }
        return r;
    }

    public static List<GroupeDiffusion> buildBeanListGroupeDiffusion(List<YvsMsgGroupeDiffusion> list) {
        List<GroupeDiffusion> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgGroupeDiffusion c : list) {
                r.add(buildBeanGroupeDiffusion(c));
            }
        }
        return r;
    }

    public static GroupeMessage buildBeanGroupeMessage(YvsMsgGroupeMessage g) {
        GroupeMessage r = new GroupeMessage();
        if (g != null) {
            r = new GroupeMessage(g.getId(), g.getLibelle());
            r.setReference(g.getReference());
            r.setCheminParent(buildCheminParentDossier(g));
        }
        return r;
    }

    private static String buildCheminParentDossier(YvsMsgGroupeMessage g) {
        String chemin;
        if ((g.getParent() != null) ? g.getParent().getId() != 0 : false) {
            chemin = buildCheminParentDossier(g.getParent()) + " / " + g.getReference();
        } else {
            chemin = g.getReference();
        }
        return chemin;
    }

    public static List<GroupeMessage> buildBeanListGroupeMessage(List<YvsMsgGroupeMessage> list) {
        List<GroupeMessage> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgGroupeMessage c : list) {
                r.add(buildBeanGroupeMessage(c));
            }
        }
        return r;
    }

    public static FiltreMail buildBeanFiltreMail(YvsMsgFiltreMessage f) {
        FiltreMail r = new FiltreMail();
        r.setBase((f.getBase() != null) ? f.getBase() : "E");
        r.setId(f.getId());
        r.setValeur(f.getValeur());
        r.setGroupeMessage((f.getGroupeMessage() != null) ? buildBeanGroupeMessage(f.getGroupeMessage()) : new GroupeMessage());
        return r;
    }

    public static List<FiltreMail> buildBeanListFiltreMail(List<YvsMsgFiltreMessage> list) {
        List<FiltreMail> r = new ArrayList<>();
        if (list != null) {
            for (YvsMsgFiltreMessage c : list) {
                r.add(buildBeanFiltreMail(c));
            }
        }
        return r;
    }

    /**
     * *******FIN GESTION CONVERSATION**********
     *
     * @param y
     * @return
     */
    public static UsersMemo buildBeanUsersMemo(YvsUsersMemo y) {
        UsersMemo u = new UsersMemo();
        if (y != null) {
            u.setId(y.getId());
            u.setDateMemo(y.getDateMemo());
            u.setDateDebutRappel(y.getDateDebutRappel());
            u.setDateFinRappel(y.getDateFinRappel());
            u.setDureeRappel(y.getDureeRappel());
            u.setDescription(y.getDescription());
            u.setTitre(y.getTitre());
            u.setUsers(buildBeanUsers(y.getUsers()));
        }
        return u;
    }

    public static YvsUsersMemo buildUsersMemo(UsersMemo y, YvsUsers us, YvsUsersAgence ua) {
        YvsUsersMemo u = new YvsUsersMemo();
        if (y != null) {
            u.setId(y.getId());
            u.setDateMemo(y.getDateMemo());
            u.setDateDebutRappel(y.getDateDebutRappel());
            u.setDateFinRappel(y.getDateFinRappel());
            u.setDescription(y.getDescription());
            u.setDureeRappel(y.getDureeRappel());
            u.setTitre(y.getTitre());
            u.setUsers(us);
            u.setAuthor(ua);
            u.setNew_(true);
        }
        return u;
    }

    public static CodeAcces buildBeanCodeAcces(YvsBaseCodeAcces y) {
        CodeAcces r = new CodeAcces();
        if (y != null) {
            r.setId(y.getId());
            r.setCode(y.getCode());
            r.setDescription(y.getDescription());
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsBaseCodeAcces buildCodeAcces(CodeAcces y, YvsSocietes st, YvsUsersAgence ua) {
        YvsBaseCodeAcces r = new YvsBaseCodeAcces();
        if (y != null) {
            r.setId(y.getId());
            r.setCode(y.getCode());
            r.setDescription(y.getDescription());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setAuthor(ua);
            r.setSociete(st);
            r.setNew_(true);
        }
        return r;
    }
}
