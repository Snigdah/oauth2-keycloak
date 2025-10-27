import { Component } from '@angular/core';
import { Navbar } from '../navbar/navbar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-about',
  imports: [Navbar, CommonModule],
  templateUrl: './about.html',
  styleUrl: './about.css'
})
export class About {

}
