import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from '../config';
import { Observable } from 'rxjs';
import { Grid } from '../common/grid';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private baseURL = `${API_URL}/api`;
  private createNewGameURL = `${this.baseURL}/creategame`;
  private joinGameURL = `${this.baseURL}/joinGame`;
  private getGameBoardURL = `${this.baseURL}/gameDetails`;
  private submitMoveURL = `${this.baseURL}/submitMove`;

  constructor(private httpClient: HttpClient) { }

  createNewGame(): Observable<Number> {
    return this.httpClient.post<Number>(this.createNewGameURL,null).pipe(
    );
  }

  joinGame(game_id: string): Observable<Number> {
    let myParams = new HttpParams().set("gameId",game_id);
    return this.httpClient.post<Number>(this.joinGameURL,'',{params: myParams}).pipe(
    );
  }

  getGameDetails(game_id: string): Observable<any> {
    let myParams = new HttpParams().set("gameId",game_id); //Create new HttpParams
    return this.httpClient.get<any>(this.getGameBoardURL, {params: myParams}).pipe(
    );
  }

  submitmove(newMove: any): Observable<any> {
    return this.httpClient.post<any>(this.submitMoveURL,newMove).pipe(
    );
  }


}

