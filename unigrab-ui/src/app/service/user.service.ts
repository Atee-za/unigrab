import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {Town} from "../model/town";
import {Campus} from "../model/campus";
import {PasswordUpdate} from "../model/passwordUpdate";
import {environment} from "../environments/environment";
import {Token} from "../model/token";

@Injectable({ providedIn: 'root' })
export class UserService {

  private readonly BASE_URL = environment.API_BASE_URL + 'api/v1/user'

  constructor(private http: HttpClient) {}

  getUserInfo(){
    return this.http.get<User>(`${this.BASE_URL}/info`);
  }

  updateUserAddressTown(town: Town){
    return this.http.post<Town>(`${this.BASE_URL}/town`, town);
  }

  updateUserAddressCampus(campus: Campus){
    return this.http.post<Campus>(`${this.BASE_URL}/campus`, campus);
  }

  updateUserPassword(passwordUpdate: PasswordUpdate){
    return this.http.post<boolean>(`${this.BASE_URL}/password`, passwordUpdate);
  }

  refreshToken() {
    return this.http.get<Token>(`${this.BASE_URL}/refresh`);
  }

}
