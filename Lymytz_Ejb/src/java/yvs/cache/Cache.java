/*
 * Système de Cache Générique pour Lymytz ERP
 * Compatible Java 7
 */
package yvs.cache;

import java.io.Serializable;

/**
 * Interface générique pour un système de cache
 *
 * @param <K> Type de la clé
 * @param <V> Type de la valeur
 *
 * @author Lymytz ERP
 * @version 1.0
 */
public interface Cache<K, V> extends Serializable {

    /**
     * Récupère une valeur du cache
     *
     * @param key La clé
     * @return La valeur ou null si non trouvée ou expirée
     */
    V get(K key);

    /**
     * Ajoute ou met à jour une valeur dans le cache
     *
     * @param key La clé
     * @param value La valeur
     */
    void put(K key, V value);

    /**
     * Ajoute ou met à jour une valeur avec un TTL spécifique
     *
     * @param key La clé
     * @param value La valeur
     * @param ttlMillis Durée de vie en millisecondes
     */
    void put(K key, V value, long ttlMillis);

    /**
     * Supprime une entrée du cache
     *
     * @param key La clé à supprimer
     * @return true si l'élément existait et a été supprimé
     */
    boolean remove(K key);

    /**
     * Vide complètement le cache
     */
    void clear();

    /**
     * Retourne la taille actuelle du cache
     *
     * @return Nombre d'éléments dans le cache
     */
    int size();

    /**
     * Vérifie si une clé existe dans le cache (et n'est pas expirée)
     *
     * @param key La clé
     * @return true si la clé existe et est valide
     */
    boolean containsKey(K key);

    /**
     * Retourne les statistiques du cache
     *
     * @return Objet contenant les statistiques
     */
    CacheStats getStats();

    /**
     * Invalide les entrées expirées
     */
    void evictExpired();
}

