package com.thewinningteam.pms.Seed;

import com.thewinningteam.pms.Repository.RoleRepository;
import com.thewinningteam.pms.model.ERole;
import com.thewinningteam.pms.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner initializeRoles() {
        return args -> {
           // roleService.initializeRoles()
            for (ERole roleName : ERole.values()) {
                if (!roleRepository.existsByName(roleName)) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}