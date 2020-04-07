package com.java.udemy.libraryapi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.udemy.libraryapi.api.dto.BookDTO;
import com.java.udemy.libraryapi.exception.BusinessException;
import com.java.udemy.libraryapi.model.entity.Book;
import com.java.udemy.libraryapi.service.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
//Não precisa adicionar no Test de Service pois elas configuram testes para a API Rest
@WebMvcTest
@AutoConfigureMockMvc // o spring vai fazer uma configuração no teste , onde vai configurar o objeto para fazer as requisições
public class BookControllerTest {

    static String BOOK_API = "/api/books";
    // MockBean - Mock especializado - para criar uma instância mockada, e colocar dentro do contexto de injeção de depêndencia
    // que foi trazida através do @ExtendWith
    @MockBean
    BookService service;

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        BookDTO dto = createNewBook();
        Book savedBook = Book.builder()
                .id(10l)
                .tittle("A garota mais linda do mundo")
                .author("Ilara Almeida")
                .isbn("001").build();

        //o save apenas está simulando
        // o BDD given pega o service.save onde ele espera retornar o SavedBook da classe Book.java
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto);

          MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
          mvc
                  .perform(request)
                  .andExpect( status().isCreated() )
                  .andExpect( jsonPath("id").isNotEmpty() )
                  .andExpect( jsonPath("tittle").value(dto.getTittle()) )
                  .andExpect( jsonPath("author").value(dto.getAuthor()) )
                  .andExpect( jsonPath("isbn").value(dto.getIsbn()) )
          ;
    }

    //validação de integridadae do objeto
    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro")
    public void createdInvalidBookTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(3))) //que serão os tres erros, das 3 propriedades do BookDTO
        ;

    }

    //validação com uma regra de negócio
    @Test
    @DisplayName("Vai lançar ao tentar cadastrar livro com ISBN já utilizado por outro")
    public void createBookWithDuplicatedIsbn() throws Exception {

        BookDTO dto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "ISBN já cadastrado";
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                ;

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro))
        ;
    }

    private BookDTO createNewBook() {
        return BookDTO.builder().tittle("A garota mais linda do mundo").author("Ilara Almeida").isbn("001").build();
    }
}

//biblioteca ModelMapper -> Serve para mapear uma classe, no caso uma instancia de um objeto exemplo Order para um OrderDTO e
//distribui as propriedades de uma para outra