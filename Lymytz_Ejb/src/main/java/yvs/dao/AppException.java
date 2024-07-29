/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao;

/**
 *
 * @author LYMYTZ-PC
 */
public class AppException extends Exception{

    public AppException() {
        System.err.println("--- erreur de suppresion-- ");
    }

    public AppException(String message) {
        super(message);
    }        
    
}
