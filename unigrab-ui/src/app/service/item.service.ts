import {Injectable} from '@angular/core';
import {Item} from "../model/item";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ToPage} from "../model/toPage";
import {SearchData} from "../model/searchData";
import {environment} from "../environments/environment";

@Injectable({ providedIn: 'root' })
export class ItemService {

  item: Item;
  items: Item[] = [];

  private readonly baseUrl = environment.API_BASE_URL + 'api/v1';

  constructor(private http: HttpClient) { }

  loadItems(offset: number, pageSize: number, params: SearchData) {
    return this.http.get<ToPage<Item>>(`${this.baseUrl}/auth/items/${offset}/${pageSize}`,
      {params: this.getParams(params)});
  }

  getImage(url: string){ return`${this.baseUrl}/auth/image/${url}`; }

  getItem(itemId: string){
    return this.http.get<Item>(`${this.baseUrl}/auth/item/${itemId}`);
  }

  saveNewItem(formData: FormData){
    return this.http.post<Item>(`${this.baseUrl}/item/save`, formData);
  }

  deleteItemById(itemId: string){
    return this.http.get<boolean>(`${this.baseUrl}/item/delete/${itemId}`);
  }

  updateItem(item: Item){
    return this.http.post<Item>(`${this.baseUrl}/item/update`, item);
  }

  private getParams(data: SearchData){
    let params = new HttpParams();
    if(data == null){
      return params;
    }
    params = params.set('name', data.name);
    params = params.set('price', data.price ? data.price : '');
    params = params.set('location', data.location);
    data.type.forEach(type => {
      params = params.append('type', type);
    });

    return params;
  }

}
