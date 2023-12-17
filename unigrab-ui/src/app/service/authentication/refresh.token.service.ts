import {Injectable} from "@angular/core";
import {Token} from "../../model/token";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../model/unsubscribe";
import {UserService} from "../user.service";
import {Constants} from "../../model/constants";

@Injectable({providedIn: 'root' })
export class RefreshTokenService extends Unsubscribe {

  private lastUserActivity: Date;                           //  Logs last user interaction time with the system
  private isUserStillActive: boolean;                       //  Used to cancel refresh token in case user logs out
  private readonly INACTIVE_MINUTES = 2;                //  Token will not be refreshed if inactive for more than 2 minutes before refresh cycle
  private readonly EXTRA_MILLISECONDS = 1410000;  //  1410000 = 23.5 minutes; backend token expires in 24 minutes

  constructor(private userService: UserService) {
    super();
  }

  public startExpiryTimeCounter() {
    this.isUserStillActive = true;
    this.setTokenTimeout();
  }

  public handleUserActivity(){
    this.lastUserActivity = new Date;
  }

  public cancelRefreshToken() {
    this.isUserStillActive = false;
  }

  private setTokenTimeout(){
    setTimeout(() => this.refreshToken(), this.EXTRA_MILLISECONDS);
  }

  private refreshToken() {
    if (this.lastUserActivity >= this.currentTime() && this.isUserStillActive) {
      this.userService.refreshToken()
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe(
          (token: Token) => this.setToken(token.token)
        );
    }
  }

  private currentTime() {
    let currentTime = new Date();
    currentTime.setMinutes(currentTime.getMinutes() - this.INACTIVE_MINUTES);
    return currentTime;
  }

  private setToken(token: string){
    sessionStorage.setItem(Constants.JWT_TOKEN, token);
    this.startExpiryTimeCounter();
  }

}
