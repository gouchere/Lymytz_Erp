/*
 * Statistiques de Cache pour Lymytz ERP
 * Compatible Java 7
 */
package yvs.cache;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Classe contenant les statistiques d'utilisation d'un cache
 * Thread-safe pour Java 7
 *
 * @author Lymytz ERP
 * @version 1.0
 */
public class CacheStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong puts = new AtomicLong(0);
    private final AtomicLong removals = new AtomicLong(0);

    /**
     * Enregistre un accès réussi (hit)
     */
    public void recordHit() {
        hits.incrementAndGet();
    }

    /**
     * Enregistre un accès manqué (miss)
     */
    public void recordMiss() {
        misses.incrementAndGet();
    }

    /**
     * Enregistre une éviction
     */
    public void recordEviction() {
        evictions.incrementAndGet();
    }

    /**
     * Enregistre un ajout
     */
    public void recordPut() {
        puts.incrementAndGet();
    }

    /**
     * Enregistre une suppression
     */
    public void recordRemoval() {
        removals.incrementAndGet();
    }

    /**
     * Réinitialise toutes les statistiques
     */
    public void reset() {
        hits.set(0);
        misses.set(0);
        evictions.set(0);
        puts.set(0);
        removals.set(0);
    }

    // Getters

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getEvictions() {
        return evictions.get();
    }

    public long getPuts() {
        return puts.get();
    }

    public long getRemovals() {
        return removals.get();
    }

    /**
     * Calcule le nombre total de requêtes
     */
    public long getTotalRequests() {
        return hits.get() + misses.get();
    }

    /**
     * Calcule le taux de hit (0.0 à 1.0)
     */
    public double getHitRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) hits.get() / total;
    }

    /**
     * Calcule le taux de miss (0.0 à 1.0)
     */
    public double getMissRate() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) misses.get() / total;
    }

    @Override
    public String toString() {
        return String.format(
            "CacheStats{hits=%d, misses=%d, hitRate=%.2f%%, evictions=%d, puts=%d, removals=%d}",
            getHits(),
            getMisses(),
            getHitRate() * 100,
            getEvictions(),
            getPuts(),
            getRemovals()
        );
    }
}

