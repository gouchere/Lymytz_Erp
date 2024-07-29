/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

/**
 *
 * @author Lymytz Dowes
 */
public class MyExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        String fieldName = f.getName();
        if (fieldName.equals("sourceAttributeName") || fieldName.equals("isCoordinatedWithProperty") || fieldName.equals("isCoordinatedWithProperty") || fieldName.equals("isInstantiated")) {
            return true;
        }
        return !(f.getAnnotation(Column.class) == null || f.getAnnotation(JoinColumn.class) == null);
    }
}
