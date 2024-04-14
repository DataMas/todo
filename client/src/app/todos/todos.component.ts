import {Component, model, OnInit} from '@angular/core';
import {ApiService} from "../services/api.service";
import {TaskCategoriesService} from "../services/taskCategories.service";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-todos',
  templateUrl: './todos.component.html',
  styleUrl: './todos.component.css'
})
export class TodosComponent {
  model = {
    categoryName: "",
    categoryDescription: "",
  };

  // categories = [
  //   { id: 1, name: 'Option 1' },
  //   { id: 2, name: 'Option 2' },
  //   { id: 3, name: 'Option 3' }
  // ]

  // categoriesForm: FormGroup;
  categoriesList: any[] = [];
  //
  selectedCategory: string = "";

  constructor(private apiService: ApiService, private taskCategoryService: TaskCategoriesService) {
    // this.selectedCategory = this.categories[0].id;
    // this.categoriesForm = t
  };





}
