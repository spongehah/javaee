import ProxyTest.Calculator;
import ProxyTest.CalculatorImpl;
import ProxyTest.activeproxy.ProxyFactory;
import org.junit.Test;

public class ActiveProxyTest {

    @Test
    public void test() {
        ProxyFactory proxyFactory = new ProxyFactory(new CalculatorImpl());
        Calculator proxy = (Calculator) proxyFactory.getProxy();
        
        proxy.add(1,2);
    }
}
