import { Injectable } from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import { Observable } from 'rxjs';
import {Constants} from "../../model/constants";


@Injectable()
export class RequestInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let token = sessionStorage.getItem(Constants.JWT_TOKEN);
    if(token){
      request = request.clone({headers: request.headers.append('Authorization', `Bearer ${token}`)});
    }
    return next.handle(request);
  }
}

