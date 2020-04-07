package com.java.udemy.libraryapi.service.impl;

import com.java.udemy.libraryapi.exception.BusinessException;
import com.java.udemy.libraryapi.model.entity.Book;
import com.java.udemy.libraryapi.model.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.nio.BufferOverflowException;

@Service
//para ser gerenciado pelo spring framework, quando for injetar no BookController
//precisa de uma implementação do BookService e só vai achar a implementação se ele estiver dentro do Container
//e a annotation @Service serve para isso
public class BookServiceImpl implements com.java.udemy.libraryapi.service.BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("ISBN já cadastrado");
        }
        return repository.save(book);
    }
}

//precisa implementar o book onde ele vai retornar uma instancia salva na base do book
//jpa repository - é uma interface do springdatajpa que serve para fazer operações da entidade na base de dados
