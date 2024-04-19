package com.thewinningteam.pms.Service;

import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.ServiceProvider;

import java.util.List;

public interface AdminService {
    public List<ServiceProvider> getAllPendingServiceProviders();
    public List<ServiceProvider> findByAcceptanceStatus(AcceptanceStatus status);
}
