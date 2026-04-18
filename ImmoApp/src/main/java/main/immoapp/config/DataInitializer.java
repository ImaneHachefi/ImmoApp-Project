package main.immoapp.config;

import main.immoapp.entity.Role;
import main.immoapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) {
        List<String> roles = List.of(
                "ADMIN",
                "AGENT_COMMERCIAL",
                "AGENT_SAV",
                "CLIENT"
        );

        for (String nom : roles) {
            if (roleRepository.findByNom(nom).isEmpty()) {
                Role role = new Role();
                role.setNom(nom);
                role.setDescription(nom);
                roleRepository.save(role);
                System.out.println("✅ Rôle créé : " + nom);
            } else {
                System.out.println("ℹ️ Rôle déjà existant : " + nom);
            }
        }
    }
}