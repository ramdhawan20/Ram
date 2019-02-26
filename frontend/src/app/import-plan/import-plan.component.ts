import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ModalsService } from '../modal.service';
import { RequestOptions } from '@angular/http';
import { FlashMessagesService } from 'angular2-flash-messages';
import { GlobalServiceService } from '../global-service.service';
import { ErrorDownloadComponent } from "../error-download.component";
import {FileDownloadComponent} from '../file-download.component';

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

  filename: string;
  
  flag: boolean = false;
  inputvalue: boolean = false;
  file;
  selectedfile;
  

  ngOnInit() {

    document.getElementById("exportImportBox").style.display = "block";
    document.getElementById("productGrid").style.display = "none";
    //(document.getElementById("downloadFile") as HTMLInputElement).disabled = true;



  }
  showFlash() {
    this.flashMessage.show('Choose a file', { cssClass: 'alert-danger', timeout: 2000 });
  }

  constructor(private globalServiceService: GlobalServiceService, private http: HttpClient, private modalService: ModalsService, private flashMessage: FlashMessagesService) {

    this.columnDefs = [
      { headerName: 'Sl.no', field: 'slno' },
      { headerName: 'Date Added', field: 'dateAdded' },
      { headerName: 'File Name', field: 'uploadFileName',cellRenderer:"FileDownloadComponent" ,colId: "params" },
      { headerName: 'No.of Records', field: 'noOfRecords' },
      { headerName: 'Updated Records', field: 'noOfSuccessRecords' },
      { headerName: 'Status', field: 'status' },
      { headerName: 'Error', field: 'errorLogFileName',cellRenderer:"ErrorDownloadComponent" ,colId: "params"},

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

  onQuickFilterChanged() {ErrorDownloadComponent
    var inputElement = <HTMLInputElement>document.getElementById("quickFilter");
    this.gridApi.setQuickFilter(inputElement.value);
  }

 
  // export to Csv code start
  onBtExport() {
    // var inputElements= <HTMLInputElement>document.getElementById("#fileName");
    var params = {

      // fileName: inputElements.value,
    };
    this.gridApi.exportDataAsCsv(params);
  }
  // export to Csv code end



  onGridReady(params) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;

    this.globalServiceService.jsonCalling().subscribe(
      data => {
        this.rowData = data;
      });
  }


  //Upload function
  inputfilename(x) {
    this.inputvalue = true;
    console.log(x);
    let fileList: FileList = x.target.files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      this.file = file;
      this.filename = file.name;
      let filenamearr = this.filename.split(".");
      console.log(filenamearr);
      let index = filenamearr.length;
      if (filenamearr[index - 1] == "csv") {
        this.flashMessage.show('Proceed with the upload button!!', { cssClass: 'alert-success', timeout: 2000 });
        this.flag = true;
        console.log("correct file");
      }
      else {
        this.flashMessage.show('Please select a .csv file !!', { cssClass: 'alert-danger', timeout: 2000 });
        this.flag = false;
        console.log("wrong file");
      }

    }

  }
  fileChange() {
console.log(this.selectedfile);

    if (this.inputvalue == true) {
      if (this.flag == true) {
        let formData: FormData = new FormData();
        formData.append('uploadFile', this.file, this.filename);
        let headers = new Headers();
        headers.append('Content-Type', 'multipart/form-data');
        headers.append('Accept', 'application/json');
        //let options = new RequestOptions({ headers: headers });
        document.getElementById("productGrid").style.display = "block";
        this.globalServiceService.uploadExpData(formData).subscribe(

          result => {

          },
          err => {

          });

      }
      this.selectedfile="";
    }
    else {
      this.showFlash();
    }

  }



}
