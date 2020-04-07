package com.java.udemy.libraryapi.api.resource;

import com.java.udemy.libraryapi.api.dto.BookDTO;
import com.java.udemy.libraryapi.api.exceptions.apiErrors;
import com.java.udemy.libraryapi.exception.BusinessException;
import com.java.udemy.libraryapi.model.entity.Book;
import com.java.udemy.libraryapi.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final ModelMapper modelMapper;
    private BookService service;

    public BookController(BookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) //Status que retorne por padrão
    //Request Body diz que o parametro BOOKkDTO vai ser o corpo deve o corpo da requisição que o client vai mandar para a API
    //

    //é uma boa prática sempre receber e retornar um dto para nunca expor algumas propriedades das entidades
    //o @Valid vai fazer com que o SpringMVC ele valide o objeto baseado nas annotations de @NotEmpty do BootDTO
    public BookDTO create(@RequestBody @Valid BookDTO dto){
//        BookDTO dto = new BookDTO();
//        dto.setId(1l);
//        dto.setTittle("Meu Livro");
//        dto.setAuthor("Autor");
//        dto.setIsbn("1234");

        //Sem o Model Mapper
//        Book entity = Book.builder()
//                .tittle(dto.getTittle())
//                .author(dto.getAuthor())
//                .isbn(dto.getIsbn()).build();

        //Com o Model Mapper
        Book entity = modelMapper.map( dto, Book.class );
        entity = service.save(entity);

        //Sem o Model Mapper
//        return BookDTO.builder()
//                .id(entity.getId())
//                .tittle(entity.getTittle())
//                .author(entity.getAuthor())
//                .isbn(entity.getIsbn()).build();
        //Com o Model Mapper
        return modelMapper.map( entity, BookDTO.class );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //foi preciso colocar o HTTP status BAD_REQUEST porque no teste tava vindo com Status padrão(200)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public apiErrors handleValidationExceptions(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        return new apiErrors(bindingResult);

    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //foi preciso colocar o HTTP status BAD_REQUEST porque no teste tava vindo com Status padrão(200)
    @ExceptionHandler(BusinessException.class)
    public apiErrors handleBussinessException(BusinessException ex) {
        return new apiErrors(ex);
    }
}
//getAllErrors - consegue todos os erros que teve na validação
//bindResult - é o resultado da validação que ocorreu ao validar o objeto anotado com o @verde.
//MethodArgumentNotValidException é lançada toda vez quando tenta validar um objeto e ele não está válido

//ExceptionHandler - É uma forma que o Spring dá para a gente tratar os Exceptions que acontece na nossa api para
//mapeamento de retorno
