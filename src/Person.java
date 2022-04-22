import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Person extends ArrayList<Account> implements Printing {
    private String icNumber;
    private String name;
    private String address;
    private String phoneNum;
    private Date birthday;
    private String gender;
    private String nationality;
    private boolean set = false;
    public String[] accounts;
    Scanner enter = new Scanner(System.in);

    public Person(){}

    public Person(String icNumber, String name, String address, String phoneNum, Date birthday, String gender, String nationality, String[] accounts) {
        this.icNumber = icNumber;
        this.name = name;
        this.address = address;
        this.phoneNum = phoneNum;
        this.birthday = birthday;
        this.gender = gender;
        this.nationality = nationality;
        this.accounts = accounts;
        this.set = true;
    }

    public void setPerson(String icNumber, String name, String address, String phoneNum, Date birthday, String gender, String nationality, String[] accounts) throws NoSuchMethodException {
        if (!set) {
            // Only set once security feature.
            this.icNumber = icNumber;
            this.name = name;
            this.address = address;
            this.phoneNum = phoneNum;
            this.birthday = birthday;
            this.gender = gender;
            this.nationality = nationality;
            this.accounts = accounts;
            this.set = true;
        } else {
            throw new NoSuchMethodException("Can only set once , please do not attempt to set user detail more than once.");
        }
    }

    public String getName() {
        return this.name;
    }

    public void updatePhoneNum(String newPhoneNum) {
        phoneNum = newPhoneNum;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void viewAccounts() {
        for (int i = 0; i < accounts.length; i++) {
            int temp = i + 1;
            System.out.println(temp + " - " + accounts[i]);
        }
        System.out.print("\nPlease Input Option 1 - " + accounts.length + ": ");
    }

    public void waitForEnter() {
        System.out.println("\nPress enter to continue ...");
        this.enter.nextLine();
    }

    @Override
    public void printDetail() {
        System.out.println("Name : " + this.name);
        System.out.println("Address : " + this.address);
        System.out.println("Phone Number : " + this.phoneNum);
        System.out.println("Birthday : " + this.birthday);
        System.out.println("Gender : " + this.gender);
        System.out.println("Nationality : " + this.nationality);
        System.out.println("====================================================================================================");
    }
}
