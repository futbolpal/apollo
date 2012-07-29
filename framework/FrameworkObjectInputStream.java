package framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class FrameworkObjectInputStream extends ObjectInputStream {

	protected FrameworkObjectInputStream(FileInputStream str)
			throws IOException, SecurityException {
		super(str);
	}

	protected Class resolveClass(ObjectStreamClass osc) throws IOException,
			ClassNotFoundException {
		return Class.forName(osc.getName(), true, ApolloFramework.instance()
				.getClassLoader());
	}

}
