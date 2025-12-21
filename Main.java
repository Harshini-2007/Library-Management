import java.util.*;

class User {
    static int NextID = 1;
    int usrid;
    String name;
    ArrayList<Book> issued = new ArrayList<>();

    public void registration(String name) {
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

    HashMap<Long, Book> books = new HashMap<>();                 
    HashMap<String, List<Book>> titleMap = new HashMap<>();      
    HashMap<String, List<Book>> authorMap = new HashMap<>();     
    HashMap<Integer, User> users = new HashMap<>();

    Library() {
        addBook(10000L, "You Can Win", "Shiv Khera", "Motivation");
        addBook(10002L, "AI Basics", "John Doe", "AI");
        addBook(10003L, "DSA", "Jane Smith", "Programming");
    }

    public void addUser(User u) {
        users.put(u.usrid, u);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

  
    public void addBook(long ISBN, String title, String author, String category) {

        Book b = new Book(ISBN, title, author, category);
        books.put(ISBN, b);

        titleMap.computeIfAbsent(title.toLowerCase(), k -> new ArrayList<>()).add(b);
        authorMap.computeIfAbsent(author.toLowerCase(), k -> new ArrayList<>()).add(b);

        System.out.println("Book Added Successfully!");
    }

    public void displayBooks() {
        System.out.println("\n------ LIBRARY BOOKS ------");
        for (Book b : books.values()) {
            System.out.println(b);
        }
    }

    
    public void searchBook(String key) {

      
        try {
            long isbn = Long.parseLong(key);
            Book b = books.get(isbn);
            if (b != null) {
                System.out.println("Book Found:");
                System.out.println(b);
                return;
            }
        } catch (NumberFormatException ignored) {}

        
        List<Book> byTitle = titleMap.get(key.toLowerCase());
        if (byTitle != null) {
            byTitle.forEach(System.out::println);
            return;
        }

     
        List<Book> byAuthor = authorMap.get(key.toLowerCase());
        if (byAuthor != null) {
            byAuthor.forEach(System.out::println);
            return;
        }

       
        System.out.println("Book Not Found!");
        System.out.println("Did you mean?");
        boolean suggestion = false;

        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(key.toLowerCase()) ||
                b.author.toLowerCase().contains(key.toLowerCase())) {

                System.out.println("- " + b.title + " by " + b.author);
                suggestion = true;
            }
        }

        if (!suggestion) {
            System.out.println("No similar books found.");
        }
    }

    public void issueBook(User u, long isbn) {
        Book b = books.get(isbn);

        if (b == null) {
            System.out.println("Book Not Found!");
            return;
        }

        if (b.bookStatus.equals("Available")) {
            b.bookStatus = "Issued";
            u.issued.add(b);
            System.out.println("Book Issued Successfully!");
        } else {
            System.out.println("Book Already Issued!");
        }
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

                case 1 -> {
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
                }

                case 2 -> {
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    User u = new User();
                    u.registration(name);
                    l.addUser(u);
                }

                case 3 -> {
                    System.out.print("Enter Title / Author / ISBN: ");
                    String key = sc.nextLine();
                    l.searchBook(key);
                }

                case 4 -> {
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
                }

                case 5 -> {
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
                }

                case 6 -> l.displayBooks();
                case 0 -> System.out.println("Thank You!");
                default -> System.out.println("Invalid Choice!");
            }

        } while (choice != 0);
    }
}
