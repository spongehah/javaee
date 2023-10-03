package ProxyTest.staticproxy;

import ProxyTest.Calculator;
import ProxyTest.CalculatorImpl;

public class CalculatorStaticProxy implements Calculator {
    
    private CalculatorImpl target;

    public CalculatorStaticProxy(CalculatorImpl target) {
        this.target = target;
    }

    @Override
    public int add(int i, int j) {
        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] add 方法开始了，参数是：" + i + "," + j);

        // 通过目标对象来实现核心业务逻辑
        int result = target.add(i, j);

        System.out.println("[日志] add 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int sub(int i, int j) {
        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] sub 方法开始了，参数是：" + i + "," + j);

        // 通过目标对象来实现核心业务逻辑
        int result = target.sub(i, j);

        System.out.println("[日志] sub 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int mul(int i, int j) {
        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] mul 方法开始了，参数是：" + i + "," + j);

        // 通过目标对象来实现核心业务逻辑
        int result = target.mul(i, j);

        System.out.println("[日志] mul 方法结束了，结果是：" + result);
        return result;
    }

    @Override
    public int div(int i, int j) {
        // 附加功能由代理类中的代理方法来实现
        System.out.println("[日志] div 方法开始了，参数是：" + i + "," + j);

        // 通过目标对象来实现核心业务逻辑
        int result = target.div(i, j);

        System.out.println("[日志] div 方法结束了，结果是：" + result);
        return result;
    }
}
