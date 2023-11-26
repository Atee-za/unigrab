import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {SharedService} from "../../../service/shared.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{

  searchData: FormGroup;
  loading: boolean = false;
  itemTypes = ['Book', 'Camera', 'Phone', 'Laptop'];
  selectedTypes: string[] = [];

  constructor(private formBuilder: FormBuilder,
              private sharedService: SharedService) {}

  ngOnInit(): void {
    this.intForms();
  }

  search(){
    this.loading = true;
    this.searchData.value.type = this.selectedTypes;
    if(this.isValidSearch()){
      this.sharedService.updateSearchParams(this.searchData.value);
    } else {
      alert('Search boo-boo');
    }
  }

  clear(){
    this.intForms();
  }

  private isValidSearch(){
    return true;
  }

  addItemType(event: any, value: string){
    event.checked ? this.selectedTypes.push(value) :
      this.selectedTypes.splice(this.selectedTypes.indexOf(value), 1);
  }

  private intForms(){
    this.searchData = this.formBuilder.group({
      name: [''],
      price: [],
      location: [''],
      type: [[]],
    })
  }

}
