package gov.healthit.chpl.manager;

import java.util.Set;

import gov.healthit.chpl.dao.EntityRetrievalException;
import gov.healthit.chpl.domain.CertificationCriterion;
import gov.healthit.chpl.domain.CriteriaSpecificDescriptiveModel;
import gov.healthit.chpl.domain.DescriptiveModel;
import gov.healthit.chpl.domain.KeyValueModel;
import gov.healthit.chpl.domain.KeyValueModelStatuses;
import gov.healthit.chpl.domain.SurveillanceRequirementOptions;
import gov.healthit.chpl.domain.TestFunctionality;
import gov.healthit.chpl.domain.TestStandard;
import gov.healthit.chpl.domain.UploadTemplateVersion;
import gov.healthit.chpl.domain.notification.NotificationType;

public interface SearchMenuManager {
    Set<NotificationType> getNotificationTypes();

    Set<KeyValueModel> getJobTypes();

    Set<KeyValueModel> getClassificationNames();

    Set<KeyValueModel> getEditionNames(Boolean simple);

    Set<KeyValueModel> getCertificationStatuses();

    Set<KeyValueModel> getPracticeTypeNames();

    Set<KeyValueModelStatuses> getProductNames();

    Set<KeyValueModelStatuses> getDeveloperNames();

    Set<KeyValueModel> getCertBodyNames(Boolean showDeleted);

    Set<KeyValueModel> getAccessibilityStandards();

    Set<KeyValueModel> getUcdProcesses();

    Set<KeyValueModel> getQmsStandards();

    Set<KeyValueModel> getTargetedUesrs();

    Set<KeyValueModel> getEducationTypes();

    Set<KeyValueModel> getAgeRanges();

    Set<TestFunctionality> getTestFunctionality();

    Set<TestStandard> getTestStandards();

    Set<KeyValueModel> getTestTools();

    Set<KeyValueModel> getDeveloperStatuses();

    Set<KeyValueModel> getSurveillanceTypes();

    Set<KeyValueModel> getSurveillanceRequirementTypes();

    Set<KeyValueModel> getSurveillanceResultTypes();

    Set<KeyValueModel> getNonconformityStatusTypes();

    SurveillanceRequirementOptions getSurveillanceRequirementOptions();

    Set<KeyValueModel> getNonconformityTypeOptions();

    public Set<UploadTemplateVersion> getUploadTemplateVersions();

    Set<CriteriaSpecificDescriptiveModel> getMacraMeasures();

    Set<DescriptiveModel> getCertificationCriterionNumbers(Boolean simple) throws EntityRetrievalException;

    Set<CertificationCriterion> getCertificationCriterion();

    Set<DescriptiveModel> getCQMCriterionNumbers(Boolean simple);

}
