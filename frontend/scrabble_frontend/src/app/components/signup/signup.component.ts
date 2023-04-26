import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';
import { SignupService } from 'src/app/services/signup.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})



export class SignupComponent implements OnInit{
  username = ''
  password = ''
  firstname = ''
  lastname = ''
  emailid = ''

  errorMessage = 'Invalid data provided. Please try again!'
  successMessage = 'Account created! Click on login to play scrabble!'
  

  invalidAccount = false
  accountCreated = false

  constructor(private router: Router, 
    private signupService: SignupService,
    private basicAuthenticationService: BasicAuthenticationService) {
  }

  ngOnInit(): void {
    this.basicAuthenticationService.logout();
  }

  createAccount(){
    this.signupService.createPlayerAccount(this.username, this.password,this.firstname,this.lastname,this.emailid)
    .subscribe(
      data => {
        console.log(data)
        this.router.navigate(['login'])
        this.invalidAccount = false
        this.accountCreated = true
      },
      error => {
        console.log(error)
        this.invalidAccount = true
        this.accountCreated = false
      }
    )
  }
  
  
}
