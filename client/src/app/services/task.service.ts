import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

export interface Task {
    taskName: string;
    taskDescription : string;
    deadline: string;
    category: number
}

export class TaskService {
    private baseUrl = "http://localhost:8080/api/categories";

    constructor(private http:HttpClient) {
    }

    addTask(task: Task): Observable<Task> {
        return this.http.post<Task>(this.baseUrl, task);
    }
}