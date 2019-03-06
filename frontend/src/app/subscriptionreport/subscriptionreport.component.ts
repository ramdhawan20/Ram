import { Component, OnInit } from '@angular/core';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { ModalsService } from '../modal.service';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'app-subscriptionreport',
  templateUrl: './subscriptionreport.component.html',
  styleUrls: ['./subscriptionreport.component.css']
})
export class SubscriptionreportComponent implements OnInit {

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

  constructor(private router : Router,private modalService: NgbModal,private flashMessage: FlashMessagesService,private globalServiceService: GlobalServiceService) { 
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

  ngOnInit() {
  }
  onBtExport() {
    // var inputElements= <HTMLInputElement>document.getElementById("#fileName");
    var params = {
      fileName: "Subscriptionreport",
      // fileName: inputElements.value,
    };
    this.gridApi.exportDataAsCsv(params);
  }
  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.globalServiceService.SubscriptionreportCalling().subscribe(
      data => {
        this.rowData = data;
        params.api.paginationGoToPage(1);
      });
    // this.globalServiceService.getUserData().subscribe(
    //   data => {
    //     this.rowData = data;
    //     params.api.paginationGoToPage(1);
    //   });
  }
}
