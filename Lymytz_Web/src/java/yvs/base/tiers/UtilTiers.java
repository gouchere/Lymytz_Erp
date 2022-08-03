/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.UtilCompta;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.commercial.YvsComComerciale;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersDocument;
import yvs.entity.tiers.YvsBaseTiersTelephone;
import yvs.entity.users.YvsUsersAgence;
import static yvs.grh.UtilGrh.buildBeanDictionnaire;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author hp Elite 8300
 */
public class UtilTiers {
    
    public static YvsBaseTiers buildTiers(Tiers t) {
        YvsBaseTiers r = new YvsBaseTiers();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeTiers(t.getCodeTiers());
            r.setNom(t.getNom());
            r.setPrenom(t.getPrenom());
        }
        return r;
    }
    
    public static Tiers buildSimpleBeanTiers(YvsBaseTiers t) {
        Tiers r = new Tiers();
        if (t != null) {
            r.setId(t.getId());
            r.setCodeTiers(t.getCodeTiers());
            r.setNom(t.getNom());
            r.setPrenom(t.getPrenom());
            r.setSite(t.getSite());
            r.setNom_prenom(t.getNom_prenom());
            r.setBp(t.getBp());
            r.setLogo(t.getLogo());
            r.setTelephone(t.getTel());
            r.setCodePostal(t.getCodePostal());
            r.setCivilite(t.getCivilite());
            r.setAdresse(t.getAdresse());
            r.setEmail(t.getEmail());
            r.setClient(t.getClient());
            r.setFournisseur(t.getFournisseur());
            r.setRepresentant(t.getRepresentant());
            r.setSociete(t.getStSociete());
            r.setResponsable(t.getResponsable());
            r.setPersonnel(t.getPersonnel());
            r.setEmploye(t.getEmploye());
            r.setFabricant(t.getFabriquant());
            r.setCodeRation(t.getCodeRation());
            r.setVille((t.getVille() != null) ? buildBeanDictionnaire(t.getVille()) : new Dictionnaire());
            r.setPays((t.getPays() != null) ? buildBeanDictionnaire(t.getPays()) : new Dictionnaire());
            r.setSecteur((t.getSecteur() != null) ? buildBeanDictionnaire(t.getSecteur()) : new Dictionnaire());
            r.setAgence(t.getAgence() != null ? new Agence(t.getAgence().getId()) : new Agence());
        }
        return r;
    }
    
    public static Tiers buildBeanTiers(YvsBaseTiers t) {
        Tiers r = buildSimpleBeanTiers(t);
        if (t != null) {
            r.setCompteCollectif((t.getCompteCollectif() != null) ? t.getCompteCollectif().getId() : 0);
            r.setCompte((t.getCompteCollectif() != null) ? UtilCompta.buildBeanCompte(t.getCompteCollectif()) : new Comptes());
            if (t.getCategorieComptable() != null) {
                r.setCategorieComptable(new CategorieComptable(t.getCategorieComptable().getId(), t.getCategorieComptable().getCode(), t.getCategorieComptable().getNature()));
            }
//            r.setPlanRistourne(t.getRistourne() != null ? t.getRistourne().getId() : 0);
//            r.setPlanComission(t.getComission() != null ? t.getComission().getId() : 0);
            r.setModelDeRglt(t.getMdr() != null ? t.getMdr().getId().intValue() : 0);
            r.setContact(t.getContact() != null ? buildBeanContact(t.getContact()) : new Contact());
            r.setType(t.getType());
            r.setActif(t.getActif());
            r.setClients(t.getClients());
            r.setProfils(buildBeanListProfil(t));
            r.setUpdate(true);
        }
        return r;
    }
    
    public static List<Profil> buildBeanListProfil(YvsBaseTiers t) {
        List<Profil> list = new ArrayList<>();
        if (t != null) {
            list.add(new Profil(t.getId(), t.getCodeTiers(), t.getNom(), t.getPrenom(), t.getCompteCollectif(), Constantes.BASE_TIERS_TIERS, t.getActif(), 5, t.getId(), Constantes.BASE_TIERS_TIERS));
            if (t.getFournisseurs() != null) {
                for (YvsBaseFournisseur y : t.getFournisseurs()) {
                    list.add(new Profil(y.getId(), y.getCodeFsseur(), y.getNom(), y.getPrenom(), y.getCompte(), Constantes.BASE_TIERS_FOURNISSEUR, y.getActif(), 1, y.getId(), Constantes.BASE_TIERS_FOURNISSEUR));
                }
            }
            if (t.getCommerciaux() != null) {
                for (YvsComComerciale y : t.getCommerciaux()) {
                    list.add(new Profil(y.getId(), y.getCodeRef(), y.getNom(), y.getPrenom(), null, Constantes.BASE_TIERS_COMMERCIAL, y.getActif(), 2, t.getId(), Constantes.BASE_TIERS_TIERS));
                }
            }
            if (t.getEmployes() != null) {
                for (YvsGrhEmployes y : t.getEmployes()) {
                    list.add(new Profil(y.getId(), t.getCodeTiers(), y.getNom(), y.getPrenom(), y.getCompteCollectif(), Constantes.BASE_TIERS_EMPLOYE, y.getActif(), 3, t.getId(), Constantes.BASE_TIERS_TIERS, y.getMatricule()));
                }
            }
            if (t.getClients() != null) {
                for (YvsComClient y : t.getClients()) {
                    list.add(new Profil(y.getId(), y.getCodeClient(), y.getNom(), y.getPrenom(), y.getCompte(), Constantes.BASE_TIERS_CLIENT, y.getActif(), 4, y.getId(), Constantes.BASE_TIERS_CLIENT));
                }
            }
        }
        return list;
    }
    
    public static Contact buildBeanContact(YvsBaseTiersTelephone y) {
        Contact c = new Contact();
        if (y != null) {
            c.setId(y.getId() != null ? y.getId() : 0);
            c.setNumero(y.getNumero());
            c.setPrincipal((y.getPrincipal() != null) ? y.getPrincipal() : false);
            c.setUpdate(true);
        }
        return c;
    }
    
    public static List<Contact> buildBeanListContact(List<YvsBaseTiersTelephone> list) {
        List<Contact> r = new ArrayList<>();
        if (list != null) {
            for (YvsBaseTiersTelephone a : list) {
                r.add(buildBeanContact(a));
            }
        }
        return r;
    }
    
    public static TiersDocument buildBeanTiersDocument(YvsBaseTiersDocument y) {
        TiersDocument c = new TiersDocument();
        if (y != null) {
            c.setId(y.getId() != null ? y.getId() : 0);
            c.setTitre(y.getTitre());
            c.setFichier(y.getFichier());
            c.setType(y.getType());
            c.setNumero(y.getNumero());
            c.setExtension(y.getExtension());
            c.setDateSave(y.getDateSave());
            c.setTiers(new Tiers(y.getTiers().getId()));
        }
        return c;
    }
    
    public static YvsBaseTiersDocument buildTiersDocument(TiersDocument y, YvsUsersAgence ua) {
        YvsBaseTiersDocument c = new YvsBaseTiersDocument();
        if (y != null) {
            c.setId(y.getId());
            c.setTitre(y.getTitre());
            c.setFichier(y.getFichier());
            c.setType(y.getType());
            c.setNumero(y.getNumero());
            c.setExtension(y.getExtension());
            c.setDateSave(y.getDateSave());
            c.setTiers(new YvsBaseTiers(y.getTiers().getId()));
            c.setAuthor(ua);
            c.setDateUpdate(new Date());
        }
        return c;
    }
    
}
