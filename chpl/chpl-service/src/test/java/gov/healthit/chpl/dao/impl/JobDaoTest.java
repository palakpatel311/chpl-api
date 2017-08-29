package gov.healthit.chpl.dao.impl;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.healthit.chpl.auth.permission.GrantedPermission;
import gov.healthit.chpl.auth.user.JWTAuthenticatedUser;
import gov.healthit.chpl.caching.UnitTestRules;
import gov.healthit.chpl.dao.EntityCreationException;
import gov.healthit.chpl.dao.EntityRetrievalException;
import gov.healthit.chpl.dao.JobDAO;
import gov.healthit.chpl.dto.ContactDTO;
import gov.healthit.chpl.dto.JobDTO;
import gov.healthit.chpl.dto.JobTypeDTO;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { gov.healthit.chpl.CHPLTestConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:data/testData.xml")
public class JobDaoTest extends TestCase {

	@Autowired
	private JobDAO jobDao;
	
	@Rule
    @Autowired
    public UnitTestRules cacheInvalidationRule;

	private static JWTAuthenticatedUser adminUser;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		adminUser = new JWTAuthenticatedUser();
		adminUser.setFirstName("Administrator");
		adminUser.setId(-2L);
		adminUser.setLastName("Administrator");
		adminUser.setSubjectName("admin");
		adminUser.getPermissions().add(new GrantedPermission("ROLE_ADMIN"));
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testAddJob() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		assertNotNull(job);
		assertNotNull(job.getId());
		assertTrue(job.getId() > 0);
		
		job = jobDao.getById(job.getId());
		assertNotNull(job.getJobType());
		assertEquals(job.getJobType().getId().longValue(), jobType.getId().longValue());
		assertNotNull(job.getContact());
		assertEquals(job.getContact().getId().longValue(), contact.getId().longValue());
		assertEquals(data, job.getData());
		assertNotNull(job.getStartTime());
		assertEquals(startTime.getTime(), job.getStartTime().getTime());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testFindAllJobs() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		List<JobDTO> allJobs = jobDao.findAll();
		assertEquals(1, allJobs.size());
		assertNotNull(allJobs.get(0));
		assertNotNull(allJobs.get(0).getId());
		assertEquals(job.getId().longValue(), allJobs.get(0).getId().longValue());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testFindRunningJobs() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		assertNotNull(job);
		assertNotNull(job.getId());
		assertTrue(job.getId() > 0);
		
		job = new JobDTO();
		job.setJobType(jobType);
		job.setContact(contact);
		job.setData(data);
		job.setStartTime(startTime);
		job.setEndTime(new Date());
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		assertNotNull(job);
		assertNotNull(job.getId());
		assertTrue(job.getId() > 0);
		
		List<JobDTO> jobs = jobDao.findAllRunning();
		assertNotNull(jobs);
		assertEquals(1, jobs.size());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testFindJobsByUserWithJob() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		List<JobDTO> allJobs = jobDao.getByUser(contact.getId());
		assertEquals(1, allJobs.size());
		assertNotNull(allJobs.get(0));
		assertNotNull(allJobs.get(0).getId());
		assertEquals(job.getId().longValue(), allJobs.get(0).getId().longValue());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testFindJobsByUserWithoutJob() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		List<JobDTO> allJobs = jobDao.getByUser(-4L);
		assertEquals(0, allJobs.size());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testUpdateJob() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		assertNotNull(job);
		assertNotNull(job.getId());
		assertTrue(job.getId() > 0);
		
		Date endTime = new Date(System.currentTimeMillis()+1000);
		job.setEndTime(endTime);
		try {
			jobDao.update(job);
		} catch(EntityRetrievalException ex) {
			fail(ex.getMessage());
		}
		
		job = jobDao.getById(job.getId());
		assertNotNull(job.getJobType());
		assertEquals(job.getJobType().getId().longValue(), jobType.getId().longValue());
		assertNotNull(job.getContact());
		assertEquals(job.getContact().getId().longValue(), contact.getId().longValue());
		assertEquals(data, job.getData());
		assertNotNull(job.getStartTime());
		assertEquals(startTime.getTime(), job.getStartTime().getTime());
		assertNotNull(job.getEndTime());
		assertEquals(endTime.getTime(), job.getEndTime().getTime());
	}
	
	@Test
	@Rollback(true)
	@Transactional
	public void testDeleteJob() {
		SecurityContextHolder.getContext().setAuthentication(adminUser);
		JobDTO job = new JobDTO();
		JobTypeDTO jobType = new JobTypeDTO();
		jobType.setId(1L);
		ContactDTO contact = new ContactDTO();
		contact.setId(-2L);
		job.setJobType(jobType);
		job.setContact(contact);
		String data = "Some,CSV,Data";
		job.setData(data);
		Date startTime = new Date();
		job.setStartTime(startTime);
		
		try {
			job = jobDao.create(job);
		} catch(EntityCreationException ex) {
			fail(ex.getMessage());
		}
		
		assertNotNull(job);
		assertNotNull(job.getId());
		assertTrue(job.getId() > 0);

		try {
			jobDao.delete(job.getId());
		} catch(EntityRetrievalException ex) {
			fail(ex.getMessage());
		}
		
		job = jobDao.getById(job.getId());
		assertNull(job);
	}
}