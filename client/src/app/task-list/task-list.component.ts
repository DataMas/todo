import { Component } from '@angular/core';
import {ApiService} from "../services/api.service";
import {TaskService} from "../services/task.service";
import {Task} from "../models/task.model";
import {ToastrService} from "ngx-toastr";
import {SharedService} from "../services/shared.service";
import {Subscription} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {EditTaskComponent} from "../edit-task/edit-task.component";

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})

export class TaskListComponent {
  tasks: Task[] = [];
  private subscriptions = new Subscription();


  constructor(private apiService: ApiService,
              private taskService: TaskService,
              private toastr: ToastrService,
              private sharedService: SharedService,
              public dialog: MatDialog) {}

  ngOnInit():void {
    this.getSetTasks();

    this.subscriptions.add(this.sharedService.refreshNeeded.subscribe(() => {
      this.getSetTasks();
    }));
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  getSetTasks() {
    this.taskService.getShowTasks().subscribe({
      next: (data) => {
        this.tasks = data
      this.tasks.map(item => {
        let time = item.deadline.split("T")[1].split(":")[0] + ":"+item.deadline.split("T")[1].split(":")[1];
        item.taskDate=item.deadline.split("T")[0].replaceAll("-","/");
        item.taskTime=time;
      })},
      error: (err) => this.toastr.error('Failed to load tasks.')
    });
  }

  updateTaskStatus(task: Task, status: number): void {
    // Set the task status to 1 (deleted/completed)
    task.status = status;

    // Call the service to update the task on the server
    this.taskService.updateTask(task).subscribe({
      next: (updatedTask) => {
        this.toastr.success('Task status updated successfully!');
        this.getSetTasks();
        // Update the local tasks array or refresh the list from the server
      },
      error: (error) => {
        this.toastr.error('Failed to update task status.');
        console.error('There was an error!', error);
      }
    });
  }

  openEditModal(task: Task): void {
    const dialogRef = this.dialog.open(EditTaskComponent, {
      width: '100%',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Handle the result, update the task
        console.log('The dialog was closed, result:', result);
      }
    });
  }
}
