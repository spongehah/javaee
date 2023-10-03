package project.ui;

import org.w3c.dom.css.CSSUnknownRule;
import project.bean.Customer;
import project.service.CustomerList;
import project.util.CMUtility;

public class CustomerView {
    private CustomerList customerList = new CustomerList(10);

    public CustomerView(){
        Customer customer = new Customer("王涛 ",'男',23,"17312341234","wt@gmail.com");
        customerList.addCustomer(customer);
    }

    public void enterMainMenu(){
        boolean isFlag = true;

        while(isFlag){
            System.out.println();
            System.out.println("-----------------客户信息管理软件-----------------");
            System.out.println();
            System.out.println("                 1 添 加 客 户");
            System.out.println("                 2 修 改 客 户");
            System.out.println("                 3 删 除 客 户");
            System.out.println("                 4 客 户 列 表");
            System.out.println("                 5 退       出\n");
            System.out.printf("                 请选择(1-5)：");

            char menu = CMUtility.readMenuSelection();
            switch (menu){
                case '1':
                    addNewCustomer();
                    break;
                case '2':
                    modifyCustomer();
                    break;
                case '3':
                    deleteCustomer();
                    break;
                case '4':
                    listAllCustomers();
                    break;
                case '5':
                    System.out.printf("确认是否退出(Y/N)：");
                    char isExit = CMUtility.readConfirmSelection();
                    if(isExit == 'Y'){
                        isFlag = false;
                        break;
                    }
            }
        }
    }

    private void addNewCustomer(){
        System.out.println("---------------------添加客户---------------------");
        System.out.println("姓名：");
        String name = CMUtility.readString(5);
        if(name.length() == 2) name = name + " ";
        System.out.println("性别：");
        char gender = CMUtility.readChar();
        System.out.println("年龄：");
        int age = CMUtility.readInt();
        System.out.println("电话：");
        String phone = CMUtility.readString(15);
        System.out.println("邮箱：");
        String email = CMUtility.readString(15);

        Customer customer = new Customer(name,gender,age,phone,email);
        boolean flag = customerList.addCustomer(customer);
        if(flag){
            System.out.println("---------------------添加完成---------------------");
        }else{
            System.out.println("----------------记录已满,无法添加-----------------");
        }
    }

    private void modifyCustomer(){
        System.out.println("---------------------修改客户---------------------");

        int index;
        Customer customer;
        for(;;){
            System.out.print("请选择1要修改的客户编号(-1退出)：");
            index = CMUtility.readInt();
            if(index == -1){
                return;
            }
            customer = customerList.getCustomer(index - 1);
            if(customer == null) {
                System.out.println("无法找到指定客户！");
            }else {
                break;
            }
        }

        System.out.print("姓名(" + customer.getName() + ")：");
        String name = CMUtility.readString(5, customer.getName());

        System.out.print("性别(" + customer.getGender() + ")：");
        char gender = CMUtility.readChar(customer.getGender());

        System.out.print("年龄(" + customer.getAge() + ")：");
        int age = CMUtility.readInt(customer.getAge());

        System.out.print("电话(" + customer.getPhone() + ")：");
        String phone = CMUtility.readString(15, customer.getPhone());

        System.out.print("邮箱(" + customer.getEmail() + ")：");
        String email = CMUtility.readString(15, customer.getEmail());

        customer = new Customer(name,gender,age,phone,email);

        boolean flag = customerList.replaceCustomer(index - 1,customer);
        if(flag){
            System.out.println("---------------------修改完成---------------------");
        }else {
            System.out.println("----------无法找到指定客户,修改失败--------------");
        }
    }

    private void deleteCustomer(){
        System.out.println("---------------------删除客户---------------------");

        int index;
        Customer customer;
        while(true){
            System.out.print("请选择1要删除的客户编号(-1退出)：");
            index = CMUtility.readInt();
            if(index == -1){
                return ;
            }
            customer = customerList.getCustomer(index - 1);
            if(customer == null){
                System.out.println("无法找到指定客户！");
            }else {
                break;
            }
        }

        System.out.print("请确认是否删除(Y/N)：");
        char isDelete = CMUtility.readConfirmSelection();
        if(isDelete == 'N'){
            return;
        }
        boolean flag = customerList.deleteCustomer(index - 1);
        if(flag){
            System.out.println("---------------------删除完成---------------------");
        }else {
            System.out.println("----------无法找到指定客户,删除失败--------------");
        }
    }

    private void listAllCustomers(){
        System.out.printf("---------------------------客户列表---------------------------\n");

        int total = customerList.getTotal();
        if(total == 0){
            System.out.println("没有客户记录");
        }
        else {
            System.out.println("编号\t姓名\t\t性别\t年龄\t电话\t\t\t邮箱");
            Customer[] customers = customerList.getAllCustomers();
            for( int i = 0; i < customers.length; i++){
                Customer customer = customers[i];
                System.out.println((i + 1) + "\t" + customer.getName() + "\t" + customer.getGender() + "\t"  + customer.getAge() + "\t"
                + customer.getPhone() + "\t" + customer.getEmail());
            }
        }


        System.out.printf("-------------------------客户列表完成-------------------------\n");
    }

    public static void main(String[] args){
        CustomerView customerView = new CustomerView();

        customerView.enterMainMenu();

    }

}
