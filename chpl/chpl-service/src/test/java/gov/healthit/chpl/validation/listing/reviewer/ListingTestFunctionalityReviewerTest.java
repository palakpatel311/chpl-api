package gov.healthit.chpl.validation.listing.reviewer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import gov.healthit.chpl.dao.CertificationCriterionDAO;
import gov.healthit.chpl.dao.CertificationEditionDAO;
import gov.healthit.chpl.dao.PracticeTypeDAO;
import gov.healthit.chpl.dao.TestFunctionalityDAO;
import gov.healthit.chpl.domain.CertificationResult;
import gov.healthit.chpl.domain.CertificationResultTestFunctionality;
import gov.healthit.chpl.domain.CertifiedProductSearchDetails;
import gov.healthit.chpl.domain.InheritedCertificationStatus;
import gov.healthit.chpl.dto.CertificationCriterionDTO;
import gov.healthit.chpl.dto.CertificationEditionDTO;
import gov.healthit.chpl.dto.PracticeTypeDTO;
import gov.healthit.chpl.dto.TestFunctionalityDTO;
import gov.healthit.chpl.manager.TestingFunctionalityManager;
import gov.healthit.chpl.util.ErrorMessageUtil;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = { gov.healthit.chpl.CHPLTestConfig.class })
public class ListingTestFunctionalityReviewerTest {

    @Spy
    private TestFunctionalityDAO testFunctionalityDAO;

    @Spy
    private CertificationCriterionDAO certificationCriterionDAO;

    @Spy
    private PracticeTypeDAO practiceTypeDAO;

    @Mock
    private ErrorMessageUtil msgUtil;

    @Spy
    private CertificationEditionDAO certificationEditionDAO;

    @Spy
    private TestingFunctionalityManager testFunctionalityManager;

    @InjectMocks
    private TestFunctionalityReviewer tfReviewer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn("In Criteria 170.314 (a)(6), Test Functionality (a)(6)(11) is for "
                + "Criteria other and is not valid for Criteria 170.314 (a)(6).")
            .when(msgUtil).getMessage(
                ArgumentMatchers.eq("listing.criteria.testFunctionalityCriterionMismatch"),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), 
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        Mockito.doReturn("In Criteria 170.314 (a)(6), Test Functionality (a)(6)(11) is for "
                + "other Settings and is not valid for Practice Type Ambulatory.")
            .when(msgUtil).getMessage(
                ArgumentMatchers.eq("listing.criteria.testFunctionalityPracticeTypeMismatch"),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), 
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        Mockito.when(certificationEditionDAO.findAll()).thenReturn(getEditions());

        Mockito.when(testFunctionalityManager.getTestFunctionalityCriteriaMap2014()).thenReturn(getTestFunctionalityCriteriaMap2014());
    }

    //Case 1: A valid test functionality
    @Test
    public void validateCertifiedProductTestFunctionality() {
        Mockito.when(testFunctionalityDAO.getByNumberAndEdition(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(getTestFunctionalityId_7());
        Mockito.when(certificationCriterionDAO.getByNameAndYear(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(getCertificationCriterion_a6());

        CertifiedProductSearchDetails listing = createListing("2014");
        List<CertificationResult> certResults = new ArrayList<CertificationResult>();
        CertificationResult certResult = createCertResult("170.314 (a)(6)");
        CertificationResultTestFunctionality crtf = new CertificationResultTestFunctionality();
        crtf.setDescription("No description required");
        crtf.setId(1l);
        crtf.setName("(a)(6)(i)");
        crtf.setTestFunctionalityId(7l);
        crtf.setYear("2014");
        certResult.setTestFunctionality(new ArrayList<CertificationResultTestFunctionality>());
        certResult.getTestFunctionality().add(crtf);
        certResults.add(certResult);
        listing.getCertificationResults().add(certResult);

        tfReviewer.onApplicationEvent(null);
        tfReviewer.review(listing);

        assertFalse(doesTestFunctionalityPracticeTypeErrorMessageExist(listing.getErrorMessages()));
        assertFalse(doesTestFunctionalityCriterionErrorMessageExist(listing.getErrorMessages()));
    }

    //Case 2: An invalid test functionality based on practice type
    @Test
    public void validateCertifiedProductTestFunctionalityPracticeTypeMismatch() throws Exception {
        Mockito.when(testFunctionalityDAO.getByNumberAndEdition(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(getTestFunctionalityId_18());
        Mockito.when(certificationCriterionDAO.getByNameAndYear(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(getCertificationCriterion_a6());

        CertifiedProductSearchDetails listing = createListing("2014");
        List<CertificationResult> certResults = new ArrayList<CertificationResult>();
        CertificationResult certResult = createCertResult("170.314 (a)(6)");
        CertificationResultTestFunctionality crtf = new CertificationResultTestFunctionality();
        crtf.setDescription("No description required");
        crtf.setId(1l);
        crtf.setName("(a)(6)(ii)");
        crtf.setTestFunctionalityId(18l);
        crtf.setYear("2104");
        certResult.setTestFunctionality(new ArrayList<CertificationResultTestFunctionality>());
        certResult.getTestFunctionality().add(crtf);
        certResults.add(certResult);
        listing.getCertificationResults().add(certResult);

        tfReviewer.onApplicationEvent(null);
        tfReviewer.review(listing);

        assertTrue(doesTestFunctionalityPracticeTypeErrorMessageExist(listing.getErrorMessages()));
    }

    //Case 3: An invalid test functionality based on certifcation criterion
    @Test
    public void validateCertifiedProductTestFunctionalityCertificationCriterionMismatch() {
        Mockito.when(testFunctionalityDAO.getByNumberAndEdition(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(getTestFunctionalityId_7());

        Mockito.when(certificationCriterionDAO.getByNameAndYear(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(getCertificationCriterion_a7());

        CertifiedProductSearchDetails listing = createListing("2014");
        List<CertificationResult> certResults = new ArrayList<CertificationResult>();
        CertificationResult certResult = createCertResult("170.314 (a)(7)");
        CertificationResultTestFunctionality crtf = new CertificationResultTestFunctionality();
        crtf.setDescription("No description required");
        crtf.setId(1l);
        crtf.setName("(a)(6)(i)");
        crtf.setTestFunctionalityId(18l);
        crtf.setYear("2104");
        certResult.setTestFunctionality(new ArrayList<CertificationResultTestFunctionality>());
        certResult.getTestFunctionality().add(crtf);
        certResults.add(certResult);
        listing.getCertificationResults().add(certResult);

        tfReviewer.onApplicationEvent(null);
        tfReviewer.review(listing);

        assertTrue(doesTestFunctionalityCriterionErrorMessageExist(listing.getErrorMessages()));
    }

    private Boolean doesTestFunctionalityPracticeTypeErrorMessageExist(Set<String> errorMessages) {
        for (String error : errorMessages) {
            if (error.contains("In Criteria")
                    && error.contains("Test Functionality")
                    && error.contains("is for")
                    && error.contains("Settings and is not valid for Practice Type")) {
                return true;
            }
        }
        return false;
    }

    private Boolean doesTestFunctionalityCriterionErrorMessageExist(Set<String> errorMessages) {
        for (String error : errorMessages) {
            if (error.contains("In Criteria")
                    && error.contains("Test Functionality")
                    && error.contains("is for Criteria")
                    && error.contains("and is not valid for Criteria")) {
                return true;
            }
        }
        return false;
    }

    private CertifiedProductSearchDetails createListing(final String year) {
        CertifiedProductSearchDetails listing = new CertifiedProductSearchDetails();
        String certDateString = "11-09-2016";
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date inputDate = dateFormat.parse(certDateString);
            listing.setCertificationDate(inputDate.getTime());
        } catch (ParseException ex) {
            fail(ex.getMessage());
        }
        listing.setId(1L);
        if (year.equals("2015")) {
            listing.getCertificationEdition().put("name", "2015");
            listing.getCertificationEdition().put("id", "3");
            listing.setChplProductNumber("15.07.07.2642.IC04.36.00.1.160402");
            listing.setPracticeType(null);
        } else if (year.equals("2014")) {
            listing.getCertificationEdition().put("name", "2014");
            listing.getCertificationEdition().put("id", "2");
            listing.setChplProductNumber("14.07.07.2642.IC04.36.00.1.160402");
            listing.getPracticeType().put("name", "Ambulatory");
            listing.getPracticeType().put("id", "1");
            listing.getClassificationType().put("name", "Modular EHR");
        }
        InheritedCertificationStatus ics = new InheritedCertificationStatus();
        ics.setInherits(Boolean.FALSE);
        listing.setIcs(ics);
        return listing;
    }

    private CertificationResult createCertResult(final String number) {
        CertificationResult certResult = new CertificationResult();
        certResult.setId(1L);
        certResult.setAdditionalSoftware(null);
        certResult.setApiDocumentation(null);
        certResult.setG1Success(false);
        certResult.setG2Success(false);
        certResult.setGap(null);
        certResult.setNumber(number);
        certResult.setPrivacySecurityFramework("Approach 1 Approach 2");
        certResult.setSed(null);
        certResult.setG1Success(false);
        certResult.setG2Success(false);
        certResult.setTestDataUsed(null);
        certResult.setTestFunctionality(null);
        certResult.setTestProcedures(null);
        certResult.setTestStandards(null);
        certResult.setSuccess(true);
        return certResult;
    }

    private CertificationCriterionDTO getCertificationCriterion_a6() {
        CertificationCriterionDTO cc = new CertificationCriterionDTO();
        cc.setCertificationEdition("2014");
        cc.setCertificationEditionId(2l);
        cc.setDeleted(false);
        cc.setDescription("Medication list");
        cc.setId(66l);
        cc.setNumber("170.314 (a)(6)");
        cc.setTitle("Medication list");
        return cc;
    }
    
    private CertificationCriterionDTO getCertificationCriterion_a7() {
        CertificationCriterionDTO cc = new CertificationCriterionDTO();
        cc.setCertificationEdition("2014");
        cc.setCertificationEditionId(2l);
        cc.setDeleted(false);
        cc.setDescription("Medication allergy list");
        cc.setId(67l);
        cc.setNumber("170.314 (a)(7)");
        cc.setTitle("Medication allergy list");
        return cc;
    }
    
    private TestFunctionalityDTO getTestFunctionalityId_18() {
        TestFunctionalityDTO tf = new TestFunctionalityDTO();
        tf.setId(18l);
        tf.setName("(a)(6)(ii)");
        tf.setYear("2014");

        PracticeTypeDTO pt = new PracticeTypeDTO();
        pt.setDeleted(false);
        pt.setId(2l);
        pt.setName("Inpatient");
        pt.setDescription("Inpatient");

        CertificationCriterionDTO cc = new CertificationCriterionDTO();
        cc.setDeleted(false);
        cc.setId(66l);
        cc.setNumber("170.314 (a)(6)");
        tf.setPracticeType(pt);

        return tf;
    }
    
    private TestFunctionalityDTO getTestFunctionalityId_7() {
        TestFunctionalityDTO tf = new TestFunctionalityDTO();
        tf.setId(7l);
        tf.setName("(a)(6)(i)");
        tf.setYear("2014");

        PracticeTypeDTO pt = new PracticeTypeDTO();
        pt.setDeleted(false);
        pt.setId(1l);
        pt.setName("Ambulatory");
        pt.setDescription("Ambulatory");

        CertificationCriterionDTO cc = new CertificationCriterionDTO();
        cc.setDeleted(false);
        cc.setId(66l);
        cc.setNumber("170.314 (a)(6)");

        tf.setPracticeType(pt);

        return tf;
    }
    
    private List<CertificationEditionDTO> getEditions() {
        List<CertificationEditionDTO> editions = new ArrayList<CertificationEditionDTO>();
        CertificationEditionDTO edition2011 = new CertificationEditionDTO();
        edition2011.setId(1l);
        edition2011.setYear("2011");
        editions.add(edition2011);

        CertificationEditionDTO edition2014 = new CertificationEditionDTO();
        edition2014.setId(2l);
        edition2014.setYear("2014");
        editions.add(edition2014);

        CertificationEditionDTO edition2015 = new CertificationEditionDTO();
        edition2015.setId(3l);
        edition2015.setYear("2015");
        editions.add(edition2015);

        return editions;
     }
    
    private Map<String, List<TestFunctionalityDTO>> getTestFunctionalityCriteriaMap2014() {
        Map<String, List<TestFunctionalityDTO>> map = new HashMap<String, List<TestFunctionalityDTO>>();

        List<TestFunctionalityDTO> tfs = new ArrayList<TestFunctionalityDTO>();
        tfs.add(getTestFunctionalityId_18());
        tfs.add(getTestFunctionalityId_7());

        map.put("170.314 (a)(6)", tfs);

        return map;
    }
}
