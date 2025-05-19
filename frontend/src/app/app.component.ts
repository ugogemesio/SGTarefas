import { Component } from '@angular/core';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { TabComponent } from './shared/tab/tab.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NavbarComponent, TabComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
