import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { UserSecComponent } from '../../modules/users/user-sec/user-sec.component';
import { TaskSecComponent } from "../../modules/tasks/task-sec/task-sec.component";

@Component({
  standalone: true,
  imports: [CommonModule, UserSecComponent, TaskSecComponent], 
  selector: 'app-tab',
  templateUrl: './tab.component.html',
  styleUrls: ['./tab.component.css']
})
export class TabComponent {
  selectedTab: 'users' | 'tasks' = 'users';

  selectTab(tab: 'users' | 'tasks') {
    this.selectedTab = tab;
  }
}