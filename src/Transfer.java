import java.text.ParseException;
import java.util.Objects;
import java.util.Scanner;

public class Transfer extends Transaction {
    private final Person toUser;
    private Account toAccount;
    private String toAccountNumber;

    Transfer(String accountNumber, Account userAccount, Person toUser) throws ParseException {
        super(accountNumber, userAccount);
        this.toUser = toUser;
    }

    @Override
    boolean executeTransaction() throws Exception {
        printBalance();
        System.out.println("Select a type of Transfer:");
        System.out.println("1. Inter-Account Transfer");
        System.out.println("2. 3rd Party Transfer");
        System.out.println("3. Overseas Transfer ");
        System.out.println();
        System.out.print("Please Input Option 1 - 3: ");
        int transferType = userInput.nextInt();
        switch (transferType) {
            case 1: //Inter-Account Transfer
                this.transactionType = "Local Transfer";
                System.out.println("Select an account to transfer to:");
                //Viewing own accounts
                toUser.viewAccounts();
                int toAccountOption = userInput.nextInt();
                //Checking if from and to account are the same
                while (Objects.equals(toUser.accounts[toAccountOption - 1], userAccountNumber)) {
                    System.out.println("Unable to transfer to selected account. Please select a different account!");
                    toUser.viewAccounts();
                    toAccountOption = userInput.nextInt();
                }
                toAccount = toUser.get(toAccountOption - 1);
                toAccountNumber = toAccount.getAccountNumber();
                transactionLimit = userAccount.getTransferLimit();
                checkDeductAmt(transactionLimit);                   //checking amount and setting exitOption
                transactionMade = updateAccountFile(exitOption);    //setting transaction made if based on exitOption
                break;

            case 2: //3rd Party transfer
                this.transactionType = "Local Transfer";
                printToAccPrompt();
                //Getting toAccount
                toAccount = FileManager.getAccount(toAccountNumber);
                //Ensuring to account is not empty and not own account
                while (toAccount == null || toAccountNumber.equals(userAccountNumber)) {
                    if (toAccount == null) {
                        System.out.println("Unable to find account!");
                    } else {
                        System.out.println("Unable to transfer to own account!");
                    }
                    System.out.println("Please enter account number again: ");
                    toAccountNumber = userInput.next();
                    toAccount = FileManager.getAccount(toAccountNumber);
                }
                //Executing transfer
                transactionLimit = userAccount.getTransferLimit();
                checkDeductAmt(transactionLimit);
                transactionMade = updateAccountFile(exitOption);
                break;
            case 3:
                this.transactionType = "Overseas Transfer";
                System.out.println("Enter a country to transfer to:");
                System.out.println("E.g. SGD,AUD,CHF,EUR,GBP,PLN");
                System.out.print("Please input country code: ");
                String userInputString = userInput.next();
                double EXCHANGE_RATE = OverseasAPICall.getRate(userInputString);
                System.out.println("Current Exchange rate from USD to " + userInputString + " is: " + EXCHANGE_RATE);
                //Getting to account
                printToAccPrompt();
                //Executing transfer
                transactionLimit = userAccount.getOverseasTransferLimit();
                checkDeductAmt(transactionLimit);
                transactionMade = updateAccountFile(exitOption);
                if (transactionMade) {
                    double SGDTransferAmt = transactionAmt * EXCHANGE_RATE;
                    System.out.println("You have transferred " + "USD" + transactionAmt + " at exchange rate " + EXCHANGE_RATE + " for " + userInputString + SGDTransferAmt);
                }
                break;

        }
        return transactionMade;
    }

    @Override
    void updateTransactionFile() throws Exception {
        if (this.transactionType.equals("Local Transfer")) {
            //Updating user account
            History fromAccountHistory = new History(date, transactionType+" to account: "+toAccountNumber, transactionAmt, 0, totalBalance);
            FileManager.addHistoryLog(userAccount, userAccountNumber, fromAccountHistory);
            //Updating to account
            History toAccountHistory = new History(date, transactionType+" from account: " +userAccountNumber, 0, transactionAmt, toAccount.getTotalBalance());
            FileManager.addHistoryLog(userAccount, toAccountNumber, toAccountHistory);
        } else if (this.transactionType.equals("Overseas Transfer")) {
            //Updating user account
            History fromAccountHistory = new History(date, transactionType+" to Overseas account: "+toAccountNumber, transactionAmt, 0, totalBalance);
            FileManager.addHistoryLog(userAccount, userAccountNumber, fromAccountHistory);
        }
    }

    @Override
    boolean updateAccountFile(int exitOption) throws Exception {
        switch (exitOption) {
            case 1:
                try {
                    //Deducting from user account
                    userAccount.subFromBalance(transactionAmt);
                    //Updating details
                    FileManager.updateAccountCSV(userAccountNumber, 1, String.format("%.2f", userAccount.getAvailableBalance() - transactionAmt));
                    FileManager.updateAccountCSV(userAccountNumber, 2, String.format("%.2f", userAccount.getTotalBalance()));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transaction: Transfer Un-successful!");
                }
                //Adding to target account
                if (this.transactionType.equals("Local Transfer")) {
                    this.toAccount.addToBalance(transactionAmt);
                    //Updating target account
                    FileManager.updateAccountCSV(toAccountNumber, 1, String.format("%.2f", this.toAccount.getAvailableBalance() + transactionAmt));
                    FileManager.updateAccountCSV(toAccountNumber, 2, String.format("%.2f", this.toAccount.getTotalBalance()));
                }
                transactionMade = true;
                break;
            case 2:
                transactionMade = false;
                break;
            default:
                System.out.print("\nYou did not enter a valid selection. Try again.");
                System.out.println("\nPlease Select Option 1 or 2:");
                break;
        }
        //Throwing an exception
        return transactionMade;
    }

    @Override
    void printTransactionDetails(boolean transactionMade) {
        //if transaction was made, print transaction details
        if (transactionMade) {
            System.out.println("============================Executing Transaction=====================================");
            Scanner enter = new Scanner(System.in);
            System.out.println("\nYou have transferred USD " + transactionAmt + " to Account Number: " + toAccountNumber);
            System.out.println("\nYour Remaining Balance is: $" + userAccount.getTotalBalance());
            System.out.println("\nPress enter to continue");
            enter.nextLine();
        }
    }

    void printToAccPrompt() {
        //Get account to transfer input from user
        System.out.println();
        System.out.println("Please enter an account number to transfer to:");
        toAccountNumber = userInput.next();
    }
}
