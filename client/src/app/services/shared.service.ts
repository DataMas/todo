import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SharedService {
    // Subject for sending notifications
    private refreshNeeded$ = new Subject<void>();

    // Observable that other components can subscribe to
    get refreshNeeded() {
        return this.refreshNeeded$.asObservable();
    }

    // Method to call to trigger a refresh
    triggerRefresh() {
        this.refreshNeeded$.next();
    }
}
