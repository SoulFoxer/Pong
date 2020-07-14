package com.pong;

public class TestMain {


    private byte[] bytesArray;
    private int num;
    private int sqrtOfNum;
    private int[] primes;
    private int primeLength;

    public TestMain(int num) {
        this.sqrtOfNum = (int) Math.sqrt(num);
        this.num = num;
        this.bytesArray = new byte[(int) Math.sqrt(num) + 1];
        this.primes = new int[(int) (2 * sqrtOfNum / Math.log(sqrtOfNum) + 1)];

        int ssn = (int) Math.sqrt(sqrtOfNum);
        bytesArray[0] = 1;
        bytesArray[1] = 1;
        for (int i = 2; i <= ssn; i++) {
            if (bytesArray[i] == 0) {                                          //then i is prime
                for (int j = 2 * i; j <= sqrtOfNum; j += i) {               //j will be composite
                    bytesArray[j] = 1;                                      //mark j as composite
                }
            }
        }


        int j = 0;
        for (int i = 0; i <= sqrtOfNum; i++) {
            if (bytesArray[i] == 0) {
                primes[j] = i;
                j++;
            }                   //then i is prime
        }
        primeLength = j;

    }

    public boolean checkifprime(int n) {
        if (n < 2) return false;
        if (n > num) {
            throw new IllegalArgumentException("to large");
        }
        int sn = (int) Math.sqrt(n);
        for (int i = 0; primes[i] <= sn && i <primeLength; i++) {
            if (n % primes[i] == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {


//        for (int i = 2; i <= 100; i++) {
//            if(checkPrime(i)){
//                System.out.println(i);
//            }
//        }

        TestMain testMain = new TestMain(150);

        for (int i = 2; i <= testMain.num; i++) {
            if (testMain.checkifprime(i)) {
                System.out.println(i + " is prime");
            }

        }
    }


//    public static boolean checkPrime(int num) {
//        for (int i = 2; i <= Math.sqrt(num); i++) {
//
//            // checks if
//            if (num % i == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
}