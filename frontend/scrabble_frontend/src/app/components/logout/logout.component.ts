import { Component, OnInit } from '@angular/core';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {
  constructor(
    private router: Router,
    private basicAuthenticationService: BasicAuthenticationService
  ) { }

  ngOnInit() {
    this.basicAuthenticationService.logout();
  }
}
