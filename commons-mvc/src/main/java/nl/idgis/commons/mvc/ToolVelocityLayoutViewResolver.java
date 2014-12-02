package nl.idgis.commons.mvc;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * Variation on the Spring VelocityLayoutViewResolver that uses the ToolLayoutVelocity view and doesn't depend
 * on the velocity toolbox.
 */
public class ToolVelocityLayoutViewResolver extends VelocityViewResolver {

	private String layoutUrl;
	private String layoutKey;
	private String screenContentKey;
	
	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}
	
	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}
	
	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}
	
	/**
	 * Requires VelocityLayoutView.
	 * @see VelocityLayoutView
	 */
	@Override
	protected Class<?> requiredViewClass() {
		return ToolLayoutVelocityView.class;
	}
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		ToolLayoutVelocityView view = (ToolLayoutVelocityView) super.buildView(viewName);
		// Use not-null checks to preserve VelocityLayoutView's defaults.
		if (this.layoutUrl != null) {
			view.setLayoutUrl(this.layoutUrl);
		}
		if (this.layoutKey != null) {
			view.setLayoutKey(this.layoutKey);
		}
		if (this.screenContentKey != null) {
			view.setScreenContentKey(this.screenContentKey);
		}
		return view;
	}	
}
