/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.entity.users.YvsUsersFavoris;

/**
 *
 * @author GOUCHERE YVES
 * @param <T>
 */
public class UtilitaireGenerique<T> {

    Class<T> classe;

    public UtilitaireGenerique() {
    }

    public int triDichontomique(List<T> l, String attribut, String chaine) {
        int indexDebut = 0, indexFin = l.size() - 1, indexMilieu = 0;
        boolean trouve = false;
        if (!l.isEmpty()) {
            String milieu = null;
            T c;
            while (!trouve && ((indexFin - indexDebut) > 1)) {
                indexMilieu = (indexDebut + indexFin) / 2;   //indice du milieu
                c = l.get(indexMilieu);  //récupère l'objet au milieu de la liste
                try {
                    Field f = c.getClass().getDeclaredField(attribut);
                    f.setAccessible(true);
                    milieu = (String) f.get(c); //code de l'objet  qui se trouve au milieu de la liste
                    trouve = (chaine.equals(milieu));
                    if (chaine.compareTo(milieu) > 0) {
                        indexDebut = indexMilieu;
                    } else if (chaine.compareTo(milieu) < 0) {
                        indexFin = indexMilieu;
                    } else {
                        trouve = true;
                    }
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                    Logger.getLogger(UtilitaireGenerique.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (indexMilieu == 1 && !trouve) {
                //on teste l'élément 0
                c = l.get(0);
                Field f = null;
                try {
                    f = c.getClass().getDeclaredField(attribut);
                    f.setAccessible(true);
                    milieu = (String) f.get(c);
                    indexMilieu = 0;
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(UtilitaireGenerique.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (chaine.equals(milieu)) {
                return indexMilieu;
            } else {
                return -1;
            }
        }
        return -1;

    }

    //trie dichotomique dans une liste de chaine de caractère
    public static int triDichontomique(List<String> l, String element) {
        int id = 0, ifin = l.size() - 1, im = 0;
        boolean trouve = false;
        if (!l.isEmpty()) {
            String milieu;
            while (!trouve && ((id - ifin) <= 0)) {
                im = (id + ifin) / 2;
                milieu = l.get(im);
//                trouve = (element.equals(milieu));
                if (milieu.equals(element)) {
                    trouve = true;
                    return im;
                } else if (element.compareTo(milieu) < 0) {
                    ifin = im - 1;
                } else {
                    id = im + 1;
                }
            }
//            if (im == 1 && !trouve) {
//                //on teste l'élément 0
//                milieu = l.get(0);
//                im = 0;
//
//            }
//            if (element.equals(milieu)) {
//                return im;
//            } else {
//                return -1;
//            }
        }
        return -1;
    }

    public static YvsUsersFavoris findInListFavoris(List<YvsUsersFavoris> list, String page) {
        if ((list != null ? !list.isEmpty() : false) && page != null) {
            List<YvsUsersFavoris> subList1;
            List<YvsUsersFavoris> subList2=new ArrayList<>();
            if (list.size() == 1) {
                if (page.equals(list.get(0).getPage())) {
                    return list.get(0);
                } else {
                    return null;
                }
            }
            int idx = ((int) list.size() / 2);
            subList1 = list.subList(0, idx);
            if (idx + 1 < list.size()) {
                subList2 = list.subList(idx + 1, list.size() - 1);
            }
            int re = page.compareTo(list.get(idx).getPage());
            if (re == 0) {
                return list.get(idx);
            } else if (re < 0) {
                return findInListFavoris(subList1, page);
            } else {
                return findInListFavoris(subList2, page);
            }
        }
        return null;
    }

}
