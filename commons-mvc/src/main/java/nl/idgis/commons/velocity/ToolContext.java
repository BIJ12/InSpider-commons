package nl.idgis.commons.velocity;

import java.util.Map;

import nl.idgis.commons.velocity.tools.DateTool;
import nl.idgis.commons.velocity.tools.EscapeTool;
import nl.idgis.commons.velocity.tools.ExceptionTool;
import nl.idgis.commons.velocity.tools.NumberTool;
import nl.idgis.commons.velocity.tools.StringTool;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * A convenient velocity subclass that registers several Velocity tools by default:
 * - EscapeTool: escaping for various languages (including HTML and JavaScript)
 * - NumberTool: various number formatting methods
 * - DateTool: date and time formatting methods.
 *
 * These tools are available using the 'esc', 'number' and 'date' keys respectively.
 */
public class ToolContext extends VelocityContext {
	
	private static EscapeTool escapeTool = new EscapeTool ();
	private static NumberTool numberTool = new NumberTool ();
	private static DateTool dateTool = new DateTool ();
	private static StringTool stringTool = new StringTool ();
	private static ExceptionTool exceptionUtils = new ExceptionTool ();
	
	public ToolContext () {
		super ();
		
		registerTools ();
	}
	
	public ToolContext (final Context innerContext) {
		super (innerContext);
		
		registerTools ();
	}
	
	public ToolContext (final Map<?, ?> context) {
		super (context);
		
		registerTools ();
	}
	
	public ToolContext (final Map<?, ?> context, final Context innerContext) {
		super (context, innerContext);
		
		registerTools ();
	}
	
	private void registerTools () {
		put ("esc", escapeTool);
		put ("number", numberTool);
		put ("date", dateTool);
		put ("strings", stringTool);
		put ("exceptionUtils", exceptionUtils);
	}
}