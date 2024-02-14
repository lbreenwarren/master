public class Book {

    String book;
    Author[] authors;
    double price;
    
    public Book(String book, Author[] authors, double price) {
         this.book = book;
         this.authors = authors;
         this.price = price;
    }

    public String getTitle() {
        return book;
    }

    public void setTitle(String title) {
        book = title;
    }
    
    public Author[] geAuthors() {
        return authors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString() {
        return "Book[title =" + book + ", authors=" + authors + ", price=" + price;
    }

    public String getAuthorNames() {
        String ret = "";
        for (int i = 0; i < authors.length; ++i) {
            ret += authors[i];
            if (i != authors.length - 1) {
                ret += ", ";
            }
        }
        return ret;
    }
    
}

