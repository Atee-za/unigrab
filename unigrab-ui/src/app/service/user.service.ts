import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {Town} from "../model/town";
import {Campus} from "../model/campus";
import {PasswordUpdate} from "../model/passwordUpdate";
import {environment} from "../environments/environment";

@Injectable({ providedIn: 'root' })
export class UserService {

  private readonly baseUrl = environment.API_BASE_URL + 'api/v1/user'

  constructor(private http: HttpClient) {}

  getUserInfo(){
    return this.http.get<User>(`${this.baseUrl}/info`);
  }

  updateUserAddressTown(town: Town){
    return this.http.post<Town>(`${this.baseUrl}/town`, town);
  }

  updateUserAddressCampus(campus: Campus){
    return this.http.post<Campus>(`${this.baseUrl}/campus`, campus);
  }

  updateUserPassword(passwordUpdate: PasswordUpdate){
    return this.http.post<boolean>(`${this.baseUrl}/password`, passwordUpdate);
  }

}
