import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'client-frontend';

  navLinks = [
      {label: 'Search connection', path: '/connection'},
      {label: 'Buy tickets', path: '/buy'},
      {label: 'My tickets', path: '/tickets'}
    ];

  constructor(private router: Router) {
    router.navigate(['connection'])
  }

}
