
/*
	MySQL的数据库厂家负责编写JDBC接口的实现类
*/
public class MySQL implements JDBC{

	public void getConnection(){
		// 具体这里的代码怎么写，对于我们Java程序员来说没关系
		// 这段代码涉及到mysql底层数据库的实现原理。
		System.out.println("连接MYSQL数据库成功！");
	}
}

// 实现类被称为驱动。（mysql驱动）