import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {Constants} from "../../model/constants";

@Injectable({ providedIn: 'root' })
export class AuthenticationGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if(state.url === '/home' || state.url.startsWith('/item?id=' ) || state.url === '/login'){
      return true;
    }

    let token = sessionStorage.getItem(Constants.JWT_TOKEN);

    if (!token){
      return this.router.parseUrl('/login');
    }

    return true;
  }

}
