package com.java.udemy.libraryapi.model.repository;

import com.java.udemy.libraryapi.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

//O JpaRepository recebe dois parâmetros de entrada que são:
//entidade que será trabalhada dentro do repository
//é o tipo da propriedade do id, da chave primária Book
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
