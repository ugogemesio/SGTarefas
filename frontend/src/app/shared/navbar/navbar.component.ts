import { Component, ViewEncapsulation  } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
    encapsulation: ViewEncapsulation.None  // <-- isto aqui,
      ,imports: [CommonModule],

})
export class NavbarComponent {
  isDarkMode = false;

  toggleTheme() {
    this.isDarkMode = !this.isDarkMode;
    document.body.classList.toggle('dark', this.isDarkMode);
  }
}