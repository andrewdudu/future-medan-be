package com.future.medan.backend.seeders;

import com.future.medan.backend.models.entity.PaymentMethod;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.repositories.PaymentMethodRepository;
import com.future.medan.backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    private RoleRepository roleRepository;

    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public DatabaseSeeder(
            RoleRepository roleRepository,
            PaymentMethodRepository paymentMethodRepository) {
        this.roleRepository = roleRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedPaymentMethodTable();
        seedRolesTable();
    }

    private void seedPaymentMethodTable() {
        seedPaymentMethod("BNI", "BANK_TRANSFER");
        seedPaymentMethod("BANK MANDIRI", "BANK_TRANSFER");
        seedPaymentMethod("BTPN", "BANK_TRANSFER");
        seedPaymentMethod("BCA", "BANK_TRANSFER");
        seedPaymentMethod("BCA Virtual Account", "VIRTUAL_BANK");
        seedPaymentMethod("BNI Virtual Account", "VIRTUAL_BANK");
    }

    private void seedPaymentMethod(String name, String type) {
        Boolean isExists = paymentMethodRepository.existsByNameAndType(name, type);

        if (!isExists) {
            PaymentMethod paymentMethod = new PaymentMethod();

            paymentMethod.setName(name);
            paymentMethod.setType(type);

            paymentMethodRepository.save(paymentMethod);
        }
    }

    private void seedRolesTable() {
        seedRole(RoleEnum.ROLE_ADMIN);
        seedRole(RoleEnum.ROLE_MERCHANT);
        seedRole(RoleEnum.ROLE_USER);
    }

    private void seedRole(RoleEnum roleEnum) {
        if (!roleRepository.existsByName(roleEnum)) {
            Role role = new Role();
            role.setName(roleEnum);

            roleRepository.save(role);
        }
    }
}
