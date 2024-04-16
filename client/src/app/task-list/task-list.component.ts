import { Component } from '@angular/core';
import {ApiService} from "../services/api.service";
import {TaskService} from "../services/task.service";
import {Task} from "../models/task.model";
import {ToastrService} from "ngx-toastr";
import {SharedService} from "../services/shared.service";
import {Subscription} from "rxjs";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {EditTaskComponent} from "../edit-task/edit-task.component";
import {Category} from "../models/category.model";
import {TaskCategoriesService} from "../services/taskCategories.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {EditCategoryComponent} from "../edit-category/edit-category.component";

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})

export class TaskListComponent {
  tasks: Task[] = [];
  categories: Category[] =[];
  private subscriptions = new Subscription();
  buttonText: string = "Tasks"


  constructor(private apiService: ApiService,
              private taskService: TaskService,
              private categoryService: TaskCategoriesService,
              private toastr: ToastrService,
              private sharedService: SharedService,
              public dialog: MatDialog) {}

  ngOnInit():void {
    this.getSetTasks();
    this.getSetCategories()

    this.subscriptions.add(this.sharedService.refreshNeeded.subscribe(() => {
      this.getSetTasks();
      this.getSetCategories();
    }));
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

  /**
   * Get and set tasks
   */
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

  /**
   * Get and set categories
   */
  getSetCategories() {
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categories = data},
      error: () => this.toastr.error('Failed to load tasks.')
    })
  }

  /**
   * Update task status
   * @param task The task to update
   * @param status The status to update to
   */
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
      }
    });
  }

  /**
   * Delete task category
   * @param id The id of the category to be deleted
   */
  deleteCategory(id: number): void {
    this.categoryService.deleteCategory(id).subscribe(
        () => {
          this.toastr.success('Category deleted successfully!');
          this.getSetCategories();
        },
        error => {
          this.toastr.error('Failed to delete category.');
        }
    )
  }

  /**
   * Open task edit modal
   * @param task The task to edit
   */
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

  openCategoryEditModal(category: Category): void {
    const dialogRef:MatDialogRef<EditCategoryComponent> = this.dialog.open(EditCategoryComponent, {
      width: '100%',
      data: category
    });
  }

  /**
   * Change display view between tasks and categories
   */
  toggleButtonText() {
    console.log(this.categories)
    this.buttonText = this.buttonText === "Tasks" ? "Categories" : "Tasks";
  }
}
