package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.Repository.*;
import com.thewinningteam.pms.Service.ServiceProviderService;
import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {
    private ServiceProviderRepository serviceProviderRepository;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;
    private AddressRepository addressRepository;
    private ProfileRepository profileRepository;
    private WorkExperienceRepository workExperienceRepository;
    private EducationRepository educationRepository;



    @Override
    public ServiceProvider SaveServiceProvider(ServiceProvider serviceProvider) {
        ServiceProvider existingServiceProvider = serviceProviderRepository.findByEmailOrUserName(serviceProvider.getEmail(), serviceProvider.getUsername());
        if (existingServiceProvider != null) {
            throw new IllegalArgumentException("A service provider with the same email or username already exists.");
        }
        Address address = serviceProvider.getAddress();
        address = addressRepository.save(address);

        serviceProvider.setAddress(address);

//        Long roleID = 2L;
//        Role role = roleRepository.findById(roleID).orElseThrow(() -> new IllegalArgumentException("Role not found."));
////        System.out.println("Retrieved Role: " + role);
//        serviceProvider.setRole(role);

        Role role = roleRepository.findByName(ERole.ROLE_CUSTOMER).orElseThrow(() -> new IllegalArgumentException("Role not found."));

        serviceProvider.setRoles((Role) role);

        System.out.println(serviceProvider.getProfile());

        Profile profile = serviceProvider.getProfile();
        profile = profileRepository.save(profile);

        System.out.println(profile);

        List<WorkExperience> workExperienceList = profile.getWorkExperienceList();
        if (workExperienceList != null) {
            for (WorkExperience workExperience : workExperienceList) {
                workExperience.setProfile(profile);
                workExperienceRepository.save(workExperience);
            }
        }

        List<Education> educationList = profile.getEducation();
        if (educationList != null) {
            for (Education education : educationList) {
                education.setProfile(profile);
                educationRepository.save(education);
            }
        }

        serviceProvider.setProfile(profile);

        return serviceProviderRepository.save(serviceProvider);
    }

    @Override
    public Optional<ServiceProviderDTO> GetServiceProviderById(Long userID) {
        return Optional.empty();
    }

    @Override
    public void DeleteServiceProviderById(Long id) {
        serviceProviderRepository.deleteById(id);

    }

    @Override
    public void UpdateServiceProviderAccessStatus(Long id) {

    }

    @Override
    public Optional<ServiceProviderDTO> updateCustomer(ServiceProvider updatedServiceProvider) {
        return Optional.empty();
    }
}
