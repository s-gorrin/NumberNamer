import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * A class to convert integers into their names.
 */
public class NumberNamer {
  private static HashMap<Integer, String> dictionary;

  public static final int INT_MIN = -2147483648;

  /**
   * Generate the number-name reference dictionary.
   * Note that the format is 1,One for numbers 0-90,
   *  and that the thousands represent grouping names.
   */
  private static void initialize() {
    dictionary = new HashMap<>();
    File ref = new File("dictionary.csv");

    try (Scanner scanner = new Scanner(ref)) {
      while (scanner.hasNext()) {
        String line = scanner.nextLine();
        int number = Integer.parseInt(line.split(",")[0].trim());
        String name = line.split(",")[1].trim();
        dictionary.put(number, name);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a space to the StringBuilder if necessary.
   *
   * @param builder a StringBuilder
   */
  private static void spacer(StringBuilder builder) {
    if (builder.length() > 0) {
      builder.append(" ");
    }
  }

  /**
   * Handle the two edge cases directly.
   *
   * @return  the name string
   */
  private static String edgeCase(int number) {
    if (number == 0) {
      return "Zero";
    }

    initialize();
    return "Negative Two Billion " + handlePart(147) + " Million "
        + handlePart(483) + " Thousand " + handlePart(648);
  }

  /**
   * Get names for each three-digit (or smaller) segment of the number.
   *
   * @param part  a segment of the original number
   * @return      the name string for the segment
   */
  private static String handlePart(int part) {
    assert part > 0 && part < 1000;

    StringBuilder builder = new StringBuilder();
    if (part >= 100) {
      int h = part / 100;
      builder.append(dictionary.get(h)).append(" Hundred");
      part %= 100;
    }

    if (part >= 10) {
      spacer(builder);
      int tens = part / 10;
      if (tens == 1) { // remaining part is in the range 10-19
        builder.append(dictionary.get(part));
        return builder.toString();
      }
      builder.append(dictionary.get(tens * 10));
      part %= 10;
    }

    if (part > 0) {
      spacer(builder);
      builder.append(dictionary.get(part));
    }

    return builder.toString();
  }

  /**
   * Take a number and return that number written as words in English.
   * For example, with number = 1, returns "One"
   *
   * @param number  an integer
   * @return        the number as English words
   */
  public static String namer(int number) {
    System.out.println("Input number = " + number);
    if (number == 0 || number == INT_MIN) {
      return edgeCase(number);
    }

    initialize();
    ArrayList<Integer> parts = new ArrayList<>();
    StringBuilder builder = new StringBuilder();

    if (number < 0) {
      builder.append("Negative");
      number *= -1;
    }

    // break number into groups of hundreds with 100s place at index 0
    while (number > 0) {
      parts.add(number % 1000);
      number /= 1000;
    }

    for (int i = parts.size() - 1; i >= 0; i--) {
      if (parts.get(i) > 0) {
        spacer(builder);
        builder.append(handlePart(parts.get(i)));

        // append the grouping name if the part is not in the hundreds place
        if (i != 0) {
          builder.append(" ").append(dictionary.get(i * 1000));
        }
      }
    }

    return builder.toString();
  }
}
