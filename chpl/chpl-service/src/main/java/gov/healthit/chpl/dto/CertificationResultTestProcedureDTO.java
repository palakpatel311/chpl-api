package gov.healthit.chpl.dto;

import java.io.Serializable;

import gov.healthit.chpl.entity.listing.CertificationResultTestProcedureEntity;

public class CertificationResultTestProcedureDTO implements Serializable {
    private static final long serialVersionUID = -961853252507290637L;
    private Long id;
    private Long certificationResultId;
    private Long testProcedureId;
    private String testProcedureVersion;
    private Boolean deleted;

    public CertificationResultTestProcedureDTO() {
    }

    public CertificationResultTestProcedureDTO(CertificationResultTestProcedureEntity entity) {
        this.id = entity.getId();
        this.certificationResultId = entity.getCertificationResultId();
        this.testProcedureId = entity.getTestProcedureId();
        if (entity.getTestProcedure() != null) {
            this.testProcedureVersion = entity.getTestProcedure().getVersion();
        }
        this.deleted = entity.getDeleted();
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getTestProcedureId() {
        return testProcedureId;
    }

    public void setTestProcedureId(final Long testProcedureId) {
        this.testProcedureId = testProcedureId;
    }

    public String getTestProcedureVersion() {
        return testProcedureVersion;
    }

    public void setTestProcedureVersion(final String testProcedureVersion) {
        this.testProcedureVersion = testProcedureVersion;
    }

}
