import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {TaskCategoriesService, TaskCategory} from "../services/taskCategories.service";
import {ToastrService} from "ngx-toastr";
import {SharedService} from "../services/shared.service";

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.css'
})
export class StatsComponent {

  categoryForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient,
              private taskCategoryService: TaskCategoriesService,
              private toastr: ToastrService,
              private sharedService: SharedService) {
    this.categoryForm = this.fb.group({
      categoryName: ['', Validators.required],
      categoryDescription: ['']
    });
  }

  triggerRefresh() {
    // Call the service method to notify other components
    this.sharedService.triggerRefresh();
  }

  onSubmit(): void {
    if (this.categoryForm.valid) {
      this.taskCategoryService.addCategory(this.categoryForm.value as TaskCategory)
        .subscribe(
          () => {
            this.toastr.success("New task category created.", "Success!")
            this.categoryForm.reset();
            this.triggerRefresh();
          },
          error => {
            this.toastr.error("Internal server error.", "Server error")
          }
        )
    } else {
      this.toastr.error("Name is required", "New category");
    }
  }

}
