/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.grh.recrutement.entretien;

import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author hp Elite 8300
 */
public class QuestionModel {

    private long id;
    private long idQuestion;
    private String question;
    private String name;
    private Object value;
    private Object[] values;
    private boolean required;
    private int note;   //note affecté à la réponse donné
    private List<SelectItem> items;
    private boolean save;   //marque les questions déjà enregistré

    public QuestionModel() {
    }

    public QuestionModel(long idQ,String question, String name, boolean required) {
        this.name = name;
        this.required = required;
        this.question = question;
        this.idQuestion=idQ;
    }

    public QuestionModel(long idQ,String question, String name, Object value, boolean required) {
        this.name = name;
        this.value = value;
        this.required = required;
        this.question = question;
        this.idQuestion=idQ;
    }

    public QuestionModel(long idQ,String question, Object value, boolean required, List<SelectItem> items) {
        this.value = value;
        this.required = required;
        this.items = items;
        this.question = question;
        this.idQuestion=idQ;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionModel(String name, Object[] values, boolean required, List<SelectItem> items) {
        this.name = name;
        this.values = values;
        this.required = required;
        this.items = items;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<SelectItem> getItems() {
        return items;
    }

    public void setItems(List<SelectItem> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(long idQuestion) {
        this.idQuestion = idQuestion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QuestionModel other = (QuestionModel) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
