package com.spongehah.juc.CompletableFuture;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTestDemo {

    public static void main(String[] args) {
        /**
         * Stream实例化
         */
        List<Employee> list = Employee.getEmployees();

        //default Stream<E> stream() : 返回一个顺序流
        Stream<Employee> stream = list.stream();
        
        //default Stream<E> parallelStream() : 返回一个并行流
        Stream<Employee> stream1 = list.parallelStream();
        
        Integer[] arr = new Integer[]{1,2,3,4,5};
        Stream<Integer> stream2 = Arrays.stream(arr);

        Stream<String> stream3 = Stream.of("aa", "bb", "cc", "dd");

    }

    @Test
    public void test01(){
        /**
         * 中间操作
         */
        /**
         * 筛选与切片
         */
        //filter(Predicate p): 接收Lambda,从流中排除某些元素。
        //练习：查询员工表中薪资大于7000的员工信息
        List<Employee> list = Employee.getEmployees();
        list.stream().filter(emp -> emp.getSalary() > 7000).forEach(System.out::println);

        System.out.println();

        //limit(n): 截断流，使其元素不超过给定数量.
        //因为stream已经执行了终止操作，就不可以再调用其它的中间操作或终止操作了，所以需要重新获取流对象。
        list.stream().limit(3).forEach(System.out::println);

        System.out.println();

        //skip(n): 跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。
        list.stream().skip(3).forEach(System.out::println);
        list.stream().skip(10).forEach(System.out::println);    //返回空流

        System.out.println();

        //distinct(): 筛选，通过流所生成元素的hashCode()和equals()去除重复元素
        list.add(new Employee(10,"马斯克",50,21000.11));
        list.add(new Employee(10,"马斯克",50,21000.11));
        list.add(new Employee(10,"马斯克",50,21000.11));
        list.add(new Employee(10,"马斯克",50,21000.11));
        
        list.stream().distinct().forEach(System.out::println);

    }
    
    @Test
    public void test02(){
        /**
         * 映射
         */
        //map(Function f)：接收一个函数作为参数，将元素转换成其它形式或提取信息，该函数会被应用到每个元素上，并将其映射成一个新的元素
        //练习：将字母转换为大写
        String[] arr = new String[]{"aa","bb","cc","dd"};
        //方式1：
        Arrays.stream(arr).map(str -> str.toUpperCase()).forEach(System.out::println);
        //方式2：
        Arrays.stream((arr)).map(String::toUpperCase).forEach(System.out::println);
        
        //练习：获取员工姓名长度大于3的员工的姓名
        List<Employee> list = Employee.getEmployees();
        //获取员工姓名长度大于3的员工
        list.stream().filter(emp -> emp.getName().length() > 3).forEach(System.out::println);
        //方式1：
        list.stream().filter(emp -> emp.getName().length() > 3).map(Employee::getName).forEach(System.out::println);
        //方式2：
        list.stream().map(Employee::getName).filter(name -> name.length() > 3).forEach(System.out::println);


        /**
         * 排序
         */
        //sorted(): 自然排序
        Integer[] arr2 = new Integer[]{312,24123,132,4323,12,23};
        Arrays.stream(arr2).sorted().forEach(System.out::println);

    }
    
    @Test
    public void test03(){
        /**
         * 终止操作
         */
        /**
         * 匹配与查找
         */
        //allMatch(Predicate p): 检查是否匹配所有元素。
        //练习: 是否所有的员工的年龄都大于18
        List<Employee> list = Employee.getEmployees();
        System.out.println(list.stream().allMatch(emp -> emp.getAge() > 18));

        //anyMatch(Predicate p): 检查是否至少匹配一个元素。
        //练习: 是否存在年龄大于18岁的员工
        System.out.println(list.stream().anyMatch(emp -> emp.getAge() > 18));

        //findFirst一返回第一个元素
        System.out.println(list.stream().findFirst().get());
        
        //count(): 返回流的数量
        System.out.println(list.stream().filter(emp -> emp.getSalary() > 7000).count());
        
        //max(Comparator c): 返回流中最大值
        //返回工资最高的员工
        System.out.println(list.stream().max((e1,e2) -> Double.compare(e1.getSalary(),e2.getSalary())));
        //返回最高的工资
        System.out.println(list.stream().map(Employee::getSalary).max(Double::compareTo));
        
        //min(Comparator c): 返回流中最小值
        System.out.println(list.stream().map(Employee::getSalary).min(Double::compareTo));
        
        //forEach(Consumer c): 内部迭代
        list.stream().forEach(System.out::println);
    }
    
    @Test   
    public void test04(){
        /**
         * 归约
         */
        //reduce(T identity, BinaryOperator): 可以将流中元素反复结合起来，得到一个值。返回T
        //练习1：计算1-10的自然数的和
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(list.stream().reduce(0, (x1, x2) -> x1 + x2));
        System.out.println(list.stream().reduce(0, Integer::sum));//55
        System.out.println(list.stream().reduce(10, Integer::sum));//65

        //reduce(BinaryOperator): 可以将流中元素反复结合起来，得到一个值。返回Optional<T>
        //练习2：计算公司所有员工工资的总和
        List<Employee> list2 = Employee.getEmployees();
        System.out.println(list2.stream().map(Employee::getSalary).reduce(Double::sum));

    }
    
    @Test
    public void tesst05(){
        /**
         * 收集
         */
        //collect(Collector c): 将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法
        //练习1：查找工资大于6000的员工，结果返回为一个List或Set
        List<Employee> list = Employee.getEmployees();
        List<Employee> collect = list.stream().filter(emp -> emp.getSalary() > 6000).collect(Collectors.toList());
        collect.forEach(System.out::println);
        
        //练习2：按照员工的年龄进行排序，返回到一个新的List中
        List<Employee> collect1 = list.stream().sorted((e1, e2) -> e1.getAge() - e2.getAge()).collect(Collectors.toList());
        collect1.forEach(System.out::println);
        
    }
}

