import { Injectable } from '@angular/core';
import { API_URL } from './../config';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import {map} from 'rxjs/operators';

export const TOKEN = 'token'
export const AUTHENTICATED_USER = 'authenticaterUser'

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {
  constructor(private http: HttpClient) { }

  executeJWTAuthenticationService(username: string, password: string) {
    
    return this.http.post<any>(
      `${API_URL}/authenticate`,{
        username,
        password
      }).pipe(
        map(
          data => {
            sessionStorage.setItem(AUTHENTICATED_USER, username);
            sessionStorage.setItem(TOKEN, `Bearer ${data.token}`);
            return data;
          }
        )
      );
    //console.log("Execute Hello World Bean Service")
  }

  getAuthenticatedUser() {
    return sessionStorage.getItem(AUTHENTICATED_USER)
  }

  getAuthenticatedToken() {

    if(this.getAuthenticatedUser())
      return sessionStorage.getItem(TOKEN)
    else
      return null;
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(AUTHENTICATED_USER)
    return !(user === null)
  }

  logout(){
    sessionStorage.removeItem(AUTHENTICATED_USER)
    sessionStorage.removeItem(TOKEN)
  }

  getCSRFToken() {
    
    return this.http.get<any>(
      `${API_URL}/csrf`,{withCredentials: true
      }).pipe(
        map(
          data => {
            return data;
          }
        )
      );
    //console.log("Execute Hello World Bean Service")
  }


}

export class AuthenticationBean{
  constructor(public message:string) { }
}