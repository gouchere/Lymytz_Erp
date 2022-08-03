/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import static org.apache.taglibs.standard.resources.Resources.getMessage;
import yvs.dao.DaoInterfaceLocal;
import yvs.entity.grh.personnel.YvsGrhContratEmps;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.entity.grh.salaire.YvsGrhElementSalaire;
import yvs.entity.grh.salaire.YvsGrhBulletins;
import yvs.entity.grh.salaire.YvsGrhDetailBulletin;
import yvs.entity.grh.salaire.YvsGrhHeaderBulletin;
import yvs.entity.grh.salaire.YvsGrhOrdreCalculSalaire;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ classe qui permet d'analyser et d'evaluer une expression
 * mathématique
 */
public class ManagedSalaire implements Serializable {
//

    private Stack<String> pile;
    DaoInterfaceLocal dao;
    /**
     * Attribut pour le calcul du salaire
     */
    private Date debut, fin;

    public List<Options> erreurs;

    private List<Options> tempon;  //cette liste permet de garder les éléments déjà calculé afin de ne pas reprendre deux fois le calcul du même élément

    public String[] champ;
    public Object[] val;
    YvsSocietes currentScte;
    YvsUsersAgence author;
    YvsGrhOrdreCalculSalaire header;
    private int numero;
    private YvsParametreGrh paramGrh;
    private YvsGrhElementSalaire regleArrondi;

    public static Map<String, Integer> tabMotCle = new HashMap<>();

    public ManagedSalaire() {
    }

    public ManagedSalaire(YvsParametreGrh paramGrh, DaoInterfaceLocal dao, YvsUsersAgence author, YvsSocietes scte, YvsGrhOrdreCalculSalaire head) {
        this.dao = dao;
        this.author = author;
        this.currentScte = scte;
        this.header = head;
        this.paramGrh = paramGrh;
        pile = new Stack<>();
        erreurs = new ArrayList<>();
        tempon = new ArrayList<>();

        tabMotCle.put("SI", UniteLexicale.SI);
        tabMotCle.put("ALORS", UniteLexicale.ALORS);
        tabMotCle.put("SINON", UniteLexicale.SINON);
        tabMotCle.put("SINONSI", UniteLexicale.SINON);
        tabMotCle.put("FINSI", UniteLexicale.FINSI);
    }
    YvsGrhContratEmps contrat;

    public int nombreBulletinSave = 0;

    public synchronized YvsGrhBulletins createBulletin(YvsGrhContratEmps contrat, Date debut, Date fin) {
        this.contrat = contrat;
        this.debut = debut;
        this.fin = fin;
        //Charge la règle arrodi
        champ = new String[]{"societe"};
        val = new Object[]{currentScte};
        regleArrondi = (YvsGrhElementSalaire) dao.loadOneByNameQueries("YvsGrhElementSalaire.findRegleArrondi_", champ, val);
        if (contrat != null) {
            tempon.clear();
            //création du bulletin
            UtilFormules formule = new UtilFormules(paramGrh, debut, fin, dao, currentScte, contrat, contrat.getEmploye(), header);
            YvsGrhBulletins bs = formule.createBulletin(contrat, debut, fin);
            if (bs != null) {
                bs.setDateDebut(debut);
                bs.setDateFin(fin);
                bs.setContrat(contrat);
                bs.setRefBulletin(giveReference());
                bs.setEntete(header);
                saveBulletin(bs);
                nombreBulletinSave++;
                return bs;
            }
        } else {
            getMessage("Erreur, L'employé ne dispose d'aucun contrat", FacesMessage.SEVERITY_ERROR);
        }
        return null;
    }

    private boolean canSave(List<YvsGrhDetailBulletin> l) {
        double net = 0;
        for (YvsGrhDetailBulletin d : l) {
            if (d.getElement().getRetenue()) {
                net = net - d.getRetenuSalariale();
            } else {
                net = net + d.getMontantPayer();
            }
        }
//        return net > 0;

        return true;
    }

    double cumulArrondi = 0;
    private double montantSalaire;

    private void saveBulletin(YvsGrhBulletins bul) {
        if (bul != null) {
            if (canSave(bul.getListDetails())) {
                bul.setRefBulletin(giveReference());
                bul.setNumMois(month);
                bul.setSociete(currentScte);
                bul.setNumero((short) numero);
                bul.setStatut(Constantes.STATUT_EDITABLE);
                bul.setId(null);
                bul.setDateCalcul(new Date());
                bul.setDateSave(new Date());
                bul.setAuthor(author);
                List<YvsGrhDetailBulletin> le = new ArrayList<>(bul.getListDetails());
                bul.getListDetails().clear();
                bul = (YvsGrhBulletins) dao.save1(bul);     //Enregistre le bulletin
                //enregistre le header
                dao.save(buildHeaderBulletin(bul));
                cumulArrondi = 0;
                montantSalaire = 0;
                for (YvsGrhDetailBulletin e : le) {
                    e.setId(null);
                    e.setElement(e.getElement());
                    e.setYvsGrhBulletins(bul);
                    e.setAuthor(author);
                    cumulArrondi += Constantes.giveDecimalPart(e.getMontantPayer());
                    e.setMontantPayer(Constantes.arrondiA0Chiffre(e.getMontantPayer()));
                    e.setNowVisible(e.getMontantEmployeur() > 0 || e.getMontantPayer() > 0 || e.getRetenuSalariale() > 0);
                    if (e.getElement().getRegleConge()) {
                        champ = new String[]{"employe", "debut", "fin"};
                        val = new Object[]{contrat.getEmploye(), header.getDebutMois(), header.getFinMois()};
                        Long nb = (Long) dao.loadObjectByNameQueries("YvsGrhCongeEmps.findCongeAPaye", champ, val);
                        e.setNowVisible((nb != null) ? nb > 0 : false);
                    }
                    montantSalaire = montantSalaire + (e.getMontantPayer() - e.getRetenuSalariale());
                    try {
                        dao.save1(e, false);
                    } catch (Exception ex) {
                        Logger.getLogger(ManagedSalaire.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // enregistre les arrondi! en tant que charges patronale
                if (regleArrondi != null) {
                    YvsGrhDetailBulletin arrondi = new YvsGrhDetailBulletin();
                    arrondi.setElement(regleArrondi);
                    arrondi.setBase(0.0);
                    arrondi.setAuthor(author);
                    arrondi.setMontantEmployeur(Constantes.arrondi(cumulArrondi, 0));
                    arrondi.setMontantPayer(0);
                    arrondi.setNowVisible(cumulArrondi != 0);
                    arrondi.setQuantite(0.0);
                    arrondi.setRetenuSalariale(0);
                    arrondi.setTauxPatronal(0.0);
                    arrondi.setTauxSalarial(0.0);
                    arrondi.setYvsGrhBulletins(bul);
                    dao.save(arrondi);
                }
                //récupère l'aperçu du bulletin
                champ = new String[]{"bulletin"};
                val = new Object[]{bul};
            } else {
                erreurs.add(new Options("Non enregistré ", " "));
            }
        }

//        }
    }

    public YvsGrhHeaderBulletin buildHeaderBulletin(YvsGrhBulletins b) {
        YvsGrhHeaderBulletin head = new YvsGrhHeaderBulletin();
        head.setAuthor(author);
        head.setBulletin(b);
        head.setCategoriePro(contrat.getEmploye().getConvention().getCategorie().getCategorie());
        head.setEchelonPro(contrat.getEmploye().getConvention().getEchelon().getEchelon());
        head.setCni(contrat.getEmploye().getCni());
        head.setDateEmbauche(contrat.getDateDebut());
        head.setDepartement(contrat.getEmploye().getPosteActif().getDepartement().getIntitule());
        head.setIdService(contrat.getEmploye().getPosteActif().getDepartement());
        head.setEmail(contrat.getEmploye().getMail1() != null ? contrat.getEmploye().getMail1() : "" + "  " + contrat.getEmploye().getMail2() != null ? contrat.getEmploye().getMail2() : "");
        head.setMatricule(contrat.getEmploye().getMatricule());
        head.setMatriculeCnps(contrat.getEmploye().getMatriculeCnps());
        head.setPoste(contrat.getEmploye().getPosteActif().getIntitule());
        head.setIdPoste(contrat.getEmploye().getPosteActif());
        head.setTelephone(contrat.getEmploye().getTelephone1() != null ? contrat.getEmploye().getTelephone1() : "" + "  " + contrat.getEmploye().getTelephone2() != null ? contrat.getEmploye().getTelephone2() : "");
        head.setAdresseAgence(contrat.getEmploye().getAgence().getAdresse());
        head.setAdresseSociete(contrat.getEmploye().getAgence().getSociete().getAdressSiege());
        head.setCodeAgence(contrat.getEmploye().getAgence().getCodeagence());
        head.setAgence(contrat.getEmploye().getAgence());
        head.setCodeSociete(contrat.getEmploye().getAgence().getSociete().getCodeAbreviation());
        head.setDesignationAgence(contrat.getEmploye().getAgence().getDesignation());
        head.setNomEmploye(contrat.getEmploye().getNom() + " " + contrat.getEmploye().getPrenom());
        head.setCivilite(contrat.getEmploye().getCivilite());
        head.setAnciennete(Constantes.calculNbyear(b.getContrat().getDateDebut(), b.getDateFin()));
        head.setAdresseEmploye(contrat.getEmploye().getAdresse1());
        head.setModePaiement((contrat.getModePaiement() != null) ? contrat.getModePaiement().getDesignation() : null);
        return head;
    }
    String month;

    private String giveReference() {
        Calendar c = Calendar.getInstance();
        c.setTime(debut);
        month = ((c.get(Calendar.MONTH) + 1) > 9) ? "" + (c.get(Calendar.MONTH) + 1) : "0" + ((c.get(Calendar.MONTH) + 1));
        String year = c.get(Calendar.YEAR) + "";
        month += "-" + year;
        //cherche le dernier bulletin du  mois pour cet employé
        champ = new String[]{"mois", "contrat"};
        val = new Object[]{month, contrat};
        YvsGrhBulletins last = (YvsGrhBulletins) dao.loadOneByNameQueries("YvsGrhBulletins.findLastBp", champ, val);
        if (last != null) {
            numero = last.getNumero() + 1;
        } else {
            numero = 0;
        }
        return "BP/" + contrat.getEmploye().getNom() + "/" + month + "/0" + numero;
    }

//    private void traiterRetenue(YvsGrhBulletins bul) {
//        if (bul != null) {
//            List<YvsGrhDetailPrelevementEmps> aReporter = new ArrayList<>();
//            List<YvsGrhDetailPrelevementEmps> aConserver = new ArrayList<>();
//            if (bul.getListeRetenue() != null && montantSalaire <= 0) {
//                //equilibre les retenues
//                double q = 100;
//                if (paramGrh.getQuotiteCessible() > 0) {
//                    q = paramGrh.getQuotiteCessible();
//                }
//                double quotite = (montantSalaire * q / 100);    ////maximun à prélever
//                //calcul la valeur total des retenue
//                Collections.sort(bul.getListeRetenue(), new YvsGrhDetailPrelevementEmps());
//                double montantAReplacer = 0;
//                for (YvsGrhDetailPrelevementEmps pe : bul.getListeRetenue()) {
//                    if (quotite > 0) {
//                        if (quotite >= pe.getValeur()) {
//                            //pe.setValeur(0.0);
//                            quotite = (quotite - pe.getValeur());
//                            aConserver.add(pe);
//                        } else {
//                            double toReport = pe.getValeur() - quotite;
//                            pe.setValeur(quotite);
//                            aConserver.add(pe);
//                            YvsGrhDetailPrelevementEmps c = new YvsGrhDetailPrelevementEmps(pe);
//                            c.setId(null);
//                            c.setValeur(toReport);
//                            aReporter.add(c);
//                            quotite = 0;
//                        }
//                    }
//                }
//                bul.getListeRetenue().removeAll(aConserver);
//                bul.getListeRetenue().addAll(aReporter);
//                //reporter tout les élément au mois suivant la date en cours et et update ceux qu'on à modifier pour conserver
//                for (YvsGrhDetailPrelevementEmps d : aConserver) {
//                    dao.update(d);
//                }
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.MONTH, 1);
//                for (YvsGrhDetailPrelevementEmps d : bul.getListeRetenue()) {
//                    d.setDatePrelevement(c.getTime());
//                    dao.update(d);
//                }
//                //modifier les montants des retenues correspondant parmi les élément de salaire déjà enregistré
//
//            }
//        }
//    }
}
