package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.GeoJSON;

@RestController
public class NauticalWarningController {

    String DUMMY_DATA = "{foo: 'foo'}";

    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + API_V1_BASE_PATH + "/nautical-warnings",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public GeoJSON nauticalWarnings() {
        return new GeoJSON(DUMMY_DATA);
    }

}
