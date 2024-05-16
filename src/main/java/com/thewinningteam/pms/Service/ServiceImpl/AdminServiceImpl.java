package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.*;
import com.thewinningteam.pms.Repository.ServiceProviderRepository;
import com.thewinningteam.pms.Service.AdminService;
import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<ServiceProvider> getAllPendingServiceProviders() {
//        System.out.println( serviceProviderRepository.findByAcceptanceStatus(AcceptanceStatus.PENDING));
        return null;
    }

    @Override
    public List<ServiceProviderDTO> findByAcceptanceStatus(AcceptanceStatus status) {
//        System.out.println(serviceProviderRepository.findByAcceptanceStatus(status));
//        System.out.println("Acceptance Status " +status);
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findByAcceptanceStatus(status);
//        System.out.println(serviceProviders.size());
        return serviceProviders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public ServiceProviderDTO ChangeAcceptanceStatus(Long SPID, AcceptanceStatus status) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(SPID)
                .orElseThrow(() -> new UsernameNotFoundException("Service Provider not found with ID: " +SPID));;
        serviceProvider.setAcceptanceStatus(status);
        return convertToDto(serviceProviderRepository.save(serviceProvider));
    }

    public ServiceProviderDTO convertToDto(ServiceProvider serviceProvider) {
        System.out.println("test 1 " + serviceProvider.getName() + " uid " + serviceProvider.getUserId());
        ServiceProviderDTO serviceProviderDTO = modelMapper.map(serviceProvider, ServiceProviderDTO.class);
        serviceProviderDTO.setRole(modelMapper.map(serviceProvider.getRoles(), RoleDTO.class));

        System.out.println("ses 1");


        List<ReviewDTO> reviewDTOs = new ArrayList<>();
//        for (Review review : serviceProvider.getReviews()) {
//            reviewDTOs.add(modelMapper.map(review, ReviewDTO.class));
//        }
        serviceProviderDTO.setReviews(reviewDTOs);

        ProfileDTO profileDTO = modelMapper.map(serviceProvider.getProfile(), ProfileDTO.class);

        if (profileDTO == null) {
            profileDTO = new ProfileDTO();
        }

        List<WorkExperienceDTO> workExperienceDTOs = new ArrayList<>();
        for (WorkExperience workExperience : serviceProvider.getProfile().getWorkExperienceList()) {
            WorkExperienceDTO workExperienceDTO = modelMapper.map(workExperience, WorkExperienceDTO.class);
            workExperienceDTOs.add(workExperienceDTO);
        }
        profileDTO.setWorkExperienceList(workExperienceDTOs);

        List<EducationDTO> educationDTOs = new ArrayList<>();
        for (Education education : serviceProvider.getProfile().getEducation()) {
            EducationDTO educationDTO = modelMapper.map(education, EducationDTO.class);
            educationDTOs.add(educationDTO);
        }
        profileDTO.setEducation(educationDTOs);

        serviceProviderDTO.setProfile(profileDTO);

        return serviceProviderDTO;
    }
}
