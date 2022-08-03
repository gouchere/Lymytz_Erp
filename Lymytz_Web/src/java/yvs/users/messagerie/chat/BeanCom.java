/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.users.messagerie.chat;

import java.util.Date;

/**
 *
 * @author Lymytz_Serveur
 */
public class BeanCom {

    private String sender;
    private String receiver;
    private int idReceiver, idSender;
    private String msg;
    private Date hour;
    private boolean askConnection;
    private int typeMessage;    //défini le type de message qu'on envoi ou qu'on reçois et détermine le traitement à effectuer

    /**
     * 1-reponse à une demande de connexion 2-information de la connexion d'un nouvel utilisateur
     *  3-reception du canal de com 4-reception d'un ping
     * d'écriture 5- reception d'un nouveau message 6- l'arrêt de saisie
     *
     */
    public BeanCom() {
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isAskConnection() {
        return askConnection;
    }

    public void setAskConnection(boolean askConnection) {
        this.askConnection = askConnection;
    }

    public int getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(int idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getHour() {
        return hour;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public int getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(int typeMessage) {
        this.typeMessage = typeMessage;
    }

}
