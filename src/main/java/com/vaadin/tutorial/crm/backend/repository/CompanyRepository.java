package com.vaadin.tutorial.crm.backend.repository;

import com.vaadin.tutorial.crm.backend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
}
