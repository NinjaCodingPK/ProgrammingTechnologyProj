/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.command;

import com.wookie.devteam.controller.command.impl.CheckInCommand;
import com.wookie.devteam.controller.command.impl.AddProjectQualificationCommand;
import com.wookie.devteam.controller.command.impl.SetProjectDoneCommand;
import com.wookie.devteam.controller.command.impl.ShowProfileCommand;
import com.wookie.devteam.controller.command.impl.AddTaskCommand;
import com.wookie.devteam.controller.command.impl.BackToMainPageCommand;
import com.wookie.devteam.controller.command.impl.SubmitProjectSettings;
import com.wookie.devteam.controller.command.impl.SubmitRegistrationCommand;
import com.wookie.devteam.controller.command.impl.SubmitProjectQualificationCommand;
import com.wookie.devteam.controller.command.impl.ProcessProjectCommand;
import com.wookie.devteam.controller.command.impl.SubmitTaskCommand;
import com.wookie.devteam.service.factory.ServiceFactory;
import com.wookie.devteam.controller.command.impl.ShowProjectDevsCommand;
import com.wookie.devteam.controller.command.impl.ChangeTaskStatusCommand;
import com.wookie.devteam.controller.command.impl.LogoutCommand;
import com.wookie.devteam.controller.command.impl.ShowProjectsByPersonCommand;
import com.wookie.devteam.controller.command.impl.LoginCommand;
import com.wookie.devteam.controller.command.impl.SubmitFilledProjectCommand;
import com.wookie.devteam.controller.command.impl.CreateProjectCommand;
import com.wookie.devteam.controller.command.impl.FillProjectCommand;
import com.wookie.devteam.controller.command.impl.ShowTasksCommand;
import com.wookie.devteam.constants.Commands;
import com.wookie.devteam.controller.command.impl.ClientUpdateProjectCommand;
import com.wookie.devteam.controller.command.impl.ShowTaskStatusCommand;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wookie
 */
public class CommandList {
	private ServiceFactory serviceFactory;
	private final Map<String, Command> list = new HashMap<>();
	
    public CommandList(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
		commandListInit();
	}
	
    private void commandListInit() {
        list.put(Commands.USER_LOGIN_COMMAND, new LoginCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SHOW_DEV_LIST_COMMAND, new ShowProjectDevsCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SHOW_TASK_LIST_COMMAND, new ShowTasksCommand().setServiceFactory(serviceFactory));
        list.put(Commands.PROCESS_PROJECT_COMMAND, new ProcessProjectCommand().setServiceFactory(serviceFactory));
        list.put(Commands.CREATE_PROJECT_COMMAND, new CreateProjectCommand().setServiceFactory(serviceFactory));
        list.put(Commands.FILL_PROJECT_COMMAND, new FillProjectCommand().setServiceFactory(serviceFactory));
        list.put(Commands.ADD_TASK_COMMAND, new AddTaskCommand().setServiceFactory(serviceFactory));
        list.put(Commands.ADD_PROJECT_QUALIFICATION_COMMAND, new AddProjectQualificationCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SUBMIT_ADD_TASK_COMMAND, new SubmitTaskCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SUBMIT_ADD_PROJECT_QUALIFICATION_COMMAND, new SubmitProjectQualificationCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SUBMIT_FILLED_PROJECT_COMMAND, new SubmitFilledProjectCommand().setServiceFactory(serviceFactory));
        list.put(Commands.CHECK_IN_COMMAND, new CheckInCommand().setServiceFactory(serviceFactory));
        list.put(Commands.CHANGE_TASK_STATUS_COMMAND, new ChangeTaskStatusCommand().setServiceFactory(serviceFactory));
        list.put(Commands.LOGOUT_COMMAND, new LogoutCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SUBMIT_PROJECT_SETTINGS_COMMAND, new SubmitProjectSettings().setServiceFactory(serviceFactory));
        list.put(Commands.SHOW_PROFILE_COMMAND, new ShowProfileCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SHOW_PROJECTS_IN_PROGRESS_COMMAND, new ShowProjectsByPersonCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SET_PROJECT_DONE_COMMAND, new SetProjectDoneCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SHOW_TASK_STATUS_COMMAND, new ShowTaskStatusCommand().setServiceFactory(serviceFactory));
        list.put(Commands.CLIENT_UPDATE_PROJECT_COMMAND, new ClientUpdateProjectCommand().setServiceFactory(serviceFactory));
        list.put(Commands.SUBMIT_REGISTRATION_COMMAND, new SubmitRegistrationCommand().setServiceFactory(serviceFactory));
        list.put(Commands.BACK_TO_MAIN_PAGE_COMMAND, new BackToMainPageCommand().setServiceFactory(serviceFactory));
    }

    public Map<String, Command> getList() {
        return list;
    }
}
