package fi.livi.digitraffic.meri.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Jax2bMarshallerConfiguration {

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPaths(
                "ibnet_baltice_ports",
                "ibnet_baltice_schema",
                "ibnet_baltice_waypoints",
                "ibnet_baltice_winterships");
        return marshaller;
    }
}