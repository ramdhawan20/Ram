import { Component, OnInit } from '@angular/core';
import { GlobalServiceService } from '../global-service.service';
import { FlashMessagesService } from 'angular2-flash-messages';
import { ChildMessageRenderer } from "../child-message-renderer.component";
import { ModalsService } from '../modal.service';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NgbDatepickerConfig, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { NgbDateFRParserFormatter } from "../ngb-date-fr-parser-formatter";
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
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
  producttype;
  name;
  description;
  sku;
  startDate;
  endDate;
  DrodownArray;
  code;
  P_code_Type;


  constructor(private spinnerService: Ng4LoadingSpinnerService,private router : Router,private modalService: NgbModal,private flashMessage: FlashMessagesService,private childMessageRenderer: ChildMessageRenderer,private globalServiceService: GlobalServiceService) {
    this.columnDefs = [
      { headerName: 'Name', field: 'productDispName',editable:true },
      { headerName: 'Code', field: 'productTypeCode',editable:true  },
      { headerName: 'Description', field: 'productDescription',editable:true  },
      { headerName: 'SKU', field: 'sku' ,editable:true },
      { headerName: 'Start Date', field: 'productStartDate',editable:true  },
      { headerName: 'End Date', field: 'productExpDate',editable:true  },
      // { headerName: 'Start Date', field: 'startdate',editable:true  },
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
    this.globalServiceService.fetchdropdownvalues().subscribe(
      data => {
        this.DrodownArray=data;
        console.log(this.DrodownArray);
      });
    
  }
  dropDown(producttype){
    console.log(producttype);
    for(let i=0;i<this.DrodownArray.length;i++){
      if(this.DrodownArray[i].productType==producttype){
        this.P_code_Type=this.DrodownArray[i].productTypeCode;
      }
    }
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

    let sDate=startDate.day+'/'+startDate.month+'/'+startDate.year;
    let eDate=endDate.day+'/'+endDate.month+'/'+endDate.year;
    this.spinnerService.show();
    this.globalServiceService.addProduct(name,description,sku,sDate,eDate,this.P_code_Type).subscribe(
      data => {
      console.log(data);
      this.rowData=[];
      
      this.globalServiceService.usermanagementCalling().subscribe(
        data => {
          this.spinnerService.hide();
          this.rowData = data;  
          this.producttype="";
          this.name="";
          this.description="";
          this.sku="";
          this.startDate="";
          this.endDate="";
          
        });

      this.flashMessage.show('New Product added successfully!!', { cssClass: 'alert-success', timeout: 10000 });
      },
    error=>{
      this.spinnerService.hide();
      if(error.error.errorCode==1062){       
        let msg=error.error.message;
        this.flashMessage.show(msg, { cssClass: 'alert-danger', timeout: 10000 });
      }else{
        this.flashMessage.show('Product not added !!', { cssClass: 'alert-danger', timeout: 10000 });
      }    
      
    });
   }

}
