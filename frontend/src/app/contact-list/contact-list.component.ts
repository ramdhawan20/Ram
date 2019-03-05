import { Component, OnInit } from '@angular/core';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { HttpClient } from "@angular/common/http";
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { Router } from '@angular/router';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NgbDatepickerConfig, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateFRParserFormatter } from "../ngb-date-fr-parser-formatter";
@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
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

  constructor(private spinnerService: Ng4LoadingSpinnerService, private router : Router,private flashMessage: FlashMessagesService,private http: HttpClient, private modalService: ModalsService, private globalServiceService: GlobalServiceService,private childMessageRenderer: ChildMessageRenderer) {

    this.columnDefs = [
      { headerName: 'SUBSCRIPTION NO', field: 'subscriptionId' },
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


  searchSubcription(subscriptionNo,customerName,email,planName,status,price,createdDate,activatedDate,lastBillDate,nextBillDate){
    // if(subscriptionNo==undefined&&customerName==undefined&&email==undefined&&planName==undefined&&status==undefined&&price==undefined&&createdDate==undefined&&activatedDate==undefined&&lastBillDate==undefined&&nextBillDate==undefined){
    //   this.flashMessage.show('Please enter filter criteria', { cssClass: 'alert-danger', timeout: 10000 });
    // }
    // else{
      this.spinnerService.show();
      this.globalServiceService.searchSubcription(subscriptionNo,customerName,email,planName,status,price,createdDate,activatedDate,lastBillDate,nextBillDate).subscribe(
        data => {
        this.spinnerService.hide();
        this.rowData=[];
        console.log(data);
        this.rowData = data;
        this.rowData=this.rowData.subscriptionList;
      //  this.flashMessage.show('Search successfully!!', { cssClass: 'alert-success', timeout: 10000 });
        
        },
      error=>{
        this.spinnerService.hide();
        this.flashMessage.show('No data found!!', { cssClass: 'alert-danger', timeout: 10000 });
      });
    // }
   
  }

  isValid(): boolean {
    if (this.router.url != '/subscriptions/report') {
              return true;
      }
    return false;
  }

  onGridReady(params) {
    this.rowData = [];
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.globalServiceService.SubscriptionCalling().subscribe(
      data => {
        this.rowData = data;
        this.rowData=this.rowData.subscriptionList;
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

