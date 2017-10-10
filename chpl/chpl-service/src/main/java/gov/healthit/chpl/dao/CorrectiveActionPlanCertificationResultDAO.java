package gov.healthit.chpl.dao;

import java.util.List;

import gov.healthit.chpl.dto.CorrectiveActionPlanCertificationResultDTO;

public interface CorrectiveActionPlanCertificationResultDAO {
    CorrectiveActionPlanCertificationResultDTO create(CorrectiveActionPlanCertificationResultDTO toCreate)
            throws EntityCreationException, EntityRetrievalException;

    CorrectiveActionPlanCertificationResultDTO update(CorrectiveActionPlanCertificationResultDTO toUpdate)
            throws EntityRetrievalException;

    CorrectiveActionPlanCertificationResultDTO getById(Long id) throws EntityRetrievalException;

    List<CorrectiveActionPlanCertificationResultDTO> getAllForCorrectiveActionPlan(Long capId);

    void delete(Long id) throws EntityRetrievalException;
}
