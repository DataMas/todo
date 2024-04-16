import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { tap } from 'rxjs/operators';
import {Task} from "../models/task.model";
import {Category} from "../models/category.model";

export interface TaskCategory {
  id?: number;
  name: string;
  description: string;
}


@Injectable({
  providedIn: 'root'
})

export class TaskCategoriesService {
  private baseUrl = 'http://localhost:8080/api/categories';
  private taskCategoriesSubject = new BehaviorSubject(<TaskCategory[]>([]));

  taskCategories$ = this.taskCategoriesSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Get all the categories
   */
  getCategoryNames(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl).pipe(
      tap(categoryNames => this.taskCategoriesSubject.next(categoryNames))
    );
  }

  /**
   * Get all categories
   */
  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl);
  }

  /**
   * Add new category
   * @param item
   */
  addCategory(item: TaskCategory): Observable<TaskCategory> {
    return this.http.post<TaskCategory>(this.baseUrl, item).pipe(
      tap(newItem => {
        const items = this.taskCategoriesSubject.getValue();
        this.taskCategoriesSubject.next([...items, newItem]);
      })
    )
  }

  deleteCategory(id: number): Observable<any> {
    return this.http.delete(this.baseUrl+`/${id}`)
  }

  updateCategory(category: Category): Observable<any> {
    if (!category.categoryId) {
      throw new Error("Category ID is required for update.")
    }
    return this.http.put(this.baseUrl+`/${category.categoryId}`, category);
  }
}
