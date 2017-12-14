
package fr.gtm.webservice;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


 @ApplicationPath("/proxapi")
public class ConfigAppli extends Application {
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jersey.config.server.provider.packages", "fr.gtm.webservice");
        return properties;
    }
}
