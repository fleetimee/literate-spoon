import java.io.*;
import java.util.*;

enum Category {
    DRINK,
    FOOD
}

interface MenuDisplayable {
    void showMenu();
}

class MenuItem {
    private String name;
    private double price;
    private Category category;

    public MenuItem(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}

class Food extends MenuItem implements MenuDisplayable {
    private String type;

    public Food(String name, double price, String type) {
        super(name, price, Category.FOOD);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void showMenu() {
        System.out.println(String.format("%-20s $%-10.2f %-10s", getName(), getPrice(), getType()));
    }
}

class Drink extends MenuItem implements MenuDisplayable {
    private String type;

    public Drink(String name, double price, String type) {
        super(name, price, Category.DRINK);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void showMenu() {
        System.out.println(String.format("%-20s $%-10.2f %-10s", getName(), getPrice(), getType()));
    }
}

class Discount extends MenuItem implements MenuDisplayable {
    private double discount;

    public Discount(String name, double price, Category category, double discount) {
        super(name, price, category);
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public double getPrice() {
        return super.getPrice() - discount;
    }

    @Override
    public void showMenu() {
        System.out.printf("Discount Name: %s, Price: %.2f, Category: %s, Discount: %.2f\n", getName(), getPrice(), getCategory(), getDiscount());
    }
}

class Menu {
    private List<MenuDisplayable> menuItems;

    public Menu() {
        this.menuItems = new ArrayList<>();

        this.menuItems.add(new Food("Pizza", 30000.0, "Italian"));
        this.menuItems.add(new Food("Burger", 40000.0, "American"));
        this.menuItems.add(new Food("Pasta", 50000.0, "Italian"));
        this.menuItems.add(new Food("Steak", 60000.0, "American"));
        this.menuItems.add(new Drink("Soda", 10000.0, "Carbonated"));
        this.menuItems.add(new Drink("Juice", 15000.0, "Fruit"));
        this.menuItems.add(new Drink("Coffee", 20000.0, "Hot"));
        this.menuItems.add(new Drink("Tea", 25000.0, "Hot"));
        this.menuItems.add(new Discount("Weekend Discount", 5000.0, Category.FOOD, 5000.0));
    }

    public void addItem(MenuDisplayable item) {
        menuItems.add(item);
    }

    public void displayMenu() {
        List<MenuDisplayable> discounts = new ArrayList<>();
        Map<Category, List<MenuDisplayable>> sortedMenuItems = new LinkedHashMap<>();
        for (MenuDisplayable item : menuItems) {
            if (item instanceof Discount) {
                discounts.add(item);
            } else if (item instanceof MenuItem) {
                MenuItem menuItem = (MenuItem) item;
                sortedMenuItems.computeIfAbsent(menuItem.getCategory(), k -> new ArrayList<>()).add(item);
            }
        }

        System.out.println("\n----------------------------------------");
        System.out.println("               OUR MENU                 ");
        System.out.println("----------------------------------------");

        System.out.println("\nDISCOUNTS:\n");
        for (MenuDisplayable discount : discounts) {
            discount.showMenu();
        }

        int i = 1;
        for (Map.Entry<Category, List<MenuDisplayable>> entry : sortedMenuItems.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":\n");
            for (MenuDisplayable item : entry.getValue()) {
                item.showMenu();
                i++;
            }
        }
        System.out.println("----------------------------------------\n");
    }

    public List<MenuDisplayable> getMenuItems() {
        return menuItems;
    }
}

class Admin {
    private Menu menu;

    public Admin(Menu menu) {
        this.menu = menu;
    }

    public void addItem(MenuDisplayable item) {
        menu.addItem(item);
    }

    public void updateItem(int itemNumber, String newName, double newPrice) {
        if (itemNumber > 0 && itemNumber <= menu.getMenuItems().size()) {
            MenuItem item = (MenuItem) menu.getMenuItems().get(itemNumber - 1);
            item.setName(newName);
            item.setPrice(newPrice);
        }
    }

    public void removeItem(int itemNumber) {
        if (itemNumber > 0 && itemNumber <= menu.getMenuItems().size()) {
            menu.getMenuItems().remove(itemNumber - 1);
        }
    }
}

class Order {
    private Map<MenuItem, Integer> orderedItems;

    public Order() {
        this.orderedItems = new HashMap<>();
    }

    public void addItem(MenuItem item, int quantity) {
        if (orderedItems.size() < 4) {
            orderedItems.put(item, quantity);
        } else {
            System.out.println("You can only order up to 4 items at a time.");
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : orderedItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        double discount = (total > 100000) ? total * 0.10 : 0; // 10% discount if total exceeds 100000
        total -= discount;
        double tax = total * 0.10; // 10% tax
        double serviceFee = 20000; // fixed service fee
        total += tax + serviceFee;
        return total;
    }

    public void displayOrder() {
        System.out.println("\n----------------------------------------");
        System.out.println("               YOUR ORDER               ");
        System.out.println("----------------------------------------");
        double subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : orderedItems.entrySet()) {
            double accumulatedPrice = entry.getKey().getPrice() * entry.getValue();
            System.out.printf("%-20s $%-5.2f x%-2d $%-5.2f\n", entry.getKey().getName(), entry.getKey().getPrice(), entry.getValue(), accumulatedPrice);
            subtotal += accumulatedPrice;
        }
        double discount = (subtotal > 100000) ? subtotal * 0.10 : 0; // 10% discount if subtotal exceeds 100000
        double tax = (subtotal - discount) * 0.10; // 10% tax
        double serviceFee = 20000; // fixed service fee
        System.out.println("----------------------------------------");
        System.out.printf("SUBTOTAL: $%-30.2f\n", subtotal);
        System.out.printf("DISCOUNT (10%%): $%-30.2f\n", discount);
        System.out.printf("TAX (10%%): $%-30.2f\n", tax);
        System.out.printf("SERVICE FEE: $%-30.2f\n", serviceFee);
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL: $%-30.2f\n", getTotalPrice());
        System.out.println("----------------------------------------\n");
    }
}

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Admin admin = new Admin(menu);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n----------------------------------------");
            System.out.println("   WELCOME TO OUR RESTAURANT");
            System.out.println("----------------------------------------");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("0. Exit");
            System.out.println("----------------------------------------\n");

            System.out.println("Please enter your choice (1 for Customer, 2 for Admin, 0 for Exit):");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("\n----------------------------------------");
                System.out.println("   THANK YOU FOR VISITING OUR RESTAURANT");
                System.out.println("----------------------------------------\n");
                break;
            } else if (choice == 1) {

                Order order = new Order();

                while (true) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               OUR MENU                 ");
                    System.out.println("----------------------------------------");
                    menu.displayMenu();
                    System.out.println("Please enter the name of the item you want to order, 'admin' to switch to admin mode, or 'done' to finish your order:");
                    System.out.println("----------------------------------------\n");

                    scanner.nextLine(); // consume the newline
                    String itemName = scanner.nextLine();

                    if (itemName.equalsIgnoreCase("done")) {
                        System.out.println("\n----------------------------------------");
                        System.out.println("   THANK YOU FOR YOUR ORDER");
                        System.out.println("----------------------------------------\n");
                        break;
                    } else if (itemName.equalsIgnoreCase("admin")) {
                        System.out.println("\n----------------------------------------");
                        System.out.println("   SWITCHING TO ADMIN MODE");
                        System.out.println("----------------------------------------\n");
                        break;
                    } else {
                        MenuItem selectedItem = null;
                        for (MenuDisplayable item : menu.getMenuItems()) {
                            if (item instanceof MenuItem && ((MenuItem) item).getName().equalsIgnoreCase(itemName)) {
                                selectedItem = (MenuItem) item;
                                break;
                            }
                        }
                        if (selectedItem != null) {
                            System.out.println("\n----------------------------------------");
                            System.out.println("You selected " + selectedItem.getName() + ". How many do you want to order?");
                            System.out.println("----------------------------------------\n");
                            int quantity = scanner.nextInt();
                            order.addItem(selectedItem, quantity);
                            System.out.println("\n----------------------------------------");
                            System.out.println("You added " + quantity + " " + selectedItem.getName() + " to your order.");
                            System.out.println("----------------------------------------\n");
                        } else {
                            System.out.println("\n----------------------------------------");
                            System.out.println("Invalid item name. Please try again.");
                            System.out.println("----------------------------------------\n");
                        }
                    }
                }


                // Check if total price exceeds 50000 and offer buy 1 get 1 free Tea
                if (order.getTotalPrice() > 50000) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("   SPECIAL OFFER FOR YOU");
                    System.out.println("----------------------------------------");
                    System.out.println("Your total exceeds 50000. Would you like to buy 1 get 1 free Tea? Enter yes to accept, no to decline:");
                    System.out.println("----------------------------------------\n");
                    String teaOffer = scanner.next();
                    if (teaOffer.equalsIgnoreCase("yes")) {
                        // Find the Tea item
                        MenuItem tea = null;
                        for (MenuDisplayable item : menu.getMenuItems()) {
                            if (item instanceof Drink && ((MenuItem) item).getName().equalsIgnoreCase("Tea")) {
                                tea = (MenuItem) item;
                                break;
                            }
                        }
                        if (tea != null) {
                            // Add 2 Teas to the order for the price of 1
                            order.addItem(tea, 2);
                            System.out.println("\n----------------------------------------");
                            System.out.println("You added 2 Teas to your order for the price of 1.");
                            System.out.println("----------------------------------------\n");
                        } else {
                            System.out.println("\n----------------------------------------");
                            System.out.println("Sorry, Tea is not available in the menu.");
                            System.out.println("----------------------------------------\n");
                        }
                    }
                }
                order.displayOrder();
                System.out.println("Do you want to print the invoice to a text file? Enter yes to confirm, no to decline:");
                String printConfirmation = scanner.next();
                if (printConfirmation.equalsIgnoreCase("yes")) {
                    try {
                        PrintWriter writer = new PrintWriter("invoice.txt", "UTF-8");
                        PrintStream console = System.out;
                        System.setOut(new PrintStream(new FileOutputStream("invoice.txt")));
                        order.displayOrder();
                        System.setOut(console);
                        writer.close();
                        System.out.println("Invoice has been printed to invoice.txt");
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        System.out.println("An error occurred while trying to print the invoice to a text file.");
                    }
                }
                System.exit(0); // Close the application after the order is displayed

            } else if (choice == 2) {
                // Admin functionality
                System.out.println("\n----------------------------------------");
                System.out.println("               ADMIN MENU               ");
                System.out.println("----------------------------------------");
                System.out.println("1. Add an item");
                System.out.println("2. Update an item");
                System.out.println("3. Remove an item");
                System.out.println("4. Import menu from a text file");
                System.out.println("5. Export menu to a text file");
                System.out.println("0. Go back");
                System.out.println("----------------------------------------\n");

                System.out.println("Please enter your choice (1 for Add, 2 for Update, 3 for Remove, 4 for Import, 5 for Export, 0 for Go back):");
                int adminChoice = scanner.nextInt();

                if (adminChoice == 1) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               ADD ITEM                 ");
                    System.out.println("----------------------------------------");
                    System.out.println("Enter the name of the item:");
                    scanner.nextLine(); // consume the newline
                    String name = scanner.nextLine();
                    System.out.println("Enter the price of the item:");
                    double price = scanner.nextDouble();
                    System.out.println("Enter the category of the item (1 for FOOD, 2 for DRINK):");
                    int categoryChoice = scanner.nextInt();
                    Category category = null;
                    switch (categoryChoice) {
                        case 1:
                            category = Category.FOOD;
                            break;
                        case 2:
                            category = Category.DRINK;
                            break;
                    }
                    System.out.println("Are you sure you want to add this item? Enter yes to confirm, no to cancel:");
                    String confirmation = scanner.next();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        if (category == Category.FOOD) {
                            System.out.println("Enter the type of the food:");
                            scanner.nextLine(); // consume the newline
                            String type = scanner.nextLine();
                            admin.addItem(new Food(name, price, type));
                        } else if (category == Category.DRINK) {
                            System.out.println("Enter the type of the drink:");
                            scanner.nextLine(); // consume the newline
                            String type = scanner.nextLine();
                            admin.addItem(new Drink(name, price, type));
                        }
                        System.out.println("Item added successfully.");
                    } else {
                        System.out.println("Item addition cancelled.");
                    }
                    System.out.println("----------------------------------------\n");
                } else if (adminChoice == 2) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               UPDATE ITEM              ");
                    System.out.println("----------------------------------------");
                    System.out.println("Enter the number of the item you want to update:");
                    int itemNumber = scanner.nextInt();
                    scanner.nextLine(); // consume the newline
                    System.out.println("Enter the new name of the item:");
                    String newName = scanner.nextLine();
                    System.out.println("Enter the new price of the item:");
                    double newPrice = scanner.nextDouble();
                    System.out.println("Are you sure you want to update this item? Enter yes to confirm, no to cancel:");
                    String confirmation = scanner.next();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        admin.updateItem(itemNumber, newName, newPrice);
                        System.out.println("Item updated successfully.");
                    } else {
                        System.out.println("Item update cancelled.");
                    }
                    System.out.println("----------------------------------------\n");
                } else if (adminChoice == 3) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               REMOVE ITEM              ");
                    System.out.println("----------------------------------------");
                    System.out.println("Enter the number of the item you want to remove:");
                    int itemNumber = scanner.nextInt();
                    System.out.println("Are you sure you want to remove this item? Enter yes to confirm, no to cancel:");
                    String confirmation = scanner.next();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        admin.removeItem(itemNumber);
                    } else {
                        System.out.println("Item removal cancelled.");
                    }
                    System.out.println("----------------------------------------\n");
                } else if (adminChoice == 4) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               IMPORT MENU              ");
                    System.out.println("----------------------------------------");
                    System.out.println("Enter the name of the text file:");
                    scanner.nextLine(); // consume the newline
                    String fileName = scanner.nextLine();
                    try {
                        File file = new File(fileName);
                        Scanner fileScanner = new Scanner(file);
                        while (fileScanner.hasNextLine()) {
                            String line = fileScanner.nextLine();
                            String[] parts = line.split(",");
                            String name = parts[0];
                            double price = Double.parseDouble(parts[1]);
                            String type = parts[2];
                            if (parts[3].equalsIgnoreCase("FOOD")) {
                                admin.addItem(new Food(name, price, type));
                            } else if (parts[3].equalsIgnoreCase("DRINK")) {
                                admin.addItem(new Drink(name, price, type));
                            }
                        }
                        fileScanner.close();
                        System.out.println("Menu imported successfully.");
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found. Please try again.");
                    }
                    System.out.println("----------------------------------------\n");
                } else if (adminChoice == 5) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("               EXPORT MENU              ");
                    System.out.println("----------------------------------------");
                    System.out.println("Enter the name of the text file:");
                    scanner.nextLine(); // consume the newline
                    String fileName = scanner.nextLine();
                    try {
                        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
                        for (MenuDisplayable item : menu.getMenuItems()) {
                            if (item instanceof MenuItem) {
                                MenuItem menuItem = (MenuItem) item;
                                writer.println(menuItem.getName() + "," + menuItem.getPrice() + "," + menuItem.getCategory());
                            }
                        }
                        writer.close();
                        System.out.println("Menu exported successfully.");
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        System.out.println("An error occurred while trying to export the menu to a text file.");
                    }
                    System.out.println("----------------------------------------\n");
                } else {
                    System.out.println("\n----------------------------------------");
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("----------------------------------------\n");
                }
            }

        }
    }
}