package marvin.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassLoader to load classes from jar files dynamically. This feature is used to
 * load plug-ins.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinJarLoader extends ClassLoader {
	
	private JarFile jarFile;
	private Enumeration<JarEntry> eJarEntries;
	
	/**
	 * Constructor
	 * @param a_jarPath 	- jar file´s path.
	 */
	MarvinJarLoader(String a_jarPath){
		super();
		
		try{
			jarFile = new JarFile(a_jarPath);
		}
		catch(IOException a_expt){
			a_expt.printStackTrace();
		}
	}
	
	/**
	 * @return an object instance of the class specified as parameter. The class must be inside the
	 * jar file.
	 */
	public Object getObject(String a_className){
		Class l_class = getClass(a_className);
		try{
			Constructor<?> l_con = l_class.getConstructor();
			return l_con.newInstance();
		}
		catch(Exception a_expt){
			a_expt.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get a Class object from a class inside the Jar file by its name.
	 * @param a_name	- class name.
	 * @return a Class object
	 */
	public Class<?> getClass(String a_name){		
		a_name = a_name.replace(".class", "");
		eJarEntries = jarFile.entries();
		JarEntry l_entry = null;
		byte[] l_arrBuffer=null;
		String l_name;
		while(eJarEntries.hasMoreElements()){
			l_entry = eJarEntries.nextElement();
			if(l_entry.getName().contains(a_name+".class")){
				l_arrBuffer = getEntryBytes(l_entry);
				Class l_class = super.defineClass(null, l_arrBuffer, 0, (int)l_entry.getSize());
				return l_class;
			}
		}
		return null;		
	}
	
	/**
	 * @param a_entry	- JarEntry to be read.
	 * @returns a byte array of the entry´s content.
	 */
	public byte[] getEntryBytes(JarEntry a_entry){
		int l_size = (int)a_entry.getSize();
		byte[] l_arrBuffer = new byte[l_size];
		InputStream l_inputStream;		
		try{
			l_inputStream = jarFile.getInputStream(a_entry);
			for(int i=0; i<l_size; i++){
				l_arrBuffer[i] = (byte)l_inputStream.read();
			}
		}
		catch(IOException a_expt){
			a_expt.printStackTrace();
		}
		return l_arrBuffer;
	}
	
	/**
	 * Find a class specified as parameter. In this case, the class must be found in the Jar
	 * file. The methods getClass returns a Class object of the specified class whether it´s inside the
	 * jar file.
	 * @param a_name	- class name
	 * @return Class object
	 */
	public Class<?> findClass(String a_name){
		return getClass(a_name.substring(a_name.lastIndexOf('.')+1));
	}
}
