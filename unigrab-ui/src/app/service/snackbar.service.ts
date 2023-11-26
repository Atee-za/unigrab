import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({ providedIn: 'root' })
export class SnackbarService {

  constructor(private snackBar: MatSnackBar) {}

  openSnackBar(message: string, action?: string) {
    if(action === undefined){ action = ''; }
    this.snackBar.open(message, action, {duration: 5 * 1000});
  }

  openErrorSnackBar(message: string) {
    this.snackBar.open(message, '', {duration: 5 * 1000, panelClass: ['warning']});
  }

}

