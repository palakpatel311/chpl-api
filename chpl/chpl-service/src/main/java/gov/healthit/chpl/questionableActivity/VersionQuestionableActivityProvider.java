package gov.healthit.chpl.questionableActivity;

import org.springframework.stereotype.Component;

import gov.healthit.chpl.dto.ProductVersionDTO;
import gov.healthit.chpl.dto.questionableActivity.QuestionableActivityVersionDTO;

@Component
public class VersionQuestionableActivityProvider {
    
    public QuestionableActivityVersionDTO checkNameUpdated(ProductVersionDTO origVersion, ProductVersionDTO newVersion) {
        
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