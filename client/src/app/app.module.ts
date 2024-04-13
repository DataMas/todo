import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import { TodosComponent } from './todos/todos.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { StatsComponent } from './stats/stats.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
// import {ToastrModule} from "ngx-toastr";

@NgModule({
  declarations: [
    AppComponent,
    TodosComponent,
    StatsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    // ToastrModule.forRoot({
    //   timeOut: 10000,
    //   positionClass: 'toast-bottom-right',
    //   preventDuplicates: true,
    // }), // ToastrModule added
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
