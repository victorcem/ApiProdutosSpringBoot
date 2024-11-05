package br.com.victordev.springboot.dtos;

import jakarta.validation.constraints.NotNull;

public record LoginResponseRecordDto(@NotNull String token) {
}
