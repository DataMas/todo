import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  getHelloWorld(): Observable<string> {
    return this.http.get<string>('http://localhost:8080/api/hello-world', { responseType: 'text' as 'json' });
  }

  sendData(data: any) {
    const apiUrl = 'http://localhost:8080/api/categories';
    return this.http.post(apiUrl, data);
  }
}
