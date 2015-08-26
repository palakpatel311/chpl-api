package gov.healthit.chpl.web.controller;

import java.util.List;

import gov.healthit.chpl.auth.json.UserInfoJSONObject;
import gov.healthit.chpl.auth.user.UserRetrievalException;
import gov.healthit.chpl.dao.EntityRetrievalException;
import gov.healthit.chpl.domain.CQMResult;
import gov.healthit.chpl.domain.CertificationResult;
import gov.healthit.chpl.domain.CertifiedProductSearchDetails;
import gov.healthit.chpl.domain.CertifiedProductSearchResult;
import gov.healthit.chpl.entity.CertificationBodyEntity;
import gov.healthit.chpl.manager.CertificationBodyManager;
import gov.healthit.chpl.manager.CertifiedProductSearchManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchViewController {
	
	
	@Autowired
	private CertifiedProductSearchManager certifiedProductSearchManager;
	
	@Autowired
	private CertificationBodyManager certificationBodyManager;
	
	
	@RequestMapping(value="/certified_product_details", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody CertifiedProductSearchDetails getCertifiedProductDetails(@RequestParam("productId") Long id){
		
		CertifiedProductSearchDetails product = certifiedProductSearchManager.getCertifiedProductDetails(id);
		
		return product;
	}
	
	@RequestMapping(value="/list_certified_products", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<CertifiedProductSearchResult> listCertifiedProducts() throws EntityRetrievalException {
		
		return certifiedProductSearchManager.getAllCertifiedProducts();
	}
	
	@RequestMapping(value="/list_certs", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<CertificationResult> getCertifications() {
		return certifiedProductSearchManager.getCertifications();
	}
	
	@RequestMapping(value="/list_cqms", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<CQMResult> getCQMResults() {
		return certifiedProductSearchManager.getCQMResults();
	}
	
	@RequestMapping(value="/list_classification_names", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getClassificationNames() {
		return certifiedProductSearchManager.getClassificationNames();
	}
	
	@RequestMapping(value="/list_edition_names", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getEditionNames() {
		return certifiedProductSearchManager.getEditionNames();
	}
	
	
	@RequestMapping(value="/list_practice_types", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getPracticeTypeNames() {
		return certifiedProductSearchManager.getPracticeTypeNames();
	}
	
	
	@RequestMapping(value="/list_product_names", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getProductNames() {
		return certifiedProductSearchManager.getProductNames();
	}
	
	@RequestMapping(value="/list_vendor_names", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getVendorNames() {
		return certifiedProductSearchManager.getVendorNames();
	}
	
	@RequestMapping(value="/list_certification_body_names", method=RequestMethod.GET,
			produces="application/json; charset=utf-8")
	public @ResponseBody List<String> getCertBodyNames() {
		return certifiedProductSearchManager.getCertBodyNames();
	}
	
	public CertificationBodyManager getCertificationBodyManager() {
		return certificationBodyManager;
	}

	public void setCertificationBodyManager(
			CertificationBodyManager certificationBodyManager) {
		this.certificationBodyManager = certificationBodyManager;
	}
	
}