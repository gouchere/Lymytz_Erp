/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.util;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author hp Elite 8300
 * @param <T>
 */
public class DynamicExport<T extends Serializable> {

    private Class classe;

    public DynamicExport() {
    }

    public DynamicExport(Class<T> aClass) {
        this.classe = aClass;
    }

    public Class getClasse() {
        return classe;
    }

    public List<String> giveFieldClass() {
        List<String> re = new ArrayList<>();
        Field[] champs = classe.getDeclaredFields();
        for (Field f : champs) {
            if (f.getType().getSimpleName().toLowerCase().equals("double") || f.getType().getSimpleName().toLowerCase().equals("long")
                    || f.getType().getSimpleName().toLowerCase().equals("int") || f.getType().getSimpleName().toLowerCase().equals("string")
                    || f.getType().getSimpleName().toLowerCase().equals("boolean") || f.getType().getSimpleName().toLowerCase().equals("date")) {
                re.add(f.getName());
            }
        }
        Collections.sort(re);
        return re;
    }
    

}
