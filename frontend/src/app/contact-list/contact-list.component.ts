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
      { headerName: 'SUBSCRIPTION ID', field: 'subscriptionNo' },
      { headerName: 'CUSTOMBER NAME', field: 'customerName' },
      { headerName: 'EMAIL', field: 'email' },
      { headerName: 'PLAN NAME', field: 'planName' },
      { headerName: 'STATUS', field: 'status' },
      { headerName: 'PRICE', field: 'price' },
      { headerName: 'CREATED ON', field: 'createdDate' },
      { headerName: 'ACTIVATED ON', field: 'activatedDate' },
      { headerName: 'LAST BILLED ON', field: 'lastBillDate' },
      { headerName: 'NEXT BILL DATE', field: 'nextBillDate', width:200},
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


  searchSubcription(subscriptionNo,customerName,email,planName,status,price,createdDate,activatedDate,lastBillDate,nextBillDate,first_name){
    this.globalServiceService.searchSubcription(subscriptionNo,customerName,email,planName,status,price,createdDate,activatedDate,lastBillDate,nextBillDate,first_name).subscribe(
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
        params.api.paginationGoToPage(1);
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

