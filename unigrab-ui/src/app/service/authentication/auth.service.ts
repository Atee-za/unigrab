import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Token} from "../../model/token";
import {environment} from "../../environments/environment";

@Injectable({providedIn: 'root' })
export class AuthService {

  authUrl = environment.API_BASE_URL + 'api/v1/auth'

  constructor(private http: HttpClient) {}

  login(email: string, password: string){
     const user = { email: email, password: password};
     return this.http.post<Token>(`${this.authUrl}/authenticate`, JSON.stringify(user), {
       headers: new HttpHeaders()
         .set('Content-Type', 'application/json; charset=utf-8')
         .set('Accept', 'application/json')
     });
   }

  register(email: string, password: string, confirmPassword: string, isStudent: boolean){
     const user = { email: email, password: password, confirmPassword: confirmPassword, isStudent: isStudent};
     return this.http.post<Token>(`${this.authUrl}/register`, JSON.stringify(user), {
       headers: new HttpHeaders()
         .set('Content-Type', 'application/json; charset=utf-8')
         .set('Accept', 'application/json')
     });
   }

  public setToken(token: string){
     sessionStorage.setItem('token', token);
     return sessionStorage.getItem('token');
  }

  public logout(){
    this.removeToken();
  }

  private removeToken(){
     if(this.isAuthenticated()){
       sessionStorage.removeItem('token');
     }
  }

  public isAuthenticated(){
     let token = sessionStorage.getItem('token');
     return token !== undefined && token !== null && token.length > 0;
  }

}
