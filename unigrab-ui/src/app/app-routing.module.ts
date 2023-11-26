import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AuthenticationGuard} from "./service/authentication/authentication.guard";

const routes: Routes = [
  {
    path: "",
    canActivate: [AuthenticationGuard],
    children: [
      { path: "", redirectTo: "/home", pathMatch: "full" },
      { path: "home", loadChildren: () => import('./components/home/home.module').then(m => m.HomeModule) },
      { path: "item", loadChildren: () => import('./components/home/item/item.module').then(m => m.ItemModule) },
      { path: "login", loadChildren: () => import('./components/login/login.module').then(m => m.LoginModule) },
      { path: "about", loadChildren: () => import('./components/about/about.module').then(m => m.AboutModule) },
      { path: "profile", loadChildren: () => import('./components/profile/profile.module').then(m => m.ProfileModule) },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
