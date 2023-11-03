package com.patzam.elunchapp.events;

import org.springframework.context.ApplicationEvent;
import com.patzam.elunchapp.DTO.UserDTO;

public class OperationEvidenceCreator extends ApplicationEvent {

	private final UserDTO userDTO;

	public OperationEvidenceCreator(Object source, UserDTO userDTO) {
		super(source);
		this.userDTO = userDTO;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}
}
