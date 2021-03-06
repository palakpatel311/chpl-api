package gov.healthit.chpl.web.controller.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gov.healthit.chpl.domain.TestingLab;

public class TestingLabResults implements Serializable {
    private static final long serialVersionUID = -6741433293000497548L;
    private List<TestingLab> atls;

    public TestingLabResults() {
        atls = new ArrayList<TestingLab>();
    }

    public List<TestingLab> getAtls() {
        return atls;
    }

    public void setAtls(final List<TestingLab> atls) {
        this.atls = atls;
    }
}
