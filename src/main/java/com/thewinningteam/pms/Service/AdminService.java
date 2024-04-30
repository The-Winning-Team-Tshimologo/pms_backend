package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.ServiceProvider;

import java.util.List;

public interface AdminService {
    public List<ServiceProvider> getAllPendingServiceProviders();
    public List<ServiceProviderDTO> findByAcceptanceStatus(AcceptanceStatus status);
    public ServiceProviderDTO ChangeAcceptanceStatus(Long SPID, AcceptanceStatus status);
}
