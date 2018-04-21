package irt.stock.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import irt.stock.data.jpa.beans.Company;
import irt.stock.data.jpa.beans.Company.CompanyType;
import irt.stock.data.jpa.repositories.CompanyRepository;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

	@Autowired
	private CompanyRepository companyRepository;

	@RequestMapping("vendors")
	public List<Company> getVendors(){
		return companyRepository.findByType(CompanyType.VENDOR);
	}

	@RequestMapping("co_mfr")
	public List<Company> getCoManufactures(){
		return companyRepository.findByType(CompanyType.CO_MANUFACTURER);
	}
}
