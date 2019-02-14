package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_BETA_BASE_PATH)
@ConditionalOnWebApplication
public class BetaController {
}
