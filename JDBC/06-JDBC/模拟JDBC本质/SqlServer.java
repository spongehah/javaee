
/*
	SqlServer的数据库厂家负责编写JDBC接口的实现类
*/
public class SqlServer implements JDBC{

	public void getConnection(){
		System.out.println("连接SQLServer数据库成功！");
	}
}

// 实现类被称为驱动。（sqlserver驱动）

// xxx.jar    当中有很多.class，都是对JDBC接口进行的实现。