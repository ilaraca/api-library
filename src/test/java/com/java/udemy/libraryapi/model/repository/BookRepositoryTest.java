package com.java.udemy.libraryapi.model.repository;

import com.java.udemy.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("tests")
@DataJpaTest
//essa annotation indica que vai fazer testes com o JPA, criando uma instancia do banco de dados
//em memoria e apenas para executar os testes da classe e no final irá apagar
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    //objeto usado exclusivamente para criar um cenario, simulando um Entity Manager, configurado
    //especificamente para fazer testes

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
    public void returnTrueWhenISBNExists(){
        //cenario
        String ISBN = "123";

        Book book = Book.builder().tittle("As Aventuras").author("Fulano").isbn(ISBN).build();
        entityManager.persist(book);
        //dentro das implementações do repository para executar as operações na base de dados

        //execucao
        boolean exists = repository.existsByIsbn(ISBN);
        //verificacao
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando existir um livro na base com o isbn informado")
    public void returnFalseWhenISBNDoesntExists(){
        //cenario
        String ISBN = "123";

        //execucao
        boolean exists = repository.existsByIsbn(ISBN);
        //verificacao
        assertThat(exists).isFalse();
    }

}
