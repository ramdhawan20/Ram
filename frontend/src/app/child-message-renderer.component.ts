import { Component, OnInit } from "@angular/core";
import { ICellRendererAngularComp } from "ag-grid-angular";
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MustMatch } from './_helpers/must-match.validator';
import { GlobalServiceService } from './global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
@Component({
  selector: 'child-cell',
  template: `<ng-template #content let-modal>
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title">Profile Update</h4>
      <button type="button" class="close" aria-label="Close" (click)="modal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
   
    <flash-messages></flash-messages>
  
    <div class="modal-body">
      <form>
      <div class="row">
        <div class="col-lg-6">
        <div class="form-group">
        <label for="firstName">userFirstName:</label>
        <input type="text" class="form-control"  placeholder={{params.data.userFirstName}} name="firstName" [(ngModel)]="params.data.userFirstName">
        </div> 
        </div> 
        <div class="col-lg-6">
            <div class="form-group">
              <label for="middleName">userMiddleName:</label>
              <input type="text" class="form-control"  placeholder={{params.data.userMiddleName}} name="middleName" [(ngModel)]="params.data.userMiddleName">
            </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6">
          <div class="form-group">
            <label for="lastName">userLastName:</label>
            <input type="text" class="form-control"  placeholder={{params.data.userLastName}} name="lastName" [(ngModel)]="params.data.userLastName">
          </div> 
        </div>
        <div class="col-lg-6">
            <div class="form-group">
              <label for="email">userId:</label>
              <input type="email" class="form-control" disabled placeholder={{params.data.userId}} name="email" [(ngModel)]="params.data.userId">
            </div>
        </div>
      </div>  
      <div class="row">
      <div class="col-lg-6">     
        <div class="form-group">
        <label for="userProfile">userProfile:</label>
        <input type="text" class="form-control"  placeholder={{params.data.userProfile}} name="userProfile" [(ngModel)]="params.data.userProfile">
      </div> 
      </div>
      <div class="col-lg-6">
        <div class="form-group">
          <label for="dateOfBirth"> Date:</label>
          <div class="input-group">
            <input id="dateOfBirth" class="form-control" placeholder={{params.data.isLocked}} name="dp" [(ngModel)]="params.data.isLocked" ngbDatepicker #dp="ngbDatepicker">
            <div class="input-group-append">
              <button class="btn btn-outline-secondary calendar" (click)="dp.toggle()" type="button">
              <i class="fa fa-calendar fa-2" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </div>  
      
      </div>
      </form>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-info" (click)="editData(params.data.userFirstName,params.data.userMiddleName,params.data.userLastName,params.data.userId,params.data.userProfile,params.data.isLocked)">Save</button>
      <button type="button" class="btn btn-info" (click)="modal.close('Save click')">Close</button>
    </div>
  </ng-template>
  <!--reset start-->
  <ng-template #reset let-modal>
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title">Password Reset</h4>
      <button type="button" class="close" aria-label="Close" (click)="modal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <flash-messages></flash-messages>
    <div class="modal-body">
      <div class="row">
      <div class="col-md-6 offset-md-3">                
          <form [formGroup]="registerForm">
             <div class="form-group">
                  <label>New Password</label>
                  <input type="password" formControlName="password" class="form-control" [ngClass]="{ 'is-invalid': submitted && f.password.errors }" />
                  <div *ngIf="submitted && f.password.errors" class="invalid-feedback">
                      <div *ngIf="f.password.errors.required">Password is required</div>
                      <div *ngIf="f.password.errors.minlength">Password must be at least 6 characters</div>
                  </div>
              </div>

              <div class="form-group">
                  <label>Confirm Password</label>
                  <input type="password" formControlName="confirmPassword" class="form-control" [ngClass]="{ 'is-invalid': submitted && f.confirmPassword.errors }" />
                  <div *ngIf="submitted && f.confirmPassword.errors" class="invalid-feedback">
                      <div *ngIf="f.confirmPassword.errors.required">Confirm Password is required</div>
                      <div *ngIf="f.confirmPassword.errors.mustMatch">Passwords must match</div>
                  </div>
              </div>              
          </form>
      </div>
  </div>
  </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-info" (click)="onSubmit()" >Save</button>
      <button type="button" class="btn btn-info" (click)="modal.close('Save click')" >Close</button>
    </div>
  </ng-template>
  <!--reset end-->
  <span><button style="height: 20px; font-size:12px;padding: 0 10px;" (click)="open(content)" class="btn btn-info">Edit</button></span>
  <span><button title="Reset Password" style="height: 20px; font-size:12px;padding: 0 10px; margin-left:3px;" (click)="open(reset)" class="btn btn-info">Reset Password</button></span>
  <span style="float:left;margin-right: 3px;margin-top: 4px;">
    <div class="onoffswitch">
      <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="{{params.data.userProfile}}" checked>
      <label class="onoffswitch-label" for="{{params.data.userProfile}}" (click)="ActivateDeactivate(params.data.userId)">
          <span class="onoffswitch-inner"></span>
          <span class="onoffswitch-switch"></span>
      </label>
    </div>
  </span>
  <hr>
  
  <pre>{{closeResult}}</pre>`,
  styles: [
    `
        .modal-body {
          background: #fff;
          margin: 0px auto;
          width: 100%;}
          .onoffswitch {
            position: relative; width: 50px;
            -webkit-user-select:none; -moz-user-select:none; -ms-user-select: none;
        }
        .onoffswitch-checkbox {
            display: none;
        }
        .onoffswitch-label {
            display: block; overflow: hidden; cursor: pointer;
            border: 1px solid #17a2b8; border-radius: 20px;
        }
        .onoffswitch-inner {
            display: block; width: 200%; margin-left: -100%;
            transition: margin 0.3s ease-in 0s;
        }
        .onoffswitch-inner:before, .onoffswitch-inner:after {
            display: block; float: left; width: 50%; height: 18px; padding: 0; line-height: 20px;
            font-size: 14px; color: white; font-family: Trebuchet, Arial, sans-serif; font-weight: bold;
            box-sizing: border-box;
        }
        .onoffswitch-inner:before {
            content: "Active";
            padding-left: 4px;
            background-color: #17a2b8; color: #FFFFFF; font-size:9px;
        }
        .onoffswitch-inner:after {
            content: "Deactive";
            padding-right: 4px;
            background-color: #EEEEEE; color: #999999;font-size:9px;
            text-align: right;
        }
        .onoffswitch-switch {
            display: block; width: 9px; height:9px; margin: 6px;
            background: #FFFFFF;
            position: absolute; top: 0; bottom: 0;
            right: 30px;
            border: 1px solid #999999; border-radius: 20px;
            transition: all 0.3s ease-in 0s; 
        }
        .onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-inner {
            margin-left: 0;
        }
        .onoffswitch-checkbox:checked + .onoffswitch-label .onoffswitch-switch {
            right: 0px; 
        } 
          
        #cnfmpwd{
          borderColor:"red"
      }
      .onoffswitch-switch {
        right: 37px;
      }
      .onoffswitch {
        width: 55px;
      }

      `
  ]
})
export class ChildMessageRenderer implements ICellRendererAngularComp, OnInit {
  public params: any;
  data;
  closeResult: string;
  registerForm: FormGroup;
  submitted = false;
  constructor(private modalService: NgbModal, private flashMessage: FlashMessagesService,private formBuilder: FormBuilder, private globalServiceService:GlobalServiceService) { }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({

      password: ['', [Validators.required]],
      confirmPassword: ['', Validators.required]
    }, {
        validator: MustMatch('password', 'confirmPassword')
      });
  }


  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  agInit(params: any): void {
    this.params = params;
    this.data = this.params.data.userId;
  }
  public invokeParentMethod() {
    console.log(this.params.data);
  }

  refresh(): boolean {
    return false;
  }

  // convenience getter for easy access to form fields
  get f() { return this.registerForm.controls; }

  onSubmit() {

    this.submitted = true;
    if (this.registerForm.valid) {
      let email=this.data;
      console.log(this.registerForm.value.confirmPassword,email);
      console.log(this.registerForm.value.password);
      this.globalServiceService.resetPwd(email,this.registerForm.value.confirmPassword).subscribe(
        result => {
          console.log(result);
        },
		error=>{
			
			 if(error.status==200){
          this.flashMessage.show('Password changed successfully!', { cssClass: 'alert-success', timeout: 3000 });
		  }
		  else{
			  this.flashMessage.show('Password changed unsuccessfull!', { cssClass: 'alert-danger', timeout: 3000 });
		  }
		}
       );      
    }

    // stop here if form is invalid
    if (this.registerForm.invalid) {

      return;
    }

    //do APi calling 

  }
  editData(firstName,middleName,lastName,userId,profile,date){
     this.globalServiceService.editUser(userId,profile,firstName,middleName,lastName).subscribe(
      result => {
        console.log(result);
        this.flashMessage.show('User updated successfully!', { cssClass: 'alert-danger', timeout: 3000 });
      },
      error=>{
		  if(error.status==200){
          this.flashMessage.show('User updated successfully!', { cssClass: 'alert-success', timeout: 3000 });
		  }
		  else{
			  this.flashMessage.show('User updation failed!', { cssClass: 'alert-danger', timeout: 3000 });
		  }
      }
     );
  }
  ActivateDeactivate(id){
    console.log(id)
    this.globalServiceService.activeDeactive(id).subscribe(
      result => {
        console.log(result);
      }
     );
  }
}