package gov.healthit.chpl.validation.developer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import gov.healthit.chpl.dto.DeveloperDTO;
import gov.healthit.chpl.dto.DeveloperStatusEventDTO;
import gov.healthit.chpl.util.ErrorMessageUtil;
import gov.healthit.chpl.util.ValidationUtils;

/**
 * Validate fields used when updating a developer. The main difference between validating
 * creation and update is that update does not require the address due to legacy data.
 */
@Component("developerUpdateValidator")
public class DeveloperUpdateValidator {
    private ErrorMessageUtil msgUtil;

    public DeveloperUpdateValidator() {}

    @Autowired
    public DeveloperUpdateValidator(final ErrorMessageUtil msgUtil) {
        this.msgUtil = msgUtil;
    }

    /**
     * Looks for missing or invalid fields in the developer object.
     * @param developer the developer to validate
     * @return a list of error messages generated from problems found with the developer
     */
    public Set<String> validate(final DeveloperDTO developer) {
        Set<String> errorMessages = new HashSet<String>();
        if (StringUtils.isEmpty(developer.getName())) {
            errorMessages.add(msgUtil.getMessage("developer.nameRequired"));
        }
        if (!StringUtils.isEmpty(developer.getWebsite())
                && !ValidationUtils.isWellFormedUrl(developer.getWebsite())) {
            errorMessages.add(msgUtil.getMessage("developer.websiteIsInvalid"));
        }

        if (developer.getContact() == null) {
            errorMessages.add(msgUtil.getMessage("developer.contactRequired"));
        } else {
            if (StringUtils.isEmpty(developer.getContact().getEmail())) {
                errorMessages.add(msgUtil.getMessage("developer.contact.emailRequired"));
            }
            if (StringUtils.isEmpty(developer.getContact().getPhoneNumber())) {
                errorMessages.add(msgUtil.getMessage("developer.contact.phoneRequired"));
            }
            if (StringUtils.isEmpty(developer.getContact().getFullName())) {
                errorMessages.add(msgUtil.getMessage("developer.contact.nameRequired"));
            }
        }
        errorMessages.addAll(validateDeveloperStatusEvents(developer.getStatusEvents()));
        return errorMessages;
    }

    protected List<String> validateDeveloperStatusEvents(final List<DeveloperStatusEventDTO> statusEvents) {
        List<String> errors = new ArrayList<String>();
        if (statusEvents == null || statusEvents.size() == 0) {
            errors.add(msgUtil.getMessage("developer.status.noCurrent"));
        } else {
            // sort the status events by date
            statusEvents.sort(new DeveloperStatusEventComparator());

            // now that the list is sorted by date, make sure no two statuses
            // next to each other are the same
            Iterator<DeveloperStatusEventDTO> iter = statusEvents.iterator();
            DeveloperStatusEventDTO prev = null, curr = null;
            while (iter.hasNext()) {
                if (prev == null) {
                    prev = iter.next();
                } else if (curr == null) {
                    curr = iter.next();
                } else {
                    prev = curr;
                    curr = iter.next();
                }

                if (prev != null && curr != null
                        && prev.getStatus().getStatusName().equalsIgnoreCase(curr.getStatus().getStatusName())) {
                    errors.add(msgUtil.getMessage("developer.status.duplicateStatus", prev.getStatus().getStatusName()));
                }
            }
        }
        return errors;
    }

    static class DeveloperStatusEventComparator implements Comparator<DeveloperStatusEventDTO>, Serializable {
        private static final long serialVersionUID = 7816629342251138939L;

        @Override
        public int compare(final DeveloperStatusEventDTO o1, final DeveloperStatusEventDTO o2) {
            if (o1 != null && o2 != null) {
                // neither are null, compare the dates
                return o1.getStatusDate().compareTo(o2.getStatusDate());
            } else if (o1 == null && o2 != null) {
                return -1;
            } else if (o1 != null && o2 == null) {
                return 1;
            } else {  // o1 and o2 are both null
                return 0;
            }
        }
    }

    public ErrorMessageUtil getMsgUtil() {
        return msgUtil;
    }
}