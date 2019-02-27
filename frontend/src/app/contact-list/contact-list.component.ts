import { Component, OnInit } from '@angular/core';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { HttpClient } from "@angular/common/http";
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { Router } from '@angular/router';
@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {
  private gridApi;
  private gridColumnApi;
  private columnDefs;
  private rowSelection;
  private rowGroupPanelShow;
  private pivotPanelShow;
  private paginationPageSize;
  private paginationNumberFormatter;
  private rowData;
  private context;
  private frameworkComponents;
  private fileName;

  constructor(private router : Router,private flashMessage: FlashMessagesService,private http: HttpClient, private modalService: ModalsService, private globalServiceService: GlobalServiceService,private childMessageRenderer: ChildMessageRenderer) {

    this.columnDefs = [
      { headerName: 'SUBSCRIPTION ID', field: 'subscriptionid' },
      { headerName: 'CUSTOMBER NAME', field: 'customberemail' },
      { headerName: 'STATUS', field: 'status' },
      { headerName: 'CREATED ON', field: 'create' },
      { headerName: 'ACTIVATED ON', field: 'activated' },
      { headerName: 'CUSTOMER NAME', field: 'customer' },
      { headerName: 'PLAN NAME', field: 'plan', width:200},
      { headerName: 'AMOUNT', field: 'amount' },
      // { headerName: 'AMOUNT', field: 'amount',cellRenderer: "childMessageRenderer",
      // colId: "params" },
      { headerName: 'LAST BILLED ON', field: 'lastbilled' },
      { headerName: 'NEXT', field: 'next', editable: true }
    ];
    this.rowData = this.createRowData();
    this.context = { componentParent: this };
    this.frameworkComponents = {
      childMessageRenderer: ChildMessageRenderer
    };
    this.rowSelection = "multiple";
    this.rowGroupPanelShow = "always";
    this.pivotPanelShow = "always";
    this.paginationPageSize = 10;
    this.paginationNumberFormatter = function (params) {
      return "[" + params.value.toLocaleString() + "]";
    };
  }
  //open popup code start
  openModal(id: string) {
    this.modalService.open(id);
  }
  //open popup code end
   createRowData() {
    var rowData = [];
    for (var i = 0; i < 15; i++) {
      rowData.push({
        row: "Row " + i,
        value: i,
        currency: i + Number(Math.random().toFixed(2))
      });
    }
    return rowData;
  }
  //close popup code start
  closeModal(id: string) {
    this.modalService.close(id);
  }
  //close popup code end

  ngOnInit() { }
  onPageSizeChanged(newPageSize) {
    var inputElement = <HTMLInputElement>document.getElementById("page-size");
    var value = inputElement.value;
    this.gridApi.paginationSetPageSize(Number(value));
  }


  searchSubcription(Subscription_id,customber_email,first_name,status,Created_on,Activated_on,Customber_name,plan_name,amount,last_bill,next){
    this.globalServiceService.searchSubcription(Subscription_id,customber_email,first_name,status,Created_on,Activated_on,Customber_name,plan_name,amount,last_bill,next).subscribe(
      data => {
      console.log(data);
      this.flashMessage.show('Search successfully!!', { cssClass: 'alert-success', timeout: 2000 });
      },
    error=>{
      this.flashMessage.show('Search unsuccessfully!! !!', { cssClass: 'alert-danger', timeout: 2000 });
    });
  }

  isValid(): boolean {
    if (this.router.url != '/subscriptions/report') {
              return true;
      }
    return false;
  }

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.globalServiceService.SubscriptionCalling().subscribe(
      data => {
        this.rowData = data;
        params.api.paginationGoToPage(4);
      });
  }
  onQuickFilterChanged() {
    var inputElement= <HTMLInputElement>document.getElementById("quickFilter");
    this.gridApi.setQuickFilter(inputElement.value);
  }

  exportImport(){
    document.getElementById("exportImportBox").style.display="block";
  }
  // export to Csv code start
  onBtExport() {
    // var inputElements= <HTMLInputElement>document.getElementById("#fileName");
    var params = {
     fileName : "subscriptions",
      // fileName: inputElements.value
    };
    this.gridApi.exportDataAsCsv(params);
  }
// export to Csv code end
} 

