import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Player } from 'src/app/common/player';
import { PlayerService } from 'src/app/services/player.service';
import { APP_NAME } from './../../config';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{

players: Player[] = [];

constructor(private configService: PlayerService, private router: Router) {
  const token = localStorage.getItem('token');
}
ngOnInit(): void {

  this.configService.getPlayerList().subscribe(
    data => {
      this.players = data;
    },
    (err: any) => {
      if (err.status === 403) {
        localStorage.removeItem('token');
        this.router.navigate(['login']);
      }
    }
  );
}

logout() {
  localStorage.removeItem('token');
  window.location.href = `https://${APP_NAME}?action=logout`;
}
}