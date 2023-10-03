
/*
	Java程序员角色。
	不需要关心具体是哪个品牌的数据库，只需要面向JDBC接口写代码。
	面向接口编程，面向抽象编程，不要面向具体编程。
*/
import java.util.*;

public class JavaProgrammer
{
	public static void main(String[] args) throws Exception{
		// JDBC jdbc = new MySQL();
		// JDBC jdbc = new SqlServer();

		// 创建对象可以通过反射机制。
		ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
		String className = bundle.getString("className");
		Class c = Class.forName(className);
		JDBC jdbc = (JDBC)c.newInstance();

		// 以下代码都是面向接口调用方法，不需要修改
		jdbc.getConnection();
	}
}