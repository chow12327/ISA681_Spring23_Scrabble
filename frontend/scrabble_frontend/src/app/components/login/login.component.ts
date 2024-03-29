import { BasicAuthenticationService } from '../../services/basic-authentication.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  username = ''
  password = ''
  errorMessage = 'Invalid Credentials'
  invalidLogin = false


  constructor(private router: Router,
    private basicAuthenticationService: BasicAuthenticationService) {
  }

  ngOnInit(): void {
    this.basicAuthenticationService.logout()
    this.getCSRFToken();
    
  }

  handleJWTAuthLogin() {
    this.basicAuthenticationService.executeJWTAuthenticationService(this.username, this.password)
        .subscribe(
          data => {
            console.log(data)
            this.router.navigate(['dashboard'])
            this.invalidLogin = false
          },
          error => {
            console.log(error)
            this.invalidLogin = true
          }
        )
  }

  navigatetoSignUp() {
    this.router.navigate(['signup'])
  }

  getCSRFToken() {
    this.basicAuthenticationService.getCSRFToken()
    .subscribe(
      data => {
        console.log(data)
      
      },
      error => {
        console.log(error)
     
      }
    )
  }

}
