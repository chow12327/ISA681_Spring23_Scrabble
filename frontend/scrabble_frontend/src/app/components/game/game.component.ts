import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Route, Router } from '@angular/router';
import { Grid } from 'src/app/common/grid';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';
import { GameService } from 'src/app/services/game.service';
import { InitializeGridService } from 'src/app/services/initialize-grid.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit{

  gameGrid: Grid;
  tagme: boolean = true

  moveError: boolean = false
  moveErrorMessage: string = "hello. There is an error on this page at this screen at this move blah blah blah"

  player1: string = "user1"
  player2: string = "user2"
  score1: number = 10
  score2: number = 20

  letter1: string= "A"
  letter2: string= "B"
  letter3: string= "C"
  letter4: string= "D"
  letter5: string= "E"
  
  gameid: string;

  constructor( private route: ActivatedRoute,
    private router: Router,
    private initializeGridService: InitializeGridService,
    private gameService: GameService,
    private basicAuthenticationService: BasicAuthenticationService) {
    const token = localStorage.getItem('token'); }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.gameid = (params['gameId']));
    this.getGameDetails();
    this.initializeGrid()
  }


  getGameDetails()
  {
    this.gameService.getGameDetails(this.gameid).subscribe(
      data => {
        console.log(data);
        this.player1 = data["player1Username"];
        this.score1 = data["player1Score"];
        this.player2 = data["player2Username"];
        this.score2 = data["player2Score"];
      },
      error => {
        if (error.status === 401) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
      }
    )
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

  submitMove(){

  this.moveError = false
  this.moveErrorMessage = ""

    var newMove = 
    {
        "gameId": this.gameid,
        "gameGrid": this.gameGrid
    };
    
    this.gameService.submitmove(newMove).subscribe(
      data => {
        // game_id = data;
        // if(game_id != 0){
        //   this.router.navigate(['game',game_id])
        // }
      },
      error => {
        this.moveError = true
        
        if (error.status == 401) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
        if (error.status == 400 ){
          //console.log("Invalid Move");
          this.moveErrorMessage = error.error.message;
        }
        if (error.status == 500 ){
          //console.log("Invalid Move");
          this.moveErrorMessage = error.error.message;
        }
      }
    )

  }

  
}
