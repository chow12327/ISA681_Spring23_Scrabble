import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Game } from '../common/game';
import {map} from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class GameService {

  private baseURL = "https://localhost/api/games";

  constructor(private httpClient: HttpClient) { }

  getGameList(): Observable<Game[]> {
    return this.httpClient.get<GetResponse>(this.baseURL).pipe(
      map(response => response._embedded.games)
    );
  }
}

interface GetResponse {
  _embedded: {
    games: Game[];
  }
}
