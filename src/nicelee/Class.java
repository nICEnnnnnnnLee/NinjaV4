package nicelee;


public class Class {

	public static java.lang.Class<?> forName(String className) throws ClassNotFoundException {
		
		return java.lang.Class.forName(className, false, Class.class.getClassLoader());
	}
}
