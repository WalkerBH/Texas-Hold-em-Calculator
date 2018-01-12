//package
package CustomUtilities;

//imports

public class CustomMath {

    CustomMath() {
        
    }
    
    //DO ERROR CHECKING
    public static int P(int n, int r) {
        if (n < r) {
            System.out.println("Error in CustomMath.P(int, int): n("+n+") must be greater than or equal to r("+r+").");
            return 0;
        } else if (n < 0) {
            System.out.println("Error in CustomMath.P(int, int): n("+n+") must be greater or equal to 0");
            return 0;
        } else if (r < 0) {
            System.out.println("Error in CustomMath.P(int, int): r("+r+") must be greater or equal to 0");
            return 0;
        } else {
            return factorial(n) / factorial(n-r);
        }
    }
    
    //DO ERROR CHECKING
    public static int C(int n, int r) {
        if (n < r) {
            System.out.println("Error in CustomMath.C(int, int): n("+n+") must be greater than or equal to r("+r+").");
            return 0;
        } else if (n < 0) {
            System.out.println("Error in CustomMath.C(int, int): n("+n+") must be greater or equal to 0");
            return 0;
        } else if (r < 0) {
            System.out.println("Error in CustomMath.C(int, int): r("+r+") must be greater or equal to 0");
            return 0;
        } else {
            return P(n, r) / factorial (r);
        }
        
    }
    
    private static int factorial(int x) {
        if (x == 1)
            return 1;
        else if (x == 0)
            return 1;
        else
            return x*factorial(x-1);
    }
}
