package irt.data;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class JacksonTest {

    protected final Logger logger = (Logger) LogManager.getLogger();

    @Test
	public void test(){
		Object o = new TestClass1();
		try {
			logger.error(Jackson.objectToJsonString(o));
		} catch (IOException e) {
			logger.catching(e);
		}
	}

    class TestClass1{
    	String className = "Class Name 1";
    	TestClass2 class2 = new TestClass2();

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public TestClass2 getClass2() {
			return class2;
		}

		public void setClass2(TestClass2 class2) {
			this.class2 = class2;
		} 
    }

    class TestClass2{
    	String className = "Class Name 2";

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		} 
    }
}
