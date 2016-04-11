package fi.livi.digitraffic.meri.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implements a dummy Pooki service to allow WebIntegration tests
 */
@RestController
@RequestMapping("/test")
public class PookiDummyController {

    /**
     *  A map of response queues. Each queue will pop (poll) the top request when called with the key.
     *
     * Key      Responses &lt;ResponseEntity&gt;
     *  A   ->  ( R1, R2, R3 )
     *  B   ->  ( E1, OK )
     *  C   ->  ()
     */
    private Map<String, Queue<ResponseEntity>> responseQueues = new HashMap<>();

    /**
     * Set one response queue for key "key"
     * @param key the key (path variable) to set up for requests
     * @param queue Queue of responses that the API will return if called with this "key"
     */
    public void setResponseQueue(String key, Queue<ResponseEntity> queue) {
        responseQueues.put(key, queue);
    }

    /**
     * Method to get the remaining responses to see if queue was called properly during test run
     * @param key Key of the queue
     * @return Remaining responses or empty queue or null.
     */
    public Queue<ResponseEntity> getResponseQueue(String key) {
        return responseQueues.get(key);
    }

//    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings",
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public GeoJSON dummyPookiNauticalWarnings() {
//        return new GeoJSON(DUMMY_DATA);
//    }

    /**
     * API that uses path variable "key" to return preset responses.
     *
     * For example /nautical-warnings/my-key will pop responses from Queue "my-key" until the response queue is empty.
     * If the queue is empty it will return the default "DUMMY_DATA"
     *
     * @see PookiDummyController#setResponseQueue(String, Queue) to set up the responses
     * @see PookiDummyController#DUMMY_DATA
     *
     * @param key the path variable to select the response queue
     * @return the next queued response, if available or DUMMY_DATA if queue is empty
     */
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{key}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity dummyPookiNauticalWarnings(@PathVariable("key") String key) {
        Queue<ResponseEntity> queue = getResponseQueue(key);
        if (queue ==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ResponseEntity responseEntity = queue.poll();
        return responseEntity!=null ? responseEntity : ResponseEntity.ok().body(DUMMY_DATA);
    }

    public static String DUMMY_DATA = "{'type':'FeatureCollection','features':[type':'Feature','properties':ID':980,"
            + "{''TOOLTIP':' NAVTEX COASTAL [Tallennettu 01.09.2014]'},"
            + "{''geometry':type':'Point','coordinates':[2327921.0,8469808.99999796,0.0]}},"
            + "{'type':'Feature','properties':ID':1000,'TOOLTIP':' NAVTEX COASTAL"
            + "{'[Tallennettu 04.09.2014]'},'geometry':type':'Point',"
            + "{''coordinates':[2374205.9999999967,8465888.999997966,0.0]}},type':'Feature',"
            + "{''properties':ID':1040,'TOOLTIP':' NAVTEX COASTAL [Tallennettu 22.01.2015]'},"
            + "{''geometry':type':'Polygon','coordinates':[[[2341315.0000000028,"
            + "{'8467098.9999979511,0.0],[2341289.9999999949,8467034.9999979641,0.0],"
            + "{'[2341385.0000000042,8467034.9999979641,0.0],[2341427.9999999967,"
            + "{'8467081.9999979679,0.0],[2341315.0000000028,8467098.9999979511,0.0]]]}},"
            + "{'type':'Feature','properties':ID':1212,'TOOLTIP':'Varoituksia veneilijöille"
            + "{'[Tallennettu 18.09.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2332479.9999999981,8464195.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1214,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 01.10.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2421004.0000000023,8424745.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1215,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 01.10.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2421004.0000000023,8424745.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1221,'TOOLTIP':'Varoituksia veneilijöille"
            + "{'[Tallennettu 10.12.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2384048.0000000033,8433840.9999979753,0.0]}},"
            + "{'type':'Feature','properties':ID':1241,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 03.02.2016]'},'geometry':type':'Point',"
            + "{''coordinates':[2340687.9999999949,8465569.9999979641,0.0]}},"
            + "{'type':'Feature','properties':ID':1242,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 03.02.2016]'},'geometry':type':'Point',"
            + "{''coordinates':[2340478.0000000005,8467729.99999796,0.0]}},type':'Feature',"
            + "{''properties':ID':1244,'TOOLTIP':'NAVIGATIONAL WARNING [Tallennettu"
            + "{'03.02.2016]'},'geometry':type':'Point','coordinates':[2343413.0,"
            + "{'8468334.9999979548,0.0]}}]}";

}
