package com.service.factory;

import com.service.PersonProjectService;
import com.service.PersonService;
import com.service.ProjectQualificationService;
import com.service.ProjectService;
import com.service.QualificationService;
import com.service.StatusService;
import com.service.TaskService;

public class ServiceFactory {
	private PersonProjectService personProjectService;
	private PersonService personService;
	private ProjectQualificationService projectQualificationService;
	private ProjectService projectService;
	private QualificationService qualificationService;
	private StatusService statusService;
	private TaskService taskService;
	
	public PersonProjectService getPersonProjectService() {
		return personProjectService;
	}
	
	public void setPersonProjectService(PersonProjectService personProjectService) {
		this.personProjectService = personProjectService;
	}
	
	public PersonService getPersonService() {
		return personService;
	}
	
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	public ProjectQualificationService getProjectQualificationService() {
		return projectQualificationService;
	}
	
	public void setProjectQualificationService(ProjectQualificationService projectQualificationService) {
		this.projectQualificationService = projectQualificationService;
	}
	
	public ProjectService getProjectService() {
		return projectService;
	}
	
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	public QualificationService getQualificationService() {
		return qualificationService;
	}
	
	public void setQualificationService(QualificationService qualificationService) {
		this.qualificationService = qualificationService;
	}
	
	public StatusService getStatusService() {
		return statusService;
	}
	
	public void setStatusService(StatusService statusService) {
		this.statusService = statusService;
	}
	
	public TaskService getTaskService() {
		return taskService;
	}
	
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}
}
