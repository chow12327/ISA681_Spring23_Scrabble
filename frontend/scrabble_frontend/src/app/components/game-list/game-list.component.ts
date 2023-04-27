import { Component } from '@angular/core';
import { GameService } from 'src/app/services/game.service';

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.css']
})
export class GameListComponent {

constructor(private gameService: GameService){}

ngOnInit(): void{
 // this.listGames()
}

// listGames() {
//     this.gameService.getGameList().subscribe(
//       data => {
//         //this.games = data;
//       }
//     )
//   }

}
