package gov.healthit.chpl.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.healthit.chpl.auth.Util;
import gov.healthit.chpl.auth.user.UserRetrievalException;
import gov.healthit.chpl.caching.CacheNames;
import gov.healthit.chpl.caching.ClearAllCaches;
import gov.healthit.chpl.dao.CertificationBodyDAO;
import gov.healthit.chpl.domain.concept.ActivityConcept;
import gov.healthit.chpl.dto.CertificationBodyDTO;
import gov.healthit.chpl.exception.EntityCreationException;
import gov.healthit.chpl.exception.EntityRetrievalException;
import gov.healthit.chpl.exception.ValidationException;
import gov.healthit.chpl.manager.ActivityManager;
import gov.healthit.chpl.manager.CertificationBodyManager;
import gov.healthit.chpl.manager.SchedulerManager;
import gov.healthit.chpl.manager.UserPermissionsManager;
import gov.healthit.chpl.permissions.Permissions;

/**
 * Business logic for accessing and updating ACBs.
 * 
 * @author kekey
 *
 */
@Service("certificationBodyManager")
public class CertificationBodyManagerImpl extends ApplicationObjectSupport implements CertificationBodyManager {
    private static final Logger LOGGER = LogManager.getLogger(CertificationBodyManagerImpl.class);

    private CertificationBodyDAO certificationBodyDao;
    private ActivityManager activityManager;
    private SchedulerManager schedulerManager;
    private UserPermissionsManager userPermissionsManager;
    private Permissions permissions;

    @Autowired
    public CertificationBodyManagerImpl(final CertificationBodyDAO certificationBodyDao,
            final ActivityManager activityManager, @Lazy final SchedulerManager schedulerManager,
            final UserPermissionsManager userPermissionsManager, final Permissions permissions) {
        this.certificationBodyDao = certificationBodyDao;
        this.activityManager = activityManager;
        this.schedulerManager = schedulerManager;
        this.userPermissionsManager = userPermissionsManager;
        this.permissions = permissions;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissions.hasAccess(T(gov.healthit.chpl.permissions.Permissions).CERTIFICATION_BODY, "
            + "T(gov.healthit.chpl.permissions.domains.CertificationBodyDomainPermissions).CREATE)")
    @ClearAllCaches
    public CertificationBodyDTO create(final CertificationBodyDTO acb)
            throws UserRetrievalException, EntityCreationException, EntityRetrievalException, JsonProcessingException {
        // assign a code
        String maxCode = certificationBodyDao.getMaxCode();
        int maxCodeValue = Integer.parseInt(maxCode);
        int nextCodeValue = maxCodeValue + 1;

        String nextAcbCode = "";
        if (nextCodeValue < 10) {
            nextAcbCode = "0" + nextCodeValue;
        } else if (nextCodeValue > 99) {
            throw new EntityCreationException(
                    "Cannot create a 2-digit ACB code since there are more than 99 ACBs in the system.");
        } else {
            nextAcbCode = nextCodeValue + "";
        }
        acb.setAcbCode(nextAcbCode);
        acb.setRetired(false);

        // Create the ACB itself
        CertificationBodyDTO result = certificationBodyDao.create(acb);

        // Grant the current principal administrative permission to the ACB
        userPermissionsManager.addPermission(result, Util.getCurrentUser().getId());

        LOGGER.debug("Created acb " + result + " and granted admin permission to recipient "
                + gov.healthit.chpl.auth.Util.getUsername());

        String activityMsg = "Created Certification Body " + result.getName();

        activityManager.addActivity(ActivityConcept.ACTIVITY_CONCEPT_CERTIFICATION_BODY, result.getId(), activityMsg,
                null, result);

        return result;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissions.hasAccess(T(gov.healthit.chpl.permissions.Permissions).CERTIFICATION_BODY, "
            + "T(gov.healthit.chpl.permissions.domains.CertificationBodyDomainPermissions).UPDATE, #acb)")
    @ClearAllCaches
    public CertificationBodyDTO update(final CertificationBodyDTO acb) throws EntityRetrievalException,
            JsonProcessingException, EntityCreationException, UpdateCertifiedBodyException {

        CertificationBodyDTO result = null;
        CertificationBodyDTO toUpdate = certificationBodyDao.getById(acb.getId());
        result = certificationBodyDao.update(acb);

        String activityMsg = "Updated acb " + acb.getName();
        activityManager.addActivity(ActivityConcept.ACTIVITY_CONCEPT_CERTIFICATION_BODY, result.getId(), activityMsg,
                toUpdate, result);
        return result;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissions.hasAccess(T(gov.healthit.chpl.permissions.Permissions).CERTIFICATION_BODY, "
            + "T(gov.healthit.chpl.permissions.domains.CertificationBodyDomainPermissions).RETIRE)")
    @CacheEvict(CacheNames.CERT_BODY_NAMES)
    public CertificationBodyDTO retire(final CertificationBodyDTO acb)
            throws EntityRetrievalException, JsonProcessingException, EntityCreationException, IllegalArgumentException,
            SchedulerException, ValidationException {
        Date now = new Date();
        if (acb.getRetirementDate() == null || now.before(acb.getRetirementDate())) {
            throw new IllegalArgumentException("Retirement date is required and must be before \"now\".");
        }
        CertificationBodyDTO result = null;
        CertificationBodyDTO toUpdate = certificationBodyDao.getById(acb.getId());
        toUpdate.setRetired(true);
        toUpdate.setRetirementDate(acb.getRetirementDate());
        result = certificationBodyDao.update(toUpdate);
        schedulerManager.retireAcb(toUpdate.getName());

        String activityMsg = "Retired acb " + toUpdate.getName();
        activityManager.addActivity(ActivityConcept.ACTIVITY_CONCEPT_CERTIFICATION_BODY, result.getId(), activityMsg,
                toUpdate, result);
        return result;
    }

    @Override
    @Transactional
    @PreAuthorize("@permissions.hasAccess(T(gov.healthit.chpl.permissions.Permissions).CERTIFICATION_BODY, "
            + "T(gov.healthit.chpl.permissions.domains.CertificationBodyDomainPermissions).UNRETIRE)")
    @CacheEvict(CacheNames.CERT_BODY_NAMES)
    public CertificationBodyDTO unretire(final Long acbId) throws EntityRetrievalException, JsonProcessingException,
            EntityCreationException, UpdateCertifiedBodyException {
        CertificationBodyDTO result = null;
        CertificationBodyDTO toUpdate = certificationBodyDao.getById(acbId);
        toUpdate.setRetired(false);
        toUpdate.setRetirementDate(null);
        result = certificationBodyDao.update(toUpdate);

        String activityMsg = "Unretired acb " + toUpdate.getName();
        activityManager.addActivity(ActivityConcept.ACTIVITY_CONCEPT_CERTIFICATION_BODY, result.getId(), activityMsg,
                toUpdate, result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificationBodyDTO> getAll() {
        return certificationBodyDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificationBodyDTO> getAllActive() {
        return certificationBodyDao.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public CertificationBodyDTO getById(final Long id) throws EntityRetrievalException {
        return certificationBodyDao.getById(id);
    }

    public void setCertificationBodyDAO(final CertificationBodyDAO acbDAO) {
        this.certificationBodyDao = acbDAO;
    }

}
