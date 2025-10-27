import { Component } from '@angular/core';
import { Navbar } from '../navbar/navbar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-registration',
  imports: [Navbar, CommonModule],
  templateUrl: './registration.html',
  styleUrl: './registration.css'
})
export class Registration {

}
