import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';

import { HttpClient } from "@angular/common/http";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  private gridApi;
  private gridColumnApi;
  private columnDefs;
  private rowSelection;
  private rowGroupPanelShow;
  private pivotPanelShow;
  private paginationPageSize;
  private paginationNumberFormatter;
  private rowData;

  constructor(private http: HttpClient, private modalService: ModalsService, private globalServiceService: GlobalServiceService) {

    this.columnDefs = [
      { headerName: 'Make', field: 'make'},
      { headerName: 'Model', field: 'model'},
      { headerName: 'Price', field: 'price', editable: true }
    ];

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

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.http
      .get("https://api.myjson.com/bins/15psn9")
      .subscribe(data => {
        this.rowData = data;
        params.api.paginationGoToPage(4);
      });
  }
}

