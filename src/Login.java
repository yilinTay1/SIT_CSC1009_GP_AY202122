import java.util.Scanner;

public class Login {
    private String username;
    private String password;
    private boolean lockedStatus;
    private final boolean resetStatus;


    private final String accountRoles;
    private final String ID;
    private int counter = 0;
    Person user;
    Scanner userInput = new Scanner(System.in);

    public Login(String username, String password, boolean lockedStatus, boolean resetStatus, String accountRoles, String ID) {
        this.username = username;
        this.password = password;
        this.lockedStatus = lockedStatus;
        this.resetStatus = resetStatus;
        this.accountRoles = accountRoles;
        this.ID = ID;
    }

    public void setLockedStatus(boolean lockedStatus) {
        this.lockedStatus = lockedStatus;
    }

    public boolean isLockedStatus() throws Exception {
        if (lockedStatus) {
            String[] newData = {"TRUE"};
            FileManager.updateLoginCSV(ID, 4, newData);
            System.out.println("Your account had been locked. Please contact our customer service team.");
            return true;
        } else
            return false;

    }

    public boolean isResetStatus() {
        return resetStatus;
    }

    public String getAccountRoles() {
        return accountRoles;
    }

    public boolean checkPassword(String inputPassword) throws Exception {
        counter = 1;

        while (!(inputPassword.equals(Encryption.encryption(password, ID, 'D'))) && counter < 5) {
            System.out.print("Please re-enter password: ");
            inputPassword = userInput.nextLine();
            counter++;
        }
        if (counter > 4) {
            setLockedStatus(true);
            isLockedStatus();
            return false;
        } else {
            user = FileManager.getDatabase(ID);
            return true;
        }
    }

    public boolean resetPassword() throws Exception {
        /* User will reset password when unable to login / select option 1 from settings */
        String newPassword = null, newRePassword = null;
        String passwordFormat = "^[0-9]{6,20}$"; /* password only consist of digit, length of 6 to 20 */
        counter = 0;
        do {
            System.out.println("======================================== Create New Password =======================================");
            if (counter > 0 && !newPassword.matches(passwordFormat))
                System.out.println("Please enter at least 6 digits for your password.");
            if (counter > 0 && !(newPassword.equals(newRePassword)))
                System.out.println("Password mismatch.");
            System.out.print("Please enter new password: ");
            newPassword = userInput.nextLine();
            System.out.print("Please re-enter new password: ");
            newRePassword = userInput.nextLine();
            counter++;
        } while (!(newPassword.equals(newRePassword)) || !newPassword.matches(passwordFormat));
        password = Encryption.encryption(newPassword, ID, 'E');
        String[] newData = {password};
        FileManager.updateLoginCSV(ID, 2, newData);
        System.out.println("Your password have been changed successfully.");
        return true;
    }

    public boolean resetUsername() throws Exception {
        String newUsername = null;
        counter = 0;
        do {
            System.out.println("========================================= Create New Username ========================================");
            if (newUsername != null && counter > 0 && newUsername.length() < 6)
                System.out.println("Please enter at least 6 characters for your username.");
            if (counter > 0 && newUsername == null)
                System.out.println("Username had been taken.");
            System.out.print("Please enter new username: ");
            newUsername = userInput.nextLine();
            if (FileManager.getLoginData(newUsername) != null)
                newUsername = null;
            counter++;
        } while (newUsername == null || newUsername.length() < 6);
        username = newUsername;
        String[] newData = {username};
        FileManager.updateLoginCSV(ID, 3, newData);
        System.out.println("Your username have been changed successfully.");
        return true;
    }

    public String getPassword() {
        return password;
    }

    public String getID() {
        return ID;
    }

}
