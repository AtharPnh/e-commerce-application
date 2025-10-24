package com.athar.ecommerce.customer;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;
    @Mock
    private CustomerMapper mapper;
    @InjectMocks
    private CustomerService sut;

    @Test
    void customer_with_invalid_email_fails_bean_validation() {

        // Arrange (Setup remains the same)
        var address = Address.builder()
                .street("street")
                .houseNumber("houseNumber")
                .zipCode("zipCode")
                .build();

        // Invalid Request
        var request = new CustomerRequest(
                "1", "Athar", "Panahi", "ath.pnhgmail", address
        );

        // Bean Validation happens at the controller layer in this app, so here we
        // validate the request directly with a Validator to assert the constraint.
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Bean Validation should flag invalid email");
        assertTrue(
                violations.stream().anyMatch(v -> "email".equals(v.getPropertyPath().toString())),
                "Validation violations should include the email field"
        );
    }

    @Test
    void create_and_save_customer_successful() {
        // Arrange
        var address = Address.builder()
                .street("123 Main Street")
                .houseNumber("Apt 4B")
                .zipCode("12345")
                .build();

        var request = new CustomerRequest(
                "1", "Athar", "Panahi", "athar.panahi@gmail.com", address
        );

        var customer = Customer.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(request.address())
                .build();

        when(mapper.toCustomer(request)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);

        // Act
        String result = sut.createCustomer(request);

        // Assert
        assertEquals("1", result);
        verify(mapper).toCustomer(request);
        verify(repository).save(customer);
    }


}