import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import { tap } from 'rxjs/operators';

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
  // addItem(item: any): Observable<any> {
  //   return this.http.post<any>(this.baseUrl, item).pipe(
  //     tap(newItem => {
  //       const items = this.itemsSubject.getValue();
  //       this.itemsSubject.next([...items, newItem]); // Add new item to the current items
  //     })
  //   );
  // }

}
