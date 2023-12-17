import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Item} from "../../model/item";
import {ItemService} from "../../service/item.service";
import {HttpErrorResponse} from "@angular/common/http";
import {SharedService} from "../../service/shared.service";
import {AuthService} from "../../service/authentication/auth.service";
import {PageEvent} from "@angular/material/paginator";
import {ToPage} from "../../model/toPage";
import {SearchData} from "../../model/searchData";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../model/unsubscribe";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends Unsubscribe implements OnInit {

  items: Item[] = [];
  pageItems: ToPage<Item>;
  searchData: SearchData;

  loading: boolean = false;

  offset: number = 0;
  pageSize: number = this.sharedService.getPageSize();
  pageSizeOptions = [3, 5, 10, 20, 30];

  constructor(private router: Router,
              private itemService: ItemService,
              private authService: AuthService,
              private sharedService: SharedService) {
    super();

    this.sharedService.searchParams$.pipe(takeUntil(this.unsubscribe$)).subscribe((params) => {
      this.searchData = params;
      this.offset = 0;
      this.loadItems();
    });

  }

  ngOnInit() {
    this.loadItems();
  }

  getImage(imageUrl: string){
    return this.itemService.getImage(imageUrl);
  }

  viewItem(item: Item){
    this.sharedService.setSharedItem(item);
    this.router.navigate([`item`], { queryParams: { id: item.id} });
  }

  onPaginateChange(event: PageEvent){
    this.offset = event.pageIndex;
    this.pageSize = event.pageSize;
    this.sharedService.setPageSize(this.pageSize);
    this.loadItems();
  }

  private loadItems(){
    this.loading = true;
    this.itemService.loadItems(this.offset, this.pageSize, this.searchData)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
          this.items = res.content;
          this.pageItems = res;
          this.loading = false;
        },
        (error: HttpErrorResponse) => {
          this.loading = false;
          this.sharedService.handleError(error);
          this.authService.logout();
    });
  }

}
