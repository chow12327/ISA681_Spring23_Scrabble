import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Route, Router } from '@angular/router';
import { Grid } from 'src/app/common/grid';
import { MoveWords } from 'src/app/common/move-words';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';
import { GameService } from 'src/app/services/game.service';
import { InitializeGridService } from 'src/app/services/initialize-grid.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit{

  gameGrid: Grid
  tagme: boolean = true

  moveError: boolean = false
  moveErrorMessage: string
  moveSuccess: boolean = false
  moveSuccessMessage: string
  turn:boolean

  player1: string
  player2: string
  score1: number
  score2: number

  letter1: string
  letter2: string
  letter3: string
  letter4: string
  letter5: string

  gameid: string

  gamemoves: [MoveWords]

  constructor( private route: ActivatedRoute,
    private router: Router,
    private initializeGridService: InitializeGridService,
    private gameService: GameService,
    private basicAuthenticationService: BasicAuthenticationService) {
    const token = localStorage.getItem('token'); }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => this.gameid = (params['gameId']));
    this.initializeGrid();
    this.getGameDetails();
  }


  getGameDetails()
  {
    this.gameService.getGameDetails(this.gameid).subscribe(
      data => {
        //console.log(data);
        this.gameid = data["id"]
        this.player1 = data["player1Username"];
        this.score1 = data["player1Score"];
        this.player2 = data["player2Username"];
        this.score2 = data["player2Score"];
        this.turn = !data["turn"];
        this.letter1 = data["l1"];
        this.letter2 = data["l2"];
        this.letter3 = data["l3"];
        this.letter4 = data["l4"];
        this.letter5 = data["l5"];
        this.gameGrid = data["gameGrid"]

        this.gamemoves = data["moves"]

      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
        else
        {
          this.moveError = true
          this.moveErrorMessage = error.error.message
        }
      }
    )
  }

  // initializeGrid() {
  //   //this.gameGrid = this.initializeGridService.initializeGrid()
  //   this.initializeGridService.initializeGrid().subscribe(
  //     data => {
  //       this.gameGrid = data;
  //     },
  //     error => {
  //       if (error.status === 401) {
  //        // this.basicAuthenticationService.logout();
  //        // this.router.navigate(['login']);
  //       }
  //     }
  //   )
  // }



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
        this.moveSuccess =  true;
        this.moveSuccessMessage = "Move submitted successfully!"
      },
      error => {

        if (error.status == 401 ||  error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
        if (error.status == 400 ){
          this.moveError = true
          this.moveErrorMessage = error.error.message;
        }
        if (error.status == 500 ){
          this.moveError = true
          this.moveErrorMessage = error.error.message;
        }
      }
    )

  }



  initializeGrid(){

    this.gameGrid
    = {
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

  }


}
