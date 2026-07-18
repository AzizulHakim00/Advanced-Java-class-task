package SRP.Question2;

public class BookPrinter {

    public void printBook(Book book) {
        IO.println("Book Title: " + book.getTitle());
        IO.println("Book Author: " + book.getAuthor());
        IO.println("Book ID: " + book.getId());
    }
}
