package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.model.ServiceProvider;

import java.util.Optional;

public interface ServiceProviderService {
    public ServiceProvider SaveServiceProvider(ServiceProvider serviceProvider);
    public Optional<ServiceProviderDTO> GetServiceProviderById(Long userID);
    public void DeleteServiceProviderById(Long id);
    public void UpdateServiceProviderAccessStatus(Long id);
    public Optional<ServiceProviderDTO> updateCustomer(ServiceProvider updatedServiceProvider);
}
