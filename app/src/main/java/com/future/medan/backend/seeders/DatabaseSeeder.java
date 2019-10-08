package com.future.medan.backend.seeders;

import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder {

    private RoleRepository roleRepository;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(
            RoleRepository roleRepository,
            JdbcTemplate jdbcTemplate) {
        this.roleRepository = roleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRolesTable();
    }

    private void seedRolesTable() {
        seedRole("ROLE_ADMIN", RoleEnum.ROLE_ADMIN);
        seedRole("ROLE_MERCHANT", RoleEnum.ROLE_MERCHANT);
        seedRole("ROLE_USER", RoleEnum.ROLE_USER);
    }

    private void seedRole(String roleName, RoleEnum roleEnum) {
        String sql = "SELECT name FROM roles R WHERE R.name = '" + roleName + "'";
        List<Role> u = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
        if (u == null || u.size() <= 0) {
            Role role = new Role();
            role.setName(roleEnum);

            roleRepository.save(role);
        }
    }
}
