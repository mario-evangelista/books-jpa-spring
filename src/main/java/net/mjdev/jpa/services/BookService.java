package net.mjdev.jpa.services;

import jakarta.transaction.Transactional;
import net.mjdev.jpa.dtos.BookRecordDto;
import net.mjdev.jpa.models.BookModel;
import net.mjdev.jpa.models.ReviewModel;
import net.mjdev.jpa.repositories.AuthorRepository;
import net.mjdev.jpa.repositories.BookRepository;
import net.mjdev.jpa.repositories.PublisherRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;


    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<BookModel> getAllBooks() {
        return bookRepository.findAll();
    }

    /*
    * @Transactional is invoked, a transaction is started before the method is executed and is
    * finished at the end of the method. If the method execution is successful,
    * the transaction is committed. If an exception occurs during method execution,
    * the transaction is rolled back, ensuring that all operations performed within the transaction are undone.
    * */
    @Transactional //
    public BookModel saveBook(BookRecordDto bookRecordDto) {
        BookModel book = new BookModel();
        book.setTitle(bookRecordDto.title());
        book.setPublisher(publisherRepository.findById(bookRecordDto.publisherId()).get());
        book.setAuthors(authorRepository.findAllById(bookRecordDto.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComment(bookRecordDto.reviewComment());
        reviewModel.setBook(book);
        book.setReview(reviewModel);

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }


}
