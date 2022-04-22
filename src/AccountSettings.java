import java.util.Scanner;
import java.util.InputMismatchException;

public class AccountSettings implements Settings {
    /* option 3 to 6 of setting menu */
    /* option 3 - Lock / Unlock Account(s) */
    /* option 4 - Transfer Limit */
    /* option 5 - Withdrawal Limit */
    /* option 6 - Overseas Transfer Limit */

    /* Scanner for user inputs */
    static Scanner myObj = new Scanner(System.in);

    /* Necessary variables */
    private final String accountNumber;
    private final Account accountInformation;
    private static String userOption;
    private static double inputAmount;

    /* Constructor */
    public AccountSettings(String userAccountNumber, Account accountInformation) {
        this.accountNumber = userAccountNumber;
        this.accountInformation = accountInformation;
    }

    /* option 3 - Lock / Unlock Account(s) */
    public void suspendAccount() throws Exception {
        boolean lock = accountInformation.getLocked();
        boolean check = false;

        System.out.println("\n===================================== Lock / Unlock Account(s) =====================================");

        /* Loop when user keep in invalid inputs, stops when check = true */
        while (!check) {
            if (lock) { /* lock = true, account is locked */
                System.out.println("Account is locked.");
                System.out.println("Do you wish to unlock your account? Y/N");
                userOption = myObj.next().toUpperCase();

                if (userOption.equals("Y")) {
                    check = true;
                    accountInformation.setLocked(false);
                    FileManager.updateAccountCSV(accountNumber, 6, "FALSE");
                    System.out.println("Account " + accountNumber + " is unlocked.");
                } else if (userOption.equals("N")) {
                    check = true;
                    System.out.println("Account " + accountNumber + " remains locked.");
                } else {
                    /* check remains false */
                    System.out.println("Invalid option, please try again.");
                }
            } else { /* lock = false, account is not locked */
                System.out.println("Your account is not locked.");
                System.out.println("Are you sure you wish to lock your account? Y/N");
                userOption = myObj.next().toUpperCase();

                if (userOption.equals("Y")) {
                    check = true;
                    accountInformation.setLocked(true);
                    FileManager.updateAccountCSV(accountNumber, 6, "TRUE");
                    System.out.println("Account " + accountNumber + " is locked.");
                } else if (userOption.equals("N")) {
                    check = true;
                    System.out.println("Account " + accountNumber + " remains unlocked.");
                } else {
                    /* check remains false */
                    System.out.println("Invalid option, please try again.");
                }
            }
        }
    }

    /* option 4 - Transfer Limit */
    public void transferLimit() throws Exception {
        double transferLimit = accountInformation.getTransferLimit();
        boolean check = false;

        System.out.println("\n================================ Account Transfer Limit Information ================================");
        System.out.println(accountNumber + " Current transfer limit: $" + transferLimit);

        /* Loop when user keep in invalid inputs, stops when check = true */
        while (!check) {
            System.out.println("Would you like to update your transfer limit? Y/N");
            userOption = myObj.next().toUpperCase();
            if (userOption.equals("Y")) {
                check = true;
                changeTransferLimit();
            } else if (userOption.equals("N")) {
                check = true;
                System.out.println("Transfer limit remains unchanged.");
            } else {
                /* check remains false */
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    /* option 5 - Withdrawal Limit */
    public void withdrawalLimit() throws Exception {
        double withdrawalLimit = accountInformation.getWithdrawLimit();
        boolean check = false;

        System.out.println("\n=============================== Account Withdrawal Limit Information ===============================");
        System.out.println(accountNumber + " Current withdrawal limit: $" + withdrawalLimit);

        /* Loop when user keep in invalid inputs, stops when check = true */
        while (!check) {
            System.out.println("Would you like to update your withdrawal limit? Y/N");
            userOption = myObj.next().toUpperCase();

            if (userOption.equals("Y")) {
                check = true;
                changeWithdrawalLimit();
            } else if (userOption.equals("N")) {
                check = true;
                System.out.println("Withdrawal limit remains unchanged.");
            } else {
                /* check remains false */
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    /* option 6 - Overseas Transfer Limit */
    public void overseasTransferLimit() throws Exception {
        double overseasTransferLimit = accountInformation.getOverseasTransferLimit();
        boolean check = false;

        System.out.println("\n============================ Account Overseas Transfer Limit Information ===========================");
        System.out.println(accountNumber + " Current overseas transfer limit: $" + overseasTransferLimit);

        /* Loop when user keep in invalid inputs, stops when check = true */
        while (!check) {
            System.out.println("Would you like to update your overseas transfer limit? Y/N");
            userOption = myObj.next().toUpperCase();
            if (userOption.equals("Y")) {
                check = true;
                changeOverseasTransferLimit();
            } else if (userOption.equals("N")) {
                check = true;
                System.out.println("Overseas withdrawal limit remains unchanged.");
            } else {
                /* check remains false */
                System.out.println("Invalid option, please try again.");
            }
        }
    }

    public void changeTransferLimit() throws Exception {
        boolean check = false;
        /* Loop when user keep in invalid inputs, stops when check = true */
        while (!check) {
            try {
                System.out.println("Please enter new amount for transfer limit: ");
                inputAmount = myObj.nextDouble();
                if (inputAmount >= 0) {
                    check = true;
                    accountInformation.updateTransferLimit(inputAmount);
                    FileManager.updateAccountCSV(accountNumber, 3, String.format("%.2f", inputAmount));
                    System.out.println("\nTransfer limit updated to $" + inputAmount);
                } else {
                    /* check remains false */
                    System.out.println("Amount entered is invalid, please try again.");
                }
            } catch (InputMismatchException ex) { /* To catch non-number inputs */
                check = true;
                System.out.println("Amount entered is not numbers, we will head back to main menu. Please try again.");
            }
        }
    }

    public void changeWithdrawalLimit() throws Exception {
        boolean check = false;
        while (!check) { /* Loop when user keep in invalid inputs, stops when check = true */
            try {
                System.out.println("Please enter new amount for withdrawal limit: ");
                inputAmount = myObj.nextDouble();
                if (inputAmount >= 0) {
                    check = true;
                    accountInformation.updateWithdrawLimit(inputAmount);
                    FileManager.updateAccountCSV(accountNumber, 4, String.format("%.2f", inputAmount));
                    System.out.println("\nWithdrawal limit updated to $" + inputAmount);
                } else {
                    /* check remains false */
                    System.out.println("Amount entered is invalid, please try again.");
                }
            } catch (InputMismatchException ex) { /* To catch non-number inputs */
                check = true;
                System.out.println("Amount entered is not numbers, we will head back to main menu. Please try again.");
            }
        }
    }

    public void changeOverseasTransferLimit() throws Exception {
        boolean check = false;
        while (!check) { /* Loop when user keep in invalid inputs, stops when check = true */
            try {
                System.out.println("Please enter new amount for overseas transfer limit: ");
                inputAmount = myObj.nextDouble();
                if (inputAmount >= 0) {
                    check = true;
                    accountInformation.updateOverseasTransferLimit(inputAmount);
                    FileManager.updateAccountCSV(accountNumber, 5, String.format("%.2f", inputAmount));
                    System.out.println("\nTransfer limit updated to $" + inputAmount);
                } else {
                    /* check remains false */
                    System.out.println("Amount entered is invalid, please try again.");
                }
            } catch (InputMismatchException ex) { /* To catch non-number inputs */
                check = true;
                System.out.println("Amount entered is not numbers, we will head back to main menu. Please try again.");
            }
        }
    }

    @Override
    public void executeSettings(int option) throws Exception {
        switch (option) {
            case 3:
                suspendAccount();
                break;
            case 4:
                transferLimit();
                break;
            case 5:
                withdrawalLimit();
                break;
            case 6:
                overseasTransferLimit();
                break;
        }
    }

    @Override
    public void enterToContinue() {
        Scanner enter = new Scanner(System.in);
        System.out.println("\nPress enter to continue");
        enter.nextLine();
    }
}
