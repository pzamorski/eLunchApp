package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.Archive;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@GeneratePojoBuilder
public class EmployeeDTO {
	public static class View {
		public interface Id {}
		public interface Basic extends Id {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Basic.class)
	@NotNull
	@Embedded
	private PersonalDataDTO personalDataDTO;

	@JsonView(View.Extended.class)
	@NotNull
	@Embedded
	private LogginDataDTO logginData;

	@JsonView(View.Extended.class)
	@NotNull
	private Archive archive;


	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public PersonalDataDTO getPersonalData() {
		return personalDataDTO;
	}

	public void setPersonalData(PersonalDataDTO personalDataDTO) {
		this.personalDataDTO = personalDataDTO;
	}

	public LogginDataDTO getLogginData() {
		return logginData;
	}

	public void setLogginData(LogginDataDTO logginData) {
		this.logginData = logginData;
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}
}
