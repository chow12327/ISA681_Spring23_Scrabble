import { Injectable } from '@angular/core';
import { API_URL } from '../config';
import { HttpClient } from '@angular/common/http';
import { Grid } from '../common/grid';
import { Observable, of } from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class InitializeGridService {

  private baseURL = `${API_URL}/api/players`;
  gameGrid: Grid = { 
    isg1Disabled : false,
    isg2Disabled : false,
    isg3Disabled : false,
    isg4Disabled : false,
    isg5Disabled : false,
    isg6Disabled : false,
    isg7Disabled : false,
    isg8Disabled : false,
    isg9Disabled : false,
    isg10Disabled : false,
    isg11Disabled : false,
    isg12Disabled : false,
    isg13Disabled : false,
    isg14Disabled : false,
    isg15Disabled : false,
    isg16Disabled : false,
    isg17Disabled : false,
    isg18Disabled : false,
    isg19Disabled : false,
    isg20Disabled : false,
    isg21Disabled : false,
    isg22Disabled : false,
    isg23Disabled : false,
    isg24Disabled : false,
    isg25Disabled : false,
    isg26Disabled : false,
    isg27Disabled : false,
    isg28Disabled : false,
    isg29Disabled : false,
    isg30Disabled : false,
    isg31Disabled : false,
    isg32Disabled : false,
    isg33Disabled : false,
    isg34Disabled : false,
    isg35Disabled : false,
    isg36Disabled : false,
    g1 : "",
    g2 : "",
    g3 : "",
    g4 : "",
    g5 : "",
    g6 : "",
    g7 : "",
    g8 : "",
    g9 : "",
    g10 : "",
    g11 : "",
    g12 : "",
    g13 : "",
    g14 : "",
    g15 : "",
    g16 : "",
    g17 : "",
    g18 : "",
    g19 : "",
    g20 : "",
    g21 : "",
    g22 : "",
    g23 : "",
    g24 : "",
    g25 : "",
    g26 : "",
    g27 : "",
    g28 : "",
    g29 : "",
    g30 : "",
    g31 : "",
    g32 : "",
    g33 : "",
    g34 : "",
    g35 : "",
    g36 : ""}

  constructor(private httpClient: HttpClient ) { }
  
  initializeGrid(): Observable<Grid> {
    
   

    //  return this.httpClient.get<GetResponse>(this.baseURL).pipe(
    //    map(response => response.this.players)
    //  );


    return of(this.gameGrid);
  }

}

interface GetResponse {
  _embedded: {
    gameGrid: Grid;
  }
}
