import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Grid } from 'src/app/common/grid';
import { InitializeGridService } from 'src/app/services/initialize-grid.service';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit{

  gameGrid: Grid;
  tagme: boolean = true
  gameid: string = history.state.data;
  player1: string = "user1"
  player2: string = "user2"
  score1: number = 10
  score2: number = 20

  letter1: string= "A"
  letter2: string= "B"
  letter3: string= "C"
  letter4: string= "D"
  letter5: string= "E"
  

  constructor( private router: Router,
    private basicAuthenticationService: BasicAuthenticationService,
    private initializeGridService: InitializeGridService) {
    const token = localStorage.getItem('token'); }

  ngOnInit(): void {
    this.initializeGrid()
  }

  initializeGrid() {
    //this.gameGrid = this.initializeGridService.initializeGrid()
    this.initializeGridService.initializeGrid().subscribe(
      data => {
        this.gameGrid = data;
      },
      error => {
        if (error.status === 401) {
         // this.basicAuthenticationService.logout();
         // this.router.navigate(['login']);
        }
      }
    )
  }
  lgout(){
    this.basicAuthenticationService.logout()
    alert("you have successfully logged out")
    this.router.navigate(['login'])
  }
  
}
