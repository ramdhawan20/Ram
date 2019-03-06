import { Component, OnInit } from '@angular/core';
import { AgGridModule } from 'ag-grid-angular';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { HttpClient } from "@angular/common/http";
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-usermanagement',
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.css']
})
export class UsermanagementComponent implements OnInit {
  private gridApi;
  private gridColumnApi;
  private columnDefs;
  private rowSelection;
  private rowGroupPanelShow;
  private pivotPanelShow;
  private paginationPageSize;
  private paginationStartPage;
  private paginationNumberFormatter;
  private rowData;
  private context;
  private frameworkComponents;
  closeResult: string;
  constructor(private flashMessage: FlashMessagesService,private modalService: NgbModal,private http: HttpClient, private globalServiceService: GlobalServiceService, private childMessageRenderer: ChildMessageRenderer) {
    this.columnDefs = [
      { headerName: 'User Profile', field: 'userProfile' , width: 125},
      { headerName: 'First Name', field: 'userFirstName' , width: 145},
      { headerName: 'Middle Name', field: 'userMiddleName' , width: 145},
      { headerName: 'Last Name', field: 'userLastName', width: 145 },
      { headerName: 'User Id', field: 'userId' , width: 145},
      { headerName: 'Status', cellRenderer: "childMessageRenderer", colId: "params", width: 250 }
    ];
    // this.rowData = this.createRowData();
    this.context = { componentParent: this };
    this.frameworkComponents = {
      childMessageRenderer: ChildMessageRenderer
    };
    this.rowSelection = "multiple";
    this.rowGroupPanelShow = "always";
    this.pivotPanelShow = "always";
    this.paginationPageSize = 10;
    // this.paginationStartPage =  0;
    this.paginationNumberFormatter = function (params) {
      return "[" + params.value.toLocaleString() + "]";
    };
  }
  // //open popup code start
  // openModal(id: string) {
  //   this.modalService.open(id);
  // }
  // //open popup code end

  // //close popup code start
  // closeModal(id: string) {
  //   this.modalService.close(id);
  // }
  // //close popup code end

  ngOnInit() { }
  onPageSizeChanged(newPageSize) {
    var inputElement = <HTMLInputElement>document.getElementById("page-size");
    var value = inputElement.value;
    this.gridApi.paginationSetPageSize(Number(value));
  }

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    /* this.globalServiceService.usermanagementCalling().subscribe(
      data => {
        this.rowData = data;
        params.api.paginationGoToPage(1);
      }); */
    this.globalServiceService.getUserData().subscribe(
		data => {
			 this.rowData = data;
			 params.api.paginationGoToPage(1);
        },
    error=>{
     // this.flashMessage.show('Record not found !!', { cssClass: 'alert-danger', timeout: 2000 });
    });
  }
  onQuickFilterChanged() {
    var inputElement = <HTMLInputElement>document.getElementById("quickFilter");
    this.gridApi.setQuickFilter(inputElement.value);
  }

  exportImport() {
    document.getElementById("exportImportBox").style.display = "block";
  }
  // export to Csv code start
  onBtExport() {
    // var inputElements= <HTMLInputElement>document.getElementById("#fileName");
    var params = {
      fileName: "usermanagement",
      // fileName: inputElements.value,
    };
    this.gridApi.exportDataAsCsv(params);
  }
  // export to Csv code end
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
  addUserData(firstName,middleName,lastName,email,userProfile,password){
    this.globalServiceService.addUser(email,userProfile,firstName,middleName,lastName,password).subscribe(
      result => {
      console.log(result);
      this.flashMessage.show('User created successfully!!', { cssClass: 'alert-success', timeout: 3000 });
      },
		error=>{
			console.log(error.status );
			if(error.status===200){
				this.flashMessage.show('User created successfully!!', { cssClass: 'alert-success', timeout: 3000 });
			}
			else{
			
      this.flashMessage.show('User creation  failed !!', { cssClass: 'alert-danger', timeout: 3000 });
			}
    });
  }
  searchUser(user_profile,user_name,first_name,status_val){

    console.log(user_profile,user_name,first_name,status_val);
    this.globalServiceService.searchUserData(user_profile,user_name,first_name,status_val).subscribe(
      data => {
      console.log(data);
      });
  }
}

