import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ModalsService } from '../modal.service';
import { RequestOptions, Headers } from '@angular/http';
import { FlashMessagesService } from 'angular2-flash-messages';
import { GlobalServiceService } from '../global-service.service';
import { ErrorDownloadComponent } from "../error-download.component";
import { FileDownloadComponent } from '../file-download.component';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
@Component({
  selector: 'app-import-plan',
  templateUrl: './import-plan.component.html',
  styleUrls: ['./import-plan.component.css']
})
export class ImportPlanComponent implements OnInit {
  private gridApi;
  private gridColumnApi;
  private columnDefs;
  private rowData;
  private context;
  private frameworkComponents;
  private defaultColDef;
  private rowSelection;
  fileToUpload;
  fileToUpload1;
  filename;
  flag: boolean = false;
  inputvalue: boolean = false;
  closeResult;
  file;
  valarr = [];
  f_Name;
  selectedfile;
  
 
  
 
  ngOnInit() {

    document.getElementById("exportImportBox").style.display = "block";
    document.getElementById("productGrid").style.display = "none";
  }
  showFlash() {
    this.flashMessage.show('Choose a file', { cssClass: 'alert-danger', timeout: 10000 });
  }

  constructor( private spinnerService: Ng4LoadingSpinnerService,private router: Router,private modalService: NgbModal, private globalServiceService: GlobalServiceService, private http: HttpClient, private flashMessage: FlashMessagesService) {

    this.columnDefs = [
      { headerName: 'Date Added', field: 'dateAdded' },
      { headerName: 'File Name', field: 'uploadFileName', cellRenderer: "FileDownloadComponent", colId: "params" },
      { headerName: 'No.of Records', field: 'noOfRecords' },
      { headerName: 'Updated Records', field: 'noOfSuccessRecords' },
      { headerName: 'Status', field: 'status' },
      { headerName: 'Error', field: 'errorLogFileName', cellRenderer: "ErrorDownloadComponent", colId: "params" },
    ];

    this.context = { componentParent: this };
    this.frameworkComponents = {
      ErrorDownloadComponent: ErrorDownloadComponent,
      FileDownloadComponent: FileDownloadComponent,
    };

    this.defaultColDef = {
      resizable: true,
      width: 110
    };
    this.rowSelection = "multiple";
  }

  onQuickFilterChanged() {
    ErrorDownloadComponent
    var inputElement = <HTMLInputElement>document.getElementById("quickFilter");
    this.gridApi.setQuickFilter(inputElement.value);
  }

  exportImport() {
    document.getElementById("exportImportBox").style.display = "block";
  }

  // export to Csv code start
  onBtExport() {
    var params = {
    };
    this.gridApi.exportDataAsCsv(params);
  }
  // export to Csv code end

  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;

    //this.globalServiceService.jsonCalling().subscribe(
    //data => {
    //this.rowData = data;
    //});
  }

  //Upload function
  inputfilename(x) {
    this.inputvalue = true;

    let fileList: FileList = x.target.files;
    this.f_Name = fileList;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      this.file = file;
      this.filename = file.name;
      let filenamearr = this.filename.split(".");

      let index = filenamearr.length;
      if (filenamearr[index - 1] == "csv") {
        this.flashMessage.show('Proceed with the upload button!!', { cssClass: 'alert-success', timeout: 10000 });
        this.flag = true;
        (document.getElementById("uploadbtn") as HTMLInputElement).disabled = false;
        // console.log("correct file");
      }
      else {
        this.flashMessage.show('Please select a .csv file !!', { cssClass: 'alert-danger', timeout: 10000 });
        this.flag = false;
        (document.getElementById("uploadbtn") as HTMLInputElement).disabled = true;
        //   console.log("wrong file");
      }

    }

  }
  reload(){
    //window.location.reload();
    this.router.navigate(['/product/import']);
  }
  fileChange() {

    if (this.inputvalue == true) {
      if (this.flag == true) {
       
        let formData: FormData = new FormData();
        formData.append('uploadFile', this.file, this.filename);
        let headers = new Headers();
        headers.append('Content-Type', 'multipart/form-data');
        headers.append('Accept', 'application/json');
        let options = new RequestOptions({ headers: headers });
        document.getElementById("productGrid").style.display = "block";
        this.globalServiceService.uploadExpData(formData).subscribe(

          result => {

          },
          err => {

          });

      }
    }
    else {
      this.showFlash();
    }

  }



  // postMethod(files: FileList) {
  //   this.fileToUpload = files.item(0); 
  //   console.log(this.fileToUpload);
  //   let formData = new FormData(); 
  //   formData.append('file', this.fileToUpload, this.fileToUpload.name); 
  //   this.http.post(this.globalServiceService.url+ '/uploadProductData', formData).subscribe((val) => {
  //   //  console.log(val);
  //   this.valarr.push(val);
  // //  console.log(this.valarr);

  //   this.rowData = this.valarr;
  //   document.getElementById("productGrid").style.display = "block";
  //   });
  //   return false; 
  //   }

  postMethod1() {
    if (this.inputvalue == true) {
    if (this.flag == true) {
    this.fileToUpload1 = this.f_Name.item(0);
    console.log(this.fileToUpload1);
    let formData = new FormData();
    formData.append('file', this.fileToUpload1, this.fileToUpload1.name);
    this.spinnerService.show();
    
    this.http.post(this.globalServiceService.url + '/uploadProductData', formData).subscribe((val) => { 
      this.rowData=[];    
      this.spinnerService.hide();
      this.valarr=[];
      this.valarr.push(val);      
      this.rowData = this.valarr;
      document.getElementById("productGrid").style.display = "block";
      this.flashMessage.show('File uploaded successfully!', { cssClass: 'alert-success', timeout: 10000 });
      this.fileToUpload1 = "";
      this.selectedfile = "";
      this.inputvalue=false;
      
      
    },
    error=>{
      this.spinnerService.hide();
      this.selectedfile = "";  
      this.inputvalue=false;
      if(error.error.errorCode==412){       
        let msg=error.error.message;
        this.flashMessage.show(msg, { cssClass: 'alert-danger', timeout: 10000 });
      } 
      else if(error.error.status==500){
        let msg=error.error.error;
        this.flashMessage.show(msg, { cssClass: 'alert-danger', timeout: 10000 });
      } else  if(error.error.errorCode==1062){       
        let msg=error.error.message;
        this.flashMessage.show(msg, { cssClass: 'alert-danger', timeout: 10000 });
      }     
    });
  }
}
else{
  this.fileToUpload1 = "";  
  this.flashMessage.show('Choose csv file', { cssClass: 'alert-danger', timeout: 10000 });
  
}
  
    return false;
    
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

  downloadSample(){
    let url = this.globalServiceService.url + '/downloadFile/UPLOAD.csv';
    window.location.href = url;

  }
}
