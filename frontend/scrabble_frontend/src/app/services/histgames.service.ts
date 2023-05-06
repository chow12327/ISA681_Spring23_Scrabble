import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Histgame } from '../common/histgame';
import {map} from 'rxjs/operators';
import { API_URL } from '../config';

@Injectable({
  providedIn: 'root'
})
export class HistgamesService {

  private baseURL = `${API_URL}/api/historicgames`;

  private baseURL2 = `${API_URL}/api/activegames`;

  constructor(private httpClient: HttpClient) {   }

  getActiveGameslist(): Observable<Histgame[]> {
    return this.httpClient.get<Histgame[]>(this.baseURL2).pipe(
    
    );
  } 

  getHistGameslist(): Observable<Histgame[]> {
    return this.httpClient.get<Histgame[]>(this.baseURL).pipe(
    //  map(response => response.players)
    );
  }
 }
