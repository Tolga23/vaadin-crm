package com.vaadin.tutorial.crm.backend.service;

import com.vaadin.tutorial.crm.backend.enums.Status;
import com.vaadin.tutorial.crm.backend.model.Company;
import com.vaadin.tutorial.crm.backend.model.Contact;
import com.vaadin.tutorial.crm.backend.repository.CompanyRepository;
import com.vaadin.tutorial.crm.backend.repository.ContactRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactService {

    private static final Logger LOGGER = Logger.getLogger(ContactService.class.getName());
    private ContactRepository contactRepository;
    private CompanyRepository companyRepository;

    public ContactService(ContactRepository contactRepository, CompanyRepository companyRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
    }

    public List<Contact> findAll(){
        return contactRepository.findAll();
    }

    public List<Contact> findAll(String filterText) {
        if (filterText == null) return contactRepository.findAll();
        else return contactRepository.search(filterText);
    }

    public long count(){
        return contactRepository.count();
    }

    public void delete(Contact contact){
        contactRepository.delete(contact);
    }

    public void save(Contact contact){
        if (contact == null) {
            LOGGER.log(Level.SEVERE,
                    "Contact is null");
            return;
        }
        contactRepository.save(contact);
    }

    @PostConstruct
    public void populateTestData(){
        if (companyRepository.count() == 0){
            companyRepository.saveAll(
                    Stream.of("Saturn","Media Markt","Amazon","Trendyol")
                    .map(Company::new)
                    .collect(Collectors.toList()));
        }

        if (contactRepository.count() == 0){
            Random r = new Random(0);
            List<Company> companies = companyRepository.findAll();
            contactRepository.saveAll(
                    Stream.of("John Patel", "Brian Dumbled", "Eduardo Cavla","Robin Cruse","Tolga Hard","G??lge Cat",
                            "Tim Byrit", "James Bond", "Meline Tranpe","Tesla Musk","Bernard Nils","Jay Camare",
                            "Daniel Watson", "Gunner Pon", "Elvis Cuti")
                    .map(name -> {
                        String[] split = name.split(" ");
                        Contact contact = new Contact();
                        contact.setFirstName(split[0]);
                        contact.setLastName(split[1]);
                        contact.setCompany(companies.get(r.nextInt(companies.size())));
                        contact.setStatus(Status.values()[r.nextInt(Status.values().length)]);
                        String email = (contact.getFirstName() + "." + contact.getLastName() + "@test.com");
                        contact.setEmail(email);
                        return contact;
                    }).collect(Collectors.toList()));
        }
    }
}
