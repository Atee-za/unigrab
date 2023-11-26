import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Item} from "../../../model/item";
import {ItemService} from "../../../service/item.service";
import {SharedService} from "../../../service/shared.service";
import {SnackbarService} from "../../../service/snackbar.service";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../../model/unsubscribe";

@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css']
})
export class ItemComponent extends Unsubscribe implements OnInit {

  item: Item;
  isLoading: boolean = false;

  constructor(private router: ActivatedRoute,
              private itemService: ItemService,
              private sharedService: SharedService,
              private snackbarService: SnackbarService) {
    super();
  }

  ngOnInit(): void {
    this.loadItem();
  }

  getImage(imageUrl: string){
    return this.itemService.getImage(imageUrl);
  }

  private loadItem(){
    this.router.queryParams
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe(params => {
      if(params['id']) {
        this.isLoading = true;
        this.item = this.sharedService.getSharedItem();
        if(!this.item){
          this.itemService.getItem(params['id'])
            .pipe(takeUntil(this.unsubscribe$))
            .subscribe(item => {
                this.item = item;
                this.isLoading = false;
              },
              (error => {
                this.isLoading = false;
                this.sharedService.handleError(error);
              }));
        } else {
          this.isLoading = false;
        }
      } else {
        this.isLoading = false;
        this.snackbarService.openErrorSnackBar('ItemId not provided');
      }
    });
  }

}
