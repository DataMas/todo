import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {Task} from "../models/task.model";

@Injectable({providedIn: 'root'})
export class TaskService {
    private baseUrl = "http://localhost:8080/api";

    constructor(private http:HttpClient) {}

    createTask(task: Task): Observable<Task> {
        return this.http.post<Task>(this.baseUrl+'/categories/'+task.category+"/tasks", task);
    }

    getAllTasks(): Observable<Task[]> {
        return this.http.get<Task[]>(this.baseUrl+"/tasks");
    }

    getShowTasks(): Observable<Task[]> {
        return this.http.get<Task[]>(this.baseUrl+"/tasks/statusShow");
    }

    updateTask(task: Task): Observable<Task> {
        if (!task.id) {
            throw new Error('Task ID is required for updating a task.');
        }
        return this.http.put<Task>(`${this.baseUrl}/tasks/${task.id}`, task);
    }
}