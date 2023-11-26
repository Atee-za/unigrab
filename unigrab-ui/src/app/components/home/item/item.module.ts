import { NgModule } from '@angular/core';
import {CommonModule} from "@angular/common";
import {ItemComponent} from "./item.component";
import {ItemRoutingModule} from "./item-routing.module";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  declarations: [
    ItemComponent
  ],
  imports: [
    CommonModule,
    ItemRoutingModule,
    MatCardModule,
    MatButtonModule
  ]
})
export class ItemModule { }
