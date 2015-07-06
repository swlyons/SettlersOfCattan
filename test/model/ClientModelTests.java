/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ddennis
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({})
public class ClientModelTests {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void test_1() {
        assertEquals("OK", "OK");
        assertTrue(true);
        assertFalse(false);
    }

    public static void main(String[] args) {

        String[] testClasses = new String[]{
            "model.ClientModelTests"
        };

        org.junit.runner.JUnitCore.main(testClasses);
    }
}
