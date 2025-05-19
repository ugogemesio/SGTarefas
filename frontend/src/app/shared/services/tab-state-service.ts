import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TabStateService {
  private activeTabSubject = new BehaviorSubject<string>('usuarios');
  activeTab$ = this.activeTabSubject.asObservable();
  
  setActiveTab(tabId: string): void {
    this.activeTabSubject.next(tabId);
  }
}