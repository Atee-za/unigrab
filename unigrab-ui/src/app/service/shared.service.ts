import {Injectable} from "@angular/core";
import {Item} from "../model/item";
import {HttpErrorResponse} from "@angular/common/http";
import {SnackbarService} from "./snackbar.service";
import {AuthService} from "./authentication/auth.service";
import {Router} from "@angular/router";
import {SearchData} from "../model/searchData";
import {Subject} from "rxjs";

@Injectable({ providedIn: 'root' })
export class SharedService {

  private sharedItem: Item;
  private searchParams  = new Subject<SearchData>();

  constructor(private router: Router,
              private authService: AuthService,
              private snackbar: SnackbarService) {}

  searchParams$ = this.searchParams.asObservable();

  setSharedItem(item: Item){
    this.sharedItem = item;
  }

  getSharedItem(): Item {
    return this.sharedItem;
  }

  updateSearchParams(param: SearchData) {
    this.searchParams.next(param);
  }

  handleError(error: HttpErrorResponse){
    switch ((<HttpErrorResponse>error).status) {
      case 400:
      case 401:
      case 402:
      case 404:
        this.snackbar.openErrorSnackBar(error.error.message);
        break;
      case 403:
        this.authService.logout();
        this.router.navigate(['home'])
        this.snackbar.openErrorSnackBar("Session expired, sign in again");
        break;
      case 500:
        this.snackbar.openErrorSnackBar("Server error");
        break;
      default:
        this.snackbar.openErrorSnackBar('unigrab services is unavailable');
    }
  }

}
