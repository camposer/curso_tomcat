package editor;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FechaEditor extends PropertyEditorSupport {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		Date fecha = null;

		try {
			fecha = sdf.parse(text);
		} catch (ParseException e) {
		}

		setValue(fecha);
	}

	@Override
	public String getAsText() {
		String fecha = "";
		
		if (getValue() != null)
			fecha = sdf.format(super.getValue());
		
		return fecha;
	}
}
