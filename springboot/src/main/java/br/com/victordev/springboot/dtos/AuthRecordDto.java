package br.com.victordev.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

public record AuthRecordDto(@NotBlank String login, @NotBlank String password) {

}
