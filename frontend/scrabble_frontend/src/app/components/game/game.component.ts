import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Grid } from 'src/app/common/grid';
import { InitializeGridService } from 'src/app/services/initialize-grid.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit{

  gameGrid: Grid;
  tagme: boolean = true

  constructor( private router: Router,
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
  
}
