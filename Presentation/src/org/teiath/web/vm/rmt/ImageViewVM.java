package org.teiath.web.vm.rmt;

import org.teiath.web.session.ZKSession;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

@SuppressWarnings("UnusedDeclaration")
public class ImageViewVM {

	@Wire("#imageViewWin")
	private Window win;
	@Wire("#imageView")
	private Image image;

	private String fileName;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		win.setHeight("600");
		win.setWidth("600");

		fileName = (String) ZKSession.getAttribute("imgFileName");
		image.setSrc("/images/accommodations/" + fileName);
	}

	@Command
	public void onCancel() {
		win.detach();
	}
}
