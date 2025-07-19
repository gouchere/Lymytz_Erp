/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.societe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.ParametreBase;
import yvs.base.Publicites;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.communication.YvsParametreMessagerieProfessionnel;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.param.YvsBaseGroupeSociete;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsSocietesInfosVente;
import yvs.entity.param.YvsSocietes;
import yvs.entity.produits.YvsBasePublicites;
import yvs.entity.users.YvsUsersAgence;
import yvs.grh.UtilGrh;
import yvs.parametrage.dico.Dictionnaire;
import yvs.parametrage.ParametreServeurMail;
import yvs.production.UtilProd;
import yvs.util.Managed;

/**
 *
 * @author lymytz
 */
public class UtilSte {

    public static ParametreBase buildBeanParametre(YvsBaseParametre y) {
        ParametreBase p = new ParametreBase();
        if (y != null) {
            p.setId(y.getId());
            p.setDateSave(y.getDateSave());
            p.setDefautPassword(y.getDefautPassword());
            p.setGenererPassword(y.getGenererPassword());
            p.setTaillePassword(y.getTaillePassword());
            p.setDureeInactivArticle(y.getDureeInactivArticle());
            p.setGenererReferenceArticle(y.getGenererReferenceArticle());
            p.setTailleLettreReferenceArticle(y.getTailleLettreReferenceArticle());
            p.setTailleNumeroReferenceArticle(y.getTailleNumeroReferenceArticle());
            p.setMonitorPr(y.getMonitorPr());
            p.setTauxEcartPr(y.getTauxEcartPr());
            p.setBackColorEtiquette(y.getBackColorEtiquette());
            p.setForeColorEtiquette(y.getForeColorEtiquette());
            p.setNombreEltAccueil(y.getNombreEltAccueil());
            p.setNombrePageMin(y.getNombrePageMin());
            p.setDureeDefaultPassword(y.getDureeDefaultPassword());
        }
        return p;
    }

    public static YvsBaseParametre buildParametre(ParametreBase y, YvsUsersAgence ua, YvsSocietes ste) {
        YvsBaseParametre p = new YvsBaseParametre();
        if (y != null) {
            p.setId(y.getId());
            p.setDateSave(y.getDateSave());
            p.setDefautPassword(y.getDefautPassword());
            p.setGenererPassword(y.isGenererPassword());
            p.setTaillePassword(y.getTaillePassword());
            p.setDureeInactivArticle(y.getDureeInactivArticle());
            p.setGenererReferenceArticle(y.isGenererReferenceArticle());
            p.setTailleLettreReferenceArticle(y.getTailleLettreReferenceArticle());
            p.setTailleNumeroReferenceArticle(y.getTailleNumeroReferenceArticle());
            p.setMonitorPr(y.isMonitorPr());
            p.setTauxEcartPr(y.getTauxEcartPr());
            p.setBackColorEtiquette(y.getBackColorEtiquette());
            p.setForeColorEtiquette(y.getForeColorEtiquette());
            p.setNombreEltAccueil(y.getNombreEltAccueil());
            p.setNombrePageMin(y.getNombrePageMin());
            p.setDureeDefaultPassword(y.getDureeDefaultPassword());
            p.setDateUpdate(new Date());
            p.setAuthor(ua);
            p.setSociete(ste);
        }
        return p;
    }

    public static ParametreServeurMail buildBeanParametreServeurMail(YvsParametreMessagerieProfessionnel p) {
        ParametreServeurMail r = new ParametreServeurMail();
        if (p != null) {
            r.setAdresse(p.getAdresse());
            r.setId(p.getId());
            r.setNomDomaine(p.getNomDomaine());
            r.setPassword(p.getPassword());
            r.setPort(p.getPort());
        }
        return r;
    }

    public static List<ParametreServeurMail> buildBeanListParametreServeurMail(List<YvsParametreMessagerieProfessionnel> list) {
        List<ParametreServeurMail> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsParametreMessagerieProfessionnel s : list) {
                    ParametreServeurMail ste = buildBeanParametreServeurMail(s);
                    result.add(ste);
                }
            }
        }
        return result;
    }

    public static YvsSocietes buildSociete(Societe ste, YvsUsersAgence ua) {
        YvsSocietes r = new YvsSocietes();
        if (ste != null) {
            r.setId(ste.getId());
            r.setName(ste.getRaisonSocial());
            r.setAdressSiege(ste.getAdressSiege());
            r.setCapital(ste.getCapital());
            r.setCodeAbreviation(ste.getCodeAbreviation());
            r.setCodePostal(ste.getCodePostal());
            r.setDevise(ste.getDevise());
            r.setEmail(ste.getEmail());
            r.setFormeJuridique(ste.getFormeJuridique());
            r.setLogo(ste.getLogo());
            r.setNumeroRegistreComerce(ste.getNumeroRegistreCom());
            r.setNumeroContribuable(ste.getNumeroContribuable());
            r.setSiege(ste.getSiege());
            r.setDescription(ste.getDescription());
            r.setAPropos(ste.getAPropos());
            r.setVenteOnline(ste.isVenteOnline());
            r.setFax(ste.getFax());
            r.setSiteWeb(ste.getSiteWeb());
            r.setForGlp(ste.isForGlp());
            r.setCachet(ste.getCachet());
            if ((ste.getVille() != null) ? (ste.getVille().getId() != 0 && ste.getVille().getId() != -1) : false) {
                r.setVille(new YvsDictionnaire(ste.getVille().getId()));
            } else {
                r.setVille(null);
            }
            if ((ste.getGroupe() != null) ? ste.getGroupe().getId() > 0 : false) {
                r.setGroupe(new YvsBaseGroupeSociete(ste.getGroupe().getId()));
            } else {
                r.setGroupe(null);
            }
            if ((ste.getPays() != null) ? (ste.getPays().getId() != 0 && ste.getPays().getId() != -1) : false) {
                r.setPays(new YvsDictionnaire(ste.getPays().getId()));
            } else {
                r.setPays(null);
            }
            r.setTel(ste.getTelephone());
            r.setUmonaie(ste.getUmonaie());
            r.setEcartDocument(ste.getEcartDocument());
            r.setActif(true);
            r.setSupp(false);
            r.setAuthor(ua);
            r.setDateSave(ste.getDateSave());
            r.setDateUpdate(new Date());
            r.setPrintWithEntete(ste.isPrintWithEntete());
        }
        return r;
    }

    public static Societe buildSimpleBeanSociete(YvsSocietes ste) {
        Societe society = new Societe();
        if (ste != null) {
            society.setId(ste.getId());
            society.setRaisonSocial(ste.getName());
            society.setCodeAbreviation(ste.getCodeAbreviation());
            society.setNumeroRegistreCom(ste.getNumeroRegistreComerce());
            society.setNumeroContribuable(ste.getNumeroContribuable());
            society.setSiege(ste.getSiege());
            society.setAdressSiege(ste.getAdressSiege());
            society.setTelephone(ste.getTel());
            society.setEmail(ste.getEmail());
            society.setCodePostal(ste.getCodePostal());
            society.setSiteWeb(ste.getSiteWeb());
            society.setCapital(ste.getCapital());
            society.setDevise(ste.getDevise());
            society.setFormeJuridique(ste.getFormeJuridique());
            society.setUmonaie(ste.getUmonaie());
            society.setLogo(ste.getLogo());
            society.setGroupe(ste.getGroupe());
            society.setEcartDocument(ste.getEcartDocument());
            society.setPrintWithEntete(ste.getPrintWithEntete());
            society.setLogo(ste.getLogo());
            society.setFax(ste.getFax());
            society.setVenteOnline(ste.getVenteOnline());
            society.setForGlp(ste.getForGlp());
            society.setUmonaie(ste.getUmonaie());
            society.setDateSave(ste.getDateSave());
            society.setAPropos(ste.getAPropos());
            society.setDescription(ste.getDescription());
            society.setCachet(ste.getCachet());
        }
        return society;
    }

    public static Societe buildBeanSociete(YvsSocietes ste) {
        Societe society = buildSimpleBeanSociete(ste);
        if (ste != null) {
            society.setPays((ste.getPays() != null) ? new Dictionnaire(ste.getPays().getId(), ste.getPays().getLibele()) : new Dictionnaire());
            society.setVille((ste.getVille() != null) ? new Dictionnaire(ste.getVille().getId(), ste.getVille().getLibele()) : new Dictionnaire());
            society.setAgences(ste.getAgences());
            society.setEmployePermament(ste.employePermamentByList());
            society.setEmployeStagiaire(ste.getEmployeStagiaire());
            society.setEmployeTacheron(ste.getEmployeTacheron());
            society.setEmployeTemporaire(ste.getEmployeTemporaire());
            society.setPathLogo(Managed.returnLogo(ste.getLogo()));
            society.setInfosVente(buildBeanInfosVente(ste.getInfosVente()));
        }
        return society;
    }

    public static List<Societe> buildBeanListeSociete(List<YvsSocietes> list) {
        List<Societe> result = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsSocietes s : list) {
                    result.add(buildBeanSociete(s));
                }
            }
        }
        return result;
    }

    public static InfosVenteSociete buildBeanInfosVente(YvsSocietesInfosVente y) {
        InfosVenteSociete r = new InfosVenteSociete();
        if (y != null) {
            r.setId(y.getId());
            r.setDisplayCatalogueOnList(y.getDisplayCatalogueOnList());
            r.setTypeLivraison(UtilGrh.buildBeanTypeCout(y.getTypeLivraison()));
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsSocietesInfosVente buildInfosVente(InfosVenteSociete y, YvsSocietes ste, YvsUsersAgence ua) {
        YvsSocietesInfosVente r = new YvsSocietesInfosVente();
        if (y != null) {
            r.setId(y.getId());
            if (y.getTypeLivraison() != null ? y.getTypeLivraison().getId() > 0 : false) {
                r.setTypeLivraison(new YvsGrhTypeCout(y.getTypeLivraison().getId(), y.getTypeLivraison().getLibelle()));
            }
            r.setDisplayCatalogueOnList(y.isDisplayCatalogueOnList());
            r.setDateSave(y.getDateSave());
            r.setDateUpdate(new Date());
            r.setSociete(ste);
            r.setAuthor(ua);
        }
        return r;
    }

    /**
     * *******DEBUT GESTION DICTIONNAIRE
     *
     **********
     * @param d
     * @return
     */
    public static Dictionnaire buildBeanDictionnaire(YvsDictionnaire d) {
        return UtilGrh.buildBeanDictionnaire(d);
    }

    public static List<Dictionnaire> buildBeanListDictionnaire(List<YvsDictionnaire> list) {
        List<Dictionnaire> r = new ArrayList<>();
        if (list != null) {
            if (!list.isEmpty()) {
                for (YvsDictionnaire d : list) {
                    Dictionnaire s = buildBeanDictionnaire(d);
                    r.add(s);
                }
            }
        }
        return r;
    }

    /**
     * *******FIN GESTION DICTIONNAIRE**********
     */
    /**
     * *******DEBUT PUBLICITE
     *
     **********
     * @param y
     * @return
     */
    public static Publicites buildBeanPublicites(YvsBasePublicites y) {
        Publicites r = new Publicites();
        if (y != null) {
            r.setArticle(UtilProd.buildBeanConditionnement(y.getArticle()));
            r.setDateDebut(y.getDateDebut());
            r.setDateFin(y.getDateFin());
            r.setDateSave(y.getDateSave());
            r.setDescription(y.getDescription());
            r.setId(y.getId());
            r.setImage(y.getImage());
            r.setPermanent(y.getPermanent());
            r.setPriorite(y.getPriorite());
            r.setUrl(y.getUrl());
        }
        return r;
    }

    public static YvsBasePublicites buildPublicites(Publicites y, YvsSocietes ste, YvsUsersAgence ua) {
        YvsBasePublicites r = new YvsBasePublicites();
        if (y != null) {
            if (y.getArticle() != null ? y.getArticle().getId() > 0 : false) {
                r.setArticle(UtilProd.buildConditionnement(y.getArticle(), ua));
            }
            r.setDateDebut(y.getDateDebut());
            r.setDateFin(y.getDateFin());
            r.setDateSave(y.getDateSave());
            r.setDescription(y.getDescription());
            r.setId(y.getId());
            r.setImage(y.getImage());
            r.setPermanent(y.isPermanent());
            r.setPriorite(y.getPriorite());
            r.setUrl(y.getUrl());
            r.setSociete(ste);
            r.setAuthor(ua);
            r.setDateUpdate(new Date());
            r.setNew_(true);
        }
        return r;
    }

    /**
     * *******FIN PUBLICITE**********
     */
}
