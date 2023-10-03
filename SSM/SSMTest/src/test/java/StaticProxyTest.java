import ProxyTest.CalculatorImpl;
import ProxyTest.staticproxy.CalculatorStaticProxy;
import org.junit.Test;

public class StaticProxyTest {
    
    @Test
    public void test1(){
        CalculatorStaticProxy proxy = new CalculatorStaticProxy(new CalculatorImpl());
        
        proxy.add(1,2);
    }
    
}
