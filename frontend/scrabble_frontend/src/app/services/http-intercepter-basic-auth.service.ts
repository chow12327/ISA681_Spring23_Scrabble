import { BasicAuthenticationService } from './basic-authentication.service';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HttpIntercepterBasicAuthService implements HttpInterceptor{

  constructor(
    private basicAuthenticationService : BasicAuthenticationService
  ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler){
    // let username = 'in28minutes'
    // let password = 'dummy'
    //let basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);
    let jwtHeaderString = this.basicAuthenticationService.getAuthenticatedToken();
    //let username = this.basicAuthenticationService.getAuthenticatedUser()

    if(jwtHeaderString) { 
      request = request.clone({
        setHeaders : {
            Authorization : jwtHeaderString
          }
        }) 
    }
    return next.handle(request);
  }

}
