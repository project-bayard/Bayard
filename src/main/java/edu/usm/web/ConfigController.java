package edu.usm.web;

import edu.usm.domain.BayardConfig;
import edu.usm.dto.Response;
import edu.usm.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by andrew on 1/27/16.
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * @return The BayardConfig for this implementation
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public BayardConfig getImplementationConfig() {
        return configService.getImplementationConfig();
    }

    /**
     * Updates the BayardConfig for this implementation
     * @param config The new BayardConfig
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public Response updateImplementationConfig(@RequestBody BayardConfig config){
        configService.updateConfig(config);
        return Response.successGeneric();
    }

    /**
     * @return A BayardConfig instance that only indicates whether or not startup mode is enabled
     */
    @RequestMapping(value = "/startup", method = RequestMethod.GET, produces = "application/json")
    public BayardConfig isStartupMode() {
        return configService.getStartupMode();
    }

    /**
     * Disables startupMode in the BayardConfig for this implementation
     * @return
     */
    @RequestMapping(value = "/startup/disable", method = RequestMethod.POST, produces = "application/json")
    public Response disableStartupMode(){
        configService.disableStartupMode();
        return Response.successGeneric();
    }

}