package hello;

/**
 *
 * @author zoli
 */
public class Hello {
    
    private String who;
    
    public Hello(String who) {
        this.who = who;
    }
    
    public void sayHello(int n) {
        HelloLibrary.hello_sayHello(who, n);
    }
    
}
