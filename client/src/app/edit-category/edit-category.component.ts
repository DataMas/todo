import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {TaskCategoriesService, TaskCategory} from "../services/taskCategories.service";
import {ToastrService} from "ngx-toastr";
import {SharedService} from "../services/shared.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Category} from "../models/category.model";

@Component({
  selector: 'app-edit-category',
  templateUrl: './edit-category.component.html',
  styleUrl: './edit-category.component.css'
})
export class EditCategoryComponent {

  categoryForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient,
              private categoryService: TaskCategoriesService,
              private toastr: ToastrService,
              private sharedService: SharedService,
              public dialogRef: MatDialogRef<EditCategoryComponent>,
              @Inject(MAT_DIALOG_DATA) public category: Category) {
    console.log(category)
    this.categoryForm = this.fb.group({
      categoryId: [this.category.categoryId],
      categoryName: [this.category.categoryName, Validators.required],
      categoryDescription: [this.category.categoryDescription]
    });
  };

  updateCategory(): void {
    console.log(this.categoryForm);
    if (this.categoryForm.valid) {
      const formValue = this.categoryForm.value;
      const category = {
        categoryId: formValue.categoryId,
        categoryName: formValue.categoryName,
        categoryDescription: formValue.categoryDescription
      };

      this.categoryService.updateCategory(category).subscribe({
        next:() => {
          this.toastr.success("Category updated successfully!");
          this.triggerRefresh();
          this.onClose();
      },
        error: (error) => {
          this.toastr.error('Failed to update category.');
        }
      });
    } else {
      this.toastr.error('Category name is required');
    }
  }

  triggerRefresh() {
    this.sharedService.triggerRefresh();
  }

  onSubmit(): void {
    this.updateCategory();
  }

  onClose(): void {
    this.dialogRef.close();
  }



}
