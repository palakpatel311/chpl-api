package gov.healthit.chpl.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import gov.healthit.chpl.dto.CertificationResultUcdProcessDTO;

@XmlType(namespace = "http://chpl.healthit.gov/listings")
@XmlAccessorType(XmlAccessType.FIELD)
public class CertificationResultUcdProcess implements Serializable {
	private static final long serialVersionUID = 7248865611086710891L;
	
	@XmlElement(required = true)
	private Long id;
	
	@XmlElement(required = true)
	private Long ucdProcessId;
	
	@XmlElement(required = true)
	private String ucdProcessName;
	
	@XmlElement(required = false, nillable=true)
	private String ucdProcessDetails;

	public CertificationResultUcdProcess() {
		super();
	}
	
	public CertificationResultUcdProcess(CertificationResultUcdProcessDTO dto) {
		this.id = dto.getId();
		this.ucdProcessId = dto.getUcdProcessId();
		this.ucdProcessName = dto.getUcdProcessName();
		this.ucdProcessDetails = dto.getUcdProcessDetails();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUcdProcessId() {
		return ucdProcessId;
	}

	public void setUcdProcessId(Long ucdProcessId) {
		this.ucdProcessId = ucdProcessId;
	}

	public String getUcdProcessName() {
		return ucdProcessName;
	}

	public void setUcdProcessName(String ucdProcessName) {
		this.ucdProcessName = ucdProcessName;
	}

	public String getUcdProcessDetails() {
		return ucdProcessDetails;
	}

	public void setUcdProcessDetails(String ucdProcessDetails) {
		this.ucdProcessDetails = ucdProcessDetails;
	}
}
