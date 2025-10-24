package com.athar.ecommerce.customer;

import com.athar.ecommerce.exception.CustomerNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.List;
import java.util.Optional;
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

    // Test Data Helper Methods
    private Address createValidAddress() {
        return Address.builder()
                .street("123 Main Street")
                .houseNumber("Apt 4B")
                .zipCode("12345")
                .build();
    }

    private Address createInvalidAddress() {
        return Address.builder()
                .street("street")
                .houseNumber("houseNumber")
                .zipCode("zipCode")
                .build();
    }

    private CustomerRequest createValidCustomerRequest() {
        return new CustomerRequest(
                "1", "Athar", "Panahi", "athar.panahi@gmail.com", createValidAddress()
        );
    }

    private CustomerRequest createInvalidEmailCustomerRequest() {
        return new CustomerRequest(
                "1", "Athar", "Panahi", "ath.pnhgmail", createInvalidAddress()
        );
    }

    private CustomerRequest createCustomerRequestWithId(String id) {
        return new CustomerRequest(
                id, "Athar", "Panahi", "athar.panahi@gmail.com", createValidAddress()
        );
    }

    private Customer createValidCustomer() {
        return Customer.builder()
                .id("1")
                .firstName("Athar")
                .lastName("Panahi")
                .email("athar.panahi@gmail.com")
                .address(createValidAddress())
                .build();
    }

    private Customer createCustomerWithId(String id) {
        return Customer.builder()
                .id(id)
                .firstName("Athar")
                .lastName("Panahi")
                .email("athar.panahi@gmail.com")
                .address(createValidAddress())
                .build();
    }

    private List<Customer> createListOfCustomers() {
        var c1 = Customer.builder()
                .id("1")
                .firstName("Athar")
                .lastName("Panahi")
                .email("athar.panahi@gmail.com")
                .address(createValidAddress())
                .build();

        var c2 = Customer.builder()
                .id("2")
                .firstName("Bahador")
                .lastName("Soleimani")
                .email("b.s@gmail.com")
                .address(createValidAddress())
                .build();
        return List.of(c1, c2);
    }

    @Test
    void customer_with_invalid_email_fails_bean_validation() {
        // Arrange
        var request = createInvalidEmailCustomerRequest();

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
        var request = createValidCustomerRequest();
        var customer = createValidCustomer();

        when(mapper.toCustomer(request)).thenReturn(customer);
        when(repository.save(customer)).thenReturn(customer);

        // Act
        String result = sut.createCustomer(request);

        // Assert
        assertEquals("1", result);
        verify(mapper).toCustomer(request);
        verify(repository).save(customer);
    }

    @Test
    void update_not_existed_customer_is_failed() {
        // Arrange
        var request = createValidCustomerRequest();

        // Mock repository to return empty Optional (customer not found)
        when(repository.findById("1")).thenReturn(java.util.Optional.empty());

        // Act & Assert
        var exception = assertThrows(CustomerNotFoundException.class, () -> {
            sut.updateCustomer(request);
        });

        // Verify business outcome - the correct exception message
        assertEquals("Cannot update customer:: Customer with id 1 not found", exception.getMsg());
    }

    @Test
    void update_existing_customer_successful() {
        // Arrange
        var request = createValidCustomerRequest();
        var customer = createValidCustomer();

        when(repository.findById(request.id())).thenReturn(Optional.ofNullable(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        //Act
        sut.updateCustomer(request);

        //Assert
        // Test passes if no exception is thrown - that's the business outcome

    }

    @Test
    void update_customer_with_null_id_is_not_successful() {
        //Arrange
        var request = createCustomerRequestWithId("");

        when(repository.findById(request.id())).thenReturn(Optional.ofNullable(null));

        //Act & Assert
        var exception = assertThrows(CustomerNotFoundException.class, () -> {
            sut.updateCustomer(request);
        });

        assertEquals("Cannot update customer:: Customer with id  not found", exception.getMsg());

    }

    @Test
    void update_customer_with_different_id_is_not_successful() {
        //Arrange
        var request = createCustomerRequestWithId("999");

        when(repository.findById(request.id())).thenReturn(Optional.empty());

        //Act & Assert
        var exception = assertThrows(CustomerNotFoundException.class, () -> {
            sut.updateCustomer(request);
        });

        assertEquals("Cannot update customer:: Customer with id 999 not found", exception.getMsg());

    }

    @Test
    void get_All_customers() {
        //Arrange
        var customers = createListOfCustomers();
        when(repository.findAll()).thenReturn(customers);

        //Act
        List<CustomerResponse> responses = sut.findAllCustomers();

        //Assert
        verify(repository).findAll();
        verify(mapper, times(2)).fromCustomer(any(Customer.class));
        assertEquals(2, responses.size());
    }


}