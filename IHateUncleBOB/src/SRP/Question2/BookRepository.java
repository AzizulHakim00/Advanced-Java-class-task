package SRP.Question2;

public class BookRepository {

    public void saveBook(Book book) {
        IO.println(book.getTitle() + "is saved successfully");
    }
}
