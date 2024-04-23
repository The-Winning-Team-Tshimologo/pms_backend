package com.thewinningteam.pms.Controller;


import com.thewinningteam.pms.DTO.BrowseServiceProviderDTO;
import com.thewinningteam.pms.Service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service-providers")
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @Autowired
    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")

    @GetMapping("/browse")
    public List<BrowseServiceProviderDTO> browseServiceProviders() {
        return serviceProviderService.browseServiceProviders();
    }
}
