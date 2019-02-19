import {Component} from "@angular/core";
import {ICellRendererAngularComp} from "ag-grid-angular";
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'child-cell',
    template: `<ng-template #content let-modal>
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title">Profile update</h4>
      <button type="button" class="close" aria-label="Close" (click)="modal.dismiss('Cross click')">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <form>
      <div class="row">
        <div class="col-lg-6">
        </div>
        <div class="col-lg-6">
        </div>
      </div>
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

      <div class="row">
        <div class="col-lg-6">
        </div>
        <div class="col-lg-6">
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
      <button type="button" class="btn btn-outline-dark" (click)="modal.close('Save click')">Save</button>
    </div>
  </ng-template>
  
  
  <span><button style="height: 20px; font-size:12px;    padding: 0 10px;" (click)="open(content)" class="btn btn-info">Edit</button></span>
  <hr>
  
  <pre>{{closeResult}}</pre>`,
    styles: [
        `
        .modal-body {
          background: #fff;
          margin: 0px auto;
          width: 100%;}`
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