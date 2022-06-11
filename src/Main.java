import java.util.Scanner;

/**
 * A main class to call the NumberNamer class.
 */
public class Main {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Please enter an integer, or anything else to exit:");

    // read user input and exit on any non-integer
    while (scanner.hasNext()) {
      try {
        System.out.println(NumberNamer.namer(Integer.parseInt(scanner.next())));
      } catch (NumberFormatException e) {
        break;
      }
    }
  }
}
