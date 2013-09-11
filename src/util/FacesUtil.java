package util;


import java.util.List;
import java.util.ArrayList;
import org.eclipse.emf.ecore.util.EDataTypeEList;


public class FacesUtil {
	
	// Getters
	// -----------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public static String getObjectPuma(Object object) {
		String value = "";
		try {
			if (object instanceof List) {
				if (object instanceof EDataTypeEList) {
					List l = (EDataTypeEList) object;
					value = objectToString(l);
				} else {
					List l = (ArrayList) object;
					value = objectToString(l);
				}
			} else
				value = objectToString(object);
		} catch (Exception e) {
		}
		return value;
	}

	// Converters
	// -----------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	private static String objectToString(List l) {
		String retorno = "";
		if (!l.isEmpty())
			if (l.get(0) instanceof String)
				retorno = objectToString(l.get(0));			
		return retorno;
	}

	private static String objectToString(Object object) {
		return (String) object;
	}
}
