package gov.healthit.chpl.dto;

import java.io.Serializable;

import gov.healthit.chpl.entity.listing.CertificationResultTestDataEntity;

public class CertificationResultTestDataDTO implements Serializable {
    private static final long serialVersionUID = -8409772564902652781L;
    private Long id;
    private Long certificationResultId;
    private Long testDataId;
    private TestDataDTO testData;
    private String version;
    private String alteration;

    public CertificationResultTestDataDTO() {
    }

    public CertificationResultTestDataDTO(CertificationResultTestDataEntity entity) {
        this.id = entity.getId();
        this.certificationResultId = entity.getCertificationResultId();
        this.testDataId = entity.getTestDataId();
        if(entity.getTestData() != null) {
            this.testData = new TestDataDTO(entity.getTestData());
        }
        this.version = entity.getTestDataVersion();
        this.alteration = entity.getAlterationDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getCertificationResultId() {
        return certificationResultId;
    }

    public void setCertificationResultId(final Long certificationResultId) {
        this.certificationResultId = certificationResultId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getAlteration() {
        return alteration;
    }

    public void setAlteration(final String alteration) {
        this.alteration = alteration;
    }

    public Long getTestDataId() {
        return testDataId;
    }

    public void setTestDataId(Long testDataId) {
        this.testDataId = testDataId;
    }

    public TestDataDTO getTestData() {
        return testData;
    }

    public void setTestData(TestDataDTO testData) {
        this.testData = testData;
    }

}
