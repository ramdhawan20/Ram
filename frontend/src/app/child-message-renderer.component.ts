import {Component} from "@angular/core";
import {ICellRendererAngularComp} from "ag-grid-angular";
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'child-cell',
    template: `<ng-template #content let-modal>
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title">Profile Update</h4>
      <button type="button" class="close" aria-label="Close" (click)="modal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <form>
      <div class="row">
        <div class="col-lg-6">
          <div class="form-group">
            <label for="email">userProfile:</label>
            <input type="email" class="form-control"  value={{params.data.userProfile}} name="email">
          </div> 
        </div>
        <div class="col-lg-6">
            <div class="form-group">
              <label for="email">userMiddleName:</label>
              <input type="email" class="form-control"  value={{params.data.userMiddleName}} name="email">
            </div>
        </div>
      </div>
      <div class="row">
        <div class="col-lg-6">
          <div class="form-group">
            <label for="email">userLastName:</label>
            <input type="email" class="form-control"  value={{params.data.userLastName}} name="email">
          </div> 
        </div>
        <div class="col-lg-6">
            <div class="form-group">
              <label for="email">userId:</label>
              <input type="email" class="form-control"  value={{params.data.userId}} name="email">
            </div>
        </div>
      </div>  
        <div class="form-group">
          <label for="dateOfBirth"> Date:</label>
          <div class="input-group">
            <input id="dateOfBirth" class="form-control" placeholder="yyyy-mm-dd" name="dp" ngbDatepicker #dp="ngbDatepicker">
            <div class="input-group-append">
              <button class="btn btn-outline-secondary calendar" (click)="dp.toggle()" type="button">
              <i class="fa fa-calendar fa-2" aria-hidden="true"></i>
              </button>
            </div>
          </div>
        </div>
      </form>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-info" (click)="modal.close('Save click')">Save</button>
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
    <div class="modal-body">
      <form>
      <div class="row">
        <div class="col-lg-6">
        <div class="form-group">
            <label for="email">Enter Password:</label>
            <input type="password" class="form-control"  value={{params.data.userProfile}}>
          </div> 
        </div>
        <div class="col-lg-6">
        <div class="form-group">
            <label for="email">Confirm Password:</label>
            <input type="password" class="form-control"  value={{params.data.userProfile}}>
          </div> 
        </div>
      </div>
      </form>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-info" (click)="modal.close('Save click')">Save</button>
    </div>
  </ng-template>
  <!--reset end-->
  <span><button style="height: 20px; font-size:12px;padding: 0 10px;" (click)="open(content)" class="btn btn-info">Edit</button></span>
  <span><button style="height: 20px; font-size:12px;padding: 0 10px; margin-left:3px;" (click)="open(reset)" class="btn btn-info">Reset</button></span>
  <span style="float:left;margin-right: 3px;margin-top: 4px;">
    <div class="onoffswitch">
      <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="{{params.data.userProfile}}" checked>
      <label class="onoffswitch-label" for="{{params.data.userProfile}}">
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
            content: "ON";
            padding-left: 4px;
            background-color: #17a2b8; color: #FFFFFF; font-size:9px;
        }
        .onoffswitch-inner:after {
            content: "OFF";
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
          `
    ]
})
export class ChildMessageRenderer implements ICellRendererAngularComp {
    public params: any;
    data;
    closeResult: string;
    constructor( private modalService: NgbModal) {
      
    }

    open(content) {
      this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
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
        return  `with: ${reason}`;
      }
    }

    agInit(params: any): void {
        this.params = params;
        this.data=this.params.data.userId;
    }
    public invokeParentMethod() {
        console.log(this.params.data);
    }

    refresh(): boolean {
        return false;
    }

}