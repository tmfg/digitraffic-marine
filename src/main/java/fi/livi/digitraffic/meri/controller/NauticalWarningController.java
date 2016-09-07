package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.util.RestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {
    private final RestTemplate template;

    private String pookiUrl;

    public enum Status {
        DRAFT("merivaroitus_luonnos_dt"),
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");

        public final String layer;

        Status(final String layer){
            this.layer = layer;
        }
    }

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pookiUrl) {
        this.pookiUrl = pookiUrl;
        template = new RestTemplate();
        //template.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        template.setErrorHandler(new NauticalWarningErrorHandler());
    }

    @ApiOperation("Return nautical warnings of given status.")
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> nauticalWarnings(@ApiParam(value = "Status", required = true, allowableValues = "DRAFT,PUBLISHED,ARCHIVED" )
                                              @PathVariable final String status) {
        final Status s = Status.valueOf(status.toUpperCase());
        final String url = String.format("%s?layer=%s", pookiUrl, s.layer);

        ResponseEntity<byte[]> response = template.getForEntity(url, byte[].class);

        if (RestUtil.isError(response.getStatusCode())) {
            response = template.getForEntity(url, byte[].class);
        }

        // Pooki unexpectedly returns body in GZip format, try to unzip it
        // If that fails, expect body to be "plain"
        byte[] body;
        try {
            body = decompress(response.getBody());
        } catch (final IOException e) {
            body = response.getBody();
        }

        if (RestUtil.isError(response.getStatusCode())) {
            return ResponseEntity.status(response.getStatusCode()).body(body);
        }

        return ResponseEntity.ok().body(body);
    }

    public static byte[] decompress(final byte[] contentBytes) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        return out.toByteArray();
    }


    public void setPookiUrl(final String pookiUrl) {
        this.pookiUrl = pookiUrl;
    }
}
