package gov.healthit.chpl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import gov.healthit.chpl.util.Util;

/**
 * Object mapping for hibernate-handled table: product_version. Table to store
 * individual versions of a specific product
 *
 * @author autogenerated / cwatson
 */

@Entity
@Table(name = "product_version")
public class ProductVersionEntity implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = -5400083913829291315L;

    @Basic(optional = false)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Basic(optional = false)
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "product_version_id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "last_modified_date", nullable = false)
    private Date lastModifiedDate;

    @Basic(optional = false)
    @Column(name = "last_modified_user", nullable = false)
    private Long lastModifiedUser;

    @Basic(optional = false)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Basic(optional = true)
    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", unique = true, nullable = true, insertable = false, updatable = false)
    private ProductEntity product;

    @Basic(optional = true)
    @Column(name = "version")
    private String version;

    /**
     * Default constructor, mainly for hibernate use.
     */
    public ProductVersionEntity() {
        // Default constructor
    }

    /**
     * Constructor taking a given ID.
     *
     * @param id
     *            to set
     */
    public ProductVersionEntity(Long id) {
        this.id = id;
    }

    /**
     * Return the type of this class. Useful for when dealing with proxies.
     *
     * @return Defining class.
     */
    @Transient
    public Class<?> getClassType() {
        return ProductVersionEntity.class;
    }

    public Date getCreationDate() {
        return Util.getNewDate(creationDate);
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = Util.getNewDate(creationDate);
    }

    /**
     * Return the value associated with the column: deleted.
     *
     * @return A Boolean object (this.deleted)
     */
    public Boolean isDeleted() {
        return this.deleted;

    }

    /**
     * Set the value related to the column: deleted.
     *
     * @param deleted
     *            the deleted value you wish to set
     */
    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Return the value associated with the column: id.
     *
     * @return A Long object (this.id)
     */
    public Long getId() {
        return this.id;

    }

    /**
     * Set the value related to the column: id.
     *
     * @param id
     *            the id value you wish to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    public Date getLastModifiedDate() {
        return Util.getNewDate(lastModifiedDate);
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = Util.getNewDate(lastModifiedDate);
    }

    /**
     * Return the value associated with the column: lastModifiedUser.
     *
     * @return A Long object (this.lastModifiedUser)
     */
    public Long getLastModifiedUser() {
        return this.lastModifiedUser;

    }

    /**
     * Set the value related to the column: lastModifiedUser.
     *
     * @param lastModifiedUser
     *            the lastModifiedUser value you wish to set
     */
    public void setLastModifiedUser(final Long lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    /**
     * Return the value associated with the column: product.
     *
     * @return A Product object (this.product)
     */
    public Long getProductId() {
        return this.productId;

    }

    /**
     * Set the value related to the column: product.
     * @param productId the product value you wish to set
     */
    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    /**
     * Return the value associated with the column: version.
     * @return A String object (this.version)
     */
    public String getVersion() {
        return this.version;

    }

    /**
     * Set the value related to the column: version.
     * @param version
     *            the version value you wish to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Provides toString implementation.
     * @see java.lang.Object#toString()
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("creationDate: " + this.getCreationDate() + ", ");
        sb.append("deleted: " + this.isDeleted() + ", ");
        sb.append("id: " + this.getId() + ", ");
        sb.append("lastModifiedDate: " + this.getLastModifiedDate() + ", ");
        sb.append("lastModifiedUser: " + this.getLastModifiedUser() + ", ");
        sb.append("version: " + this.getVersion());
        return sb.toString();
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(final ProductEntity product) {
        this.product = product;
    }
}
