package org.teiath.web.vm.user;

import org.apache.log4j.Logger;
import org.teiath.data.domain.User;
import org.teiath.data.ldap.LdapProperties;
import org.teiath.service.exceptions.AuthenticationException;
import org.teiath.service.exceptions.ServiceException;
import org.teiath.service.user.CreateUserService;
import org.teiath.service.user.UserValidationService;
import org.teiath.web.PasswordGenerator;
import org.teiath.web.session.ZKSession;
import org.teiath.web.util.MessageBuilder;
import org.teiath.web.util.PageURL;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;

import java.util.Date;

@SuppressWarnings("UnusedDeclaration")
public class CreateUserVM {

	static Logger log = Logger.getLogger(CreateUserVM.class.getName());

	@Wire("#genderCombo")
	private Combobox genderCombo;

	@WireVariable
	private UserValidationService userValidationService;
	@WireVariable
	private CreateUserService createUserService;
	@WireVariable
	private LdapProperties ldapProperties;

	private User user;
	private Validator userValidator;

	@AfterCompose
	@NotifyChange("user")
	public void afterCompose(
			@ContextParam(ContextType.VIEW)
			Component view) {
		Selectors.wireComponents(view, this, false);

		user = new User();
		userValidator = new UserValidator(userValidationService, ldapProperties);
	}

	@Command
	public void onSave() {
		try {
			user.setRegistrationDate(new Date());
			if (genderCombo.getValue().equals(Labels.getLabel("user.male"))) {
				user.setGender(User.GENDER_MALE);
			} else {
				user.setGender(User.GENDER_FEMALE);
			}
			user.setUserType(User.USER_TYPE_EXTERNAL);
			user.setPassword(new PasswordGenerator().getPassword());
			createUserService.saveUser(user);
			Messagebox.show(Labels.getLabel("user.message.success"), Labels.getLabel("common.messages.save_title"),
					Messagebox.OK, Messagebox.INFORMATION, new EventListener<Event>() {
				public void onEvent(org.zkoss.zk.ui.event.Event evt) {
					ZKSession.sendRedirect(PageURL.INDEX);
				}
			});
		} catch (AuthenticationException e) {
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("user.register")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR);
		} catch (ServiceException e) {
			log.error(e.getMessage());
			Messagebox.show(MessageBuilder.buildErrorMessage(e.getMessage(), Labels.getLabel("user.register")),
					Labels.getLabel("common.messages.save_title"), Messagebox.OK, Messagebox.ERROR);
		}
	}

	@Command
	public void onCancel() {
		ZKSession.sendRedirect(PageURL.INDEX);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Validator getUserValidator() {
		return userValidator;
	}

	public void setUserValidator(Validator userValidator) {
		this.userValidator = userValidator;
	}
}
