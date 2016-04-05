package fi.livi.digitraffic.meri.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.GeoJSON;

/**
 * Implements a dummy Pooki service to allow WebIntegration tests
 */
@RestController
@RequestMapping("/test")
public class PookiDummyController {

    String DUMMY_DATA = "{foo: 'foo'}";

    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public GeoJSON dummyPookiNauticalWarnings() {
        return new GeoJSON(DUMMY_DATA);
    }
}
