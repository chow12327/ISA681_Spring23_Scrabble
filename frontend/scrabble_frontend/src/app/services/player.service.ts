import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Player } from '../common/player';
import { API_URL } from '../config';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  
  private baseURL = `${API_URL}/api/players`;

  constructor(private httpClient: HttpClient) { }
  
  getPlayerList(): Observable<Player[]> {
    return this.httpClient.get<Player[]>(this.baseURL,{withCredentials: true}).pipe(
    //  map(response => response.players)
    );
  }
}