import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Player } from 'src/app/common/player';
import { PlayerService } from 'src/app/services/player.service';
import { Histgame } from 'src/app/common/histgame';
import { HistgamesService } from 'src/app/services/histgames.service';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';
import { GameService } from 'src/app/services/game.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit {

  game = { 
    gmid: ''
  };

  players: Player[] = [];
  histgames: Histgame[] = [];
  activegames: Histgame[] = [];

  moveError: boolean = false
  moveErrorMessage: string


  constructor(private playerService: PlayerService,
    private histgamesService: HistgamesService,
    private gameService: GameService,
    private router: Router,
    private basicAuthenticationService: BasicAuthenticationService) {
    const token = localStorage.getItem('token');
  }

  ngOnInit(): void {
    this.listPlayers()
    this.listHistoricGames()
    this.listActiveGames()
  }


  listPlayers() {
    this.playerService.getPlayerList().subscribe(
      data => {
        this.players = data;
      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
      }
    )
  }

  listActiveGames() {
    this.histgamesService.getActiveGameslist().subscribe(
      data => {
        this.activegames = data;
      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
      }
    )
  }

  listHistoricGames() {
    this.histgamesService.getHistGameslist().subscribe(
      data => {
        this.histgames = data;
      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
      }
    )
  }

  startNewGame() {
    let game_id : Number;
    this.gameService.createNewGame().subscribe(
      data => {
        game_id = data;
        if(game_id != 0){
          this.router.navigate(['game',game_id])
        }
      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
      }
    )
  }

  JoinGame(gmid: number) {
    this.gameService.joinGame(gmid.toString()).subscribe(
      data => {
          this.router.navigate(['game',gmid])
      },
      error => {
        if (error.status == 401 || error.status == 0) {
          this.basicAuthenticationService.logout();
          this.router.navigate(['login']);
        }
        if(error.status == 400){
          this.moveError = true
          this.moveErrorMessage = error.error.message;
        }
        if(error.status == 500){
          this.moveError = true
          this.moveErrorMessage = error.error.message;
        }
      }
    )

  }

  ViewGame(gmid: number) {
    this.router.navigate(['game',gmid])
  }

  EnterGame(){
    let gmidstr: any;
    if(this.game.gmid!=null){
      gmidstr = this.game.gmid+"";
    }
    this.JoinGame(gmidstr);
  }

  lgout(){
    this.basicAuthenticationService.logout()
    alert("you have successfully logged out")
    this.router.navigate(['login'])
  }

}