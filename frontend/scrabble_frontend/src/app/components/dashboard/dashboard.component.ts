import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Player } from 'src/app/common/player';
import { PlayerService } from 'src/app/services/player.service';
import { Histgame } from 'src/app/common/histgame';
import { HistgamesService } from 'src/app/services/histgames.service';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit{

players: Player[] = [];
histgames: Histgame[] = [];
activegames: Histgame[] = [];

constructor(private playerService: PlayerService, 
  private histgamesService: HistgamesService,
  private router: Router,
  private basicAuthenticationService: BasicAuthenticationService) {
  const token = localStorage.getItem('token'); }

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
      if (error.status === 401) {
        this.basicAuthenticationService.logout();
        this.router.navigate(['login']);
      }
    }
  )
}

listActiveGames(){
  this.histgamesService.getActiveGameslist().subscribe(
    data => {
      this.activegames = data;
    },
    error => {
      if (error.status == 401){
        this.basicAuthenticationService.logout();
        this.router.navigate(['login']);
      }
    }
  )
}

listHistoricGames(){
  this.histgamesService.getHistGameslist().subscribe(
    data => {
      this.histgames = data;
    },
    error => {
      if (error.status == 401){
        this.basicAuthenticationService.logout();
        this.router.navigate(['login']);
      }
    }
  )
}

  startNewGame() {
    this.router.navigate(['game'])
  }

}