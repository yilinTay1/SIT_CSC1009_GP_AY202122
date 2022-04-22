import java.util.Scanner;

public class Display {
    static Scanner userInput = new Scanner(System.in);
    static Login validateUser;
    static String username;
    static String password;

    public static void userLogin() throws Exception {
        System.out.println("Welcome to Bank of Singapore");
        System.out.print("Please enter username: ");
        username = userInput.nextLine();
        System.out.print("Please enter password: ");
        password = userInput.nextLine();
        boolean end = true;
        validateUser = FileManager.getLoginData(username);
        if (validateUser != null) {
            if (validateUser.checkPassword(password)) {
                if ((!validateUser.isLockedStatus())) {
                    if (validateUser.isResetStatus()) {
                        if (validateUser.resetUsername() && validateUser.resetPassword()) {
                            if (validateUser.getAccountRoles().equals("User")) {
                                while (end) {
                                    end = userMenu(validateUser.user);
                                }
                            } else if (validateUser.getAccountRoles().equals("Admin"))
                                while (end) {
                                    end = adminMenu(validateUser.user);
                                }
                            else
                                System.out.println("Error");
                        }
                    } else {
                        if (validateUser.getAccountRoles().equals("User")) {
                            while (end) {
                                end = userMenu(validateUser.user);
                            }
                        } else if (validateUser.getAccountRoles().equals("Admin")) {
                            while (end) {
                                end = adminMenu(validateUser.user);
                            }
                        }
                    }
                }
            }
        } else
            System.out.println("User " + username + " not found.");
    }

    public static boolean adminMenu(Person user) throws Exception {
        System.out.println("====================================================================================================");
        System.out.println("Welcome " + user.getName());
        System.out.println("\nAdmin Menu:");
        System.out.println("Select 1 - Add user");
        System.out.println("Select 2 - Unlock Account");
        System.out.println("Select 0 - Exit\n");
        System.out.print("\nPlease Input Option 0 - 2: ");

        int userOption = userInput.nextInt();
        System.out.println("====================================================================================================");
        switch (userOption) {
            case 1:
                Customer.newUser();
                break;

            case 2:
                Customer.unlockAcc();
                break;

            case 0:
                System.out.print("\nExiting the system ...");
                return false;

            default:
                System.out.print("\nYou did not enter a valid selection. Try again.");
                System.out.println("\nPlease Select Option 0 - 2:");
                break;
        }

        return true;
    }

    public static boolean userMenu(Person user) throws Exception {
        System.out.println("====================================================================================================");
        System.out.println("Welcome " + user.getName());
        System.out.println("\nAutomated Teller Machine (ATM) Menu:");
        System.out.println("Select 1 - View my balance");
        System.out.println("Select 2 - Withdraw cash");
        System.out.println("Select 3 - Deposit funds");
        System.out.println("Select 4 - Transfer funds");
        System.out.println("Select 5 - Settings");
        System.out.println("Select 6 - View personal information");
        System.out.println("Select 7 - View account information");
        System.out.println("Select 8 - View most recent 100 transfer history");
        System.out.println("Select 9 - View balance Analytic");
        System.out.println("Select 0 - Exit\n");
        System.out.print("Please Input Option 0 - 9: ");

        boolean transactionMade;
        int accountOption;
        int userOption = userInput.nextInt();
        System.out.println("====================================================================================================");
        switch (userOption) {
            case 1:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like view balance ");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                Balance balanceInquiry = new Balance(user.get(accountOption - 1));
                balanceInquiry.printDetails();
                break;

            case 2:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like Withdraw Cash from");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                Withdraw withdrawTransaction = new Withdraw(user.accounts[accountOption - 1], user.get(accountOption - 1));
                transactionMade=withdrawTransaction.executeTransaction();
                if(transactionMade){
                    withdrawTransaction.updateTransactionFile();
                }
                withdrawTransaction.printTransactionDetails(transactionMade);
                break;

            case 3:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to Deposit Cash to ");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                Deposit depositTransaction = new Deposit(user.accounts[accountOption - 1], user.get(accountOption - 1));
                transactionMade=depositTransaction.executeTransaction();
                if(transactionMade){
                    depositTransaction.updateTransactionFile();
                }
                depositTransaction.printTransactionDetails(transactionMade);
                break;

            case 4:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like make a transfer from");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                Transfer transferTransaction = new Transfer(user.accounts[accountOption - 1], user.get(accountOption - 1), user);
                transactionMade = transferTransaction.executeTransaction();
                if(transactionMade){
                    transferTransaction.updateTransactionFile();
                }
                transferTransaction.printTransactionDetails(transactionMade);
                break;

            case 5:
                boolean check = true;
                while (check) {
                    check = settingsMenu(user);
                }
                break;

            case 6:
                user.printDetail();
                user.waitForEnter();
                break;

            case 7:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view your accounts from");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                user.get(accountOption - 1).printDetail();
                user.waitForEnter();
                break;

            case 8:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view your most recent 100 history from");
                user.viewAccounts();
                String ic = user.getIcNumber();
                user = FileManager.getDatabase(ic);
                accountOption = userInput.nextInt();
                System.out.println("------------------------------------------------");
                for (int i = 0; i < user.get(accountOption - 1).size(); i++) {
                    System.out.println(i + 1 + ".");
                    user.get(accountOption - 1).get(i).printDetail();
                    System.out.println("------------------------------------------------");
                }
                user.waitForEnter();
                break;

            case 9:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view analytic from");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                user.get(accountOption - 1).printBalanceAnalytic();
                user.waitForEnter();
                break;

            case 0:
                System.out.print("\nExiting the system...");
                return false;
            default:
                System.out.println("\nYou did not enter a valid selection. Try again.\n");
                break;
        }
        return true;
    }

    public static boolean settingsMenu(Person user) throws Exception {
        System.out.println("Welcome " + user.getName());
        System.out.println("\nAutomated Teller Machine (ATM) Settings Menu:");
        System.out.println("Select 1 - Change password");
        System.out.println("Select 2 - Update contact number");
        System.out.println("Select 3 - Lock / Unlock Account(s)");
        System.out.println("Select 4 - Transfer Limit");
        System.out.println("Select 5 - Withdrawal Limit");
        System.out.println("Select 6 - Overseas Transfer Limit");
        System.out.println("Select 0 - Back\n"); /* Goes back to Main Menu */

        System.out.print("Please Input Option 0 - 6: ");
        int userOption = userInput.nextInt();
        int accountOption;
        System.out.println("====================================================================================================");

        switch (userOption) {
            case 1:
                System.out.println("Hello, " + user.getName());
                UserSettings changePassword = new UserSettings(validateUser);
                changePassword.executeSettings(1);
                changePassword.enterToContinue();
                break;
            case 2:
                System.out.println("Hello, " + user.getName());
                UserSettings updateContact = new UserSettings(validateUser);
                updateContact.executeSettings(2);
                updateContact.enterToContinue();
                break;
            case 3:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                AccountSettings suspendAcc = new AccountSettings(user.accounts[accountOption - 1], user.get(accountOption - 1));
                suspendAcc.executeSettings(3);
                suspendAcc.enterToContinue();
                break;
            case 4:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view the Transfer Limit ");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                AccountSettings transferLimit = new AccountSettings(user.accounts[accountOption - 1], user.get(accountOption - 1));
                transferLimit.executeSettings(4);
                transferLimit.enterToContinue();
                break;
            case 5:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view the Withdrawal Limit ");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                AccountSettings withdrawalLimit = new AccountSettings(user.accounts[accountOption - 1], user.get(accountOption - 1));
                withdrawalLimit.executeSettings(5);
                withdrawalLimit.enterToContinue();
                break;
            case 6:
                System.out.println("Hello, " + user.getName() + " please choose which account(s) you would like to view the Overseas Transfer Limit ");
                user.viewAccounts();
                accountOption = userInput.nextInt();
                AccountSettings overseasTransferLimit = new AccountSettings(user.accounts[accountOption - 1], user.get(accountOption - 1));
                overseasTransferLimit.executeSettings(6);
                overseasTransferLimit.enterToContinue();
                break;
            case 0:
                userMenu(user);
                break;

            default:
                System.out.println("\nYou did not enter a valid selection. Try again.\n");
                System.out.println("====================================================================================================");
                return true;
        }
        return false;
    }
}
