import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/authentication/auth.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {SnackbarService} from "../../service/snackbar.service";
import {SharedService} from "../../service/shared.service";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../model/unsubscribe";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends Unsubscribe implements OnInit {

  hidePassword = true;
  hideConfirmPassword = true;
  loginForm: FormGroup;
  registerForm: FormGroup;
  loading: boolean = false;
  viewLoginTemplate: boolean = true;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private snackbar: SnackbarService,
              private sharedService: SharedService) {
    super();
  }

  ngOnInit(): void {
    if(this.isAuthenticated){
      this.router.navigate(['/home'])
    } else {
      this.initForms();
    }
  }

  private get isAuthenticated(){
    return this.authService.isAuthenticated();
  }

  signIn(){
    this.loading = true;
    if(!this.isValidLogin()){
      this.snackbar.openSnackBar('Invalid credentials');
      this.loading = false;
      return;
    }

    this.authService.login(this.loginForm.value.email, this.loginForm.value.password)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
        this.authService.setToken(res.token);
        this.loading = false;
        this.router.navigate(['/home'])
      },
        (error: HttpErrorResponse) => {
          this.sharedService.handleError(error);
          this.loading = false;
      });
  }

  register(){
    this.loading = true;
    if(!this.isValidRegister()){
      this.snackbar.openErrorSnackBar('Invalid Registration information provided');
      this.loading = false;
      return;
    }

    this.authService.register(this.registerForm.value.email, this.registerForm.value.password,
      this.registerForm.value.confirmPassword ,this.registerForm.value.isStudent)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
          this.authService.setToken(res.token);
          this.loading = false;
          this.router.navigate(['/home'])
        },
        (error: HttpErrorResponse) => {
          this.loading = false;
          this.sharedService.handleError(error);
      });
  }

  formsToggle(){
    this.viewLoginTemplate = !this.viewLoginTemplate;
  }

  isValidLogin(){
    return this.loginForm.valid;
  }

  isValidRegister(){
    return this.registerForm.valid;
  }

  private initForms(){
    this.loginForm = this.formBuilder.group({
      email: ['a@t.com', Validators.required],
      password: ['123456', Validators.required]
    });

    this.registerForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      isStudent: ['', Validators.required],
      accepted: ['', Validators.required]
    });
  }

}
