import { Component } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'my-app';
  constructor(private router : Router) { 
    
  }
  isValid(): boolean {
    if ((this.router.url != '/') && (this.router.url != '/login') && (this.router.url != '/reset-password/:email_for/:token_for')) {
              return true;
      }
    return false;
  }
}