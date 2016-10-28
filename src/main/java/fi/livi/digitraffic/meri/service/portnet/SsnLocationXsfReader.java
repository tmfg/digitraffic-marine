package fi.livi.digitraffic.meri.service.portnet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Component
public class SsnLocationXsfReader {
    private static final Logger log = LoggerFactory.getLogger(SsnLocationXsfReader.class);

    public List<SsnLocation> readLocations(final Path path) throws IOException, OpenXML4JException, SAXException {
        try (
                final FileInputStream fis = new FileInputStream(path.toFile());
                final POIFSFileSystem poifs = new POIFSFileSystem(fis);
                final InputStream in = poifs.createDocumentInputStream("Workbook")
        ) {
            final HSSFRequest request = new HSSFRequest();
            final XlsListener listener = new XlsListener();

            request.addListenerForAllRecords(new MissingRecordAwareHSSFListener(listener));

            final HSSFEventFactory factory = new HSSFEventFactory();
            factory.processEvents(request, in);

            return listener.locations;
        }
    }

    private class XlsListener implements HSSFListener {
        private int currentColumn = 1;
        private boolean header = true;

        private SSTRecord record;
        private List<SsnLocation> locations = new ArrayList<>();
        private SsnLocation current = new SsnLocation();

        @Override
        public void processRecord(final Record record) {
            switch (record.getSid()) {
            case SSTRecord.sid:
                this.record = (SSTRecord) record;
                break;
            case BlankRecord.sid:
                currentColumn++;
                break;
            case LabelSSTRecord.sid:
                final LabelSSTRecord lrec = (LabelSSTRecord) record;
                final String value = this.record.getString(lrec.getSSTIndex()).toString();

                switch (currentColumn) {
                case 1:
                    current.setLocode(value);
                    break;
                case 2:
                    current.setLocationName(value);
                    break;
                case 3:
                    current.setCountry(value);
                    break;
                case 4:
                    current.setWgs84Lat(LocationParser.parseLatitude(value));
                    break;
                case 5:
                    current.setWgs84Long(LocationParser.parseLongitude(value));
                    break;
                default:
                    throw new IllegalArgumentException();
                }

                currentColumn++;

                break;
            case -1:
                currentColumn = 1;

                // first line is the header
                if (header) {
                    header = false;
                } else {
                    locations.add(current);
                    current = new SsnLocation();
                }
                break;
            }
        }
    }
}
