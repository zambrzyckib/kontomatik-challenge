package pl.zambrzyckib;

import java.util.Scanner;

public class KontomatikChallengeApp {

  final static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    BankScrapper pkoScrapper = new PkoScrapper();
    final var pkoResult = pkoScrapper.getAccountsInfo();
    if (pkoResult.isDefined()) {
      System.out.println("Udało się pobrać informacje o kontach");
      System.out.println(pkoResult.get());
    } else if (pkoResult.isEmpty()) {
      System.out.println("Nie udało się pobrać informacji o kontach");
    }
  }

}
