package com.vaadin.tutorial.crm.backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Company extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private List<Contact> employees = new LinkedList<>();

    public Company(){}

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contact> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Contact> employees) {
        this.employees = employees;
    }
}
