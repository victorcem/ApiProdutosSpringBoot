package br.com.victordev.springboot.dtos;

import br.com.victordev.springboot.models.user.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterRecordDto(@NotBlank String login, @NotBlank String password, UserRole role) {}