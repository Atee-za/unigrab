import {Component, OnInit, ViewChild} from '@angular/core';
import {AuthService} from "../../service/authentication/auth.service";
import {Router} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user";
import {Item} from "../../model/item";
import {HttpErrorResponse} from "@angular/common/http";
import {ItemService} from "../../service/item.service";
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {MatSort} from "@angular/material/sort";
import {MatDialog} from "@angular/material/dialog";
import {DialogComponent} from "./dialog/dialog.component";
import {SnackbarService} from "../../service/snackbar.service";
import {Campus} from "../../model/campus";
import {Town} from "../../model/town";
import {SharedService} from "../../service/shared.service";
import {DialogData} from "../../model/dialogData";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../model/unsubscribe";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent extends Unsubscribe implements OnInit {

  userCopy: User;
  items: Item[] = [];
  images: File[] = [];
  newItemForm: FormGroup;
  userInfoForm: FormGroup;
  userPasswordForm: FormGroup;
  addressTownForm: FormGroup;
  addressCampusForm: FormGroup;

  errorMsg: string;
  imageUrls: string[] = [];
  itemTypes: string[] = ['Book', 'Camera', 'Laptop', 'Phone'];

  loadingPage = true;

  hideNPassword = true;
  hideCPassword = true;

  loadingUserInfo = false;
  loadingPassword = false;
  loadingUploadItem = false;

  dataSource = new MatTableDataSource<any>;
  displayedColumns: string[] = ['name', 'type', 'price', 'action'];

  sort: MatSort;
  paginator: MatPaginator;

  @ViewChild(MatSort) set matSort(ms: MatSort) {
    this.sort = ms;
    this.refreshItemList();
  }

  @ViewChild(MatPaginator) set matPaginator(mp: MatPaginator) {
    this.paginator = mp;
    this.refreshItemList();
  }

  currentSection = 'items'

  constructor(private router: Router,
              public dialog: MatDialog,
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private itemService: ItemService,
              private userService: UserService,
              private sharedService: SharedService,
              private snackbarService: SnackbarService) {
    super();
  }

  ngOnInit(): void {
    if (!this.isAuthenticated) {
      this.router.navigate(['/login']);
    } else {
      this.initForms();
      this.loadUserInfo();
    }
  }

  showSection(section:string){
    this.currentSection = section;
  }

  get isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  saveItem() {
    this.loadingUploadItem = true;
    if (this.isValidNewItem() && this.images.length > 0) {
      if (!this.isValidString(this.userCopy.locationId)) {
        this.snackbarService.openSnackBar('Provide your location first');
        this.loadingUploadItem = false;
        return;
      }

      const data = new FormData();

      data.append('name', this.newItemForm.value.itemName);
      data.append('type', this.newItemForm.value.type);
      data.append('price', this.newItemForm.value.price);
      data.append('description', this.newItemForm.value.description);

      for (const image of this.images) {
        data.append('images', image, image.name);
      }

      this.itemService.saveNewItem(data)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((res) => {
          this.resetNewItemForm();
          this.items.push(res);
          this.refreshItemList();
          this.snackbarService.openSnackBar('Item saved successfully');
          this.loadingUploadItem = false;
        }, (error: HttpErrorResponse) => {
          this.sharedService.handleError(error);
          this.loadingUploadItem = false;
        });
    } else {
      this.errorMsg ? this.snackbarService.openSnackBar(this.errorMsg) :
      this.snackbarService.openSnackBar('Item not saved, make sure all fields are populated and in correct format');
      this.loadingUploadItem = false;
    }
  }

  onFileUpload(event: Event) {
    this.errorMsg = '';

    let input = event.target as HTMLInputElement;
    if (!input.files?.length || (input.files.length + this.images.length > 3)) {
      this.snackbarService.openSnackBar('Upload 1 - 3 image(s) allowed. Remove image from Preview by clicking on it.');
      return;
    }

    let size = input.files.length;
    for (let i = 0; i < size; i++) {
      let image = input.files[i];
      if (image.type === 'image/jpg' || image.type === 'image/jpeg' || image.type === 'image/png') {
        this.images.push(input.files[i]);
        this.previewImage(input.files[i], this.images.length - 1);
      } else {
        this.errorMsg = 'Incorrect format detected, Only jpg, jpeg and png accepted.';
        this.newItemForm.value.images = '';
      }
    }
  }

  removeImageFromPreview(index: number) {
    this.images.splice(index, 1);
    this.imageUrls.splice(index, 1);
  }

  isStudent() {
    return this.userInfoForm.value.student === 'true';
  }

  updateAddress(isStudent: boolean) {
    this.loadingUserInfo = true;
    let town: Town;
    let campus: Campus;

    if (isStudent) {
      campus = this.addressCampusForm.value;
      campus.id = '';
      this.userService.updateUserAddressCampus(campus)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((res) => {
          this.updateCampusAddressValues(res);
          this.snackbarService.openSnackBar('Address updates successfully');
          this.loadingUserInfo = false;
        }, (error: HttpErrorResponse) => {
          this.sharedService.handleError(error);
          this.loadingUserInfo = false;
      });
    } else {
      town = this.addressTownForm.value;
      town.id = '';
      this.userService.updateUserAddressTown(town)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((res) => {
          this.updateTownAddressValues(res);
          this.snackbarService.openSnackBar('Address updates successfully');
          this.loadingUserInfo = false;
        }, (error: HttpErrorResponse) => {
          this.sharedService.handleError(error);
          this.loadingUserInfo = false;
      });
    }
  }

  deleteItem(itemId: string, itemType: string) {
    if (confirm("Are you sure to delete this " + itemType + " item?")) {
      this.itemService.deleteItemById(itemId)
        .pipe(takeUntil(this.unsubscribe$))
        .subscribe((res) => {
          if (res) {
            this.snackbarService.openSnackBar('Item deleted successfully');
            this.removeItemFromList(itemId);
          } else {
            this.snackbarService.openErrorSnackBar('Item could NOT be deleted');
          }
        }, (error: HttpErrorResponse) => {
          this.sharedService.handleError(error);
        });
    }
  }

  isValidNewItem() {
    return this.newItemForm.valid;
  }

  canUpdateAddress(){
    if(this.isStudent()){
      return !this.isValidAddressCampusForm() || this.loadingUserInfo;
    } else {
      return !this.isValidAddressTownForm() || this.loadingUserInfo;
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openDialog(item: Item, type: string) {
    const dialogData: DialogData = {item, type};
    this.dialog.open(DialogComponent, {
      data: dialogData
    });

  }

  updatePassword(){
    this.loadingPassword = true;
    this.userService.updateUserPassword(this.userPasswordForm.value)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
        if(res){
          this.snackbarService.openSnackBar('Password updated successfully');
          this.userPasswordForm.reset();
          this.userPasswordForm.markAsUntouched();
        } else {
          this.snackbarService.openErrorSnackBar('Failed to updated password');
        }
        this.loadingPassword = false;
        this.userPasswordForm.reset();
      }, (error: HttpErrorResponse) => {
        this.sharedService.handleError(error);
        this.loadingPassword = false;
    });
  }

  isValidPasswords(){
    return this.userPasswordForm.valid &&
      (this.userPasswordForm.value.newPassword === this.userPasswordForm.value.confirmPassword)
      && !this.loadingPassword;
  }

  private isValidString(data: string): boolean {
    return data != undefined && true && data.trim().length > 0;
  }

  private loadUserInfo() {
    this.loadingPage = true;
    this.userService.getUserInfo()
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
        this.assignValuesToUserInfoForms(res);
        this.userCopy = res;
        this.loadingPage = false;
      }, (error: HttpErrorResponse) => {
        this.sharedService.handleError(error);
        this.router.navigate(['/login']);
      });
  }

  private isValidAddressCampusForm() {
    return this.addressCampusForm.valid && this.changeDetected('true');
  }

  private isValidAddressTownForm() {
    return this.addressTownForm.valid && this.changeDetected('false');
  }

  private resetNewItemForm() {
    this.images = [];
    this.imageUrls = [];
    this.newItemForm.reset();
    this.newItemForm.markAsUntouched();
  }

  private removeItemFromList(id: string) {
    for (let i = 0; i < this.items.length; i++) {
      if (this.items[i].id === id) {
        this.items.splice(i, 1);
        this.refreshItemList();
        break;
      }
    }
  }

  private refreshItemList() {
    this.dataSource = new MatTableDataSource(this.items);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  private previewImage(file: File, index: number) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onerror = reject;
      reader.onload = () => {
        this.imageUrls[index] = reader.result as string;
      }
      reader.readAsDataURL(file);
    });
  }

  private initForms() {
    //field: new FormControl('', [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&].{6,}')]),
    this.newItemForm = this.formBuilder.group({
      images: ['', Validators.required],
      type: ['', Validators.required],
      itemName: ['', Validators.required],
      price: [0, Validators.required],
      description: ['', Validators.required]
    });

    this.userInfoForm = new FormGroup({
      email: new FormControl('', Validators.required),
      contact: new FormControl(''),
      locationId: new FormControl(0, Validators.required),
      student: new FormControl('', Validators.required)
    });

    this.userPasswordForm = new FormGroup({
      currentPassword: new FormControl('', Validators.required),
      newPassword: new FormControl('', Validators.required),
      confirmPassword: new FormControl('', Validators.required),
    });

    this.addressTownForm = this.formBuilder.group({
      suburb: ['', Validators.required],
      townName: ['', Validators.required]
    });

    this.addressCampusForm = this.formBuilder.group({
      campusName: ['', Validators.required],
      schoolName: ['', Validators.required],
      suburb: ['', Validators.required]
    });

  }

  private changeDetected(addressType: string) {
    if (addressType === 'true') {
      return this.userCopy.campusName !== this.addressCampusForm.value.campusName ||
        this.userCopy.schoolName !== this.addressCampusForm.value.schoolName ||
        this.userCopy.suburb !== this.addressCampusForm.value.suburb;

    } else {
      return this.userCopy.townName !== this.addressTownForm.value.townName ||
        this.userCopy.suburb !== this.addressTownForm.value.suburb;
    }

  }

  private assignValuesToUserInfoForms(user: User) {
    this.userInfoForm.setValue({
      email: user.email, contact: user.contact,
      locationId: user.locationId, student: user.student ? 'true' : 'false'
    });
    if (user.student) {
      this.addressCampusForm.setValue({
        campusName: user?.campusName,
        schoolName: user?.schoolName,
        suburb: user?.suburb
      });
    } else {
      this.addressTownForm.setValue({townName: user?.townName, suburb: user?.suburb});
    }

    if (user.items) {
      this.items = user.items;
      this.refreshItemList();
    }

  }

  private updateTownAddressValues(town: Town) {
    this.addressTownForm.value.townName = town?.townName;
    this.addressTownForm.value.suburb = town?.suburb;
    this.userCopy.locationId = town?.id;
    this.userCopy.suburb = town?.suburb;
    this.userCopy.townName = town?.townName;
  }

  private updateCampusAddressValues(campus: Campus) {
    this.addressCampusForm.value.suburb = campus?.suburb;
    this.addressCampusForm.value.campusName = campus?.campusName;
    this.addressCampusForm.value.schoolName = campus?.schoolName;
    this.userCopy.locationId = campus?.id;
    this.userCopy.suburb = campus?.suburb;
    this.userCopy.campusName = campus?.campusName;
    this.userCopy.schoolName = campus?.schoolName;
  }

}
