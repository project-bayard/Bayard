package edu.usm.web;

import edu.usm.domain.ConfigDto;
import edu.usm.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andrew on 1/27/16.
 */
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ConfigDto getImplementationConfig() {
        return configService.getImplementationConfig();
    }

}
