package com.thewinningteam.pms.Service;


import com.thewinningteam.pms.DTO.BrowseServiceProviderDTO;
import com.thewinningteam.pms.DTO.RequestSystemWideDTO;
import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.model.ServiceProvider;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderService {
    public ServiceProvider SaveServiceProvider(ServiceProvider serviceProvider);
    public ServiceProviderDTO GetServiceProviderById(Long userID);
    public void DeleteServiceProviderById(Long id);
    public void UpdateServiceProviderAccessStatus(Long id);
    public Optional<ServiceProviderDTO> updateCustomer(ServiceProvider updatedServiceProvider);

    List<BrowseServiceProviderDTO> browseServiceProviders();
}
