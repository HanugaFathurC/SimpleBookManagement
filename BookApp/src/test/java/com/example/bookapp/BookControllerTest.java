package com.example.bookapp;

import com.example.bookapp.Controller.BookController;
import com.example.bookapp.Entity.Book;
import com.example.bookapp.Service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBooks() throws Exception {
        Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "9780134685991", 2018);
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "9780132350884", 2008);

        Mockito.when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Effective Java")))
                .andExpect(jsonPath("$[1].title", is("Clean Code")));
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", "9780134685991", 2018);

        Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.author", is("Joshua Bloch")));
    }

    @Test
    void testCreateBook() throws Exception {
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", "9780134685991", 2018);

        Mockito.when(bookService.addBook(Mockito.any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.author", is("Joshua Bloch")));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book(1L, "Effective Java", "Joshua Bloch", "9780134685991", 2018);

        Mockito.when(bookService.updateBook(Mockito.anyLong(), Mockito.any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Effective Java")))
                .andExpect(jsonPath("$.author", is("Joshua Bloch")));
    }

    @Test
    void testDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}
