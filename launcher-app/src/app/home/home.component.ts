import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  goToOneBank(): void {
    window.location.href = 'http://localhost:4001';
  }

  goToCityBank(): void {
    window.location.href = 'http://localhost:4002';
  }
}
