import {Component} from '@angular/core';
import {AuthService} from "../../service/authentication/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  showSearch = false;

  constructor(private authService: AuthService, private route: Router) {}

  toggleSearch() {
    this.showSearch = !this.showSearch;
  }

  currentRouteIsHome(): boolean {
    if(this.route.url.substring(1)  === 'home'){
      this.removeActiveLink();
      return true;
    }
    return false;
  }

  removeActiveLink() {
    const activeLinks = document.querySelectorAll('.active-link');
    activeLinks.forEach(link => {
      link.classList.remove('active-link');
    });
  }

  logout(){
    this.authService.logout();
  }

  get isAuthenticated(){
    return this.authService.isAuthenticated();
  }

}
