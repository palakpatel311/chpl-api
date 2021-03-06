package gov.healthit.chpl.questionableactivity;

import org.springframework.stereotype.Component;

import gov.healthit.chpl.dto.ProductVersionDTO;
import gov.healthit.chpl.dto.questionableActivity.QuestionableActivityVersionDTO;

/**
 * Tools for checking for Questionable Activity related to Versions.
 */
@Component
public class VersionQuestionableActivityProvider {

    /**
     * Check for QA re: Version name update.
     * @param origVersion the original version
     * @param newVersion the new version
     * @return DTO iff there was a change
     */
    public QuestionableActivityVersionDTO checkNameUpdated(
            final ProductVersionDTO origVersion, final ProductVersionDTO newVersion) {

        QuestionableActivityVersionDTO activity = null;
        if ((origVersion.getVersion() != null && newVersion.getVersion() == null)
                || (origVersion.getVersion() == null && newVersion.getVersion() != null)
                || !origVersion.getVersion().equals(newVersion.getVersion())) {
            activity = new QuestionableActivityVersionDTO();
            activity.setBefore(origVersion.getVersion());
            activity.setAfter(newVersion.getVersion());
        }

        return activity;
    }
}
