import { NgModule } from '@angular/core';
import {MatCardModule} from "@angular/material/card";
import {MatPaginatorModule} from "@angular/material/paginator";
import {ReactiveFormsModule} from "@angular/forms";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatButtonModule} from "@angular/material/button";
import {CommonModule, NgOptimizedImage} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {HomeComponent} from "./home.component";
import {SearchComponent} from "./search/search.component";
import {HeaderComponent} from "../header/header.component";
import {HomeRoutingModule} from "./home-routing.module";
import {MatInputModule} from "@angular/material/input";
import {OverlayModule} from "@angular/cdk/overlay";

@NgModule({
  declarations: [
    HomeComponent,
    SearchComponent,
    HeaderComponent
  ],
  exports: [
    HeaderComponent,
    SearchComponent,
    MatSnackBarModule
  ],
    imports: [
        CommonModule,
        MatCardModule,
        MatPaginatorModule,
        MatCheckboxModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatButtonModule,
        MatSnackBarModule,
        HomeRoutingModule, MatInputModule, OverlayModule, NgOptimizedImage
    ]
})
export class HomeModule { }
