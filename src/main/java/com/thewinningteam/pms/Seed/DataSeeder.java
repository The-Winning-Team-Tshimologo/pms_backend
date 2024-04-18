package com.thewinningteam.pms.Seed;
import com.thewinningteam.pms.Repository.CategoryRepository;
import com.thewinningteam.pms.Repository.RoleRepository;
import com.thewinningteam.pms.Repository.UserRepository;
import com.thewinningteam.pms.model.Category;
import com.thewinningteam.pms.model.ERole;
import com.thewinningteam.pms.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.thewinningteam.pms.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;



    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        seedAdmin();
        seedCategories();

    }

    public void initializeRoles() {

            // roleService.initializeRoles()
            for (ERole roleName : ERole.values()) {
                if (!roleRepository.existsByName(roleName)) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }

    }

    private void seedAdmin() {
        // Check if admin user already exists
        var userEmail =  userRepository.findByEmail("admin@example.com").isPresent();
        if (userEmail) {

            return; // Admin user already exists, skip seeding
        }

        System.out.println(userEmail);
        // If admin user doesn't exist, create and seed it
        User admin = new User();
        admin.setUserName("admin"); // You can set the username if required
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("password"));

        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalArgumentException("Admin role not found."));
        admin.setRoles(adminRole);

        admin.setEnabled(true);

        // Save the admin user to the database
        userRepository.save(admin);
    }
    private void seedCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "PLUMBING"),
                new Category(2L, "ELECTRIC"),
                new Category(3L, "HOUSEKEEPING"),
                new Category(4L, "GARDEN"),
                new Category(5L, "CLEANING")
        );

        // Save the categories to the database
        categoryRepository.saveAll(categories);
    }

}
