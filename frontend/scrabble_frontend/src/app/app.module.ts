import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GameListComponent } from './components/game-list/game-list.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http'
import {GameService} from './services/game.service';
import { PlayerListComponent } from './components/player-list/player-list.component'
import { PlayerService } from './services/player.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { HttpIntercepterBasicAuthService } from './services/http-intercepter-basic-auth.service';
import { LogoutComponent } from './components/logout/logout.component';
import { SignupComponent } from './components/signup/signup.component';

@NgModule({
  declarations: [
    AppComponent,
    GameListComponent,
    PlayerListComponent,
    DashboardComponent,
    LoginComponent,
    LogoutComponent,
    SignupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [GameService,PlayerService, {provide: HTTP_INTERCEPTORS, useClass: HttpIntercepterBasicAuthService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
