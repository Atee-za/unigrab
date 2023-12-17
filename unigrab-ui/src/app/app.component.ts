import {Component, HostListener} from '@angular/core';
import {RefreshTokenService} from "./service/authentication/refresh.token.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private refreshTokenService: RefreshTokenService) {}

  @HostListener('document:click', ['$event'])
  handleUserClick(event: MouseEvent): void {
    this.refreshTokenService.handleUserActivity();
  }

  @HostListener('document:keydown', ['$event'])
  handleUserKeyPress(event: KeyboardEvent): void {
    this.refreshTokenService.handleUserActivity();
  }

}
