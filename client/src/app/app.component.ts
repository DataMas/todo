import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ApiService} from "./services/api.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'client';
  result = '';

  constructor(private apiService: ApiService) {}

  sayHello() {
    this.result = 'loading...'
    this.apiService.getHelloWorld().subscribe((data: string) => {
      this.result = data;
    }, error => {
      console.error('There was an error!', error);
    });
  }
}
