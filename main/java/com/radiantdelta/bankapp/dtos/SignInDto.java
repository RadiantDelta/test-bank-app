package com.radiantdelta.bankapp.dtos;

import jakarta.validation.constraints.NotEmpty;

public record SignInDto(
        @NotEmpty(message = "Input login cannot be empty.")
    String login,
        @NotEmpty(message = "Input password cannot be empty.")
    String password) {
}
