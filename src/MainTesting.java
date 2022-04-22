import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

public class MainTesting {

    @Test
    public final void checkSubFromBalance(){
        Account ac = new Account();
        //Expect Success
        assertTrue(ac.subFromBalance(1));
        assertTrue(ac.subFromBalance(0));
        //Expect Fail
        assertFalse(ac.subFromBalance(-1));
        System.out.println("Test success : Check Sub from balance");
    }

    @Test
    public final void checkAddToBalance(){
        Account ac = new Account();
        //Expect Success
        assertTrue(ac.addToBalance(1));
        assertTrue(ac.subFromBalance(0));
        //Expect Fail
        assertFalse(ac.subFromBalance(-1));
        System.out.println("Test success : Check add to balance");
    }

    @Test
    public final void dataLockTest() throws NoSuchMethodException {
        //Expect Success
        //Allow developers to declare empty data for flexibility
        Person user = new Person();
        History his  = new History();
        Account acc = new Account();
        Date tempDate = new Date();
        user.setPerson("S9122312D" , "Munchkin Kim" ,"Blk 70 Cat street 23 ,Singapore 2000300", "98329201",tempDate,"Female","Singaporean" , new String[]{} );
        his.setHistory(tempDate,"DBS - Test" , 0,0,100);
        acc.setAccount("4001223'",0,0,10,0,0,false);

        try {
            // Error when try to set again
            user.setPerson("S219312323D" , "Munchkin Kim" ,"Blk 70 Cat street 23 ,Singapore 2000300", "98329201",tempDate,"Female","Singaporean" , new String[]{} );
            // Will not enter here if found exception
            fail();
        }catch (NoSuchMethodException e){
            System.out.println("Test success : Failed user change.");
        }

        try {
            // Error when try to set again
            his.setHistory(tempDate,"DBS - Test changed" , 0,0,100);
            // Will not enter here if found exception
            fail();
        }catch (NoSuchMethodException e){
            System.out.println("Test success : Failed history change.");
        }

        try {
            // Error when try to set again
            acc.setAccount("4003421223'",0,0,10,0,0,false);
            // Will not enter here if found exception
            fail();
        }catch (NoSuchMethodException e){
            System.out.println("Test success : Failed account change.");
        }
    }
}