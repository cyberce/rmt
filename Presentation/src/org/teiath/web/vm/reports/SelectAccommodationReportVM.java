package org.teiath.web.vm.reports;

import org.teiath.web.session.ZKSession;
import org.teiath.web.util.PageURL;
import org.teiath.web.vm.BaseVM;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;

@SuppressWarnings("UnusedDeclaration")
public class SelectAccommodationReportVM
		extends BaseVM {

	@Wire("#typesCombo")
	Combobox typesCombo;

	@AfterCompose
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);
	}

	@Command
	public void onNextStep() {
		if (typesCombo.getSelectedItem().getValue().toString().equals("0")) {
			ZKSession.sendRedirect(PageURL.ACCOMMODATIONS_BY_ACCOMMODATION_TYPE_DIALOG);
		} else if (typesCombo.getSelectedItem().getValue().toString().equals("1")) {
			ZKSession.sendRedirect(PageURL.ACCOMMODATIONS_BY_NUMBER_OF_BEDROOMS_DIALOG);
		}
	}
}
