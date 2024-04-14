import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {Task} from "../models/task.model";

@Injectable({providedIn: 'root'})
export class TaskService {
    private baseUrl = "http://localhost:8080/api/categories";

    constructor(private http:HttpClient) {}

    createTask(task: Task): Observable<Task> {
        return this.http.post<Task>(this.baseUrl+'/'+task.category+"/tasks", task);
    }
}