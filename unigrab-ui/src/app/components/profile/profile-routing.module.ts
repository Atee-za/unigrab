import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ProfileComponent} from "./profile.component";
import {AuthenticationGuard} from "../../service/authentication/authentication.guard";

const routes: Routes = [ { path: "", component: ProfileComponent, canActivate: [AuthenticationGuard] } ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileRoutingModule { }
