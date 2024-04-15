import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApiService} from "../services/api.service";
import {TaskCategoriesService} from "../services/taskCategories.service";
import {ToastrService} from "ngx-toastr";
import {TaskService} from "../services/task.service";
import {Task} from "../models/task.model";
import {SharedService} from "../services/shared.service";

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrl: './task-form.component.css'
})

export class TaskFormComponent {
  taskForm: FormGroup;
  categoriesList: any[] = [];
  tasks: Task[] = [];
  //
  selectedCategory: string = "";

  constructor(private apiService: ApiService,
              private taskCategoryService: TaskCategoriesService,
              private taskService: TaskService,
              private fb: FormBuilder,
              private toastr: ToastrService,
              private sharedService: SharedService) {
    this.taskForm = this.fb.group({
      taskName: ['', Validators.required],
      taskDescription: [''],
      date: ['', Validators.required],
      time: [''],
      category: [null, Validators.required] // Prepopulate with categories
    });
  };

  ngOnInit():void {
    this.taskCategoryService.taskCategories$.subscribe(items => {
      this.categoriesList = items;
    });

    this.taskCategoryService.getCategoryNames().subscribe();
  }

  triggerRefresh() {
    // Call the service method to notify other components
    this.sharedService.triggerRefresh();
  }

  onSubmit(): void {

    if (this.taskForm.valid) {
    const formValue = this.taskForm.value;
    const task = {
      taskName: formValue.taskName,
      taskDescription: formValue.taskDescription,
      // Combine date and time to a ISO string, assuming date and time are in proper format
      deadline: new Date(formValue.date + 'T' + formValue.time).toISOString(),
      category: formValue.category,
      status: 0
    };

    this.taskService.createTask(task).subscribe({
      next: () => {
        this.toastr.success('Task added successfully!');
        this.taskForm.reset();
        this.triggerRefresh();
      },
      error: (error) => {
        this.toastr.error('Failed to add task.');
        console.error('There was an error!', error);
      }
    });
  } else {
    this.toastr.error('Name, data, time and category are required', 'New todo');
    this.taskForm.markAllAsTouched();
  }
  }

}
