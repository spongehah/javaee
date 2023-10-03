package ProxyTest;

import ProxyTest.activeproxy.ProxyFactory;
import ProxyTest.staticproxy.CalculatorStaticProxy;

public class Test {

    public static void main(String[] args) {
        //静态代理测试
//        CalculatorStaticProxy proxy = new CalculatorStaticProxy(new CalculatorImpl());
//        proxy.add(1,2);
        
        
        
        ProxyFactory proxyFactory = new ProxyFactory(new CalculatorImpl());
        Calculator proxy = (Calculator) proxyFactory.getProxy();
        
        proxy.add(1,2);
    }
    
}
