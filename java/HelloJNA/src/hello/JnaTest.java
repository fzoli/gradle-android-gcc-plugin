package hello;

import com.sun.jna.*;
 
public class JnaTest {
 
    public static native double cos(double x);
    public static native double sin(double x);
 
    static {
        Native.register(Platform.C_LIBRARY_NAME);
    }
 
    public static void main(String[] args) {
        System.out.println("cos(0)=" + cos(0));
        System.out.println("sin(0)=" + sin(0));
    }
}