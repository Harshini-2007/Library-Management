import java.util.*;

class User {
    static int NextID = 1;

    int usrid;
    String name;
    ArrayList<Book> issued = new ArrayList<>();

    public void registeration(String name) {
        this.name = name;
        this.usrid = NextID++;
        System.out.println("User Registered Successfully!");
        System.out.println("User ID: " + usrid);
    }
}

class Book {
    long ISBN;
    String title;
    String author;
    String category;
    String bookStatus = "Available";

    Book(long ISBN, String title, String author, String category) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    public String toString() {
        return "ISBN: " + ISBN +
               "\nTitle: " + title +
               "\nAuthor: " + author +
               "\nCategory: " + category +
               "\nStatus: " + bookStatus + "\n";
    }
}

class Library {

    ArrayList<Book> lib = new ArrayList<>(Arrays.asList(
            new Book(10000L, "You Can Win", "Shiv Khera", "Motivation"),
            new Book(10002L, "AI Basics", "John Doe", "AI"),
            new Book(10003L, "DSA", "Jane Smith", "Programming")
    ));

    ArrayList<User> users = new ArrayList<>();

   

    public void addUser(User u) {
        users.add(u);
    }

    public User getUserById(int id) {
        for (User u : users) {
            if (u.usrid == id) {
                return u;
            }
        }
        return null;
    }

  

    public void addBook(long ISBN, String title, String author, String category) {
        lib.add(new Book(ISBN, title, author, category));
        System.out.println("Book Added Successfully!");
    }

    public void displayBooks() {
        System.out.println("\n------ LIBRARY BOOKS ------");
        for (Book b : lib) {
            System.out.println(b);
        }
    }

    public void searchBook(String key) {
        boolean found = false;

        for (Book b : lib) {
            if (b.title.equalsIgnoreCase(key) ||
                b.author.equalsIgnoreCase(key)) {

                System.out.println(b);
                found = true;
                return;
            }

            try {
                long isbn = Long.parseLong(key);
                if (b.ISBN == isbn) {
                    System.out.println(b);
                    found = true;
                    return;
                }
            } catch (NumberFormatException e) {}
        }

        if (!found) {
            System.out.println("Book Not Found!");
        }
    }

     

    public void issueBook(User u, long isbn) {
        for (Book b : lib) {
            if (b.ISBN == isbn) {
                if (b.bookStatus.equals("Available")) {
                    b.bookStatus = "Issued";
                    u.issued.add(b);
                    System.out.println("Book Issued Successfully!");
                    return;
                } else {
                    System.out.println("Book Already Issued!");
                    return;
                }
            }
        }
        System.out.println("Book Not Found!");
    }

    public void returnBook(User u, long isbn) {
        Iterator<Book> it = u.issued.iterator();

        while (it.hasNext()) {
            Book b = it.next();
            if (b.ISBN == isbn) {
                b.bookStatus = "Available";
                it.remove();
                System.out.println("Book Returned Successfully!");
                return;
            }
        }
        System.out.println("Book Not Issued By This User!");
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Library l = new Library();

        int choice;

        do {
            System.out.println("""
                    
                    ===== LIBRARY MANAGEMENT =====
                    1. Add Book
                    2. Register User
                    3. Search Book
                    4. Issue Book
                    5. Return Book
                    6. Display Books
                    0. Exit
                    """);

            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

                case 1:
                    System.out.print("ISBN: ");
                    long isbn = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Title: ");
                    String title = sc.nextLine();

                    System.out.print("Author: ");
                    String author = sc.nextLine();

                    System.out.print("Category: ");
                    String category = sc.nextLine();

                    l.addBook(isbn, title, author, category);
                    break;

                case 2:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    User u = new User();
                    u.registeration(name);
                    l.addUser(u);
                    break;

                case 3:
                    System.out.print("Enter Title / Author / ISBN: ");
                    String key = sc.nextLine();
                    l.searchBook(key);
                    break;

                case 4:
                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt();

                    User issueUser = l.getUserById(uid);
                    if (issueUser == null) {
                        System.out.println("User Not Found!");
                        break;
                    }

                    System.out.print("Enter ISBN to Issue: ");
                    long issueIsbn = sc.nextLong();

                    l.issueBook(issueUser, issueIsbn);
                    break;

                case 5:
                    System.out.print("Enter User ID: ");
                    int rid = sc.nextInt();

                    User returnUser = l.getUserById(rid);
                    if (returnUser == null) {
                        System.out.println("User Not Found!");
                        break;
                    }

                    System.out.print("Enter ISBN to Return: ");
                    long returnIsbn = sc.nextLong();

                    l.returnBook(returnUser, returnIsbn);
                    break;

                case 6:
                    l.displayBooks();
                    break;

                case 0:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 0);
    }
}
