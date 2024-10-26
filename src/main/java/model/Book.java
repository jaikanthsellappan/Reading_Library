package model;

public class Book {
	private String title;
    private String authors;
    private int physicalCopies;
    private double price;
    private int soldCopies;

    // Constructors, getters and setters

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public int getSoldCopies() {
        return soldCopies;
    }

	public void setTitle(String title) {
		this.title = title;
		
	}

	public void setAuthors(String author) {
		this.authors = author;
		
	}

	public void setPhysicalCopies(int copies) {
		this.physicalCopies = copies;
	}

	public void setPrice(double price) {
		this.price = price;
		
	}

	public void setSoldCopies(int soldcopy) {
		this.soldCopies = soldcopy;
		
	}
	

}
