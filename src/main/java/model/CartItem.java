package model;

public class CartItem {
	private String bookTitle;
    private int quantity;

    public CartItem(String bookTitle, int quantity) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

}
