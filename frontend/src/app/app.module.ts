import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';
import { UserFormComponent } from './modules/users/user-form/user-form.component';

@NgModule({
  declarations: [
    
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    UserFormComponent 
  ],
  bootstrap: []
})
export class AppModule { }
