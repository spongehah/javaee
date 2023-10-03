
/*
	Oracle的数据库厂家负责编写JDBC接口的实现类
*/
public class Oracle implements JDBC{

	public void getConnection(){
		System.out.println("连接Oracle数据库成功！");
	}
}

// 实现类被称为驱动。（oracle驱动）