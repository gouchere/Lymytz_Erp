/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.JDBC;
import yvs.dao.salaire.service.Constantes;
import static yvs.dao.salaire.service.Constantes.dfs;
import yvs.dao.services.DateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseDepots;
import yvs.entity.base.YvsBaseModelReglement;
import yvs.entity.base.YvsBasePointVente;
import yvs.entity.commercial.client.YvsComClient;
import yvs.entity.commercial.commission.YvsComCommissionVente;
import yvs.entity.commercial.creneau.YvsComCreneauHoraireUsers;
import yvs.entity.commercial.creneau.YvsComCreneauPoint;
import yvs.entity.commercial.stock.YvsComReservationStock;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaJournaux;
import yvs.entity.compta.saisie.YvsComptaContentJournalFactureVente;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.workflow.YvsWorkflowValidFactureVente;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsNiveauAcces;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_doc_ventes")
@NamedQueries({
    @NamedQuery(name = "YvsComDocVentes.findAll_", query = "SELECT y FROM YvsComDocVentes y ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findAll", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByDepot", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauDepot.depot = :depot"),
    @NamedQuery(name = "YvsComDocVentes.findByCreneauHoraire", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauDepot = :creneau"),
    @NamedQuery(name = "YvsComDocVentes.findByUsers", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.users = :users"),
    @NamedQuery(name = "YvsComDocVentes.findByUsersDate", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.users = :users AND y.enteteDoc.dateEntete = :dateEntete"),
    @NamedQuery(name = "YvsComDocVentes.findByUsersDateHeure", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.users = :users AND y.enteteDoc.dateEntete = :dateEntete AND y.heureDoc = :heureDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByUsersDates", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.users = :users AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComDocVentes.findByCreneauEmploye", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau = :crenauHoraire"),
    @NamedQuery(name = "YvsComDocVentes.findStatutRegById", query = "SELECT y.statutRegle FROM YvsComDocVentes y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocVentes.findById", query = "SELECT y FROM YvsComDocVentes y JOIN FETCH y.enteteDoc JOIN FETCH y.enteteDoc.agence JOIN FETCH y.enteteDoc.creneau JOIN FETCH y.enteteDoc.creneau.creneauPoint.point WHERE y.id = :id"),
    @NamedQuery(name = "YvsComDocVentes.findByIds", query = "SELECT y FROM YvsComDocVentes y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComDocVentes.findByParent", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie"),
    @NamedQuery(name = "YvsComDocVentes.countByParent", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.documentLie = :documentLie"),
    @NamedQuery(name = "YvsComDocVentes.findBLVByParent", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = 'BLV'"),
    @NamedQuery(name = "YvsComDocVentes.findBRLByParent", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = 'BRL'"),
    @NamedQuery(name = "YvsComDocVentes.countBLVByParent", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = 'BLV'"),
    @NamedQuery(name = "YvsComDocVentes.findNotBLVByParent", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc != 'BLV'"),
    @NamedQuery(name = "YvsComDocVentes.findNotBLVBRLByParent", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc NOT IN ('BLV', 'BRL')"),
    @NamedQuery(name = "YvsComDocVentes.countNotBLVByParent", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc != 'BLV'"),
    @NamedQuery(name = "YvsComDocVentes.countNotBLVBRLByParent", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc NOT IN ('BLV', 'BRL')"),
    @NamedQuery(name = "YvsComDocVentes.findByParentTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByParentsTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE (y.documentLie = :documentLie1 OR y.documentLie.documentLie = :documentLie2) AND y.typeDoc = :typeDoc ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByParentTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByParentTypeDocStatut", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.typeDoc = :typeDoc AND y.statut = :statut"),
    @NamedQuery(name = "YvsComDocVentes.findByNumPiece", query = "SELECT y FROM YvsComDocVentes y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComDocVentes.findByNumDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.numDoc = :numDoc AND  y.enteteDoc.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocVentes.findIdByNumeroExterne", query = "SELECT y.id FROM YvsComDocVentes y WHERE y.numeroExterne = :numExterne AND  y.enteteDoc.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocVentes.findByNumeroExterne", query = "SELECT y FROM YvsComDocVentes y WHERE y.numeroExterne = :numExterne AND  y.enteteDoc.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocVentes.findByReference", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findReferenceByReference", query = "SELECT y.numDoc FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.numDoc >:numDoc AND y.numDoc <:numDoc1 ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteReference", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.typeDoc = :typeDoc AND y.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteParentReference", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.typeDoc = :typeDoc AND y.documentLie.numDoc LIKE :numDoc ORDER by y.numDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByTypeDocEntete", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.agence.societe = :societe AND y.enteteDoc = :entete ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocVentes.findByTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.agence.societe = :societe"),
    @NamedQuery(name = "YvsComDocVentes.findByDateTypeDocCount", query = "SELECT COUNT(y)FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.agence.societe = :societe AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComDocVentes.findByDateTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.agence.societe = :societe AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "YvsComDocVentes.findFactureByDate", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.enteteDoc.agence.societe = :societe AND y.enteteDoc.dateEntete = :date ORDER BY y.numDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByClientTypeDocCount", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client = :client"),
    @NamedQuery(name = "YvsComDocVentes.findByClientTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client = :client ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByTiersTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client.tiers = :tiers ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByTiersTypeStatuts", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client.tiers = :tiers AND y.statut IN :statut AND y.statutRegle IN :statutRegle ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteClient", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND y.typeDoc = :typeDoc AND y.client = :client ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteClientStatut", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND y.typeDoc = :typeDoc AND y.client = :client AND y.statut = :statut ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByTypeDocUsers", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.creneau.users = :users ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByStatut", query = "SELECT y FROM YvsComDocVentes y WHERE y.statut = :statut"),
    @NamedQuery(name = "YvsComDocVentes.factureByNotLivreAndWait", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.statut = 'V' AND y.statutLivre != 'L' AND y.livraisonAuto = TRUE"),
    @NamedQuery(name = "YvsComDocVentes.findByPrefixe", query = "SELECT y FROM YvsComDocVentes y WHERE y.nomClient = :prefixe"),
    @NamedQuery(name = "YvsComDocVentes.findBySupp", query = "SELECT y FROM YvsComDocVentes y WHERE y.supp = :supp"),
    @NamedQuery(name = "YvsComDocVentes.findByLivreur", query = "SELECT y FROM YvsComDocVentes y WHERE y.livreur = :livreur"),
    @NamedQuery(name = "YvsComDocVentes.findByNomClient", query = "SELECT y FROM YvsComDocVentes y WHERE UPPER(y.nomClient) = UPPER(:nomClient) AND y.enteteDoc.agence.societe = :societe"),

    @NamedQuery(name = "YvsComDocVentes.findEnteteById", query = "SELECT y.enteteDoc FROM YvsComDocVentes y WHERE y.id  = :id"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.findIdByEnteteTypeDoc", query = "SELECT y.id FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.findByEnteteNotTypeDoc", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND y.typeDoc != :typeDoc"),

    @NamedQuery(name = "YvsComDocVentes.findByHeader", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc = :entete AND (y.typeDoc = :typeDoc OR y.typeDoc=:typeDoc2) ORDER BY y.numDoc"),
    @NamedQuery(name = "YvsComDocVentes.findBLNonLivre", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie =:facture AND y.typeDoc =:typeDoc AND y.statut!=:statut"),
    @NamedQuery(name = "YvsComDocVentes.countFactureNonLivreOrNonPayeByHeader", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE (y.statut != 'A' AND (y.statut != :statut OR y.statutLivre != :statutLivre OR y.statutRegle != :statutRegle)) AND y.typeDoc = :typeDoc AND y.enteteDoc = :header"),
    @NamedQuery(name = "YvsComDocVentes.countDocByHeaderAndType", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE (y.typeDoc =:type1 OR y.typeDoc=:type2) AND y.enteteDoc=:header"),

    @NamedQuery(name = "YvsComDocVentes.countDocByNumDoc", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence.societe=:societe AND y.numDoc=:numDoc AND y.id!=:id"),
    @NamedQuery(name = "YvsComDocVentes.findByParentStatut", query = "SELECT y FROM YvsComDocVentes y WHERE y.documentLie = :documentLie AND y.statut = :statut"),
    @NamedQuery(name = "YvsComEnteteDocVente.findByUsersActif", query = "SELECT y FROM YvsComEnteteDocVente y WHERE y.creneau.users = :users AND y.cloturer=FALSE ORDER BY y.dateEntete DESC"),
    @NamedQuery(name = "YvsComDocVentes.countByClient", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.client = :client"),

    @NamedQuery(name = "YvsComDocVentes.countByClients", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.client.id IN :clients"),
    @NamedQuery(name = "YvsComDocVentes.findByClient", query = "SELECT y FROM YvsComDocVentes y WHERE y.client = :client ORDER BY y.dateLivraison, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.lastByClient", query = "SELECT y FROM YvsComDocVentes y WHERE y.client = :client ORDER BY y.enteteDoc.dateEntete DESC"),

    @NamedQuery(name = "YvsComDocVentes.findByClientNonRegle", query = "SELECT y FROM YvsComDocVentes y WHERE y.client = :client AND y.statutRegle!=:statutRegle AND y.statut='V' AND y.typeDoc='FV' ORDER BY y.enteteDoc.dateEntete ASC"),
    @NamedQuery(name = "YvsComDocVentes.findByClientNonRegleByAgence", query = "SELECT y FROM YvsComDocVentes y WHERE y.client = :client AND y.statutRegle!=:statutRegle AND y.statut='V' AND y.typeDoc='FV' AND y.enteteDoc.agence = :agence ORDER BY y.enteteDoc.dateEntete ASC"),
    @NamedQuery(name = "YvsComDocVentes.findByClients", query = "SELECT y FROM YvsComDocVentes y WHERE y.client.id IN :clients ORDER BY y.dateLivraison, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findByClientDepotInvalid", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = 'BLV' AND y.depotLivrer = :depot AND y.client = :client AND y.statut != 'V' ORDER BY y.dateLivraison, y.heureDoc DESC"),

    @NamedQuery(name = "YvsComDocVentes.countFactureNoClotureByEntete", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.enteteDoc = :entete AND y.cloturer = :cloturer"),
    @NamedQuery(name = "YvsComDocVentes.countFactureImpayeByClient", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.client = :client AND y.statutRegle != 'P' AND y.statut NOT IN ('A','E')"),
    @NamedQuery(name = "YvsComDocVentes.findFactureImpayeByClient", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.client = :client AND y.statutRegle != 'P' AND y.statut = 'V' ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findImpayeByClientDates", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client = :client AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' AND y.statut = 'V' ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.findImpayeByClientDatesAgence", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.client = :client AND y.enteteDoc.agence = :agence AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.statutRegle != 'P' AND y.statut = 'V' ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),

    @NamedQuery(name = "YvsComDocVentes.findFactureImpayeByVendeur", query = "SELECT y FROM YvsComDocVentes y WHERE y.typeDoc = 'FV' AND y.enteteDoc.creneau.users = :vendeur AND y.statutRegle != 'P' AND y.statut = 'V' ORDER BY y.enteteDoc.dateEntete, y.heureDoc DESC"),
    @NamedQuery(name = "YvsComDocVentes.countByVendeur", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.enteteDoc.creneau.users = :vendeur"),
    @NamedQuery(name = "YvsComDocVentes.countByVendeurNature", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.typeDoc = :typeDoc AND y.nature = :nature AND y.enteteDoc.creneau.users = :vendeur"),

    @NamedQuery(name = "YvsComDocVentes.findByStatutDates", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.findEnteteByStatutDates", query = "SELECT DISTINCT y.enteteDoc FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),

    @NamedQuery(name = "YvsComDocVentes.countByEnteteStatut", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.statut = :statut AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.countByEnteteNotStatutsLivre", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.statut = :statut AND y.statutLivre != :statutLivre AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.countByEnteteNotStatutsRegle", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.statut = :statut AND y.statutRegle != :statutRegle AND y.typeDoc = :typeDoc"),
    @NamedQuery(name = "YvsComDocVentes.countByEnteteNotAllStatuts", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc = :enteteDoc AND y.statut != :statut AND y.statutRegle != :statutRegle AND y.typeDoc = :typeDoc"),

    @NamedQuery(name = "YvsComDocVentes.countByDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutPDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countBy2StatutPDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND (y.typeDoc = :type1 OR y.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComDocVentes.countBy2StatutPDatesLivre", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statut = :statut AND y.dateLivraison BETWEEN :dateDebut AND :dateFin AND (y.typeDoc = :type1 OR y.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutRDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statutRegle = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutLDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statutLivre = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND (y.statut = :statut OR y.statutLivre = :statut OR y.statutRegle = :statut) AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutPDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countBy2StatutPDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND (y.typeDoc = :type1 OR y.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComDocVentes.countBy2StatutPDatesLivreAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statut = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND (y.typeDoc = :type1 OR y.typeDoc = :type2)"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutLDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statutLivre = :statut AND y.dateLivraison BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutRDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statutRegle = :statut AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByStatutDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND (y.statut = :statut OR y.statutLivre = :statut OR y.statutRegle = :statut) AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByNotStatutDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND (NOT(y.statut = :statut OR y.statutLivre = :statut OR y.statutRegle = :statut)) AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByNotStatutDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND (NOT(y.statut = :statut OR y.statutLivre = :statut OR y.statutRegle = :statut)) AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByRetardLDates", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.agence.societe = :societe AND y.statutLivre != 'L' AND y.dateLivraisonPrevu < :dateFin AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countByRetardLDatesAgence", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point.agence = :agence AND y.statutLivre != 'L' AND y.dateLivraisonPrevu < :dateFin AND y.enteteDoc.dateEntete BETWEEN :dateDebut AND :dateFin AND y.typeDoc = :type"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderN", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc != :typeDoc  AND y.enteteDoc=:header"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderNLivre", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE y.enteteDoc=:header AND y.statutLivre!=:statutLivre AND y.typeDoc IN :typesDocs"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeader", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  (y.typeDoc = :typesDoc OR y.typeDoc = :typesDoc1)  AND y.enteteDoc=:header AND y.statut='V'"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeader1", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V'"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderL", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V' AND y.statutLivre=:statutL"),
    @NamedQuery(name = "YvsComDocVentes.countAllFacturesByHeaderR", query = "SELECT COUNT(y) FROM YvsComDocVentes y WHERE  y.typeDoc = :typesDoc AND y.enteteDoc=:header AND y.statut='V' AND y.statutRegle=:statutR"),
    @NamedQuery(name = "YvsComDocVentes.countClientByHeader", query = "SELECT COUNT(DISTINCT y.client) FROM YvsComDocVentes y WHERE  y.typeDoc IN :typesDocs  AND y.enteteDoc=:header"),
    @NamedQuery(name = "YvsComDocVentes.findIdAndTypeDocById", query = "SELECT y.id, y.typeDoc FROM YvsComDocVentes y WHERE  y.id=:id"),
    @NamedQuery(name = "YvsComDocVentes.findCmdeNonServie", query = "SELECT y FROM YvsComDocVentes y WHERE y.enteteDoc.creneau.creneauPoint.point=:point AND y.typeDoc IN :typesDocs  AND y.enteteDoc!=:header AND y.statut=:statut AND y.enteteDoc.agence=:agence AND (y.statutLivre!=:statutLivre OR y.statutRegle!=:statutRegle)")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComDocVentes extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_doc_ventes_id_seq", name = "yvs_doc_ventes_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_doc_ventes_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Size(max = 2147483647)
    @Column(name = "numero_externe")
    private String numeroExterne;
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "num_doc")
    private String numDoc;
    @Size(max = 2147483647)
    @Column(name = "type_doc")
    private String typeDoc;
    @Size(max = 2147483647)
    @Column(name = "nature")
    private String nature = Constantes.VENTE;
    @Column(name = "comptabilise")
    private Boolean comptabilise;
    @Column(name = "statut")
    private String statut;
    @Column(name = "statut_livre")
    private String statutLivre;
    @Column(name = "statut_regle")
    private String statutRegle;
    @Size(max = 2147483647)
    @Column(name = "nom_client")
    private String nomClient;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "livraison_auto")
    private Boolean livraisonAuto;
    @Column(name = "montant_avance")
    private Double montantAvance;
    @Column(name = "commision")
    private Double commision;
    @Column(name = "heure_doc")
    @Temporal(TemporalType.TIME)
    private Date heureDoc;
    @Size(max = 2147483647)
    @Column(name = "action")
    private String action;
    @Column(name = "date_valider")
    @Temporal(TemporalType.DATE)
    private Date dateValider;
    @Column(name = "date_annuler")
    @Temporal(TemporalType.DATE)
    private Date dateAnnuler;
    @Column(name = "date_solder")
    @Temporal(TemporalType.DATE)
    private Date dateSolder;
    @Column(name = "date_livraison")
    @Temporal(TemporalType.DATE)
    private Date dateLivraison;
    @Column(name = "date_livraison_prevu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLivraisonPrevu;
    @Column(name = "cloturer")
    private Boolean cloturer;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;
    @Column(name = "date_cloturer")
    @Temporal(TemporalType.DATE)
    private Date dateCloturer;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "notes")
    private String notes;

    @OneToOne(mappedBy = "facture", fetch = FetchType.LAZY)
    private YvsComDocVentesInformations information;
    @OneToOne(mappedBy = "facture", fetch = FetchType.LAZY)
    private YvsComptaContentJournalFactureVente pieceContenu;

    @JoinColumn(name = "tiers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient tiers;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComClient client;
    @JoinColumn(name = "valider_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers validerBy;
    @JoinColumn(name = "annuler_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers annulerBy;
    @JoinColumn(name = "depot_livrer", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseDepots depotLivrer;
    @JoinColumn(name = "tranche_livrer", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhTrancheHoraire trancheLivrer;
    @JoinColumn(name = "model_reglement", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModelReglement modelReglement;
    @JoinColumn(name = "livreur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers livreur;
    @JoinColumn(name = "adresse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsDictionnaire adresse;
    @JsonManagedReference
    @JoinColumn(name = "document_lie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes documentLie;
    @JoinColumn(name = "entete_doc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComEnteteDocVente enteteDoc;
    @JoinColumn(name = "categorie_comptable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCategorieComptable categorieComptable;
    @JoinColumn(name = "cloturer_by", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers cloturerBy;
    @JoinColumn(name = "operateur", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers operateur;

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @JsonBackReference
    @OneToMany(mappedBy = "docVente", fetch = FetchType.LAZY)
    private List<YvsComContenuDocVente> contenus;
    @JsonBackReference
    @OneToMany(mappedBy = "vente", fetch = FetchType.LAZY)
    private List<YvsComptaCaissePieceVente> reglements;
    @JsonBackReference
    @OneToMany(mappedBy = "docVente", fetch = FetchType.LAZY)
    private List<YvsComRistourneDocVente> ristournes;
    @JsonBackReference
    @OneToMany(mappedBy = "docVente", fetch = FetchType.LAZY)
    private List<YvsComRemiseDocVente> remises;
    @JsonBackReference
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY)
    private List<YvsComMensualiteFactureVente> mensualites;
    @JsonBackReference
    @OneToMany(mappedBy = "docVente", fetch = FetchType.LAZY)
    private List<YvsComCoutSupDocVente> couts;
    @JsonBackReference
    @OneToMany(mappedBy = "factureVente", fetch = FetchType.LAZY)
    private List<YvsWorkflowValidFactureVente> etapesValidations;
    @JsonBackReference
    @OneToMany(mappedBy = "documentLie", fetch = FetchType.LAZY)
    private List<YvsComDocVentes> documents;
    @JsonBackReference
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY)
    private List<YvsComCommercialVente> commerciaux;
    @JsonBackReference
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY)
    private List<YvsComCommissionVente> commissions;

    @Transient
    private YvsComptaJournaux journal;
    @Transient
    private List<YvsComReservationStock> reservations;
    @Transient
    private YvsNiveauAcces niveau = new YvsNiveauAcces();
    @Transient
    private boolean selectActif;
    @Transient
    private boolean charger;
    @Transient
    private Boolean consigner;
    @Transient
    private Date dateConsigner;
    @Transient
    private boolean synchroniser;
//    @Transient
//    private boolean update;
    @Transient
    private double total;
    @Transient
    private boolean errorComptabilise;
    @Transient
    private boolean comptabilised;
    @Transient
    private boolean new_;
    @Transient
    private boolean int_;
    @Transient
    private boolean facture;
    @Transient
    private String onfacture;
    @Transient
    private String reference;
    @Transient
    private String nom_client;
    @Transient
    private String libEtapes;
    @Transient
    private String libelleStatut;
    @Transient
    private String maDateSave;
    @Transient
    private String oldReference;
    @Transient
    private double montantHT, montantTaxe, montantTTC, montantRemise, montantRemises, montantTotal, montantRistourne, montantCommission, montantCS,
            montantResteApayer, montantTaxeR, montantPlanifier, montantNetAPayer, montantAvoir, montantAvanceAvoir;
    @Transient
    private boolean livrer = true; //Utilisé sur l'app de caisse

    public YvsComDocVentes() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        etapesValidations = new ArrayList<>();
        reservations = new ArrayList<>();
        commerciaux = new ArrayList<>();
        commissions = new ArrayList<>();
        remises = new ArrayList<>();
        mensualites = new ArrayList<>();
        ristournes = new ArrayList<>();
        contenus = new ArrayList<>();
        reglements = new ArrayList<>();
        documents = new ArrayList<>();
        couts = new ArrayList<>();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public YvsComDocVentes(Long id) {
        this();
        this.id = id;
    }

    public YvsComDocVentes(Long id, double montantTTC) {
        this();
        this.id = id;
        this.montantTTC = montantTTC;
    }

    public YvsComDocVentes(Long id, String numDoc) {
        this(id);
        this.numDoc = numDoc;
    }

    public YvsComDocVentes(String numDoc, String typeDoc) {
        this(0L);
        this.numDoc = numDoc;
        this.typeDoc = typeDoc;
    }

    public YvsComDocVentes(Long id, String numDoc, String typeDoc, String statut) {
        this(id, numDoc);
        this.typeDoc = typeDoc;
    }

    public YvsComDocVentes(Long id, String numDoc, String statut) {
        this(id, numDoc);
        this.statut = statut;
    }

    public YvsComDocVentes(Long id, String numDoc, String statut, String statutLivre, String statutRegle) {
        this(id, numDoc, statut);
        this.statutLivre = statutLivre;
        this.statutRegle = statutRegle;
    }

    public YvsComDocVentes(Long id, String numDoc, String statut, YvsComEnteteDocVente enteteDoc) {
        this(id, numDoc, statut);
        this.enteteDoc = enteteDoc;
    }

    public YvsComDocVentes(Long id, YvsComDocVentes y) {
        this(id, y, y.getEnteteDoc());
    }

    public YvsComDocVentes(YvsComDocVentes y) {
        this(y.getId(), y, y.getEnteteDoc());
    }

    public YvsComDocVentes(YvsComDocVentes y, YvsComEnteteDocVente e) {
        this(y.getId(), y, e);
    }

    public YvsComDocVentes(Long id, YvsComDocVentes y, YvsComEnteteDocVente e) {
        this.id = id;
        this.enteteDoc = e;
        this.annulerBy = y.getAnnulerBy();
        this.cloturer = y.getCloturer();
        this.commision = y.getCommision();
        this.dateAnnuler = y.getDateAnnuler();
        this.dateCloturer = y.getDateCloturer();
        this.dateLivraison = y.getDateLivraison();
        this.dateLivraisonPrevu = y.getDateLivraisonPrevu();
        this.comptabilise = y.getComptabilise();
        this.dateSave = y.getDateSave();
        this.dateSolder = y.getDateSolder();
        this.dateUpdate = y.getDateUpdate();
        this.dateValider = y.getDateValider();
        this.depotLivrer = y.getDepotLivrer();
        this.description = y.getDescription();
        this.heureDoc = y.getHeureDoc();
        this.livraisonAuto = y.getLivraisonAuto();
        this.livreur = y.getLivreur();
        this.modelReglement = y.getModelReglement();
        this.montantAvance = y.getMontantAvance();
        this.nomClient = y.getNomClient();
        this.numDoc = y.getNumDoc();
        this.numeroExterne = y.getNumeroExterne();
        this.numPiece = y.getNumPiece();
        this.tiers = y.getTiers();
        this.trancheLivrer = y.getTrancheLivrer();
        this.typeDoc = y.getTypeDoc();
        this.statut = y.getStatut();
        this.statutLivre = y.getStatutLivre();
        this.statutRegle = y.getStatutRegle();
        this.supp = y.getSupp();
        this.validerBy = y.getValiderBy();
        this.adresse = y.getAdresse();
        this.author = y.getAuthor();
        this.telephone = y.getTelephone();
        this.documentLie = y.getDocumentLie();
        this.client = y.getClient();
        this.categorieComptable = y.getCategorieComptable();
        this.cloturerBy = y.getCloturerBy();
        this.ristournes = new ArrayList<>(y.getRistournes());
        this.remises = new ArrayList<>(y.getRemises());
        this.mensualites = new ArrayList<>(y.getMensualites());
        this.reglements = new ArrayList<>(y.getReglements());
        this.couts = new ArrayList<>(y.getCouts());
        this.contenus = new ArrayList<>(y.getContenus());
        this.documents = new ArrayList<>(y.getDocuments());
        this.etapesValidations = new ArrayList<>(y.getEtapesValidations());
        this.commerciaux = new ArrayList<>(y.getCommerciaux());
        this.reservations = new ArrayList<>(y.getReservations());
        this.selectActif = y.isSelectActif();
        this.consigner = y.getConsigner();
        this.dateConsigner = y.getDateConsigner();
//        this.update = y.update;
        this.new_ = y.isNew_();
        this.int_ = y.isInt_();
        this.comptabilised = y.isComptabilised();
        this.facture = y.isFacture();
        this.onfacture = y.getOnfacture();
        this.reference = y.getReference();
        this.nom_client = y.getNom_client();
        this.etapeValide = y.getEtapeValide();
        this.etapeTotal = y.getEtapeTotal();
        this.libEtapes = y.getLibEtapes();
        this.operateur = y.getOperateur();
        this.idDistant = y.getIdDistant();
    }

    public Boolean getComptabilise() {
        return comptabilise != null ? comptabilise : false;
    }

    public void setComptabilise(Boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getCommision() {
        return commision != null ? commision : 0;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public void setCommision(Double commision) {
        this.commision = commision;
    }

    @XmlTransient
    @JsonIgnore
    public String getNom_client() {
        nom_client = "";
        if (getNomClient().trim().length() > 0 && getClient() != null) {
            if (getNomClient().equals(getClient().getNom_prenom())) {
                nom_client = getClient().getNom_prenom();
            } else {
                nom_client = getNomClient() + " [" + getClient().getCodeClient() + "]";
            }
        } else if (getClient() != null) {
            nom_client = getClient().getNom_prenom();
        } else if (getNomClient().trim().length() > 0) {
            nom_client = getNomClient();
        }
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public String getLibEtapes() {
        libEtapes = "Etp. " + getEtapeValide() + " / " + getEtapeTotal();
        return libEtapes;
    }

    public void setLibEtapes(String libEtapes) {
        this.libEtapes = libEtapes;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public Boolean getLivraisonAuto() {
        return livraisonAuto != null ? livraisonAuto : false;
    }

    public void setLivraisonAuto(Boolean livraisonAuto) {
        this.livraisonAuto = livraisonAuto;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraisonPrevu() {
        return dateLivraisonPrevu != null ? dateLivraisonPrevu : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraisonPrevu(Date dateLivraisonPrevu) {
        this.dateLivraisonPrevu = dateLivraisonPrevu;
    }

    public String getStatutLivre() {
        return statutLivre != null ? statutLivre.trim().length() > 0 ? statutLivre : "W" : "W";
    }

    public void setStatutLivre(String statutLivre) {
        this.statutLivre = statutLivre;
    }

    public String getStatutRegle() {
        return statutRegle != null ? statutRegle.trim().length() > 0 ? statutRegle : "W" : "W";
    }

    public void setStatutRegle(String statutRegle) {
        this.statutRegle = statutRegle;
    }

    @XmlTransient
    @JsonIgnore
    public String getReference() {
        reference = getNumDoc() + " / ";
        if (client != null ? client.getId() > 0 : false) {
            reference += client.getNom_prenom() + " / ";
        }
        if (enteteDoc != null ? enteteDoc.getId() > 0 : false) {
            reference += new SimpleDateFormat("dd MMM yyyy").format(enteteDoc.getDateEntete());
        }
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    @JsonIgnore
    public String getOnfacture() {
        onfacture = Constantes.ETAT_VALIDE;
        if (!isFacture()) {
            onfacture = Constantes.ETAT_ATTENTE;
            if (documents != null ? !documents.isEmpty() : false) {
                onfacture = Constantes.ETAT_ENCOURS;
                for (YvsComDocVentes d : documents) {
                    if (d.getStatut().equals(Constantes.ETAT_VALIDE)) {
                        onfacture = Constantes.ETAT_VALIDE;
                        break;
                    }
                }
            }
        }
        return onfacture;
    }

    public void setOnfacture(String onfacture) {
        this.onfacture = onfacture;
    }

    public boolean isFacture() {
        facture = (typeDoc != null ? (typeDoc.equals("FV") ? true : (typeDoc.equals("FAV") ? true : (typeDoc.equals("FRV")))) : false);
        return facture;
    }

    public void setFacture(boolean facture) {
        this.facture = facture;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isUpdate() {
        return getId() > 0;
    }

//    public void setUpdate(boolean update) {
//        this.update = update;
//    }
    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isInt_() {
        return int_;
    }

    public void setInt_(boolean int_) {
        this.int_ = int_;
    }

    public Double getMontantAvance() {
        return montantAvance != null ? montantAvance : 0;
    }

    public void setMontantAvance(Double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getHeureDoc() {
        return heureDoc != null ? heureDoc : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setHeureDoc(Date heureDoc) {
        this.heureDoc = heureDoc;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public String getNumDoc() {
        return numDoc != null ? numDoc : "";
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getNature() {
        return nature != null ? nature.trim().length() > 0 ? nature : Constantes.VENTE : Constantes.VENTE;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getStatut() {
        return statut != null ? statut.trim().length() > 0 ? statut : Constantes.ETAT_EDITABLE : Constantes.ETAT_EDITABLE;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNomClient() {
        return nomClient != null ? nomClient : "";
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateValider() {
        return dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateValider(Date dateValider) {
        this.dateValider = dateValider;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateAnnuler() {
        return dateAnnuler;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateAnnuler(Date dateAnnuler) {
        this.dateAnnuler = dateAnnuler;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSolder() {
        return dateSolder;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSolder(Date dateSolder) {
        this.dateSolder = dateSolder;
    }

    public Boolean getConsigner() {
        consigner = reservations != null ? !reservations.isEmpty() : false;
        return consigner != null ? consigner : false;
    }

    public void setConsigner(Boolean consigner) {
        this.consigner = consigner;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateConsigner() {
        if (getConsigner()) {
            dateConsigner = reservations.get(0).getDateReservation();
        }
        return dateConsigner;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateConsigner(Date dateConsigner) {
        this.dateConsigner = dateConsigner;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateLivraison() {
        return dateLivraison != null ? dateLivraison : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Boolean getCloturer() {
        return cloturer != null ? cloturer : false;
    }

    public void setCloturer(Boolean cloturer) {
        this.cloturer = cloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateCloturer() {
        return dateCloturer;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateCloturer(Date dateCloturer) {
        this.dateCloturer = dateCloturer;
    }

    public double getMontantHT() {
        montantHT = getMontantTTC() - getMontantTaxe();
        return montantHT;
    }

    public void setMontantHT(double montantHT) {
        this.montantHT = montantHT;
    }

    public double getMontantPlanifier() {
        return montantPlanifier;
    }

    public void setMontantPlanifier(double montantPlanifier) {
        this.montantPlanifier = montantPlanifier;
    }

    public double getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(double montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public double getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(double montantTTC) {
        this.montantTTC = montantTTC;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public double getMontantRemises() {
        return montantRemises;
    }

    public void setMontantRemises(double montantRemises) {
        this.montantRemises = montantRemises;
    }

    public double getMontantAvoir() {
        return montantAvoir;
    }

    public void setMontantAvoir(double montantAvoir) {
        this.montantAvoir = montantAvoir;
    }

    public double getMontantAvanceAvoir() {
        return montantAvanceAvoir;
    }

    public void setMontantAvanceAvoir(double montantAvanceAvoir) {
        this.montantAvanceAvoir = montantAvanceAvoir;
    }

    public double getMontantTotal() {
        montantTotal = getMontantTTC() + getMontantCS() - getMontantRemises() - getMontantAvoir() + getMontantAvanceAvoir();
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantRistourne() {
        return montantRistourne;
    }

    public void setMontantRistourne(double montantRistourne) {
        this.montantRistourne = montantRistourne;
    }

    public double getMontantCommission() {
        return montantCommission;
    }

    public void setMontantCommission(double montantCommission) {
        this.montantCommission = montantCommission;
    }

    public double getMontantCS() {
        return montantCS;
    }

    public void setMontantCS(double montantCS) {
        this.montantCS = montantCS;
    }

    public double getMontantResteApayer() {
        montantResteApayer = getMontantNetAPayer() - getMontantAvance();
        return montantResteApayer;
    }

    public void setMontantResteApayer(double montantResteApayer) {
        this.montantResteApayer = montantResteApayer;
    }

    public double getMontantResteAPlanifier() {
        montantResteApayer = getMontantNetAPayer() - getMontantPlanifier();
        return montantResteApayer;
    }

    public double getMontantNetAPayer() {
        montantNetAPayer = getMontantTotal();
        return montantNetAPayer;
    }

    public void setMontantNetAPayer(double montantNetAPayer) {
        this.montantNetAPayer = montantNetAPayer;
    }

    public double getMontantTaxeR() {
        return montantTaxeR;
    }

    public void setMontantTaxeR(double montantTaxeR) {
        this.montantTaxeR = montantTaxeR;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isErrorComptabilise() {
        return errorComptabilise;
    }

    public void setErrorComptabilise(boolean errorComptabilise) {
        this.errorComptabilise = errorComptabilise;
    }

    public boolean isComptabilised() {
        return comptabilised;
    }

    public void setComptabilised(boolean comptabilised) {
        this.comptabilised = comptabilised;
    }

    @XmlTransient
    @JsonIgnore
    public double getMontantMensualite() {
        double re = 0;
        if (mensualites != null) {
            for (YvsComMensualiteFactureVente m : mensualites) {
                re += m.getMontant();
            }
        }
        return re;
    }

    @XmlTransient
    @JsonIgnore
    public double getMontantResteAPlanifiers() {
        double re = getMontantTotal();
        for (YvsComptaCaissePieceVente p : reglements) {
            re -= p.getMontant();
        }
        return re;
    }

    public String getMaDateSave() {
        return getDateSave() != null ? dfs.format(getDateSave()) : "";
    }

    public void setMaDateSave(String maDateSave) {
        this.maDateSave = maDateSave;
    }

    @XmlTransient
    @JsonIgnore
    public String getLibelleStatut() {
        String titre = getTypeDoc() != null ? getTypeDoc().equals("FV") ? "Facture" : "Bon" : "";
        if (!getStatut().equals("V")) {
            if (getStatut().equals("E")) {
                libelleStatut = titre + " en attente de validation";
            } else {
                libelleStatut = titre + " en cours de validation";
            }
        } else {
            if (!getStatutRegle().equals("P") && getTypeDoc().equals("FV")) {
                libelleStatut = titre + " en attente de paiement";
            } else if (!getStatutLivre().equals("L")) {
                libelleStatut = titre + " en cours de livraison";
            }
        }
        return libelleStatut;
    }

    public void setLibelleStatut(String libelleStatut) {
        this.libelleStatut = libelleStatut;
    }

    public boolean isLivrer() {
        return livrer;
    }

    public void setLivrer(boolean livrer) {
        this.livrer = livrer;
    }

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public YvsDictionnaire getAdresse() {
        return adresse;
    }

    public void setAdresse(YvsDictionnaire adresse) {
        this.adresse = adresse;
    }

    public YvsBaseModelReglement getModelReglement() {
        return modelReglement;
    }

    public void setModelReglement(YvsBaseModelReglement modelReglement) {
        this.modelReglement = modelReglement;
    }

    public YvsComClient getTiers() {
        return tiers;
    }

    public void setTiers(YvsComClient tiers) {
        this.tiers = tiers;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isCharger() {
        return charger;
    }

    public void setCharger(boolean charger) {
        this.charger = charger;
    }

    public YvsComClient getClient() {
        return client;
    }

    public void setClient(YvsComClient client) {
        this.client = client;
    }

    public YvsBaseCategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(YvsBaseCategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public YvsComEnteteDocVente getEnteteDoc() {
        return enteteDoc;
    }

    public void setEnteteDoc(YvsComEnteteDocVente enteteDoc) {
        this.enteteDoc = enteteDoc;
    }

    public YvsBaseDepots getDepotLivrer() {
        return depotLivrer;
    }

    public void setDepotLivrer(YvsBaseDepots depotLivrer) {
        this.depotLivrer = depotLivrer;
    }

    public YvsGrhTrancheHoraire getTrancheLivrer() {
        return trancheLivrer;
    }

    public void setTrancheLivrer(YvsGrhTrancheHoraire trancheLivrer) {
        this.trancheLivrer = trancheLivrer;
    }

    public YvsUsers getLivreur() {
        return livreur;
    }

    public void setLivreur(YvsUsers livreur) {
        this.livreur = livreur;
    }

    public YvsUsers getCloturerBy() {
        return cloturerBy;
    }

    public void setCloturerBy(YvsUsers cloturerBy) {
        this.cloturerBy = cloturerBy;
    }

    public YvsUsers getValiderBy() {
        return validerBy;
    }

    public void setValiderBy(YvsUsers validerBy) {
        this.validerBy = validerBy;
    }

    public YvsUsers getAnnulerBy() {
        return annulerBy;
    }

    public void setAnnulerBy(YvsUsers annulerBy) {
        this.annulerBy = annulerBy;
    }

    public YvsUsers getOperateur() {
        return operateur;
    }

    public void setOperateur(YvsUsers operateur) {
        this.operateur = operateur;
    }

    public YvsComDocVentes getDocumentLie() {
        return documentLie;
    }

    public void setDocumentLie(YvsComDocVentes documentLie) {
        this.documentLie = documentLie;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComDocVentesInformations getInformation() {
        return information;
    }

    public void setInformation(YvsComDocVentesInformations information) {
        this.information = information;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaContentJournalFactureVente getPieceContenu() {
        return pieceContenu;
    }

    public void setPieceContenu(YvsComptaContentJournalFactureVente pieceContenu) {
        this.pieceContenu = pieceContenu;
    }

    public List<YvsComContenuDocVente> getContenus() {
        return contenus;
    }

    public void setContenus(List<YvsComContenuDocVente> contenus) {
        this.contenus = contenus;
    }

    public List<YvsComptaCaissePieceVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComptaCaissePieceVente> reglements) {
        this.reglements = reglements;
    }

    public List<YvsComDocVentes> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsComDocVentes> documents) {
        this.documents = documents;
    }

    public List<YvsWorkflowValidFactureVente> getEtapesValidations() {
        return etapesValidations;
    }

    public void setEtapesValidations(List<YvsWorkflowValidFactureVente> etapesValidations) {
        this.etapesValidations = etapesValidations;
    }

    public List<YvsComCoutSupDocVente> getCouts() {
        return couts;
    }

    public void setCouts(List<YvsComCoutSupDocVente> couts) {
        this.couts = couts;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComMensualiteFactureVente> getMensualites() {
        if (mensualites != null) {
            Collections.sort(mensualites);
        }
        return mensualites;
    }

    public void setMensualites(List<YvsComMensualiteFactureVente> mensualites) {
        this.mensualites = mensualites;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComRistourneDocVente> getRistournes() {
        return ristournes;
    }

    public void setRistournes(List<YvsComRistourneDocVente> ristournes) {
        this.ristournes = ristournes;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComRemiseDocVente> getRemises() {
        return remises;
    }

    public void setRemises(List<YvsComRemiseDocVente> remises) {
        this.remises = remises;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommercialVente> getCommerciaux() {
        return commerciaux;
    }

    public void setCommerciaux(List<YvsComCommercialVente> commerciaux) {
        this.commerciaux = commerciaux;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComReservationStock> getReservations() {
        return reservations;
    }

    public void setReservations(List<YvsComReservationStock> reservations) {
        this.reservations = reservations;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComCommissionVente> getCommissions() {
        return commissions;
    }

    public void setCommissions(List<YvsComCommissionVente> commissions) {
        this.commissions = commissions;
    }

    public String getOldReference() {
        return oldReference;
    }

    public void setOldReference(String oldReference) {
        this.oldReference = oldReference;
    }

    @XmlTransient
    @JsonIgnore
    public YvsNiveauAcces getNiveau() {
        return niveau;
    }

    public void setNiveau(YvsNiveauAcces niveau) {
        this.niveau = niveau;
    }

    public YvsComptaJournaux getJournal() {
        return journal;
    }

    public void setJournal(YvsComptaJournaux journal) {
        this.journal = journal;
    }

    public char getStatutReglement() {
        return getStatutRegle().charAt(0);
    }

    public boolean canEditable() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE);
    }

    public boolean canDelete() {
        return statut.equals(Constantes.ETAT_ATTENTE) || statut.equals(Constantes.ETAT_EDITABLE) || statut.equals(Constantes.ETAT_SUSPENDU) || statut.equals(Constantes.ETAT_ANNULE);
    }

    public static String query() {
        String colonne = JDBC.colonnes(YvsComDocVentes.class) + ", " + JDBC.colonnes(YvsComClient.class) + ", " + JDBC.colonnes(YvsBaseTiers.class) + ", " + JDBC.colonnes(YvsComEnteteDocVente.class) + ", " + JDBC.colonnes(YvsComCreneauHoraireUsers.class) + ", " + JDBC.colonnes(YvsComCreneauPoint.class) + ", " + JDBC.colonnes(YvsBasePointVente.class) + ", " + JDBC.colonnes(YvsUsers.class);
        String query = "SELECT " + colonne + " FROM yvs_com_doc_ventes yvs_com_doc_ventes "
                + "INNER JOIN yvs_com_entete_doc_vente yvs_com_entete_doc_vente ON yvs_com_doc_ventes.entete_doc = yvs_com_entete_doc_vente.id "
                + "INNER JOIN yvs_com_creneau_horaire_users yvs_com_creneau_horaire_users ON yvs_com_entete_doc_vente.creneau = yvs_com_creneau_horaire_users.id "
                + "INNER JOIN yvs_com_creneau_point yvs_com_creneau_point ON yvs_com_creneau_horaire_users.creneau_point = yvs_com_creneau_point.id "
                + "INNER JOIN yvs_base_point_vente yvs_base_point_vente ON yvs_com_creneau_point.point = yvs_base_point_vente.id "
                + "INNER JOIN yvs_users yvs_users ON yvs_com_creneau_horaire_users.users = yvs_users.id "
                + "INNER JOIN yvs_com_client yvs_com_client ON yvs_com_doc_ventes.client = yvs_com_client.id "
                + "INNER JOIN yvs_base_tiers yvs_base_tiers ON yvs_com_client.tiers = yvs_base_tiers.id "
                + "INNER JOIN yvs_agences yvs_agences ON yvs_base_point_vente.agence = yvs_agences.id ";
        return query;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComDocVentes)) {
            return false;
        }
        YvsComDocVentes other = (YvsComDocVentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComDocVentes[ id=" + id + " ]";
    }
}
