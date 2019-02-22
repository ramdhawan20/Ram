import { Component, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ModalsService } from '../modal.service';
import { RequestOptions } from '@angular/http';
import { FlashMessagesService } from 'angular2-flash-messages';
import { GlobalServiceService } from '../global-service.service';
import { ErrorDownloadComponent } from "../error-download.component";
import {FileDownloadComponent} from '../file-download.component';
@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
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
  

  ngOnInit() {

    document.getElementById("exportImportBox").style.display = "none";
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

  exportImport() {
    document.getElementById("exportImportBox").style.display = "block";
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
    }
    else {
      this.showFlash();
    }

  }





  // Download Functionality



  //   downloadToCsv(selectedrolepage, Username, firstName, toggleDropdown) {
  //     this.allApicallingService.searchUseruser_accountDownload(this.newdropdownID, Username, firstName, this.statusvalue).subscribe(
  //         result => {
  //             document.getElementById("errofactive").style.display = "none";
  //             this.spinnerService.hide();
  //             this.items = JSON.parse(JSON.stringify(result.userDetailsResponseDTOList).replace(/\s(?=\w+":)/g, ""));
  //             this.data = this.items;

  //             let excelData = Object.keys(this.items)
  //                 .map(i => {
  //                     var obj = {};
  //                     obj["uidPk"] = this.items[i].uidPk;
  //                     obj["userId"] = this.items[i].userId;
  //                     obj["firstName"] = this.items[i].firstName;
  //                     obj["middleName"] = this.items[i].middleName;
  //                     obj["lastName"] = this.items[i].lastName;
  //                     obj["email"] = this.items[i].email;
  //                     obj["roleId"] = this.items[i].roleId;
  //                     obj["roleName"] = this.items[i].roleName;
  //                     obj["expiryDate"] = this.items[i].expiryDate;
  //                     obj["active"] = this.items[i].active;
  //                     obj["role"] = this.items[i].role.roleName;
  //                     return obj;
  //                 });

  //             this.data = excelData;
  //             this.download();
  //         },
  //         err => {
  //             this.spinnerService.hide();
  //             document.getElementById("errofactive").style.display = "block";
  //             setTimeout(this.myFunctionreset, 3000);
  //         });
  // }
  // download() {
  //     var csvData = this.ConvertToCSV(this.data);
  //     var a = document.createElement("a");
  //     a.setAttribute('style', 'display:none;');
  //     document.body.appendChild(a);
  //     var blob = new Blob([csvData], {
  //         type: 'text/csv'
  //     });
  //     var url = window.URL.createObjectURL(blob);
  //     a.href = url;
  //     a.download = 'Useraccount.csv';
  //     a.click();
  // }
  // ConvertToCSV(objArray) {
  //     var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
  //     var str = '';
  //     var row = "";

  //     for (var index in objArray[0]) {

  //         row += index + ',';
  //     }
  //     row = row.slice(0, -1);

  //     str += row + '\r\n';

  //     for (var i = 0; i < array.length; i++) {
  //         var line = '';
  //         for (var index in array[i]) {
  //             if (line != '') line += ','

  //             line += array[i][index];
  //         }
  //         str += line + '\r\n';
  //     }
  //     return str;
  // }

}