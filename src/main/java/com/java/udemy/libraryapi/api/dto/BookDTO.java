package com.java.udemy.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder //ela faz com que o Lookbok gera um builder para classe alvo que facilita a criação das instâncias
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;

    @NotEmpty
    private String tittle;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;
}
