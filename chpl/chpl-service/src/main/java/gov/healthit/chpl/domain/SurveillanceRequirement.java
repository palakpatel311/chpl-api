package gov.healthit.chpl.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(namespace = "http://chpl.healthit.gov/listings")
@XmlAccessorType(XmlAccessType.FIELD)
public class SurveillanceRequirement implements Serializable {
	private static final long serialVersionUID = -4406043308588618231L;
	
	@XmlElement(required = true)
	private Long id;
	
	@XmlElement(required = true)
	private SurveillanceRequirementType type;
	
	@XmlElement(required = true)
	private String requirement;
	
	@XmlElement(required = false, nillable=true)
	private SurveillanceResultType result;
	
	@XmlElementWrapper(name = "nonconformities", nillable = true, required = false)
	@XmlElement(name = "nonconformity")
	private List<SurveillanceNonconformity> nonconformities;
	
	public SurveillanceRequirement() {
		this.nonconformities = new ArrayList<SurveillanceNonconformity>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SurveillanceRequirementType getType() {
		return type;
	}

	public void setType(SurveillanceRequirementType type) {
		this.type = type;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public SurveillanceResultType getResult() {
		return result;
	}

	public void setResult(SurveillanceResultType result) {
		this.result = result;
	}

	public List<SurveillanceNonconformity> getNonconformities() {
		return nonconformities;
	}

	public void setNonconformities(List<SurveillanceNonconformity> nonconformities) {
		this.nonconformities = nonconformities;
	}
	
	@Override
	public boolean equals(Object anotherObject) {
		// If the object is compared with itself then return true  
        if (anotherObject == this) {
            return true;
        }
 
        //check if anotherObject is the same type of class as this
        if (!(anotherObject instanceof SurveillanceRequirement)) {
            return false;
        }
         
        // typecast anotherObject to this type so that we can compare data members 
        SurveillanceRequirement anotherReq = (SurveillanceRequirement) anotherObject;
         
        // Compare the data members and return accordingly 
       if((this.getRequirement() == null && anotherReq.getRequirement() != null) ||
    	  (this.getRequirement() != null && anotherReq.getRequirement() == null)) {
    	   return false;
       }
       return this.getRequirement().equals(anotherReq.getRequirement());
	}
	
	@Override
	public int hashCode() {
		if(this.getRequirement() == null) {
			return -1;
		}
		return this.getRequirement().hashCode();
	}
}