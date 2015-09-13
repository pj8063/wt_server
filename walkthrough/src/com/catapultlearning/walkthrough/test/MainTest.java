package com.catapultlearning.walkthrough.test;

public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try{
            throw new RuntimeException("test run time exception");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
