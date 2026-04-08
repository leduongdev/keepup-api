package com.ra.base_spring_boot.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ra.base_spring_boot.utils.CustomDateDeserializer;
import com.ra.base_spring_boot.model.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRegisterRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotNull(message = "Please select your gender.")
    private Gender gender;

    @NotNull(message = "Please enter your birthday.")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Past(message = "Birthday must be a past date")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Email cannot be blank")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Invalid email format!")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long, including uppercase letters and numbers")
    private String password;
}