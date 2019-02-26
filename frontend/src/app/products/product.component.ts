import { Component, OnInit } from '@angular/core';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { ModalsService } from '../modal.service';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {


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


  constructor(private router : Router,private modalService: NgbModal,private flashMessage: FlashMessagesService,private childMessageRenderer: ChildMessageRenderer,private globalServiceService: GlobalServiceService) {
    this.columnDefs = [
      { headerName: 'Name', field: 'name',editable:true },
      { headerName: 'Code', field: 'code',editable:true  },
      { headerName: 'Description', field: 'description',editable:true  },
      { headerName: 'SKU', field: 'sku' ,editable:true },
      { headerName: 'Start Date', field: 'startdate',editable:true  },
      { headerName: 'End Date', field: 'enddate',editable:true  },
      { headerName: 'Start Date', field: 'startdate',editable:true  },
      //{ headerName: 'Status', cellRenderer: "childMessageRenderer", colId: "params",editable:true  },
      { headerName: 'Status', field:'status',editable:true  },
      { headerName: 'Plans', field: 'plans',editable:true  },
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

  isValid(): boolean {
    if (this.router.url != '/product/import') {
              return true;
      }
    return false;
  }

  
  addProductData(name,description,sku,startDate,endDate){
    this.globalServiceService.addProduct(name,description,sku,startDate,endDate).subscribe(
      data => {
      console.log(data);
      this.flashMessage.show('New Product added successfully!!', { cssClass: 'alert-success', timeout: 2000 });
      },
    error=>{
      this.flashMessage.show('Product not added !!', { cssClass: 'alert-danger', timeout: 2000 });
    });
  }
}
