package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.Repository.ServiceProviderRepository;
import com.thewinningteam.pms.Service.AdminService;
import com.thewinningteam.pms.model.AcceptanceStatus;
import com.thewinningteam.pms.model.ServiceProvider;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ServiceProviderRepository serviceProviderRepository;
    @Override
    public List<ServiceProvider> getAllPendingServiceProviders() {
//        System.out.println( serviceProviderRepository.findByAcceptanceStatus(AcceptanceStatus.PENDING));
        return null;
    }

    @Override
    public List<ServiceProvider> findByAcceptanceStatus(AcceptanceStatus status) {
//        System.out.println(serviceProviderRepository.findByAcceptanceStatus(status));
//        System.out.println("Acceptance Status " +status);
        return serviceProviderRepository.findByAcceptanceStatus(status);
    }
}
