package com.edu_netcracker.academ_resourse.services;

import com.edu_netcracker.academ_resourse.domain.Task;
import com.edu_netcracker.academ_resourse.domain.TaskLvl;
import com.edu_netcracker.academ_resourse.repos.ITasksRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class TasksService {

    final
    ITasksRepo tasksRepo;

    final
    TaskLvlService taskLvlService;



    final static Logger logger = LoggerFactory.getLogger(TasksService.class);

    public TasksService(ITasksRepo tasksRepo, TaskLvlService taskLvlService) {
        this.tasksRepo = tasksRepo;
        this.taskLvlService = taskLvlService;
    }


    public Task addTasks(final Task task) {
        Task taskFromBD = tasksRepo.findTasksByTaskAndDate(task.getTask(), task.getDate());

        if (taskFromBD != null) {
            return taskFromBD;
        } else{
            tasksRepo.save(task);
            return task;
        }
    }

    public void setTaskLvl(Task task, String taskLvl) {
        task.setTaskLvl(taskLvlService.addTaskLvl(new TaskLvl(taskLvl)));

        tasksRepo.saveAndFlush(task);
    }


    private Set<Task> getTasks(Set<Task> tasks, Date[] dates) {
        HashSet<Task> result = new HashSet<>();
        for(Task task : tasks) {
            if(task.getDate().getTime() > dates[0].getTime() && task.getDate().getTime() < dates[1].getTime()) {
                result.add(task);
            }
        }
        return result;
    }

}
