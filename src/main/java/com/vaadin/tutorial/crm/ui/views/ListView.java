package com.vaadin.tutorial.crm.ui.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.model.Company;
import com.vaadin.tutorial.crm.backend.model.Contact;
import com.vaadin.tutorial.crm.backend.service.CompanyService;
import com.vaadin.tutorial.crm.backend.service.ContactService;
import com.vaadin.tutorial.crm.ui.MainLayout;


@Route(value = "",layout = MainLayout.class)
@PageTitle("Contact")
public class ListView extends VerticalLayout {

    private final ContactForm contactForm;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();

    private ContactService contactService;

    public <T extends ComponentEvent<?>> ListView(ContactService contactService,
                                                  CompanyService companyService) {
        this.contactService = contactService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        contactForm = new ContactForm(companyService.findAll());
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        contactForm.addListener(ContactForm.DeleteEvent.class,this::deleteContact);
        contactForm.addListener(ContactForm.CloseEvent.class, e -> closedEditor());

        Div content = new Div(grid,contactForm);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closedEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent evt) {
        contactService.delete(evt.getContact());
        updateList();
        closedEditor();
    }

    private void saveContact(ContactForm.SaveEvent evt) {
        contactService.save(evt.getContact());
        updateList();
        closedEditor();
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add contact", click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText,addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName","lastName","email","status");
        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(contactColumn -> contactColumn.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void closedEditor() {
        contactForm.setContact(null);
        contactForm.setVisible(false);
        removeClassName("editing");
    }

    private void editContact(Contact contact) {
        if (contact == null) closedEditor();
        else {contactForm.setContact(contact);
        contactForm.setVisible(true);
        addClassName("editing");
        }
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }

}
