package com.thewinningteam.pms.Service.ServiceImpl;

import com.thewinningteam.pms.DTO.BrowseServiceProviderDTO;
import com.thewinningteam.pms.DTO.ServiceProviderDTO;
import com.thewinningteam.pms.Repository.*;
import com.thewinningteam.pms.Service.ServiceProviderService;
import com.thewinningteam.pms.mapper.AddressMapper;
import com.thewinningteam.pms.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProviderServiceImpl implements ServiceProviderService {
    private final ServiceProviderRepository serviceProviderRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final ProfileRepository profileRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final EducationRepository educationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressMapper addressMapper;
    private final CategoryRepository categoryRepository;
    private final AdminServiceImpl adminService;




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

        Role role = roleRepository.findByName(ERole.ROLE_SERVICE_PROVIDER).orElseThrow(() -> new IllegalArgumentException("Role not found."));

        serviceProvider.setRoles((Role) role);

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
        serviceProvider.setPassword(passwordEncoder.encode(serviceProvider.getPassword()));

        serviceProvider.setProfile(profile);

        Category category = serviceProvider.getCategory();
        if (category != null && category.getCategoryId() == null) {
            category = categoryRepository.save(category);
            serviceProvider.setCategory(category);
        }

        return serviceProviderRepository.save(serviceProvider);
    }

    @Override
    public ServiceProviderDTO GetServiceProviderById(Long userID) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(userID)
                .orElseThrow(() -> new UsernameNotFoundException("Service Provider not found with ID: " +userID));;
        return adminService.convertToDto(serviceProvider);
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

    @Override
    public List<BrowseServiceProviderDTO> browseServiceProviders() {
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAll();
        return serviceProviders.stream()
                .map(this::mapToBrowseServiceProviderDTO)
                .collect(Collectors.toList());
    }

    public BrowseServiceProviderDTO mapToBrowseServiceProviderDTO(ServiceProvider serviceProvider) {
        BrowseServiceProviderDTO dto = new BrowseServiceProviderDTO();
        dto.setFirstName(serviceProvider.getFirstName());
        dto.setLastName(serviceProvider.getLastName());
        dto.setAddress(addressMapper.toDTO(serviceProvider.getAddress()));
        dto.setPictures(serviceProvider.getProfilePicture());

        // Calculate average rating
        List<Review> reviews = serviceProvider.getReviews();
        if (reviews != null && !reviews.isEmpty()) {
            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            dto.setRating(averageRating);
        } else {
            // No reviews available, set default rating to 0
            dto.setRating(0.0);
        }

        return dto;
    }


}
