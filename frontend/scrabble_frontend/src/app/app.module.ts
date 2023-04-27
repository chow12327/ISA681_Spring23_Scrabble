import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GameListComponent } from './components/game-list/game-list.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http'
import {GameService} from './services/game.service';
import { PlayerService } from './services/player.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { HttpIntercepterBasicAuthService } from './services/http-intercepter-basic-auth.service';
import { LogoutComponent } from './components/logout/logout.component';
import { SignupComponent } from './components/signup/signup.component';
import { GameComponent } from './components/game/game.component';
import { InitializeGridService } from './services/initialize-grid.service';

@NgModule({
  declarations: [
    AppComponent,
    GameListComponent,
    DashboardComponent,
    LoginComponent,
    LogoutComponent,
    SignupComponent,
    GameComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [GameService,PlayerService, InitializeGridService,{provide: HTTP_INTERCEPTORS, useClass: HttpIntercepterBasicAuthService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
