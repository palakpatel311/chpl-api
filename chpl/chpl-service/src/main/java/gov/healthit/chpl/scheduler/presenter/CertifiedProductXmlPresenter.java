package gov.healthit.chpl.scheduler.presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import gov.healthit.chpl.domain.CertifiedProductDownloadResponse;
import gov.healthit.chpl.scheduler.job.DownloadableResourceCreatorJob;

/**
 * Present objects as XML file.
 * @author alarned
 *
 */
public class CertifiedProductXmlPresenter implements CertifiedProductPresenter {
    private static final Logger LOGGER = LogManager.getLogger(DownloadableResourceCreatorJob.class);

    @Override
    public int presentAsFile(final File file, final CertifiedProductDownloadResponse cpList) {
        int numRecords = 0;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(cpList.getClass());
            marshaller.marshal(cpList, new StreamResult(os));
            numRecords = (cpList.getListings() == null ? 0 : cpList.getListings().size());
        } catch (final FileNotFoundException ex) {
            LOGGER.error("file not found " + file);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (final IOException ignore) {
                }
            }
        }
        return numRecords;
    }
}