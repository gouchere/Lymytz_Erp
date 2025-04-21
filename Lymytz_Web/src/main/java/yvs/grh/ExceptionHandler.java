/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.grh;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@RequestScoped
public class ExceptionHandler {
    
    public void throwNullPointerException(){
        throw new NullPointerException("A nullPointer Exception");
    }
}
