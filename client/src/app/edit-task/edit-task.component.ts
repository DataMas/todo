import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../models/task.model";
import {ApiService} from "../services/api.service";
import {TaskCategoriesService} from "../services/taskCategories.service";
import {TaskService} from "../services/task.service";
import {ToastrService} from "ngx-toastr";
import {SharedService} from "../services/shared.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {coerceStringArray} from "@angular/cdk/coercion";
import {find} from "rxjs";
import {Category} from "../models/category.model";
import {TaskRead} from "../models/task-read.model";

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrl: './edit-task.component.css'
})
export class EditTaskComponent {
  taskForm: FormGroup;
  categoriesList: any[] = [];

  //
  selectedCategory: string = "";

  constructor(private apiService: ApiService,
              private taskService: TaskService,
              private taskCategoryService: TaskCategoriesService,
              private fb: FormBuilder,
              private toastr: ToastrService,
              private sharedService: SharedService,
              public dialogRef: MatDialogRef<EditTaskComponent>,
              @Inject(MAT_DIALOG_DATA) public task: TaskRead) {
    console.log(task);
    this.taskForm = this.fb.group({
      taskName: [this.task.taskName, Validators.required],
      taskDescription: [this.task.taskDescription],
      date: [this.task.deadline.split("T")[0], Validators.required],
      time: [this.task.deadline.split("T")[1].split("+")[0]],
      category: [this.getIdByName(this.categoriesList,this.task.categoryName), Validators.required] // Prepopulate with categories
    });
  };

  triggerRefresh() {
    // Call the service method to notify other components
    this.sharedService.triggerRefresh();
  }


  ngOnInit():void {
    this.taskCategoryService.taskCategories$.subscribe(items => {
      this.categoriesList = items;
    });

    this.taskCategoryService.getCategoryNames().subscribe();
  }

  getIdByName(categs:Category[], name: String):number {
    const category = categs.find(item => item.categoryName === name);
    return category ? category.categoryId : 1;
  }

  updateStatus(): void {
    console.log(this.taskForm);
    console.log(this.categoriesList)
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      const task = {
        id: this.task.id,
        taskName: formValue.taskName,
        taskDescription: formValue.taskDescription,
        // Combine date and time to a ISO string, assuming date and time are in proper format
        deadline: new Date(formValue.date + 'T' + formValue.time).toISOString(),
        categoryId:  parseInt(formValue.category),
        status: 0
      };

      // Call the service to update the task on the server
      this.taskService.updateTask(task).subscribe({
        next: (updatedTask) => {
          this.toastr.success('Task updated successfully!');
          this.triggerRefresh();
          this.onClose();
          // Update the local tasks array or refresh the list from the server
        },
        error: (error) => {
          this.toastr.error('Failed to update task status.');
          console.error('There was an error!', error);
        }
      });
    }
  }

  onSubmit(): void {
    this.updateStatus();
  }

  onClose(): void {
    this.dialogRef.close();
  }

}
