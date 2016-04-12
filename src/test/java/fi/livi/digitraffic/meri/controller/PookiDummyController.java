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

    /**
     * API that uses path variable "key" to return preset responses.
     *
     * For example /nautical-warnings/my-key will pop responses from Queue "my-key" until the response queue is empty.
     * If the queue is empty it will return the default "DUMMY_DATA"
     *
     * @see PookiDummyController#setResponseQueue(String, Queue) to set up the responses
     * @see NauticalWarningControllerTest#DUMMY_DATA
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
        return responseEntity!=null ? responseEntity : ResponseEntity.ok().body(null);
    }

}
