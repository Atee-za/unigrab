import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef, MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Item} from "../../../model/item";
import {DialogData} from "../../../model/dialogData";
import {SnackbarService} from "../../../service/snackbar.service";
import {ItemService} from "../../../service/item.service";
import {HttpErrorResponse} from "@angular/common/http";
import {SharedService} from "../../../service/shared.service";
import {takeUntil} from "rxjs";
import {Unsubscribe} from "../../../model/unsubscribe";

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent extends Unsubscribe implements OnInit {

  item: Item;
  itemCopy: Item;
  itemForm: FormGroup;

  msgType: string;
  heading: string;
  exitButton: string;
  hideUpdate: boolean;

  constructor(private formBuilder: FormBuilder,
              private itemService: ItemService,
              private sharedService: SharedService,
              private snackbar: SnackbarService,
              @Inject(MAT_DIALOG_DATA) public dialogData: DialogData,
              private dialogRef: MatDialogRef<DialogComponent>) {
    super();
  }

  ngOnInit(): void {
    this.itemForm = this.formBuilder.group({
      type: ['', Validators.required],
      name: ['', Validators.required],
      price: [0, Validators.required],
      description: ['', Validators.required]
    });

    this.item = this.dialogData.item;
    this.msgType = this.dialogData.type;

    if (this.item) {
      this.itemCopy = this.item;
      this.mapDataToForm(this.item);
      this.initButtons();
    } else {
      this.closeDialog();
    }

  }

  getImage(imageUrl: string){
    return this.itemService.getImage(imageUrl);
  }

  isValidNewItem(){
    return this.itemForm.valid && this.changeDetectedInItem();
  }


  update(){
    if(!this.isValidNewItem()){
      this.snackbar.openErrorSnackBar('Fill in all required fields');
      return;
    }

    this.assignNewValuesToItem();
    this.itemService.updateItem(this.itemCopy)
      .pipe(takeUntil(this.unsubscribe$))
      .subscribe((res) => {
        this.item = res;
        this.itemCopy = this.item;
        this.snackbar.openSnackBar('Item updates successfully');
      }, (error: HttpErrorResponse) => {
        this.sharedService.handleError(error);
      });
    this.closeDialog();
  }

  private mapDataToForm(item: Item){
    this.itemForm.controls['name'].setValue(item.name);
    this.itemForm.controls['type'].setValue(item.type.toLowerCase());
    this.itemForm.controls['price'].setValue(item.price);
    this.itemForm.controls['description'].setValue(item.description);
  }

  private initButtons(){
    if(this.msgType === 'view'){
      this.exitButton = 'Close'
      this.heading = 'Viewing'
      this.hideUpdate = true;
    } else if (this.msgType === 'edit'){
      this.exitButton = 'Cancel'
      this.heading = 'Updating'
      this.hideUpdate = false;
    }
  }

  private closeDialog(){
    this.dialogRef.close();
  }

  private changeDetectedInItem(){
    return this.itemCopy.name !== this.itemForm.value.name ||
      this.itemCopy.type !== this.itemForm.value.type ||
      this.itemCopy.price !== this.itemForm.value.price ||
      this.itemCopy.description !== this.itemForm.value.description;
  }

  private assignNewValuesToItem(){
    this.itemCopy.name = this.itemForm.value.name;
    this.itemCopy.type = this.itemForm.value.type;
    this.itemCopy.price = this.itemForm.value.price;
    this.itemCopy.description = this.itemForm.value.description;
  }

}
