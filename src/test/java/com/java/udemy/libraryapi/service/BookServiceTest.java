package com.java.udemy.libraryapi.service;

import com.java.udemy.libraryapi.exception.BusinessException;
import com.java.udemy.libraryapi.model.entity.Book;
import com.java.udemy.libraryapi.model.repository.BookRepository;
import com.java.udemy.libraryapi.service.impl.BookServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendsWith(SpringExtends)  o spring deve criar um mini contexto para rodar o teste
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
//Não precisa adicionar as annoations abaixo no Test de Service pois elas configuram testes para a API Rest
//@WebMvcTest
//@AutoConfigureMockMvc
// o spring vai fazer uma configuração no teste , onde vai configurar o objeto para fazer as requisições
//essa camada fará apenas testes unitarios
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    //Será executado antes de cada teste
    public void setUp(){
        this.service = new BookServiceImpl( repository ) {

        };

    }

    @Test
    @DisplayName("Deve salva um livro")
    public void saveBookTest(){

        //cenario
        Book book = createValidBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) )
                .thenReturn(false);
        Mockito.when( repository.save(book) ).thenReturn(Book.builder()
                .id(1l)
                .isbn("123")
                .tittle("Melhor Irmao")
                .author("Igor").build());

        //execucao
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTittle()).isEqualTo("Melhor Irmao");
        assertThat(savedBook.getAuthor()).isEqualTo("Igor");

    }

    @Test
    @DisplayName("Deve lançar um erro de negocio ao tentar salvar um livro com isbn já cadastrado")
    public void sholdNotSaveABookWithDuplicatedISBN(){
        //cenario
        Book book = createValidBook();
        Mockito.when( repository.existsByIsbn(Mockito.anyString()) )
                .thenReturn(true);

        //execucao
        Throwable  exception = Assertions.catchThrowable(() -> service.save(book));

        //verificacao
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já cadastrado");
        Mockito.verify(repository, Mockito.never()).save(book);

    }

    private Book createValidBook() {
        return Book.builder()
                .isbn("123")
                .tittle("Melhor Irmao")
                .author("Igor").build();
    }
}
