package com.java.udemy.libraryapi.model.entity;

//mapear como entidade JPA para persistir na base

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Book {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tittle;

    @Column
    private String author;

    @Column
    private String isbn;
}
