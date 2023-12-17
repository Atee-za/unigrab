import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Token} from "../../model/token";
import {environment} from "../../environments/environment";
import {RefreshTokenService} from "./refresh.token.service";
import {Constants} from "../../model/constants";

@Injectable({providedIn: 'root' })
export class AuthService {

  private readonly AUTH_URL = environment.API_BASE_URL + 'api/v1/auth'

  constructor(private http: HttpClient, private refreshTokenService: RefreshTokenService) {}

  login(email: string, password: string){
     const user = { email: email, password: password};
     return this.http.post<Token>(`${this.AUTH_URL}/authenticate`, JSON.stringify(user), {
       headers: new HttpHeaders()
         .set('Content-Type', 'application/json; charset=utf-8')
         .set('Accept', 'application/json')
     });
   }

  register(email: string, password: string, confirmPassword: string, isStudent: boolean){
     const user = { email: email, password: password, confirmPassword: confirmPassword, isStudent: isStudent};
     return this.http.post<Token>(`${this.AUTH_URL}/register`, JSON.stringify(user), {
       headers: new HttpHeaders()
         .set('Content-Type', 'application/json; charset=utf-8')
         .set('Accept', 'application/json')
     });
   }

  public setToken(token: string){
     sessionStorage.setItem(Constants.JWT_TOKEN, token);
     this.refreshTokenService.startExpiryTimeCounter();
  }

  public logout(){
    this.removeToken();
  }

  private removeToken(){
     if(this.isAuthenticated()) {
       sessionStorage.removeItem(Constants.JWT_TOKEN);
       this.refreshTokenService.cancelRefreshToken();
     }
  }

  public isAuthenticated(){
     let token = sessionStorage.getItem(Constants.JWT_TOKEN);
     return token !== undefined && token !== null && token.length > 0;
  }

}
