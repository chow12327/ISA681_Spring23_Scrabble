import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Player } from 'src/app/common/player';
import { PlayerService } from 'src/app/services/player.service';
import { BasicAuthenticationService } from 'src/app/services/basic-authentication.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})

export class DashboardComponent implements OnInit{

players: Player[] = [];

constructor(private playerService: PlayerService, 
  private router: Router,
  private basicAuthenticationService: BasicAuthenticationService) {
  const token = localStorage.getItem('token'); }

  ngOnInit(): void {
    this.listPlayers()
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
  )}

  startNewGame() {
    this.router.navigate(['game'])
  }

}