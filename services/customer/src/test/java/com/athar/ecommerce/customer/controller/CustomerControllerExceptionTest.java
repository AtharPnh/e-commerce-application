package com.athar.ecommerce.customer.controller;

import com.athar.ecommerce.customer.CustomerService;
import com.athar.ecommerce.customer.handler.GlobalExceptionHandler;
import com.athar.ecommerce.exception.CustomerNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {com.athar.ecommerce.customer.CustomerController.class, GlobalExceptionHandler.class})
class CustomerControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateCustomer_whenCustomerNotFound_shouldReturn404WithErrorMessage() throws Exception {
        // Arrange
        String customerId = "123";
        String errorMessage = "Cannot update customer:: Customer with id 123 not found";
        
        when(customerService.updateCustomer(any()))
                .thenThrow(new CustomerNotFoundException(errorMessage));

        String requestBody = """
                {
                    "id": "123",
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "address": {
                        "street": "123 Main St",
                        "houseNumber": "Apt 1",
                        "zipCode": "12345"
                    }
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void updateCustomer_whenInvalidRequest_shouldReturn400WithValidationErrors() throws Exception {
        // Arrange - Invalid request with missing required fields
        String requestBody = """
                {
                    "id": "",
                    "firstName": "",
                    "lastName": "",
                    "email": "invalid-email",
                    "address": null
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors").isMap());
    }
}
