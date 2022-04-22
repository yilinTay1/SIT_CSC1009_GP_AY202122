import java.util.Scanner;

public class Balance {
    private final Account accountInformation;

    public Balance(Account accountInformation) {
        this.accountInformation = accountInformation;
    }

    public void printDetails() {
        Scanner enter = new Scanner(System.in);
        System.out.println("================================== Account Balance Information =====================================");
        System.out.println("\nYour Remaining Balance is: $" + accountInformation.getTotalBalance());
        System.out.println("\nPress enter to continue ...");
        enter.nextLine();
    }
}
