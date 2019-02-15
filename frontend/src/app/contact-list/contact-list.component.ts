import { Component, OnInit } from '@angular/core';
import { ModalsService } from '../modal.service';
import { GlobalServiceService } from '../global-service.service';

import { HttpClient } from "@angular/common/http";
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

  constructor(private http: HttpClient, private modalService: ModalsService, private globalServiceService: GlobalServiceService) {

    this.columnDefs = [
      { headerName: 'Make', field: 'make' },
      { headerName: 'Model', field: 'model' },
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

  // public data: Object[];
  // ngOnInit() {

  //     this.data = [
  //         { Num: 200, OrderID: 10248, CustomerID: 'VINET', Freight: 32.38, ShipCountry: 'France' },
  //         { Num: 99, OrderID: 10249, CustomerID: 'TOMSP', Freight: 11.61, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10250, CustomerID: 'HANAR', Freight: 65.83, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10251, CustomerID: 'VICTE', Freight: 41.34, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10252, CustomerID: 'SUPRD', Freight: 51.3, ShipCountry: 'Belgium' },
  //         { Num: 1, OrderID: 10253, CustomerID: 'HANAR', Freight: 58.17, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10254, CustomerID: 'CHOPS', Freight: 22.98, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10255, CustomerID: 'RICSU', Freight: 148.33, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10256, CustomerID: 'SUPRD', Freight: 13.97, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10257, CustomerID: 'WELLI', Freight: 14.23, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10258, CustomerID: 'VICTE', Freight: 18.33, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10259, CustomerID: 'WELLI', Freight: 28.13, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10260, CustomerID: 'CHOPS', Freight: 48.34, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10261, CustomerID: 'SUPRD', Freight: 32.73, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10262, CustomerID: 'TOMSP', Freight: 12.31, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10263, CustomerID: 'VICTE', Freight: 23.77, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10264, CustomerID: 'SUPRD', Freight: 43.47, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10265, CustomerID: 'CHOPS', Freight: 53.37, ShipCountry: 'Belgium' },

  //         { Num: 1, OrderID: 102481, CustomerID: 'VINET', Freight: 32.38, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 102493, CustomerID: 'TOMSP', Freight: 11.61, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 102504, CustomerID: 'HANAR', Freight: 65.83, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10251, CustomerID: 'VICTE', Freight: 41.34, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10248, CustomerID: 'VINET', Freight: 32.38, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10249, CustomerID: 'TOMSP', Freight: 11.61, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10250, CustomerID: 'HANAR', Freight: 65.83, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10251, CustomerID: 'VICTE', Freight: 41.34, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10252, CustomerID: 'SUPRD', Freight: 51.3, ShipCountry: 'Belgium' },
  //         { Num: 1, OrderID: 10253, CustomerID: 'HANAR', Freight: 58.17, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10254, CustomerID: 'CHOPS', Freight: 22.98, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10255, CustomerID: 'RICSU', Freight: 148.33, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10256, CustomerID: 'SUPRD', Freight: 13.97, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10257, CustomerID: 'WELLI', Freight: 14.23, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10258, CustomerID: 'VICTE', Freight: 18.33, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10259, CustomerID: 'WELLI', Freight: 28.13, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10260, CustomerID: 'CHOPS', Freight: 48.34, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10261, CustomerID: 'SUPRD', Freight: 32.73, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10262, CustomerID: 'TOMSP', Freight: 12.31, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10263, CustomerID: 'VICTE', Freight: 23.77, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10264, CustomerID: 'SUPRD', Freight: 43.47, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10265, CustomerID: 'CHOPS', Freight: 53.37, ShipCountry: 'Belgium' },

  //         { Num: 1, OrderID: 10248, CustomerID: 'VINET', Freight: 32.38, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10249, CustomerID: 'TOMSP', Freight: 11.61, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10250, CustomerID: 'HANAR', Freight: 65.83, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10251, CustomerID: 'VICTE', Freight: 41.34, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10252, CustomerID: 'SUPRD', Freight: 51.3, ShipCountry: 'Belgium' },
  //         { Num: 1, OrderID: 10253, CustomerID: 'HANAR', Freight: 58.17, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10254, CustomerID: 'CHOPS', Freight: 22.98, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10255, CustomerID: 'RICSU', Freight: 148.33, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10256, CustomerID: 'SUPRD', Freight: 13.97, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10257, CustomerID: 'WELLI', Freight: 14.23, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10258, CustomerID: 'VICTE', Freight: 18.33, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10259, CustomerID: 'WELLI', Freight: 28.13, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10260, CustomerID: 'CHOPS', Freight: 48.34, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10261, CustomerID: 'SUPRD', Freight: 32.73, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10262, CustomerID: 'TOMSP', Freight: 12.31, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10263, CustomerID: 'VICTE', Freight: 23.77, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10264, CustomerID: 'SUPRD', Freight: 43.47, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10265, CustomerID: 'CHOPS', Freight: 53.37, ShipCountry: 'Belgium' },


  //         { Num: 1, OrderID: 10248, CustomerID: 'VINET', Freight: 32.38, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10249, CustomerID: 'TOMSP', Freight: 11.61, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10250, CustomerID: 'HANAR', Freight: 65.83, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10251, CustomerID: 'VICTE', Freight: 41.34, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10252, CustomerID: 'SUPRD', Freight: 51.3, ShipCountry: 'Belgium' },
  //         { Num: 1, OrderID: 10253, CustomerID: 'HANAR', Freight: 58.17, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10254, CustomerID: 'CHOPS', Freight: 22.98, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10255, CustomerID: 'RICSU', Freight: 148.33, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10256, CustomerID: 'SUPRD', Freight: 13.97, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10257, CustomerID: 'WELLI', Freight: 14.23, ShipCountry: 'Venezuela' },
  //         { Num: 1, OrderID: 10258, CustomerID: 'VICTE', Freight: 18.33, ShipCountry: 'France' },
  //         { Num: 1, OrderID: 10259, CustomerID: 'WELLI', Freight: 28.13, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10260, CustomerID: 'CHOPS', Freight: 48.34, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10261, CustomerID: 'SUPRD', Freight: 32.73, ShipCountry: ' Germany' },
  //         { Num: 1, OrderID: 10262, CustomerID: 'TOMSP', Freight: 12.31, ShipCountry: 'Switzerland' },
  //         { Num: 1, OrderID: 10263, CustomerID: 'VICTE', Freight: 23.77, ShipCountry: 'Brazil' },
  //         { Num: 1, OrderID: 10264, CustomerID: 'SUPRD', Freight: 43.47, ShipCountry: 'Venezuela' },
  //         { Num: 100, OrderID: 10265, CustomerID: 'CHOPS', Freight: 53.37, ShipCountry: 'Belgium' },

          
  //     ];
      
  // }
} 


  
//   constructor() { }

//   ngOnInit() {
//   }

// }
