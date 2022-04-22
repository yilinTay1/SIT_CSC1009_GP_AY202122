import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

abstract class Transaction {
    String transactionType;                 //Transaction type
    String userAccountNumber;               //User Account NUmber
    Account userAccount;                    //User Account
    double transactionAmt;                  //Transaction amount
    double transactionLimit;                //Limit of transaction
    double totalBalance;                    //User total balance
    boolean transactionMade;                //True if transaction made, False if no transaction made
    int exitOption;                         //User option if they choose to exit transaction

    //Date conversion for storing in CSV
    Date temp = new Date();
    SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
    String stringDate = DateFor.format(temp);
    Date date = FileManager.stringToDate(stringDate);
    Scanner userInput = new Scanner(System.in);

    Transaction(String accountNumber, Account userAccount) throws ParseException {
        this.userAccountNumber = accountNumber;
        this.userAccount = userAccount;
        exitOption = 1;
    }

    public void exitPrompt() {
        System.out.println();
        System.out.println("Please select an Option:");
        System.out.println("1. Re-try Transaction");
        System.out.println("2. Exit");
        exitOption = userInput.nextInt();
        while (exitOption != 1 && exitOption != 2) {
            System.out.println("Please enter option 1 or 2");
            exitOption = userInput.nextInt();
        }
        if (exitOption == 1) {
            printAmtPrompt();
        }
    }

    //Ensuring amount to be deducted is valid
    public void checkDeductAmt(double transactionLimit) {
        printAmtPrompt();
        //Checking for potential errors
        while (transactionAmt > totalBalance || transactionAmt < 0 || transactionAmt > transactionLimit || totalBalance < 0) {
            //Transfer amount is more than Balance
            if (transactionAmt > totalBalance) {
                System.out.println("Insufficient funds in your account.");
                System.out.println("Current balance is :$" + userAccount.getTotalBalance());
            }
            //Transfer amount is negative
            else if (transactionAmt < 0) {
                System.out.println("Please enter a positive amount!");
            }
            //Transfer amount is more than transfer limit
            else if (transactionAmt > transactionLimit) {
                System.out.println("Amount is more than current limit! Current limit is : $" + transactionLimit);
                System.out.println("Please increase your limit or enter a lower amount.");
            }
            exitPrompt();
            if (exitOption == 2) {
                break;
            }
        }
    }

    //Ensuring amount added is valid
    public void checkAddAmt() {
        printAmtPrompt();
        while (transactionAmt < 0) {
            System.out.println("Please enter a positive amount!");
            exitPrompt();
            if (exitOption == 2) {
                break;
            }
        }
    }

    public void printBalance() {
        this.totalBalance = userAccount.getTotalBalance();
        System.out.println("============================ Details for account " + userAccountNumber + " =====================================");
        System.out.println("Your Current Balance is : $" + totalBalance);
    }

    void printAmtPrompt() {
        System.out.print("Please enter an amount: ");
        transactionAmt = userInput.nextDouble();
    }

    abstract boolean executeTransaction() throws Exception;

    abstract void updateTransactionFile() throws Exception;

    abstract boolean updateAccountFile(int exitOption) throws Exception;

    abstract void printTransactionDetails(boolean transactionMade);
}
