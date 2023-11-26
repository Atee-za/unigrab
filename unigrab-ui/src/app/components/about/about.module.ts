import { NgModule } from '@angular/core';
import {AboutComponent} from "./about.component";
import {CommonModule} from "@angular/common";
import {RouterModule, Routes} from "@angular/router";
import {AboutRoutingModule} from "./about-routing.module";

const routes: Routes = [
  {
    path: 'about',
    component: AboutComponent,
    children: [],
  },
];

@NgModule({
  declarations: [
    AboutComponent
  ],
  imports: [
    CommonModule,
    AboutRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class AboutModule { }
