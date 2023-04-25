import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../config';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  constructor(private http: HttpClient) { }

  createPlayerAccount(username: string, password: string,firstname:string,lastname: string, emailid: string){
    return this.http.post<any>(
      `${API_URL}/createAccount`,{
        username,
        password,
        firstname,
        lastname,
        emailid
      }).pipe(
       
      );

  }
}
