import { Component } from '@angular/core';
import { Player } from 'src/app/common/player';
import { PlayerService } from 'src/app/services/player.service';

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.css']
})
export class PlayerListComponent {
  players: Player[] = [];
  constructor(private playerService: PlayerService){}

  ngOnInit(): void{
    this.listPlayers()
  }

  listPlayers() {
    this.playerService.getPlayerList().subscribe(
      data => {
        this.players = data;
      }
    )
  }
}
