package gov.healthit.chpl.auth.jwt;

import static org.junit.Assert.assertNotNull;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration(classes = { gov.healthit.chpl.auth.CHPLAuthenticationSecurityTestConfig.class })
public class JWTConsumerRsaJoseJImplTest {
	
	@Autowired
	JWTConsumer consumer;
	
	@Test
	public void consumerIsNotNull(){
		assertNotNull(consumer);
	}
	
	
    @BeforeClass
    public static void setUpClass() throws Exception {
        //try {
        	/*
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, 
                "org.apache.naming");            
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:/comp");
            ic.createSubcontext("java:/comp/env");
            ic.createSubcontext("java:/comp/env/jdbc");
           
            // Construct DataSource
            PGPoolingDataSource ds = new PGPoolingDataSource();
        	ds.setServerName("jdbc:postgresql://localhost/openchpl");
        	
            ds.setUser("openchpl");
            ds.setPassword("Audac1ous");
            
            ic.bind("java:/comp/env/jdbc/openchpl", ds);
            */
        //} catch (NamingException ex) {
        //	ex.printStackTrace();
        //}
        
    }
    
}
