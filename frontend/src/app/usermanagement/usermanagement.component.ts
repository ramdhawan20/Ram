import { Component, OnInit } from '@angular/core';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';

import { HttpClient } from "@angular/common/http";
import { ChildMessageRenderer } from "../child-message-renderer.component";

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
  constructor(private http: HttpClient, private modalService: ModalsService, private globalServiceService: GlobalServiceService, private childMessageRenderer: ChildMessageRenderer) {
    this.columnDefs = [
      { headerName: 'User Profile', field: 'userProfile' },
      { headerName: 'First Name', field: 'userFirstName' },
      { headerName: 'Middle Name', field: 'userMiddleName' },
      { headerName: 'Last Name', field: 'userLastName' },
      { headerName: 'User Id', field: 'userId' },
      { headerName: 'Is Locked', field: 'isLocked' },
      { headerName: 'Status', cellRenderer: "childMessageRenderer", colId: "params" }
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

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.globalServiceService.usermanagementCalling().subscribe(
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
}

